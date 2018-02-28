/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import database_manipulator.database;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author UNNIKRISHNAN
 */
//this class contains functions that write into the neo4j database
public class clusdisp {
    static URI firstNode,secondNode; 
    static neoGraph n;
    database connection;
    String user_name;
    public clusdisp(database con,String uname)
    {
        user_name=uname;
        connection=con;
    }
    
    //this function writes everything that is present in the clusters table into neo4j
    public void clus_to_neo(String qry){
        try {
            
            PreparedStatement pst = null;
            ResultSet rs = null;
            int siz = 0,scr = 0;
            String docno="";
            pst=connection.con.prepareStatement("select no_of_retrieved_results from query_"+user_name+" where query_name ='"+qry+"'");
            rs = pst.executeQuery();
            while (rs.next()) {
                siz=rs.getInt(1);
            }
            pst.close();
            rs.close();
                firstNode = n.createNode(); //first node is created
                n.addProperty(firstNode,"id",qry );
                n.addProperty(firstNode,"kind","star");
                n.addProperty(firstNode,"size",siz);
                n.addProperty(firstNode,"score",0);
            
            
            
            
            pst=connection.con.prepareStatement("select name,size,score from clusters_"+user_name);
                rs = pst.executeQuery();
                while (rs.next()) {
                    docno=rs.getString(1);
                    System.out.println("DOC NO "+docno);
                    siz=rs.getInt(2);
                    System.out.println("SIZE"+siz);
                    scr=rs.getInt(3);
                    System.out.println("SCORE "+scr);
                    
                    secondNode = n.createNode();    //additional nodes are created iteratively and attached to the first node
                    n.addProperty(secondNode,"id",docno);
                    n.addProperty(secondNode,"kind","cluster");
                    n.addProperty(secondNode,"size",siz);
                    n.addProperty(secondNode,"score",scr);
                    URI relationshipUri = n.addRelationship( secondNode, firstNode, "CLUSTER_OF");
                }
                pst.close();
                rs.close();
                
            } catch (Exception ex) {
                System.err.println("clusdisp funct-clus_to_neo() "+ex);
            }      
        
        
    }
    
    // this function writes into the neo4j database after a node is reclustered.
    //parent_no denotes the neo4j id of the reclustered nodes parent
    public void reclus_neo(String clus_name, int parent_no){
        try {
            
            PreparedStatement pst = null;
            ResultSet rs = null;
            int siz = 0,scr = 0,parent_id=0;
            String docno="";
            pst=connection.con.prepareStatement("select clusid from clusters_"+user_name+" where name = '"+clus_name+"'");
            rs = pst.executeQuery();
            while (rs.next()) {
                parent_id=rs.getInt(1);
            }
            pst.close();
            rs.close();
            
            pst=connection.con.prepareStatement("select name,size,score from clusters_"+user_name+" where parent = '"+parent_id+"'");
            rs = pst.executeQuery();
            while (rs.next()) {
                    docno=rs.getString(1);
                    System.out.println("DOC NO "+docno);
                    siz=rs.getInt(2);
                    System.out.println("SIZE"+siz);
                    scr=rs.getInt(3);
                    System.out.println("SCORE "+scr);
                    
                    secondNode = n.createNode();
                    n.addProperty( secondNode,"id",docno);
                    n.addProperty(secondNode,"kind","cluster");
                    n.addProperty(secondNode,"size",siz);
                    n.addProperty(secondNode,"score",scr);
                    URI node= new URI("http://localhost:7474/db/data/"+parent_no);
                    URI relationshipUri = n.addRelationship( secondNode, node, "CLUSTER_OF");   //second node attached to the parent node
            }
            
            
            
                pst.close();
                rs.close();
                
            } catch (Exception ex) {
                System.err.println("clusdisp funct-reclus_neo() "+ex);
            }      
        
        
    }
    
    
    
        public static void main(String[] args) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, URISyntaxException
        {
        }
}
