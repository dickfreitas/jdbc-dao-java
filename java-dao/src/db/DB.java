package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {
    private static Connection conn = null;

    //METODO PARA CONECTAR AO BANCO DE DADOS
    public static Connection getConnection(){
        if (conn == null){
            try {
                Properties properties = loadProperties();
                String url = properties.getProperty("dburl");
                conn = DriverManager.getConnection(url,properties);
            }catch (SQLException e ){
                throw new DbException(e.getMessage());
            }

        }
        return conn;
    }

    //CARREGA AS INFORMAÇOES DE CONEXAO DO BANCO DE DADOS
    public static Properties loadProperties(){
        try(FileInputStream fileInputStream = new FileInputStream("db.properties")) {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            return properties;
        }catch (IOException e ){
            throw new DbException(e.getMessage());
        }
    }

    //METODO PARA FECHAR A CONEXAO COM O BANCO DE DADOS
    public static void closeConnection(){
        if (conn != null){
            try {
                conn.close();
            }catch (SQLException e ){
                throw new  DbException(e.getMessage());
            }
        }
    }

    //METODO PARA FECHAR O STATEMENT
    public static void closeStatement(Statement statement){
        if (statement != null){
            try {
                statement.close();
            }catch (SQLException e ){
                throw new  DbException(e.getMessage());
            }
        }
    }

    //METODO PARA FECHAR O RESULT SET
    public static void closeResultSet(ResultSet resultSet){
        if (resultSet!= null){
            try {
                resultSet.close();
            }catch (SQLException e ){
                throw new  DbException(e.getMessage());
            }
        }
    }
}
