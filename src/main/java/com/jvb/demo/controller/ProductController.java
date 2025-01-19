package com.jvb.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jvb.demo.dto.ProductDTO;
import com.jvb.demo.service.ImageService;
import com.jvb.demo.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ImageService imageService;

    @GetMapping(value = {"","/admin"})
    public String getListProduct(@RequestParam(name = "keyword", required = false)String search_keyword,
            @RequestParam(name = "type", required = false)List<String> types,
            @RequestParam(name = "brand", required = false)List<String> brands,
            @RequestParam(name = "price_range",  required = false)String price_range,
            @RequestParam(name = "order_by", required = false, defaultValue = " ")String order_by,
            @RequestParam(name = "page", required = false, defaultValue = "1")Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "12")Integer size, Model model, 
            Authentication authentication, HttpServletRequest request) throws Exception{
        float min_price = 0;
        float max_price= 999999999;
        if(price_range != null){
            try {
                min_price = Float.parseFloat(price_range.split("-")[0]);
                max_price = Float.parseFloat(price_range.split("-")[1]);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        Sort sort = null;
        switch(order_by){
            case "gia-thap-den-cao": 
                sort= Sort.by("s.price").ascending();
                break;
            case "gia-cao-den-thap":
                sort= Sort.by("s.price").descending();
                break;
            case "san-pham-moi":
                sort = Sort.by("create_date").descending();
                break;
            case "san-pham-cu":
                sort = Sort.by("create_date").ascending();
                break;
            default:
                sort = Sort.unsorted();
                break;
        } 
        Pageable pageable = PageRequest.of(page-1, size, sort);
        Page<ProductDTO> productDTO;
        if(order_by.equals("san-pham-ban-chay"))
           productDTO = productService.getAllBestSellingProduct(search_keyword, brands, types, min_price, max_price, pageable);
        else
            productDTO = productService.searchProduct(search_keyword, brands, types, min_price, max_price, pageable);
        model.addAttribute("listProduct", productDTO);
        model.addAttribute("search_keyword", search_keyword);
        // return view if user logged in and has role admin
        if(request.isUserInRole("ROLE_ADMIN") && request.getRequestURI().equals("/product/admin")){
            return "product-list-admin";
        }
        return "product-list";
    }

    @GetMapping("/{id}")
    public String getProductDetail(@PathVariable("id")long id, Model model) {
        ProductDTO productDTO = productService.findByID(id);
        model.addAttribute("product", productDTO);
        return "product-detail";
    }

    @GetMapping("/create")
    public String createProductForm(ProductDTO productDTO){
        return "product-create";
    }

    @PostMapping("/create")
    public String saveProduct(ProductDTO productDTO, Model model, 
            @RequestParam(name = "thumbnail_file", required = false)MultipartFile thumbnail_file){
        try {
            if(thumbnail_file != null){
                String thumb_url = imageService.saveImage(thumbnail_file);
                productDTO.setThumbnail(thumb_url);
            }else
                productDTO.setThumbnail("images/product/product-d-1.jpg");
            ProductDTO result = productService.saveProduct(productDTO);
            return "redirect:/product/admin/"+result.getId();
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("productDTO", productDTO);
            model.addAttribute("message", "Đã xảy ra lỗi, vui lòng thử lại");
            return "product-create";
        }
    }

    @GetMapping("/admin/{id}")
    public String getProductDetailAdmin(@PathVariable("id") long id, Model model) {
        ProductDTO productDTO = productService.findByID(id);
        model.addAttribute("productDTO", productDTO);
        return "product-detail-admin";
    }

    @PostMapping("/update")
    public String updateProductSku(ProductDTO productDTO, Model model, RedirectAttributes redirect) {
        try {
            productService.updateProduct(productDTO);
            redirect.addAttribute("success", "Cập nhật thông tin thành công");
        } catch (Exception e) {
            e.printStackTrace();
            redirect.addAttribute("error", "Đã xảy ra lỗi, vui lòng thử lại");
        }
        return "redirect:/product/admin/" + productDTO.getId();
    }
}
