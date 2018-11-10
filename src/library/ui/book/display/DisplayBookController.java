package library.ui.book.display;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import library.base.BookInfo;
import library.database.Database;

import java.io.IOException;
import java.sql.SQLException;

import library.ui.controller.Controller;

public class DisplayBookController
    extends Controller
{
    @FXML private TableView table;

    /* This initialize() method will automatically run when this Controller comes into play */
    public void initialize() 
        throws IOException
    {

        TableColumn<BookInfo, String> col_sno = new TableColumn<>("S.No.");
        col_sno.setCellValueFactory(
                        new PropertyValueFactory("sno"));

        TableColumn<BookInfo, String> col_bookId = new TableColumn<>("Book's ID");
        col_bookId.setCellValueFactory(
                        new PropertyValueFactory("bookId"));

        TableColumn<BookInfo, String> col_title = new TableColumn<>("Title");
        col_title.setCellValueFactory(
                        new PropertyValueFactory("title"));

        TableColumn<BookInfo, String> col_author = new TableColumn<>("Author");
        col_author.setCellValueFactory(
                        new PropertyValueFactory("author"));


        TableColumn<BookInfo, String> col_branch = new TableColumn<>("Branch");
        col_branch.setCellValueFactory(
                        new PropertyValueFactory("branch"));

        TableColumn<BookInfo, String> col_publisher = new TableColumn<>("Publisher");
        col_publisher.setCellValueFactory(
                        new PropertyValueFactory("publisher"));


        TableColumn<BookInfo, String> col_price = new TableColumn<>("Price");
        col_price.setCellValueFactory(
                        new PropertyValueFactory("price"));

        TableColumn<BookInfo, String> col_copies = new TableColumn<>("Copies");
        col_copies.setCellValueFactory(
                        new PropertyValueFactory("copies"));

        // Adding all columns to the Table
        table.getColumns().add(col_sno);
        table.getColumns().add(col_bookId);
        table.getColumns().add(col_branch);
        table.getColumns().add(col_title);
        table.getColumns().add(col_author);
        table.getColumns().add(col_publisher);
        table.getColumns().add(col_price);
        table.getColumns().add(col_copies);

        ObservableList<BookInfo> data = null;

        try {
            Database db = new Database();
            data = db.getAllBooks();
        } 
        catch(SQLException e) {
            System.out.println("[Database Exception]: " + e.getMessage());
            e.printStackTrace();
        }
        catch(IOException z) {
            System.out.println("[IOException]: " + z.getMessage());
        }

        table.setItems(data);
    }

    @FXML private Button back_button;
    @FXML protected void back_button_clicked()
        throws IOException
    {
        super.goto_home(back_button);
    }
}
