package view;

import javafx.fxml.FXML;

import javafx.scene.text.Text;
import model.Task;

public class TaskCardController {

    @FXML private Text titleText;
    @FXML private Text descText;
    @FXML private Text dueDateText;
    @FXML private Text statusText;

    public void setTask(Task task) {
        titleText.setText(task.getTitle());
        descText.setText(task.getDescription());
        dueDateText.setText("Due: " + task.getDueDate().toString());
        statusText.setText("Status: " + task.getStatus());
    }
}
