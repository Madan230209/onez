package com.onez.model;

import java.time.LocalDateTime;

public class FeedbackModel {
    private int feedbackId;
    private String feedbackDetails;
    private int rating;
    private LocalDateTime createdAt;
    private UserModel user;
    private ProductModel product;

    public FeedbackModel() {}

    public FeedbackModel(int feedbackId, String feedbackDetails, int rating, 
                        LocalDateTime createdAt, UserModel user, ProductModel product) {
        this.feedbackId = feedbackId;
        this.feedbackDetails = feedbackDetails;
        this.rating = rating;
        this.createdAt = createdAt;
        this.user = user;
        this.product = product;
    }

	public int getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(int feedbackId) {
		this.feedbackId = feedbackId;
	}

	public String getFeedbackDetails() {
		return feedbackDetails;
	}

	public void setFeedbackDetails(String feedbackDetails) {
		this.feedbackDetails = feedbackDetails;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public ProductModel getProduct() {
		return product;
	}

	public void setProduct(ProductModel product) {
		this.product = product;
	}
	
}