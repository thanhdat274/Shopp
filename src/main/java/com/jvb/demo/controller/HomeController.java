package com.jvb.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jvb.demo.dto.ProductDTO;
import com.jvb.demo.enums.OrderStatus;
import com.jvb.demo.service.OrderService;
import com.jvb.demo.service.ProductService;

@Controller
@RequestMapping("/")
public class HomeController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private ProductService productService;

	@GetMapping
	public String getHomePage(Model model) {
		try {
			Pageable pageable = PageRequest.of(0, 4);
			Page<ProductDTO> bestSellingProduct = productService.getAllBestSellingProduct(null, null, null, 0f, 999999999f, pageable);

			Sort sort = Sort.by("create_date").descending();
			pageable = PageRequest.of(0, 4, sort);
			Page<ProductDTO> newProduct = productService.searchProduct(null, null, null, 0f, 999999999f, pageable);

			model.addAttribute("newProduct", newProduct);
			model.addAttribute("bestSellingProduct", bestSellingProduct);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "home";
	}

	@GetMapping("/admin")
	public String getAdminHomePage(@RequestParam(name = "findBy", required = false, defaultValue = "month")String findBy, Model model){
		try {
			System.out.println(orderService.getTodayRefund());
			model.addAttribute("bestSellingProduct", productService.getTop5SellingProductRevenua());
			model.addAttribute("processingOrder", orderService.countByOrderStatus(OrderStatus.PROCESSING));
			model.addAttribute("totalProduct", productService.searchProduct(null, null, null, 0, 9999999999f, null).getTotalElements());
			model.addAttribute("todayRevenua", orderService.getTodayRevenua());
			model.addAttribute("todayRefund", orderService.getTodayRefund());
			switch(findBy){
				case "month":
					List<Object[]> revenuaByMonth = orderService.getRevenuaByMonth();
					Map<String, Map<String,Integer>> orderStatusByMonth = orderService.countOrderStatusByMonth();
					model.addAttribute("orderStatusByMonth", orderStatusByMonth);
					model.addAttribute("revenuaByMonth", revenuaByMonth);
				case "dayofweek":
					List<Object[]> revenuaByDayOfWeek = orderService.getRevenuaByDayOfWeek();
					Map<String, Map<String,Integer>> orderStatusByDayOkWeek = orderService.countOrderStatusByDayOfWeek();
					model.addAttribute("orderStatusByDayOkWeek", orderStatusByDayOkWeek);
					model.addAttribute("revenuaByDayOfWeek", revenuaByDayOfWeek);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "home-admin";
	}
}
