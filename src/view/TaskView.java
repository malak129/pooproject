package view;


import java.io.IOException;

import javafx.scene.Node;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Controller.TaskController;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import model.Task;
import model.User;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;



public class TaskView{
	
	  
	@FXML
	private VBox taskContainer;
	@FXML private ComboBox<String> sortComboBox;
	@FXML private ComboBox<String> categoryComboBox;
	@FXML private ComboBox<String> priorityComboBox;
	@FXML
    private Label quoteText;
	@FXML
	private PieChart pieChart;
	@FXML
	private Label usernameLabel;

	@FXML
	private Label dateLabel;

	    private TaskController taskController;
	    private int userId;
	    private String username;
	    @FXML
	    private Label scoreLabel;


	    
	    public void initialize() {
	    	  taskController = new TaskController();
	    	// Populate combo boxes
	    	    sortComboBox.getItems().addAll("Due Date ↑", "Due Date ↓");
	    	    categoryComboBox.getItems().addAll("All", "Work", "Personal", "Study", "Health", "Shopping", "Others");
	    	    priorityComboBox.getItems().addAll("All", "Low", "Medium", "High");

	    	    // Set default selections
	    	    categoryComboBox.setValue("All");
	    	    priorityComboBox.setValue("All");

	    	    // Add listeners to refresh task list on change
	    	    sortComboBox.setOnAction(e -> displayAllTasks());
	    	    categoryComboBox.setOnAction(e -> displayAllTasks());
	    	    priorityComboBox.setOnAction(e -> displayAllTasks());
	    	    showUncompletedTasksPieChart();
	    	    quoteText.setText(taskController.getRandomQuote());

	    }
	    
