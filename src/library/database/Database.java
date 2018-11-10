package library.database;

//================[ IMPORTS ]============{{{
/* Base Imports */
import library.base.BookInfo;
import library.base.BorrowerInfo;
import library.base.NewUser;
import library.base.MemberInfo;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/* File Handling Imports */
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/* Imports for returngin Lists for TableView */
import javafx.collections.ObservableList;
import java.util.LinkedList;
import javafx.collections.FXCollections;

/* Notification Support */
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import org.controlsfx.control.Notifications;
import javafx.util.Duration;


/* For Converting LocalDates from Strings and vice-versa */
import javafx.util.converter.LocalDateStringConverter;

//=============[ End of: IMPORTS ]==============}}}

public class Database {
	private static final String DB_NAME = "library";
	private static final String DB_URL = "jdbc:mariadb://localhost/" + DB_NAME;

	private static final String BOOKS_TABLE = "book_details";
	private static final String BORROWER_TABLE = "borrowed_books";
	private static final String AUTHORIZED = "authorized";
	private static final String MEMBERS = "members";

    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement prepStmt;

    private static Notifications notification;
    private static final String NOTIFY_ICONS = "/library/images/notifications/";

    //============[ Constructor ]============={{{
	public Database() throws SQLException,IOException {
        connection = DriverManager.getConnection(DB_URL, "root", "");
        // System.out.println("Connection Established with MariaDB");

        // Preparing the Table to store data
        final String BOOK_DETAILS_SCHEMA = readFromSqlFile("book_details.sql");
        final String AUTHORIZED_SCHEMA = readFromSqlFile("signup.sql");
        final String BORROWER_TABLE_SCHEMA = readFromSqlFile("borrowed_books.sql");
        final String MEMBERS_SCHEMA = readFromSqlFile("members.sql");

        // System.out.println(BOOK_DETAILS_SCHEMA);
        stmt = connection.createStatement();
        stmt.executeQuery(AUTHORIZED_SCHEMA);
        stmt.executeQuery(BOOK_DETAILS_SCHEMA);
        stmt.executeQuery(BORROWER_TABLE_SCHEMA);
        stmt.executeQuery(MEMBERS_SCHEMA);
        // System.out.println("Successfully Created Table [or it exists]");

        notification = Notifications.create();
        notification.darkStyle();
        notification.position(Pos.TOP_RIGHT);
        notification.hideAfter(Duration.seconds(5));
	}
    //===============[ End: Constructor ]=============}}}

  //===========[ Read From SQL File ]============{{{
  private String readFromSqlFile(String fileName) 
                                    throws IOException {
    InputStream in = Database.class.getResourceAsStream("/library/database/schema/" + fileName);
    StringBuilder text = new StringBuilder();

    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

    String line;
    while ((line = reader.readLine()) != null) {
      text.append(line);
    }
    return text.toString();
  }
  //============[ End Of: Read from SQL File ]========}}}

  //==========[ Add User ]========== {{{
  public void addUser(NewUser user) throws SQLException {

      final String ADD_USER = "INSERT INTO " + AUTHORIZED
                        + "(username,password,email,security,answer) "
                        + "VALUES(?,?,?,?,?)";
      prepStmt = connection.prepareStatement(ADD_USER);
      prepStmt.setString(1, user.getUsername());
      prepStmt.setString(2, user.getPassword());
      prepStmt.setString(3, user.getEmail());
      prepStmt.setString(4, user.getSecurity());
      prepStmt.setString(5, user.getAnswer());

      prepStmt.executeUpdate();
  }
  //=========[ End of: Add User ]=========}}}

  //=============[ Add New Library Member ]============{{{
  public void addMember(MemberInfo member)
                            throws SQLException {

    // String branchCount = "SELECT libraryId FROM " + MEMBERS
                            // + " ORDER BY branch DESC LIMIT 1 WHERE branch=?";
    // prepStmt = connection.prepareStatement(branchCount);
    // prepStmt.setString(1, member.getBranch());
    // ResultSet r = prepStmt.executeQuery();
// 
    // String max_id = "";
    // if (r.next()) {
        // String max_id = r.getString("libraryId");
    // } else {
        // max_id = member.getBranch().toLower() + "_" + "001"
    // }
    // // Replacing all Alphabets and _ with nothing 
    // int max_val_id = Integer.parseInt(max_id.replaceAll("[A-Za-z_]", ""));
    // 
// 
    // System.out.println("Max Value is " + max_val);


    //=====================================

    final String ADD_MEMBER = "INSERT INTO " + MEMBERS
                                + "(firstName,lastName,semester,rollno,branch,email,libraryId)"
                                + " VALUES(?,?,?,?,?,?,?)";

    prepStmt = connection.prepareStatement(ADD_MEMBER);
    prepStmt.setString(1, member.getFirstName());
    prepStmt.setString(2, member.getLastName());
    prepStmt.setInt   (3, member.getSemester());
    prepStmt.setInt   (4, member.getRollNo());
    prepStmt.setString(5, member.getBranch());
    prepStmt.setString(6, member.getEmail());

    // String newLibraryId = member.getBranch().toLower() + "_" + ++max_val_id;
    String newLibraryId = member.getBranch().toLowerCase() + "_" + member.getRollNo();
    prepStmt.setString(7, newLibraryId);

    prepStmt.executeUpdate();
  }
  //=============[ End of: Add New Library Member ]=====}}}

