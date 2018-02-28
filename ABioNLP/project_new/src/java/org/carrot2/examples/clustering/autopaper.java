/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carrot2.examples.clustering;

import database_manipulator.database;
import java.net.URISyntaxException;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author ROHIT
 * 
 * brede tools  federating online neuro informatics databases neilson may 14 2013
 * 
 * understanding the brain through neuro informatics 
 * 
 * neuro informatics: the integration of shared databases and tools towards integrative neuroscience
 * amari, egan.2002
 * 
 */
public class autopaper {
    database connection;
    auto_table_names table_names;
    String user_name;
    public autopaper(database con,auto_table_names tbl_nm,String uname)
    {
        connection=con;
        table_names=tbl_nm;
        user_name=uname;
    }
    /*public void get_papers()
    {
        int x;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String qry="",abs=null;
        //String names[]=new String[100];
        int i=0;

        try
        {
            int clusterNumber = 1;
            int clus_num=1;
            
            pst=connection.con.prepareStatement("select count(id) from "+table_names.unique_table_name);
            rs = pst.executeQuery();
            
            while (rs.next()) {
                i=rs.getInt(1);
            }
            
            rs.close();
            pst.close();
            int j;
            String name="";
            System.out.println(i);
            for(j=1;j<=i;++j)
            {
                String pprids[]=new String[i];
                int size[]=new int [i];
                double score[]=new double[i];
                int count=0;
                pst=connection.con.prepareStatement("select name from "+table_names.unique_table_name+" where id = "+j);
                //pst2.setInt(1,j);
                rs = pst.executeQuery();
                while (rs.next()) {
                name=rs.getString(1);
//                System.out.println("afds"+name+j);
                }
                pst=connection.con.prepareStatement("select parent, pprids,size,score from "+table_names.auto_table_name+" where clusname = (?)");
                pst.setString(1,name);
                rs=pst.executeQuery();
               
               
                System.out.println("\n\n"+name+"\n\n");
                while (rs.next()) {
                    
                System.out.println("Parent - "+rs.getInt(1)+" pprids"+rs.getString(2)+" size"+rs.getString(3));
                pprids[count]=rs.getString(2);
                size[count]=rs.getInt(3);
                score[count]=rs.getDouble(4);
                ++count;
                //break;
                }
                rs.close();
                int k,max_index;
                max_index=0;
                for(k=1;k<count;++k)
                {
                    if(size[k]>size[max_index])
                    {
                        max_index=k;
                    }
                }
                System.out.println("Max Size - "+size[max_index]+"\npprids - "+pprids[max_index]);
                pst=connection.con.prepareStatement("update "+table_names.unique_table_name+" set pprids =(?), size=(?), score2=(?) where id = "+j);
                pst.setString(1,pprids[max_index]);
                pst.setInt(2,size[max_index]);
                pst.setDouble(3,score[max_index]);
                pst.executeUpdate();
                pst.close();
            }
            
            
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    */
        public void get_clus_info(int id, String clusname)
    {
        int x;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String qry="",abs=null;
        String names[]=new String[100];
        int i=0;

        try
        {System.out.println("here!!!!");
            int clusterNumber = 1;
            int clus_num=1;
            /*Class.forName("com.mysql.jdbc.Driver").newInstance(); 
            con = DriverManager.getConnection(url, user, password);
            con2=DriverManager.getConnection(url, user, password);
            */
            
                pst=connection.con.prepareStatement("select count(parent) from "+table_names.auto_table_name+" where clusname='"+clusname+"'");
                rs=pst.executeQuery();
                while (rs.next()) {
                    i=rs.getInt(1);
                }
                
                String pprids[]=new String[i];
                int size[]=new int [i];
                double score[]=new double[i];
                int count=0;
                
                pst=connection.con.prepareStatement("select parent, pprids,size,score from "+table_names.auto_table_name+" where clusname = (?)");
                pst.setString(1,clusname);
                
                rs=pst.executeQuery();
               
                String parents="";
                System.out.println("\n\n"+clusname+"\n\n");
                while (rs.next()) {
                parents+=" "+rs.getInt(1);
                //System.out.println("Parent - "+rs.getInt(1)+" pprids"+rs.getString(2)+" size"+rs.getString(3));
                pprids[count]=rs.getString(2);
                size[count]=rs.getInt(3);
                score[count]=rs.getDouble(4);
                ++count;
                //break;
                }
                //rs.close();
                int k,max_index;
                max_index=0;
                for(k=1;k<count;++k)
                {
                    if(size[k]>size[max_index])
                    {
                        max_index=k;
                    }
                }
                System.out.println("Max Size - "+size[max_index]+"\npprids - "+pprids[max_index]);
                pst=connection.con.prepareStatement("update "+table_names.unique_table_name+" set pprids =(?), size=(?), score2=(?), clusters =(?) where id = "+id);
                pst.setString(1,pprids[max_index]);
                pst.setInt(2,size[max_index]);
                pst.setDouble(3,score[max_index]);
                parents=parents.trim();
                pst.setString(4,parents);
                pst.executeUpdate();
                pst.close();
            
           
            
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    
    
    
    public static void main(String [] args) throws URISyntaxException
    {
        
    }
    
}
