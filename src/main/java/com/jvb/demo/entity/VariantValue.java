package com.jvb.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class VariantValue {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "variant_id")
	private Variant variant;
	@ManyToOne
	@JoinColumn(name = "value_id")
	private Value value;
	@ManyToOne
	@JoinColumn(name = "sku_id")
	private Sku sku;

	public VariantValue(Variant variant, Value value, Sku sku) {
		this.variant = variant;
		this.value = value;
		this.sku = sku;
	}

	public VariantValue() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Variant getVariant() {
		return variant;
	}

	public void setVariant(Variant variant) {
		this.variant = variant;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public Sku getSku() {
		return sku;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
	}
}
