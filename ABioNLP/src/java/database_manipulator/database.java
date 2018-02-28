    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database_manipulator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author rohit
 */
public class database {
    public Connection con;
    private String username,password,url;
    
     public database()
     {
         try
         {
            username="root";
            password="pass";
            url="jdbc:mysql://localhost:3306/fin_schema"; 
            Class.forName("com.mysql.jdbc.Driver").newInstance(); 
            con = DriverManager.getConnection(url, username, password);
         }
         catch(Exception e)
         {
             System.err.println("Package - database_manager Class - database Function database() "+e);
         }
            
     }
     public void close()
     {
         try
         {
         con.close();
         System.out.println("fininsheksfjal");
         }
         catch(Exception e)
         {
             System.err.println("Package - database_manager Class - database Function - close() "+e);
         }
     }
           
}
