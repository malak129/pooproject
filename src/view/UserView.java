package view;

import java.io.IOException;

import Controller.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

public class UserView {
	@FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField newUsernameField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private Label messageLabel;

    private UserController userController;

    public void initialize() {
        userController = new UserController();
    }
    
    @FXML
    public void handleLogin(ActionEvent event) {
        // Simulate user authentication (Replace with actual authentication logic)
    	String username = usernameField.getText();
    	String password = passwordField.getText();
    	User user = userController.login(username, password);
        
        if (user != null) {
            try {
                // Load TaskView.fxml
            
                FXMLLoader taskLoader = new FXMLLoader(getClass().getResource("/view/TaskView.fxml"));
            
                Parent taskRoot = taskLoader.load();
            
                // Get the controller to pass user data (Optional)
                
                UserController userController = new UserController();
               
                userController.login(username, password); 
           
                // Switch scene
                Stage currentStage = (Stage) usernameField.getScene().getWindow();
                currentStage.setScene(new Scene(taskRoot, 800, 600));
                currentStage.show();
              
                System.out.println("Stage found: " + currentStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid login credentials!"); 
        }
    }


    @FXML
    public void handleRegister() {
        String username = newUsernameField.getText();
        String password = newPasswordField.getText();
        boolean registrationSuccessful = userController.register(username, password);

        if (registrationSuccessful) {
            messageLabel.setText("Inscription réussie !");
            
            try {
            	FXMLLoader taskLoader = new FXMLLoader(getClass().getResource("/view/TaskView.fxml"));
	            Parent taskRoot = taskLoader.load();
	            Stage currentStage = (Stage) usernameField.getScene().getWindow();
	            currentStage.setScene(new Scene(taskRoot, 800, 600));
	            currentStage.show();
	           
	          
	            System.out.println("Stage found: " + currentStage);}
            catch(IOException e){
    	   e.printStackTrace();
    	   
       
            }}
       
    }
}
