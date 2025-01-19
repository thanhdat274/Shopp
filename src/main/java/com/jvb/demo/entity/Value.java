package com.jvb.demo.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.jvb.demo.dto.ValueDTO;

@Entity
public class Value {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@ManyToOne
	@JoinColumn(name = "variant_id")
	private Variant variant;
	@OneToMany(mappedBy = "value")
	private List<VariantValue> variant_values;

	public Value(String name, Variant variant, List<VariantValue> variant_values) {
		super();
		this.name = name;
		this.variant = variant;
		this.variant_values = variant_values;
	}

	public Value(ValueDTO valueDTO) {
		this.id = valueDTO.getId();
		this.name = valueDTO.getName();
	}

	public Value() {
		super();
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

	public Variant getVariant() {
		return variant;
	}

	public void setVariant(Variant variant) {
		this.variant = variant;
	}

	public List<VariantValue> getVariant_values() {
		return variant_values;
	}

	public void setVariant_value(List<VariantValue> variant_values) {
		this.variant_values = variant_values;
	}
}
