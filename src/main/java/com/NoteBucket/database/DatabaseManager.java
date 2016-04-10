package com.NoteBucket.database;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by USER on 16.02.2014.
 */
public class DatabaseManager {

    //for connection with the paradise.db SQLite database
    private static final String CLASS_NAME = "com.mysql.jdbc.Driver";
    private static final String DEFAULT_CONNECTION = "jdbc:mysql://localhost:3306/test";
    private static final String USER = "root";
    private static final String PASS = "";

    public DatabaseManager() {
    }

    public static Object[][] executeQuery(String sqlString)
    {
        Object[][] finalResult;
        Connection connection = null;
        ResultSet rs = null;
        Statement sta = null;
        try {
            Class.forName(CLASS_NAME);
            connection = DriverManager.getConnection(DEFAULT_CONNECTION, USER, PASS);

            sta = connection.createStatement();

            rs = sta.executeQuery(sqlString);
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int columnCount = rsMetaData.getColumnCount();
            ArrayList<Object[]> result = new ArrayList<Object[]>();
            Object[] header = new Object[columnCount];
            for (int i=1; i <= columnCount; ++i){
                Object label = rsMetaData.getColumnLabel(i);
                header[i-1] = label;
            }
            while (rs.next()){
                Object[] str = new Object[columnCount];
                for (int i=1; i <= columnCount; ++i){
                    Object obj = rs.getObject(i);
                    str[i-1] = obj;
                }
                result.add(str);
            }
            int resultLength = result.size() + 1;
            finalResult = new Object[resultLength][columnCount];
            finalResult[0] = header;
            //for(int i=0;i<resultLength;++i){
            for(int i=0;i<result.size();++i){
                Object[] row = result.get(i);
                finalResult[i+1] = row;
            }
        } catch (SQLException e) {
            //System.err.println(e + " Query: " + sqlString);
            //System.out.println(e + sqlString);
            return null;
        } catch (ClassNotFoundException e) {
            System.err.println(e + " Query: " + sqlString);
            return null;
        }
        finally {
            try {
                if(rs != null) rs.close();
                if(sta != null) sta.close();
                if(connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return finalResult;
    }

    public static synchronized boolean executeUpdate(String sqlString)
    {
        Connection connection = null;
        Statement sta = null;
        try {
            Class.forName(CLASS_NAME);
            connection = DriverManager.getConnection(DEFAULT_CONNECTION);

            sta = connection.createStatement();

            sta.executeUpdate(sqlString);

        } catch (SQLException e) {
            //System.err.println(e + sqlString);
            //e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            System.err.println(e + " Query: " + sqlString);
            //e.printStackTrace();
            return false;
        }
        finally {
            try {
                if(sta != null) sta.close();
                if(connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    public static String convSQLStr(String str)
    {
        //escape the ' (single quote) char
        String key = "%&%%&%";
        str = str.replaceAll("\'", key);
        str = str.replaceAll(key, "\'\'");

        //add ' (single quote) char to the beginnings and the ends
        String in = str;
        if( !in.startsWith("\'") && !in.endsWith("\'"))
        {
            in = '\'' + in + '\'';
        }
        else if(!in.startsWith("\'"))
        {
            in = '\'' + in;
        }
        else if(!in.endsWith("\'"))
        {
            in = in + '\'';
        }
        str = in;

        return str;
    }

    public static String deconvSQLStr(String str)
    {
        //remove ' (single quote) char from the beginnings and the ends
        String in = str;
        if( in.startsWith("\'") && in.endsWith("\'"))
        {
            in = in.substring(1,in.length() - 1);
        }
        else if(in.startsWith("\'"))
        {
            in = in.substring(1,in.length());
        }
        else if(in.endsWith("\'"))
        {
            in =  in.substring(0,in.length() - 1);
        }
        str = in;

        return str;
    }

    public static String deconvCarrotStr(String str)
    {
        //todo: arrayli halini de yapmali misin aceba?

        //if(str instanceof String)
        {
            String in = str;
            if( in.startsWith("<") && in.endsWith(">"))
            {
                in = in.substring(1,in.length() - 1);
            }
            else if(in.startsWith("<"))
            {
                in = in.substring(1,in.length());
            }
            else if(in.endsWith(">"))
            {
                in =  in.substring(0,in.length() - 1);
            }
            str = in;
        }
        return str;
    }
}