package library.ui.login;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.SQLException;
import library.ui.controller.Controller;

public class LoginController
    extends Controller
{
    @FXML private TextField field_user;
    @FXML private PasswordField field_passwd;

    @FXML private Button button_login;
    @FXML private Button button_signup;

    @FXML protected void button_login_clicked()
            throws SQLException, IOException
    {
        String user = field_user.getText();
        String passwd = field_passwd.getText();

        if (user.isEmpty() || passwd.isEmpty())
            super.notify("Username & Password Required",
                    "You must provide a valid set of Username & password", "error").show();
        else {
            if (super.db.isAuthorized(user, passwd))
                    super.goto_home(button_login);
            else
                super.notify("Access Denied",
                        "You must provide a valid set of Username & password", "warning").show();
        }
    }

    // When user presses Enter Key over the Password Field
    @FXML protected void handle_EnterKey_press()
            throws IOException, SQLException
    {
        button_login_clicked();
    }

    @FXML protected void button_signup_clicked()
            throws IOException
    {
        super.sceneSwitcher("signup/signup.fxml", button_signup,
                "Library: Sign Up", 800, 800);
    }

    @FXML protected void forgot_password_clicked()
            throws IOException
    {
        super.sceneSwitcher("password/forgotPassword.fxml", button_login,
                "Library: Reset Password", 800, 600);
    }
}
