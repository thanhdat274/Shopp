package com.jvb.demo.dto;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.jvb.demo.entity.Account;

public class AccountDTO {
	private long id;
	private String name;
	private String email;
	private String password;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;
	private String address;
	private String phone;
	private String city_name;
	private Integer city_id;
	private String district_name;
	private Integer district_id;
	private String ward_name;
	private String ward_id;
	private LocalDateTime create_date;
	private Integer isEnable;

	public AccountDTO(String name, String email, String password, Date birthday, String address, String phone,
			String city_name, Integer city_id, String district_name, Integer district_id, String ward_name,
			String ward_id) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.birthday = birthday;
		this.address = address;
		this.phone = phone;
		this.city_name = city_name;
		this.city_id = city_id;
		this.district_name = district_name;
		this.district_id = district_id;
		this.ward_name = ward_name;
		this.ward_id = ward_id;
	}

	public AccountDTO() {
		super();
	}

	public AccountDTO(Account acc) {
		this.id = acc.getId();
		this.name = acc.getName();
		this.email = acc.getEmail();
		this.password = acc.getPassword();
		this.birthday = acc.getBirthday();
		this.phone = acc.getPhone();
		this.address = acc.getAddress();
		this.city_id = acc.getCity_id();
		this.city_name = acc.getCity_name();
		this.district_id = acc.getDistrict_id();
		this.district_name = acc.getDistrict_name();
		this.ward_id = acc.getWard_id();
		this.ward_name = acc.getWard_name();
		this.create_date = acc.getCreate_date();
		this.isEnable = acc.getIsEnabled();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public Integer getCity_id() {
		return city_id;
	}

	public void setCity_id(Integer city_id) {
		this.city_id = city_id;
	}

	public String getDistrict_name() {
		return district_name;
	}

	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}

	public Integer getDistrict_id() {
		return district_id;
	}

	public void setDistrict_id(Integer district_id) {
		this.district_id = district_id;
	}

	public String getWard_name() {
		return ward_name;
	}

	public void setWard_name(String ward_name) {
		this.ward_name = ward_name;
	}

	public String getWard_id() {
		return ward_id;
	}

	public void setWard_id(String ward_id) {
		this.ward_id = ward_id;
	}
	
	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public LocalDateTime getCreate_date() {
		return create_date;
	}

	public void setCreate_date(LocalDateTime create_date) {
		this.create_date = create_date;
	}

}
