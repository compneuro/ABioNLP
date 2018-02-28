/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xml_extractor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import xml_tags.pmid_tags;

/**
 *
 * @author rohit
 */
//retrieves pubmed id of papers related to a users query from pubmed 
public class paper_id_extractor {
    
    public void retrieve_count()
    {
        
    }
    
  /*  public boolean retr_ppr_ids(String qry, int no_of_papers)
    {
        try{
            String base_url="http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&retmax="+no_of_papers+"&term="; // url for retrieving pubmed ids related to the query as an xml
            
            //String ul="";&retmax=30";//&Retstart=";   modify for future max and retstart
            pmid_tags id_tag = new pmid_tags();
            
            
            base_url+=qry;
            int flag=0;
            boolean count_flag=true;
            
//            System.out.println(base_url);
            URL url_1 = new URL(base_url); 
            URLConnection url_con = url_1.openConnection(); 
            BufferedReader in = new BufferedReader(new InputStreamReader(url_con.getInputStream()));
            String inputLine;     
            
            
             while ((inputLine = in.readLine()) != null) {
                 inputLine=inputLine.trim();
  //               System.out.println(inputLine);
                 if(inputLine.contains(id_tag.open_count)&&count_flag)
                  {
                      int start_index = inputLine.indexOf(id_tag.open_count)+id_tag.open_count.length();
                      int stop_index = inputLine.indexOf(id_tag.close_count);
                      String count_str;
                      count_str=inputLine.substring(start_index, stop_index);
                      int count_val;
                      count_val=Integer.parseInt(count_str);
    //                  System.out.println("Count Val - "+count_str);
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
    
}
