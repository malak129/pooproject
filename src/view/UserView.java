package view;

import java.io.IOException;

import Controller.TaskController;
import Controller.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
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
    private Text error1,error2,error3,error4;

    private UserController userController;

    public void initialize() {
        userController = new UserController();
    }

    
    @FXML
    public void handleLogin(ActionEvent event) {
    
    	String username = usernameField.getText();
    	String password = passwordField.getText();
    	User user = userController.login(username, password);
    	error1.setText("");
    	error2.setText("");
    	error3.setText("");
    	error4.setText("");
     if(username.isEmpty()) {
         	error1.setText("username shouldn't be empty");
         }
         	if(password.isEmpty()) {
         		error2.setText("password should not be empty");
         		
         	}
         	if(!username.isEmpty() && !password.isEmpty()) {
        if (user != null) {
            try {
       
            
                FXMLLoader taskLoader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
            
                
            Parent taskRoot = taskLoader.load();
               UserController userController = new UserController();  
                userController.login(username, password); 
                TaskController taskViewController = taskLoader.getController();
                taskViewController.UserId(user.getId(),user.getUsername(),user.getScore());
                
              

           
                // Switch scene
                Stage currentStage = (Stage) usernameField.getScene().getWindow();
                Scene fullScreenScene = new Scene(taskRoot);
//                currentStage.setScene(new Scene(taskRoot, 900, 600));
                currentStage.setScene(fullScreenScene);
                currentStage.setMaximized(true);
                currentStage.show();
              
                System.out.println("Stage found: " + currentStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
        	error1.setText("Invalid login credentials!");
            System.out.println("Invalid login credentials!"); 

        }
    }}


    @FXML
    public void handleRegister() {
        String username = newUsernameField.getText();
        String password = newPasswordField.getText();
        error1.setText("");
    	error2.setText("");
    	error3.setText("");
    	error4.setText("");
        if(username.isEmpty()) {
        	error3.setText("username shouldn't be empty");
        }
        	if(password.isEmpty()) {
        		error4.setText("password should not be empty");
        		
        	}
        	if(!username.isEmpty() && !password.isEmpty()) {
        		 boolean registrationSuccessful = userController.register(username, password);
        if (registrationSuccessful) {
            
            try {

            	FXMLLoader taskLoader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
                Parent taskRoot = taskLoader.load();
                
                TaskController taskController = taskLoader.getController();


                User newUser = userController.login(username, password);
                if (newUser != null) {
                    taskController.UserId(newUser.getId(),newUser.getUsername(),0);
                }

                Stage currentStage = (Stage) usernameField.getScene().getWindow();
                currentStage.setScene(new Scene(taskRoot, 900, 600));
                currentStage.show();

                System.out.println("Stage found: " + currentStage);
                }
                
            catch(IOException e){
    	   e.printStackTrace();
    	   
       
            }
       
    }else {
    	error3.setText("Username already exist");
    }
        }}}
    
