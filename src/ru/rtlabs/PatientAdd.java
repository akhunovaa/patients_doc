package ru.rtlabs;

import ru.rtlabs.DB.DBWorker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;


public class PatientAdd {
    private Integer id;
    public void add(String surname, String name, String pName, Date bDate,DBWorker connection){
        String query3 ="INSERT INTO pim_party(id, type_id) values(nextval('pim_party_id_seq'), ?) RETURNING id";
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query3);
            preparedStatement.setInt(1, 1);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                this.id = resultSet.getInt(1);
                System.out.println(java.util.Calendar.getInstance().getTime() + " Фамилия: " + surname + " Имя: " + name + " Отчество: " + pName + " День Рождения: " + bDate + " добавлен в pim_party с id: " + this.id);
            }
            addIndiv(surname, name, pName,  bDate, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void addIndiv(String surname, String name, String pName, Date bDate, DBWorker connection){
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
            addPatient(surname, name, pName, bDate, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void addPatient(String surname, String name, String pName, Date bDate,  DBWorker connection){
        String query2 ="INSERT INTO pci_patient(id) values(?)";
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query2);
            preparedStatement.setInt(1, this.id);
            preparedStatement.execute();
            System.out.println(java.util.Calendar.getInstance().getTime() + " Фамилия: " + surname + " Имя: " + name + " Отчество: " + pName + " День Рождения: " + bDate + " добавлен в pci_patient с id: " + this.id);
            //addDoc(surname, name, pName, polnumber, bDate, attachmentDate, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addDocD(String polnumber, String snils, String polType, String codeSmo, DBWorker connection, Integer idD){

        String query = "INSERT INTO pim_individual_doc (id, type_id, series, number, issuer_id, indiv_id) values(nextval('pim_individual_doc_id_seq'), ?, ?, ?, ?, ?)";
        System.out.println("КОД СМО: " + codeSmo);
        System.out.println("Номер полиса: " + polnumber);
        System.out.println("Тип полиса: " + polType);

        try {
            if (polnumber != null && polType != null && codeSmo != null) {
                Integer smo = searchSmo(codeSmo, connection);
                System.out.println("ИД СМО: " + smo);
                PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query);
                if (polType.equals("4") || polType.equals("3")) {
                    preparedStatement.setInt(1, 26);
                    preparedStatement.setNull(2, Types.VARCHAR);
                    preparedStatement.setString(3, polnumber);
                } else if (polType.equals("2")) {
                    preparedStatement.setInt(1, 25);
                    preparedStatement.setNull(2, Types.VARCHAR);
                    preparedStatement.setString(3, polnumber);
                } else {
                    preparedStatement.setInt(1, 24);
                    preparedStatement.setString(2, polnumber.substring(0, 2));
                    preparedStatement.setString(3, polnumber.substring(2));
                }
                preparedStatement.setInt(4, smo);
                preparedStatement.setInt(5, idD);
                preparedStatement.execute();
                System.out.println(java.util.Calendar.getInstance().getTime() + " " + idD +  ": добавлен в pim_individual_doc с номером полиса: " + polnumber);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addDocSnils(String snils, DBWorker connection, Integer idD){
        String query2 = "INSERT INTO pim_individual_doc (id, type_id, number, indiv_id) values(nextval('pim_individual_doc_id_seq'), ?, ?, ?)";
        try {
            if (snils != null){
                PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query2);
                preparedStatement.setInt(1, 19);
                preparedStatement.setString(2, snils);
                preparedStatement.setInt(3, idD);
                preparedStatement.execute();
                System.out.println(java.util.Calendar.getInstance().getTime() + " " + idD +  ": добавлен в pim_individual_doc с номером СНИЛС'а: " + snils);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addPhoneD(String phone, DBWorker connection, Integer idD){
        String sql = "INSERT into pim_indiv_contact(id, value, indiv_id, type_id) VALUES (nextval('pim_indiv_contact_id_seq'), ?, ?, ?)";
        try {
            if (phone != null){
                PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
                preparedStatement.setString(1, phone);
                preparedStatement.setInt(2, idD);
                if (phone.length() < 9){
                    preparedStatement.setInt(3, 3);
                }else {
                    preparedStatement.setInt(3, 2);
                }
                preparedStatement.execute();
                System.out.println(java.util.Calendar.getInstance().getTime() + " " + idD +  ": добавлен в pim_indiv_contact с номером телефона'а: " + phone);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void adrAddRD(String streedCode, String houseR, String roomR, DBWorker connection, Integer IdD){
        String sql = "INSERT INTO address_element(id, name, level_id, parent_id, type_id) VALUES (nextval('address_element_id_seq'), ?, ?, ?, ?) RETURNING id";
        String sql2 = "INSERT INTO pim_party_address(id, addr_id, party_id, register_type_id, is_valid) VALUES (nextval('pim_party_address_id_seq'), ?, ?, ?, TRUE) RETURNING id";
        String sql3 = "INSERT INTO pim_party_addr_to_addr_type(id, address_type_id, party_address_id) VALUES (nextval('pim_party_address_id_seq'), 4, ?)";

        if (streedCode != null){
            //Integer parentIdNasR = parentIdNas(naspCode, connection);
            Integer parentIdStreetR = parentIdStreet(streedCode, connection);
            Integer parentIdRHouse = 0;
            Integer parentIdRRoom = 0;
            Integer addrId = 0;
            try {
                if(houseR != null){
                    PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
                    preparedStatement.setString(1, houseR);
                    preparedStatement.setInt(2, 7);
                    preparedStatement.setInt(3, parentIdStreetR);
                    preparedStatement.setInt(4, 22);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()){
                        parentIdRHouse =  resultSet.getInt(1);
                        System.out.println("address_element номер дома: " + houseR);
                    }
                }
               if (roomR != null){
                   PreparedStatement preparedStatement1 = connection.getConnection().prepareStatement(sql);
                   preparedStatement1.setString(1, roomR);
                   preparedStatement1.setInt(2, 8);
                   preparedStatement1.setInt(3, parentIdRHouse);
                   preparedStatement1.setInt(4, 109);
                   ResultSet resultSet1 = preparedStatement1.executeQuery();
                   while (resultSet1.next()){
                       parentIdRRoom =  resultSet1.getInt(1);
                       System.out.println("address_element вставлен номер квартиры: " + roomR);
                   }
               }
                PreparedStatement preparedStatement2 = connection.getConnection().prepareStatement(sql2);
                if (parentIdRRoom == 0 && parentIdRHouse != 0){
                    preparedStatement2.setInt(1, parentIdRHouse);
                }else if ( parentIdRHouse == 0 && parentIdRRoom == 0){
                    preparedStatement2.setInt(1, parentIdStreetR);
                }else {
                    preparedStatement2.setInt(1, parentIdRRoom);
                }
                preparedStatement2.setInt(2, IdD);
                preparedStatement2.setInt(3, 1);
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                while (resultSet2.next()){
                    addrId = resultSet2.getInt(1);
                    System.out.println("Для пациента вставлен адрес: " + houseR + " " + roomR);
                }
                PreparedStatement preparedStatement3 = connection.getConnection().prepareStatement(sql3);
                preparedStatement3.setInt(1, addrId);
                preparedStatement3.execute();
                System.out.println("Для пациента вставлен адрес как место регситрации: " + houseR + " " + roomR);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void adrAddLD(String streedCode, String houseR, String roomR, DBWorker connection, Integer idD){
        String sql = "INSERT INTO address_element(id, name, level_id, parent_id, type_id) VALUES (nextval('address_element_id_seq'), ?, ?, ?, ?) RETURNING id";
        String sql2 = "INSERT INTO pim_party_address(id, addr_id, party_id, register_type_id, is_valid) VALUES (nextval('pim_party_address_id_seq'), ?, ?, ?, TRUE) RETURNING id";
        String sql3 = "INSERT INTO pim_party_addr_to_addr_type(id, address_type_id, party_address_id) VALUES (nextval('pim_party_address_id_seq'), 3, ?)";

        if (streedCode != null){
            //Integer parentIdNasR = parentIdNas(naspCode, connection);
            Integer parentIdStreetR = parentIdStreet(streedCode, connection);
            Integer parentIdRHouse = 0;
            Integer parentIdRRoom = 0;
            Integer addrId = 0;
            try {
                if (houseR != null){
                    PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
                    preparedStatement.setString(1, houseR);
                    preparedStatement.setInt(2, 7);
                    preparedStatement.setInt(3, parentIdStreetR);
                    preparedStatement.setInt(4, 22);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()){
                        parentIdRHouse =  resultSet.getInt(1);
                        System.out.println("address_element номер дома: " + houseR);
                    }
                }

                if (roomR != null){
                    PreparedStatement preparedStatement1 = connection.getConnection().prepareStatement(sql);
                    preparedStatement1.setString(1, roomR);
                    preparedStatement1.setInt(2, 8);
                    preparedStatement1.setInt(3, parentIdRHouse);
                    preparedStatement1.setInt(4, 109);
                    ResultSet resultSet1 = preparedStatement1.executeQuery();
                    while (resultSet1.next()){
                        parentIdRRoom =  resultSet1.getInt(1);
                        System.out.println("address_element вставлен номер квартиры: " + roomR);
                    }
                }

                PreparedStatement preparedStatement2 = connection.getConnection().prepareStatement(sql2);
                if (parentIdRRoom == 0 && parentIdRHouse != 0){
                    preparedStatement2.setInt(1, parentIdRHouse);
                }else if ( parentIdRHouse == 0 && parentIdRRoom == 0){
                    preparedStatement2.setInt(1, parentIdStreetR);
                }else {
                    preparedStatement2.setInt(1, parentIdRRoom);
                }
                preparedStatement2.setInt(2, idD);
                preparedStatement2.setInt(3, 1);
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                while (resultSet2.next()){
                    addrId = resultSet2.getInt(1);
                    System.out.println("Для пациента вставлен адрес: " + houseR + " " + roomR);
                }
                PreparedStatement preparedStatement3 = connection.getConnection().prepareStatement(sql3);
                preparedStatement3.setInt(1, addrId);
                preparedStatement3.execute();
                System.out.println("Для пациента вставлен адрес как место проживания: " + houseR + " " + roomR);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void attachmentAddD(Date attachmentStart, String clinicId, DBWorker connection, Integer IdD){
        String sql = "INSERT INTO pci_patient_reg(id, reg_dt, clinic_id, patient_id, state_id, type_id) VALUES (nextval('pci_patient_reg_id_seq'), ?, ?, ?, 1, 1)";
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
            preparedStatement.setDate(1, attachmentStart);
            preparedStatement.setInt(2, Integer.valueOf(clinicId));
            preparedStatement.setInt(3, IdD);
            preparedStatement.execute();
            System.out.println("Пациен: " + IdD + " прикреплен к МО: " + clinicId + " c датой прикрепления: " + attachmentStart);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addDoc(String polnumber, String snils, String polType, String codeSmo, DBWorker connection){

        String query = "INSERT INTO pim_individual_doc (id, type_id, series, number, issuer_id, indiv_id) values(nextval('pim_individual_doc_id_seq'), ?, ?, ?, ?, ?)";
        String query2 = "INSERT INTO pim_individual_doc (id, type_id, number, indiv_id) values(nextval('pim_individual_doc_id_seq'), ?, ?, ?)";
        try {
            if (polnumber != null && polType != null && codeSmo != null) {
                Integer smo = searchSmo(codeSmo, connection);
                PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query);
                if (polType.equals("4") || polType.equals("3")) {
                    preparedStatement.setInt(1, 26);
                    preparedStatement.setNull(2, Types.VARCHAR);
                    preparedStatement.setString(3, polnumber);
                } else if (polType.equals("2")) {
                    preparedStatement.setInt(1, 25);
                    preparedStatement.setNull(2, Types.VARCHAR);
                    preparedStatement.setString(3, polnumber);
                } else {
                    preparedStatement.setInt(1, 24);
                    preparedStatement.setString(2, polnumber.substring(0, 2));
                    preparedStatement.setString(3, polnumber.substring(2));
                }
                preparedStatement.setInt(4, smo);
                preparedStatement.setInt(5, this.id);
                preparedStatement.execute();
                System.out.println(java.util.Calendar.getInstance().getTime() + " " + this.id +  ": добавлен в pim_individual_doc с номером полиса: " + polnumber);
            }

            if (snils != null){
                PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query2);
                preparedStatement.setInt(1, 19);
                preparedStatement.setString(2, snils);
                preparedStatement.setInt(3, this.id);
                preparedStatement.execute();
                System.out.println(java.util.Calendar.getInstance().getTime() + " " + this.id +  ": добавлен в pim_individual_doc с номером СНИЛС'а: " + snils);
            }
            //addAttachment(surname, name, pName, polnumber, bDate, attachmentDate, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addPhone(String phone, DBWorker connection){
        String sql = "INSERT into pim_indiv_contact(id, value, indiv_id, type_id) VALUES (nextval('pim_indiv_contact_id_seq'), ?, ?, ?)";
        try {
            if (phone != null){
                PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
                preparedStatement.setString(1, phone);
                preparedStatement.setInt(2, this.id);
                if (phone.length() < 9){
                    preparedStatement.setInt(3, 3);
                }else {
                    preparedStatement.setInt(3, 2);
                }
                preparedStatement.execute();
                //System.out.println(java.util.Calendar.getInstance().getTime() + " " + this.id +  ": добавлен в pim_indiv_contact с номером телефона'а: " + phone);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void adrAddR(String streedCode, String houseR, String roomR, DBWorker connection){
        String sql = "INSERT INTO address_element(id, name, level_id, parent_id, type_id) VALUES (nextval('address_element_id_seq'), ?, ?, ?, ?) RETURNING id";
        String sql2 = "INSERT INTO pim_party_address(id, addr_id, party_id, register_type_id, is_valid) VALUES (nextval('pim_party_address_id_seq'), ?, ?, ?, TRUE) RETURNING id";
        String sql3 = "INSERT INTO pim_party_addr_to_addr_type(id, address_type_id, party_address_id) VALUES (nextval('pim_party_address_id_seq'), 4, ?)";

        if (streedCode != null){
            //Integer parentIdNasR = parentIdNas(naspCode, connection);
            Integer parentIdStreetR = parentIdStreet(streedCode, connection);
            Integer parentIdRHouse = 0;
            Integer parentIdRRoom = 0;
            Integer addrId = 0;
            try {
                if (houseR != null){
                    PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
                    preparedStatement.setString(1, houseR);
                    preparedStatement.setInt(2, 7);
                    preparedStatement.setInt(3, parentIdStreetR);
                    preparedStatement.setInt(4, 22);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()){
                        parentIdRHouse =  resultSet.getInt(1);
                        System.out.println("address_element номер дома: " + houseR);
                    }
                }

                if (roomR != null){
                    PreparedStatement preparedStatement1 = connection.getConnection().prepareStatement(sql);
                    preparedStatement1.setString(1, roomR);
                    preparedStatement1.setInt(2, 8);
                    preparedStatement1.setInt(3, parentIdRHouse);
                    preparedStatement1.setInt(4, 109);
                    ResultSet resultSet1 = preparedStatement1.executeQuery();
                    while (resultSet1.next()){
                        parentIdRRoom =  resultSet1.getInt(1);
                        System.out.println("address_element вставлен номер квартиры: " + roomR);
                    }
                }

                PreparedStatement preparedStatement2 = connection.getConnection().prepareStatement(sql2);
              if (parentIdRRoom == 0 && parentIdRHouse != 0){
                  preparedStatement2.setInt(1, parentIdRHouse);
              }else if ( parentIdRHouse == 0 && parentIdRRoom == 0){
                  preparedStatement2.setInt(1, parentIdStreetR);
              }else {
                  preparedStatement2.setInt(1, parentIdRRoom);
              }
                preparedStatement2.setInt(2, this.id);
                preparedStatement2.setInt(3, 1);
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                while (resultSet2.next()){
                    addrId = resultSet2.getInt(1);
                    System.out.println("Для пациента вставлен адрес: " + houseR + " " + roomR);
                }
                PreparedStatement preparedStatement3 = connection.getConnection().prepareStatement(sql3);
                preparedStatement3.setInt(1, addrId);
                preparedStatement3.execute();
                System.out.println("Для пациента вставлен адрес как место регситрации: " + houseR + " " + roomR);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void adrAddL(String streedCode, String houseR, String roomR, DBWorker connection){
        String sql = "INSERT INTO address_element(id, name, level_id, parent_id, type_id) VALUES (nextval('address_element_id_seq'), ?, ?, ?, ?) RETURNING id";
        String sql2 = "INSERT INTO pim_party_address(id, addr_id, party_id, register_type_id, is_valid) VALUES (nextval('pim_party_address_id_seq'), ?, ?, ?, TRUE) RETURNING id";
        String sql3 = "INSERT INTO pim_party_addr_to_addr_type(id, address_type_id, party_address_id) VALUES (nextval('pim_party_address_id_seq'), 3, ?)";

        if (streedCode != null){
            //Integer parentIdNasR = parentIdNas(naspCode, connection);
            Integer parentIdStreetR = parentIdStreet(streedCode, connection);
            Integer parentIdRHouse = 0;
            Integer parentIdRRoom = 0;
            Integer addrId = 0;
            try {
                if (houseR != null){
                    PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
                    preparedStatement.setString(1, houseR);
                    preparedStatement.setInt(2, 7);
                    preparedStatement.setInt(3, parentIdStreetR);
                    preparedStatement.setInt(4, 22);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()){
                        parentIdRHouse =  resultSet.getInt(1);
                        System.out.println("address_element номер дома: " + houseR);
                    }
                }
                if (roomR != null){
                    PreparedStatement preparedStatement1 = connection.getConnection().prepareStatement(sql);
                    preparedStatement1.setString(1, roomR);
                    preparedStatement1.setInt(2, 8);
                    preparedStatement1.setInt(3, parentIdRHouse);
                    preparedStatement1.setInt(4, 109);
                    ResultSet resultSet1 = preparedStatement1.executeQuery();
                    while (resultSet1.next()){
                        parentIdRRoom =  resultSet1.getInt(1);
                        System.out.println("address_element вставлен номер квартиры: " + roomR);
                    }
                }

                PreparedStatement preparedStatement2 = connection.getConnection().prepareStatement(sql2);
                if (parentIdRRoom == 0 && parentIdRHouse != 0){
                    preparedStatement2.setInt(1, parentIdRHouse);
                }else if ( parentIdRHouse == 0 && parentIdRRoom == 0){
                    preparedStatement2.setInt(1, parentIdStreetR);
                }else {
                    preparedStatement2.setInt(1, parentIdRRoom);
                }

                preparedStatement2.setInt(2, this.id);
                preparedStatement2.setInt(3, 1);
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                while (resultSet2.next()){
                    addrId = resultSet2.getInt(1);
                    System.out.println("Для пациента вставлен адрес: " + houseR + " " + roomR);
                }
                PreparedStatement preparedStatement3 = connection.getConnection().prepareStatement(sql3);
                preparedStatement3.setInt(1, addrId);
                preparedStatement3.execute();
                System.out.println("Для пациента вставлен адрес как место проживания: " + houseR + " " + roomR);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private Integer parentIdNas(String code, DBWorker connection){
        String sql = "select b.id, b.name from address_code a join address_element b on a.element_id = b.id WHERE a.value = ?";
        Integer parentId = 0;
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                parentId = resultSet.getInt(1);
                System.out.println("Населенный пункт у пациента: " + resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parentId;
    }
    private Integer parentIdStreet(String code, DBWorker connection){
        String sql = "select b.id, b.name from address_code a join address_element b on a.element_id = b.id WHERE a.value = ?";
        Integer parentId = 0;
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                parentId = resultSet.getInt(1);
                System.out.println("Улица у пациента: " + resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parentId;
    }
    private Integer searchSmo(String code, DBWorker connection){
        String sql = "select id from pim_organization WHERE code like ?";
        Integer smo = 0;
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, "560%" + code);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                smo = resultSet.getInt(1);
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }

        return smo;
    }
    public void attachmentAdd(Date attachmentStart, String clinicId, DBWorker connection){
        String sql = "INSERT INTO pci_patient_reg(id, reg_dt, clinic_id, patient_id, state_id, type_id) VALUES (nextval('pci_patient_reg_id_seq'), ?, ?, ?, 1, 1)";
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
            preparedStatement.setDate(1, attachmentStart);
            preparedStatement.setInt(2, Integer.valueOf(clinicId));
            preparedStatement.setInt(3, this.id);
            preparedStatement.execute();
            System.out.println("Пациен: " + this.id + " прикреплен к МО: " + clinicId + " c датой прикрепления: " + attachmentStart);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void addDocDE26(String polnumber, String polType, String codeSmo, DBWorker connection, Integer idD){

        String query = "UPDATE pim_individual_doc set number = ?, issuer_id = ? where indiv_id = ? and type_id = 26";
        //String query = "INSERT INTO pim_individual_doc (id, type_id, series, number, issuer_id, indiv_id) values(nextval('pim_individual_doc_id_seq'), ?, ?, ?, ?, ?)";
        System.out.println("КОД СМО: " + codeSmo);
        System.out.println("Номер полиса: " + polnumber);
        System.out.println("Тип полиса: " + polType);
        try {
            if (polnumber != null && polType != null && codeSmo != null) {
                Integer smo = searchSmo(codeSmo, connection);
                PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query);
                    preparedStatement.setString(1, polnumber);
                    preparedStatement.setInt(2, smo);
                    preparedStatement.setInt(3, idD);
                    preparedStatement.execute();
                System.out.println(java.util.Calendar.getInstance().getTime() + " " + idD +  ": сделан UPDATE в pim_individual_doc ЕНП с номером : " + polnumber);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addDocDE24(String polnumber, String polType, String codeSmo, DBWorker connection, Integer idD){

        String query = "UPDATE pim_individual_doc set series = ?, number = ?, issuer_id = ? where indiv_id = ? and type_id = 24";
        //String query = "INSERT INTO pim_individual_doc (id, type_id, series, number, issuer_id, indiv_id) values(nextval('pim_individual_doc_id_seq'), ?, ?, ?, ?, ?)";
        System.out.println("КОД СМО: " + codeSmo);
        System.out.println("Номер полиса: " + polnumber);
        System.out.println("Тип полиса: " + polType);

        try {
            if (polnumber != null && polType != null && codeSmo != null) {
                Integer smo = searchSmo(codeSmo, connection);
                PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query);
                preparedStatement.setString(1, polnumber.substring(0, 2));
                preparedStatement.setString(2, polnumber.substring(2));
                preparedStatement.setInt(3, smo);
                preparedStatement.setInt(4, idD);
                preparedStatement.execute();
                System.out.println(java.util.Calendar.getInstance().getTime() + " " + idD +  ": добавлен в pim_individual_doc с номером полиса: " + polnumber);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addDocDE25(String polnumber, String polType, String codeSmo, DBWorker connection, Integer idD){

        String query = "UPDATE pim_individual_doc set number = ?, issuer_id = ? where indiv_id = ? and type_id = 25";
        //String query = "INSERT INTO pim_individual_doc (id, type_id, series, number, issuer_id, indiv_id) values(nextval('pim_individual_doc_id_seq'), ?, ?, ?, ?, ?)";
        System.out.println("КОД СМО: " + codeSmo);
        System.out.println("Номер полиса: " + polnumber);
        System.out.println("Тип полиса: " + polType);

        try {
            if (polnumber != null && polType != null && codeSmo != null) {
                Integer smo = searchSmo(codeSmo, connection);
                PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query);
                preparedStatement.setString(1, polnumber);
                preparedStatement.setInt(2, smo);
                preparedStatement.setInt(3, idD);
                preparedStatement.execute();
                System.out.println(java.util.Calendar.getInstance().getTime() + " " + idD +  ": добавлен в pim_individual_doc с номером полиса: " + polnumber);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addDocSnilsE(String snils, DBWorker connection, Integer idD){
        String query2 = "UPDATE pim_individual_doc set number = ? where indiv_id = ? and type_id = 19";
        try {
            if (snils != null){
                PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query2);
                preparedStatement.setString(1, snils);
                preparedStatement.setInt(2, idD);
                preparedStatement.execute();
                System.out.println(java.util.Calendar.getInstance().getTime() + " " + idD +  ": обновлен в pim_individual_doc с номером СНИЛС'а: " + snils);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addPhoneDE(String phone, DBWorker connection, Integer idD){
        String sql = "UPDATE pim_indiv_contact set value = ?, type_id = ? where indiv_id = ?";
        try {
            if (phone != null){
                PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
                preparedStatement.setString(1, phone);
                if (phone.length() < 9){
                    preparedStatement.setInt(2, 3);
                }else {
                    preparedStatement.setInt(2, 2);
                }
                preparedStatement.setInt(3, idD);
                preparedStatement.execute();
                System.out.println(java.util.Calendar.getInstance().getTime() + " " + idD +  ": обновлен в pim_indiv_contact с номером телефона'а: " + phone);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addrDelete(DBWorker connection, Integer idD){
        String sql = "DELETE from pim_party_address WHERE party_id = ? RETURNING id";
        try {
                PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
                preparedStatement.setInt(1, idD);
                ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String sql2 = "DELETE from pim_party_addr_to_addr_type WHERE party_address_id = ?";
                PreparedStatement preparedStatement1 = connection.getConnection().prepareStatement(sql2);
                preparedStatement1.setInt(1, resultSet.getInt(1));
                preparedStatement1.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void adrAddRDE(String streedCode, String houseR, String roomR, DBWorker connection, Integer IdD){

        String sql = "INSERT INTO address_element(id, name, level_id, parent_id, type_id) VALUES (nextval('address_element_id_seq'), ?, ?, ?, ?) RETURNING id";
        String sql2 = "INSERT INTO pim_party_address(id, addr_id, party_id, register_type_id, is_valid) VALUES (nextval('pim_party_address_id_seq'), ?, ?, ?, TRUE) RETURNING id";
        String sql3 = "INSERT INTO pim_party_addr_to_addr_type(id, address_type_id, party_address_id) VALUES (nextval('pim_party_address_id_seq'), 4, ?)";

        if (streedCode != null){
            //Integer parentIdNasR = parentIdNas(naspCode, connection);
            Integer parentIdStreetR = parentIdStreet(streedCode, connection);
            Integer parentIdRHouse = 0;
            Integer parentIdRRoom = 0;
            Integer addrId = 0;
            try {
                if(houseR != null){
                    PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
                    preparedStatement.setString(1, houseR);
                    preparedStatement.setInt(2, 7);
                    preparedStatement.setInt(3, parentIdStreetR);
                    preparedStatement.setInt(4, 22);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()){
                        parentIdRHouse =  resultSet.getInt(1);
                        System.out.println("address_element номер дома: " + houseR);
                    }
                }
                if (roomR != null){
                    PreparedStatement preparedStatement1 = connection.getConnection().prepareStatement(sql);
                    preparedStatement1.setString(1, roomR);
                    preparedStatement1.setInt(2, 8);
                    preparedStatement1.setInt(3, parentIdRHouse);
                    preparedStatement1.setInt(4, 109);
                    ResultSet resultSet1 = preparedStatement1.executeQuery();
                    while (resultSet1.next()){
                        parentIdRRoom =  resultSet1.getInt(1);
                        System.out.println("address_element вставлен номер квартиры: " + roomR);
                    }
                }
                PreparedStatement preparedStatement2 = connection.getConnection().prepareStatement(sql2);
                if (parentIdRRoom == 0 && parentIdRHouse != 0){
                    preparedStatement2.setInt(1, parentIdRHouse);
                }else if ( parentIdRHouse == 0 && parentIdRRoom == 0){
                    preparedStatement2.setInt(1, parentIdStreetR);
                }else {
                    preparedStatement2.setInt(1, parentIdRRoom);
                }
                preparedStatement2.setInt(2, IdD);
                preparedStatement2.setInt(3, 1);
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                while (resultSet2.next()){
                    addrId = resultSet2.getInt(1);
                    System.out.println("Для пациента вставлен адрес: " + houseR + " " + roomR);
                }
                PreparedStatement preparedStatement3 = connection.getConnection().prepareStatement(sql3);
                preparedStatement3.setInt(1, addrId);
                preparedStatement3.execute();
                System.out.println("Для пациента вставлен адрес как место регситрации: " + houseR + " " + roomR);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void adrAddLDE(String streedCode, String houseR, String roomR, DBWorker connection, Integer idD){
        String sql = "INSERT INTO address_element(id, name, level_id, parent_id, type_id) VALUES (nextval('address_element_id_seq'), ?, ?, ?, ?) RETURNING id";
        String sql2 = "INSERT INTO pim_party_address(id, addr_id, party_id, register_type_id, is_valid) VALUES (nextval('pim_party_address_id_seq'), ?, ?, ?, TRUE) RETURNING id";
        String sql3 = "INSERT INTO pim_party_addr_to_addr_type(id, address_type_id, party_address_id) VALUES (nextval('pim_party_address_id_seq'), 3, ?)";

        if (streedCode != null){
            //Integer parentIdNasR = parentIdNas(naspCode, connection);
            Integer parentIdStreetR = parentIdStreet(streedCode, connection);
            Integer parentIdRHouse = 0;
            Integer parentIdRRoom = 0;
            Integer addrId = 0;
            try {
                PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
                preparedStatement.setString(1, houseR);
                preparedStatement.setInt(2, 7);
                preparedStatement.setInt(3, parentIdStreetR);
                preparedStatement.setInt(4, 22);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    parentIdRHouse =  resultSet.getInt(1);
                    System.out.println("address_element номер дома: " + houseR);
                }
                PreparedStatement preparedStatement1 = connection.getConnection().prepareStatement(sql);
                preparedStatement1.setString(1, roomR);
                preparedStatement1.setInt(2, 8);
                preparedStatement1.setInt(3, parentIdRHouse);
                preparedStatement1.setInt(4, 109);
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                while (resultSet1.next()){
                    parentIdRRoom =  resultSet1.getInt(1);
                    System.out.println("address_element вставлен номер квартиры: " + roomR);
                }
                PreparedStatement preparedStatement2 = connection.getConnection().prepareStatement(sql2);
                if (parentIdRRoom == 0 && parentIdRHouse != 0){
                    preparedStatement2.setInt(1, parentIdRHouse);
                }else if ( parentIdRHouse == 0 && parentIdRRoom == 0){
                    preparedStatement2.setInt(1, parentIdStreetR);
                }else {
                    preparedStatement2.setInt(1, parentIdRRoom);
                }
                preparedStatement2.setInt(2, idD);
                preparedStatement2.setInt(3, 1);
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                while (resultSet2.next()){
                    addrId = resultSet2.getInt(1);
                    System.out.println("Для пациента вставлен адрес: " + houseR + " " + roomR);
                }
                PreparedStatement preparedStatement3 = connection.getConnection().prepareStatement(sql3);
                preparedStatement3.setInt(1, addrId);
                preparedStatement3.execute();
                System.out.println("Для пациента вставлен адрес как место проживания: " + houseR + " " + roomR);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void attachmentAddDE( DBWorker connection, Integer IdD){
        String sql = "UPDATE pci_patient_reg set state_id = 2 WHERE patient_id = ?";
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, IdD);
            preparedStatement.execute();
            System.out.println("Пациент: " + IdD + " откреплен от всех существующих прикреплений");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void codeAdd(String snils, String enp, DBWorker connection){
        String rmisCode = null;
        String code = "INSERT INTO pim_indiv_code(id, code, issue_dt, type_id, indiv_id) VALUES (nextval('pim_indiv_code_id_seq'), ?, current_date, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement("SELECT random_string(16)");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                rmisCode = resultSet.getString(1);
            }
            PreparedStatement preparedStatement1 = connection.getConnection().prepareStatement(code);
            preparedStatement1.setString(1, rmisCode);
            preparedStatement1.setInt(2, 8);
            preparedStatement1.setInt(3, id);
            preparedStatement1.execute();
            System.out.println("Добавлен код для id:" + id + "  - " + rmisCode);
            if (snils != null){
                PreparedStatement preparedStatement2 = connection.getConnection().prepareStatement(code);
                preparedStatement2.setString(1, snils);
                preparedStatement2.setInt(2, 1);
                preparedStatement2.setInt(3, id);
                preparedStatement2.execute();
                System.out.println("Добавлен код для id:" + id + "  - " + snils);
            }
            if (enp != null){
                PreparedStatement preparedStatement3 = connection.getConnection().prepareStatement(code);
                preparedStatement3.setString(1, enp);
                preparedStatement3.setInt(2, 3);
                preparedStatement3.setInt(3, id);
                preparedStatement3.execute();
                System.out.println("Добавлен код для id:" + id + "  - " + enp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }






    public Integer getId() {
        return id;
    }
}
