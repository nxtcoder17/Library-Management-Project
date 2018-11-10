package library.ui.book.returns;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.SQLException;

import library.ui.controller.Controller;

public class ReturnBookController
    extends Controller
{
    @FXML private TextField field_libraryId;
    @FXML private TextField field_bookId;
    @FXML private TextField field_fine;

    @FXML private Button button_submit;
    @FXML private Button button_cancel;

    @FXML
    protected void calc_fine()
        throws SQLException
    {
        String libraryId = field_libraryId.getText();
        String bookId = field_bookId.getText();

        if (Controller.db.isThisBookBorrowed(libraryId, bookId)) {
            int days = Controller.db.calc_days(libraryId, bookId);
            int fine = 0;
            if (days > 0)
                fine = days * 1;

            field_fine.setText(Integer.toString(fine));
        }
        else {
            // Book has not been borrowed @ this libraryId
        }
    }

    @FXML
    protected void button_submit_clicked()
        throws IOException, SQLException
    { 
        String libraryId = field_libraryId.getText();
        String bookId = field_bookId.getText();
        String fine = field_bookId.getText();

        if (Controller.db.isThisBookBorrowed(libraryId, bookId)) {
            if (Controller.db.returnBook(libraryId, libraryId))
                super.notify("Return Accepted",
                        "Book [%s] issued to [%s] successfully returned".format(bookId, libraryId), "confirm");
            else {
                // Notify about book not returned
            }

        }
    }

    @FXML
    protected void button_cancel_clicked()
        throws IOException
    {
        super.goto_home(button_cancel);
    }
}
