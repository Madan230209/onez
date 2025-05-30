package com.onez.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.onez.config.DbConfig;
import com.onez.model.CartModel;
import com.onez.model.CartItemModel;
import com.onez.model.ProductModel;

public class CartService {
    private Connection dbConn;
    // Constructor: Establishing the database connection
    public CartService() {
        try {
            this.dbConn = DbConfig.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Database connection error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    /**
     * Retrieves the cart and its items for a given user ID.
     * @param userId The ID of the user whose cart is to be fetched.
     * @return A CartModel containing cart details, items, and totals.
     */
    public CartModel getCartByUserId(int userId) {
        String cartQuery = "SELECT * FROM onez.cart WHERE user_id = ?";
        String itemsQuery = "SELECT ci.*, p.* FROM onez.cartitem ci JOIN onez.product p ON ci.product_id = p.product_id WHERE ci.cart_id = ?";
        
        CartModel cart = new CartModel();
        List<CartItemModel> items = new ArrayList<>();
        double totalPrice = 0;
        int totalItems = 0;

        try (PreparedStatement cartStmt = dbConn.prepareStatement(cartQuery)) {
            cartStmt.setInt(1, userId);
            ResultSet cartRs = cartStmt.executeQuery();

            if (cartRs.next()) {
                cart.setCartId(cartRs.getInt("cart_id"));
                cart.setCreatedAt(cartRs.getDate("createdAt").toLocalDate());
                
                // Get cart items
                try (PreparedStatement itemsStmt = dbConn.prepareStatement(itemsQuery)) {
                    itemsStmt.setInt(1, cart.getCartId());
                    ResultSet itemsRs = itemsStmt.executeQuery();

                    while (itemsRs.next()) {
                        CartItemModel item = new CartItemModel();
                        item.setCartItemId(itemsRs.getInt("cartitem_id"));
                        item.setProductQuantity(itemsRs.getInt("productQuantity"));

                        ProductModel product = new ProductModel();
                        product.setProductId(itemsRs.getInt("product_id"));
                        product.setProductName(itemsRs.getString("productName"));
                        product.setPrice(itemsRs.getDouble("price"));
                        product.setQuantity(itemsRs.getInt("quantity"));
                        product.setDescription(itemsRs.getString("description"));
                        product.setCategory(itemsRs.getString("category"));
                        product.setProductImage(itemsRs.getString("productImage"));

                        item.setProduct(product);
                        items.add(item);

                        totalPrice += (product.getPrice() * item.getProductQuantity());
                        totalItems += item.getProductQuantity();
                    }
                }

                cart.setItems(items);
                cart.setTotalPrice(totalPrice);
                cart.setTotalItems(totalItems);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving cart: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return cart;
    }
    /**
     * Adds a product to the user's cart. If the product already exists,quantity is incremented.
     * @param userId The user's ID.
     * @param productId The product's ID to be added.
     * @param quantity The quantity to add.
     * @return Updated CartModel after the product is added.
     */
    public CartModel addProductToCart(int userId, int productId, int quantity) {
        try {
            int cartId = getOrCreateCartId(userId);
            CartItemModel existingItem = getCartItem(cartId, productId);

            if (existingItem != null) {
                updateCartItemQuantity(cartId, productId, existingItem.getProductQuantity() + quantity);
            } else {
                insertCartItem(cartId, productId, quantity);
            }

            updateCartTotals(cartId);
            return getCartByUserId(userId);
        } catch (SQLException e) {
            System.err.println("Error adding product to cart: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Removes a specified quantity of a product from the cart.
     * If the quantity becomes zero or less, removes the product entirely.
     * @param userId to remove user's ID.
     * @param productId to remove product's ID .
     * @param quantity to remove  the  quantity.
     * @return updated CartModel after the product is removed.
     */
    public CartModel removeProductFromCart(int userId, int productId, int quantity) {
        try {
            int cartId = getCartId(userId);
            if (cartId == 0) return null;

            CartItemModel existingItem = getCartItem(cartId, productId);
            if (existingItem == null) return null;

            int newQuantity = existingItem.getProductQuantity() - quantity;

            if (newQuantity <= 0) {
                deleteCartItem(cartId, productId);
            } else {
                updateCartItemQuantity(cartId, productId, newQuantity);
            }

            updateCartTotals(cartId);
            return getCartByUserId(userId);
        } catch (SQLException e) {
            System.err.println("Error removing product from cart: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Clears all items from the user's cart and resets total price and total items to zero.
     * @param userId the ID of the user whose cart needs to be cleared.
     * @return true if the cart was cleared successfully; false otherwise.
     */
    public boolean clearCart(int userId) {
        try {
            int cartId = getCartId(userId);
            if (cartId == 0) return false;

            // Delete all cart items
            String deleteItems = "DELETE FROM onez.cartitem WHERE cart_id = ?";
            try (PreparedStatement stmt = dbConn.prepareStatement(deleteItems)) {
                stmt.setInt(1, cartId);
                stmt.executeUpdate();
            }

            // Reset cart totals
            String resetCart = "UPDATE onez.cart SET total_items = 0, total_price = 0 WHERE cart_id = ?";
            try (PreparedStatement stmt = dbConn.prepareStatement(resetCart)) {
                stmt.setInt(1, cartId);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error clearing cart: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Helper methods
    /**
     * Gets the existing cart ID for a user, or creates a new one if none exists.
     * @param userId .
     * @return  cart ID.
     */
    private int getOrCreateCartId(int userId) throws SQLException {
        int cartId = getCartId(userId);
        if (cartId == 0) {
            String query = "INSERT INTO onez.cart (user_id, total_items, total_price, createdAt) VALUES (?, 0, 0, ?)";
            try (PreparedStatement stmt = dbConn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, userId);
                stmt.setDate(2, Date.valueOf(LocalDate.now()));
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    cartId = rs.getInt(1);
                }
            }
        }
        return cartId;
    }

    private int getCartId(int userId) throws SQLException {
        String query = "SELECT cart_id FROM onez.cart WHERE user_id = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("cart_id") : 0;
        }
    }
    /**
     * Retrieves a specific cart item based on cart ID and product ID.
     * @param cartId cart ID to retrieves cart items .
     * @param productId retrieves products from cart.
     * @return A CartItemModel object if found, otherwise null.
     */

    private CartItemModel getCartItem(int cartId, int productId) throws SQLException {
        String query = "SELECT ci.*, p.* FROM onez.cartitem ci JOIN onez.product p ON ci.product_id = p.product_id " +
                      "WHERE ci.cart_id = ? AND ci.product_id = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, cartId);
            stmt.setInt(2, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                CartItemModel item = new CartItemModel();
                item.setCartItemId(rs.getInt("cartitem_id"));
                item.setProductQuantity(rs.getInt("productQuantity"));

                ProductModel product = new ProductModel();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("productName"));
                product.setPrice(rs.getDouble("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setDescription(rs.getString("description"));
                product.setCategory(rs.getString("category"));
                product.setProductImage(rs.getString("productImage"));

                item.setProduct(product);
                return item;
            }
        }
        return null;
    }
    /**
     * Inserts a new product into the cart.
     * @param cartId cart ID in which  new product will be added.
     * @param productId  ID of the product to insert into the cart.
     * @param quantity the quantity of the product to insert in cart.
     * @return true if the insert is successful, false otherwise.
     */
    private boolean insertCartItem(int cartId, int productId, int quantity) throws SQLException {
        String query = "INSERT INTO onez.cartitem (cart_id, product_id, productQuantity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, cartId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            return stmt.executeUpdate() > 0;
        }
    }
    /**
     * Updates the quantity of a product already present in the cart.
     * @param cartId ID of the cart for updating the quantity.
     * @param productId product ID in the cart.
     * @param newQuantity  the new quantity to set in the cart.
     * @return true if update is successful, false otherwise.
     */
    private boolean updateCartItemQuantity(int cartId, int productId, int newQuantity) throws SQLException {
        String query = "UPDATE onez.cartitem SET productQuantity = ? WHERE cart_id = ? AND product_id = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, newQuantity);
            stmt.setInt(2, cartId);
            stmt.setInt(3, productId);
            return stmt.executeUpdate() > 0;
        }
    }
    /**
     * Deletes a product from the cart entirely.
     * @param cartId  cart ID to delete .
     * @param productId  product ID to delete.
     * @return true if delete is successful, false otherwise.
     */

    private boolean deleteCartItem(int cartId, int productId) throws SQLException {
        String query = "DELETE FROM onez.cartitem WHERE cart_id = ? AND product_id = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, cartId);
            stmt.setInt(2, productId);
            return stmt.executeUpdate() > 0;
        }
    }
    /**
     * Updates the total number of items and total price of the cart
     * based on the current products and their quantities.
     * 
     * @param cartId ID of the cart to update.
     */
    private void updateCartTotals(int cartId) throws SQLException {
        String query = "UPDATE onez.cart c SET " +
                      "total_items = (SELECT COALESCE(SUM(productQuantity), 0) FROM onez.cartitem WHERE cart_id = ?), " +
                      "total_price = (SELECT COALESCE(SUM(p.price * ci.productQuantity), 0) " +
                      "FROM onez.cartitem ci JOIN onez.product p ON ci.product_id = p.product_id WHERE ci.cart_id = ?) " +
                      "WHERE c.cart_id = ?";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setInt(1, cartId);
            stmt.setInt(2, cartId);
            stmt.setInt(3, cartId);
            stmt.executeUpdate();
        }
    }
}