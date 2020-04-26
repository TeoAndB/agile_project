package rcm.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import rcm.Application;

public class LogInView extends JPanel {

    private static final long serialVersionUID = -525974348663829648L;

    private JTextField emailField = new JTextField(10);
    private JPasswordField passwordField = new JPasswordField(10);
    private JLabel lbl1 = new JLabel("Email:");
    private JLabel lbl2 = new JLabel("Password:");
    private JButton b1 = new JButton("Login");

    public LogInView(Application app) {

        JPanel panel = new JPanel(new GridBagLayout());
//        setSize(new Dimension(400, 400)); // (width, height)

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 10, 10, 10);

        // Logo
        ImageIcon image = new ImageIcon("src/main/resources/h_logo.jpg");
        JLabel logo = new JLabel(image);
        logo.setPreferredSize(new Dimension(66, 131));
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.5;
        constraints.gridwidth = 2;
        panel.add(logo, constraints);

        // Email
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(lbl1, constraints);

        constraints.gridx = 1;
        panel.add(emailField, constraints);

        // Password
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(lbl2, constraints);

        constraints.gridx = 1;
        panel.add(passwordField, constraints);

        // Login Button
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.loginUser(emailField.getText(), passwordField.getPassword());
            }
        });
        panel.add(b1, constraints);

//         set border for the panel
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Login Panel"));
        setBorder(BorderFactory
                .createCompoundBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(),
                        BorderFactory.createRaisedBevelBorder()), BorderFactory.createLoweredBevelBorder()));

        add(panel);
    }
}