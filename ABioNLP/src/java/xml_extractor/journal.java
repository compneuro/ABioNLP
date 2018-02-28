/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xml_extractor;

/**
 *
 * @author rohit
 */
//class for creating an object representing a journal.
public class journal {
    String Issn_type;
    String Issn_id;
    String journal_title;
    String journal_abbrev;
    String volume;
    String issue;
    date date=new date();
    public journal()
    {
        Issn_type="";
        Issn_id="";
        journal_title="";
        journal_abbrev="";
        volume="";
        issue="";
    }
}
