package ru.rtlabs;

import ru.rtlabs.DB.DBWorker;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class PatientAdd {
    private Integer id;
    public void add(String surname, String name, String pName, String polnumber, Date bDate, Date attachmentDate, DBWorker connection){
        String query3 ="INSERT INTO pim_party(id, type_id) values(nextval('pim_party_id_seq'), ?) RETURNING id";
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query3);
            preparedStatement.setInt(1, 1);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                this.id = resultSet.getInt(1);
                System.out.println(java.util.Calendar.getInstance().getTime() + " Фамилия: " + surname + " Имя: " + name + " Отчество: " + pName + " День Рождения: " + bDate + " добавлен в pim_party с id: " + this.id);

            }
            addIndiv(surname, name, pName, polnumber, bDate, attachmentDate, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void addIndiv(String surname, String name, String pName, String polnumber, Date bDate, Date attachmentDate, DBWorker connection){
        String query ="INSERT INTO pim_individual(id, name, patr_name, surname, birth_dt) values(?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, this.id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, pName);
            preparedStatement.setString(4, surname);
            preparedStatement.setDate(5, bDate);
            preparedStatement.execute();
            System.out.println(java.util.Calendar.getInstance().getTime() + " Фамилия: " + surname + " Имя: " + name + " Отчество: " + pName + " День Рождения: " + bDate + " добавлен в pim_individual с id: " + this.id);
            addPatient(surname, name, pName, polnumber, bDate, attachmentDate, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void addPatient(String surname, String name, String pName, String polnumber, Date bDate, Date attachmentDate, DBWorker connection){
        String query2 ="INSERT INTO pci_patient(id) values(?)";
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query2);
            preparedStatement.setInt(1, this.id);
            preparedStatement.execute();
            System.out.println(java.util.Calendar.getInstance().getTime() + " Фамилия: " + surname + " Имя: " + name + " Отчество: " + pName + " День Рождения: " + bDate + " добавлен в pci_patient с id: " + this.id);
            addDoc(surname, name, pName, polnumber, bDate, attachmentDate, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void addDoc(String surname, String name, String pName, String polnumber, Date bDate, Date attachmentDate, DBWorker connection){
        String query = "INSERT INTO pim_individual_doc (id, type_id, number, indiv_id) values(nextval('pim_individual_doc_id_seq'), ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query);
            if (polnumber.length() > 10){
                preparedStatement.setInt(1, 26);
            }else if (polnumber.startsWith("Г") || polnumber.startsWith("г")){
                preparedStatement.setInt(1, 24);
            }else {
                preparedStatement.setInt(1, 25);
            }
            preparedStatement.setString(2, polnumber);
            preparedStatement.setInt(3, this.id);
            preparedStatement.execute();
            System.out.println(java.util.Calendar.getInstance().getTime() + " Фамилия: " + surname + " Имя: " + name + " Отчество: " + pName + " День Рождения: " + bDate + " добавлен в pim_individual_doc с номером полиса: " + polnumber);
            addAttachment(surname, name, pName, polnumber, bDate, attachmentDate, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void addAttachment(String surname, String name, String pName, String polnumber, Date bDate, Date attachmentDate, DBWorker connection){
        String queryAdd = "INSERT INTO pci_patient_reg (id, reg_dt, clinic_id, type_id, patient_id, state_id) VALUES (nextval('pci_patient_reg_id_seq'), ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(queryAdd);
            preparedStatement.setDate(1, attachmentDate);
            preparedStatement.setInt(2, 10927);
            preparedStatement.setInt(3, 1);
            preparedStatement.setInt(4, this.id);
            preparedStatement.setInt(5, 1);
            preparedStatement.execute();
            System.out.println(java.util.Calendar.getInstance().getTime() + " Фамилия: " + surname + " Имя: " + name + " Отчество: " + pName + " День Рождения: " + bDate + " добавлен в pci_patient_reg с датой прикрепления " + attachmentDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
