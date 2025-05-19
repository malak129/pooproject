package view;


import java.io.IOException;
import javafx.scene.Node;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import Controller.TaskController;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import model.Task;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;



public class TaskView{
	
	  
	@FXML
	private VBox taskContainer;
	    

	    private TaskController taskController;
	    private int userId;
//
//	    @Override
//	    public void initialize(URL location, ResourceBundle resources) {
//	        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
//	        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
//	        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
//	        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
//
//	        displayAllTasks();
//	    }
	    
	    public void initialize() {
	    	  taskController = new TaskController();
	    	  displayAllTasks();

	    }
	    public void displayAllTasks() {
    	 taskContainer.getChildren().clear(); // clear previous cards
	    	 try {
	    	    
	    	 List<Task> tasks = taskController.getTasksByUserId(userId);
	         for (Task task : tasks) {
	            
	             FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskCard.fxml"));
	             AnchorPane card = loader.load();

	             TaskCardController cardController = loader.getController();
	             cardController.setTask(task);

	             taskContainer.getChildren().add(card);

	         }

	     } catch (Exception e) {
	         e.printStackTrace();
	         showAlert("Error", "Failed to load tasks: " + e.getMessage());
	     }
	    }
	    public void setUserId(int userId) {
	        this.userId = userId;
//	        taskController = new TaskController();
	        displayAllTasks(); 
	    }

//	    @FXML
//	    public void displayAllTasks() {
//	        List<Task> tasks = taskController.getTasksByUserId(userId);
//	        ObservableList<Task> observableTasks = FXCollections.observableArrayList(tasks);
//	        taskTable.setItems(observableTasks);
//	    }

		
		public void displayTask(List<Task> tasks)throws SQLException, IOException {
			// TODO Auto-generated method stub
			
		}
		
		@FXML
		private void openAddTaskDialog() {
	        Stage dialogStage = new Stage();
	        dialogStage.initModality(Modality.APPLICATION_MODAL);
	        dialogStage.setTitle("Add Task");

	        VBox vbox = new VBox(10);
	        TextField titleField = new TextField();
	        titleField.setPromptText("Title");
	        TextField descField = new TextField();
	        descField.setPromptText("Description");
	        DatePicker dueDatePicker = new DatePicker();
	        ComboBox<String> statusCombo = new ComboBox<>();
	        statusCombo.getItems().addAll("Pending", "In Progress", "Completed");
	        Button saveButton = new Button("Save");
	        
	        saveButton.setOnAction(e -> {
	            String title = titleField.getText();
	            String desc = descField.getText();
	            LocalDate dueDate = dueDatePicker.getValue();
	            String status = statusCombo.getValue();
	            
	            if (!title.isEmpty() && dueDate != null && status != null) {
	                taskController.addTask(title, desc, dueDate, status, userId);
	                displayAllTasks();
	                dialogStage.close();
	            } else {
	                showAlert("Error", "Please fill all fields correctly.");
	            }
	        });

	        vbox.getChildren().addAll( titleField,descField,dueDatePicker,statusCombo,saveButton);
	        dialogStage.setScene(new javafx.scene.Scene(vbox, 300, 200));
	        dialogStage.show();
	    }

	    private void showAlert(String title, String message) {
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle(title);
	        alert.setContentText(message);
	        alert.showAndWait();
	    
	}
	   

}

