package com.onez.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.onez.config.DbConfig;
import com.onez.model.ProductModel;
import com.onez.model.UserModel;

/**
 * Service class for interacting with the database to retrieve dashboard-related
 * data. This class handles database connections and performs queries to fetch
 * user information.
 */
public class DashboardService {

	private Connection dbConn;
	private boolean isConnectionError = false;

	/**
	 * Constructor that initializes the database connection. Sets the connection
	 * error flag if the connection fails.
	 */
	public DashboardService() {
		try {
			dbConn = DbConfig.getDbConnection();
		} catch (SQLException | ClassNotFoundException ex) {
			// Log and handle exceptions related to database connection
			ex.printStackTrace();
			isConnectionError = true;
		}
	}

	/**
	 * Retrieves all user information from the database.
	 * 
	 * @return A list of UserModel objects containing user data. Returns null
	 *         if there is a connection error or if an exception occurs during query
	 *         execution.
	 */
	public List<UserModel> getAllUsersInfo() {
		if (isConnectionError) {
			System.out.println("Connection Error!");
			return null;
		}

		// SQL query to fetch user details
		String query = "SELECT user_id, first_name, last_name, program_id, email, number FROM user";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			ResultSet result = stmt.executeQuery();
			List<UserModel> userList = new ArrayList<>();

			while (result.next()) {
				// SQL query to fetch program name based on program_id
				String programQuery = "SELECT program_id, name FROM program WHERE program_id = ?";
				try (PreparedStatement programStmt = dbConn.prepareStatement(programQuery)) {
					programStmt.setInt(1, result.getInt("program_id"));
					ResultSet programResult = programStmt.executeQuery();

					ProductModel programModel = new ProductModel();
					if (programResult.next()) {
						// Set program name in the ProgramModel
						programModel.setName(programResult.getString("name"));
						programModel.setProgramId(programResult.getInt("program_id"));
					}

					// Create and add UserModel to the list
					userList.add(new UserModel(result.getInt("user_id"), // User ID
							result.getString("first_name"), // First Name
							result.getString("last_name"), // Last Name
							programModel, // Associated Program
							result.getString("email"), // Email
							result.getString("number") // Phone Number
					));

					programResult.close(); // Close ResultSet to avoid resource leaks
				} catch (SQLException e) {
					// Log and handle exceptions related to program query execution
					e.printStackTrace();
					// Continue to process other users or handle this error appropriately
				}
			}
			return userList;
		} catch (SQLException e) {
			// Log and handle exceptions related to user query execution
			e.printStackTrace();
			return null;
		}
	}

	public UserModel getSpecificUserInfo(int UserId) {
		if (isConnectionError) {
			System.out.println("Connection Error!");
			return null;
		}

		// SQL query to join User and program tables
		String query = "SELECT s.User_id, s.first_name, s.last_name, s.username, s.dob, s.gender, "
				+ "s.email, s.number, s.program_id, s.image_path, "
				+ "p.name AS program_name, p.type AS program_type, p.category AS program_category " + "FROM User s "
				+ "JOIN program p ON s.program_id = p.program_id " + "WHERE s.User_id = ?";

		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setInt(1, UserId);
			ResultSet result = stmt.executeQuery();
			UserModel User = null;

			if (result.next()) {
				// Extract data from the result set
				int id = result.getInt("User_id");
				String firstName = result.getString("first_name");
				String lastName = result.getString("last_name");
				String userName = result.getString("username");
				LocalDate dob = result.getDate("dob").toLocalDate(); // Assuming dob is of type DATE in SQL
				String gender = result.getString("gender");
				String email = result.getString("email");
				String number = result.getString("number");
				String imageUrl = result.getString("image_path");

				// Create ProgramModel instance
				ProductModel program = new ProductModel();
				program.setProgramId(result.getInt("program_id"));
				program.setName(result.getString("program_name"));
				program.setType(result.getString("program_type"));
				program.setCategory(result.getString("program_category"));

				// Create UserModel instance
				User = new UserModel(id, firstName, lastName, userName, dob, gender, email, number, null, program,
						imageUrl);

				// Add the user to the list
			}
			return User;
		} catch (SQLException e) {
			// Log and handle exceptions
			e.printStackTrace();
			return null;
		}
	}

	public List<UserModel> getRecentUsers() {
		if (isConnectionError) {
			System.out.println("Connection Error!");
			return null;
		}

		// SQL query to fetch User details
		String query = "SELECT user_id, first_name, last_name, email, number "
				+ "FROM user ORDER BY user_id DESC LIMIT 3";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			ResultSet result = stmt.executeQuery();
			List<UserModel> userList = new ArrayList<>();

			while (result.next()) {

				// Create and add userModel to the list
				userList.add(new UserModel(result.getInt("user_id"), // User ID
						result.getString("first_name"), // First Name
						result.getString("last_name"), // Last Name
						result.getString("email"), // Email
						result.getString("number") // Phone Number
				));

			}
			return userList;
		} catch (SQLException e) {
			// Log and handle exceptions related to user query execution
			e.printStackTrace();
			return null;
		}
	}

	public boolean updateUser(UserModel user) {
		if (isConnectionError)
			return false;

		String updateQuery = "UPDATE user SET first_name = ?, last_name = ?, " + "username = ?, dob = ?, gender = ?,"
				+ "email = ?, number = ?, program_id = ?, image_path = ? WHERE user_id = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(updateQuery)) {
			stmt.setString(1, user.getFirstName());
			stmt.setString(2, user.getLastName());
			stmt.setString(3, user.getUserName());
			stmt.setDate(4, Date.valueOf(user.getDob()));
			stmt.setString(5, user.getGender());
			stmt.setString(6, user.getEmail());
			stmt.setString(7, user.getNumber());
			stmt.setInt(8, getProgramId(user.getProduct().getName()));
			stmt.setString(9, user.getLastName());

			stmt.setInt(10, user.getId());

			int rowsUpdated = stmt.executeUpdate();
			return rowsUpdated > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteUser(int userId) {
		if (isConnectionError)
			return false;

		String deleteQuery = "DELETE FROM user WHERE user_id = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(deleteQuery)) {
			stmt.setInt(1, userId);

			int rowsDeleted = stmt.executeUpdate();
			return rowsDeleted > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getProgramName(int id) {
		if (isConnectionError)
			return null;

		String deleteQuery = "select name from program where program_id = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(deleteQuery)) {
			stmt.setInt(1, id);

			ResultSet result = stmt.executeQuery();
			if (result.next())
				return result.getString("name");
			else
				return "";
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int getProgramId(String name) {
		if (isConnectionError)
			return -1;

		String deleteQuery = "select program_id from program where name  = ?";
		try (PreparedStatement stmt = dbConn.prepareStatement(deleteQuery)) {
			stmt.setString(1, name);

			ResultSet result = stmt.executeQuery();
			if (result.next())
				return result.getInt("program_id");
			else
				return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public String getTotalUsers() {
		if (isConnectionError) {
			return null;
		}

		String countQuery = "SELECT COUNT(*) AS total FROM user;";
		try (PreparedStatement stmt = dbConn.prepareStatement(countQuery)) {

			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				return result.getString("total");
			} else {
				return "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getComputingUsers() {
		if (isConnectionError) {
			return null;
		}

		String countQuery = "SELECT COUNT(*) AS total FROM user WHERE program_id = 1;";
		try (PreparedStatement stmt = dbConn.prepareStatement(countQuery)) {
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				return result.getString("total");
			} else {
				return "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getMultimediaUsers() {
		if (isConnectionError) {
			return null;
		}

		String countQuery = "SELECT COUNT(*) AS total FROM user WHERE program_id = 2;";
		try (PreparedStatement stmt = dbConn.prepareStatement(countQuery)) {
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				return result.getString("total");
			} else {
				return "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getNetworkingUsers() {
		if (isConnectionError) {
			return null;
		}

		String countQuery = "SELECT COUNT(*) AS total FROM user WHERE program_id = 3;";
		try (PreparedStatement stmt = dbConn.prepareStatement(countQuery)) {
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				return result.getString("total");
			} else {
				return "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}