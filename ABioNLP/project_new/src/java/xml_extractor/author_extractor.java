/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xml_extractor;

import java.io.BufferedReader;
import xml_tags.author_tags;

/**
 *
 * @author rohit
 */
//class used to extract the author names from the article.
//author names are stored as a single string each of the authors are seperated by |
public class author_extractor {
    author_tags author = new author_tags();
    public void extract_authors(BufferedReader in, paper_store paper, boolean complete)
    {
        try
        {
            String inputline=in.readLine();
            String temp;
            while(!inputline.contains(author.close_author_list))
            {
                if(inputline.contains(author.open_author_y)||inputline.contains(author.open_author))
                {
                    temp=indv_author_extract(in);
                    paper.author_list+=temp+" | ";
                }
                
                inputline=in.readLine();
            }
            if(!complete)
            {
                paper.author_list+="et al"; // if the author list is not complete
            }
        }
        catch (Exception e)
        {
            System.err.println("Package : XML extractor\t Class : author_extractor\t Function : extract_authors"+e);
        }
    }
    //extracts the details of the individual authors
    public String indv_author_extract(BufferedReader in)
    {
        try
        {
            String inputline=in.readLine();
            String temp="";
            while(!inputline.contains(author.close_author)) 
            {
                if(inputline.contains(author.open_lastname))
                {
                    int start_index=inputline.indexOf(author.open_lastname)+author.open_lastname.length();
                    int stop_index=inputline.indexOf(author.close_lastname);
                    temp+=inputline.substring(start_index, stop_index)+" ";
                }
                else if(inputline.contains(author.open_forename))
                {
                    int start_index=inputline.indexOf(author.open_forename)+author.open_forename.length();
                    int stop_index=inputline.indexOf(author.close_forename);
                    temp+=inputline.substring(start_index, stop_index)+" ";
                }
                else if(inputline.contains(author.open_suffix))
                {
                    int start_index=inputline.indexOf(author.open_suffix)+author.open_suffix.length();
                    int stop_index=inputline.indexOf(author.close_suffix);
                    temp+=inputline.substring(start_index, stop_index)+" ";
                }
                else if(inputline.contains(author.open_initials))
                {
                    int start_index=inputline.indexOf(author.open_initials)+author.open_initials.length();
                    int stop_index=inputline.indexOf(author.close_initials);
                    temp+=inputline.substring(start_index, stop_index)+" ";
                }
                inputline=in.readLine();
            }
            return temp;
        }
        catch (Exception e)
        {
            System.err.println("Package : XML extractor\t Class : author_extractor\t Function : indv_author_extract"+e);
        }
        return "";
    }
}
