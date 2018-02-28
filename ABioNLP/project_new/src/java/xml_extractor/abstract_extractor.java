/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xml_extractor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import xml_extractor.paper_store;
import xml_tags.overall_tags;

/**
 *
 * @author rohit
 */

//starts of paper retrieval and then passes on the inputstream to overall_extractor
public class abstract_extractor {
    
    paper_store ppr_store=new paper_store();
    overall_extractor overall=new overall_extractor();
    overall_tags overall_tags=new overall_tags();
    //pubmedids are sent to this function. it then retrieves the papers corresponding to these 
    //ids and initiates the writing into the database procedure.
    public void extract(String pid_list, Connection con)
    {
        try
        {
            pid_list=pid_list.trim();
            String u="https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id="+pid_list;//20879892
            String ul="&retmode=xml&rettype=citation";// &rettype=fasta&retmode=text";
            u+=ul;
            System.out.println("URL - "+u);
            URL google = new URL(u);            
            URLConnection yc = google.openConnection();     
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputline;
            
            while ((inputline = in.readLine()) != null) 
            {
           //     System.out.println(inputline);
                if (inputline.contains(overall_tags.open_pubmed_article_set))
                {
                    paper_store abs = overall.extract_terms(in,con);
                }   
                
            }
        }
        catch(Exception e)
        {
            System.err.println("Class : Abstract_extractor\t Function : extract()"+e);
        }
    }
    
    public static void main(String[] args) 
    {          
        abstract_extractor abs=new abstract_extractor();
        //abs.extract("");
    }
}
