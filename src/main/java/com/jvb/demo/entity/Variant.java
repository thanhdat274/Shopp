package com.jvb.demo.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.jvb.demo.dto.ValueDTO;
import com.jvb.demo.dto.VariantDTO;

@Entity
public class Variant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	@OneToMany(mappedBy = "variant")
	@Cascade(CascadeType.ALL)
	private List<Value> values;
	@OneToMany(mappedBy = "variant")
	private List<VariantValue> variant_values;

	public Variant(VariantDTO variantDTO) {
		super();
		this.id = variantDTO.getId();
		this.name = variantDTO.getName();
		this.values = new ArrayList<>();
		for (ValueDTO valueDTO : variantDTO.getValueDTO()) {
			Value value = new Value(valueDTO);
			value.setVariant(this);
			this.values.add(value);
		}
	}

	public Variant() {
		super();
	}

	public Variant(String name, Product product, List<Value> values, List<VariantValue> variant_values) {
		super();
		this.name = name;
		this.product = product;
		this.values = values;
		this.variant_values = variant_values;
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

	public Product getProdct() {
		return product;
	}

	public void setProdct(Product prodct) {
		this.product = prodct;
	}

	public List<Value> getValues() {
		return values;
	}

	public void setValues(List<Value> values) {
		this.values = values;
	}

	public List<VariantValue> getVariant_value() {
		return variant_values;
	}

	public void setVariant_value(List<VariantValue> variant_values) {
		this.variant_values = variant_values;
	}
}
