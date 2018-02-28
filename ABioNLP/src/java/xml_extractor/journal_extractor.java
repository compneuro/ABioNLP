/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xml_extractor;

import java.io.BufferedReader;
import xml_tags.journal_tags;
/**
 *
 * @author rohit
 */
// class for extracting the information about the journal or book
public class journal_extractor {
    journal_tags journal_tags=new journal_tags();
    public void extract_journal(BufferedReader in,paper_store paper)
    {
        try
        {
            String inputline=in.readLine();
            while (!inputline.contains(journal_tags.close_journal))
            {
                if(inputline.contains(journal_tags.open_issn_type_electronic))
                {
                    paper.journal.Issn_type="Electronic";
                    int start_index=(inputline.indexOf(journal_tags.open_issn_type_electronic)+journal_tags.open_issn_type_electronic.length());
                    int stop_index= inputline.indexOf(journal_tags.close_issn_type);
                    paper.journal.Issn_id=inputline.substring(start_index,stop_index);
                }
                else if(inputline.contains(journal_tags.open_issn_type_print))
                {
                    paper.journal.Issn_type="Print";
                    int start_index=(inputline.indexOf(journal_tags.open_issn_type_print)+journal_tags.open_issn_type_print.length());
                    int stop_index= inputline.indexOf(journal_tags.close_issn_type);
                    paper.journal.Issn_id=inputline.substring(start_index,stop_index);
                }
                else if(inputline.contains(journal_tags.open_issn_type_undetermined))
                {
                    paper.journal.Issn_type="Undetermined";
                    int start_index=(inputline.indexOf(journal_tags.open_issn_type_undetermined)+journal_tags.open_issn_type_undetermined.length());
                    int stop_index= inputline.indexOf(journal_tags.close_issn_type);
                    paper.journal.Issn_id=inputline.substring(start_index,stop_index);
                }
                
                if(inputline.contains(journal_tags.open_journal_issue_internet)||inputline.contains(journal_tags.open_journal_issue_print))
                {
                    extract_journal_issue_info(in,paper);
                }
                else if(inputline.contains(journal_tags.open_title))
                {
                    int start_index=(inputline.indexOf(journal_tags.open_title)+journal_tags.open_title.length());
                    int stop_index= inputline.indexOf(journal_tags.close_title);
                    paper.journal.journal_title=inputline.substring(start_index,stop_index);
                }
                else if (inputline.contains(journal_tags.open_iso_abbrv))
                {
                    int start_index=(inputline.indexOf(journal_tags.open_iso_abbrv)+journal_tags.open_iso_abbrv.length());
                    int stop_index= inputline.indexOf(journal_tags.close_iso_abbrv);
                    paper.journal.journal_abbrev=inputline.substring(start_index,stop_index);
                }
                
                inputline=in.readLine();
            
                
            }
        }
        catch(Exception e)
        {
            System.err.println("Package : XML extractor\t Class : journal_extractor\t Function : extract_journal()"+e);
        }
    }
    
    public void extract_journal_issue_info(BufferedReader in,paper_store paper)
    {
        try
        {
            String inputline=in.readLine();
            while (!inputline.contains(journal_tags.close_journal_issue))
            {
                if(inputline.contains(journal_tags.open_volume))
                {
                    int start_index=inputline.indexOf(journal_tags.open_volume)+journal_tags.open_volume.length();
                    int stop_index=inputline.indexOf(journal_tags.close_volume);
                    paper.journal.volume=inputline.substring(start_index, stop_index);
                }
                else if(inputline.contains(journal_tags.open_issue))
                {
                    int start_index=inputline.indexOf(journal_tags.open_issue)+journal_tags.open_issue.length();
                    int stop_index=inputline.indexOf(journal_tags.close_issue);
                    paper.journal.issue=inputline.substring(start_index, stop_index);
                }
                else if(inputline.contains(journal_tags.open_pubdate))
                {
                    extract_journal_issue_pubdate(in,paper);
                }
                
                inputline=in.readLine();
            }
            
        }
        catch (Exception e)
        {
            System.err.println("Package : XML Extractor\t Class : journal_extractor\t Function : extract_journal_info()"+e);
        }
    }
    
