/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
This part of the code uses orginal Carrot2 APIs which can be downloaded from http://download.carrot2.org/stable/javadoc/org/carrot2/core/Cluster.htm
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
 * @author rohit
 */
public class doubleqry_autopaper {
    int qry_no;
    database con;
    public doubleqry_autopaper(int no,database con1)
    {
        qry_no=no;
        con=con1;
    }
 public void get_papers()
    {
        int x;
        PreparedStatement pst = null,pst2=null;
        ResultSet rs = null,rs2=null;
        String qry="",abs=null;
        String names[]=new String[100];
        int i=0;

        try
        {
            int clusterNumber = 1;
            int clus_num=1;
            pst=con.con.prepareStatement("select count(id) from unique_"+qry_no);
            //pst = con.prepareStatement("Select clusid,name,pprids from clusters");
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
                pst2=con.con.prepareStatement("select name from unique_"+qry_no+" where id = "+j);
                //pst2.setInt(1,j);
                rs = pst2.executeQuery();
                while (rs.next()) {
                name=rs.getString(1);
                System.out.println("afds"+name+j);
                }
                pst2.close();
                pst2=con.con.prepareStatement("select parent, pprids,size,score from auto_"+qry_no+" where clusname = (?)");
                pst2.setString(1,name);
                
               rs=pst2.executeQuery();
               
               
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
                pst2=con.con.prepareStatement("update unique_"+qry_no+" set pprids =(?), size=(?), score2=(?) where id = "+j);
                pst2.setString(1,pprids[max_index]);
                pst2.setInt(2,size[max_index]);
                pst2.setDouble(3,score[max_index]);
                pst2.executeUpdate();
                pst2.close();
            }
            
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    
        public void get_clus_info(int id, String clusname,database con)
    {
        int x;
        PreparedStatement pst = null,pst2=null;
        ResultSet rs = null,rs2=null;
        String qry="",abs=null;
        String names[]=new String[100];
        int i=0;

        try
        {System.out.println("here!!!!");
            int clusterNumber = 1;
            int clus_num=1;
            
            
                pst2=con.con.prepareStatement("select count(parent) from auto_"+qry_no+" where clusname='"+clusname+"'");
                rs=pst2.executeQuery();
                while (rs.next()) {
                    i=rs.getInt(1);
                }
                
                String pprids[]=new String[i];
                int size[]=new int [i];
                double score[]=new double[i];
                int count=0;
                
                pst2=con.con.prepareStatement("select parent, pprids,size,score from auto_"+qry_no+" where clusname = (?)");
                pst2.setString(1,clusname);
                
                rs=pst2.executeQuery();
               
                String parents="";
                System.out.println("\n\n"+clusname+"\n\n");
                while (rs.next()) {
                parents+=" "+rs.getInt(1);
                System.out.println("Parent - "+rs.getInt(1)+" pprids"+rs.getString(2)+" size"+rs.getString(3));
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
                pst2=con.con.prepareStatement("update unique_"+qry_no+" set pprids =(?), size=(?), score2=(?), clusters =(?) where id = "+id);
                pst2.setString(1,pprids[max_index]);
                pst2.setInt(2,size[max_index]);
                pst2.setDouble(3,score[max_index]);
                parents=parents.trim();
                pst2.setString(4,parents);
                pst2.executeUpdate();
                pst2.close();
            
           
            
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    
    
    
    public static void main(String [] args) throws URISyntaxException
    {
        database con= new database();
        doubleqry_autopaper a=new doubleqry_autopaper(1,con);
        a.get_papers();
    }   
}
