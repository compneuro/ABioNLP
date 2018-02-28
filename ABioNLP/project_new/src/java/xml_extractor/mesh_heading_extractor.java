/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xml_extractor;
import java.io.BufferedReader;
import xml_tags.mesh_descrip_tags;
/**
 *
 * @author rohit
 */
//extracts mesh headings from the xml
// mesh terms seperated by |
public class mesh_heading_extractor {
    
    mesh_descrip_tags mesh_descrip_tags=new mesh_descrip_tags();
    public void mesh_term_extractor(BufferedReader in, paper_store paper)
    {
        try
        {
            String inputline=in.readLine();
            String temp;
            while(!inputline.contains(mesh_descrip_tags.close_mesh_heading_list))
            {
                if(inputline.contains(mesh_descrip_tags.open_mesh_heading))
                {
                    temp=indv_mesh_heading_extr(in);
                    paper.mesh_descr+=temp+" | ";
                }
                inputline=in.readLine();
            }
        }
        catch (Exception e)
        {
            System.err.println("Package : XML extractor\t Class : mesh_heading_extractor\t Function : mesh_term_extractor"+e);
        }
    }
    
    public String indv_mesh_heading_extr(BufferedReader in)
    {
        try
        {
            String inputline=in.readLine();
            String temp="";
            while(!inputline.contains(mesh_descrip_tags.close_mesh_heading))
            {
                
                //Mesh Descriptor
                if(inputline.contains(mesh_descrip_tags.open_descrp_name))
                {
                    //Major Topic Y
                    if(inputline.contains(mesh_descrip_tags.maj_topic_y))
                    {
                        //Geographic 
                        if(inputline.contains(mesh_descrip_tags.geographical))
                        {
                            int start_index=inputline.indexOf(mesh_descrip_tags.geographical)+mesh_descrip_tags.geographical.length();
                            int stop_index=inputline.indexOf(mesh_descrip_tags.close_descrp_name);
                            temp+="<mesh_descriptor_y_geographical: "+inputline.substring(start_index, stop_index)+">";
                        }
                        //Normal
                        else
                        {
                            int start_index=inputline.indexOf(mesh_descrip_tags.maj_topic_y)+mesh_descrip_tags.maj_topic_y.length();
                            int stop_index=inputline.indexOf(mesh_descrip_tags.close_descrp_name);
                            temp+="<mesh_descriptor_y: "+inputline.substring(start_index+1, stop_index)+">";
                        }
                    }
                    //Major Topic N
                    else if(inputline.contains(mesh_descrip_tags.maj_topic_n))
                    {
                        //Geographic
                        if(inputline.contains(mesh_descrip_tags.geographical))
                        {
                            int start_index=inputline.indexOf(mesh_descrip_tags.geographical)+mesh_descrip_tags.geographical.length();
                            int stop_index=inputline.indexOf(mesh_descrip_tags.close_descrp_name);
                            temp+="<mesh_descriptor_n_geographical: "+inputline.substring(start_index, stop_index)+"> ";
                        }
                        //Normal
                        else
                        {
                            int start_index=inputline.indexOf(mesh_descrip_tags.maj_topic_n)+mesh_descrip_tags.maj_topic_n.length();
                            int stop_index=inputline.indexOf(mesh_descrip_tags.close_descrp_name);
                            temp+="<mesh_descriptor_n: "+inputline.substring(start_index+1, stop_index)+"> ";
                        }
                    }
                }
                //Mesh Qualifier
                else if(inputline.contains(mesh_descrip_tags.open_qualifier_name))
                {
                    //Major Topic Y
                    if(inputline.contains(mesh_descrip_tags.maj_topic_y))
                    {
                        //Geographical
                        if(inputline.contains(mesh_descrip_tags.geographical))
                        {
                            int start_index=inputline.indexOf(mesh_descrip_tags.geographical)+mesh_descrip_tags.geographical.length();
                            int stop_index=inputline.indexOf(mesh_descrip_tags.close_qualifier_name);
                            temp+="<mesh_qualifier_y_geographical: "+inputline.substring(start_index, stop_index)+">";
                        }
                        //Normal
                        else
                        {
                            int start_index=inputline.indexOf(mesh_descrip_tags.maj_topic_y)+mesh_descrip_tags.maj_topic_y.length();
                            int stop_index=inputline.indexOf(mesh_descrip_tags.close_qualifier_name);
                            temp+="<mesh_qualifier_y: "+inputline.substring(start_index+1, stop_index)+">";
                        }
                    }
                    //Major Topic N
                    else if(inputline.contains(mesh_descrip_tags.maj_topic_n))
                    {
                        //Geographical
                        if(inputline.contains(mesh_descrip_tags.geographical))
                        {
                            int start_index=inputline.indexOf(mesh_descrip_tags.geographical)+mesh_descrip_tags.geographical.length();
                            int stop_index=inputline.indexOf(mesh_descrip_tags.close_qualifier_name);
                            temp+="<mesh_qualifier_n_geographical: "+inputline.substring(start_index, stop_index)+">";
                        }
                        //Normal
                        else
                        {
                            int start_index=inputline.indexOf(mesh_descrip_tags.maj_topic_n)+mesh_descrip_tags.maj_topic_n.length();
                            int stop_index=inputline.indexOf(mesh_descrip_tags.close_qualifier_name);
                            temp+="<mesh_qualifier_n: "+inputline.substring(start_index+1, stop_index)+">";
                        }
                    }
                }
                inputline=in.readLine();
            }
            return temp;
        }
        catch (Exception e)
        {
            System.err.println("Package : XML extractor\t Class : mesh_heading_extractor\t Function : indv_mesh_heading_extr"+e);
        }
        return "";
    }
    
}