  //=============[ Remove Library Member ]=========={{{
  public boolean removeMember(MemberInfo member) throws SQLException {
      // First, Check if Item is in Database or not
      String checkExistance = "SELECT * from " + MEMBERS
                                + " WHERE libraryId=?";
      prepStmt = connection.prepareStatement(checkExistance);
      prepStmt.setString(1, member.getLibraryId());
      ResultSet r = prepStmt.executeQuery();
      
      if (r.next()) {
          // Second, Remove the member
          String deleteMember = "DELETE FROM " + MEMBERS
                                   + " WHERE libraryId=?";
          prepStmt = connection.prepareStatement(deleteMember);
          prepStmt.setString(1, member.getLibraryId());
          int exitStatus = prepStmt.executeUpdate();
          if (exitStatus == 0 ? true: false) {
              return true;
          } 
          else {
              return false;
          }
      }
      return false;
  }
  //===========[ End of: Remove Member ]==========}}}

  //==========[ Change Password ]============={{{
  public void changePassword(String username, String email, String passwd)
          throws SQLException
  {
      final String PASS_CHANGE="UPDATE " + AUTHORIZED
                                 + " set password=? "
                                 + " WHERE username=? and email=?";
      prepStmt = connection.prepareStatement(PASS_CHANGE);
      prepStmt.setString(1, passwd);
      prepStmt.setString(2, username);
      prepStmt.setString(3, email);

      prepStmt.executeUpdate();
  }
  //==========[ End Of: Change Password ]========}}}

  //==========[ Add Book ]==========={{{
  public void addBook(BookInfo book) throws SQLException {
      final String ADD_BOOK = "INSERT INTO " + BOOKS_TABLE
                      + "(bookId,title,author,publisher,price,copies,branch) "
                      + "VALUES(?,?,?,?,?,?,?)";
      prepStmt = connection.prepareStatement(ADD_BOOK); 
      prepStmt.setString (1, book.getBookId());
      prepStmt.setString (2, book.getTitle());
      prepStmt.setString (3, book.getAuthor());
      prepStmt.setString (4, book.getPublisher());
      prepStmt.setInt    (5, book.getPrice());
      prepStmt.setInt    (6, book.getCopies());
      prepStmt.setString (7, book.getBranch());

      prepStmt.executeUpdate();
  }
  //===========[ End of: Add Book ]============}}}

  //============[ Remove Book ]============{{{
  public void removeBook(String bookId) throws SQLException {
    /* Removing a Book from DB involves 4 steps here: 
       # Search the to be deleted book in DB 
       # Remove that Book
       # Update the Serial Nos to val - 1 for all books below
       # Adjust AUTO_INCREMENT value to 1 again
   */

    final String SEARCH_BOOKID = "SELECT sno,bookId from " + BOOKS_TABLE
                                    + " WHERE bookId=?";
    prepStmt = connection.prepareStatement(SEARCH_BOOKID);
    prepStmt.setString(1, bookId);
    ResultSet r = prepStmt.executeQuery();
    r.next();
    int sno = r.getInt("sno");
    System.out.println("Serial No: " + sno);

    final String REMOVE_BOOK = "DELETE FROM " + BOOKS_TABLE
                              + " WHERE bookId=?";

    prepStmt = connection.prepareStatement(REMOVE_BOOK);
    prepStmt.setString(1, bookId);
    prepStmt.executeUpdate();

    final String DECREMENT_SNO = "UPDATE " + BOOKS_TABLE
                                    + " SET sno=sno-1 where sno > " + sno;
    prepStmt = connection.prepareStatement(DECREMENT_SNO);
    prepStmt.executeUpdate();

    final String FIX_AUTO_INCREMENT = "ALTER TABLE " + BOOKS_TABLE
                                        + " AUTO_INCREMENT=1";
    prepStmt = connection.prepareStatement(FIX_AUTO_INCREMENT);
    prepStmt.executeUpdate();

    System.out.println("Successfully Removed book: " + bookId+ " from the DB");
  }
  //==========[ End of: Remove Book ]===========}}}

