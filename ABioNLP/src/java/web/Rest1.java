/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import database_manipulator.database;
import java.io.BufferedReader;
import java.io.IOException;

import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.StringTokenizer;


import javax.ws.rs.core.MediaType;
import net.sf.json.JSONException;


//import net.sf.json.JSONObject;


/**
 *
 * @author UNNIKRISHNAN
 */

public class Rest1 {
    database connection;
    String query;
    String user_name;
    public Rest1(database con,String qry,String uname)
    {
        connection=con;
        query=qry;
        user_name=uname;
        
    }
    
  public ArrayList getdocuments() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
  {
      int l=0,clusno=0,docno=0;
      ArrayList alist=new ArrayList();
      ArrayList blist=new ArrayList();
      //---------------------------------
        
        PreparedStatement pst = null,pst2=null;
        ResultSet rs = null,rs2=null;
                
        String qry="",abs=null;

        try {
          
            pst=connection.con.prepareStatement("select no_of_retrieved_results from query_"+user_name+" where query_name = '"+query+"'");
            rs = pst.executeQuery();
            while (rs.next()) {
                docno=rs.getInt(1);
            }
            pst.close();
            rs.close();
            pst=connection.con.prepareStatement("select count(clusid) from clusters_"+user_name);
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
            pst = connection.con.prepareStatement("Select clusid,name,size,score,pprids from clusters_"+user_name);
            rs = pst.executeQuery();

            
            while (rs.next()) {
                clusid[i]=rs.getInt(1);
                clus_name[i]=rs.getString(2);
                clus_size[i]=rs.getInt(3);
                clus_score[i]=rs.getInt(4);
                papers[i]=rs.getString(5);
                if(clus_name[i]!=null||clusid[i]!=0)
                {
                
                }
                ++i;
            }
            pst.close();
            rs.close();
            
            
            //    setval(clus_size,docno);
            //    setscore(clus_score);
                
                
                int j=0;
                while(j<i)
                {
                StringTokenizer tok=new StringTokenizer(papers[j]);
                while (tok.hasMoreTokens()) {
                    Clus c=new Clus();
                    String token = tok.nextToken();
                    qry="Select Title,Abstract,Author_list,Journal_Name from Paper_Store where Pubmed_ID = "+token;
                  //  System.out.println(qry);
                    pst = connection.con.prepareStatement(qry);
                    rs  = pst.executeQuery();
                    
                    while (rs.next()) {                    
                    //rs.beforeFirst();//first();
                    c.clustername=clus_name[j];
                  //  System.out.println("WRITING!!!"+clus_name[j]);
                    c.doctitle=rs.getString(1);
                  //  System.out.println("WRITING!!!"+c.doctitle);
                    abs=rs.getString(2);
                    c.docauthor=rs.getString(3);
                    c.docjournal=rs.getString(4);
                  //  System.out.println("WRITING!!!"+abs);
                    }
                    
                    if(abs==null)
                    {
                        abs="No Abstract Provided";
                    }
                    c.docabstract=abs;
                    
                    alist.add(c);
                    c=null;
                    pst.close();
                    rs.close();
                }
                ++j;
                }
        } 
        catch(Exception e)
        {
            System.err.println("Rest1 function - getdocuments() - "+e);
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
                
        }
      return alist;
      
  }
  
   public static ArrayList getResult()
   {   
    
       
        //String str="";
       
        ArrayList ar=new ArrayList();
        ArrayList br=new ArrayList();
       // ar.add(str);
       // ar.get(1);
        
        String cypherUri ="http://localhost:7474/db/data/cypher";

        String cypherStatement="{\"query\" : \"START X=NODE(*)  MATCH PATH = (X-[*1..5]-Y) RETURN PATH; \",\"params\" : {}}";

        WebResource resource = Client.create()
                .resource( cypherUri );
        ClientResponse response = resource.accept( MediaType.APPLICATION_JSON_TYPE )
                .type( MediaType.APPLICATION_JSON_TYPE )
                .entity( cypherStatement )
                .post( ClientResponse.class );

             
        
        URL URLsource = null;
        JsonElement jse = null;
        BufferedReader in;
        String eq="";
        int h;
 
   jse = new JsonParser().parse(response.getEntity(String.class));
   
   JsonArray jsa = jse.getAsJsonObject().getAsJsonArray("data");
   // System.out.println(jsa);

   for (int i= 1; i<jsa.size(); i++ ) {
   
       String temp=jsa.get(i).toString();
       String temp1=temp.substring(1, temp.length()-1);
           
      
        JsonElement jelement = new JsonParser().parse(temp1);
        JsonObject  jobject = jelement.getAsJsonObject();
        
        String start = jobject.get("start").toString();
        start=start.substring(1,start.length()-1);
        start=start.trim();
        int w=0;
        for(int y=0;y<br.size();y++)
        {
            
            if(start.equals(br.get(y)))
                {
                    w=1;
                }
        }
        if(w==0)
        {    
           // System.out.println(start);
            br.add(start);
        }
        
        
        
        JsonArray jarray = jobject.getAsJsonArray("relationships");
        
        for( int y=0;y<jarray.size();y++)
        {
            String strr=jarray.get(y).toString();
            strr=strr.substring(1,strr.length()-1);
            strr=strr.trim();
            int l=0;
            w=0;
            
            while(l<ar.size())
            {           
                if(strr.equals(ar.get(l)))
                {
                    w=1;
                }
                l++;
            }
            if(w==0)
            {         
               // System.out.println(strr);
                ar.add(strr);
            }
        }

   }
    for(int q=0;q<ar.size();q++)
    {
    br.add(ar.get(q));
    }
         response.close();
          return(br);
         
   }
    
    public static void main(String[] args) throws IOException, JSONException, org.json.JSONException, URISyntaxException{
        
        String cypherUri ="http://localhost:7474/db/data/cypher";

        String cypherStatement="{\"query\" : \"START X=NODE(*)  MATCH PATH = (X-[*1..5]-Y) RETURN PATH; \",\"params\" : {}}";

        WebResource resource = Client.create()
                .resource( cypherUri );
        ClientResponse response = resource.accept( MediaType.APPLICATION_JSON_TYPE )
                .type( MediaType.APPLICATION_JSON_TYPE )
                .entity( cypherStatement )
                .post( ClientResponse.class );

             
        System.out.println(resource);
        URL URLsource = null;
        JsonElement jse = null;
        BufferedReader in;
        String eq="";
 
   jse = new JsonParser().parse(response.getEntity(String.class));
   
   JsonArray jsa = jse.getAsJsonObject().getAsJsonArray("data");


   for (int i= 1; i<jsa.size(); i++ ) {
   
       String temp=jsa.get(i).toString();
       String temp1=temp.substring(1, temp.length()-1);
           
       Data data = new Gson().fromJson(temp1, Data.class);
       String temp3=data.toString();
       
       if(eq.equals(temp3))
       {
           continue;
       }
       else
       {
           eq=temp3;
       }
      // System.out.println(eq);
       
   }
         response.close();
        
    }
}




class Data {
    private String start;
    //private String nodes;
    //private String end;

    public String getStart() { return start; }
    //public String getNodes() { return nodes; }
    //public String getEnd() { return end; }

    

    public String toString() {
        return String.format("%s", start);
    }
}