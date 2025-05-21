package view;



import java.time.LocalDate;

import Controller.TaskController;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Task;

public class TaskCardController {

    @FXML private Text titleText;
    @FXML private Text descText;
    @FXML private Text dueDateText;
    @FXML private Text statusText;
    @FXML private Text priorityText;
    @FXML private Text categoryText;
    @FXML private Button doneButton;
    @FXML private Button deleteButton;
    
    private Task task;
    private TaskController taskController;
    private Runnable refreshCallback;

    public void setTask(Task task) {
    	 this.task = task;
        titleText.setText(task.getTitle());
        descText.setText(task.getDescription());
        dueDateText.setText("Due: " + task.getDueDate().toString());
        statusText.setText("Status: " + task.getStatus());
        priorityText.setText("Priority: " + task.getPriority());
        categoryText.setText("Category: " + task.getCategory());
        doneButton.setDisable("Completed".equalsIgnoreCase(task.getStatus()));
    }
    public void setController(TaskController controller) {
        this.taskController = controller;
    }

    public void setRefreshCallback(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
    }
    @FXML
    private void handleDone() {
        if (taskController != null && task != null) {
            taskController.updateTaskStatus(task.getId(), "Completed");
            if (refreshCallback != null) refreshCallback.run();
        }
    }

    @FXML
    private void handleDelete() {
        if (taskController != null && task != null) {

            System.out.println("Deleting task with ID: " + task.getId());

            taskController.deleteTask(task.getId());
            if (refreshCallback != null) refreshCallback.run();
        }
    }
    @FXML
    private void handleUpdate() {
    	 if (task == null || taskController == null) return;

    	    Task taskToUpdate = new Task(task.getId(), task.getTitle(), task.getDescription(),task.getDueDate(), task.getStatus(),task.getUserId(),task.getPriority(),task.getCategory());
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Update Task");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));
        vbox.setStyle("-fx-background-color: #f9f9f9; -fx-background-radius: 10;");

        TextField titleField = new TextField(taskToUpdate.getTitle());
        titleField.setPromptText("Title");
        titleField.setStyle("-fx-font-size: 14px; -fx-background-radius: 5; -fx-padding: 5;");

        TextField descField = new TextField(taskToUpdate.getDescription());
        descField.setPromptText("Description");
        descField.setStyle("-fx-font-size: 14px; -fx-background-radius: 5; -fx-padding: 5;");

        DatePicker dueDatePicker = new DatePicker(taskToUpdate.getDueDate());
        dueDatePicker.setPromptText("Due Date");
        dueDatePicker.setStyle("-fx-font-size: 14px;");

        ComboBox<String> priorityCombo = new ComboBox<>();
        priorityCombo.getItems().addAll("Low", "Medium", "High");
        priorityCombo.setValue(taskToUpdate.getPriority());
        priorityCombo.setPromptText("Priority");
        priorityCombo.setStyle("-fx-font-size: 14px;");

        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll(
            "Work",
            "Personal",
            "Study",
            "Health",
            "Shopping",
            "Others"
        );
        categoryCombo.setValue(taskToUpdate.getCategory());
        categoryCombo.setPromptText("Category");
        categoryCombo.setStyle("-fx-font-size: 14px;");

        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");

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

            // Update the task with new values
            taskToUpdate.setTitle(title);
            taskToUpdate.setDescription(desc);
            taskToUpdate.setDueDate(dueDate);
            taskToUpdate.setPriority(priority);
            taskToUpdate.setCategory(category);
            taskToUpdate.setStatus(status);

          
            taskController.updateTask(taskToUpdate);
            if (refreshCallback != null) refreshCallback.run();

            dialogStage.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> dialogStage.close());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        vbox.getChildren().addAll(
            new Label("Edit Task"),
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

    // Example status logic based on due date
    private String computeStatusBasedOnDueDate(LocalDate dueDate) {
        LocalDate today = LocalDate.now();

        if (dueDate.isBefore(today)) {
            return "in Progress";
        } else {
            return "Upcoming";
        }
    }

    // Dummy alert function - replace with your own alert method
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
