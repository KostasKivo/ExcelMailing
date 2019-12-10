package com.company;

import com.company.MailAPI;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import javax.swing.*;
import java.util.List;
import java.io.File;

/*
    TODO: fix openExcelFile
 */


public class Main {

    protected static JFileChooser excelFilePath;
    protected static JFileChooser wordFilePath;
    protected static JFileChooser proofOfPaymentFolder;
    private static ArrayList<ErasmusStudents> students = new ArrayList<>();
    //Always numberOfStudents-1 because of first row with the info
    private static int numberOfStudents = 0;

    private static int posSurname, posName, posEmail, posTel;

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

       SwingUtilities.invokeLater(new Runnable() {
           @Override
           public void run() {
               MyForm f1 = new MyForm();
               f1.setVisible(true);
           }
       });
    }



    protected static void openExcelFile() {

        try {

            //getExcelFilePath();

            getInfoColumns();

            Workbook workbook = WorkbookFactory.create(new File(excelFilePath.getSelectedFile().getPath()));
            Sheet sheet = workbook.getSheetAt(0);

            //Read all non-empty rows
            for (Row row : sheet) {
                if (isRowEmpty(row)) {
                    continue;
                }
                numberOfStudents++;
            }

            Row row;
            for (int i = 1; i < numberOfStudents; i++) {

                row = sheet.getRow(i);


                students.add(new ErasmusStudents(
                        row.getCell(posName).toString(),
                        row.getCell(posSurname).toString(),
                        emailRightFormat(row.getCell(posEmail).toString()),
                        row.getCell(posTel).toString()
                ));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void createWordFiles() {

        try {
            for(int i=0;i<numberOfStudents-1;i++){

                Files.copy(wordFilePath.getSelectedFile().toPath(),
                        Paths.get(proofOfPaymentFolder.getSelectedFile().toPath() + "/1.docx"),
                        StandardCopyOption.REPLACE_EXISTING);

                XWPFDocument doc = new XWPFDocument(OPCPackage.open(proofOfPaymentFolder.getSelectedFile().toPath() + "/1.docx"));

                for (XWPFParagraph p : doc.getParagraphs()) {
                    List<XWPFRun> runs = p.getRuns();
                    if (runs != null) {
                        for (XWPFRun r : runs) {
                            String text = r.getText(0);
                            //Receipt number changer
                            if (text != null && text.contains("###")) {
                                text = text.replace("###", Integer.toString(i+1));
                                r.setText(text, 0);
                            }
                        }
                    }
                }


                //counter to find what to write where
                int j = 0;

                for (XWPFTable tbl : doc.getTables()) {
                    for (XWPFTableRow row : tbl.getRows()) {
                        for (XWPFTableCell cell : row.getTableCells()) {
                            if (j == 3) {  //Name of Erasmus student
                                cell.setText(students.get(i).getName());
                            } else if (j == 5) { //Telephone of Student
                                cell.setText(students.get(i).getSurname());
                            } else if (j == 7) { //Email of student
                                cell.setText(students.get(i).getEmail());
                            } else if (j == 16) {//Purpose of Payment
                                cell.setText("");
                            } else if (j == 17) {//Quantity
                                cell.setText("1");
                            } else if (j == 18) {//Price
                                cell.setText("");
                            } else if (j == 19) {//Amount of euros
                                cell.setText("");
                            } else if (j == 29) {//Amount of euros
                                cell.setText("");
                            } else if (j == 31) {//Total amount of euros
                                cell.setText("");
                            } else if (j == 33) {//Amount received
                                cell.setText("");
                            } else if (j == 39) {//Name of section
                                cell.setText("ESN Athens AUEB");
                            } else if (j == 41) {//Email of section
                                cell.setText("aueb@esnathens.gr");
                            } else if (j == 43) {//Vat Number
                                cell.setText("");
                            } else if (j == 45) {//Address
                                cell.setText("Evelpidon 29, Athens 11362");
                            } else if (j == 47) {//Amount received by
                                cell.setText("");
                            }
                            j++;
                        }
                    }
                }

                doc.write(new FileOutputStream(proofOfPaymentFolder.getSelectedFile().getAbsolutePath() +"/"+ students.get(i).getName() +".docx"));

                MailAPI.sendMail(students.get(i), proofOfPaymentFolder.getSelectedFile().getAbsolutePath() +"/"+ students.get(i).getName() +".docx" );

               doc.close();

            }
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param row The row that we want to check if it's empty
     * @return If the row is empty or not
     */
    private static boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK)
                return false;
        }
        return true;
    }


    /**
     * Finds the columns that we need the info for which are:
     * Name/Surname/Email/Telephone
     */
    private static void getInfoColumns() {

        try {
            Workbook workbook = WorkbookFactory.create(new File(excelFilePath.getSelectedFile().getPath()));
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();

            //Get the positions of Name,Surname,Email,Telephone
            Row firstRow = sheet.getRow(0);

            ArrayList<String> splited = new ArrayList<>();

            for (Cell cell : firstRow) {
                String cellValue = dataFormatter.formatCellValue(cell);
                splited.add(cellValue);
            }

            for (int i = 0; i < splited.size(); i++) {
                if (splited.get(i).toLowerCase().contains("sur") || splited.get(i).toLowerCase().contains("amily")) {
                    posSurname = i;
                } else if (splited.get(i).toLowerCase().contains("name")) {
                    posName = i;
                } else if (splited.get(i).toLowerCase().contains("phone")) {
                    posTel = i;
                } else if (splited.get(i).toLowerCase().contains("mail")) {
                    posEmail = i;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Return the email adress in the right format
     */
    private static String emailRightFormat(String email) {

        int[] emailIndexes = new int[4];
        int emailCharCounter = 0;

        if (email.contains("HYPERLINK")) {

            for(int i=0;i<email.length();i++)
            {
                if(email.charAt(i) == '\"')
                {
                    emailIndexes[emailCharCounter++]=i;
                }
            }

            return email.substring(emailIndexes[2]+1,emailIndexes[3]);
        }
        return email;
    }
}


