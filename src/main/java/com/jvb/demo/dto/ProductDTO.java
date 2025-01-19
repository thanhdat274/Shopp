package com.jvb.demo.dto;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import com.jvb.demo.entity.Image;
import com.jvb.demo.entity.Product;
import com.jvb.demo.entity.Sku;
import com.jvb.demo.entity.Variant;

public class ProductDTO {
	private long id;
	private String name;
	private String price;
	private String brand;
	private String type;
	private String thumbnail;
	private String description;
	private String status;
	private LocalDateTime create_date;
	private List<VariantDTO> variantDTO;
	private List<SkuDTO> skuDTO;
	private List<ImageDTO> imageDTO;
	private String storage;
	private String battery;
	private String ram;
	private String cpu;
	private String operating_system;
	private String screen_size;
	private String screen_reslution;
	private String weight;

	public ProductDTO() {
		super();
	}

	public ProductDTO(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.brand = product.getBrand();
		this.type = product.getType();
		this.thumbnail = product.getThumbnail();
		this.description = product.getDescription();
		this.status = product.getStatus();
		this.create_date = product.getCreate_date();
		List<VariantDTO> variantDTO = new ArrayList<>();
		List<SkuDTO> skuDTO = new ArrayList<>();
		this.imageDTO = new ArrayList<>();
		if(product.getVariants() != null)
			for (Variant variant : product.getVariants()) {
				variantDTO.add(new VariantDTO(variant));
			}
		if(product.getSkus() != null)
			for (Sku sku : product.getSkus()) {
				skuDTO.add(new SkuDTO(sku));
			}
		this.variantDTO = variantDTO;
		this.skuDTO = skuDTO;
		if (product.getImages() != null)
			for (Image image : product.getImages())
				this.imageDTO.add(new ImageDTO(image));
		this.storage = product.getStorage();
		this.battery = product.getBattery();
		this.ram = product.getRam();
		this.cpu = product.getCpu();
		this.operating_system = product.getOperating_system();
		this.screen_size = product.getScreen_size();
		this.screen_reslution = product.getScreen_reslution();
		this.weight = product.getWeight();
		getPriceRange();
	}

	public void getPriceRange(){
		try {
			DecimalFormat formatter = new DecimalFormat("###,###,###");
			Long min = this.skuDTO.stream().filter(skuDTO -> skuDTO.getPrice() > 0)
				.min(Comparator.comparing(SkuDTO::getPrice)).orElseThrow(NoSuchElementException::new).getPrice().longValue();
			Long max = this.skuDTO.stream().filter(skuDTO -> skuDTO.getPrice() > 0)
				.max(Comparator.comparing(SkuDTO::getPrice)).orElseThrow(NoSuchElementException::new).getPrice().longValue();
			if(min.equals(max) )
				this.price = formatter.format(min) +" ₫";
			else
				this.price = formatter.format(min) + " ₫ - " + formatter.format(max) +" ₫";
		} catch (Exception e) {
			e.printStackTrace();
			this.price = "Đang cập nhật";
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
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

	public List<VariantDTO> getVariantDTO() {
		return variantDTO;
	}

	public void setVariantDTO(List<VariantDTO> variantDTO) {
		this.variantDTO = variantDTO;
	}

	public List<SkuDTO> getSkuDTO() {
		return skuDTO;
	}

	public void setSkuDTO(List<SkuDTO> skuDTO) {
		this.skuDTO = skuDTO;
	}

	public List<ImageDTO> getImageDTO() {
		return imageDTO;
	}

	public void setImageDTO(List<ImageDTO> imageDTO) {
		this.imageDTO = imageDTO;
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
