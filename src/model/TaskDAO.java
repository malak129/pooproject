package model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                        rs.getDate("due_date").toLocalDate(), rs.getString("status"), rs.getInt("user_id")));
            }
        }
        return tasks;
    }

    public void addTask(Task task) throws SQLException, IOException {
        String sql = "INSERT INTO tasks (title, description, due_date, status, user_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setDate(3, java.sql.Date.valueOf(task.getDueDate()));
            pstmt.setString(4, task.getStatus());
            pstmt.setInt(5, task.getUserId());
            pstmt.executeUpdate();
        }
    }

	public void updateTask(Task task) {
		// TODO Auto-generated method stub
		
	}

	public List<Task> getAllTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteTask (int taskId)throws SQLException, IOException {
		// TODO Auto-generated method stub
		
	}

	public List<Task> getTasksByStatus(int userId, String status) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Task> getTasksSortedByDueDate(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

   
}