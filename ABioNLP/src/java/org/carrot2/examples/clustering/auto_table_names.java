/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
This part of the code uses orginal Carrot2 APIs which can be downloaded from http://download.carrot2.org/stable/javadoc/org/carrot2/core/Cluster.htm
 */
package org.carrot2.examples.clustering;

//import static org.carrot2.examples.clustering.cf.QRY;

import database_manipulator.database;
import database_manipulator.database;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 *
 * @author rohit
 */
public class auto_table_names {
    String query;
    String auto_table_name,unique_table_name;
    database connection;
    String user_name;
    // creates tables for auto_clustering
    // two tables are primarily required for auto_clustering
    // they are the auto table - which stores the whole list of clusters formed after auto clustering
    // and the unique_table - which stores the unique elements from the above table
    
    public auto_table_names(String qry,database con,String uname,boolean is_auto_clustered)
    {
        try
        {
            user_name=uname;
            
            System.out.println("AUTO - TABLE - USERNAME = "+qry);
               query=qry.replace("+","_");
               auto_table_name="auto_"+user_name+"_"+query; 
               unique_table_name="unique_"+user_name+"_"+query;
               System.out.println("HELP HELP!!! "+ user_name+" ---- "+query);
               connection=con;
               user_name=uname;
               PreparedStatement pst = null;
               ResultSet rs = null;
               //if(!is_auto_clustered)
               
                    pst=connection.con.prepareStatement("Drop table if exists "+auto_table_name);
                    pst.execute();
                    pst=connection.con.prepareStatement("Drop table if exists "+unique_table_name);
                    pst.execute();
               String stm="CREATE TABLE IF NOT EXISTS `"+auto_table_name+"` (  `id` int(11) NOT NULL AUTO_INCREMENT,  `clusname` varchar(255) NOT NULL,  `score` double NOT NULL DEFAULT '0',  `size` int(11) DEFAULT '0',  `parent` int(11) NOT NULL DEFAULT '0',  `pprids` text NOT NULL,  `freq` double NOT NULL DEFAULT '0',  `thresh` double NOT NULL DEFAULT '0',  PRIMARY KEY (`id`))";

               System.out.println("1st statement - "+stm);
            pst=connection.con.prepareStatement(stm);
            pst.execute();
            System.out.println("auto_created "+auto_table_name);
            
            stm="CREATE TABLE IF NOT EXISTS `"+unique_table_name+"` (\n" +
                                                "  `id` int(11) NOT NULL,\n" +
                                                "  `name` mediumtext NOT NULL,\n" +
                                                "  `clusters` text,\n" +
                                                "  `concepts` longtext,\n" +
                                                "  `score` int(11) NOT NULL DEFAULT '0',\n" +
                                                "  `pprids` text,\n" +
                                                "  `size` int(11) NOT NULL DEFAULT '0',\n" +
                                                "  `score2` double NOT NULL DEFAULT '0',\n" +
                                                "  `fin_score` double NOT NULL DEFAULT '0',\n" +
                                                "  PRIMARY KEY (`id`)\n" +
                                                ") ;";
            System.out.println("2nd statement - "+stm);
            pst=connection.con.prepareStatement(stm);
            pst.execute();
            System.out.println("unique_created "+unique_table_name);            
            
            
            if(!is_auto_clustered)
            {
                
                pst=connection.con.prepareStatement("SELECT table_created from session_manager where"+
                                                 " session_id = '"+user_name+"'");
            
                rs=pst.executeQuery();
                String tbls="";
                while(rs.next())
                {
                    tbls=rs.getString(1);
                    System.out.println("checking  + "+tbls);
                }
                if(!(tbls.contains(auto_table_name)&&tbls.contains(unique_table_name)))
                {
                    tbls+=auto_table_name+" "+unique_table_name+" ";
                    System.out.println("checking2  + "+tbls);
                    pst=connection.con.prepareStatement("update session_manager set table_created = (?) where session_id = '"+user_name+"'");
                    pst.setString(1, tbls);
                    pst.executeUpdate();
                }
            }
        
        }
        catch(Exception e)
        {
            System.out.println("Auto_table_names funct auto_table_names() - "+e);
        }
    }
    
     public static void main(String [] args) throws URISyntaxException
    {
        database con = new database();
        auto_table_names atn = new auto_table_names("ataxia", con,"unni",false);
    }
}
