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
public class create_tables {
    database connection;
    String Session;
    public create_tables(database con,String str)
    {
        connection=con;
        Session=str;
    }
    public void create_initial_tables()
    {
        PreparedStatement ps;
        ResultSet rs;
        try
        {
            System.out.println("INSIDE CREATE TABLES SESSION - "+Session);
            String stm="SELECT table_created from session_manager where session_id = '"+Session+"'";
            System.out.println("checkcheck - "+stm );
         ps=connection.con.prepareStatement(stm);
        rs=ps.executeQuery();
        String tbls="";
        
        while(rs.next())
        {
            tbls=rs.getString(1);
        }
            
        if(tbls!=null)
        {
            System.out.println("checkcheck - 2"+tbls);
            StringTokenizer tok= new StringTokenizer(tbls);
            while(tok.hasMoreTokens())
            {
                String tbl_name=tok.nextToken();
                ps=connection.con.prepareStatement("Drop table if exists "+tbl_name);
                ps.execute();
            }
        }
        System.out.println("checkcheck - 3");
        ps=connection.con.prepareStatement("CREATE TABLE IF NOT EXISTS `clusters_"+Session+"` (\n" +
                                            "  `clusid` int(255) NOT NULL,\n" +
                                            "  `name` longtext NOT NULL,\n" +
                                            "  `size` int(255) NOT NULL,\n" +
                                            "  `score` double NOT NULL,\n" +
                                            "  `parent` int(255) NOT NULL,\n" +
                                            "  `pprids` longtext,\n" +
                                            "  PRIMARY KEY (`clusid`),\n" +
                                            "  UNIQUE KEY `clusid_UNIQUE` (`clusid`)\n" +
                                            ")");
        ps.execute();
        System.out.println("checkcheck - 4");
        ps=connection.con.prepareStatement("CREATE TABLE IF NOT EXISTS `query_"+Session+"` (\n" +
                                            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                                            "  `query_name` varchar(255) NOT NULL,\n" +
                                            "  `total_results` int(11) NOT NULL DEFAULT '0',\n" +
                                            "  `paper_ids` longtext,\n" +
                                            "  `no_of_retrieved_results` int(11) DEFAULT NULL,\n" +
                                            "  `is_auto_clustered` tinyint(1) DEFAULT '0',\n" +
                                            "  PRIMARY KEY (`id`)\n" +
                                            ")");
        ps.execute();
        
        
            tbls="clusters_"+Session+" "+"query_"+Session+" ";
        
        ps=connection.con.prepareStatement("update session_manager set table_created = (?) where session_id = '"+Session+"'");
        ps.setString(1, tbls);
        ps.executeUpdate();
        System.out.println("byebye");
        }
        catch(Exception e)
        {
            System.out.println("Create_tables - "+e);
        }
    }
}
