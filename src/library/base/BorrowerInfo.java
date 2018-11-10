package library.base;

import library.base.BookInfo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.time.LocalDate;

public class BorrowerInfo extends BookInfo {
  SimpleStringProperty libraryId = new SimpleStringProperty();
  SimpleStringProperty borrowerName = new SimpleStringProperty();
  SimpleStringProperty issueDate = new SimpleStringProperty();
  SimpleStringProperty returnDate = new SimpleStringProperty();

  SimpleIntegerProperty sno = new SimpleIntegerProperty();
  SimpleIntegerProperty fines = new SimpleIntegerProperty();

  public BorrowerInfo() {}

  public BorrowerInfo(String id, String name, String iDate, 
          String rDate, int fine, int sno)
  {
      libraryId.set(id);
      borrowerName.set(name);
      issueDate.set(iDate);
      returnDate.set(rDate);
      fines.set(fine);
      this.sno.set(sno);
  }

  public void setLibraryId(String libraryId) { this.libraryId.set(libraryId); }
  public void setBorrowerName (String bName) { this.borrowerName.set(bName);  }

  // Formats java.util.Date class into MySQL like Date Representation
  public void setIssueDate(String iDate)     { issueDate.set(iDate); }
  public void setReturnDate(String rDate)    { returnDate.set(rDate); }
  public void setFines(int fine)             { this.fines.set(fine);          }
  public void setSno(int sno)                { this.sno.set(sno);             }

  public String getLibraryId()              { return libraryId.get(); }
  public String getBorrowerName()           { return borrowerName.get(); }
  public String getIssueDate()              { return issueDate.get(); }
  public String getReturnDate()             { return returnDate.get(); }
  public int getFines()                     { return fines.get(); }
  public int getSno()                       { return sno.get(); } 
}
