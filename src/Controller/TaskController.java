package controller;

import java.util.List;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import model.Task;
import model.TaskDAO;
import view.TaskView;

public class TaskController {
	
	 private TaskDAO taskDAO;
	    private TaskView taskView;

	    public TaskController() {
	        this.taskDAO = new TaskDAO();
	        this.taskView = new TaskView();
	    }

	    public void displayTasks(int userId) {
	        try {
	            List<Task> tasks = taskDAO.getTasksByUserId(userId);
	            taskView.displayTask(tasks);
	        } catch (SQLException | IOException e) {
	            System.err.println("Erreur lors de la rÃ©cupÃ©ration des tÃ¢ches : " + e.getMessage());
	        }
	    }

	    public void addTask(String title, String description, LocalDate dueDate, String status, int userId) {
	        Task task = new Task(title, description, dueDate, status, userId);
	        try {
	            taskDAO.addTask(task);
	            System.out.println("TÃ¢che ajoutÃ©e avec succÃ¨s.");
	        } catch (SQLException | IOException e) {
	            System.err.println("Erreur lors de l'ajout de la tÃ¢che : " + e.getMessage());
	        }
	    }

	    // MÃ©thodes pour modifier, supprimer, filtrer, trier, etc.
	    public void updateTaskStatus(int taskId, String newStatus) {
	        try {
	            Task task = getTaskById(taskId);
	            if (task != null) {
	                task.setStatus(newStatus);
	                taskDAO.updateTask(task);
	                System.out.println("Statut de la tÃ¢che mis Ã  jour avec succÃ¨s.");
	            } else {
	                System.out.println("TÃ¢che non trouvÃ©e.");
	            }
	        } catch (SQLException | IOException e) {
	            System.err.println("Erreur lors de la mise Ã  jour du statut de la tÃ¢che : " + e.getMessage());
	        }
	    }

	    public Task getTaskById(int taskId) throws SQLException, IOException {
	    	 
	        List<Task> tasks = taskDAO.getAllTasks();
	        for (Task task : tasks) {
	            if (task.getId() == taskId) {
	                return task;
	            }
	        }
	        return null;
	    }
	    public List<Task> getTasksByUserId(int userId) {
	        try {
	            return taskDAO.getTasksByUserId(userId); // Ensure this method exists in TaskDAO
	        } catch (SQLException | IOException e) {
	            e.printStackTrace();
	            return List.of(); // Return an empty list in case of an error
	        }
	    }
	    public void deleteTask(int taskId) {
	        try {
	            taskDAO.deleteTask(taskId);
	            System.out.println("TÃ¢che supprimÃ©e avec succÃ¨s.");
	        } catch (SQLException | IOException e) {
	            System.err.println("Erreur lors de la suppression de la tÃ¢che : " + e.getMessage());
	        }
	    }

	    public void filterTasksByStatus(int userId, String status) {
	        try {
	            List<Task> tasks = taskDAO.getTasksByStatus(userId, status);
	            taskView.displayTask(tasks);
	        } catch (SQLException | IOException e) {
	            System.err.println("Erreur lors du filtrage des tÃ¢ches : " + e.getMessage());
	        }
	    }

	    public void sortTasksByDueDate(int userId) {
	        try {
	            List<Task> tasks = taskDAO.getTasksSortedByDueDate(userId);
	            taskView.displayTask(tasks);
	        } catch (SQLException | IOException e) {
	            System.err.println("Erreur lors du tri des tÃ¢ches : " + e.getMessage());
	        }
	    }

	    
}
