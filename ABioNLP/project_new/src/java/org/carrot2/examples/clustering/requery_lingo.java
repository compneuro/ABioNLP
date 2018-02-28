/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carrot2.examples.clustering;

import database_manipulator.database;
import gov.nih.nlm.nls.metamap.Ev;
import gov.nih.nlm.nls.metamap.Mapping;
import gov.nih.nlm.nls.metamap.MetaMapApi;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;
import gov.nih.nlm.nls.metamap.PCM;
import gov.nih.nlm.nls.metamap.Result;
import gov.nih.nlm.nls.metamap.Utterance;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import static org.carrot2.examples.clustering.Lingo_autoclust.calcscore;
import static org.carrot2.examples.clustering.double_auto_lingo.calcscore;

/**
 *
 * @author rohit
 */
public class requery_lingo {
    
    database connection;
    String query1,query2;
    auto_table_names table_name1;
    auto_table_names table_name2;
    ArrayList<auto_table_names> table_names=new ArrayList<auto_table_names>();
    
    public requery_lingo(database con)
    {
        try{
        connection=con;
        
        }
        catch(Exception e)
        {
            System.out.println("Requery_lingo func requery_lingo() - "+e);
        }
    }
    
    public void requery(String qrys)
    {
        try{
        
        StringTokenizer tok = new StringTokenizer(qrys);
        int no_of_qrys= tok.countTokens();
        int i=0;
        PreparedStatement ps;
        ResultSet rs;
        while(tok.hasMoreTokens())
        {
            String tmp_str=tok.nextToken();
            auto_table_names temp= new auto_table_names(tmp_str,connection,"",false);
            table_names.add(temp);
            System.out.println("jjjjjaaaaajjjjj - "+temp.query);
            String qry_tmp=tmp_str.replace("_","+");
            ps= connection.con.prepareStatement("select is_auto_clustered from query_1 where query_name = '"+qry_tmp+"'");
            rs=ps.executeQuery();
            while(rs.next())
            {
                if(rs.getInt(1)==0)
                {
                    Lingo_autoclust auto= new Lingo_autoclust(temp,connection,"");
                    auto.cluster(10);
                }
                else
                {
                    System.out.println("Already Reclustered !! - "+temp.query);
                }
            }
        }
        }
        catch(Exception e)
        {
            System.out.println("Requery_lingo function - requery()"+e);
        }
       selcluster();
    }
    
    
    
    
    public int selcluster() 
    {
      try {
            
           PreparedStatement ps;
           ResultSet rs;
           String temp;
           String concept="";  
           quicksort qk= new quicksort();
           
           qk.QuickSort_Recursive(table_names,0, (table_names.size()-1));
           
           ArrayList<conceptstype> ct2=new ArrayList<conceptstype>();
           ArrayList<ArrayList<conceptstype>> concept_list= new ArrayList<ArrayList<conceptstype>>();
           
           int id,no_of_qry=table_names.size(),i;
           for(i=0;i<table_names.size();++i)
           {
               System.out.println("jajaja"+table_names.get(i).query);
           }
           for(i=0;i<no_of_qry;++i)
           {
               ArrayList<conceptstype> temp_concept_list=new ArrayList<conceptstype>();
               auto_table_names temp_name=  table_names.get(i);
               System.out.println(temp_name.query);
               System.out.println(temp_name.auto_table_name);
               System.out.println(temp_name.unique_table_name);
               ps=connection.con.prepareStatement("select id,name,concepts,score from "+temp_name.unique_table_name+" where score < -800");
               rs = ps.executeQuery();
               while (rs.next()) {
                    conceptstype temp_concept= new  conceptstype();
                    temp_concept.id=rs.getInt(1);
                    temp_concept.name=rs.getString(2);
                    temp_concept.concepts=rs.getString(3);
                    temp_concept.score=rs.getInt(4);
                
                    temp_concept_list.add(temp_concept);
                    System.out.println("ID - "+temp_concept.id);
                    System.out.println("name - "+temp_concept.name);
                    System.out.println("concepts - "+temp_concept.concepts);
                    System.out.println("score - "+temp_concept.score);
               }
                concept_list.add(temp_concept_list);
           }
           table_names.size();
           
           String name;
           ArrayList<String> tbl_name= new ArrayList<String>();
           int j;
           if(concept_list.size()== no_of_qry)
           {
               System.out.println("EQUAL-EQUAL-EQUAL-EQUAL");
           }
//           table_names.
           for(i=0;i<concept_list.size();++i)
           {
               auto_table_names table_name_1=table_names.get(i);
               ArrayList<conceptstype> temp_concept_list_1;
               temp_concept_list_1=concept_list.get(i);
               int l1=temp_concept_list_1.size();
               for(j=i+1;j<concept_list.size();++j)
               {
                   auto_table_names table_name_2=table_names.get(j);
                   ArrayList<conceptstype> temp_concept_list_2;
                   temp_concept_list_2=concept_list.get(j);
                   int l2=temp_concept_list_2.size();
                   
                   if(j==(i+1))
                   {
                       String tbl=table_name_1.query+"___"+table_name_2.query;
                       tbl_name.add(tbl);
                   }
                   else if(i==0&&j==(no_of_qry-1))
                   {
                       String tbl=table_name_2.query+"___"+table_name_1.query;
                       tbl_name.add(tbl);
                   }
               }
           }
           int size= tbl_name.size();
           String tmp=tbl_name.get(1);
           tbl_name.remove(1);
           tbl_name.add(tmp);
           
           for(i=0;i<tbl_name.size();++i)
           {
               System.out.println(tbl_name.get(i));
           }
           
                   
                   /*
                   int k,l;
                   simmatrix[][] matrix= new simmatrix[l1][l2];
                   for(k=0;k<l1;++k)
                   {
                       String ctone=temp_concept_list_1.get(k).concepts;
                       temp_concept_list_1.get(k).concepts=temp_concept_list_1.get(k).concepts.replace(" ","_");
                       temp_concept_list_1.get(k).concepts=temp_concept_list_1.get(k).concepts.replace("|"," ");
                       for(l=0;l<l2;++l)
                       {
                    //String ctwo=ct2[i].concepts;
                            matrix[k][l]= new simmatrix();
                    
                            if (temp_concept_list_1.get(k).name.equals(temp_concept_list_2.get(l).name))
                            {
                                    matrix[i][j].complete_title=true;
                                    matrix[i][j].final_score=3000;
                                    System.out.println("COMPLETE TITLE = "+temp_concept_list_1.get(k).name+"------"+temp_concept_list_2.get(l).name);
                            }
                            else if (ctone.equals(temp_concept_list_2.get(l).concepts))
                            {
                                    matrix[i][j].complete_concept=true;
                                    System.out.println("COMPLETE CONCEPTS = "+temp_concept_list_1.get(k).concepts+"------"+temp_concept_list_2.get(l).concepts);
                                    if(temp_concept_list_1.get(k).score==-1000&&temp_concept_list_2.get(l).score==-1000)
                                    {
                                            matrix[i][j].final_score=2000;
                                            System.out.println("COMPLETE SCORE = "+temp_concept_list_1.get(k).score+"------"+temp_concept_list_2.get(l).score);
                                    }
                                    else
                                    {
                                            matrix[i][j].final_score=1000;
                                            System.out.println("INCOMPLETE SCORE = "+temp_concept_list_1.get(k).score+"------"+temp_concept_list_2.get(l).score);
                                    }
                            }
                            else
                            {
                                    /*ct2[j].concepts=ct2[j].concepts.replace(" ","_");
                                    ct2[j].concepts=ct2[j].concepts.replace("|"," ");*/
                                
                     /*               StringTokenizer tok= new StringTokenizer(temp_concept_list_1.get(k).concepts);
                                    while(tok.hasMoreTokens())
                                    {
                                        String token;
                                        token=tok.nextToken();
                                        token=token.replace("_"," ");
                                        token=token.trim();
                                        
                                        if(temp_concept_list_2.get(l).concepts.contains(token)&&!token.equals(""))
                                        {
                                                System.out.println(token+"===="+temp_concept_list_2.get(l).concepts);
                                                matrix[i][j].name1=token;
                                                matrix[i][j].final_score+=100;
                                        }
                                    }
                            }
                     }
            temp_concept_list_1.get(k).concepts=temp_concept_list_1.get(k).concepts.replace(" ","|");
            temp_concept_list_1.get(k).concepts=temp_concept_list_1.get(k).concepts.replace("_"," ");
            }
                       
               }               
           }
           
            
            for(i=0;i<l1;++i){}
            
            
            //afds
            int len=l1;
            simmatrix[]fin= new simmatrix[len];
            int count=0;
            int max1=0,max2=0,max=0;
            for(i=0;i<l1;++i)
            {
                fin[count]=new simmatrix();
                max1=0;max2=0;max=0;
                for(j=0;j<l2;++j)
                {
                    if(matrix[i][j].final_score>max)
                    {
                        max=matrix[i][j].final_score;
                        max1=i;
                        max2=j;
                    }
                }
                
                if(matrix[max1][max2].complete_title&&max!=0)
                {
                    fin[count].name1=ct1[max1].name;
                    fin[count].name2=ct2[max2].name;
                    fin[count].score=max;
                    ++count;
                    System.out.println("NAMES - "+ct1[max1].name+" ----- "+ct2[max2].name);
                    System.out.println(ct1[max1].name+"---title---"+ct2[max2].name+"---"+matrix[max1][max2].final_score+"\n\n");
                }
                else if(matrix[max1][max2].complete_concept&&max!=0)
                {
                    fin[count].name1=ct1[max1].concepts;
                    fin[count].name2=ct2[max2].concepts;
                    fin[count].score=max;
                    ++count;
                    System.out.println("NAMES - "+ct1[max1].name+" ----- "+ct2[max2].name);
                    System.out.println(ct1[max1].concepts+"---complete---"+ct2[max2].concepts+"---"+matrix[max1][max2].final_score+"\n\n");
                }
                else if(max!=0)
                {
                    fin[count].name1=matrix[max1][max2].name1;
                    fin[count].name2=matrix[max1][max2].name1;
                    fin[count].score=max;
                    ++count;
                    System.out.println("NAMES - "+ct1[max1].name+" ----- "+ct2[max2].name);
                    System.out.println(ct1[max1].concepts+"------"+ct2[max2].concepts+"---"+matrix[max1][max2].final_score+"\n\n");
                }
            }
            System.out.println("FINALL!!");
            for(i=0;i<count;++i)
            {
                System.out.println(fin[i].name1+"-------"+fin[i].name2+"-------"+fin[i].score);
                
                ps=connection.con.prepareStatement("insert into fin (id, name1,name2,score) values(?,?,?,?)");
                ps.setInt(1, i);
                ps.setString(2, fin[i].name1);
                ps.setString(3, fin[i].name2);
                ps.setInt(4, fin[i].score);
                ps.executeUpdate();
            }*/
            
            
            
        }
        catch (Exception e)
        {
            System.out.println("Auto_lingo selcluster() - "+e);
        }
        return 1;
    }
    
    
    public static void main(String [] args) 
    {
        database con= new database();
        requery_lingo req= new requery_lingo(con);
        req.requery("granule+neuron cell+death ataxia fragile+x+syndrome");
    }
    
}
