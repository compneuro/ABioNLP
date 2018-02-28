/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carrot2.examples.clustering;

/**
 *
 * @author rohit
 */
public class simmatrix {
    public boolean complete_title;
    public boolean complete_concept;
    public int concpets_match;
    public int final_score;
    public String name1;
    public String name2;
    public int score;
    public String conc;
    public simmatrix()
    {
        complete_title=false;
        complete_concept=false;
        concpets_match=0;
        final_score=0;
        name1="";
        name2="";
        score=0;
    }
}