	    public void updateScore(int newScore) {
	        scoreLabel.setText("Score: " + newScore);
	    }
	    public void displayAllTasks() {
    	 taskContainer.getChildren().clear(); // clear previous cards
	    	 try {
	    	    
	    	 List<Task> tasks = taskController.getTasksByUserId(userId);
	    	// Filter by category
	         String selectedCategory = categoryComboBox.getValue();
	         if (selectedCategory != null && !"All".equals(selectedCategory)) {
	             tasks = tasks.stream()
	                 .filter(t -> selectedCategory.equalsIgnoreCase(t.getCategory()))
	                 .collect(Collectors.toList());
	         }

	         // Filter by priority
	         String selectedPriority = priorityComboBox.getValue();
	         if (selectedPriority != null && !"All".equals(selectedPriority)) {
	             tasks = tasks.stream()
	                 .filter(t -> selectedPriority.equalsIgnoreCase(t.getPriority()))
	                 .collect(Collectors.toList());
	         }

	         // Sort by date
	         String sortOption = sortComboBox.getValue();
	         if ("Due Date ↑".equals(sortOption)) {
	             tasks.sort(Comparator.comparing(Task::getDueDate));
	         } else if ("Due Date ↓".equals(sortOption)) {
	             tasks.sort(Comparator.comparing(Task::getDueDate).reversed());
	         }
	         for (Task task : tasks) {
	            
	             FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskCard.fxml"));
	             AnchorPane card = loader.load();

	             TaskCardController cardController = loader.getController();
	             cardController.setTask(task);
	             cardController.setController(taskController); 
	             cardController.setRefreshCallback(() -> {
	            	    displayAllTasks();
	            	    showUncompletedTasksPieChart();
	            	});


	             taskContainer.getChildren().add(card);

	         }

	     } catch (Exception e) {
	         e.printStackTrace();
	         showAlert("Error", "Failed to load tasks: " + e.getMessage());
	     }
	    }
	    public void setUserId(int userId,String username,int score) {
	        this.userId = userId;
	        this.username = username;
	        scoreLabel.setText("yourScore: "+ score);
	        taskController.updatePendingTasksToInProgress(userId);
	        displayAllTasks(); 
	        showUncompletedTasksPieChart();
	        quoteText.setText(taskController.getRandomQuote());
	        if (usernameLabel != null) {
	            usernameLabel.setText("Hello, " + username + "!");
	        }

	        // Set today’s date
	        if (dateLabel != null) {
	            LocalDate today = LocalDate.now();
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);
	            dateLabel.setText(today.format(formatter));
	        }
	    }
	    public void displayTask(List<Task> tasks)throws SQLException, IOException {
			// TODO Auto-generated method stub
			
		}
	    public void showUncompletedTasksPieChart() {
	        Map<String, Integer> data = taskController.getUncompletedTaskCountsByCategoryLastMonth(userId);
	        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

	        for (Map.Entry<String, Integer> entry : data.entrySet()) {
	            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
	        }
	       
	        pieChart.setData(pieChartData);
	        pieChart.setTitle("Tâches non complétées ");
	    }
		
		@FXML
		private void openAddTaskDialog() {
			Stage dialogStage = new Stage();
		    dialogStage.initModality(Modality.APPLICATION_MODAL);
		    dialogStage.setTitle("Add Task");

		    VBox vbox = new VBox(10);
		    vbox.setPadding(new Insets(15));
		    vbox.setStyle("-fx-background-color: #f9f9f9; -fx-background-radius: 10;");

		    TextField titleField = new TextField();
		    titleField.setPromptText("Title");
		    titleField.setStyle("-fx-font-size: 14px; -fx-background-radius: 5; -fx-padding: 5;");

		    TextField descField = new TextField();
		    descField.setPromptText("Description");
		    descField.setStyle("-fx-font-size: 14px; -fx-background-radius: 5; -fx-padding: 5;");

		    DatePicker dueDatePicker = new DatePicker();
		    dueDatePicker.setPromptText("Due Date");
		    dueDatePicker.setStyle("-fx-font-size: 14px;");

		    ComboBox<String> priorityCombo = new ComboBox<>();
		    priorityCombo.getItems().addAll("Low", "Medium", "High");
		    priorityCombo.setPromptText("Priority");
		    priorityCombo.setStyle("-fx-font-size: 14px;");

		    ComboBox<String> categoryCombo = new ComboBox<>();
		    categoryCombo.getItems().addAll(
		        "Work", "Personal", "Study", "Health", "Shopping", "Others"
		    );
		    categoryCombo.setPromptText("Category");
		    categoryCombo.setStyle("-fx-font-size: 14px;");

		    Button saveButton = new Button("Save");
		    saveButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold;");

		    Button cancelButton = new Button("Cancel");
		    cancelButton.setOnAction(e -> dialogStage.close());

		    saveButton.setOnAction(e -> {
		        String title = titleField.getText().trim();
		        String desc = descField.getText().trim();
		        LocalDate dueDate = dueDatePicker.getValue();
		        String priority = priorityCombo.getValue();
		        String category = categoryCombo.getValue();

		        if (title.isEmpty() || dueDate == null || priority == null || category == null) {
		            showAlert("Error", "Please fill all fields correctly.");
		            return;
		        }

		        String status = computeStatusBasedOnDueDate(dueDate);

		        // Make sure taskController and userId are properly initialized
		        taskController.addTask(title, desc, dueDate, status, userId, priority, category);

		        displayAllTasks();
		        showUncompletedTasksPieChart();
		        dialogStage.close();
		    });

		    HBox buttonBox = new HBox(10, saveButton, cancelButton);
		    buttonBox.setAlignment(Pos.CENTER_RIGHT);

		    vbox.getChildren().addAll(
		        new Label("Add New Task"),
		        titleField,
		        descField,
		        dueDatePicker,
		        priorityCombo,
		        categoryCombo,
		        buttonBox
		    );

		    Scene scene = new Scene(vbox, 350, 350);
		    dialogStage.setScene(scene);
		    dialogStage.showAndWait();
	    }

	    private void showAlert(String title, String message) {
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle(title);
	        alert.setContentText(message);
	        alert.showAndWait();
	    
	}
	    
	    private String computeStatusBasedOnDueDate(LocalDate dueDate) {
	        LocalDate today = LocalDate.now();
	        if (dueDate.isBefore(today)) {
	            return "In Progress";
	        } else {
	            return "Pending";
	        }
	    }
	   

}

