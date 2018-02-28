/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carrot2.examples.clustering;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

/**
 *
 * @author rohit
 */
public class paramtest {
    
    public static int selcluster() 
    {
        int no_of_papers=0;
        PreparedStatement pst = null,pst2=null;
        ResultSet rs = null,rs2=null;
        Connection con ;
        String url = "jdbc:mysql://localhost:3306/hippo";
        String user = "root";
        String password = "pass";
        try{
            int clusterNumber = 1;
            int clus_num=0;
            Class.forName("com.mysql.jdbc.Driver").newInstance(); 
            con = DriverManager.getConnection(url, user, password);
            //con2=DriverManager.getConnection(url, user, password);
            
            pst=con.prepareStatement("select count(id) from test");
           
            rs = pst.executeQuery();
            //String name;
            
            while (rs.next()) {
                no_of_papers=rs.getInt(1);
            }
            
            
            clusters start=new clusters();
            clusters end;
            String clusters[]=new String[20];
            
            Class.forName("com.mysql.jdbc.Driver").newInstance(); 
            con = DriverManager.getConnection(url, user, password);
            
            pst=con.prepareStatement("select id,name,size,score,score2,pprids,clusters from unique_1 order by size desc limit 0,5");
            rs = pst.executeQuery();
            String name;
            int id,count=0,flag=0;
            
            clusters temp;
            temp=start;
            String id_not_eq=" ";
            while (rs.next()) {
                if (flag==0)
                {
                    temp.original_id=rs.getInt(1);
                    temp.clustername=rs.getString(2);
                    temp.size=rs.getInt(3);
                    temp.score=-1*rs.getInt(4);
                    temp.algo_score=rs.getDouble(5);
                    temp.pprids=rs.getString(6);
                    temp.cluster_ids=rs.getString(7);
                    flag=1;
                }
                else
                {
                    clusters node=new clusters();
                    temp.next=node;
                    temp=node;
                    temp.original_id=rs.getInt(1);
                    temp.clustername=rs.getString(2);
                    temp.size=rs.getInt(3);
                    temp.score=-1*rs.getInt(4);
                    temp.algo_score=rs.getDouble(5);
                    temp.pprids=rs.getString(6);
                    temp.cluster_ids=rs.getString(7);
                }
                id_not_eq+="and id!="+temp.original_id+" ";
            }
            temp.next=null;
            
            pst=con.prepareStatement("select id,name,size,score,score2,pprids from unique_1 where score = -1000"+id_not_eq+"order by size desc limit 0, 10");
            rs = pst.executeQuery();
            while (rs.next()) {
                    clusters node=new clusters();
                    temp.next=node;
                    temp=node;
                    temp.original_id=rs.getInt(1);
                    temp.clustername=rs.getString(2);
                    temp.size=rs.getInt(3);
                    temp.score=-1*rs.getInt(4);
                    temp.algo_score=rs.getDouble(5);
                    temp.pprids=rs.getString(6);
                    temp.cluster_ids=rs.getString(7);
                    id_not_eq+="and id!="+temp.original_id+" ";
            }
            
            pst=con.prepareStatement("select id,name,size,score,score2,pprids from unique_1 where score = -1000"+id_not_eq+" order by score2 desc limit 0, 5");
            rs = pst.executeQuery();
            while (rs.next()) {
                    clusters node=new clusters();
                    Double temp_score=0.0;
                    temp.next=node;
                    temp=node;
                    temp.original_id=rs.getInt(1);
                    temp.clustername=rs.getString(2);
                    temp.size=rs.getInt(3);
                    temp.score=-1*rs.getInt(4);
                    temp_score=(double)temp.score/100;
                    temp.algo_score=rs.getDouble(5);
                    
                    temp.pprids=rs.getString(6);
                    temp.cluster_ids=rs.getString(7);
                    id_not_eq+="and id!="+temp.original_id+" ";
            }
            temp.next=null;
            end=temp;
            
            temp=start;
            String total="";
            int n=0;
            while (temp!=null) {
                    ++n;
                    pst=con.prepareStatement("insert into clusters (clusid,name,size,score,parent,pprids) values (?,?,?,?,?,?)");
                    pst.setInt(1,n);
                    pst.setString(2,temp.clustername);
                    pst.setInt(3,temp.size);
                    pst.setDouble(4,temp.score);
                    pst.setInt(5,0);
                    pst.setString(6,temp.pprids);
                    pst.executeUpdate();
                    
                    //rs = pst.executeQuery();
                    
                    
                    System.out.println(temp.clustername);
                    StringTokenizer tok=new StringTokenizer(temp.pprids);
                    
                    while(tok.hasMoreTokens())
                    {
                         
                         String temp_str=tok.nextToken();
                         if(!total.contains(" "+temp_str+" "))
                         {
                             total+=" "+temp_str+" ";
                            // System.err.println("inside");
                         }
                     }
                     temp=temp.next;
                }
            
            StringTokenizer tok=new StringTokenizer(total);
            System.out.println(tok.countTokens());
            double ratio=100*tok.countTokens()/no_of_papers;
            System.out.println(ratio);
            pst.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return 1;
    }
    
    
    
}
