package dao;

import dao.exception.DaoException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    private static final Logger log = Logger.getLogger(ConnectionFactory.class);
    private static final ConnectionFactory connectionFactory = new ConnectionFactory();
    private static String URL;
    private static String CLASS_NAME;
    private static String USER;
    private static String PASS;

    private void getResource() throws IOException {
        log.info("Enter method getResource");
        Properties prop = new Properties();
        String propFileName = "connection.properties";
        prop.load(getClass().getClassLoader().getResourceAsStream(propFileName));
        log.info("Get data from resource");
        URL = prop.getProperty("url");
        CLASS_NAME = prop.getProperty("class_name");
        USER = prop.getProperty("user_name");
        PASS = prop.getProperty("password");
        log.info("exit method getResource");
    }

    public static Connection getConnection() {
        log.info("Enter method getConnection");
        try {
            connectionFactory.getResource();
        } catch (IOException e) {
            throw new DaoException("Can't obtain resources", e);
        }
        try {
            Class.forName(CLASS_NAME);
            log.info("Return connection");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            log.error("Can't obtain a connection", e);
            throw new DaoException("Can't obtain a connection", e);
        } catch (ClassNotFoundException e){
            log.error("Driver not found");
            throw new DaoException("Driver not found" ,e);
        }
    }
}
