package web;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import database_manipulator.database;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
   import java.util.*;
import javax.ws.rs.core.MediaType;
   
   public class cleardb {
       database connection;
       String user_name;
       public cleardb(database con,String uname)
       {
        connection=con;   
        user_name=uname;
       }
      public void clear()
               {
                  // System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAA!!!!!!!!!!AAAAAAAAAAAAAAAAAAA");
                  
        PreparedStatement pst = null;
        
               
        try {
            pst=connection.con.prepareStatement("delete from clusters_"+user_name);
            pst.execute();
            pst.close();
      /*      System.err.println("REST1.JAVA!!!");
            Class.forName("com.mysql.jdbc.Driver").newInstance(); 
            
            //pst = con.prepareStatement("select t.title,t.abstract,c.name from test t,clusters c WHERE c.clusid=t.cluster_no");
            pst=connection.con.prepareStatement("delete from test");
            pst.execute();
            pst.close();
            
            pst=connection.con.prepareStatement("delete from clusters");
            pst.execute();
            pst.close();
            
            pst=connection.con.prepareStatement("drop table auto_2;");
            pst.execute();
            pst.close();
            
            pst=connection.con.prepareStatement("CREATE TABLE `auto_2` (`id` int(11) NOT NULL AUTO_INCREMENT,`clusname` varchar(255) NOT NULL,`score` double NOT NULL DEFAULT '0',`size` int(11) DEFAULT '0',`parent` int(11) NOT NULL DEFAULT '0',`pprids` text NOT NULL,`freq` double NOT NULL DEFAULT '0',`thresh` double NOT NULL DEFAULT '0',PRIMARY KEY (`id`)) ;");
            pst.execute();
            pst.close();
            
            
            pst=connection.con.prepareStatement("delete from unique_1");
            pst.execute();
            pst.close();*/
           
        }
        catch(Exception e)
        {
            System.err.println(e);
        }       
         
       //------------------------------------------------------------------------------  
        
          
      String cypherUri ="http://localhost:7474/db/data/cypher";

        String cypherStatement="{\"query\" : \"START n=node(*) MATCH n-[r?]->() DELETE r WHERE ID(n)<>0 DELETE n,r; \",\"params\" : {}}";

        WebResource resource = Client.create()
                .resource( cypherUri );
        ClientResponse response = resource.accept( MediaType.APPLICATION_JSON_TYPE )
                .type( MediaType.APPLICATION_JSON_TYPE )
                .entity( cypherStatement )
                .post( ClientResponse.class );
        
        String cypherUri1 ="http://localhost:7474/db/data/cypher";
               }
       
     public static void main(String args[]) throws IOException {
         Connection con = null;
        PreparedStatement pst = null;
        
                
        String url = "jdbc:mysql://localhost:3306/hippo";
        String user = "root";
        String password = "pass";
        

        try {
            System.err.println("REST1.JAVA!!!");
            Class.forName("com.mysql.jdbc.Driver").newInstance(); 
            con = DriverManager.getConnection(url, user, password);
            //pst = con.prepareStatement("select t.title,t.abstract,c.name from test t,clusters c WHERE c.clusid=t.cluster_no");
            pst=con.prepareStatement("delete from test");
            pst.execute();
            pst.close();
            
            pst=con.prepareStatement("delete from clusters");
            pst.execute();
            pst.close();
           
        }
        catch(Exception e)
        {
            System.err.println(e);
        }       
         
       //------------------------------------------------------------------------------  
        
          
      String cypherUri ="http://localhost:7474/db/data/cypher";

        String cypherStatement="{\"query\" : \"START n=node(*) MATCH n-[r?]->() DELETE r WHERE ID(n)<>0 DELETE n,r; \",\"params\" : {}}";

        WebResource resource = Client.create()
                .resource( cypherUri );
        ClientResponse response = resource.accept( MediaType.APPLICATION_JSON_TYPE )
                .type( MediaType.APPLICATION_JSON_TYPE )
                .entity( cypherStatement )
                .post( ClientResponse.class );
        
        String cypherUri1 ="http://localhost:7474/db/data/cypher";

     
         //System.err.println(response1.getLocation());
     /*    String s="";
       
       s="curl -X DELETE http://localhost:7474/db/data/cleandb/password";
       try {
         Process child = Runtime.getRuntime().exec(s);
       }
       catch (IOException e) {
        }*/
       }

     }
    