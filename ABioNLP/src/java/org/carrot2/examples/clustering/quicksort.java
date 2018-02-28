/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
This part of the code uses orginal Carrot2 APIs which can be downloaded from http://download.carrot2.org/stable/javadoc/org/carrot2/core/Cluster.htm
 */
package org.carrot2.examples.clustering;

import java.util.ArrayList;
/**
 *
 * @author rohit
 */
public class quicksort {
    public int Partition(ArrayList<auto_table_names> collection, int left_index, int right_index)
    {
        //try{
        int size= collection.size();
        auto_table_names temp=collection.get(left_index);
        String pivot=temp.query;
        boolean lflag=true,rflag=true;
        while (true)
        {
            while(collection.get(left_index).query.compareTo(pivot)<0)
            {
                ++left_index;
            }
            while(collection.get(right_index).query.compareTo(pivot)>0)
            {
                --right_index;
            }
            if (left_index < right_index)
            {
                auto_table_names temp_right=collection.get(right_index);
                collection.remove(right_index);
                collection.add(right_index, collection.get(left_index));
                collection.remove(left_index);
                collection.add(left_index, temp_right);
            }
            else
            {
                return right_index;
            }
       }
        }
        
    
    
    
 
      public void QuickSort_Recursive(ArrayList<auto_table_names> collection, int left_index, int right_index)
    {
        // For Recursion
        if(left_index < right_index)
        {
            int pivot = Partition(collection, left_index, right_index);
 
            if(pivot > 1)
                QuickSort_Recursive(collection, left_index, pivot - 1);
 
            if(pivot + 1 < right_index)
                QuickSort_Recursive(collection, pivot + 1, right_index);
        }
    }
 
    
     public static void main(String [] args) {
          quicksort qk= new quicksort();
          ArrayList<String> str= new ArrayList<String>();
         str.add("granule+neuron");
         str.add("cell+death");
         str.add("fragile+x+syndrome");
         str.add("ataxia");
         /*str.add("FACK5");
         str.add("FACK9");
         str.add("FACK0");
         str.add("FACK2");*/
         //auto_table_names
         ArrayList<auto_table_names> a1 = new ArrayList<auto_table_names>();
         int i;
         //qk.QuickSort_Recursive(str,0,(str.size()-1));
         for(i=0;i<str.size();++i)
         {
             System.out.println(str.get(i));
         }   
     }
 }
