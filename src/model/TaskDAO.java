package model;

import java.io.IOException;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskDAO {
	public List<Task> getTasksByUserId(int userId) throws SQLException, IOException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE user_id = ?";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tasks.add(new Task(rs.getInt("id"), rs.getString("title"), rs.getString("description"),
                        rs.getDate("due_date").toLocalDate(), rs.getString("status"), rs.getInt("user_id"),rs.getString("priority"),
                        rs.getString("category")));
            }
        }
        return tasks;
    }

    public void addTask(Task task) throws SQLException, IOException {
        String sql = "INSERT INTO tasks (title, description, due_date, status, user_id, priority, category) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setDate(3, java.sql.Date.valueOf(task.getDueDate()));
            pstmt.setString(4, task.getStatus());
            pstmt.setInt(5, task.getUserId());
            pstmt.setString(6, task.getPriority());
            pstmt.setString(7, task.getCategory());
            pstmt.executeUpdate();
        }
    }
    
    public List<Task> getAllTasks()throws SQLException{
    	 List<Task> tasks = new ArrayList<>();
    	    try {
    	        Connection conn = DataConnection.getConnection();
    	        String sql = "SELECT id, title, description, due_date, status, user_id, priority, category FROM tasks";
    	        try (PreparedStatement stmt = conn.prepareStatement(sql);
    	             ResultSet rs = stmt.executeQuery()) {
    	            while (rs.next()) {
    	                int id = rs.getInt("id");
    	                String title = rs.getString("title");
    	                String description = rs.getString("description");
    	                LocalDate dueDate = rs.getDate("due_date").toLocalDate();
    	                String status = rs.getString("status");
    	                int userId = rs.getInt("user_id");
    	                String priority = rs.getString("priority");
    	                String category = rs.getString("category");
    	                Task task = new Task(id, title, description, dueDate, status, userId, priority, category);
    	                tasks.add(task);
    	            }
    	        }
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	       
    	    }
    	    return tasks;
    }

	public void updateTaskstatus(int taskId, String newStatus) {
		 String sql = "UPDATE tasks SET status = ? WHERE id = ?";

		    try (Connection conn = DataConnection.getConnection();
		         PreparedStatement stmt = conn.prepareStatement(sql)) {

		        stmt.setString(1, newStatus);
		        stmt.setInt(2, taskId);

		        stmt.executeUpdate();
		        System.out.println(" Task status updated successfully.");
		    } catch (SQLException | IOException e) {
		        System.err.println(" Failed to update task status: " + e.getMessage());
		    }
	}
	
	  public void updateTask(Task task) throws SQLException {
	        String sql = "UPDATE tasks SET title = ?, description = ?, due_date = ?, status = ?, priority = ?, category = ? WHERE id = ?";
	        try (Connection conn = DataConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setString(1, task.getTitle());
	            stmt.setString(2, task.getDescription());
	            stmt.setDate(3, Date.valueOf(task.getDueDate()));
	            stmt.setString(4, task.getStatus());
	            stmt.setString(5, task.getPriority());
	            stmt.setString(6, task.getCategory());
	            stmt.setInt(7, task.getId());
	            stmt.executeUpdate();
	         } catch (IOException e) {
    	        e.printStackTrace();
    	       
    	    }
	    }


	public void deleteTask (int taskId)throws SQLException, IOException {
		 String sql = "DELETE FROM tasks WHERE id = ?";

	        // Connexion à la base de données
	        try (Connection conn = DataConnection.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {

	            
	            pstmt.setInt(1, taskId);
	            int affectedRows = pstmt.executeUpdate();

	            if (affectedRows > 0) {
	                System.out.println(" Task deleted successfully with ID: " + taskId);
	            } else {
	                System.out.println(" No task found with ID: " + taskId);
	            }

	        } catch (SQLException e) {
	            System.err.println("Error deleting task: " + e.getMessage());
	            throw e;
	        }
		
	}

	public List<Task> getTasksByStatus(int userId, String status) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Task> getTasksSortedByDueDate(int userId) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<Task> getOverdueTasks(int userId) throws SQLException, IOException {
		 List<Task> overdueTasks = new ArrayList<>();
		    String sql = "SELECT * FROM tasks WHERE due_date < CURDATE() AND user_id = ?";
		    try (Connection conn = DataConnection.getConnection();
		         PreparedStatement stmt = conn.prepareStatement(sql)) {

		        stmt.setInt(1, userId);

		        try (ResultSet rs = stmt.executeQuery()) {
		            while (rs.next()) {
		                Task task = new Task(
		                    rs.getInt("id"),
		                    rs.getString("title"),
		                    rs.getString("description"),
		                    rs.getDate("due_date").toLocalDate(),
		                    rs.getString("status"),
		                    rs.getInt("user_id"),
		                    rs.getString("priority"),
		                    rs.getString("category")
		                );
		                overdueTasks.add(task);
		            }
		        }
		    }
		    return overdueTasks;
	}

	public Map<String, Integer> getUncompletedTaskCountsByCategoryLastMonth(int userId) {
	    Map<String, Integer> result = new HashMap<>();

	    LocalDate now = LocalDate.now();
	    LocalDate lastMonth = now.minusMonths(1);
	    int month = lastMonth.getMonthValue();
	    int year = lastMonth.getYear();

	    String sql = """
	         SELECT category, COUNT(*) AS count
        FROM tasks
        WHERE status != 'Completed'
          AND MONTH(due_date) = ?
          AND YEAR(due_date) = ?
          AND user_id = ?
        GROUP BY category
	    """;

	    try (Connection conn = DataConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setInt(1, month);
	        stmt.setInt(2, year);
	        stmt.setInt(3, userId);

	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            String category = rs.getString("category");
	            int count = rs.getInt("count");
	            result.put(category, count);
	        }

	    } catch (SQLException | IOException  e) {
	        e.printStackTrace();
	    }

	    return result;
	}

	public List<Task> getTodaysTasksForUser(int userId) {
	    List<Task> tasks = new ArrayList<>();
	    String sql = "SELECT * FROM tasks WHERE due_date = CURDATE() AND user_id = ?";

	    try (Connection conn = DataConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setInt(1, userId);

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                Task task = new Task(
	                    rs.getInt("id"),
	                    rs.getString("title"), 
	                    rs.getString("description"),
	                    rs.getDate("due_date").toLocalDate(),
	                    rs.getString("status"),
	                    rs.getInt("user_id") ,
	                    rs.getString("priority"),
	                    rs.getString("category")
	                    
	                   
	                  
	                );
	                tasks.add(task);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return tasks;
	}
   
                                                                                                                                                                                                                     
}

