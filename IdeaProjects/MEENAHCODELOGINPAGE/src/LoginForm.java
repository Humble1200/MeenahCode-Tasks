import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog{

    private JButton btnlogin;
    private JTextField tfemail;
    private JButton btncancel;
    private JPasswordField pfpassword;
    private JPanel LoginPanel;

    public LoginForm(JFrame parent){
        super(parent);
        setTitle("Login Page");
        setContentPane(LoginPanel);
        setMinimumSize(new Dimension(700,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);



        btnlogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = tfemail.getText();
                String password = String.valueOf(pfpassword.getPassword());
                System.out.println("Email and passwod entered " + email + password);

                user = getAuthenticatedUser(email, password);
                if (user != null) {
                    dispose();
                }else {
                        JOptionPane.showMessageDialog(LoginForm.this, "Email or Password Invalid",
                                "Try Again", JOptionPane.ERROR_MESSAGE);
                    }

                }

            });
        btncancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }
        public User user;
        private User getAuthenticatedUser(String email, String password) {
            User user = null;

            final String DB_URL = "jdbc:mysql://localhost:3306/meenacode?serverTimezone=UTC";
            final String USERNAME = "root";
            final String PASSWORD = "";

            try {
                Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

                Statement stmt = connection.createStatement();
                String sql = "SELECT * FROM users WHERE email=? AND password=?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);

                ResultSet resultSet;
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()){
                    user = new User();
                    user.lastname = resultSet.getString("lastname");
                    user.firstname = resultSet.getString("firsttname");
                    user.middlename = resultSet.getString("middlename");
                    user.email = resultSet.getString("email");
                    user.phone = resultSet.getString("phone");
                    user.state = resultSet.getString("state");
                    user.residenceaddress = resultSet.getString("residenceaddress");
                    user.password = resultSet.getString("password");

                    stmt.close();
                    connection.close();


                }
            }

            catch (Exception e){
                e.fillInStackTrace();
                System.out.println("catch message " + e.getMessage() + " " + e.toString() + " " + e.getCause() + " " + e.getLocalizedMessage()
                        + "" + e.getStackTrace());
            }

            return user;

    }


    public static void main(String[] args) {
        LoginForm Loginform = new LoginForm(null);
        User user = Loginform.user;
        if (user != null){
            System.out.println("Successful Authentication of " + user.lastname + user.firstname);
        }else {
            System.out.println("Authentication cancelled");
        }
    }
}