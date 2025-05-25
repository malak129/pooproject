package Controller;

import java.util.List;


import java.util.Map;
import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Task;
import model.TaskDAO;
import model.User;
import model.UserDAO;
import view.NotificationController;
import view.TaskView;
import view.TodayTaskController;

public class TaskController {
	
	 private TaskDAO taskDAO;
	    private TaskView taskView;

	    public TaskController() {
	        this.taskDAO = new TaskDAO();
	        this.taskView = new TaskView();
	    }

	   

	    public void addTask(String title, String description, LocalDate dueDate, String status, int userId, String priority, String category) {
	        Task task = new Task(title, description, dueDate, status, userId,priority,category);
	        try {
	            taskDAO.addTask(task);
	            System.out.println("Tâche ajoutée avec succès.");
	        } catch (SQLException | IOException e) {
	            System.err.println("Erreur lors de l'ajout de la tâche : " + e.getMessage());
	        }
	    }

	    // Méthodes pour modifier, supprimer, filtrer, trier, etc.
	    public void updateTaskStatus(int taskId, String newStatus) {
	        try {
	            Task task = getTaskById(taskId);
	            System.out.print(taskId);
	            if (task != null) {
	                task.setStatus(newStatus);
	                taskDAO.updateTaskstatus(taskId,newStatus);
	                int points = getScoreForPriority(task.getPriority());
	                UserDAO userDAO = new UserDAO();
	                // Get current score
	                User user = userDAO.getUserById(task.getUserId());
	                int newScore = user.getScore() + points;
	                this.score = newScore;
	                this.UserId( this.userId, this.username, this.score);
	                // Update DB
	                userDAO.updateUserScore(user.getId(), newScore);
	                System.out.println("Statut de la tâche mis à jour avec succès.");
	            } else {
	                System.out.println("Tâche non trouvée.");
	            }
	        } catch (SQLException | IOException e) {
	            System.err.println("Erreur lors de la mise à jour du statut de la tâche : " + e.getMessage());
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
	            return taskDAO.getTasksByUserId(userId); 
	        } catch (SQLException | IOException e) {
	            e.printStackTrace();
	            return List.of();
	        }
	    }
	    public void deleteTask(int taskId) {
	        try {
	        	 if (taskDAO == null) {
	                 System.err.println(" taskDAO is null!");
	                 return;
	             }
	            taskDAO.deleteTask(taskId);
	            System.out.println("Tâche supprimée avec succès.");
	        } catch (SQLException | IOException e) {
	            System.err.println("Erreur lors de la suppression de la tâche : " + e.getMessage());
	        }
	    }

	  
	  
	    @FXML
	    private AnchorPane dynamicContent;
	    
	    private TaskView taskViewController;
	    
	    @FXML
	    private void handleNotification(ActionEvent event) {
	    	 try {
	    	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/notification.fxml"));
	    	        AnchorPane notificationPane = loader.load();

	    	        // Get controller
	    	        NotificationController controller = loader.getController();

	    	        controller.setUserId(userId); 
	    	        controller.setTaskController(this);
	    	        controller.loadOverdueInProgressTasks();

	    	        dynamicContent.getChildren().setAll(notificationPane);
	    	    } catch (IOException e) {
	    	        e.printStackTrace();
	    	    }
	    }
	    public void updateTask(Task task) {
	        try {
	            taskDAO.updateTask(task);
	            System.out.println("Task updated successfully.");
	        } catch (SQLException e) {
	            System.err.println("Error updating task: " + e.getMessage());
	        }
	    }
	    public Map<String, Integer> getUncompletedTaskCountsByCategoryLastMonth(int userId) {
	        return taskDAO.getUncompletedTaskCountsByCategoryLastMonth(userId);
	    }
	    
