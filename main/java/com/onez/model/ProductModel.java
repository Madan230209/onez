package com.onez.model;

public class ProductModel {

	private int programId;
	private String name;
	private String type;
	private String category;

	public ProductModel() {
	}

	public ProductModel(String name, String type, String category) {
		super();
		this.name = name;
		this.type = type;
		this.category = category;
	}

	public ProductModel(String name) {
		this.name = name;
	}

	public int getProgramId() {
		return programId;
	}

	public void setProgramId(int programId) {
		this.programId = programId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}