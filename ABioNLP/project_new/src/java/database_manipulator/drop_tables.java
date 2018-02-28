/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database_manipulator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

/**
 *
 * @author rohit
 */
public class drop_tables {
    database connection;
    String Session;
    public drop_tables(database con,String str)
    {
        connection=con;
        Session=str;
    }
    public void final_drop_tables()
    {
        PreparedStatement ps;
        ResultSet rs;
        try
        {
         ps=connection.con.prepareStatement("SELECT table_created from session_manager where"+
                                             " session_id = '"+Session+"'");
        rs=ps.executeQuery();
        String tbls="";
        while(rs.next())
        {
            tbls=rs.getString(1);
        }
        StringTokenizer tok= new StringTokenizer(tbls);
        while(tok.hasMoreTokens())
        {
            String tbl_name=tok.nextToken();
            System.out.println("dropping - "+tbl_name);
            ps=connection.con.prepareStatement("Drop table "+tbl_name);
            ps.execute();
        }
        System.out.println("Session - drop table - "+Session);
        ps=connection.con.prepareStatement("delete from session_manager where session_id= '"+Session+"'");
        ps.executeUpdate();
        }
        catch(Exception e)
        {
            System.err.println("drop_tables funct - final_drop_tables() - "+e);
        }
    }
}
