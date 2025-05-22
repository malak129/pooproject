package Controller;

import java.io.IOException;



import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import model.User;
import model.UserDAO;
import view.UserView;

public class UserController {

	 private UserDAO userDAO;
	    private UserView userView;
	    @FXML
	    private Text error1,error2,error3,error4;
	    
	    public UserController() {
	        this.userDAO = new UserDAO();
	        this.userView = new UserView();
	    }

	    public User login(String username, String password) {
	        try {
	        
	            User user = userDAO.getUserByUsername(username);
	            if (user != null && user.getPassword().equals(password)) {
	            	
	                return user; // Connexion réussie 
	            }
	        } catch (SQLException | IOException e) {
	            System.err.println("Erreur lors de la connexion : " + e.getMessage());
	        }
	        return null; // Échec de la connexion
	    }

	    public boolean register(String username, String password) {
	        User user = new User(username, password);
	        if(username.isEmpty() || password.isEmpty()) {
	        	return false;
	        }
	        try {
	            userDAO.addUser(user);
	            System.out.println("Utilisateur enregistré avec succès.");
	            return true;
	            
	           
	        } catch (SQLException | IOException e) {
	            System.err.println("Erreur lors de l'enregistrement de l'utilisateur : " + e.getMessage());
	            return false;
	        }
	    }

}
