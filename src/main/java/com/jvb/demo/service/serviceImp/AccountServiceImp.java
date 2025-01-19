package com.jvb.demo.service.serviceImp;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.DisabledException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jvb.demo.entity.Role;
import com.jvb.demo.dto.AccountDTO;
import com.jvb.demo.entity.Account;
import com.jvb.demo.entity.Cart;
import com.jvb.demo.repository.AccountRepository;
import com.jvb.demo.repository.RoleRepository;
import com.jvb.demo.service.AccountService;

@Service
public class AccountServiceImp implements AccountService {
	@Autowired
	private AccountRepository accountRepo;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private RoleRepository roleRepo;

	@Lazy
	@Autowired
	SessionRegistry sessionRegistry;
	
	@Override
	public Account saveAccount(AccountDTO dto) {
		Account acc= new Account();
		Cart cart = new Cart();
		acc.setPassword(encoder.encode(dto.getPassword()));
		acc.setRoles(Arrays.asList(roleRepo.findByName("ROLE_CUSTOMER")));
		acc.setName(dto.getName());
		acc.setEmail(dto.getEmail());
		acc.setPhone(dto.getPhone());
		acc.setAddress(dto.getAddress());
		acc.setBirthday(dto.getBirthday());
		acc.setCity_id(dto.getCity_id());
		acc.setCity_name(dto.getCity_name());
		acc.setDistrict_id(dto.getDistrict_id());
		acc.setDistrict_name(dto.getDistrict_name());
		acc.setWard_id(dto.getWard_id());
		acc.setWard_name(dto.getWard_name());
		acc.setCreate_date(LocalDateTime.now());
		acc.setIsEnabled(1); // 1: account enabled, 0: disabled
		acc.setCart(cart);
		cart.setAccount(acc);
		return accountRepo.save(acc);
	}
	
	@Override 
	public Boolean checkPhoneExist(String phone) {
		return accountRepo.checkPhoneExist(phone);
	}
	
	@Override 
	public Boolean checkEmailExist(String email) {
		Account account = accountRepo.findByEmail(email);
		return account != null;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Account account = accountRepo.findByEmail(email);
		if(account == null) {
			System.out.println("Invalid username or password.");
			throw new UsernameNotFoundException("Invalid username or password.");
		}else if(account.getIsEnabled() != 1){
			throw new DisabledException("Account disabled");
		}
		User user = new User(account.getEmail(), account.getPassword(), mapRolesToAuthorities(account.getRoles()));
		return user;
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	public AccountDTO findByEmail(String email) {
		Account account = accountRepo.findByEmail(email);
		return new AccountDTO(account);
	}

	@Override
	public Account updateAccount(AccountDTO accountDTO) throws Exception{
		try {
			Account account = accountRepo.findByEmail(accountDTO.getEmail());
			account.setName(accountDTO.getName());
			account.setPhone(accountDTO.getPhone());
			account.setAddress(accountDTO.getAddress());
			account.setBirthday(accountDTO.getBirthday());
			account.setCity_id(accountDTO.getCity_id());
			account.setCity_name(accountDTO.getCity_name());
			account.setDistrict_id(accountDTO.getDistrict_id());
			account.setDistrict_name(accountDTO.getDistrict_name());
			account.setWard_id(accountDTO.getWard_id());
			account.setWard_name(accountDTO.getWard_name());
			return accountRepo.save(account);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public void changePassword(String email, String password) throws Exception {
		try {
			Account account = accountRepo.findByEmail(email);
			account.setPassword(encoder.encode(password));
			accountRepo.save(account);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Exception");
		}
	}

	@Override
	public Page<AccountDTO> getAll(String searchKeyword, Pageable pageable) {
		try {
			Page<Account> accounts = accountRepo.searchAccount(searchKeyword, pageable);
			Page<AccountDTO> accountDTOs = accounts.map(account -> new AccountDTO(account));
			return accountDTOs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Boolean enableAccount(String email, boolean isEnable) {
		try {
			if(isEnable){
				accountRepo.enableAccount(email, 1);
			}else{
				accountRepo.enableAccount(email, 0);
				logoutUser(email);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean logoutUser(String email){
		try {
			List<Object> principals = sessionRegistry.getAllPrincipals();
			for (Object principal: principals){
				System.out.println(principal);
				if(principal instanceof User){
					UserDetails userdeDetails = (UserDetails) principal;
					if(userdeDetails.getUsername().equals(email)){
						List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(principal,false);
						for (SessionInformation sessionInformation : sessionInformations){
							sessionInformation.expireNow();
						}
						break;
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
