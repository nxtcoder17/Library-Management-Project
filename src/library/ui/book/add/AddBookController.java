package library.ui.book.add;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import library.base.BookInfo;
import library.ui.controller.Controller;

import java.io.IOException;
import java.sql.SQLException;

public class AddBookController
    extends Controller
{
    @FXML private TextField field_bookId;
    @FXML private TextField field_title;
    @FXML private TextField field_author;
    @FXML private TextField field_branch;
    @FXML private TextField field_publisher;
    @FXML private TextField field_price;
    @FXML private TextField field_copies;

    @FXML private Button button_submit;
    @FXML private Button button_cancel;

    @FXML
    protected void button_submit_clicked()
        throws SQLException
    {
        BookInfo book = new BookInfo();
        book.setBookId    (field_bookId.getText());
        book.setTitle     (field_title.getText());
        book.setAuthor    (field_bookId.getText());
        book.setBranch    (field_branch.getText());
        book.setPublisher (field_publisher.getText());
        book.setPrice     (Integer.parseInt(field_price.getText()) );
        book.setCopies    (Integer.parseInt(field_copies.getText()) );

        Controller.db.addBook(book);
        super.notify("Book Added",
                    "New Book Successfully added to Library", "confirm").show();
    }

    @FXML
    protected void button_cancel_clicked()
        throws IOException
    {
        super.goto_home(button_cancel);
    }
}
