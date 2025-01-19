
package com.jvb.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.jvb.demo.dto.AccountDTO;
import com.jvb.demo.entity.Account;

@Service
public interface AccountService extends UserDetailsService{
	Account saveAccount(AccountDTO dto);
	Boolean checkPhoneExist(String phone);
	Boolean checkEmailExist(String email);
	Page<AccountDTO> getAll(String searchKeyword, Pageable pageable);
	Boolean enableAccount(String email, boolean isEnable);
	Boolean logoutUser(String email);
	AccountDTO findByEmail(String email);
	Account updateAccount(AccountDTO accountDTO) throws Exception;
	void changePassword(String email, String password) throws Exception;
}
