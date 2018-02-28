    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xml_highlevel;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import xml_tags.*;
import xml_extractor.*;
/**
 *
 * @author ROHIT
 */

public class query_extractor {
    query query =new query();
    String user_name;
    public query_extractor(String uname)
    {
        user_name=uname;
    }
    //checks if a query has been entered before and if it has, an object with all the details of the
    //query is returned
    public query existance_check(String qry, Connection con)
    {
        try{
            int exist_check=0;
            query qry_return=new query();
            PreparedStatement ps=con.prepareStatement("SELECT EXISTS(SELECT 1 FROM query_"+user_name+" WHERE query_name='"+qry+"')");
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                exist_check=rs.getInt(1);
                System.out.println("thisworks!!");
            }
            if(exist_check==1)
            {
                ps=con.prepareStatement("Select query_name, total_results, paper_ids, no_of_retrieved_results from query_"+user_name);
                rs=ps.executeQuery();
                while(rs.next()){
                    qry_return.query_name=rs.getString(1);
                    qry_return.total_results=rs.getInt(2);
                    qry_return.paper_ids=rs.getString(3);
                    qry_return.no_retrieved_results=rs.getInt(4);
                    System.out.println("thisworks34");
                   
                }
                ps.close();
                rs.close();
                return qry_return;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            System.err.println("Package: XML_HIGLEVEL\tClass: query_extractor\tFunction: existance_check"+e);
        }
        return null;
    }
    
    //processes the query entered and sends back an object with all the details of the query
    public query process_qry(int sessn_no,String qry, int no_of_papers, Connection con)
    {
        try{

            PreparedStatement ps;
            ResultSet rs;
            query query_proc=existance_check(qry,con);// checks if the query has been entered before, if it has and object with all the details of the query is returned
            query query_var=null;
            if(query_proc==null||query_proc.total_results!=0)
            {
                query_var=retr_papers(qry,no_of_papers);
                if(query_var.total_results<query_var.no_retrieved_results)  // if the maximum number of results that can be retrieved for the query is lesser than the number of papers requested by the user
                {
                    query_var.no_retrieved_results=query_var.total_results; // the number of retrieved results is changed to the maximum number of papers that can be retrieved
                }
                
            }
            else
            {
                if(query_proc.no_retrieved_results==query_proc.total_results)   //if the number of results that were retrieved before was equal to the maximum number of results that can be retrieved for the particular query
                {
                    query_var=query_proc;
                }
                else if((no_of_papers)<query_proc.total_results)        //if the total number of papers to be retrieved is lesser than the total number of papers related to the query
                {
                    query_var=retr_papers(query_proc,(no_of_papers-query_proc.no_retrieved_results));
                }
                
                else if(query_proc.total_results<no_of_papers)      // if the total number of papers to be retrieved is greater than the total number of papers 
                {
                    query_var=retr_papers(query_proc,(query_proc.total_results-query_proc.no_retrieved_results));
                }

            }
            if (query_var==null)
            {
                Exception e=new Exception("Null Object Exception Should be Some Error in Returning Object");
                throw e;
            }
            else
           {
                return query_var;
            }
        }
        catch (Exception e)
        {
            System.err.println("Package: XML_HIGLEVEL\tClass: query_extractor\tFunction: process_qry "+e);
        }
            return null;
    }
    
    //handles queries that are entered for the first time
    public query retr_papers(String qry,int no_to_retr)
    {
        query query_var=new query();
        query_var.query_name=qry;
        query_var.no_retrieved_results= no_to_retr;
        try{
            String base_url="http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&retmax="+query_var.no_retrieved_results+"&term=";
            base_url+=qry;
            String result=retr_ppr_ids(base_url);
            int index=result.indexOf("+");  //seperating the total result count from the pubmed ids list
            query_var.paper_ids="|"+result.substring(0, index); 
            
            /*| is used to signify how much of the pubmed ids have been processed ie, abstracts retrieved. 
             * all pubmed ids to the left of | have been retrieved and to the right of | have not been retrieved*/
        //    
            query_var.total_results=Integer.parseInt(result.substring(index+1));
            
            return query_var;
            
        }
        catch(Exception e)
        {
            System.err.println("Package: XML_HIGLEVEL\tClass: query_extractor\tFunction: retr_papers(String,int)");
        }
        return null;
    }
    
