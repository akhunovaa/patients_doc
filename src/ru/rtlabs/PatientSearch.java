package ru.rtlabs;

import ru.rtlabs.DB.DBWorker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class PatientSearch {
    private Integer id;
    private boolean hasId;


    private boolean hasDoc14;
    private boolean hasDoc1;
    private String oldDocSer1;
    private String oldDocNumber1;
    private Date  oldDocDate1;
    private String oldDocSer14;
    private String oldDocNumber14;
    private Date  oldDocDate14;


    public void search(String surname, String name, String pName, Date bDate, String polNumber, DBWorker connection){
        String sql = "select id from pim_individual where replace(upper(name), 'ё', 'е') = replace(upper(?), 'ё', 'е') and replace(upper(surname), 'ё', 'е') = replace(upper(?), 'ё', 'е') and replace(upper(patr_name), 'ё', 'е') = replace(upper(?), 'ё', 'е') and birth_dt = ?";
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
            if (name != null){
                preparedStatement.setString(1, name);
            }else {
                preparedStatement.setNull(1, java.sql.Types.VARCHAR);
            }
            if (surname != null){
                preparedStatement.setString(2, surname);
            }else {
                preparedStatement.setNull(2, java.sql.Types.VARCHAR);
            }
            if (pName != null){
                preparedStatement.setString(3, pName);
            }else {
                preparedStatement.setNull(3, java.sql.Types.VARCHAR);
            }
            if (bDate != null){
                preparedStatement.setDate(4, bDate);
            }else {
                preparedStatement.setNull(4, Types.DATE);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                if (resultSet.getInt(1) == 0 || resultSet.wasNull()){
                    this.hasId = false;
                    System.out.println(java.util.Calendar.getInstance().getTime() + " Поиск по Фамилии:" + surname + " Имени: " + name + " Отчеству: " + pName + " и Дню рождени" + bDate + " не принес результатов. Поиск ведется по номеру полиса...");
                    searchPol(polNumber, connection);
                }else {
                    System.out.println(java.util.Calendar.getInstance().getTime() + " Поиск по Фамилии:" + surname + " Имени: " + name + " Отчеству: " + pName + " и Дню рождению " + bDate + " принес результаты. Id:" + resultSet.getInt(1));
                    this.hasId = true;
                    this.id = resultSet.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void searchPol(String polnumber, DBWorker connection){
        String sql = "select a.indiv_id, b.name, b.surname, b.patr_name, b.birth_dt from pim_individual_doc a JOIN pim_individual b ON a.indiv_id = b.id where a.number = ?";
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, polnumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                if (resultSet.getInt(1) != 0){
                    this.id = resultSet.getInt(1);
                    this.hasId = true;
                    System.out.println(java.util.Calendar.getInstance().getTime() + " Поиск по полису:" + polnumber + "  принес результаты. Id:" + resultSet.getInt(1) + " Фамилия: " + resultSet.getString(3) + " Имя: " + resultSet.getString(1) + " Отчество: " + resultSet.getString(4) + " День Рождения: " + resultSet.getDate(5));
                }else {
                    System.out.println(java.util.Calendar.getInstance().getTime() + " Поиск по номеру полиса не принёс результатов...");
                    this.id = 0;
                    this.hasId = false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void hasDoc1(DBWorker connection){

        String sql = "SELECT id, series, number, issue_dt FROM pim_individual_doc where indiv_id = ? and type_id = 1";

        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, this.id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                hasDoc1 = !(resultSet.getInt(1) == 0 || resultSet.wasNull());
            }
            if (!resultSet.wasNull()){
                while (resultSet.next()){
                    oldDocSer1 = resultSet.getString(2);
                    oldDocNumber1 = resultSet.getString(3);
                    oldDocDate1 = resultSet.getDate(4);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void hasDoc14(DBWorker connection){

        String sql = "SELECT id, series, number, issue_dt FROM pim_individual_doc where indiv_id = ? and type_id = 13";
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, this.id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                hasDoc14 = !(resultSet.getInt(1) == 0 || resultSet.wasNull());
            }
            if (!resultSet.wasNull()){
                while (resultSet.next()){
                    oldDocSer14 = resultSet.getString(2);
                    oldDocNumber14 = resultSet.getString(3);
                    oldDocDate14 = resultSet.getDate(4);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isHasId() {
        return hasId;
    }


    public void docUpdate1(String series, String number, Date date, DBWorker connection){
        String sql = "UPDATE pim_individual_doc SET series = ?, number = ?, issue_dt = ? WHERE indiv_id = ? and type_id = 1";
        try {
            System.out.println(series + " " + number + " " + date + " " + this.id);
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, series);
            preparedStatement.setString(2, number);
            preparedStatement.setDate(3, date);
            preparedStatement.setInt(4, this.id);
            preparedStatement.execute();
            System.out.println(java.util.Calendar.getInstance().getTime() + " у  пациента обновлены данные по документу Свидетельство о Рождении. Серия c " + this.oldDocSer1 + " на " + series + " номер с " + this.oldDocNumber1 + " на " + number + " дата " + this.oldDocDate1 + " на " + date);
            FileWriter fileWriter = new FileWriter("log.txt", true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(java.util.Calendar.getInstance().getTime() + " у  пациента обновлены данные по документу Свидетельство о Рождении. Серия c " + this.oldDocSer1 + " на " + series + " номер с " + this.oldDocNumber1 + " на " + number + " дата " + this.oldDocDate1 + " на " + date);
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    public void docUpdate14(String series, String number, Date date, DBWorker connection){
        String sql = "UPDATE pim_individual_doc SET series = ?, number = ?, issue_dt = ? WHERE indiv_id = ? and type_id = 13";
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, series);
            preparedStatement.setString(2, number);
            preparedStatement.setDate(3, date);
            preparedStatement.setInt(4, this.id);
            preparedStatement.execute();
            System.out.println(java.util.Calendar.getInstance().getTime() + " у  пациента обновлены данные по документу Паспорт РФ. Серия c " + this.oldDocSer14 + " на " + series + " номер с " + this.oldDocNumber14 + " на " + number + " дата " + this.oldDocDate14 + " на " + date);
            FileWriter fileWriter = new FileWriter("log.txt", true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(java.util.Calendar.getInstance().getTime() + " у  пациента обновлены данные по документу Паспорт РФ. Серия c " + this.oldDocSer14 + " на " + series + " номер с " + this.oldDocNumber14 + " на " + number + " дата " + this.oldDocDate14 + " на " + date);
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void docInsert1(String series, String number, Date date, DBWorker connection){
        String sql = "INSERT INTO pim_individual_doc (id, type_id, series, number, issue_dt, indiv_id) values(nextval('pim_individual_doc_id_seq'), ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, series);
            preparedStatement.setString(3, number);
            preparedStatement.setDate(4, date);
            preparedStatement.setInt(5, this.id);
            preparedStatement.execute();
            System.out.println(java.util.Calendar.getInstance().getTime() + " пациенту добавлен документ Свидетельство о Рождении с данными " + series + " " + number + " " + date);
            FileWriter fileWriter = new FileWriter("log.txt", true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(java.util.Calendar.getInstance().getTime() + " пациенту добавлен документ Свидетельство о Рождении с данными " + series + " " + number + " " + date);
            writer.newLine();
            writer.flush();
            writer.close();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    public void docInsert14(String series, String number, Date date, DBWorker connection){
        String sql = "INSERT INTO pim_individual_doc (id, type_id, series, number, issue_dt, indiv_id) values(nextval('pim_individual_doc_id_seq'), ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, 13);
            preparedStatement.setString(2, series);
            preparedStatement.setString(3, number);
            preparedStatement.setDate(4, date);
            preparedStatement.setInt(5, this.id);
            preparedStatement.execute();
            System.out.println(java.util.Calendar.getInstance().getTime() + " пациенту добавлен документ Паспорт с данными " + series + " " + number + " " + date);
            FileWriter fileWriter = new FileWriter("log.txt", true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(java.util.Calendar.getInstance().getTime() + " пациенту добавлен документ Паспорт с данными " + series + " " + number + " " + date);
            writer.newLine();
            writer.flush();
            writer.close();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    public Integer getId() {
        return id;
    }
    public boolean isHasDoc14() {
        return hasDoc14;
    }

    public boolean isHasDoc1() {
        return hasDoc1;
    }


}
