package library.ui.controller;

//==============[ Imports ]======================={{{

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import library.database.Database;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.sql.SQLException;
//==============[ Imports ]=======================}}}

public class Controller {
    protected static Database db;
    protected static String username = null;
    protected static String email = null;
    private static final String NOTIFY_ICONS = "/library/images/notifications/";

    public Controller()
    {
        try {
            db = new Database();
        } catch (SQLException e) {
            System.out.println("Error Connecting to the DB");
        } catch (IOException e) {
            System.out.println("Error finding schema files");
        }
    }

    //==================[ Utility Methods ]================={{{

    protected void goto_home(Parent node)
        throws IOException
    {
        Stage stage = (Stage)node.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(getClass().
                                getResource("/library/ui/homepage/homepage.fxml"));
        stage.hide();
        stage.setScene(new Scene(newRoot, 800, 400));
        stage.setTitle("Home Page");
        stage.show();
    }

    protected Notifications notify(String title, String text)
    {
        return notify(title, text, "normal");
    }

    protected Notifications notify(String title, String text, String type)
    {
        Notifications obj = Notifications.create();

        obj.position(Pos.TOP_RIGHT);
        obj.darkStyle();
        obj.hideAfter(Duration.seconds(5));
        obj.title(title);
        obj.text(text);

        switch(type) {
            case "normal":
                break;
            case "error":
                obj.graphic(new ImageView(NOTIFY_ICONS + "invalid-64px.png"));
                break;
            case "confirm":
                obj.graphic(new ImageView(NOTIFY_ICONS + "confirmation-64px.png"));
                break;
            case "warning":
                obj.graphic(new ImageView(NOTIFY_ICONS + "invalid-64px.png"));
                break;
        }

        return obj;
    }

    protected void sceneSwitcher(String file,Parent node, 
                    String title, double width, double height)
        throws IOException
    {
        Stage stage = (Stage) node.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(
                            getClass().getResource("/library/ui/" + file));

        stage.hide();
        stage.setScene(new Scene(newRoot, width, height));
        stage.setTitle(title);
        stage.show();
    }

    //==================[ End: Utility Methods ]=================}}}

//    //============[ Login Screen : login.fxml ]======================{{{
//
//    @FXML private TextField login_field_user;
//    @FXML private PasswordField login_field_passwd;
//
//    @FXML private Button login_button_submit;
//    @FXML private Button signup_button;
//
//    /* Verifies User Credentials */
//    @FXML protected void login_button_submit_clicked()
//        throws SQLException, IOException
//    {
//        String username = login_field_user.getText();
//        String passwd  = login_field_passwd.getText();
//
//        if (username.length() == 0 || passwd.length() == 0) {
//            Notifications notification;
//            notification = notify("Username & Password Required",
//                            "You need to provide a valid set of Username & Password");
//            notification.show();
//        } else {
//            if (db.isAuthorized(username, passwd)) {
//                System.out.println("Login Successfull");
//                goto_home(login_field_user);
//            } else {
//                Notifications notification;
//                notification = notify("Access Denied",
//                                    "You must provide a valid set of Username & Password");
//                notification.graphic(new ImageView(NOTIFY_ICONS + "invalid-64px.png"));
//                notification.show();
//            }
//        }
//    }
//
//    /* When User presses <Enter-Key> on Password Field */
//    @FXML protected void handle_EnterKey_press()
//        throws IOException, SQLException
//    {
//        // login_button_submit.setSelected();
//        login_button_submit_clicked();
//    }
//
//    @FXML protected void signup_button_clicked()
//        throws IOException, SQLException
//    {
//        sceneSwitcher("signup.fxml", signup_button,
//                "SignUp to Library", 800, 400);
//    }
//
//    @FXML protected void forgot_password_clicked()
//        throws IOException
//    {
//        // System.out.println("Forget Password Click");
//        sceneSwitcher("forgotPassword.fxml", login_field_user,
//                "Reset Password", 600, 300);
//    }
//
//    // ============[ End: Login Screen ]======================}}}
}
