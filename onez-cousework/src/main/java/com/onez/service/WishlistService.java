package com.onez.service;

import com.onez.config.DbConfig;
import com.onez.model.ProductModel;
import com.onez.model.UserModel;
import com.onez.model.WishlistModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Service class to manage user wishlists.
 */
public class WishlistService {
    private final Connection connection;
    /**
     * Initializes  WishlistService by setting up  database connection.
     * @throws SQLException if database connection fails.
     */
    public WishlistService() throws SQLException {
        try {
            this.connection = DbConfig.getDbConnection();
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Database driver not found", ex);
        }
    }
    /**
     * Gets the wishlist of a specific user and creates new wishlist if wishlist is not created
     * @param user The user whose wishlist is to be fetched.
     * @return WishlistModel object containing details of wishlist.
     * @throws SQLException if there is  database error.
     */
    public WishlistModel getWishlistByUser(UserModel user) throws SQLException {
        String sql = "SELECT w.* FROM wishlist w WHERE w.user_id = ?";
        WishlistModel wishlist = null;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                wishlist = new WishlistModel();
                wishlist.setWishlistId(rs.getInt("wishlist_id"));
                wishlist.setUser(user);
                wishlist.setWishlistName(rs.getString("wishlist_name"));
                wishlist.setAddedAt(rs.getTimestamp("addedAt").toLocalDateTime());
                wishlist.setProducts(getWishlistProducts(wishlist.getWishlistId()));
            } else {
                // Create wishlist if it doesn't exist
                wishlist = createWishlist(user);
            }
        }
        return wishlist;
    }
    /**
     * Gets the list of products in a wishlist.
     * @param wishlistId It is the  ID of the wishlist.
     * @return  the list of ProductModel objects in the wishlist.
     * @throws SQLException if there is a database error.
     */
    private List<ProductModel> getWishlistProducts(int wishlistId) throws SQLException {
        List<ProductModel> products = new ArrayList<>();
        String sql = "SELECT p.*, i.quantity as quantity FROM wishlist_product wp " +
                     "JOIN product p ON wp.product_id = p.product_id " +
                     "LEFT JOIN product i ON p.product_id = i.product_id " +
                     "WHERE wp.wishlist_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, wishlistId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ProductModel product = new ProductModel();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("productName"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setProductImage(rs.getString("productImage"));
                product.setQuantity(rs.getInt("quantity")); // For stock status display
                products.add(product);
            }
        }
        return products;
    }
    /**
     * Adds a product to the user's wishlist.
     * @param user   User adding the product in wishlist.
     * @param productId  It is the ID of the product to add.
     * @return true if the product was added, false if an error occurred.
     * @throws SQLException if there is a database error.
     */
    public boolean addToWishlist(UserModel user, int productId) throws SQLException {
        WishlistModel wishlist = getWishlistByUser(user);
        if (wishlist == null) {
            throw new SQLException("Failed to create or retrieve wishlist");
        }
        
        // Check if product already exists in wishlist
        if (isProductInWishlist(wishlist.getWishlistId(), productId)) {
            return true; // Already exists, consider it a success
        }
        
        String sql = "INSERT INTO wishlist_product (wishlist_id, product_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, wishlist.getWishlistId());
            stmt.setInt(2, productId);
            return stmt.executeUpdate() > 0;
        }
    }
    /**
     * Checks if a product exists in a wishlist
     * @param wishlistId  It is the  ID of the wishlist to check whether product exist
     * @param productId It is the  ID of the product to check
     * @return true if product exists in wishlist, returns false otherwise
     * @throws SQLException if database operations fail
     */
    private boolean isProductInWishlist(int wishlistId, int productId) throws SQLException {
        String sql = "SELECT 1 FROM wishlist_product WHERE wishlist_id = ? AND product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, wishlistId);
            stmt.setInt(2, productId);
            return stmt.executeQuery().next();
        }
    }
    /**
     * Creates a new wishlist for a user
     * @param user 
     * @return  recent created WishlistModel by  the user
     * @throws SQLException if creation fails
     */
    private WishlistModel createWishlist(UserModel user) throws SQLException {
        String sql = "INSERT INTO wishlist (user_id, wishlist_name, addedAt) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, user.getId());
            stmt.setString(2, "My Wishlist");
            stmt.setObject(3, LocalDateTime.now());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        WishlistModel wishlist = new WishlistModel();
                        wishlist.setWishlistId(generatedKeys.getInt(1));
                        wishlist.setUser(user);
                        wishlist.setWishlistName("My Wishlist");
                        wishlist.setAddedAt(LocalDateTime.now());
                        return wishlist;
                    }
                }
            }
            throw new SQLException("Creating wishlist failed, no ID obtained.");
        }
    }
    /**
     * Removes a product from user's wishlist
     * @param user It is the user whose wishlist  is to be  modified
     * @param productId  It is the  ID of product to remove
     * @return true if product is removed, false otherwise
     * @throws SQLException if database operations fail
     */
    public boolean removeFromWishlist(UserModel user, int productId) throws SQLException {
        WishlistModel wishlist = getWishlistByUser(user);
        if (wishlist == null) return false;
        
        String sql = "DELETE FROM wishlist_product WHERE wishlist_id = ? AND product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, wishlist.getWishlistId());
            stmt.setInt(2, productId);
            return stmt.executeUpdate() > 0;
        }
    }
}