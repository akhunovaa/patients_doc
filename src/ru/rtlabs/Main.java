package ru.rtlabs;

import ru.rtlabs.DB.DBWorker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        String file = null;
        DBWorker connection = new DBWorker();
        Parser parser = new Parser();
        FileInputStream fis;
        Properties property = new Properties();
        try {
            fis = new FileInputStream("config.properties");
            property.load(fis);
            file = property.getProperty("db.file");
        } catch (FileNotFoundException e1) {
            System.err.println("ОШИБКА: Файл properties отсуствует!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        parser.parse(connection, file);
        try {
            System.out.println("Закрываю соед. с БД");
            connection.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
