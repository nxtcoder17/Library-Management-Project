package library.ui.password;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

import library.ui.controller.Controller;

public class ResetPasswordController
    extends Controller
{
    @FXML private TextField field_password;
    @FXML private TextField field_confirmPassword;

    @FXML private Button button_submit;
    @FXML private Button button_cancel;

    @FXML
    protected void button_submit_clicked()
        throws SQLException, IOException, InterruptedException
    {
        // Check whether that both fields have same password
        String passwd = field_password.getText();
        String confPasswd = field_confirmPassword.getText();
        if (passwd.isEmpty() || confPasswd.isEmpty())
            super.notify("Enter Password",
                    "You must enter the password in both the fields", "warning").show();
        else {
            if (passwd.equals(confPasswd)) {
                Controller.db.changePassword(Controller.username, Controller.email, passwd);
                Controller.username = null;
                Controller.email = null;
                super.notify("Password Reset Successful",
                        "You can now login with your new password");
                super.sceneSwitcher("login/login.fxml", button_submit,
                        "Library: Login", 800, 600);
            }
            else
                super.notify("No Match",
                        "Password's don't match, Try Again", "warning");
        }
    }

    @FXML
    protected void button_cancel_clicked()
        throws IOException
    {
        super.goto_home(button_cancel);
    }
}
