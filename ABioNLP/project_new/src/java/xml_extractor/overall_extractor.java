    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xml_extractor;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import xml_tags.*;
/**
 *
 * @author rohit
 */
// coordinates the overall extraction of papers and writes it to the database
public class overall_extractor {
    overall_tags overall_tags = new overall_tags();
    journal_extractor journal_extractor=new journal_extractor();
    mesh_heading_extractor mesh = new mesh_heading_extractor();
    author_extractor author = new author_extractor();
    chemical_extractor chemical = new chemical_extractor();
    title_abstract_extractor title_abstract_extractor = new title_abstract_extractor(); 
    keyword_extractor keyword = new keyword_extractor();
    
    public void database_manipulator(Connection con, paper_store paper)
    {
        try
        {
                    //for structured abstract all the divisions are combined into one string.            
                    
                    if(paper.artitle_abstr_affil.Abstract.compareTo("")==0)
                    {
                        if(paper.artitle_abstr_affil.Unlabelled.compareTo("")!=0)
                        {
                            paper.abstrct+="Unlabelled : "+paper.artitle_abstr_affil.Unlabelled;
                        }
                        if(paper.artitle_abstr_affil.Background.compareTo("")!=0)
                        {
                            paper.abstrct+="Background : "+paper.artitle_abstr_affil.Background;
                        }
                        if(paper.artitle_abstr_affil.Objectives.compareTo("")!=0)
                        {
                            paper.abstrct+="Objectives : "+paper.artitle_abstr_affil.Objectives;
                        }
                        if(paper.artitle_abstr_affil.Methods.compareTo("")!=0)
                        {
                            paper.abstrct+="Methods : "+paper.artitle_abstr_affil.Methods;
                        }
                        if(paper.artitle_abstr_affil.Results.compareTo("")!=0)
                        {
                            paper.abstrct+="Results : "+paper.artitle_abstr_affil.Results;
                        }
                        if(paper.artitle_abstr_affil.Conclusion.compareTo("")!=0)
                        {
                            paper.abstrct+="Conclusion : "+paper.artitle_abstr_affil.Conclusion;
                        }
                        if(paper.artitle_abstr_affil.Unassigned.compareTo("")!=0)
                        {
                            paper.abstrct+="Unassigned : "+paper.artitle_abstr_affil.Unassigned;
                        }
                    }
                    else
                    {
                        paper.abstrct=paper.artitle_abstr_affil.Abstract;
                    }
                    
                    if(!paper.keywords.equals(""))
                    {
                        paper.mesh_descr+=" _|KEYWORDS|_ "+paper.keywords;//mesh descriptors and keywords are stored into a single string
                    }
                   /* String date="";
                    if(!(paper.journal.date.day==0))
                    {
                        if(!paper.journal.date.month.equals(""))
                        {
                            if(!(paper.journal.date.year==0))
                            {
                                date=paper.journal.date.day+" - "+paper.journal.date.month+" - "+paper.journal.date.year;
                            }
                            else
                            {
                                date=paper.journal.date.day+" - "+paper.journal.date.month;
                            }
                        }
                        else
                        {
                            if(!(paper.journal.date.year==0))
                            {
                                date=paper.journal.date.day+" - "+paper.journal.date.year;
                            }
                            else
                            {
                                date=""+paper.journal.date.day;
                            }
                        }
                    }
                    else
                    {
                        if(!paper.journal.date.month.equals(""))
                        {
                            if(!(paper.journal.date.year==0))
                            {
                                date=paper.journal.date.month+" - "+paper.journal.date.year;
                            }
                            else
                            {
                                date=paper.journal.date.month;
                            }
                        }
                        else
                        {
                            if(!(paper.journal.date.year==0))
                            {
                                date=""+paper.journal.date.year;
                            }
                        }
                    }
                    */
                    
                    PreparedStatement ps=con.prepareStatement("Insert into Paper_Store (Pubmed_ID,Title,Abstract,Author_list,   Journal_Name,Journal_Abbrev,ISSN_ID,ISSN_TYPE,Volume,Issue,date_day,date_month,date_year,Mesh_Heading_List,Chemical_List,Affiliation) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    ps.setInt(1, paper.pubmed_id);
                    ps.setString(2, paper.title);
                    ps.setString(3, paper.abstrct);
                    ps.setString(4, paper.author_list);
                    ps.setString(5, paper.journal.journal_title);
                    ps.setString(6, paper.journal.journal_abbrev);
                    ps.setString(7, paper.journal.Issn_id);
                    ps.setString(8, paper.journal.Issn_type);
                    ps.setString(9, paper.journal.volume);
                    ps.setString(10,paper.journal.issue);
                    ps.setInt(11,paper.journal.date.day);
                    ps.setString(12,paper.journal.date.month);
                    ps.setInt(13,paper.journal.date.year);
                    ps.setString(14,paper.mesh_descr);
                    ps.setString(15,paper.chemical_list);
                    ps.setString(16,paper.artitle_abstr_affil.Affiliation);
                    ps.executeUpdate();
                    
        }
        catch(Exception e)
        {
            System.err.println("Package : XML extractor\t Class : Overall_extractor\t Function : database_manipulator"+e);
        }
    }
    
    public paper_store extract_terms(BufferedReader in, Connection con)
    {
        try
        {
            String inputline=in.readLine();
            while (!inputline.contains(overall_tags.close_pubmed_article_set))
            {
                if(inputline.contains(overall_tags.open_pubmed_article))
                {
                    paper_store paper = new paper_store();
                    article_extractor(in,paper);
                    database_manipulator(con, paper);
                    /*
                    System.out.println("PMID = "+paper.pubmed_id); 
                    System.out.println("ISSN ID = "+paper.journal.Issn_id);
                    System.out.println("ISSN TYPE = "+paper.journal.Issn_type);
                    System.out.println("VOLUME = "+paper.journal.volume);
                    System.out.println("ISSUE = "+paper.journal.issue);
                    System.out.println("ABBREV = "+paper.journal.journal_abbrev);
                    System.out.println("TITLE = "+paper.journal.journal_title);
                    System.out.println("DAY = "+paper.journal.date.day);
                    System.out.println("MONTH = "+paper.journal.date.month);
                    System.out.println("YEAR = "+paper.journal.date.year);
                    System.out.println("TITLE = "+paper.title);
                    System.out.println("Abstract = "+paper.artitle_abstr_affil.Abstract);
                    System.out.println("Background = "+paper.artitle_abstr_affil.Background);
                    System.out.println("Objectives = "+paper.artitle_abstr_affil.Objectives);
                    System.out.println("Methods = "+paper.artitle_abstr_affil.Methods);
                    System.out.println("Results = "+paper.artitle_abstr_affil.Results);
                    System.out.println("Conclusion = "+paper.artitle_abstr_affil.Conclusion);
                    System.out.println("Unassigned = "+paper.artitle_abstr_affil.Unassigned);
                    System.out.println("Unlabelled = "+paper.artitle_abstr_affil.Unlabelled);
                    System.out.println("Abstract - \n"+paper.abstrct);
                    System.out.println("Affilliation = "+paper.artitle_abstr_affil.Affiliation+"\n\n");
                    
                    System.out.println("Chemical List = "+paper.chemical_list);
                    System.out.println("Mesh Headings = "+paper.mesh_descr);
                    System.out.println("Keywords - "+paper.keywords);
                    System.out.println("Authors - "+paper.author_list);*/

                    
                }
                
                //BOOK
             
                else if(inputline.contains(overall_tags.open_pubmedbook_article))
                {
                    paper_store paper = new paper_store();
                    book_extractor(in, paper);
                    database_manipulator(con, paper);
                }
                inputline=in.readLine();
                
            }
        }
        catch(Exception e)
        {
            System.err.println("Package : XML extractor\t Class : Overall_extractor\t Function : extract_terms"+e);
        }
        
        return null;
    }
    
    public paper_store book_extractor(BufferedReader in, paper_store paper)
    {
        try
        {
            String inputline=in.readLine();
            boolean flag=true;
            while (!inputline.contains(overall_tags.close_pubmedbook_article))
            {
                //Extract PubMed ID
                if (inputline.contains(overall_tags.open_pmid)&&flag)
                {
                    int start_index=inputline.indexOf('>');
                    flag=false;
                    paper.pubmed_id=Integer.parseInt(inputline.substring((inputline.indexOf('>')+1), (inputline.indexOf(overall_tags.close_pmid))));
                    System.out.println("PID"+ paper.pubmed_id);
                    
                }
                //Extract Book Info
                else if(inputline.contains(overall_tags.journal_tags.open_book))
                {
                    journal_extractor.extract_book(in, paper);
                    System.out.println("Publisher - "+paper.journal.journal_title);
                    System.out.println("Book Title"+paper.journal.journal_abbrev);
                }
                //Extract Article Title
                else if(inputline.contains(overall_tags.article.book_open_article_title))
                {
                    int start_index=inputline.indexOf(">")+1;
                    int stop_index=inputline.indexOf(overall_tags.article.close_article_title);
                    paper.artitle_abstr_affil.Article_title=inputline.substring(start_index, stop_index);
                    paper.title=paper.artitle_abstr_affil.Article_title;
                    System.out.println("Article Title"+paper.title);
                    
                }
                //Extract Abstract
                else if(inputline.contains(overall_tags.article.open_abstract))
                {
                    title_abstract_extractor.abstract_extractor(in, paper);
                    System.out.println("Abstract"+paper.artitle_abstr_affil.Abstract);
                }
                //Extract Mesh Heading List
                else if(inputline.contains(overall_tags.mesh.open_mesh_heading_list))
                {
                    mesh.mesh_term_extractor(in,paper);
                    System.out.println("Mesh List"+paper.mesh_descr);
                }
                //Extract Author List
                else if(inputline.contains(overall_tags.author.open_author_list))
                {
                        author.extract_authors(in, paper,true);
                        System.out.println("Authors"+paper.author_list);
                }
                //Extract Chemical Name
                else if(inputline.contains(overall_tags.chemicals.open_chemical_list))
                {
                    chemical.extract_chemical(in, paper);
                    System.out.println("Chemical Name"+paper.chemical_list);
                }
                //Extract Keywords
                else if(inputline.contains(overall_tags.keywords.open_keyword_list))
                {
                    keyword.extract_keyword(in, paper);
                    System.out.println("Keywords - "+paper.keywords);
                }
                inputline=in.readLine();
            }
        }
        catch(Exception e)
        {
            System.err.println("Package : XML extractor\t Class : Overall_extractor\t Function : book_extractor"+e);
        }
        return null;
    }
    
    
    public paper_store article_extractor(BufferedReader in, paper_store paper)
    {
        try
        {
            String inputline=in.readLine();
            boolean flag=true;
            while (!inputline.contains(overall_tags.close_pubmed_article))
            {
                //Extract PubMed ID
                if (inputline.contains(overall_tags.open_pmid)&&flag)
                {
                    int start_index=inputline.indexOf('>');
                    flag=false;
                    paper.pubmed_id=Integer.parseInt(inputline.substring((inputline.indexOf('>')+1), (inputline.indexOf(overall_tags.close_pmid))));
                }
                //Extract Journal Info
                else if(inputline.contains(overall_tags.journal_tags.open_journal))
                {
                    journal_extractor.extract_journal(in, paper);
                }
                //Extract Article Title
                else if(inputline.contains(overall_tags.article.open_article_title))
                {
                    int start_index=inputline.indexOf(overall_tags.article.open_article_title)+overall_tags.article.open_article_title.length();
                    int stop_index=inputline.indexOf(overall_tags.article.close_article_title);
                    paper.artitle_abstr_affil.Article_title=inputline.substring(start_index, stop_index);
                    paper.title=paper.artitle_abstr_affil.Article_title;
                }
                //Extract Abstract
                else if(inputline.contains(overall_tags.article.open_abstract))
                {
                    title_abstract_extractor.abstract_extractor(in, paper);
                }
                //Extract Affiliation
                else if(inputline.contains(overall_tags.article.open_affiliation))
                {
                    int start_index=inputline.indexOf(overall_tags.article.open_affiliation)+overall_tags.article.open_affiliation.length();
                    int stop_index=inputline.indexOf(overall_tags.article.close_affiliation);
                    paper.artitle_abstr_affil.Affiliation=inputline.substring(start_index, stop_index);
                }
                //Extract Mesh Heading List
                else if(inputline.contains(overall_tags.mesh.open_mesh_heading_list))
                {
                    mesh.mesh_term_extractor(in,paper);
                }
                //Extract Author List
                else if(inputline.contains(overall_tags.author.open_author_list))
                {
                    if(inputline.contains(overall_tags.author.complete_n))
                    {
                        author.extract_authors(in, paper,false);
                    }
                    else //if(inputline.contains(overall_tags.author.complete_y))
                    {
                        author.extract_authors(in, paper,true);
                    }
                    
                }
                //Extract Chemical Name
                else if(inputline.contains(overall_tags.chemicals.open_chemical_list))
                {
                    chemical.extract_chemical(in, paper);
                }
                //Extract Keywords
                else if(inputline.contains(overall_tags.keywords.open_keyword_list))
                {
                    keyword.extract_keyword(in, paper);
                }
                inputline=in.readLine();
            }
        }
        catch (Exception e)
        {
            System.err.println("Package : XML extractor\t Class : Overall_extractor\t Function : article_extractor"+e);
        }
        return null;
    }
    
    
    
    
}