    //this function is used to handle queries that have already been entered
    public query retr_papers(query query_var,int no_of_papers)
    {
        try{
            
            String base_url="http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&retstart="+query_var.no_retrieved_results+"&retmax="+no_of_papers+"&term="+query_var.query_name;
            query_var.no_retrieved_results+=no_of_papers;
            String result=retr_ppr_ids(base_url);   //the url built is sent to the retr_ppr_ids() function where the xml is downloaded and the pubmed ids are extracted
            int index=result.indexOf("+");  //seperating the total result count from the pubmed ids list
            query_var.paper_ids+=" "+result.substring(0, index);
//            System.out.println(query_var.paper_ids);
            //query_var.total_results=result.substring(0, index);
            query_var.total_results=Integer.parseInt(result.substring(index+1));
            return query_var;
        }
        catch(Exception e)
        {
            System.err.println("Package: XML_HIGLEVEL\tClass: query_extractor\tFunction: retr_papers(query)");
        }
        return null;
    }
    
    
    //retrieves pubmed ids related to the users query from the xml retrieved from the url
    public String retr_ppr_ids(String url)
    {
        try{
            
            int flag=0;
            pmid_tags id_tag = new pmid_tags();
  //          System.out.println(url);
            URL url_1 = new URL(url); 
            
            
            URLConnection url_con = url_1.openConnection(); 
            BufferedReader in = new BufferedReader(new InputStreamReader(url_con.getInputStream()));
            String inputLine;     
            String pmid_list="";
            
            boolean count_flag=true;
            int count_val=0;
            
             while ((inputLine = in.readLine()) != null) {
                 inputLine=inputLine.trim();
    //             System.out.println(inputLine);
                 if(inputLine.contains(id_tag.open_count)&&count_flag)
                  {
                      int start_index = inputLine.indexOf(id_tag.open_count)+id_tag.open_count.length();
                      int stop_index = inputLine.indexOf(id_tag.close_count);
                      String count_str;
                      count_str=inputLine.substring(start_index, stop_index);
                      
                      count_val=Integer.parseInt(count_str);
       //               System.out.println("Count Val - "+count_str);
                      count_flag=false;
                  }
                 else if(inputLine.contains(id_tag.open_id))
                 {
                     int start_index = inputLine.indexOf(id_tag.open_id)+id_tag.open_id.length();
                     int stop_index = inputLine.indexOf(id_tag.close_id);
                     String id_str;
                     id_str=inputLine.substring(start_index, stop_index);
      //               System.out.println("ID - "+id_str);
                     pmid_list+=id_str+",";
                 }
                 
             }
            
             int i=1;
            
        //     System.out.println(pmid_list);
             pmid_list+="+"+count_val;// the total number of results for the query is appended to the pmidlist
            return pmid_list;
        }
        catch(Exception e)
        {
            System.err.println("Package : xml_extractor \t Class : paper_id_extractor \t funct : retr_ppr_ids"+e);
        }
        return "";
        
    }
    /*
    
    
    public boolean retr_ppr_ids(String qry, int no_of_papers)
    {
        try{
            String base_url="http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&retmax="+no_of_papers+"&term=";
            
            //String ul="";&retmax=30";//&Retstart=";   modify for future max and retstart
            pmid_tags id_tag = new pmid_tags();
            
            base_url+=qry;
            int flag=0;
            boolean count_flag=true;
            
            System.out.println(base_url);
            URL url_1 = new URL(base_url); 
            URLConnection url_con = url_1.openConnection(); 
            BufferedReader in = new BufferedReader(new InputStreamReader(url_con.getInputStream()));
            String inputLine;     
             while ((inputLine = in.readLine()) != null) {
                 inputLine=inputLine.trim();
                 System.out.println(inputLine);
                 if(inputLine.contains(id_tag.open_count)&&count_flag)
                  {
                      int start_index = inputLine.indexOf(id_tag.open_count)+id_tag.open_count.length();
                      int stop_index = inputLine.indexOf(id_tag.close_count);
                      String count_str;
                      count_str=inputLine.substring(start_index, stop_index);
                      int count_val;
                      count_val=Integer.parseInt(count_str);
                      System.out.println("Count Val - "+count_str);
                      count_flag=false;
                  }
                 else if(inputLine.contains(id_tag.open_id))
                 {
                     int start_index = inputLine.indexOf(id_tag.open_id)+id_tag.open_id.length();
                     int stop_index = inputLine.indexOf(id_tag.close_id);
                     String id_str;
                     id_str=inputLine.substring(start_index, stop_index);
                     int ids;
                     ids=Integer.parseInt(id_str);
                      
                 }
                 
             }
            return true;
        }
        catch(Exception e)
        {
            System.err.println("Package : xml_extractor \t Class : paper_id_extractor \t funct : retr_ppr_ids"+e);
        }
        return false;
        
    }*/

public static void main(String [] args)
{
    query_extractor q=new query_extractor("");
    query qry =new query();
    q.retr_papers(qry,101);
}
//public void retr_papers(String qry,int no_to_retr)    
    
}

