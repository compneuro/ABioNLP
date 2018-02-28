/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carrot2.examples.clustering;

import database_manipulator.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.carrot2.core.Document;

/**
 *
 * @author rohit
 */
public class create_document {
    String pprids;
    database connection;
    public create_document(String pids,database con)
    {
        pids=pids.replace("|"," ");
        pids=pids.trim();
        pprids=pids;
        connection=con;
    }
    public  ArrayList getdocuments()
    {
        final ArrayList<Document> documents = new ArrayList<Document>();
        try
        {
        
           
                String inputline,qry1;
                System.err.println("query term is!!!!!!!!!!!!!!!");
                StringTokenizer tok = new StringTokenizer(pprids,",");
                PreparedStatement ps;
                ResultSet rs;
                String Abstract,Title;
                int i=0;
                
                while(tok.hasMoreTokens())
                {
                    String token= tok.nextToken();
                    System.out.println("__"+token+"__");
                    ps= connection.con.prepareStatement("Select Abstract,Title from Paper_Store where Pubmed_ID = "+token);
                    rs=ps.executeQuery();
                    while(rs.next())
                    {
                        Abstract=rs.getString(1);
                        Title=rs.getString(2);
                        Document d=new Document(Title,Abstract);
                        d.setContentUrl(token);
                        documents.add(d);
                        ++i;
                        System.out.println("PMID - "+token);
                        System.out.println("Title - "+Title);
                        System.out.println("Abstract - "+Abstract);
                    }
                    System.out.println("**************************************"+i+"**************************************");
                }
                
    }
    catch (Exception e)
    {
        System.out.println("hehe"+e);
    }
        return documents;
    }
    
    public ArrayList get_doc_recluster()
    {
        String qry;
        PreparedStatement pst;
        ResultSet rs;
        ArrayList<Document> documents = new ArrayList<Document>();
        try
        {
        StringTokenizer tok=new StringTokenizer(pprids);
        while (tok.hasMoreTokens()) {
                
            String token = tok.nextToken();
            qry="Select Title,Abstract from Paper_Store where Pubmed_ID = "+token;
            pst = connection.con.prepareStatement(qry);
            rs  = pst.executeQuery();
            String pmid,title,abs;
            while (rs.next()) {                    
                pmid=token;
                title=rs.getString("title");
                abs=rs.getString("abstract");
                Document temp_doc=new Document(title,abs);
                temp_doc.setContentUrl(pmid);
                documents.add(temp_doc);
            }
        } 
        return documents;
    }
    catch(Exception e)
    {
        System.err.println("Class - Create_document funtion - recluster_documents()"+e);
    }
        return null;
    }
}
