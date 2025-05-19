package view;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application{


	@Override
    public void start(Stage primaryStage) throws Exception {
        // Charger la vue de connexion (UserView)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserView.fxml"));
        Parent root = loader.load();
        
        primaryStage.setTitle("Gestionnaire de Tâches");
        primaryStage.setScene(new Scene(root, 663, 464));
        primaryStage.show();
    }

    public static void main(String[] args) {
    
        launch(args);
    }
}

//RUN IN TERMINAL
//javac --module-path "C:\Users\YASMINE\Downloads\javafx-sdk-23.0.2\lib" --add-modules javafx.controls,javafx.fxml view/Main.java
//java --module-path "C:\Users\YASMINE\Downloads\javafx-sdk-23.0.2\lib" --add-modules javafx.controls,javafx.fxml view.Main
//  --module-path "C:\Users\YASMINE\Downloads\javafx-sdk-23.0.2\lib" ^ --add-modules javafx.controls,javafx.fxml ^ --class-path "C:\Users\YASMINE\Downloads\mysql-connector-j-9.2.0\mysql-connector-j-9.2.0.jar;." view.Main
/*java --module-path "C:\Users\YASMINE\Downloads\javafx-sdk-23.0.2\lib" ^
--add-modules javafx.controls,javafx.fxml ^
--class-path "C:\Users\YASMINE\Downloads\mysql-connector-j-9.2.0\mysql-connector-j-9.2.0.jar;." view.Main */



