/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xml_extractor;
import java.io.BufferedReader;
import xml_tags.chemical_tags;
/**
 *
 * @author rohit
 */
// extracts the chemical list.
// each chemical has a name and a registry number which are extracted and stored
// each entry is seperated by |
public class chemical_extractor {
    chemical_tags chemical = new chemical_tags();
    
    public void extract_chemical(BufferedReader in, paper_store paper)
    {
        try
        {
            String inputline=in.readLine();
            while(!inputline.contains(chemical.close_chemical_list))
            {
                if(inputline.contains(chemical.open_chemical))
                {
                    paper.chemical_list+=indv_chemical_extractor(in)+" | ";
                }
                inputline=in.readLine();
            }
        }
        catch (Exception e)
        {
            System.err.println("Package : XML extractor\t Class : chemical_extractor\t Function : extract_chemical()"+e);
        }
    }
    
    public String indv_chemical_extractor(BufferedReader in)
    {
        try
        {
            String inputline=in.readLine();
            String temp="";
            while(!inputline.contains(chemical.close_chemical))
            {
                if(inputline.contains(chemical.open_name_of_substance))
                {
                    int start_index=inputline.indexOf(chemical.open_name_of_substance)+chemical.open_name_of_substance.length();
                    int stop_index=inputline.indexOf(chemical.close_name_of_substance);
                    temp+="Substance Name :"+inputline.substring(start_index, stop_index)+">";
                }
                else if(inputline.contains(chemical.open_reg_num))
                {
                    int start_index=inputline.indexOf(chemical.open_reg_num)+chemical.open_reg_num.length();
                    int stop_index=inputline.indexOf(chemical.close_reg_num);
                    temp+="<Registry Number : "+inputline.substring(start_index, stop_index)+" , ";
                }
                inputline=in.readLine();
            }
            return temp;
        }
        catch (Exception e)
        {
            System.err.println("Package : XML extractor\t Class : chemical_extractor\t Function : indv_chemical_extractor()"+e);
        }
        return "";
    }
}
