/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xml_highlevel;

import database_manipulator.database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import xml_extractor.abstract_extractor;

/**
 *
 * @author rohit
 */
//class manages the overall handling of a query
public class query_manager {
    database connection;//Connection con;
    query_extractor qry_ext;
    String user_name;
    
    public query_manager(database con,String uname)
    {
        try
        {
            /*String username="root";
            String password="pass";
            String url="jdbc:mysql://localhost:3306/fin_schema"; 
            Class.forName("com.mysql.jdbc.Driver").newInstance(); 
            con = DriverManager.getConnection(url, username, password);*/
            connection=con;
            qry_ext=new query_extractor(uname);
            user_name=uname;
        }
        catch(Exception e)
        {
            System.err.println("Package: XML_HIGLEVEL\tClass: session_manager\tFunction: session_manager()");
        }
    }       
    public String query_manipulator(String qry)
    {
        qry=qry.toLowerCase();
        StringTokenizer tok = new StringTokenizer(qry);
        String temp_token="";
        qry="";
        while(tok.hasMoreTokens())
        {
            String token=tok.nextToken();
            if(!(temp_token.equals("")))
            {
                qry+=temp_token+"+";
            }
            temp_token=token;
        }
        qry+=temp_token;
        return qry;
    }
    public String manage_query(String qry, int number)
    {
        try
         {
            qry=query_manipulator(qry); //removes all the white spaces from the query and adds a + sign between them
            System.out.println("After qry manip - "+qry);
            query query_var=qry_ext.process_qry(1,qry,number,connection.con);   //returns an object containing all the details pertaining to the query
            
    //        System.out.println("Query Name - "+query_var.query_name);
    //        System.out.println("Total Number Of Results - "+query_var.total_results);
    //        System.out.println("Number of Results Retrieved - "+query_var.no_retrieved_results);
    //        System.out.println("Paper IDs - "+query_var.paper_ids);
            
            System.out.println("PLEAS333 - "+qry);
            manage_paper_retr(query_var);   //used to extract the abstracts related to the query
            
            PreparedStatement ps=connection.con.prepareStatement("SELECT EXISTS(SELECT 1 FROM query_"+user_name+" WHERE query_name='"+qry+"')");    // checking if the query has been entered before
            
            ResultSet rs=ps.executeQuery();
            int exist_check=0;
            while(rs.next()){
                exist_check=rs.getInt(1);
                System.out.println("thisworks");
            }
            
            if(exist_check==0)  // if the query has not been entered before, the query is added to the query table
            {
                System.out.println("thisworksnow!!");
                ps= connection.con.prepareStatement("Insert into query_"+user_name+" (query_name, total_results, paper_ids, no_of_retrieved_results) values (?,?,?,?)");
                ps.setString(1,query_var.query_name);
                ps.setInt(2,query_var.total_results);
                ps.setString(3,query_var.paper_ids);
                ps.setInt(4,query_var.no_retrieved_results);
                ps.executeUpdate();
                
                
            }
            else if(exist_check==1) // if the query has been entered before, the values of the attributes of the query are updated
            {
                ps= connection.con.prepareStatement("Update query_"+user_name+" set total_results = ?, paper_ids = ?, no_of_retrieved_results= ? where query_name = ?");
                ps.setInt(1,query_var.total_results);
                ps.setString(2,query_var.paper_ids);
                ps.setInt(3,query_var.no_retrieved_results);
                ps.setString(4,query_var.query_name);
                ps.executeUpdate();
                System.out.println("thisworkstoo");
            }
            return query_var.query_name;
        }
        catch(Exception e)
        {
            System.err.println("Package: XML_HIGLEVEL\tClass: session_manager\tFunction: manage_session()"+e);
        }
        return"";
    }
    
    public void manage_paper_retr(query query_var)
    {
        try
        {
            String paper_ids="";
 
            int start_index=query_var.paper_ids.indexOf("|")+1;
            
            /*| is used to signify how much of the pubmed ids have been processed ie, abstracts retrieved. 
             * all pubmed ids to the left of | have been retrieved and to the right of | have not been retrieved*/
            
            int stop_index=query_var.paper_ids.length();
            String temp=query_var.paper_ids.substring(start_index, stop_index);
            StringTokenizer tok = new StringTokenizer(temp,",");
            while(tok.hasMoreTokens())
            {
                String token=tok.nextToken();
                PreparedStatement ps=connection.con.prepareStatement("SELECT EXISTS(SELECT 1 FROM Paper_Store WHERE Pubmed_ID="+token+")"); //checking if the papers corresponding to the pubmed ids are already present
                ResultSet rs=ps.executeQuery();
                int exist_check=0;
                while(rs.next()){
                exist_check=rs.getInt(1);
                }
                if(exist_check==0)
                {
                    paper_ids+=token+",";   //if it is not present, it is appended to this string and sent for extraction
                }
                else
                {
       //             System.out.println("KICK !!!! - "+token);
                }
            }
            abstract_extractor abs_extr = new abstract_extractor();
            if(paper_ids=="")
            {
       //         System.out.println("All Papers Are Present");
            }
            else
            {
            abs_extr.extract(paper_ids, connection.con);
            }
            query_var.paper_ids=query_var.paper_ids.replace("|","");    
            
            /*| is used to signify how much of the pubmed ids have been processed ie, abstracts retrieved. 
             * all pubmed ids to the left of | have been retrieved and to the right of | have not been retrieved*/
            /*here we change the position of | to mark the pmids that have been processed*/
            query_var.paper_ids+="|";
        }
        catch(Exception e)
        {
            System.err.println("Package: XML_HIGLEVEL\tClass: query_manager\tFunction: manage_paper_retr()"+e);
        }
    }
    
    public static void main(String [] args)
    {
        database con= new database();
        query_manager sm=new query_manager(con,"");
        sm.manage_query("autism",200);
        //System.out.println(sm.query_manipulator());
    }
    
}
