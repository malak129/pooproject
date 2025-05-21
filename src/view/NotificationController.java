package view;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Task;
import model.TaskDAO;

import java.io.IOException;
import java.util.List;

import Controller.TaskController;

public class NotificationController {

    @FXML
    private VBox notificationList; 

    @FXML
    private Text notificationText; 

    public void setMessage(String message) {
        if (notificationText != null) {
            notificationText.setText(message);
        }
    }
    private int userId;

    public void setUserId(int userId) {
        this.userId = userId;
    }
    private TaskController taskController;

    public void setTaskController(TaskController taskController) {
        this.taskController = taskController;
    }

    public void loadOverdueInProgressTasks() {
    	try {
            TaskDAO taskDAO = new TaskDAO();
            List<Task> overdueInProgressTasks = taskDAO.getOverdueTasks(userId).stream()
                    .filter(task -> "In Progress".equalsIgnoreCase(task.getStatus()))
                    .toList();

            setTasks(overdueInProgressTasks);
       
        } catch (Exception e) {
            e.printStackTrace();
            setMessage("Erreur lors du chargement des notifications.");
        }
    }

 
    public void setTasks(List<Task> tasks) {
        notificationList.getChildren().clear();
        if (tasks.isEmpty()) {
            setMessage("Aucune tâche en cours est en retard.");
        } else {
            setMessage("Tâches en retard et en cours :");
        }

        for (Task task : tasks) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/taskcard.fxml"));
                AnchorPane card = loader.load();

                TaskCardController controller = loader.getController();
                controller.setTask(task);
                controller.setController(taskController);
                notificationList.getChildren().add(card);
                controller.setRefreshCallback(() -> loadOverdueInProgressTasks());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
