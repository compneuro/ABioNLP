/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xml_tags;

import java.io.BufferedReader;
import xml_extractor.paper_store;

/**
 *
 * @author rohit
 */
public class overall_tags {
    
    public final String open_pubmed_article_set="<PubmedArticleSet>";
    public final String close_pubmed_article_set="</PubmedArticleSet>";
    public final String open_pubmed_article="<PubmedArticle>";
    public final String close_pubmed_article="</PubmedArticle>";
    public final String open_pubmedbook_article="<PubmedBookArticle>";
    public final String close_pubmedbook_article="</PubmedBookArticle>";
    public final String open_pmid="<PMID Version=";
    public final String close_pmid="</PMID>";
    public date_tags date=new date_tags();
    public artitle_abstr_affil_tags article = new artitle_abstr_affil_tags();
    public author_tags author=new author_tags();
    public journal_tags journal_tags = new journal_tags();
    public mesh_descrip_tags mesh = new mesh_descrip_tags();
    public chemical_tags chemicals = new chemical_tags();
    public keyword_tags keywords = new keyword_tags();
}
