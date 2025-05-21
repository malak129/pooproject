package view;
	import javafx.fxml.FXML;
	import javafx.fxml.FXMLLoader;
	import javafx.scene.layout.AnchorPane;
	import javafx.scene.layout.VBox;
	import model.Task;
	import model.TaskDAO;

	import java.time.LocalDate;
	import java.util.List;

import Controller.TaskController;

	public class TodayTaskController {

		@FXML
	    private VBox todayTaskContainer;

	    private TaskDAO taskDAO = new TaskDAO();
	    private int userId;

	    public void setUserId(int userId) {
	        this.userId = userId;
	    }
	    private TaskController taskController;

	    public void setTaskController(TaskController taskController) {
	        this.taskController = taskController;
	    }

	    public void loadTodaysTasks() {
	        try {
	        	 System.out.println("Loading tasks for user ID: " + userId);
	            List<Task> tasks = taskDAO.getTodaysTasksForUser(userId);
	            todayTaskContainer.getChildren().clear();

	            for (Task task : tasks) {
	                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/taskcard.fxml"));
	                AnchorPane taskCard = loader.load();

	                TaskCardController cardController = loader.getController();
	                cardController.setTask(task);
	                cardController.setController(taskController);
	                todayTaskContainer.getChildren().add(taskCard);
	                cardController.setRefreshCallback(() -> loadTodaysTasks());
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	
}
