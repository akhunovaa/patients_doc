package ru.rtlabs;

import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.*;
import ru.rtlabs.DB.DBWorker;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
                //PatientAdd patientAdd = new PatientAdd();
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    XSSFCell cell = row.getCell(j);
                    switch (j) {
                        case 0:
                            if (cell == null) {
                                continue;
                            } else {
                                switch (cell.getCellType()) {
                                    case XSSFCell.CELL_TYPE_STRING:
                                        patient.setSurname(cell.getStringCellValue());
                                        break;
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        patient.setSurname(String.valueOf((int)cell.getNumericCellValue()));
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
                                        patient.setName(cell.getStringCellValue());
                                        break;
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        patient.setName(String.valueOf((int)cell.getNumericCellValue()));
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
                                        patient.setpName(cell.getStringCellValue());
                                        break;
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        patient.setpName(String.valueOf((int)cell.getNumericCellValue()));
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
                                        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                                        Date parsedB = format.parse(cell.getStringCellValue());
                                        java.sql.Date sqlD = new java.sql.Date(parsedB.getTime());
                                        patient.setBdate(sqlD);
                                        break;
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        DateFormat formatqq = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
                                        Date parseB = formatqq.parse(String.valueOf(cell.getDateCellValue()));
                                        java.sql.Date sqlD2 = new java.sql.Date(parseB.getTime());
                                        patient.setBdate(sqlD2);
                                        break;
                                }
                            }
                            break;
                        case 4:
                            if (cell == null) {
                                continue;
                            }
                            else {
                                switch (cell.getCellType()) {
                                    case XSSFCell.CELL_TYPE_STRING:
                                        patient.setDocType(cell.getStringCellValue());
                                        break;
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        patient.setDocType(String.valueOf((int)cell.getNumericCellValue()));
                                        break;
                                }
                            }
                            break;
                        case 5:
                            if (cell == null) {
                                continue;
                            }
                            else {
                                switch (cell.getCellType()) {
                                    case XSSFCell.CELL_TYPE_STRING:
                                        patient.setDocSer(cell.getStringCellValue());
                                        break;
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        patient.setDocSer(String.valueOf((int)cell.getNumericCellValue()));
                                        break;
                                }
                            }
                            break;
                        case 6:
                            if (cell == null) {
                                continue;
                            }
                            else {
                                switch (cell.getCellType()) {
                                    case XSSFCell.CELL_TYPE_STRING:
                                        patient.setDocNumber(cell.getStringCellValue());
                                        break;
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        patient.setDocNumber(String.valueOf((int)cell.getNumericCellValue()));
                                        break;
                                }
                            }
                            break;
                        case 10:
                            if (cell == null) {
                                continue;
                            }
                            else {
                                switch (cell.getCellType()) {
                                    case XSSFCell.CELL_TYPE_STRING:
                                        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                                        Date parsedB = format.parse(cell.getStringCellValue());
                                        java.sql.Date sqlD = new java.sql.Date(parsedB.getTime());
                                        patient.setDocDate(sqlD);
                                        break;
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        DateFormat formatqq = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
                                        Date parseB = formatqq.parse(String.valueOf(cell.getDateCellValue()));
                                        java.sql.Date sqlD2 = new java.sql.Date(parseB.getTime());
                                        patient.setDocDate(sqlD2);
                                        break;
                                    case XSSFCell.CELL_TYPE_FORMULA:
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(patient.getBdate());
                                        cal.add(Calendar.DATE, 5144);
                                        java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());
                                        patient.setDocDate(sqlDate);
                                        System.out.println(sqlDate);
                                        break;
                                }
                            }
                            break;

                    }
                }
                FileWriter fileWriter2 = new FileWriter("log.txt", true);
                BufferedWriter writer2 = new BufferedWriter(fileWriter2);
                writer2.write(java.util.Calendar.getInstance().getTime() + " Фамилия: " + patient.getSurname() + " Имя: " + patient.getName() + " Отчество: " + patient.getpName() + " День Рождения: " + patient.getBdate());
                writer2.newLine();
                writer2.flush();
                writer2.close();
                System.out.println(java.util.Calendar.getInstance().getTime() + " Фамилия: " + patient.getSurname() + " Имя: " + patient.getName() + " Отчество: " + patient.getpName() + " День Рождения: " + patient.getBdate());
                System.out.println("Поиск пациента по базе данных...");
                search.search(patient.getSurname(), patient.getName(), patient.getpName(), patient.getBdate(), patient.getPolNumber(), connection);
                if (patient.getBdate() != null && patient.getDocDate() != null){
                    if (patient.getBdate().after(patient.getDocDate()) ){
                        System.out.println(java.util.Calendar.getInstance().getTime() + " у  пациента дата выдачи документа меньше даты рождения " + patient.getSurname() + " " + patient.getName() + " " + patient.getpName() + " День Рождения " + patient.getBdate() + " Дата Выдачи" + patient.getDocDate());
                        FileWriter fileWriter = new FileWriter("log.txt", true);
                        BufferedWriter writer = new BufferedWriter(fileWriter);
                        writer.write(java.util.Calendar.getInstance().getTime() + " у  пациента дата выдачи документа меньше даты рождения " + patient.getSurname() + " " + patient.getName() + " " + patient.getpName() + " День Рождения " + patient.getBdate() + " Дата Выдачи" + patient.getDocDate());
                        writer.newLine();
                        writer.flush();
                        writer.close();
                    }
                }else {
                    System.out.println(java.util.Calendar.getInstance().getTime() + " ошибка в сравнении дат Рождения и Даты Документа. Отсутствует какая то из дат. " + patient.getSurname() + " " + patient.getName() + " " + patient.getpName() + " День Рождения " + patient.getBdate() + " Дата Выдачи" + patient.getDocDate());
                    FileWriter fileWriter = new FileWriter("log.txt", true);
                    BufferedWriter writer = new BufferedWriter(fileWriter);
                    writer.write(java.util.Calendar.getInstance().getTime() + " ошибка в сравнении дат Рождения и Даты Документа. Отсутствует какая то из дат. " + patient.getSurname() + " " + patient.getName() + " " + patient.getpName() + " День Рождения " + patient.getBdate() + " Дата Выдачи" + patient.getDocDate());
                    writer.newLine();
                    writer.flush();
                    writer.close();
                }

                if (search.isHasId()){
                    if (patient.getDocSer() != null && patient.getDocNumber() != null && patient.getDocDate() != null){
                        search.hasDoc14(connection);
                        if (search.isHasDoc14()){
                            search.docUpdate14(patient.getDocSer(), patient.getDocNumber(), patient.getDocDate(), connection);
                        }else {
                            search.docInsert14(patient.getDocSer(), patient.getDocNumber(), patient.getDocDate(), connection);
                        }
                        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
                        FileWriter fileWriter = new FileWriter("log.txt", true);
                        BufferedWriter writer = new BufferedWriter(fileWriter);
                        writer.write("--------------------------------------------------------------------------------------------------------------------------------------");
                        writer.newLine();
                        writer.flush();
                        writer.close();
                    }else {
                        FileWriter fileWriter = new FileWriter("log.txt", true);
                        BufferedWriter writer = new BufferedWriter(fileWriter);
                        writer.write(java.util.Calendar.getInstance().getTime() + " у  пациента недостаточно данных по документам для вставки в РМИС (отсутствует Серия или Номер или Дата) " + patient.getSurname() + " " + patient.getName() + " " + patient.getpName() + " День Рождения " + patient.getBdate());
                        writer.newLine();
                        writer.write("--------------------------------------------------------------------------------------------------------------------------------------");
                        writer.newLine();
                        writer.flush();
                        writer.close();
                    }
                }else {
                    System.out.println(java.util.Calendar.getInstance().getTime() + " не найден в РМИС " + patient.getSurname() + " " + patient.getName() + " " + patient.getpName() + " День Рождения " + patient.getBdate());
                    FileWriter fileWriter = new FileWriter("log.txt", true);
                    BufferedWriter writer = new BufferedWriter(fileWriter);
                    writer.write(java.util.Calendar.getInstance().getTime() + " не найден в РМИС " + patient.getSurname() + " " + patient.getName() + " " + patient.getpName() + " День Рождения " + patient.getBdate());
                    writer.newLine();
                    writer.flush();
                    writer.close();
                }

            }
            }catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
    }