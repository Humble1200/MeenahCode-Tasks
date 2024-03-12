
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Registration extends JDialog {


    private JTextField tflastname;

    private JTextField tffirstname;
    private JTextField tfemail;
    private JTextField tfphone;
    private JTextField tfmiddlename;
    private JTextField tfresidenceaddress;
    private JPasswordField pfpassword;
    private JPasswordField pfconfirmpassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JTextField tfState;
    private JPanel registerPanel;

    public Registration(JFrame parent){
        super(parent);
        setTitle("Create a New Account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(700,500));
        setModal(true);
        setLocationRelativeTo(parent);
        SetDefaultCloseOperation();

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void SetDefaultCloseOperation() {
    }

    private void registerUser() {
        String firstname = tffirstname.getText();
         String lastname = tflastname.getText();
         String middlename = tfmiddlename.getText();
         String email = tfemail.getText();
         if(Emailtag(email)){

         }else {
             JOptionPane.showMessageDialog(this,"invalid email address", "Error", JOptionPane.ERROR_MESSAGE);
         }

         String state = tfState.getText();
         String residenceaddress = tfresidenceaddress.getText();
         String phone = tfphone.getText();
        String password = String.valueOf(pfpassword.getPassword());
        String confirmpassword = String.valueOf(pfconfirmpassword.getPassword());

        if(firstname.isEmpty() || lastname.isEmpty() || middlename.isEmpty() || email.isEmpty() || state.isEmpty() || residenceaddress.isEmpty() || phone.isEmpty() || password.isEmpty() ){
            JOptionPane.showMessageDialog(this,"Please Enter Required Feild", "Try Agin",
           JOptionPane.ERROR_MESSAGE );
            return;

        }

        if(!password.equals(confirmpassword)){
            JOptionPane.showMessageDialog(this,"Confirm Password does not match", "Try Again",
                    JOptionPane.ERROR_MESSAGE);
            return;

        }

        user = addUserToDatabase( lastname, firstname, middlename, email, phone, state, residenceaddress, password);
        if(user != null){
            dispose();
        }else {
            JOptionPane.showMessageDialog(this, "Failed to register user", "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean Emailtag(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*]-+(.:\\.[a-zA-Z0-9_+&*]+)*@(?:[a-zA-Z0-9-]+/.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);

    }
    public User user;
    private User addUserToDatabase(String firstname, String lastname, String middlename, String state, String email,
                                   String phone, String residenceaddress, String password){
        User user = null;

        final String DB_URL = "jdbc:mysql://localhost:3306/meenacode?serverTimezone=UTC";
       // jdbc:mysql://localhost:3306/test?serverTimezone=UTC"
        final String USERNAME = "root";
        final String PASSWORD = "";

        System.out.println("Database" + USERNAME);

        try {
            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = connection.createStatement();
            String sql = "INSERT INTO users(lasttname, firstname, middlename, email, phone, state, residentialaddress, password )" +
                    "VALUE(?, ?, ?, ?, ?, ?, ?, ?)";
            System.out.println("Database try");

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, lastname);
            preparedStatement.setString(2, firstname);
            preparedStatement.setString(3, middlename);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, phone);
            preparedStatement.setString(6, state);
            preparedStatement.setString(7, residenceaddress);
            preparedStatement.setString(8, password);

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0){
                user = new User();

                user.lastname = lastname;
                user.firstname = firstname;
                user.middlename = middlename;
                user.email = email;
                user.phone = phone;
                user.state = state;
                user.residenceaddress = residenceaddress;
                user.password = password;

                System.out.println("Adding to db " + user.lastname + lastname);
            }

            stmt.close();
            connection.close();

        }catch (Exception e){
            e.fillInStackTrace();
            System.out.println("catch message " + e.getMessage() + " " + e.toString() + " " + e.getCause() + " " + e.getLocalizedMessage()
            + "" + e.getStackTrace());
        }
        return user;

    }

    public static void main(String[] args){
        Registration myForm = new Registration(null );
        User user = myForm.user;

        if (user != null){
            System.out.println("Registration Successful \n" + "You are Welcome " + user.lastname + " " + user.firstname );
        }else {
            System.out.println("Registration Cancelled");
        }
    }
}
