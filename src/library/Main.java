package library;

// Imports 
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

// Imports: Exceptions
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage)
        throws IOException
    {
        Parent root = FXMLLoader.load(getClass().
                            getResource("/library/ui/login/login.fxml"));
        primaryStage.setTitle("Invoke Library");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void main(String [] args)
    {
        Application.launch(args);
    }
}
