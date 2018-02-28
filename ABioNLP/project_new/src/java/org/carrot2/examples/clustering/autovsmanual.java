/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carrot2.examples.clustering;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

/**
 *
 * @author rohit
 */
public class autovsmanual {
    
    
    
    
    public static int selcluster() 
    {
        int val=0;
        PreparedStatement pst = null,pst2=null;
        ResultSet rs = null,rs2=null;
        Connection con ,con2;    
        String url = "jdbc:mysql://localhost:3306/hippo";
        String user = "root";
        String password = "pass";
        try
        {
            int clusterNumber = 1;
            int clus_num=0;
            Class.forName("com.mysql.jdbc.Driver").newInstance(); 
            con = DriverManager.getConnection(url, user, password);
            //con2=DriverManager.getConnection(url, user, password);
            pst=con.prepareStatement("select count(id) from test");
           
            rs = pst.executeQuery();
            //String name;
            
            while (rs.next()) {
                val=rs.getInt(1);
            }
            pst.close();
            
        String names[]=new String[100];
        int i=0;

  //          int clusterNumber = 1;
//            int clus_num=0;
            Class.forName("com.mysql.jdbc.Driver").newInstance(); 
            con = DriverManager.getConnection(url, user, password);
            con2=DriverManager.getConnection(url, user, password);
            pst=con.prepareStatement("select max(parent) from auto_1");
           
            rs = pst.executeQuery();
            String name;
            int id;
            while (rs.next()) {
                clus_num=rs.getInt(1);
            }
             
            pst.close();
            int score[]=new int [clus_num];
            for(i=1;i<=clus_num;++i){
                score[i-1]=0;
                pst=con.prepareStatement("select clusname from auto_1 where parent = "+i);
                rs = pst.executeQuery();
                
                while (rs.next()) {
                String s=rs.getString(1);    
                
                pst2=con.prepareStatement("select score from unique_1 where name = (?)");
                pst2.setString(1, s);
                rs2=pst2.executeQuery();
                while(rs2.next())
                {
                    score[i-1]+=(-1*rs2.getInt(1));
                }
                }
            }
            System.out.println("!!!");
            int greatest=0;
            System.out.println((1)+"----Score----"+score[0]);
            for(i=1;i<clus_num;++i)
            {
                System.out.println((i+1)+"----Score----"+score[i]);
                if(score[greatest]<=score[i])
                {
                    greatest=i;
                }
            }
                    PreparedStatement pst5 = null;
                ResultSet rs5 = null;
                Connection con5 ;    
                greatest++;
               
                    
                    Class.forName("com.mysql.jdbc.Driver").newInstance(); 
                    con = DriverManager.getConnection(url, user, password);
                    con2=DriverManager.getConnection(url, user, password);
                    pst=con.prepareStatement("select clusname from auto_1 where parent = "+ (greatest));
                        
                    rs = pst.executeQuery();
                    //String name;                    String sd;
                    String pprids="";
                    int size=0;
                    double scr=0.0;
                    int c=1;
                    String sd=" ";
                    while (rs.next()) {
                        sd=rs.getString(1);System.out.println("TEST!!"+sd);
                        pst2=con.prepareStatement("Select score, pprids, size from auto_1 where clusname = (?) and parent=(?)");
                        System.out.println(sd);
                        pst2.setString(1,sd);
                        pst2.setInt(2,greatest);
                        rs2=pst2.executeQuery();System.out.println("TEST!!");
                        while(rs2.next())
                        {
                        size=rs2.getInt(1);
                        System.out.println(size);
                        pprids=rs2.getString(2);
                        System.out.println(pprids);System.out.println("TEST!!");
                        scr=rs2.getDouble(3);
                        System.out.println(scr);
                        }
                        
                   
                            
                    
                
                
                System.out.println(greatest+"    "+score[greatest-1]);
            }
            
             pst.close();
                    pst2.close();
            
            con.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return 1;
    }
    
    
    
    
    public static void main(String [] args) throws URISyntaxException
    {
                try
                {

                    Class.forName("com.mysql.jdbc.Driver").newInstance();  
                    Connection connection = DriverManager.getConnection
                    ("jdbc:mysql://localhost:3306/hippo", "root", "pass");  
                    String temp;
                    PreparedStatement pst=null;
                    ResultSet rs=null;
                    int i=0;
                    int score;
                    //pst=connection.prepareStatement("select pprids from unique_1 where score = -1000 limit 0,30");
                    pst=connection.prepareStatement("select pprids,score from unique_1 order by size desc limit 0,20");
                    rs = pst.executeQuery();
                    String total="";
                    int count=0;
                    while (rs.next()) {
                        score=0;
                     temp=rs.getString(1);
                     score=-1*rs.getInt(2);
                     System.out.println(temp);
                     if(score==1000)
                     {
                         ++count;
                     }
                     StringTokenizer tok=new StringTokenizer(temp);
                     while(tok.hasMoreTokens())
                     {
                         String temp2=tok.nextToken();
                         if(!total.contains(" "+temp2+" "))
                         {
                             total+=" "+temp2+" ";
                            // System.err.println("inside");
                         }
                     }
                     
                    }
                    StringTokenizer tok=new StringTokenizer(total);
                    System.out.println("Number of tokens - "+tok.countTokens());
                    System.out.println(count);
                    
                }
                  catch (Exception e)
                  {
                      System.err.println(e);
                  }
    }
    
    
}