  //============[ Get All Books ]==========={{{
  public ObservableList<BookInfo> getAllBooks() throws SQLException {
    final String FETCH_ALL = "SELECT * from " + BOOKS_TABLE;

    LinkedList<BookInfo> list = new LinkedList<>();

    stmt = connection.createStatement();
    ResultSet results = stmt.executeQuery(FETCH_ALL);
    
    while (results.next()) {
      BookInfo book = new BookInfo();
      book.setSno       (results.getInt("sno"));
      book.setBookId    (results.getString("bookId"));
      book.setTitle     (results.getString("title"));
      book.setAuthor    (results.getString("author"));
      book.setPublisher (results.getString("publisher"));
      book.setPrice     (results.getInt("price"));
      book.setCopies    (results.getInt("copies"));
      book.setBranch    (results.getString("branch"));

      list.add(book);
    }

    return FXCollections.observableList(list);
  }
  //=============[ End of: Get All Books ]=========}}}
  
  //=============[ Issue Book ]============={{{

public boolean isBookAvailable(String bookId)
    throws SQLException
{
    final String QUERY = "SELECT * from " + BOOKS_TABLE
                            + " WHERE bookId=? AND copies > 0";
    prepStmt = connection.prepareStatement(QUERY);
    prepStmt.setString(1, bookId);
    ResultSet r = prepStmt.executeQuery();
    return r.next();
}

public boolean doesBorrowerExist(String libraryId)
    throws SQLException
{
    final String QUERY = "SELECT * from " + MEMBERS
                    + " WHERE libraryId=?";
    prepStmt = connection.prepareStatement(QUERY);
    prepStmt.setString(1, libraryId);
    return prepStmt.executeQuery().next();
}

public boolean hasAlreadyBorrowed(String libraryId)
    throws SQLException
{
    final String QUERY = "SELECT * from " + MEMBERS
                    + " WHERE libraryId=?";
    prepStmt = connection.prepareStatement(QUERY);
    prepStmt.setString(1, libraryId);
    ResultSet r = prepStmt.executeQuery();
    r.next();
    return (r.getInt("borrowCount") == 1) ? true: false;

}

public boolean issueBook(BorrowerInfo  borrower) throws SQLException {

    // [ Step 3 ]: Decrement copies count from BOOKS Table 
    final String DEC_COUNT = "UPDATE " + BOOKS_TABLE
                                + " set copies = copies - 1"
                                + " WHERE bookId = ?";
    prepStmt = connection.prepareStatement(DEC_COUNT);
    prepStmt.setString(1, borrower.getBookId());
    prepStmt.executeUpdate();

    // [ Step 4]: Increment the borrowCount to 1 in MEMBERS Table
    final String INC_BORROW_COUNT = "UPDATE " + MEMBERS
                                    + " set borrowCount = borrowCount + 1"
                                    + " WHERE libraryId = ?";
    prepStmt = connection.prepareStatement(INC_BORROW_COUNT);
    prepStmt.setString(1, borrower.getLibraryId());
    prepStmt.executeUpdate();

    // [ Step 5]: Add The Borrower to the BORROWER TABLE
    final String ISSUE_BOOK = "INSERT INTO " + BORROWER_TABLE
                                    + "(bookId,libraryId,borrowerName,title,issueDate,returnDate)"
                                    + " VALUES(?,?,?,?,?,?)";
    prepStmt = connection.prepareStatement(ISSUE_BOOK);

    prepStmt.setString(1, borrower.getBookId());
    prepStmt.setString(2, borrower.getLibraryId());
    prepStmt.setString(3, borrower.getBorrowerName());
    prepStmt.setString(4, borrower.getTitle());       // getTitle() method from BookInfo
    prepStmt.setString(5, borrower.getIssueDate());
    prepStmt.setString(6, borrower.getReturnDate());

    prepStmt.executeUpdate();
    return true;
}
  //==============[ End of: Issue Book ]===========}}}

    //===============[ Return Book ]============={{{
    public boolean isThisBookBorrowed(String libraryId, String bookId)
        throws SQLException
    {
        final String QUERY = "SELECT * FROM " + MEMBERS +
                                " WHERE bookId=?";
        prepStmt = connection.prepareStatement(QUERY);
        prepStmt.setString(1, bookId);

        return prepStmt.executeQuery().next();
    }

    public int calc_days(String libraryId, String bookId)
        throws SQLException
    {
        final String QUERY = "SELECT DATEDIFF(CURDATE(), returnDate) as result from "
                                + BORROWER_TABLE
                                + " WHERE libraryId=? and bookId=?";
        prepStmt = connection.prepareStatement(QUERY);
        prepStmt.setString(1, libraryId);
        prepStmt.setString(2, bookId);
        ResultSet r = prepStmt.executeQuery();
        r.next();
        return Integer.parseInt(r.getString("result"));
    }

