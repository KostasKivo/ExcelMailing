package com.company;

import javax.swing.*;
import java.awt.*;

public class MyForm extends JFrame {
    private JPanel rootPanel;
    private JTextField usernameTextField;
    private JTextField passwordTextField;
    public JTextField purposeTextField;
    private JTextField priceTextField;
    private JTextField wordFileTextField;
    private JButton wordFindButton;
    private JTextField excelFileTextField;
    private JButton startSendingInvoicesButton;
    private JButton excelFindButton;
    private JButton outputFolderFindButton;
    private JTextField outputFolderTextField;


    private String getUsernameTextField() {
        return usernameTextField.getText();
    }

    private String getPasswordTextField() {
        return passwordTextField.getText();
    }

    String getPurposeTextField() {
        return purposeTextField.getText();
    }

    String getPriceTextField() {
        return priceTextField.getText();
    }

    MyForm() {
        add(rootPanel);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setTitle("Invoice Generator");
        setSize(screenSize.width,screenSize.height);

        wordFileTextField.setEditable(false);
        outputFolderTextField.setEditable(false);
        excelFileTextField.setEditable(false);


        //ActionListener for Word File
        wordFindButton.addActionListener(e -> {

            String userDir = System.getProperty("user.home");
            Main.wordFilePath = new JFileChooser(userDir + "/Desktop");
            Main.wordFilePath.setFileSelectionMode(JFileChooser.FILES_ONLY);
            Main.wordFilePath.showDialog(null, "Select the Word file");

            wordFileTextField.setText(Main.wordFilePath.getSelectedFile().getAbsolutePath());
        });

        //Action Listener for Excel File
        excelFindButton.addActionListener(e -> {

            String userDir = System.getProperty("user.home");
            Main.excelFilePath = new JFileChooser(userDir + "/Desktop");
            Main.excelFilePath.setFileSelectionMode(JFileChooser.FILES_ONLY);
            Main.excelFilePath.showDialog(null, "Select the Excel file");

            excelFileTextField.setText(Main.excelFilePath.getSelectedFile().getAbsolutePath());
        });

        //ActionLsitener for output Folder
        outputFolderFindButton.addActionListener(e -> {

            String userDir = System.getProperty("user.home");
            Main.proofOfPaymentFolder = new JFileChooser(userDir + "/Desktop");
            Main.proofOfPaymentFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            Main.proofOfPaymentFolder.showDialog(null, "Select a Directory to create the files");

            outputFolderTextField.setText(Main.proofOfPaymentFolder.getSelectedFile().getAbsolutePath());
        });

        //ActionListener when user has finished , has to check if textfields are empty
        startSendingInvoicesButton.addActionListener(e -> {

            startSendingInvoicesButton.setSelected(false);

            //Change the not for bug testing
            if(!(outputFolderTextField.getText().isEmpty() || excelFileTextField.getText().isEmpty()
                || wordFileTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()
                || usernameTextField.getText().isEmpty() || priceTextField.getText().isEmpty()
                || purposeTextField.getText().isEmpty())) {


                Main.emailUsername = getUsernameTextField();
                Main.emailPassword = getPasswordTextField();

                Main.openExcelFile();
                Main.createWordFiles();

                JOptionPane.showMessageDialog(null, "Invoices sent successfully");
                System.exit(1);

            } else {
                JOptionPane.showMessageDialog(null, "You have left something empty");
            }

        });
    }
}
