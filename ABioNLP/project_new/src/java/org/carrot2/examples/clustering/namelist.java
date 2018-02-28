/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carrot2.examples.clustering;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.List;

/**
 *
 * @author rohit
 */
public class namelist {
    public List<String> type;
    public String name;
    public String pref_name;
    public int score;
    public namelist prev;
    public namelist next;
    
    public namelist()
    {
        name="";
        score=0;
        prev=null;
        next=null;
    }
    
}