	    @FXML
	    private void handleMytasks(ActionEvent event) {
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TaskView.fxml"));
	            AnchorPane notificationPane = loader.load();
	            TaskView controller = loader.getController();
	            controller.updateScore(this.score);
	            controller.setUserId(this.userId,this.username,this.score);
	            dynamicContent.getChildren().setAll(notificationPane);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    @FXML
	    private BorderPane  mainPane;  

	    @FXML
	    private void handleTodayTasks() {
	    	try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/todaytask.fxml"));
	            AnchorPane todayTasksContent = loader.load();

	            TodayTaskController controller = loader.getController();
	            controller.setUserId(userId);
	            controller.setTaskController(this);
	            controller.loadTodaysTasks();  

	            dynamicContent.getChildren().setAll(todayTasksContent);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    
	    @FXML
	    public void initialize() {
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TaskView.fxml"));
	            AnchorPane taskViewRoot = loader.load();
	            taskViewController = loader.getController();
	            dynamicContent.getChildren().setAll(taskViewRoot);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	        
	    private int userId;
	    private String username;
	    private int score;

	    public void UserId(int userId,String username,int score) {
	    	 this.userId = userId;
	    	 this.username = username;
	    	 this.score = score;
	    	    if (taskViewController != null) {
	    	        taskViewController.setUserId(userId,username,score);
	    	        
	    	    } else {
	    	        System.out.println("taskViewController is null");
	    	    }
	    }
	    
	    
	    private final static List<String> quotes = List.of(
	            "Stay focused. Stay positive. Get it done!",
	            "Little progress each day adds up to big results.",
	            "Don’t watch the clock; do what it does. Keep going.",
	            "You’ve got this! One task at a time.",
	            "Your only limit is you.",
	            "Push yourself, because no one else is going to do it for you.",
	            "It always seems impossible until it's done.",
	            "The secret of getting ahead is getting started.",
	            "Success is the sum of small efforts repeated day in and day out.",
	            "Make each day your masterpiece.",
	            "Be stronger than your excuses.",
	            "Discipline is the bridge between goals and accomplishment.",
	            "Dream it. Wish it. Do it.",
	            "Don’t limit your challenges. Challenge your limits.",
	            "Keep going. Everything you need will come to you.",
	            "The key to success is to focus on goals, not obstacles.",
	            "You don’t have to be great to start, but you have to start to be great.",
	            "Every day is a chance to be better.",
	            "Action is the foundational key to all success.",
	            "Success doesn’t come to you. You go to it."
	        );

	        public  String getRandomQuote() {
	            SecureRandom random = new SecureRandom();
	            return quotes.get(random.nextInt(quotes.size()));
	        }
	    
	        @FXML
	        private void handleLogout(ActionEvent event) {
	            try {
	                // Clear session variables if needed
	                this.userId = 0;
	                this.username = null;

	                // Load login page
	                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserView.fxml"));
	                Parent loginRoot = loader.load();

	                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	                stage.setScene(new Scene(loginRoot, 663, 464));
	                stage.setTitle("Login");
	                stage.show();
	                
	                System.out.println("User logged out and returned to login screen.");
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }

	        public void updatePendingTasksToInProgress(int userId) {
	            try {
	                List<Task> tasks = taskDAO.getTasksByUserId(userId);
	                LocalDate today = LocalDate.now();

	                for (Task task : tasks) {
	                    if (task.getStatus().equalsIgnoreCase("Pending") &&
	                        (task.getDueDate().isEqual(today) || task.getDueDate().isBefore(today))) {

	                        task.setStatus("In Progress");
	                        taskDAO.updateTaskstatus(task.getId(), "In Progress");
	                        System.out.println(" Task ID " + task.getId() + " status updated to In Progress.");
	                    }
	                }
	            } catch (SQLException | IOException e) {
	                System.err.println(" Error updating task statuses: " + e.getMessage());
	            }
	        }
	        public int getScoreForPriority(String priority) {
	            return switch (priority.toLowerCase()) {
	                case "high" -> 15;
	                case "medium" -> 10;
	                case "low" -> 5;
	                default -> 0;
	            };
	        }
}
