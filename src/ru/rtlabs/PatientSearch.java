package ru.rtlabs;

import ru.rtlabs.DB.DBWorker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class PatientSearch {
    private Integer id;
    private boolean hasId;

    public void search(String surname, String name, String pName, Date bDate, Date attachmentDate, String polNumber, DBWorker connection){
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
                if (resultSet.getInt(1) == 0){
                    this.hasId = false;
                    System.out.println(java.util.Calendar.getInstance().getTime() + " Поиск по Фамилии:" + surname + " Имени: " + name + " Отчеству: " + pName + " и Дню рождени" + bDate + " не принес результатов. Поиск ведется по номеру полиса...");
                    searchPol(polNumber, connection);
                }else {
                    System.out.println(java.util.Calendar.getInstance().getTime() + " Поиск по Фамилии:" + surname + " Имени: " + name + " Отчеству: " + pName + " и Дню рождени" + bDate + " принес результаты. Id:" + resultSet.getInt(1));
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
    public void regSearch(Integer id, String surname, String name, String pName, Date bDate, Date attachmentDate, DBWorker connection){
        String sql = "select b.full_name from pci_patient_reg a JOIN pim_organization b on a.clinic_id=b.id where a.patient_id = ? and type_id = 1";
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                if (resultSet.getString(1) != null){
                    FileWriter fileWriter = new FileWriter("log.txt", true);
                    BufferedWriter writer = new BufferedWriter(fileWriter);
                    writer.write( java.util.Calendar.getInstance().getTime() + " у пациента с ID: " + id + " Имя:" + name + " Фамилия: " + surname + " Отчество: " + pName + " День Рождения: " + bDate + " Имеется прикрепление в : " + resultSet.getString(1) + " Для постоянного динамического наблюдения");
                    writer.newLine();
                    writer.flush();
                    writer.close();
                }else {
                    addAttachment(surname, name, pName, bDate, attachmentDate, id, connection);
                }
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    private void addAttachment(String surname, String name, String pName,  Date bDate, Date attachmentDate, Integer id, DBWorker connection){
        String queryAdd = "INSERT INTO pci_patient_reg (id, reg_dt, clinic_id, type_id, patient_id, state_id) VALUES (nextval('pci_patient_reg_id_seq'), ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(queryAdd);
            preparedStatement.setDate(1, attachmentDate);
            preparedStatement.setInt(2, 10927);
            preparedStatement.setInt(3, 1);
            preparedStatement.setInt(4, id);
            preparedStatement.setInt(5, 1);
            preparedStatement.execute();
            System.out.println(java.util.Calendar.getInstance().getTime() + " Фамилия: " + surname + " Имя: " + name + " Отчество: " + pName + " День Рождения: " + bDate + " добавлен в pci_patient_reg с датой прикрепления " + attachmentDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public boolean isHasId() {
        return hasId;
    }

    public Integer getId() {
        return id;
    }
}
