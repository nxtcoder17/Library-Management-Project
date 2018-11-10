package library.ui.homepage;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.SQLException;

import library.ui.controller.Controller;

public class HomePageController
    extends Controller
{
    @FXML private Button button_addBook;
    @FXML private Button button_removeBook;
    @FXML private Button button_issueBook;
    @FXML private Button button_returnBook;
    @FXML private Button button_displayBook;
    @FXML private Button button_listOfIssuedBooks;
    @FXML private Button button_addMembers;
    @FXML private Button button_removeMember;


    // Method - homepage_button_addBook_clicked()
    @FXML protected void logout_method()
        throws IOException
    {
        super.sceneSwitcher("login/login.fxml", button_addBook,
                "Library: Login", 800, 400);
    }

    @FXML protected void button_addBook_clicked()
        throws IOException
    {
        super.sceneSwitcher("book/add/addBook.fxml", button_addBook,
                "Library: Add Books", 800, 400);
    }

    @FXML protected void button_issueBook_clicked()
        throws IOException
    {
        super.sceneSwitcher("book/issue/issueBook.fxml", button_issueBook,
                "Library: Issue Book", 800, 400);
    }
    
    @FXML
    protected void button_returnBook_clicked()
        throws IOException
    {
        super.sceneSwitcher("book/returns/returnBook.fxml", button_returnBook,
                "Library: Return Book", 800, 400);
    }

    @FXML protected void button_displayBook_clicked()
        throws IOException
    {
        super.sceneSwitcher("book/display/displayBook.fxml", button_displayBook,
                "Library: Display Books", 1000, 400);
    }

    @FXML protected void button_listOfIssuedBooks_clicked()
        throws IOException
    {
        super.sceneSwitcher("book/display/listOfIssuedBooks.fxml", button_listOfIssuedBooks,
                "Library: Issued Books", 1000, 400);
    }

    @FXML protected void button_addMembers_clicked()
        throws IOException
    {
        super.sceneSwitcher("members/addMembers.fxml", button_addMembers,
                "Library: Add Members", 1000, 400);
    }

    @FXML protected void button_removeBook_clicked()
        throws IOException
    {
        super.sceneSwitcher("book/remove/removeBook.fxml", button_removeBook,
                "Library: Remove Book", 600, 400);
    }

    @FXML protected void button_removeMember_clicked()
        throws IOException
    {
        super.sceneSwitcher("members/removeMembers.fxml", button_removeMember,
                "Library: Remove Member", 600, 400);
    }
}
