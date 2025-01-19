package com.jvb.demo.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.jvb.demo.dto.CartDTO;
import com.jvb.demo.dto.OrderDTO;
import com.jvb.demo.entity.Order;
import com.jvb.demo.service.CartService;
import com.jvb.demo.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @GetMapping
    public String getOrderForm(Principal principal, Model model){
        CartDTO cartDTO = cartService.findByEmail(principal.getName());
        if(cartDTO == null || cartDTO.getCartDetailDTOs().isEmpty())
            return "redirect:./cart";
        OrderDTO orderDTO = orderService.createOrderInfor(principal.getName());
        model.addAttribute("orderDTO", orderDTO);
        model.addAttribute("cartDTO", cartDTO);
        return "order-create";
    }

    @PostMapping("/create")
    public RedirectView createOrder(@ModelAttribute OrderDTO orderDTO, Principal principal, HttpServletRequest request,
        @RequestParam(name = "payment_method")String payment_method, RedirectAttributes redirect){
        OrderDTO result = orderService.saveOrder(orderDTO, principal.getName());
        Long order_id = result.getId();
        Float amount = result.getTotal_price();
        redirect.addAttribute("payment_method", payment_method);
        redirect.addAttribute("order_id", order_id);
        redirect.addAttribute("amount", amount);
        return new RedirectView("/payment");
    }

    @GetMapping("/all")
    public String getAllOrders(@RequestParam(name = "status", required = false)String status, Model model, Principal principal,
            @RequestParam(name = "page", required = false, defaultValue = "1")Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "6")Integer size){
        String email = principal.getName();
        if(status != null && status.equals("ALL")) status = null; // null = find all
        Pageable pageable = PageRequest.of(page-1, size);
        Page<OrderDTO> orderDTOs = orderService.findByEmail(email, status, pageable);
        model.addAttribute("orderDTOs", orderDTOs);
        return "order-list";
    }

    @GetMapping("/{id}")
    public String getOrderDetail(@PathVariable("id")Long order_id, Model model){
        OrderDTO orderDTO = new OrderDTO(orderService.findById(order_id));
        model.addAttribute("orderDTO", orderDTO);
        return "order-detail";
    }

    @PostMapping("/cancel")
    public String cancelOrder(@RequestParam("id")Long orderID, RedirectAttributes redirect){
        try {
            orderService.cancelOrder(orderID);
            redirect.addFlashAttribute("success", "Hủy đơn hàng thành công");
        } catch (Exception e) {
            e.printStackTrace();
            redirect.addFlashAttribute("error", "Đã xảy ra lỗi");
        }
        return "redirect:./"+orderID;
    }

    @GetMapping("/all/admin")
    public String getAllOrdersAdmin(@RequestParam(name = "status", required = false, defaultValue = "PROCESSING")String status,
            @RequestParam(name = "from_date", required = false)String fromDate,
            @RequestParam(name = "to_date", required = false)String toDate,
            @RequestParam(name = "page", required = false, defaultValue = "1")Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "6")Integer itemOnPage, Model model ){
        if(status.equals("ALL")) status = null; //null = find all
        Pageable pageable = PageRequest.of(page-1, itemOnPage);
        Page<OrderDTO> orderDTOs = orderService.findAll(status, fromDate, toDate, pageable);
        model.addAttribute("orderDTOs", orderDTOs);
        return "order-list-admin";
    }

    @GetMapping("/admin/{id}")
    public String getOrderDetailAdmin(@PathVariable("id")Long orderID, Model model, RedirectAttributes redirectAttributes){
        Order order = orderService.findById(orderID);
        if(order == null){
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng ID: "+orderID);
            return "redirect:../all/admin";
        }
        else{
            model.addAttribute("orderDTO", new OrderDTO(order));
            model.addAttribute("email", order.getAccount().getEmail());
            model.addAttribute("emailStatus", order.getAccount().getIsEnabled());
        }
        return "order-detail-admin";
    }

    @PostMapping("/admin/accept")
    public String acceptOrder(@RequestParam("id")Long orderID, RedirectAttributes redirectAttributes){
        try {
            Order order = orderService.findById(orderID);
            if(order == null) 
                redirectAttributes.addFlashAttribute("error", "Đơn hàng không tồn tại");
            else if(order.getIsPaid() != 1 || order.getPayment() == null)
                redirectAttributes.addFlashAttribute("error", "Đơn hàng chưa được thanh toán");
            else if(!orderService.checkOrderQuantity(order))
                redirectAttributes.addFlashAttribute("error", "Sản phẩm không đủ số lượng để đặt hàng");
            else{
                orderService.acceptOrder(orderID);
                redirectAttributes.addFlashAttribute("success", "Trạng thái đơn hàng cập nhật thành công");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi");
        }
        return "redirect:../admin/"+orderID;
    }

    @PostMapping("/admin/update/status")
    public String updateOrderStatus(@RequestParam("id") Long orderID, @RequestParam("order_status")String orderStatus,
            RedirectAttributes redirectAttributes){
        try {
            OrderDTO orderDTO = orderService.updateOrderStatus(orderID, orderStatus);
            if(orderDTO == null)
                throw new Exception("Trạng thái đơn hàng không hợp lệ");
            else
                redirectAttributes.addFlashAttribute("success", "Trạng thái đơn hàng cập nhật thành công");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error","Cập nhật thất bại, đã có lỗi xảy ra");
        }
        return "redirect:../"+orderID;
    }

    @PostMapping("/admin/reject")
    public String rejectOrder(@RequestParam("id") Long orderID, RedirectAttributes redirectAttributes) {
        try {
            Order order = orderService.findById(orderID);
            String orderStatus = order.getOrderStatus().name();
            if (!orderStatus.equalsIgnoreCase("CONFIRMED") && !orderStatus.equalsIgnoreCase("SHIPPING")
                    && !orderStatus.equalsIgnoreCase("PROCESSING")) {
                redirectAttributes.addFlashAttribute("error", "Đơn hàng không thể hủy ở trạng thái hiện tại");
            } else {
                OrderDTO orderDTO = orderService.rejectOrder(orderID);
                if(orderDTO == null) 
                    throw new Exception();
                redirectAttributes.addFlashAttribute("success", "Hủy đơn hàng thành công");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Hủy đơn hàng thất bại, đã xảy ra lỗi");
        }
        return "redirect:./" + orderID;
    }
}
