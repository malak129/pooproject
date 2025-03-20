package view;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import Controller.TaskController;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.Task;



public class TaskView {
	
	 @FXML
	    private TableView<Task> taskTable;

	    @FXML
	    private TableColumn<Task, String> titleColumn;

	    @FXML
	    private TableColumn<Task, String> descriptionColumn;

	    @FXML
	    private TableColumn<Task, LocalDate> dueDateColumn;

	    @FXML
	    private TableColumn<Task, String> statusColumn;
	    
	    private TaskController taskController;
	    private int userId;

	    public void initialize() {
	        taskController = new TaskController();
	        userId = 1; // Remplacez par l'ID de l'utilisateur connecté
	        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
	        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
	        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
	        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
	    }

	    @FXML
	    public void displayAllTasks() {
	        List<Task> tasks = taskController.getTasksByUserId(userId);
	        ObservableList<Task> observableTasks = FXCollections.observableArrayList(tasks);
	        taskTable.setItems(observableTasks);
	    }

		
		public void displayTask(List<Task> tasks)throws SQLException, IOException {
			// TODO Auto-generated method stub
			
		}
}
