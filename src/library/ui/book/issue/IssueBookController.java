package library.ui.book.issue;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.converter.LocalDateStringConverter;
import library.base.BorrowerInfo;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import library.ui.controller.Controller;

public class IssueBookController
    extends Controller
{
    @FXML private TextField field_libraryId;
    @FXML private TextField field_bookId;
    @FXML private TextField field_borrowerName;
    @FXML private TextField field_bookName;
    @FXML private DatePicker date_issue;
    @FXML private DatePicker date_return;

    @FXML private Button button_submit;
    @FXML private Button button_cancel;


    @FXML
    public void initialize()
        throws IOException
    {
        DateTimeFormatter dpFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateStringConverter tempConv = new LocalDateStringConverter(dpFormatter, null);

        date_issue.setConverter(tempConv);
        date_issue.setValue(LocalDate.now());

        date_return.setConverter(tempConv);
        date_return.setValue(LocalDate.now().plusDays(15));
    }

    @FXML
    protected void button_submit_clicked()
        throws SQLException, IOException
    {
        String libraryId = field_libraryId.getText();
        String bookId = field_bookId.getText();
        String borrowerName = field_borrowerName.getText();
        String title = field_bookName.getText();
        LocalDate iDate = date_issue.getValue();
        LocalDate rDate = date_return.getValue();

        // First Check whether the book is available or not
        if (Controller.db.isBookAvailable(bookId)) {
            System.out.println("Book is Available");
            // Second, Check whether borrower is a member of Library
            if (Controller.db.doesBorrowerExist(libraryId)) {
                System.out.println("Borrower Exists also");
                if (!Controller.db.hasAlreadyBorrowed(libraryId)) {
                    System.out.println("Borrower has not borrowed any book too");

                    BorrowerInfo b = new BorrowerInfo();
                    b.setLibraryId(libraryId);
                    b.setBookId(bookId);
                    b.setBorrowerName(borrowerName);
                    b.setTitle(title);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDateStringConverter conv = new  LocalDateStringConverter(formatter, null);

                    System.out.println("Issue Date: " + conv.toString(iDate));
                    System.out.println("Return Date: " + conv.toString(rDate));

                    b.setIssueDate(conv.toString(iDate));
                    b.setReturnDate(conv.toString(rDate));

                    if (Controller.db.issueBook(b)) {
                        System.out.println("Book has been issued Successfully");
                        super.notify("Successfull", " Book Issued Successfully",
                                    "confirm").show();
                    }
                    else {
                        // Book Not Issued, don't know why
                    }   
                }
                else {
                    // Can't be issued until u returns the book
                    super.notify("Already Borrowed",
                            "Book can't be issued until you returns", "warning").show();
                }
            }
            else {
                // Borrower doesn't exist in the DB
                super.notify("Unknown Borrower",
                            "Any Stranger can't be issued a book", "error").show();
            }
        }
        else {
            // Book ain't available
            super.notify("Not Available",
                    "Requested book is not available in library", "error").show();
        }
    }

    @FXML
    protected void button_cancel_clicked()
        throws IOException
    {
        super.goto_home(button_cancel);
    }
}
