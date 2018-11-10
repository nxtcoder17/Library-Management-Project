package library.ui.book.display;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.SQLException;

import javafx.scene.control.cell.PropertyValueFactory;
import library.base.BorrowerInfo;
import library.database.Database;

import library.ui.controller.Controller;

public class ListOfIssuedBooksController
    extends Controller
{
    @FXML private TableView table;
    @FXML private Button button_cancel;

    public void initialize()
        throws IOException
    {
        TableColumn<BorrowerInfo, String> col_sno = new TableColumn<>("S.No");
        col_sno.setCellValueFactory(
                new PropertyValueFactory("sno"));

        TableColumn<BorrowerInfo, String> col_libraryId = new TableColumn<>("Library ID");
        col_libraryId.setCellValueFactory(
                new PropertyValueFactory("libraryId"));

        TableColumn<BorrowerInfo, String> col_borrowerName = new TableColumn<>("Student Name");
        col_borrowerName.setCellValueFactory(
                new PropertyValueFactory("borrowerName"));

        TableColumn<BorrowerInfo, String> col_bookId = new TableColumn<>("Book Id");
        col_bookId.setCellValueFactory(
                new PropertyValueFactory("bookId"));

        TableColumn<BorrowerInfo, String> col_title = new TableColumn<>("Title");
        col_title.setCellValueFactory(
                new PropertyValueFactory("title"));

        TableColumn<BorrowerInfo, String> col_issueDate = new TableColumn<>("Issue Date");
        col_issueDate.setCellValueFactory(
                new PropertyValueFactory("issueDate"));

        TableColumn<BorrowerInfo, String> col_returnDate = new TableColumn<>("Issue Date");
        col_returnDate.setCellValueFactory(
                new PropertyValueFactory("returnDate"));

        table.getColumns().add(col_sno);
        table.getColumns().add(col_libraryId);
        table.getColumns().add(col_borrowerName);
        table.getColumns().add(col_bookId);
        table.getColumns().add(col_title);
        table.getColumns().add(col_issueDate);
        table.getColumns().add(col_returnDate);

        ObservableList<BorrowerInfo> data  = null;
        try {
            Database db = new Database();
            data = db.getAllIssuedBooks();
        }
        catch (SQLException e) {
            System.out.println("[Database Invocation]: " + e.getMessage());
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out.println("[GetAllIssuedBooks Exception: "  + e.getMessage());
            e.printStackTrace();
        }

        table.setItems(data);
    }

    @FXML
    protected void button_cancel_clicked()
            throws IOException
    {
        super.goto_home(button_cancel);
    }
}
