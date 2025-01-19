package com.jvb.demo.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.jvb.demo.dto.ProductDTO;
import com.jvb.demo.dto.VariantDTO;

@Entity
@Table(name = "product")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "brand")
	private String brand;

	@Column(name = "type")
	private String type;

	@Column(name = "thumbnail")
	private String thumbnail;

	@Column(name = "description")
	private String description;

	@Column(name = "status")
	private String status;

	@Column(name = "create_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime create_date;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<Variant> variants;
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<Sku> skus;

	@OneToMany(mappedBy = "product")
	private List<CartDetail> cartDetails;

	@OneToMany(mappedBy = "product")
	private List<OrderDetail> orderDetails;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<Image> images;

	private String storage;
	private String battery;
	private String ram;
	private String cpu;
	private String operating_system;
	private String screen_size;
	private String screen_reslution;
	private String weight;

	public Product() {
		super();
	}

	public Product(String name, String brand, String type, String thumbnail, String description,
			String status, LocalDateTime create_date) {
		super();
		this.name = name;
		this.brand = brand;
		this.type = type;
		this.thumbnail = thumbnail;
		this.description = description;
		this.status = status;
		this.create_date = create_date;
	}

	public Product(ProductDTO dto) {
		this.id = dto.getId();
		this.name = dto.getName();
		this.brand = dto.getBrand();
		this.type = dto.getType();
		this.thumbnail = dto.getThumbnail();
		this.description = dto.getDescription();
		this.status = dto.getStatus();
		this.create_date = dto.getCreate_date();
		this.variants = new ArrayList<>();
		if (dto.getVariantDTO() != null)
			for (VariantDTO variantDTO : dto.getVariantDTO()) {
				Variant variant = new Variant(variantDTO);
				variant.setProdct(this);
				this.variants.add(variant);
			}
		this.storage = dto.getStorage();
		this.battery = dto.getBattery();
		this.ram = dto.getRam();
		this.cpu = dto.getCpu();
		this.operating_system = dto.getOperating_system();
		this.screen_size = dto.getScreen_size();
		this.screen_reslution = dto.getScreen_reslution();
		this.weight = dto.getWeight();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreate_date() {
		return create_date;
	}

	public void setCreate_date(LocalDateTime create_date) {
		this.create_date = create_date;
	}

	public List<Variant> getVariants() {
		return variants;
	}

	public void setVariants(List<Variant> variants) {
		this.variants = variants;
	}

	public List<Sku> getSkus() {
		return skus;
	}

	public void setSkus(List<Sku> skus) {
		this.skus = skus;
	}

	public List<CartDetail> getCartDetails() {
		return cartDetails;
	}

	public void setCartDetails(List<CartDetail> cartDetails) {
		this.cartDetails = cartDetails;
	}

	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public String getBattery() {
		return battery;
	}

	public void setBattery(String battery) {
		this.battery = battery;
	}

	public String getRam() {
		return ram;
	}

	public void setRam(String ram) {
		this.ram = ram;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getOperating_system() {
		return operating_system;
	}

	public void setOperating_system(String operating_system) {
		this.operating_system = operating_system;
	}

	public String getScreen_size() {
		return screen_size;
	}

	public void setScreen_size(String screen_size) {
		this.screen_size = screen_size;
	}

	public String getScreen_reslution() {
		return screen_reslution;
	}

	public void setScreen_reslution(String screen_reslution) {
		this.screen_reslution = screen_reslution;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

}