    public boolean returnBook(String libraryId, String bookId)
        throws SQLException
    {
        // Decrement the borrowBook Count
        final String QUERY = "UPDATE " + MEMBERS +
                                " SET borrowcount=0" +
                                " WHERE libraryId=?";
        prepStmt = connection.prepareStatement(QUERY);
        prepStmt.setString(1, libraryId);
        prepStmt.executeUpdate();

        // Increment the copies count
        final String RESET_COPIES = "UPDATE " + BOOKS_TABLE +
                                        " SET copies=copies+1" +
                                        " WHERE bookId=?";
        prepStmt = connection.prepareStatement(RESET_COPIES);
        prepStmt.setString(1, bookId);
        prepStmt.executeUpdate();

        return true;
    }
    //===============[ End: Return Book ]========}}}

  //=============[ Get All Issued Books ]============={{{
  public ObservableList<BorrowerInfo> getAllIssuedBooks() throws SQLException {
    LinkedList<BorrowerInfo> list = new LinkedList<>();

    final String FETCH_ALL = "SELECT * from " + BORROWER_TABLE;
    stmt = connection.createStatement();
    ResultSet results = stmt.executeQuery(FETCH_ALL);

    LocalDateStringConverter stringToLocalDate = new LocalDateStringConverter();
    while (results.next()) {
      BorrowerInfo borrower = new BorrowerInfo();
      borrower.setSno         ( results.getInt("sno"));
      borrower.setBookId      ( results.getString("bookId"));
      borrower.setLibraryId   ( results.getString("libraryId"));
      borrower.setBorrowerName( results.getString("borrowerName"));
      borrower.setTitle       ( results.getString("title"));    // setTitle() method from BookInfo
      borrower.setIssueDate   ( results.getString("issueDate"));
      borrower.setReturnDate  ( results.getString("returnDate"));
      borrower.setFines       ( results.getInt("fines"));

      list.add(borrower);
    }

    return FXCollections.observableList(list);
  }
  //=============[ End of: Get All Issued Books ]===========}}}

  //==============[ Does User Exist ?]=============={{{
  public boolean doesUserExist(String username, String email) 
                                            throws SQLException {
      /*
       Queries the DB for credentials of user with given username 
        and password.
            Then, if the r.next() i.e. any info exist in the returned
        ResultSet object, means user exist and r.text() returns True
            Otherwise, does not exist
        and, r.next() returns False

        */
      final String QUERY = "SELECT * from " + AUTHORIZED
                    + " where username=? and email=?";
      prepStmt = connection.prepareStatement(QUERY);
      prepStmt.setString(1, username);
      prepStmt.setString(2, email);

      ResultSet r = prepStmt.executeQuery();
      return r.next();
  }
  //==============[ End of: Does User Exist ]=============}}}

  //=============[ Is Authorized ? ]==========={{{
  public boolean isAuthorized(String username, String passwd) 
    throws SQLException {
      /*
       Queries the DB for credentials of user with given username 
        and password.
            Then, if the r.next() i.e. any info exist in the returned
        ResultSet object, means user exist and r.next() returns True
            Otherwise, does not exist
        and, r.next() returns False

        */

    final String QUERY = "SELECT * from " + AUTHORIZED
                    + " WHERE username=? and password=?";
    prepStmt = connection.prepareStatement(QUERY);
    prepStmt.setString(1, username);
    prepStmt.setString(2, passwd);

    ResultSet r = prepStmt.executeQuery();
    return r.next();
  }
  //=============[ End of: Is Authorized ]===========}}}

  //==============[ Get Security Question ]==========={{{
  public String getSecurityQuestion(String username, String email)
  throws SQLException {
      final String QUERY = "SELECT security from " + AUTHORIZED
                    + " WHERE username=? and email=?";
      prepStmt = connection.prepareStatement(QUERY);
      prepStmt.setString(1, username);
      prepStmt.setString(2, email);

      ResultSet r = prepStmt.executeQuery();
      r.next();
      return r.getString("security");
  }
  //=============[ End of: Get Security Question ]==========}}}

  //=============[ Check Security Answer ]==========={{{
  public boolean checkSecurityAnswer(String username, String answer) 
      throws SQLException {

      final String QUERY = "SELECT answer from " + AUTHORIZED
                    + " WHERE username=?";
      prepStmt = connection.prepareStatement(QUERY);
      prepStmt.setString(1, username);
      ResultSet r = prepStmt.executeQuery();
      r.next();
      return r.getString("answer").equalsIgnoreCase(answer);
  }
  //=============[ End of: Check Security Answer ]=========}}}
}
