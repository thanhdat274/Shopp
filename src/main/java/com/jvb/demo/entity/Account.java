package com.jvb.demo.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.format.annotation.DateTimeFormat;

import com.jvb.demo.dto.AccountDTO;

@Entity
@Table(name = "account", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "account_role", joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Collection<Role> roles;

	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
	private Cart cart;

	@OneToMany(mappedBy = "account")
	private List<Order> orders;

	@Column(name = "name")
	private String name;

	@Column(name = "phone")
	private String phone;

	@Column(name = "birthday")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;

	@Column(name = "address")
	private String address;
	private String city_name;
	private Integer city_id;
	private String district_name;
	private Integer district_id;
	private String ward_name;
	private String ward_id;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime create_date;
	
	private Integer isEnabled; // 1: account enabled, 0: disabled

	public Account() {
		super();
	}

	public Account(String email, String password, Collection<Role> roles, String name, String phone, Date birthday,
			String address, String city_name, Integer city_id, String district_name, Integer district_id, String ward_name,
			String ward_id) {
		this.email = email;
		this.password = password;
		this.roles = roles;
		this.name = name;
		this.phone = phone;
		this.birthday = birthday;
		this.address = address;
		this.city_name = city_name;
		this.city_id = city_id;
		this.district_name = district_name;
		this.district_id = district_id;
		this.ward_name = ward_name;
		this.ward_id = ward_id;
	}

	public Account(AccountDTO dto) {
		this.id = dto.getId();
		this.name = dto.getName();
		this.email = dto.getEmail();
		this.password = dto.getPassword();
		this.phone = dto.getPhone();
		this.address = dto.getAddress();
		this.birthday = dto.getBirthday();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
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

	public Integer getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}

	public LocalDateTime getCreate_date() {
		return create_date;
	}

	public void setCreate_date(LocalDateTime create_date) {
		this.create_date = create_date;
	}

}
