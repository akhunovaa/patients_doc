package ru.rtlabs;

import org.apache.poi.util.SystemOutLogger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.rtlabs.DB.DBWorker;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Parser {

    public void parse(DBWorker connection, String file){
        try {
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
            XSSFSheet sheet = wb.getSheetAt(0);
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                XSSFRow row = sheet.getRow(i);
                Patient patient = new Patient();
                PatientSearch search = new  PatientSearch();
                PatientAdd patientAdd = new PatientAdd();
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    XSSFCell cell = row.getCell(j);
                    switch (j) {
                        case 0:
                            if (cell == null) {
                                continue;
                            } else {
                                switch (cell.getCellType()) {
                                    case XSSFCell.CELL_TYPE_STRING:
                                        patient.setPolNumber(cell.getStringCellValue());
                                        break;
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        patient.setPolNumber(String.valueOf(cell.getNumericCellValue()));
                                        break;
                                }
                            }
                            break;
                        case 1:
                            if (cell == null) {
                                continue;
                            } else {
                                switch (cell.getCellType()) {
                                    case XSSFCell.CELL_TYPE_STRING:
                                        patient.setSurname(cell.getStringCellValue());
                                        break;
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        patient.setSurname(String.valueOf(cell.getNumericCellValue()));
                                        break;
                                }
                            }
                            break;
                        case 2:
                            if (cell == null) {
                                continue;
                            } else {
                                switch (cell.getCellType()) {
                                    case XSSFCell.CELL_TYPE_STRING:
                                        patient.setName(cell.getStringCellValue());
                                        break;
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        patient.setName(String.valueOf(cell.getNumericCellValue()));
                                        break;
                                }
                            }
                            break;
                        case 3:
                            if (cell == null) {
                                continue;
                            } else {
                                switch (cell.getCellType()) {
                                    case XSSFCell.CELL_TYPE_STRING:
                                        patient.setpName(cell.getStringCellValue());
                                        break;
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        patient.setpName(String.valueOf(cell.getNumericCellValue()));
                                        break;
                                }
                            }
                            break;
                        case 4:
                            if (cell == null) {
                                continue;
                            } else {
                                switch (cell.getCellType()) {
                                    case XSSFCell.CELL_TYPE_STRING:
                                        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                                        Date parsedB = format.parse(cell.getStringCellValue());
                                        java.sql.Date sqlD = new java.sql.Date(parsedB.getTime());
                                        patient.setbDate(sqlD);
                                        break;
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        DateFormat formatqq = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
                                        Date parseB = formatqq.parse(String.valueOf(cell.getDateCellValue()));
                                        java.sql.Date sqlD2 = new java.sql.Date(parseB.getTime());
                                        patient.setbDate(sqlD2);
                                        break;
                                }
                            }
                            break;
                        case 5:
                            if (cell == null) {
                                continue;
                            } else {
                                switch (cell.getCellType()) {
                                    case XSSFCell.CELL_TYPE_STRING:
                                        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                                        Date parsedB = format.parse(cell.getStringCellValue());
                                        java.sql.Date sqlD = new java.sql.Date(parsedB.getTime());
                                        patient.setAttachmentDate(sqlD);
                                        break;
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        DateFormat formatqq = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
                                        Date parseB = formatqq.parse(String.valueOf(cell.getDateCellValue()));
                                        java.sql.Date sqlD2 = new java.sql.Date(parseB.getTime());
                                        patient.setAttachmentDate(sqlD2);
                                        break;
                                }
                            }
                            break;
                    }
                }
                System.out.println(java.util.Calendar.getInstance().getTime() + " Фамилия: " + patient.getSurname() + " Имя: " + patient.getName() + " Отчество: " + patient.getpName() + " День Рождения: " + patient.getbDate() + " Дата прикрепления: " + patient.getAttachmentDate());
                System.out.println("Поиск пациента по базе данных...");
                search.search(patient.getSurname(), patient.getName(), patient.getpName(), patient.getbDate(), patient.getAttachmentDate(), patient.getPolNumber(), connection);
                if (!search.isHasId()){
                    patientAdd.add(patient.getSurname(), patient.getName(), patient.getpName(), patient.getPolNumber(), patient.getbDate(), patient.getAttachmentDate(), connection);
                }
            }
            }catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
    }




