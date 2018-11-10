package library.ui.book.remove;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.SQLException;

import library.ui.controller.Controller;

public class RemoveBookController
    extends Controller
{
    @FXML private TextField field_bookId;

    @FXML private Button button_submit;
    @FXML private Button button_cancel;

    @FXML protected void button_submit_clicked()
        throws IOException, SQLException
    {
        Controller.db.removeBook(field_bookId.getText());
    }

    @FXML protected void button_cancel_clicked()
        throws IOException
    {
        super.goto_home(button_cancel);
    }
}