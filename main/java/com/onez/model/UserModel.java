package com.onez.model;

import java.time.LocalDate;

public class UserModel {

	private int id;
	private String firstName;
	private String lastName;
	private String userName;
	private LocalDate dob;
	private String gender;
	private String email;
	private String number;
	private String password;
	private ProductModel Product;
	private String imageUrl;

	public UserModel() {
	}

	public UserModel(String username, String pasword) {
		this.userName = username;
		this.password = pasword;
	}

	public UserModel(int id, String firstName, String lastName, String userName, LocalDate dob, String gender,
			String email, String number, String password, ProductModel Product, String imageUrl) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.dob = dob;
		this.gender = gender;
		this.email = email;
		this.number = number;
		this.password = password;
		this.Product = Product;
		this.imageUrl = imageUrl;
	}

	public UserModel(String firstName, String lastName, String userName, LocalDate dob, String gender, String email,
			String number, String password, ProductModel Product, String imageUrl) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.dob = dob;
		this.gender = gender;
		this.email = email;
		this.number = number;
		this.password = password;
		this.Product = Product;
		this.imageUrl = imageUrl;
	}

	public UserModel(int id, String firstName, String lastName, ProductModel Product, String email, String number) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.Product = Product;
		this.email = email;
		this.number = number;
	}
	
	public UserModel(int id, String firstName, String lastName, String email, String number) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.number = number;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ProductModel getProduct() {
		return Product;
	}

	public void setProduct(ProductModel Product) {
		this.Product = Product;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}