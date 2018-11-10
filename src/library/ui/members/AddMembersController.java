package library.ui.members;

import library.base.MemberInfo;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.SQLException;

import library.regex.RegexMatcher;
import library.ui.controller.Controller;

public class AddMembersController
    extends Controller
{
    @FXML private TextField field_firstName;
    @FXML private TextField field_lastName;
    @FXML private ComboBox<String> combo_branch;
    @FXML private ComboBox<String> combo_semester;

    @FXML private TextField field_rollno;
    @FXML private TextField field_email;


    @FXML private Button button_submit;
    @FXML private Button button_cancel;

    @FXML
    protected void button_submit_clicked()
        throws SQLException
    {
        String firstName = field_firstName.getText();
        String lastName  = field_lastName.getText();
        String branch = combo_branch.getValue();
        String semester = combo_semester.getValue();
        String rollno = field_rollno.getText();
        String email = field_email.getText();

        RegexMatcher regex = new RegexMatcher();
        boolean integFlag = false;
        if (regex.checkEmail(email)) {
            field_email.getStyleClass().remove("error");
            integFlag = true;
        } else {
            field_email.getStyleClass().add("error");
            integFlag = false;
        }

        if (integFlag) {
            MemberInfo member = new MemberInfo();
            member.setFirstName(firstName);
            member.setLastName(lastName);
            member.setBranch(branch);
            member.setRollNo(Integer.parseInt(rollno));
            member.setSemester(Integer.parseInt(semester));
            member.setEmail(email);

            Controller.db.addMember(member);

            super.notify("Success",
                    "New Library Member Successfully Added", "confirm").show();
        }
    }

    @FXML
    protected void button_cancel_clicked()
        throws IOException
    {
        super.goto_home(button_cancel);
    }
}
