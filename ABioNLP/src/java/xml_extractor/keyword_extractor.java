/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xml_extractor;

import java.io.BufferedReader;
import xml_tags.keyword_tags;
/**
 *
 * @author rohit
 */
// extracts the keywords related to the article.
// three types of keywords maj topic y, maj topic n and normal keywords.
// multiple keywords seperated by |
public class keyword_extractor {
    keyword_tags keyword = new keyword_tags();
    public void extract_keyword(BufferedReader in, paper_store paper)
    {
        try
        {
            String inputline=in.readLine();
            while(!inputline.contains(keyword.close_keyword_list))
            {
                if(inputline.contains(keyword.open_keyword_majtopic_y))
                {
                    int start_index=inputline.indexOf(keyword.open_keyword_majtopic_y)+keyword.open_keyword_majtopic_y.length();
                    int stop_index=inputline.indexOf(keyword.close_keyword);
                    paper.keywords+="<key_maj_y: "+inputline.substring(start_index, stop_index)+"> | ";
                }
                else if(inputline.contains(keyword.open_keyword_majtopic_n))
                {
                    int start_index=inputline.indexOf(keyword.open_keyword_majtopic_n)+keyword.open_keyword_majtopic_n.length();
                    int stop_index=inputline.indexOf(keyword.close_keyword);
                    paper.keywords+="<key_maj_n: "+inputline.substring(start_index, stop_index)+"> | ";
                }
                else if(inputline.contains(keyword.open_keyword))
                {
                    int start_index=inputline.indexOf(">")+1;
                    int stop_index=inputline.indexOf(keyword.close_keyword);
                    paper.keywords+="<keyword: "+inputline.substring(start_index, stop_index)+"> | ";
                }
                inputline=in.readLine();
            }
        }
        catch (Exception e)
        {
            System.err.println("Package : XML extractor\t Class : keyword_extractor\t Function : keyword_extractor"+e);
        }
    }
}
