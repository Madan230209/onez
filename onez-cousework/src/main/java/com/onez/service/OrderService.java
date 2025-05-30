package com.onez.service;

import java.sql.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.onez.config.DbConfig;
import com.onez.model.OrderModel;
import com.onez.model.OrderItemModel;
import com.onez.model.CartModel;
import com.onez.model.CartItemModel;
import com.onez.model.ProductModel;
import com.onez.model.UserModel;
import com.onez.model.AddressModel;
/**
 * Service class for handling order-related operations including:
 * Order processing,Order retrieval,Order status updates,Order deletion
 * Implements AutoCloseable to ensure proper resource cleanup
 */
public class OrderService implements AutoCloseable {
    // Constants
    private static final String ORDER_STATUS_PROCESSING = "Processing";
    private static final String ORDER_STATUS_PENDING = "Pending";
    
    private final Connection dbConn;
    private boolean isConnectionError = false;
    
    /**
     * Constructor - establishes database connection
     * @throws SQLException if connection fails
     */
    public OrderService() throws SQLException {
        try {
            this.dbConn = DbConfig.getDbConnection();
            this.dbConn.setAutoCommit(false); // Transactions disabled by default
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Database driver not found", ex);
        }
    }
    /**
     * Close method to clean up resources
     */
    @Override
    public void close() {
        try {
            if (dbConn != null && !dbConn.isClosed()) {
                dbConn.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    // Main order processing method
    /**
     * Main method to process an order from a user's cart
     * @param userId  ID of the user  placing the order
     * @param paymentMethod  payment method used by user
     * @return The created OrderModel if successful, null otherwise
     * @throws SQLException if database operations fail
     */
    public OrderModel processOrder(int userId, String paymentMethod) throws SQLException {
        validatePaymentMethod(paymentMethod);
        
        try {
            // 1. Get user's cart
            CartModel cart = getCartForUser(userId);
            if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
                return null;
            }

            // 2. Create order record
            OrderModel order = createOrderRecord(cart, paymentMethod);
            if (order == null) {
                dbConn.rollback();
                return null;
            }

            // 3. Save order items
            saveOrderItems(order.getOrderId(), cart.getItems());
            
            // 4. Update product quantities
            if (!updateProductQuantities(cart)) {
                dbConn.rollback();
                return null;
            }

            // 5. Clear the cart
            if (!clearUserCart(cart.getCartId())) {
                dbConn.rollback();
                return null;
            }

            dbConn.commit();
            return order;
        } catch (SQLException e) {
            dbConn.rollback();
            throw e;
        }
    }

    // Cart-related methods
    /**
     * Retrieves the cart for a given user including all cart items
     * @param userId ID of the user
     * @return CartModel with items or  will give null if not found
     * @throws SQLException if database operations fail
     */
    public CartModel getCartForUser(int userId) throws SQLException {
        String cartSql = "SELECT * FROM cart WHERE user_id = ?";
        String itemsSql = "SELECT ci.*, p.* FROM cartitem ci JOIN product p ON ci.product_id = p.product_id WHERE ci.cart_id = ?";
        
        try (PreparedStatement cartStmt = dbConn.prepareStatement(cartSql)) {
            cartStmt.setInt(1, userId);
            
            try (ResultSet cartRs = cartStmt.executeQuery()) {
                if (cartRs.next()) {
                    CartModel cart = mapCart(cartRs, userId);
                    cart.setItems(getCartItems(cart.getCartId(), itemsSql));
                    return cart;
                }
            }
        }
        return null;
    }

    private List<CartItemModel> getCartItems(int cartId, String itemsSql) throws SQLException {
        List<CartItemModel> items = new ArrayList<>();
        
        try (PreparedStatement itemsStmt = dbConn.prepareStatement(itemsSql)) {
            itemsStmt.setInt(1, cartId);
            
            try (ResultSet itemsRs = itemsStmt.executeQuery()) {
                while (itemsRs.next()) {
                    items.add(mapCartItem(itemsRs));
                }
            }
        }
        return items;
    }

    // Order-related methods
    /**
     * Helper method to retrieve cart items for a given cart
     * @param cartId ID of the cart
     * @param itemsSql  SQL query 
     * @return list of CartItemModel objects
     * @throws SQLException if database operations fails
     */
    public List<OrderModel> getUserOrders(int userId) throws SQLException {
        String orderSql = "SELECT * FROM order_table WHERE user_id = ? ORDER BY order_date DESC";
        String itemsSql = "SELECT oi.*, p.* FROM order_items oi JOIN product p ON oi.product_id = p.product_id WHERE oi.order_id = ?";
        
        List<OrderModel> orders = new ArrayList<>();
        
        try (PreparedStatement orderStmt = dbConn.prepareStatement(orderSql)) {
            orderStmt.setInt(1, userId);
            
            try (ResultSet orderRs = orderStmt.executeQuery()) {
                while (orderRs.next()) {
                    OrderModel order = mapOrder(orderRs);
                    order.setItems(getOrderItems(order.getOrderId(), itemsSql));
                    orders.add(order);
                }
            }
        }
        return orders;
    }
    /**
     * Retrieves all order items for a given order
     * @param orderId ID of the order
     * @param itemsSql  SQL query 
     * @return The list of OrderItemModel objects
     * @throws SQLException if database operations fail
     */
    private List<OrderItemModel> getOrderItems(int orderId, String itemsSql) throws SQLException {
        List<OrderItemModel> items = new ArrayList<>();
        
        try (PreparedStatement itemsStmt = dbConn.prepareStatement(itemsSql)) {
            itemsStmt.setInt(1, orderId);
            
            try (ResultSet itemsRs = itemsStmt.executeQuery()) {
                while (itemsRs.next()) {
                    items.add(mapOrderItem(itemsRs));
                }
            }
        }
        return items;
    }

    // Database operations
    /**
     * Creates an order record in the database
     * @param cart The cart of  specific user being converted to an order
     * @param paymentMethod  payment method used by user
     * @return OrderModel    created order
     * @throws SQLException if database operations fail
     */
    
    private OrderModel createOrderRecord(CartModel cart, String paymentMethod) throws SQLException {
        String sql = "INSERT INTO order_table (user_id, cart_id, order_date, order_status, paymentMethod) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, cart.getUser().getId());
            stmt.setInt(2, cart.getCartId());
            stmt.setDate(3, Date.valueOf(LocalDate.now()));
            stmt.setString(4, ORDER_STATUS_PROCESSING);
            stmt.setString(5, paymentMethod);
            
            if (stmt.executeUpdate() == 0) {
                return null;
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    OrderModel order = new OrderModel();
                    order.setOrderId(generatedKeys.getInt(1));
                    order.setItems(convertCartItemsToOrderItems(cart.getItems()));
                    order.setUser(cart.getUser());
                    order.setOrderDate(LocalDate.now());
                    order.setOrderStatus(ORDER_STATUS_PENDING);
                    order.setPaymentMethod(paymentMethod);
                    return order;
                }
            }
        }
        return null;
    }
    /**
     * Saves all order items to the database
     * @param orderId  ID of the order
     * @param cartItems The cart items  converted to order items
     * @throws SQLException if database operations fail
     */
    private void saveOrderItems(int orderId, List<CartItemModel> cartItems) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price_at_order) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            for (CartItemModel item : cartItems) {
                ProductModel product = item.getProduct();
                stmt.setInt(1, orderId);
                stmt.setInt(2, product.getProductId());
                stmt.setInt(3, item.getProductQuantity());
                stmt.setDouble(4, product.getPrice());
                stmt.addBatch();
            }
            
            if (!executeBatchSuccessfully(stmt)) {
                throw new SQLException("Failed to insert order items");
            }
        }
    }
    /**
     * Updates product quantities  after an order
     * @param cart cart containing items to update 
     * @return true if successful, false otherwise
     * @throws SQLException if database operations fail
     */
    private boolean updateProductQuantities(CartModel cart) throws SQLException {
        String sql = "UPDATE product SET quantity = quantity - ? WHERE product_id = ? AND quantity >= ?";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            for (CartItemModel item : cart.getItems()) {
                stmt.setInt(1, item.getProductQuantity());
                stmt.setInt(2, item.getProduct().getProductId());
                stmt.setInt(3, item.getProductQuantity()); // Ensure sufficient quantity
                stmt.addBatch();
            }
            return executeBatchSuccessfully(stmt);
        }
    }

    /**
     * Clears a user's cart after successful order processing
     * @param cartId  ID of the cart 
     * @return true if successful, false otherwise
     * @throws SQLException if database operations fail
     */
    private boolean clearUserCart(int cartId) throws SQLException {
        try {
            deleteCartItems(cartId);
            resetCartTotals(cartId);
            return true;
        } catch (SQLException e) {
            throw e;
        }
    }

    // Helper methods
    /**
     * Executes a batch operation and verifies whether  all statements succeeded or not 
     * @param stmt 
     * @return true if all batch operations succeeded
     * @throws SQLException if database operations fail
     */
    private boolean executeBatchSuccessfully(PreparedStatement stmt) throws SQLException {
        int[] results = stmt.executeBatch();
        for (int result : results) {
            if (result != PreparedStatement.SUCCESS_NO_INFO && result <= 0) {
                return false;
            }
        }
        return true;
    }
    /**
     * Validates that a payment method is provided
     * @param paymentMethod  payment method that need to be  validated
     * @throws IllegalArgumentException if payment method is invalid
     */
    private void validatePaymentMethod(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new IllegalArgumentException("Payment method cannot be null or empty");
        }
    }
    /**
     * Converts cart items to order items
     * @param cartItems It is list of CartItemModel objects
     * @return  The list of converted OrderItemModel objects
     */
    private List<OrderItemModel> convertCartItemsToOrderItems(List<CartItemModel> cartItems) {
        List<OrderItemModel> orderItems = new ArrayList<>();
        for (CartItemModel cartItem : cartItems) {
            ProductModel product = cartItem.getProduct();
            OrderItemModel orderItem = new OrderItemModel();
            orderItem.setProductId(product.getProductId());
            orderItem.setQuantity(cartItem.getProductQuantity());
            orderItem.setPriceAtOrder(product.getPrice());
            orderItem.setProduct(product);
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    // Mapping methods
    /**
     * Maps a ResultSet to a CartModel
     * @param rs  It is the  ResultSet to map from
     * @param userId It is the ID of the user who owns the cart
     * @return  mapped CartModel
     * @throws SQLException if database operations fail
     */
    private CartModel mapCart(ResultSet rs, int userId) throws SQLException {
        CartModel cart = new CartModel();
        cart.setCartId(rs.getInt("cart_id"));
        
        UserModel user = new UserModel();
        user.setId(userId);
        cart.setUser(user);
        
        cart.setTotalItems(rs.getInt("total_items"));
        cart.setTotalPrice(rs.getDouble("total_price"));
        cart.setCreatedAt(rs.getDate("createdAt").toLocalDate());
        return cart;
    }
    /**
     * Maps a ResultSet to a CartItemModel
     * @param rs It is the ResultSet to map from
     * @return The mapped CartItemModel
     * @throws SQLException if database operations fail
     */
    private CartItemModel mapCartItem(ResultSet rs) throws SQLException {
        CartItemModel item = new CartItemModel();
        item.setCartItemId(rs.getInt("cartitem_id"));
        item.setProductQuantity(rs.getInt("productQuantity"));
        item.setProduct(mapProduct(rs));
        return item;
    }
    /**
     * Maps a ResultSet to an OrderModel
     * @param rs It is the  ResultSet to map from
     * @return The mapped OrderModel
     * @throws SQLException if database operations fail
     */
    private OrderModel mapOrder(ResultSet rs) throws SQLException {
        OrderModel order = new OrderModel();
        order.setOrderId(rs.getInt("order_id")); 
        Date sqlDate = rs.getDate("order_date");
        if (sqlDate != null) {
        	order.setOrderDate(rs.getDate("order_date").toLocalDate());
        }
        
        order.setOrderStatus(rs.getString("order_status"));
        order.setPaymentMethod(rs.getString("paymentMethod"));
        return order;
    }
    /**
     * Maps a ResultSet to an OrderItemModel
     * @param rs It is the  ResultSet to map from
     * @return The mapped OrderModel
     * @throws SQLException if database operations fail
     */
    private OrderItemModel mapOrderItem(ResultSet rs) throws SQLException {
        OrderItemModel item = new OrderItemModel();
        item.setOrderItemId(rs.getInt("order_item_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setPriceAtOrder(rs.getDouble("price_at_order"));
        item.setProduct(mapProduct(rs));
        return item;
    }
    /**
     * Maps a ResultSet to a ProductModel
     * @param rs It is the  ResultSet to map from
     * @return The mapped OrderModel
     * @throws SQLException if database operations fail
     */
    private ProductModel mapProduct(ResultSet rs) throws SQLException {
        ProductModel product = new ProductModel();
        product.setProductId(rs.getInt("product_id"));
        product.setProductName(rs.getString("productName"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getDouble("price"));
        product.setQuantity(rs.getInt("quantity"));
        product.setCategory(rs.getString("category"));
        product.setProductImage(rs.getString("productImage"));
        return product;
    }
    /**
     * Deletes all items from a cart
     * @param cartId It is the  ID of the cart to clear
     * @throws SQLException if database operations fail
     */
    private void deleteCartItems(int cartId) throws SQLException {
        String sql = "DELETE FROM cartitem WHERE cart_id = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            stmt.executeUpdate();
        }
    }
    /**
     * Resets a cart's totals to zero
     * @param cartId It is the cart ID of the cart to reset
     * @throws SQLException if database operations fail
     */
    private void resetCartTotals(int cartId) throws SQLException {
        String sql = "UPDATE cart SET total_items = 0, total_price = 0 WHERE cart_id = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Retrieves all order  in the system with user and address information
     * @return The list of all OrderModel objects
     * @throws SQLException if database operations fail
     */
    public List<OrderModel> getAllOrders() throws SQLException {
        String orderSql = "SELECT o.*, u.first_name, u.last_name, a.name as address " +
                         "FROM order_table o " +
                         "JOIN user u ON o.user_id = u.user_id " +
                         "LEFT JOIN address a ON u.address_id = a.address_id " +
                         "ORDER BY o.order_date DESC";
        
        String itemsSql = "SELECT oi.*, p.* FROM order_items oi " +
                         "JOIN product p ON oi.product_id = p.product_id " +
                         "WHERE oi.order_id = ?";
        
        List<OrderModel> orders = new ArrayList<>();
        
        try (PreparedStatement orderStmt = dbConn.prepareStatement(orderSql)) {
            try (ResultSet orderRs = orderStmt.executeQuery()) {
                while (orderRs.next()) {
                    OrderModel order = mapOrder(orderRs);
                    order.setUser(mapOrderUser(orderRs));
                    order.setItems(getOrderItems(order.getOrderId(), itemsSql));
                    
                    // Calculate total price
                    double totalPrice = order.getItems().stream()
                        .mapToDouble(item -> item.getPriceAtOrder() * item.getQuantity())
                        .sum();
                    order.setTotalPrice(totalPrice);
                    
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    /**
     * Updates an order's status
     * @param orderId It is  ID of the order to update
     * @param newStatus It is the new status to set
     * @return true if update was successful, false otherwise
     * @throws SQLException if database operations fail
     */
    public boolean updateOrderStatus(int orderId, String newStatus) throws SQLException {
        String sql = "UPDATE order_table SET order_status = ? WHERE order_id = ?";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, orderId);
            int rowsAffected = stmt.executeUpdate();
            dbConn.commit();
            System.out.println("Rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            dbConn.rollback();
            System.err.println("Update failed: " + e.getMessage());
            throw e;
        }
    }
    /**
     * Maps a ResultSet to a UserModel with basic info and address
     * @param rs It is the ResultSet to map from
     * @return The mapped OrderModel
     * @throws SQLException if database operations fail
     */
    private UserModel mapOrderUser(ResultSet rs) throws SQLException {
        UserModel user = new UserModel();
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        
        // Create and set AddressModel with just the name
        AddressModel address = new AddressModel();
        address.setName(rs.getString("address"));
        
        user.setAddress(address);
        return user;
    }
    /**
     * Retrieves the most recent orders (limited to 3)
     * @return The list of recent OrderModel objects, or return null if error occurs
     */
    public List<OrderModel> getRecentOrders() {
        if (isConnectionError) {
            System.out.println("Connection Error!");
            return null;
        }

        // SQL query to fetch recent orders with customer details and address
        String query = "SELECT o.order_id, o.order_status, u.first_name, u.last_name, u.address_id, a.name as name " +
                       "FROM order_table o " +
                       "JOIN user u ON o.user_id = u.user_id " +
                       "LEFT JOIN address a ON u.address_id = a.address_id " +
                       "ORDER BY o.order_date DESC LIMIT 3";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            ResultSet result = stmt.executeQuery();
            List<OrderModel> orderList = new ArrayList<>();

            while (result.next()) {
                // Create AddressModel
                AddressModel addressModel = new AddressModel();
                addressModel.setAddressId(result.getInt("address_id"));
                addressModel.setName(result.getString("name"));

                // Create UserModel with basic info and address
                UserModel userModel = new UserModel();
                userModel.setFirstName(result.getString("first_name"));
                userModel.setLastName(result.getString("last_name"));
                userModel.setAddress(addressModel);

                // Create OrderModel
                OrderModel orderModel = new OrderModel();
                orderModel.setOrderId(result.getInt("order_id"));
                orderModel.setOrderStatus(result.getString("order_status"));
                orderModel.setUser(userModel);

                orderList.add(orderModel);
            }
            return orderList;
        } catch (SQLException e) {
            // Log and handle exceptions related to order query execution
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Deletes an order from the system
     * Only allows deletion if order is completed or cancelled
     * @param orderId   ID of the order to delete
     * @return true if order deletion was successful, false otherwise
     * @throws SQLException if database operations fail
     */
    public boolean deleteOrder(int orderId) throws SQLException {
        try {
            // First check if the order is completed or canceled
            String checkStatusSql = "SELECT order_status FROM order_table WHERE order_id = ?";
            String orderStatus = null;
            
            try (PreparedStatement checkStmt = dbConn.prepareStatement(checkStatusSql)) {
                checkStmt.setInt(1, orderId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        orderStatus = rs.getString("order_status");
                    } else {
                        // Order doesn't exist
                        return false;
                    }
                }
            }
            
            // Only proceed if order is completed or canceled
            if (orderStatus == null || 
                (!orderStatus.equalsIgnoreCase("Completed") && 
                 !orderStatus.equalsIgnoreCase("Cancelled"))) {
                return false;
            }
            
            // First delete order items
            String deleteItemsSql = "DELETE FROM order_items WHERE order_id = ?";
            try (PreparedStatement itemsStmt = dbConn.prepareStatement(deleteItemsSql)) {
                itemsStmt.setInt(1, orderId);
                itemsStmt.executeUpdate();
            }
            
            // Then delete the order
            String deleteOrderSql = "DELETE FROM order_table WHERE order_id = ?";
            try (PreparedStatement orderStmt = dbConn.prepareStatement(deleteOrderSql)) {
                orderStmt.setInt(1, orderId);
                int affectedRows = orderStmt.executeUpdate();
                dbConn.commit();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            dbConn.rollback();
            throw e;
        }
    }
}