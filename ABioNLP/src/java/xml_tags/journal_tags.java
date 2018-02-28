/*
 * To change this template, choose Tools | Templates
 * and open the tepubmed_article_setmplate in the editor.
 */
package xml_tags;

import java.io.BufferedReader;
import xml_extractor.paper_store;
/**
 *
 * @author rohit
 */
public class journal_tags {
    public final String open_journal="<Journal>";
    public final String close_journal="</Journal>";
    public final String open_issn_type_print="<ISSN IssnType=\"Print\">";
    public final String open_issn_type_electronic="<ISSN IssnType=\"Electronic\">";
    public final String open_issn_type_undetermined="<ISSN IssnType=\"Undetermined\">";
    public final String close_issn_type="</ISSN>";
    public final String open_journal_issue_internet="<JournalIssue CitedMedium=\"Internet\">";
    public final String open_journal_issue_print="<JournalIssue CitedMedium=\"Print\">";
    public final String close_journal_issue="</JournalIssue>";
    public final String open_volume="<Volume>";
    public final String close_volume="</Volume>";
    public final String open_issue="<Issue>";
    public final String close_issue="</Issue>";
    public final String open_pubdate="<PubDate>";
    public final String close_pubdate="</PubDate>";
    public date_tags date_tag=new date_tags();
    public final String open_title="<Title>";
    public final String close_title="</Title>";
    public final String open_iso_abbrv="<ISOAbbreviation>";
    public final String close_iso_abbrv="</ISOAbbreviation>";
    public final String open_book="<Book>";
    public final String close_book="</Book>";
    public final String book_publisher_open="<Publisher>";
    public final String book_publisher_close="</Publisher>";
    public final String book_publisher_name_open="<PublisherName>";
    public final String book_publisher_name_close="</PublisherName>";
    public final String book_publisher_location_open="<PublisherLocation>";
    public final String book_publisher_location_close="</PublisherLocation>";
    public final String book_publisher_title_open="<BookTitle";
    public final String book_publisher_title_close="</BookTitle>";
    
}
