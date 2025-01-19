package com.jvb.demo.controller;


import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.jvb.demo.dto.AccountDTO;
import com.jvb.demo.service.AccountService;

@Controller
public class AccountController {
	@Autowired
	private AccountService accService;
 
	@GetMapping("/login")
	public String getLoginPage(@ModelAttribute("success")String success,@ModelAttribute("error")String error, Model model, Authentication auth) {
		if(auth == null || auth instanceof AnonymousAuthenticationToken){
			model.addAttribute("success", success);
			model.addAttribute("error", error);
			return "sign-in";
		}else 
			return "redirect:./";
	}
	
	@GetMapping("/login-error")
	public String loginError(Model model) {
		model.addAttribute("error", "Email/Mật khẩu không chính xác");
		return "sign-in";
	}
	
	@GetMapping("/login-expired")
	public String loginExpired(Model model){
		model.addAttribute("error", "Phiên đăng nhập đã kết thúc, vui lòng đăng nhập lại");
		return "sign-in";
	}

	@GetMapping("/sign-up")
	public String getSignUpPage(AccountDTO dto) {
		return "sign-up";
	}
	
	@PostMapping("/sign-up")
	public String doSignUp(AccountDTO dto, Model model, RedirectAttributes redirect) {
		try {
			if(accService.checkEmailExist(dto.getEmail())) {
				model.addAttribute("emailError", "Email đã được đăng ký");
				model.addAttribute("accountDTO", dto);
				return "sign-up";
			}else if(accService.checkPhoneExist(dto.getPhone())) {
				model.addAttribute("phoneError", "Số điện thoại đã được đăng ký");
				model.addAttribute("accountDTO", dto);
				return "sign-up";
			}
			else {
				accService.saveAccount(dto);
				redirect.addFlashAttribute("success", "Đăng ký tài khoản thành công, vui lòng đăng nhập");
			}	
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "Đăng ký tài khoản thất bại, đã có lỗi xảy ra");
			return "sign-up";
		}
		return "redirect:./login";
	}

	@GetMapping("/account")
	public String getAccountDetail(Principal principal, Model model){
		AccountDTO accountDTO = accService.findByEmail(principal.getName());
		model.addAttribute("accountDTO", accountDTO);
		return "account-detail";
	}

	@PostMapping("/account/update/password")
	public RedirectView changePassword(@RequestParam("password")String password, Principal principal, RedirectAttributes redirect){
		String  message;
		try {
			accService.changePassword(principal.getName(), password);
			message = "Đổi mật khẩu thành công";
		} catch (Exception e) {
			message = "Đổi mật khẩu thất bại, đã xảy ra lỗi";
		}
		redirect.addAttribute("message", message);
		return new RedirectView("/account");
	}

	@PostMapping("/account/update/profile")
	public RedirectView updateAccountProfile(AccountDTO accountDTO, Principal principal, RedirectAttributes redirect){
		String message;
		try {
			accService.updateAccount(accountDTO);
			message = "Cập nhật thông tin thành công";
		} catch (Exception e) {
			message = "Cập nhật thông tin thất bại, đã xảy ra lỗi";
		}
		redirect.addAttribute("message", message);
		return new RedirectView("/account");
	}
	
	@GetMapping("/account/all")
	public String getAllAccount(@RequestParam(name = "keyword", required = false)String searchKeyword,
			@RequestParam(name = "page", required = false, defaultValue = "1")Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "12")Integer itemOnPage,
			Model model){
		Pageable pageable = PageRequest.of(page-1, itemOnPage);
		Page<AccountDTO> accountDTOs = accService.getAll(searchKeyword, pageable);
		model.addAttribute("accountDTOs", accountDTOs);
		return "account-list-admin";
	}

	@PostMapping("/account/disable")
	public RedirectView disableAccount(@RequestParam("email")String email, RedirectAttributes redirect){
		String message = "success";
		try {
			accService.enableAccount(email, false);
		} catch (Exception e) {
			e.printStackTrace();
			message = "failed";
		}
		redirect.addAttribute("message", message);
		return new RedirectView("/account/all");
	}

	@PostMapping("/account/enable")
	public RedirectView enableAccount(@RequestParam("email")String email, RedirectAttributes redirect){
		String message = "success";
		try {
			accService.enableAccount(email, true);
		} catch (Exception e) {
			e.printStackTrace();
			message = "failed";
		}
		redirect.addAttribute("message", message);
		return new RedirectView("/account/all");
	}
}
