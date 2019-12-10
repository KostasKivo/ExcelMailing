package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.company.Main;

public class MyForm extends JFrame {
    private JPanel rootPanel;
    protected JTextField usernameTextField;
    protected JTextField passwordTextField;
    private JTextField purposeTextField;
    private JTextField priceTextField;
    private JTextField wordFileTextField;
    private JButton wordFindButton;
    private JTextField excelFileTextField;
    private JButton startSendingInvoicesButton;
    private JButton excelFindButton;
    private JButton outputFolderFindButton;
    private JTextField outputFolderTextField;

    public MyForm() {
        add(rootPanel);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setTitle("Invoice Generator");
        setSize(screenSize.width,screenSize.height);

        wordFileTextField.setEditable(false);
        excelFileTextField.setEditable(false);
        outputFolderTextField.setEditable(false);


        //ActionListener for Word File
        wordFindButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String userDir = System.getProperty("user.home");
                Main.wordFilePath = new JFileChooser(userDir + "/Desktop");
                Main.wordFilePath.setFileSelectionMode(JFileChooser.FILES_ONLY);
                Main.wordFilePath.showDialog(null, "Select the Word file");

                wordFileTextField.setText(Main.wordFilePath.getSelectedFile().getAbsolutePath());
            }
        });

        //Action Listener for Excel File
        excelFindButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String userDir = System.getProperty("user.home");
                Main.excelFilePath = new JFileChooser(userDir + "/Desktop");
                Main.excelFilePath.setFileSelectionMode(JFileChooser.FILES_ONLY);
                Main.excelFilePath.showDialog(null, "Select the Excel file");

                excelFileTextField.setText(Main.excelFilePath.getSelectedFile().getAbsolutePath());
            }
        });

        //ActionLsitener for output Folder
        outputFolderFindButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String userDir = System.getProperty("user.home");
                Main.proofOfPaymentFolder = new JFileChooser(userDir + "/Desktop");
                Main.proofOfPaymentFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                Main.proofOfPaymentFolder.showDialog(null, "Select a Directory to create the files");

                outputFolderTextField.setText(Main.proofOfPaymentFolder.getSelectedFile().getAbsolutePath());
            }
        });

        //ActionListener when user has finished , has to check if textfields are empty
        startSendingInvoicesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(!(outputFolderTextField.getText().isEmpty() || excelFileTextField.getText().isEmpty()
                    || wordFileTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()
                    || usernameTextField.getText().isEmpty() || priceTextField.getText().isEmpty()
                    || purposeTextField.getText().isEmpty())) {

                    // TODO: start actions
                    //Main.openExcelFile();
                    System.out.println(Main.excelFilePath.getSelectedFile().getPath());

                } else {
                    JOptionPane.showMessageDialog(null, "You have left something empty");
                }

            }
        });
    }
}
