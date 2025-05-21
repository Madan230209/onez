package com.onez.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.onez.config.DbConfig;
import com.onez.model.ProductModel;
/**
 * Service class for handling product-related operations including:
 * Product creation, update, and deletion, Product retrieval by various criteria and Inventory management
 */
public class ProductService {
	 // Database connection
    private Connection dbConn;

    /**
      establishes database connection
     */
    public ProductService() {
        try {
            this.dbConn = DbConfig.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Database connection error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    /**
     * Adds a new product to the database
     */
    public boolean addProduct(ProductModel product) {
        String sql = "INSERT INTO product (productName, description, price, quantity, category, productImage) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());
            stmt.setString(5, product.getCategory());
            stmt.setString(6, product.getProductImage());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Updates an existing product in the database
     * @param product It is the  ProductModel that contains updated product details
     * @return true if product was updated successfully, false otherwise
     */
    public boolean updateProduct(ProductModel product) {
        String sql = "UPDATE product SET productName = ?, description = ?, price = ?, quantity = ?, category = ?, productImage = ? WHERE product_id = ?";

        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());
            stmt.setString(5, product.getCategory());
            stmt.setString(6, product.getProductImage());
            stmt.setInt(7, product.getProductId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Deletes a product from the database
     * @param productId It is the  ID of the product to delete
     * @return true if product was deleted successfully, false otherwise
     */
    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM product WHERE product_id = ?";

        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Retrieves a single product by its ID
     * @param productId TIt is the ID of the product to retrieve
     * @return ProductModel if found, null otherwise
     */
    public ProductModel getProductById(int productId) {
        String sql = "SELECT * FROM product WHERE product_id = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new ProductModel(
                    rs.getInt("product_id"),
                    rs.getString("productName"),
                    rs.getString("description"),
                    rs.getInt("price"),
                    rs.getInt("quantity"),
                    rs.getString("category"),
                    rs.getString("productImage")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving product: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Retrieves all products from the database
     * @return list of all ProductModel objects
     */
    public List<ProductModel> getAllProducts() {
        List<ProductModel> products = new ArrayList<>();
        String sql = "SELECT * FROM product";

        try (Statement stmt = dbConn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                ProductModel product = new ProductModel(
                    rs.getInt("product_id"),
                    rs.getString("productName"),
                    rs.getString("description"),
                    rs.getInt("price"),
                    rs.getInt("quantity"),
                    rs.getString("category"),
                    rs.getString("productImage")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving products: " + e.getMessage());
            e.printStackTrace();
        }

        return products;
    }
    /**
     * Retrieves the  recently added products
     * @return list of recent ProductModel objects
     */
    public List<ProductModel> getRecentProducts() {
        List<ProductModel> recentProducts = new ArrayList<>();
        String sql = "SELECT * FROM product ORDER BY product_id DESC LIMIT 3";

        try (Statement stmt = dbConn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                ProductModel product = new ProductModel(
                    rs.getInt("product_id"),
                    rs.getString("productName"),
                    rs.getString("description"),
                    rs.getInt("price"),
                    rs.getInt("quantity"),
                    rs.getString("category"),
                    rs.getString("productImage")
                );
                recentProducts.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving products: " + e.getMessage());
            e.printStackTrace();
        }

        return recentProducts;
    }
    /**
     * Retrieves products belonging to a specific category
     * @param category It is the  category used  to filter  the products
     * @return list of ProductModel objects in the specified category
     */
    public List<ProductModel> getProductsByCategory(String category) {
        List<ProductModel> products = new ArrayList<>();
        String sql = "SELECT * FROM product WHERE category = ?";

        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProductModel product = new ProductModel(
                    rs.getInt("product_id"),
                    rs.getString("productName"),
                    rs.getString("description"),
                    rs.getInt("price"),
                    rs.getInt("quantity"),
                    rs.getString("category"),
                    rs.getString("productImage")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving products by category: " + e.getMessage());
            e.printStackTrace();
        }

        return products;
    }
    /**
     * Searches for products by name or category
     * @param search It  search for in product names or categories
     * @return list of ProductModel objects matching the search criteria
     */
    public List<ProductModel> getProductsBySearch(String search) {
        List<ProductModel> products = new ArrayList<>();
        String sql = "SELECT * FROM product WHERE productName LIKE ? OR category LIKE ?";

        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            String pattern = "%" + search + "%";
        	stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProductModel product = new ProductModel(
                    rs.getInt("product_id"),
                    rs.getString("productName"),
                    rs.getString("description"),
                    rs.getInt("price"),
                    rs.getInt("quantity"),
                    rs.getString("category"),
                    rs.getString("productImage")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving products by productName: " + e.getMessage());
            e.printStackTrace();
        }

        return products;
    }
}
