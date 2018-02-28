/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xml_extractor;

import xml_tags.author_tags;

/**
 *
 * @author rohit
 */
//object for storing all the details of a single article
public class paper_store {
    
    int id;
    int pubmed_id;
    String title;
    String abstrct;
    String mesh_descr;
    String keywords;
    String author_list;
    String chemical_list;
    artitle_abstr_affil artitle_abstr_affil = new artitle_abstr_affil();
    journal journal =new journal();
    date date = new date();
    
    public paper_store()
    {
        id=0;
        pubmed_id=0;
        title="";
        abstrct="";
        mesh_descr="";
        keywords="";
        author_list="";
        chemical_list="";
    }
    public static void main(String[] args) 
            {          
             /*   authors a=new authors();
                authors temp;
                int i;
                temp=a;
                temp.insert_details("s", "c", "d", "");
                for(i=1;i<=4;++i)
                {
                    authors b=new authors();
                    b.insert_details("s", "c", "d", "");
                    temp.next=b;
                    temp=b;
                }
                temp.next=null;
                temp=a;
                while(temp!=null)
                {
                    System.out.println(temp.suffix);
                    temp=temp.next;
                }
                author_tags at=new author_tags();*/
                paper_id_extractor pide=new paper_id_extractor();
            //    pide.retr_ppr_ids("cerebellum", 3000);
                
            }
            
    
}
