package library.ui.password;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.sql.SQLException;
import java.io.IOException;

import library.ui.controller.Controller;

public class ForgotPasswordController
    extends Controller
{
    @FXML private TextField field_username;
    @FXML private TextField field_email;
    @FXML private TextField field_question;
    @FXML private TextField field_answer;

    @FXML
    protected void fetch_securityQuestion()
        throws SQLException
    {
        String user = field_username.getText();
        String email = field_email.getText();

        // Throws SQLException
        if (super.db.doesUserExist(user, email)) {
            field_question.setText(super.db.getSecurityQuestion(user,email));
        }
    }

    @FXML private Button button_submit;
    @FXML private Button button_cancel;

    @FXML
    protected void button_submit_clicked()
        throws IOException, SQLException
    {
        String answer = field_answer.getText();
        if (super.db.checkSecurityAnswer(field_username.getText(), answer)) {
            super.email = field_email.getText();
            super.username = field_username.getText();
            super.sceneSwitcher("password/resetPassword.fxml", button_submit,
                    "Library: Forgot Password", 800, 400);
        }
    }

    @FXML
    protected void button_cancel_clicked()
        throws IOException
    {
        super.goto_home(button_cancel);
    }
}
