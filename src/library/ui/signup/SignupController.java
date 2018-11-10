package library.ui.signup;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import library.base.NewUser;
import library.regex.RegexMatcher;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.sql.SQLException;

import library.ui.controller.Controller;

public class SignupController
    extends Controller
{
    @FXML private TextField field_username;
    @FXML private TextField field_password;
    @FXML private TextField field_confirmPassword;
    @FXML private TextField field_email;

    @FXML private ComboBox<String> combo_securityQuestion;
    @FXML private TextField field_securityAnswer;

    @FXML private Button button_submit;
    @FXML private Button button_cancel;

    RegexMatcher regex = null;

    @FXML
    protected void button_submit_clicked()
        throws SQLException
    {
        regex = new RegexMatcher();
        // Validate Email
        String user = field_username.getText();
        String email = field_email.getText();

        String password = field_password.getText();
        String confirmPassword = field_confirmPassword.getText();
        String security = combo_securityQuestion.getValue();
        String answer = field_securityAnswer.getText();

        boolean integFlag = false;
        // Regex Check: username
        if (regex.checkUsername(user) && user.length() < 16) {
            System.out.println("UserName is in Correct Format");
            field_username.getStyleClass().remove("error");
            integFlag = true;
        } else {
            System.out.println("Username is in invalid Format");
            field_username.getStyleClass().add("error");
            integFlag = false;
        }

        // Regex Check: email
        if (integFlag && regex.checkEmail(email)) {
            System.out.println("Email is in correct format");
            field_email.getStyleClass().remove("error");
            integFlag = true;
        } else {
            System.out.println("Email is in Incorrect Format");
            field_email.getStyleClass().add("error");
            integFlag = false;
        }

        // Matching the passwords
        if (integFlag && !password.equals(confirmPassword)) {
            System.out.println("Password does not match");
            field_password.getStyleClass().add("error");
            field_confirmPassword.getStyleClass().add("error");
            integFlag = false;
        } else {
            field_password.getStyleClass().remove("error");
            field_confirmPassword.getStyleClass().remove("error");

            integFlag = true;
        }

        if (integFlag && security.isEmpty() || answer.isEmpty())
            integFlag = false;

        Notifications notification = null;
        if (integFlag) {
            NewUser newUser= new NewUser();
            newUser.setUsername(user);
            newUser.setPassword(password);
            newUser.setEmail(email);
            newUser.setSecurity(security);
            newUser.setAnswer(answer);

                // Throws IOException and SQLException 
            super.db.addUser(newUser);
            System.out.println("A new User has been created");
            notification = super.notify("User Created",
                                    "User [%s] created successfully".format(user),
                                    "confirm");
            notification.show();
        } else {
            System.out.println("Wrong Info has been submited");
            notification = super.notify("User Creation Failed",
                                    "User [%s] can't be created".format(user),
                                    "error");
            notification.show();
        }
    }

    @FXML
    protected void button_cancel_clicked()
        throws IOException, SQLException
    {
        super.goto_home(button_cancel);
    }
}
