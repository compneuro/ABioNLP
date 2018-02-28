/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carrot2.examples.clustering;

/**
 *
 * @author rohit
 */
public class clusters {
    String clustername;
    String pprids;
    String cluster_ids;
    int size;
    int score;
    double algo_score;
    int original_id;
    clusters next;
    public clusters()
    {
        clustername="";
        pprids="";
        cluster_ids="";
        size=0;
        score=0;
        original_id=0;
        next=null;
    }
    
}
