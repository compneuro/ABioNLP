/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xml_extractor;

import java.io.BufferedReader;
import xml_tags.artitle_abstr_affil_tags;

/**
 *
 * @author rohit
 */
//extracts the article title and abstracts from the xml.
public class title_abstract_extractor {
    artitle_abstr_affil_tags artitle_abstr_affil_tags=new artitle_abstr_affil_tags();
    public void abstract_extractor(BufferedReader in,paper_store paper)
    {
        try
        {
            String inputline= in.readLine();
            while(!inputline.contains(artitle_abstr_affil_tags.close_abstract))
            {
                
                if(inputline.contains(artitle_abstr_affil_tags.open_abstract_text_label_not_complete))
                {
                    if(inputline.contains(artitle_abstr_affil_tags.open_abstract_text))
                    {
                        int start_index=inputline.indexOf(artitle_abstr_affil_tags.open_abstract_text)+artitle_abstr_affil_tags.open_abstract_text.length();
                        int stop_index=inputline.indexOf(artitle_abstr_affil_tags.close_abstract_text);
                        paper.artitle_abstr_affil.Abstract+=inputline.substring(start_index, stop_index)+"\n";
                    }
                
                    //For Structured Abstracts
                    else if(inputline.contains(artitle_abstr_affil_tags.nlm_category))
                    {
                        //Background
                        if(inputline.contains(artitle_abstr_affil_tags.nlm_category_background))
                        {
                            int start_index=inputline.indexOf(artitle_abstr_affil_tags.nlm_category_background)+artitle_abstr_affil_tags.nlm_category_background.length();
                            int stop_index=inputline.indexOf(artitle_abstr_affil_tags.close_abstract_text);
                            paper.artitle_abstr_affil.Background+=inputline.substring(start_index, stop_index)+"\n";
                        }
                        //Objective
                        else if(inputline.contains(artitle_abstr_affil_tags.nlm_category_objective))
                        {
                            int start_index=inputline.indexOf(artitle_abstr_affil_tags.nlm_category_objective)+artitle_abstr_affil_tags.nlm_category_objective.length();
                            int stop_index=inputline.indexOf(artitle_abstr_affil_tags.close_abstract_text);
                            paper.artitle_abstr_affil.Objectives+=inputline.substring(start_index, stop_index)+"\n";
                        }
                        //Methods
                        else if(inputline.contains(artitle_abstr_affil_tags.nlm_category_methods))
                        {
                            int start_index=inputline.indexOf(artitle_abstr_affil_tags.nlm_category_methods)+artitle_abstr_affil_tags.nlm_category_methods.length();
                            int stop_index=inputline.indexOf(artitle_abstr_affil_tags.close_abstract_text);
                            paper.artitle_abstr_affil.Methods+=inputline.substring(start_index, stop_index)+"\n";
                        }
                        //Results
                        else if(inputline.contains(artitle_abstr_affil_tags.nlm_category_results))
                        {
                            int start_index=inputline.indexOf(artitle_abstr_affil_tags.nlm_category_results)+artitle_abstr_affil_tags.nlm_category_results.length();
                            int stop_index=inputline.indexOf(artitle_abstr_affil_tags.close_abstract_text);
                            paper.artitle_abstr_affil.Results+=inputline.substring(start_index, stop_index)+"\n";
                        }
                        //Conclusion
                        else if(inputline.contains(artitle_abstr_affil_tags.nlm_category_conclusion))
                        {
                            int start_index=inputline.indexOf(artitle_abstr_affil_tags.nlm_category_conclusion)+artitle_abstr_affil_tags.nlm_category_conclusion.length();
                            int stop_index=inputline.indexOf(artitle_abstr_affil_tags.close_abstract_text);
                            paper.artitle_abstr_affil.Conclusion+=inputline.substring(start_index, stop_index)+"\n";
                        }
                        //Unassigned
                        else if(inputline.contains(artitle_abstr_affil_tags.nlm_category_unassigned))
                        {
                            int start_index=inputline.indexOf(artitle_abstr_affil_tags.nlm_category_unassigned)+artitle_abstr_affil_tags.nlm_category_unassigned.length();
                            int stop_index=inputline.indexOf(artitle_abstr_affil_tags.close_abstract_text);
                            paper.artitle_abstr_affil.Unassigned+=inputline.substring(start_index, stop_index)+"\n";
                        }
                        //Unlabeled
                        else if(inputline.contains(artitle_abstr_affil_tags.nlm_category_unlabelled))
                        {
                            int start_index=inputline.indexOf(artitle_abstr_affil_tags.nlm_category_unlabelled)+artitle_abstr_affil_tags.nlm_category_unlabelled.length();
                            int stop_index=inputline.indexOf(artitle_abstr_affil_tags.close_abstract_text);
                            paper.artitle_abstr_affil.Unlabelled+=inputline.substring(start_index, stop_index)+"\n";
                        }
                    }
                    else if(inputline.contains(artitle_abstr_affil_tags.unlabelled_tag))
                    {
                        int start_index=inputline.indexOf(artitle_abstr_affil_tags.unlabelled_tag)+artitle_abstr_affil_tags.unlabelled_tag.length();
                        int stop_index=inputline.indexOf(artitle_abstr_affil_tags.close_abstract_text);
                        paper.artitle_abstr_affil.Unlabelled+=inputline.substring(start_index, stop_index)+"\n";
                    }
                }
                inputline= in.readLine();
            }
        }
        catch (Exception e)
        {
            System.err.println("Package : XML Extractor\t Class : title_abstract_extractor\t Function : abstract_extractor()"+e);
        }
    }
    
}
