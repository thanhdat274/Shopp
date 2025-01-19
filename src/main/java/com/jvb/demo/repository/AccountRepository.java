package com.jvb.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.jvb.demo.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
	// @Query(value = "select * from account where email = ?1",nativeQuery = true)
	Account findByEmail(String email);

	@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM Account a WHERE a.phone = :phone", nativeQuery = true)
	Boolean checkPhoneExist(String phone);

	@Query(value = "SELECT * FROM account WHERE (:searchKeyword is NULL OR (name LIKE %:searchKeyword% OR email LIKE %:searchKeyword%))", nativeQuery = true)
	Page<Account> searchAccount(String searchKeyword, Pageable page);

	@Transactional
	@Modifying
	@Query(value = "UPDATE account SET is_enabled =:isEnabled WHERE email =:email",nativeQuery = true)
	int enableAccount(String email, int isEnabled);
}