    public void extract_journal_issue_pubdate(BufferedReader in, paper_store paper)
    {
        try
        {
            String inputline=in.readLine();
            while (!inputline.contains(journal_tags.close_pubdate))
            {
                if(inputline.contains(journal_tags.date_tag.open_year))
                {
                    int start_index=inputline.indexOf(journal_tags.date_tag.open_year)+journal_tags.date_tag.open_year.length();
                    int stop_index=inputline.indexOf(journal_tags.date_tag.close_year);
                    paper.journal.date.year=Integer.parseInt(inputline.substring(start_index, stop_index));
                }
                else if(inputline.contains(journal_tags.date_tag.open_month))
                {
                    int start_index=inputline.indexOf(journal_tags.date_tag.open_month)+journal_tags.date_tag.open_month.length();
                    int stop_index=inputline.indexOf(journal_tags.date_tag.close_month);
                    paper.journal.date.month=inputline.substring(start_index, stop_index);
                }
                else if(inputline.contains(journal_tags.date_tag.open_day))
                {
                    int start_index=inputline.indexOf(journal_tags.date_tag.open_day)+journal_tags.date_tag.open_day.length();
                    int stop_index=inputline.indexOf(journal_tags.date_tag.close_day);
                    paper.journal.date.day=Integer.parseInt(inputline.substring(start_index, stop_index));
                }
                
                inputline=in.readLine();
            }
            
            
        }
        catch (Exception e)
        {
            System.err.println("Package : XML Extractor\t Class : journal_extractor\t Function : extract_journal_issue_pubdate()"+e);
        }
    }
    // used to extract details about the book
    public void extract_book(BufferedReader in,paper_store paper)
    {
        try
        {
            String inputline=in.readLine();
            while(!inputline.contains(journal_tags.close_book))
            {
                if(inputline.contains(journal_tags.book_publisher_open))
                {
                    extract_publisher_info(in,paper);
                }
                else if(inputline.contains(journal_tags.book_publisher_title_open))
                {
                    int start_index=inputline.indexOf(">")+1;
                    int stop_index=inputline.indexOf(journal_tags.book_publisher_title_close);
                    paper.journal.journal_abbrev="Book title: "+inputline.substring(start_index, stop_index);
                }
                else if(inputline.contains(journal_tags.open_pubdate))
                {
                    extract_journal_issue_pubdate(in, paper);
                    System.out.println("Finish");
                }
                inputline=in.readLine();
            }
        }
        catch (Exception e)
        {
            System.err.println("Package : XML Extractor\t Class : journal_extractor\t Function : extract_book()"+e);
        }
    }

    public void extract_publisher_info(BufferedReader in, paper_store paper)
    {
        try
        {
            String inputline=in.readLine();
            while(!inputline.contains(journal_tags.book_publisher_close))
            {
                if(inputline.contains(journal_tags.book_publisher_name_open))
                {
                    int start_index=inputline.indexOf(journal_tags.book_publisher_name_open)+journal_tags.book_publisher_name_open.length();
                    int stop_index=inputline.indexOf(journal_tags.book_publisher_name_close);
                    paper.journal.journal_title+="Book: "+inputline.substring(start_index, stop_index);
                }
                else if(inputline.contains(journal_tags.book_publisher_location_open))
                {
                    int start_index=inputline.indexOf(journal_tags.book_publisher_location_open)+journal_tags.book_publisher_location_open.length();
                    int stop_index=inputline.indexOf(journal_tags.book_publisher_location_close);
                    paper.journal.journal_title+=", "+inputline.substring(start_index, stop_index);
                }
                inputline=in.readLine();
            }
        }
        catch (Exception e)
        {
            System.err.println("Package : XML Extractor\t Class : journal_extractor\t Function : extract_publisher_info()"+e);
        }
    }
    
}
