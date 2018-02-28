/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package result_calc;

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

/**
 *
 * @author rohit
 */
public class auto_validate {
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
        String qry="",abs=null,keywords=null;
        
        
        String file_name="/home/rohit/files/analysis_auto.txt";
                    File file = new File(file_name);                    
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
            pst=con.prepareStatement("select count(id) from unique_1");
            //pst = con.prepareStatement("Select clusid,name,pprids from clusters");
            rs = pst.executeQuery();
            while (rs.next()) {
                clusno=rs.getInt(1);
            }
            pst.close();
            rs.close();
            int clusid[]=new int[clusno];
            String clus_name[]=new String[clusno];
           // int clus_size[]=new int[clusno];
            int clus_score[]=new int[clusno];
            String papers[]=new String[clusno];
            int i=0;
            pst = con.prepareStatement("Select id,name,score,pprids from unique_1");
            rs = pst.executeQuery();

            System.err.println("REST2.JAVA!!!");
            while (rs.next()) {
                clusid[i]=rs.getInt(1);
                //System.err.println(clusid[i]);
                clus_name[i]=rs.getString(2);
  
                clus_score[i]=rs.getInt(3);
                //System.err.println(clus_score[i]);
                papers[i]=rs.getString(4);
                //System.err.println(papers[i]);
                ++i;
            }
            pst.close();
            rs.close();
          
                    
                
                int j=0;
                while(j<i){
                    
                if(!clus_name[j].contains("Other Topics"))
                {
                StringTokenizer tok=new StringTokenizer(papers[j]);
                pout.write(clus_name[j]+"\n");
                
                while (tok.hasMoreTokens()) {
                    Clus c=new Clus();
                    String token = tok.nextToken();
                    qry="Select title,abstract,keywords from test where id = "+token;
                   // System.err.println(qry);
                    pout.write("paper id ="+token+"\t");
                    pst = con.prepareStatement(qry);
                    rs  = pst.executeQuery();
                    
                    while (rs.next()) {                    
                    //rs.beforeFirst();//first();
                    c.clustername=clus_name[j];
                  //  System.out.println("WRITING!!!"+clus_name[j]);
                    c.doctitle=rs.getString(1);
                    System.out.println("WRITING!!!"+c.doctitle);
                    abs=rs.getString(2);
                    keywords=rs.getString(3);
                   if(abs==null)
                    {
                        abs="No Abstract Provided";
                    }
                   if(keywords==null)
                    {
                        keywords="No Keywords Provided";
                    }
                   c.docabstract=abs;
                 //  c.dockeywords=keywords;
                   int flag1=0,flag2=0;
                   String stop_words="of with a";
                    if(c.doctitle.contains(c.clustername))
                    {
                        pout.write("+TTL"+"\t");
                        flag1=1;
                    }
                    else
                    {
                        pout.write("-TTL"+"\t");
                    }
                    if(c.docabstract.contains(c.clustername))
                    {
                        pout.write("+ABS"+"\t");
                        flag2=1;
                    }
                    else
                    {
                        pout.write("-ABS"+"\t");
                    }
                    if(true)//c.dockeywords.contains(c.clustername))
                    {
                        pout.write("+KEY"+"\t");
                        flag2=1;
                    }
                    else
                    {
                        pout.write("-KEY"+"\t");
                    }
                    
                    ///if(!(flag1==1&&flag2==1)){          
                    String temp=clus_name[j];
                    temp=temp.replace(","," ");
                    StringTokenizer tok2=new StringTokenizer(temp);
                    //pout.write("\n\n"+c.clustername+"\n\n");
                    int title_count=0,abs_count=0,key_count=0;
                    
                    while (tok2.hasMoreTokens()) {
                     
                        String token2 = tok2.nextToken();
                        if(!stop_words.contains(token2))
                        {
                        
                        System.out.println(token2+"\n");
                        c.doctitle=c.doctitle.toLowerCase();
                        c.docabstract=c.docabstract.toLowerCase();
                        //c.dockeywords=c.dockeywords.toLowerCase();
                        token2=token2.toLowerCase();
                        
                       // pout.write(c.doctitle+"\n");
                       // pout.write(c.docabstract+"\n");
                        if(c.doctitle.contains(token2))
                        {
                            ++title_count;
                            //pout.write(title_count+"\t"+"Abstract No - "+abs_count+"\t");
                        }
                        if(c.docabstract.contains(token2))
                        {
                            ++abs_count;
                        }
                        
                        if(c.docabstract.contains(token2))
                        {
                            ++key_count;
                        }
                        
                        }
                    }
                    pout.write("Title No - "+title_count+"\t"+"Abstract No - "+abs_count+"\t"+"Keyword No - "+key_count+"\t");
                    if(title_count==0&&abs_count==0&&key_count==0)
                    {
                    pout.write("\n\n"+c.clustername+"\n\n");    
                    //pout.write("\n\n"+c.doctitle+"\n"+c.docabstract+"\n"+c.dockeywords+"\n\n");
                        
                    }
                    
                    pout.write("\n");
                    
                    }
                    
                     
                    
                }
                ++j;
                System.out.println("*************************");
                System.out.println("!@#!@#@#@#!#@!@#@!@!#@!#");
                }
                else
                {
                    ++j;
                    continue;
                }
            System.out.println("!@#!@#@#@#!#@!@#@!@!#@!#");    
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
