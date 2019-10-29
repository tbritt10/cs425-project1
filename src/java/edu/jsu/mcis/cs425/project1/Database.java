package edu.jsu.mcis.cs425.project1;

import java.sql.ResultSetMetaData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.json.simple.JSONArray;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Database {
    String sessionID;
    
    private Connection getConnection() {
        
        Connection conn = null;
        
        try {
            
            Context envContext = new InitialContext();
            Context initContext  = (Context)envContext.lookup("java:/comp/env");
            DataSource ds = (DataSource)initContext.lookup("jdbc/db_pool");
            conn = ds.getConnection();
            
        }   
        
        catch (Exception e) { e.printStackTrace(); }
        
        return conn;

    }
    public String getQueryResults(String sessionID) {
        this.sessionID = sessionID;
        StringBuilder table = new StringBuilder();
        String query;
        
        Connection conn = getConnection();
        
        query = "SELECT * FROM registrations r WHERE sessionid = ?;";
        
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, sessionID);
            boolean hasResults = statement.execute();
            
            if (hasResults) {
                ResultSet resultset = statement.getResultSet();
                table.append("<table>");
                while (resultset.next()) {
                 table.append("<tr>");
                 table.append("<td>").append(resultset.getString("id")).append("</td>");
                 table.append("<td>").append(resultset.getString("firstname")).append("</td>");
                 table.append("<td>").append(resultset.getString("lastname")).append("</td>");
                 table.append("<td>").append(resultset.getString("displayname")).append("</td>");
                 table.append("<td>").append(sessionID).append("</td>");
                 table.append("</tr>");
                }
                table.append("</table>");
            }
        }
        catch(Exception e){System.err.println(e);}
        return (table.toString());
        
    }
    public String addRegistration(String fname, String lname, String dname, String sessionid) {
        String query;
        String displayname;
        String id = "";
        String regCode;
        ResultSet keys;
        JSONObject results = new JSONObject();
        
        Connection conn = getConnection();
        query = ("INSERT INTO registrations (firstname, lastname, displayname, sessionid) values (?, ?, ?, ?);");
        
        try {
            PreparedStatement statement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, fname);
            statement.setString(2, lname);
            statement.setString(3, dname);
            statement.setString(4, sessionid);
            int bool = statement.executeUpdate();
            if (bool == 1) {
                keys = statement.getGeneratedKeys();
                if (keys.next()) {
                    id = keys.getString(1);
                }
            }
            
            regCode = String.format("R" + "%0" + (6-id.length()) + "d%s", 0, id);
            results.put("displayname", dname);
            results.put("code", regCode);
            
        }
        catch(Exception e){}
        finally {
            return (results.toJSONString());
        }
    }
    
}
