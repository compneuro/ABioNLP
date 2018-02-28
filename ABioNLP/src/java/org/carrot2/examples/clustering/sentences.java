/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
This part of the code uses orginal Carrot2 APIs which can be downloaded from http://download.carrot2.org/stable/javadoc/org/carrot2/core/Cluster.htm
 */
package org.carrot2.examples.clustering;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import web.Clus;
//import static web.Rest1.setscore;
//import static web.Rest1.setval;

/**
 *
 * @author UNNIKRISHNAN
 */
public class sentences {
     public static void main(String [] args) throws SQLException, IOException {
        
        
         int l=0,clusno=0,docno=0;
      ArrayList alist=new ArrayList();
      ArrayList blist=new ArrayList();
      //---------------------------------
        Connection con = null;
        PreparedStatement pst = null,pst2=null;
        ResultSet rs = null,rs2=null;
                
        String url = "jdbc:mysql://localhost:3306/hippo";
        String user = "root";
        String password = "pass";
        String qry="",abs=null;
        
        File file;
        String file_name="/home/rohit/files/sentences.txt";
                    file = new File(file_name);                    
                    BufferedWriter pout;
                    boolean exist = file.createNewFile();
                    FileWriter fstream = new FileWriter(file);
                    pout = new BufferedWriter(fstream);
                    String sentence = null;
        
        

        try {
            System.err.println("REST1.JAVA!!!");
            Class.forName("com.mysql.jdbc.Driver").newInstance(); 
            con = DriverManager.getConnection(url, user, password);
            //pst = con.prepareStatement("select t.title,t.abstract,c.name from test t,clusters c WHERE c.clusid=t.cluster_no");
            pst=con.prepareStatement("select count(id) from test");
            rs = pst.executeQuery();
            while (rs.next()) {
                docno=rs.getInt(1);
            }
            pst.close();
            rs.close();
            pst=con.prepareStatement("select count(clusid) from clusters");
            //pst = con.prepareStatement("Select clusid,name,pprids from clusters");
            rs = pst.executeQuery();
            while (rs.next()) {
                clusno=rs.getInt(1);
            }
            pst.close();
            rs.close();
            int clusid[]=new int[clusno];
            String clus_name[]=new String[clusno];
            int clus_size[]=new int[clusno];
            int clus_score[]=new int[clusno];
            String papers[]=new String[clusno];
            int i=0;
            pst = con.prepareStatement("Select clusid,name,size,score,pprids from clusters ");
            rs = pst.executeQuery();

            System.err.println("REST2.JAVA!!!");
            while (rs.next()) {
                clusid[i]=rs.getInt(1);
                //System.err.println(clusid[i]);
                clus_name[i]=rs.getString(2);
                //System.err.println(clus_name[i]);
                clus_size[i]=rs.getInt(3);
                //System.err.println(clus_size[i]);
                clus_score[i]=rs.getInt(4);
                //System.err.println(clus_score[i]);
                papers[i]=rs.getString(5);
                //System.err.println(papers[i]);
                ++i;
            }
            pst.close();
            rs.close();
          
                    
                
                int j=0;
                while(j<i)
                {
                StringTokenizer tok=new StringTokenizer(papers[j]);
                while (tok.hasMoreTokens()) {
                    Clus c=new Clus();
                    String token = tok.nextToken();
                    qry="Select title,abstract from test where id = "+token;
                   // System.err.println(qry);
                    pst = con.prepareStatement(qry);
                    rs  = pst.executeQuery();
                    
                    while (rs.next()) {                    
                    //rs.beforeFirst();//first();
                    c.clustername=clus_name[j];
                  //  System.out.println("WRITING!!!"+clus_name[j]);
                    c.doctitle=rs.getString(1);
                    System.out.println("WRITING!!!"+c.doctitle);
                    abs=rs.getString(2);
                   if(abs==null)
                    {
                        abs="No Abstract Provided";
                    }
                    abs=abs.replace(" ","><><");
                    abs=abs.replace("."," ");
                    System.out.println("______________________________");
                    StringTokenizer tok2=new StringTokenizer(abs);
                    pout.write("\n\n"+c.clustername+"\n\n");
                    while (tok2.hasMoreTokens()) {
                        String token2 = tok2.nextToken();
                        token2=token2.replace("><><"," ");
                        token2+=".";
                        sentence =token2;
                        System.out.println("*************************");
                        System.out.println(token2+"\n");
                        if(token2.contains(c.clustername))
                        {System.out.println("!!!!!Successs!!!!!");
                            pout.write(token2+"\n");
                        }
                    }
                    
                    
                    
                    }
                    
                     
                    
                }
                ++j;
                }
                
               //}pout.close();
               //System.out.println(clus_name);
               /*if(clus_name.equals("Mitochondrial Protein"))
               {
                   System.out.println("title:  ");
                   System.out.println(titl);
                   System.out.println("\nabstract:  ");
                   System.out.println(abs);
                   
               }*/
                    
        }   
        catch(Exception e)
        {
            System.out.println("!@#!@#@#@#!#@!@#@!@!#@!#"+e+sentence);
        }
        finally {            
            
           
                if (rs != null) {
                    rs.close();
                }
                if (rs2 != null) {
                    rs2.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (pst2 != null) {
                    pst2.close();
                }
                if (con != null) {
                    con.close();
                }
                
               
    
    }
        pout.close();
}
}