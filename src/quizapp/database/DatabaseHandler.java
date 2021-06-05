/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quizapp.database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class DatabaseHandler {
    private static final String connection_string="jdbc:sqlite:quiz.db";
    private static final String Jdbc_driver="org.sqlite.JDBC";
    private static Connection con=null;
    private static Statement stmt=null;
    private static final String questions="CREATE TABLE if not exists questions (" +
"qid INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
"question VARCHAR NOT NULL," +
"option1 VARCHAR NOT NULL," +
"option2 VARCHAR NOT NULL," +
"option3 VARCHAR NOT NULL," +
"option4 VARCHAR NOT NULL," +
"answer VARCHAR NOT NULL" +
");";
 private static final String answers="CREATE TABLE if not exists answers (username VARCHAR NOT NULL REFERENCES users (username),"
         + "qno INT NOT NULL,response VARCHAR NOT NULL,PRIMARY KEY (username,qno))";
    private static final String users="create table if not exists users(username varchar ,password varchar,attempt varchar,PRIMARY KEY(username))";
    private static final String admin="create table if not exists admin(username VARCHAR,password VARCHAR,PRIMARY KEY(username))";
    public static void connect()
    {
        try {
            Class.forName(Jdbc_driver);
            con=DriverManager.getConnection(connection_string);
            stmt=con.createStatement();
        } catch (ClassNotFoundException|SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    public static void insert_root_admin()
    {
        String sql="insert into admin values('root123','password123')";
        if(executeUpdate(sql)==0)
            System.out.println("root admin already present");
    }
    public static void create_tables()
    {
        connect();
        executeUpdate(users);
        executeUpdate(admin);
        executeUpdate(questions);
        executeUpdate(answers);
        insert_root_admin();
        disconnect();
        System.out.println("System ready...");
    }
    public static void disconnect()
    {
        if(con!=null)
        {
            try {
                stmt.close();
                con.close();
                stmt=null;
                con=null;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    public static ResultSet executeQuery(String sql)
    {
        ResultSet rs=null;
        try{
            connect();
             rs=stmt.executeQuery(sql);
        }catch(SQLException ex)
        {
            System.out.println("DB#:No worries...");
        }
        return rs;
    }
    public static int executeUpdate(String sql)
    {
        int i=0;
        try{
            connect();
            Statement stmt=con.createStatement();
             i=stmt.executeUpdate(sql);
             disconnect();
        }catch(SQLException ex)
        {
            //System.out.println(ex.getMessage());
        }
        return i;
    }
}
