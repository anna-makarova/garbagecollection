package com.db;

import com.google.appengine.api.utils.SystemProperty;
import org.json.JSONArray;

import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

public class DatabaseUtil {
    private static final Logger log = Logger.getLogger(DatabaseUtil.class.getName());
    private static Connection getConnection() {
        String USER ="adm";
        String PASS ="88imkHjNplcH3Okz";
        String url;
        try {
            if (SystemProperty.environment.value() ==
                    SystemProperty.Environment.Value.Production) {
                Class.forName("com.mysql.jdbc.GoogleDriver");
                url = "jdbc:google:mysql://garbagecollection-257006:us-central1:garbagedb/garbage";
            } else {
                Class.forName("com.mysql.jdbc.Driver");
                url="jdbc:google:mysql://garbagecollection-257006:us-central1:garbagedb/garbage";
            }
            return DriverManager.getConnection(url, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getSQLState() + "|Can't load driver: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Class not found: " + e.getMessage());
        }
    }

    public static JSONArray executeStatement(String s) {
        Connection con = DatabaseUtil.getConnection();
        try {

            PreparedStatement pstmt = null;

            pstmt = con.prepareStatement("{"+ s +"}");
            pstmt.addBatch();

            JSONArray result = new JSONArray();
            int[] count = {};
            count= pstmt.executeBatch();

            boolean hasRs = count.length!=0;

            while(hasRs)
            {
                ResultSet rs = pstmt.getResultSet();
                if(rs!=null)
                {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();

                    while (rs.next()) {
                        if (false) {
                            Map obj = new LinkedHashMap();
                            for (int i = 1; i < columnCount + 1; i++) {
                                String name = rsmd.getColumnLabel(i);
                                obj.put(name, rs.getString(i));
                            }

                            result.put(obj);
                        } else {
                            JSONArray paramArr = new JSONArray();
                            for (int i = 1; i < columnCount + 1; i++) {
                                paramArr.put(rs.getString(i));
                            }
                            result.put(paramArr);
                        }
                    }

                    rs.close();
                }
                hasRs=pstmt.getMoreResults();
            }
            if(pstmt != null) pstmt.close();
            return result;

    } catch (SQLException e) {
            throw new RuntimeException(e.getSQLState() + "|Sql error: " + e.getMessage());
    } finally {
        try {
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getSQLState() + "|Can't close connection: " + e.getMessage());
        }
    }
    }
}
