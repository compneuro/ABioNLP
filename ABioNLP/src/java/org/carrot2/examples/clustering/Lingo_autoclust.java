/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
This part of the code uses orginal Carrot2 APIs which can be downloaded from http://download.carrot2.org/stable/javadoc/org/carrot2/core/Cluster.htm
 */
package org.carrot2.examples.clustering;

import com.google.common.collect.Maps;
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
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.StringTokenizer;




import org.carrot2.clustering.lingo.LingoClusteringAlgorithm;
import org.carrot2.clustering.lingo.LingoClusteringAlgorithmDescriptor;

import org.carrot2.core.Cluster;
import org.carrot2.core.Controller;
import org.carrot2.core.ControllerFactory;
import org.carrot2.core.Document;
import org.carrot2.core.IDocumentSource;

import org.carrot2.core.ProcessingResult;
import org.carrot2.examples.ConsoleFormatter;
import static org.carrot2.examples.clustering.double_auto_lingo.calcscore;
import org.carrot2.matrix.factorization.IterationNumberGuesser;
import org.carrot2.matrix.factorization.KMeansMatrixFactorizationFactory;
import org.carrot2.matrix.factorization.LocalNonnegativeMatrixFactorizationFactory;
import org.carrot2.matrix.factorization.NonnegativeMatrixFactorizationEDFactory;
import org.carrot2.matrix.factorization.NonnegativeMatrixFactorizationKLFactory;
import org.carrot2.matrix.factorization.PartialSingularValueDecompositionFactory;
import org.carrot2.text.vsm.LinearTfIdfTermWeighting;
import org.carrot2.text.vsm.LogTfIdfTermWeighting;
import org.carrot2.text.vsm.TfTermWeighting;
import web.neoGraph;
//import web.Clus;
/**
 *
 * @author ROHIT
 */

public class Lingo_autoclust {

    String qry="";
    database connection;
    auto_table_names table_names;
    String user_name;
    public Lingo_autoclust(String str,database con,String uname)
    {
        qry=str;
        connection=con;
        table_names= new auto_table_names(str,connection,uname,false);
        user_name= uname;
    }
    public Lingo_autoclust(auto_table_names tbl_nm,database con,String uname)
    {
        qry=tbl_nm.query.replace("_","+");
        table_names=tbl_nm;
        connection=con;
        user_name= uname;
    }
    public  int cluster(int no_of_clusters) throws URISyntaxException
    {
             int i=0;
             String paperids;
              
            try
            {
                
                ArrayList<Document> documents = new ArrayList<Document>();
                PreparedStatement ps;
                ResultSet rs;
                String pprids="";
                
                /*ps=connection.con.prepareStatement("Select is_auto_clustered from query_"+user_name+" where query_name = '"+qry+"'");
                rs=ps.executeQuery();
                while(rs.next())
                {*/
                   /* int is_auto_clustered=0;
                    is_auto_clustered=rs.getInt(1);
                    if(is_auto_clustered==1)
                    {*/
                        //table_names= new auto_table_names(qry,connection,user_name,true);
                        //Lingo_autoclust sel=new Lingo_autoclust(table_names,connection,user_name);
                       // selcluster();
                   /* }
                }*/
                
                
                ps=connection.con.prepareStatement("Select paper_ids from query_"+user_name+" where query_name = '"+qry+"'");
                rs=ps.executeQuery();
                while(rs.next())
                {
                    pprids=rs.getString(1);
                }
                rs.close();
                ps.close();
                create_document doc=new create_document(pprids,connection);
                documents=doc.getdocuments();
                
            /* Prepare Carrot2 documents */
            //final ArrayList<Document> documents = new ArrayList<Document>();
            int j;
            
            final Controller controller = ControllerFactory.createSimple();


            int freq=12;
            int tw_count=1,ff_count=1;
            cf c= new cf(connection,table_names,user_name);
            
            System.out.println("FREQ - "+freq);
            
            while(tw_count<=3){
                ff_count=1;
                while(ff_count<=5)
                {
               final Map<String, Object> fastAttributes = Maps.newHashMap();
               LingoClusteringAlgorithmDescriptor.attributeBuilder(fastAttributes)
                .documents( documents)
                .desiredClusterCountBase(no_of_clusters)
                .query(qry)
                .matrixBuilder()
                    .titleWordsBoost(3.5)
                    .maximumMatrixSize(60000)
                    .maxWordDf(0.9); 
               
               if (tw_count==1){
               
                   LingoClusteringAlgorithmDescriptor.attributeBuilder(fastAttributes).matrixBuilder().termWeighting(LinearTfIdfTermWeighting.class);
                   System.out.println("Linear TFIDF");
               
               }
               else if(tw_count ==2){
                   
                   LingoClusteringAlgorithmDescriptor.attributeBuilder(fastAttributes).matrixBuilder().termWeighting(LogTfIdfTermWeighting.class);
                   System.err.println("LOG TFIDF");
               }
               else if(tw_count ==3){
                   
                   LingoClusteringAlgorithmDescriptor.attributeBuilder(fastAttributes).matrixBuilder().termWeighting(TfTermWeighting.class);
                   System.err.println("TF TERM WEIGHTING");
               }
               
               
               
                
               if (ff_count==1){
               
                   LingoClusteringAlgorithmDescriptor.attributeBuilder(fastAttributes).matrixReducer().factorizationFactory(NonnegativeMatrixFactorizationEDFactory.class);
                   System.out.println("NonnegativeMatrixFactorizationEDFactory");
               
               }
               else if(ff_count ==2){
                   
                   LingoClusteringAlgorithmDescriptor.attributeBuilder(fastAttributes).matrixReducer().factorizationFactory(NonnegativeMatrixFactorizationKLFactory.class);
                   System.err.println("NonnegativeMatrixFactorizationKLFactory");
               }
               else if(ff_count==3){
                   
                   LingoClusteringAlgorithmDescriptor.attributeBuilder(fastAttributes).matrixReducer().factorizationFactory(LocalNonnegativeMatrixFactorizationFactory.class);
                   System.err.println("LocalNonnegativeMatrixFactorizationFactory");
               }
               else if(ff_count==4){
                   
                   LingoClusteringAlgorithmDescriptor.attributeBuilder(fastAttributes).matrixReducer().factorizationFactory(KMeansMatrixFactorizationFactory.class);
                   System.err.println("KMeansMatrixFactorizationFactory");
               }
               else if(ff_count==5){
                   
                   LingoClusteringAlgorithmDescriptor.attributeBuilder(fastAttributes).matrixReducer().factorizationFactory(PartialSingularValueDecompositionFactory.class);
                   System.err.println("PartialSingularValueDecompositionFactory");
               }
               
               
               
               
               LingoClusteringAlgorithmDescriptor.attributeBuilder(fastAttributes)
                       .clusterBuilder().clusterMergingThreshold(0.7)
                       .phraseLabelBoost(4.0);
               
            final ProcessingResult byTopicClusters = controller.process(fastAttributes,
            LingoClusteringAlgorithm.class);
            final List<Cluster> clustersByTopic = byTopicClusters.getClusters();

            
            ++c.number;
            c.phrasethresh=1;
            c.max_doc=0;
            c.displayClusters(clustersByTopic);
            ++ff_count;
                }
            ++tw_count;
            }
            

            System.err.println(qry);
               findunique(); 
               
               ps=connection.con.prepareStatement("update query_"+user_name+" set is_auto_clustered = 1 where query_name = '"+qry+"'");
               ps.executeUpdate();

            }
            catch (Exception e)
            {
                System.err.println("Auto clust ()" +e);
            }
           
       return 1; 
        
    }
    
  
    
    
    
    public int findunique() 
    {
         int x;
        PreparedStatement pst = null,pst2=null;
        ResultSet rs = null,rs2=null;
        String qry="",abs=null;
        String names[]=new String[100];
        int i=0;
        System.err.println("FIND UNIQUE");
        try
        {
            int clusterNumber = 0;
            int clus_num=1;
            pst=connection.con.prepareStatement("select distinct(clusname) from "+table_names.auto_table_name);
            //pst = con.prepareStatement("Select clusid,name,pprids from clusters");
            rs = pst.executeQuery();
            
            
            MetaMapApi api = new MetaMapApiImpl();
                List<String> theOptions = new ArrayList<String>();
                String options=" -g -i -y -z -u -d";
                
      //          theOptions.add("-g");
                theOptions.add("-i");
                theOptions.add("-y");
                theOptions.add("-z");
                theOptions.add("-u");
                theOptions.add("-d");
                if(theOptions.size()>0)
                {
                    api.setOptions(theOptions);    
                }
               
                
            String temp;
           
            while (rs.next()) {
                temp=rs.getString(1);
                if(temp.equalsIgnoreCase("Other Topics")!=true)
                {
                    int score=0;
                conceptstype conc;
                conc=calcscore(rs.getString(1),api);
                ++i;
               // score=calcscore(rs.getString(1),api);
                
                /*pst2=con.prepareStatement("select parent from auto_1 where clusname = (?)");
                pst2.setString(1,rs.getString(1));
                String clusters="";
                
                rs2=pst2.executeQuery();
                while (rs2.next()) {
                clusters+=rs2.getInt(1)+" ";
                }
                System.out.println(i+"\t"+temp+"\t"+score+"\t"+clusters+"\n");*/
                pst2=connection.con.prepareStatement("insert into "+table_names.unique_table_name+" (id, name,concepts,score) values (?,?,?,?)");
                pst2.setInt(1,i);
                pst2.setString(2,rs.getString(1));
                pst2.setString(3,conc.pref_name);
                pst2.setInt(4,conc.score);
                //pst2.setString(4,clusters);
                pst2.executeUpdate();
                //pst2.close();
                autopaper auto_paper=new autopaper(connection,table_names,user_name);
                auto_paper.get_clus_info(i,rs.getString(1));
                
                pst2=connection.con.prepareStatement("Select score, score2, size, clusters from "+table_names.unique_table_name+" where id = "+i);
                rs2 = pst2.executeQuery();
                double fin_score=0.0;
                while(rs2.next())
                {
                    int metamap_score=rs2.getInt(1);
                    double algo_score=rs2.getDouble(2);
                    int size=rs2.getInt(3);
                    String clusters=rs2.getString(4);
                    StringTokenizer tok=new StringTokenizer(clusters);
                    int no_of_parents=tok.countTokens();
                    fin_score=0.5*no_of_parents+size*0.3+-1*0.2*(metamap_score/100)+algo_score*0.01;
                    
                }
                pst2=connection.con.prepareStatement("Update "+table_names.unique_table_name+" set fin_score = (?) where id=(?)");
                pst2.setDouble(1, fin_score);
                pst2.setInt(2,i);
                pst2.executeUpdate();
                
                }
            }
            
            rs.close();
            pst.close();
            
        }
        catch(Exception e)
        {
            System.err.println("lingo_autoclust.JAVA\t findunique() \t"+e);
        }
        
        //calcscore();
        selcluster();
        return 1;
    }
    
    public static conceptstype calcscore(String clusname, MetaMapApi api) 
    {
                     int x;
        String qry="",abs=null;
        String pref_name="";
        String names[]=new String[100];
        conceptstype conc= new conceptstype();
        int i=0;
        //System.err.println("CALCSCORE");
        try
        {
        
    
            int clusterNumber = 1;
            int clus_num=1;
            
            int ppr_counter;
            
           
            
                    List<Result> resultList = api.processCitationsFromString(clusname);
                    Result result = resultList.get(0);
                    int no_of_maps=0;
                                            //namelist temp=null;
                    int a[]=new int [4];
                    
                    int j;
                    i=0;
                    int score=0;
                    namelist temp=new namelist();                
                    for (Utterance utterance: result.getUtteranceList()) {
    
                        int counter=0;
                        score=0;
                        for (PCM pcm: utterance.getPCMList()) {
                            int count=0, flag=0;
    
                            namelist head[]=new namelist[30];
                                
                            for (Mapping map: pcm.getMappingList()) {
                            //System.out.println("mmPhrase:");
            //System.out.println(" mmMap Score: " + map.getScore());
            
            
                            if(flag==0)
                            {
                                    score+=map.getScore();
                                    System.out.println("inside score"+score);
                                    ++counter;
                                    flag++;
                            }
                            System.out.println("outside score"+map.getScore());
                                i=0;
            
                            for (Ev mapEv: map.getEvList()) {
                                namelist entity=new namelist();
                                entity.name=mapEv.getConceptName();
                                entity.score=mapEv.getScore();
                                entity.type=mapEv.getSemanticTypes();
                                entity.pref_name=mapEv.getPreferredName();
                                pref_name+=entity.pref_name+" | ";
                                /*System.out.println("2.Concept Name"+entity.name);
                                System.out.println("2.Score"+entity.score);
                                System.out.println("2.Type"+entity.type);
                                System.out.println("2.Preffered name"+entity.pref_name);*/
            
                                if (i==0)
                                {
                                    head[count]=entity;
                                    temp=head[count];
                                    //System.out.println("inside");
                                    ++i;
                                }
                                else
                                {
                                    temp.next=entity;
                                    entity.prev=temp;
                                    temp=entity;
                                    ++i;
                                }
                                 System.out.println(" geScore: " + mapEv.getScore());  
                                /*System.out.println(" geSemantic Types: " + mapEv.getSemanticTypes());
                                System.out.println(" geConcept Name: " + mapEv.getConceptName());
                                System.out.println(" gePreferred Name: " + mapEv.getPreferredName());*/
                       }
                        ++count;    
                }
            
                int c;
               for(j=0;j<count;++j)
                {
                    temp=head[j];
                    c=0;
                    while(temp!=null)
                    {
                      /*  System.out.println("1.Concept Name - "+temp.name+"\n");
                        System.out.println("1.Type - "+temp.type+"\n");
                        System.out.println("1.Score - "+temp.score+"\n");
                        System.out.println("1.Preffered name - "+temp.pref_name+"\n");*/
                        
        
                        temp=temp.next;
       
       
                    }
                   // System.out.println("!!!!!!!!!!!");
    
                                    // System.out.println("\n\n\n\n\n\n\n\n");
             }

        }
            int size=0;
            
            
      
            
        if(counter>0)
        {
             score=score/counter;
        }
        System.out.println("final - "+score);
      //  score=(int) (score*(size*0.1));
    
        conc.pref_name=pref_name;
        conc.score=score;
      return conc;
    }   
      
                    
       
            }
       
        catch(Exception e)
        {
         System.err.println("cluster examples\tLingo_autoclust\tcalcscore()"+e);   
        // pout.write(e.getMessage()+"\n\n"+e.getLocalizedMessage()+"\n\n");
        }
       
       // selcluster();
        
        //return 1;
        return conc;
    }
    
    
    public int selcluster() 
    {
        int no_of_papers=0;
        PreparedStatement pst = null,pst2=null;
        ResultSet rs = null,rs2=null;
        System.out.println("SELCLUSTER");
        try{
            int clusterNumber = 1;
            int clus_num=0;
            //con2=DriverManager.getConnection(url, user, password);
            String query_name=table_names.query;
            query_name=query_name.replace("_", "+");
/*            pst=connection.con.prepareStatement("select no_of_retrieved_results from query_"+user_name+" where query_name = '"+query_name+"'");
           
            rs = pst.executeQuery();
            //String name;
            
            while (rs.next()) {
                no_of_papers=rs.getInt(1);
            }
            
  */          
            clusters start=new clusters();
            clusters end;
            String clusters[]=new String[20];
            
            /*pst=connection.con.prepareStatement("select id,name,size,score,score2,pprids from "+table_names.unique_table_name+" order by size desc limit 0,5");
            rs = pst.executeQuery();*/
            String name;
            int id,count=0,flag=0;
            clusters temp;
            temp=start;
            String id_not_eq=" ";
/*            while (rs.next()) {
                if (flag==0)
                {
                    temp.original_id=rs.getInt(1);
                    temp.clustername=rs.getString(2);
                    temp.size=rs.getInt(3);
                    temp.score=-1*rs.getInt(4);
                    temp.algo_score=rs.getDouble(5);
                    temp.pprids=rs.getString(6);
                    flag=1;
                }
                else
                {
                    clusters node=new clusters();
                    temp.next=node;
                    temp=node;
                    temp.original_id=rs.getInt(1);
                    temp.clustername=rs.getString(2);
                    temp.size=rs.getInt(3);
                    temp.score=-1*rs.getInt(4);
                    temp.algo_score=rs.getDouble(5);
                    temp.pprids=rs.getString(6);
                }
                id_not_eq+="and id!="+temp.original_id+" ";
            }
            temp.next=null;
            */
            /*pst=connection.con.prepareStatement("select id,name,size,score,score2,pprids from "+table_names.unique_table_name+" where score = -1000"+id_not_eq+"order by size desc limit 0, 10");
            rs = pst.executeQuery();
            while (rs.next()) {
                    clusters node=new clusters();
                    temp.next=node;
                    temp=node;
                    temp.original_id=rs.getInt(1);
                    temp.clustername=rs.getString(2);
                    temp.size=rs.getInt(3);
                    temp.score=-1*rs.getInt(4);
                    temp.algo_score=rs.getDouble(5);
                    temp.pprids=rs.getString(6);
                    id_not_eq+="and id!="+temp.original_id+" ";
            }
            
            pst=connection.con.prepareStatement("select id,name,size,score,score2,pprids from "+table_names.unique_table_name+" where score = -1000"+id_not_eq+" order by score2 desc limit 0, 5");
            rs = pst.executeQuery();
            while (rs.next()) {
                    clusters node=new clusters();
                    temp.next=node;
                    temp=node;
                    temp.original_id=rs.getInt(1);
                    temp.clustername=rs.getString(2);
                    temp.size=rs.getInt(3);
                    temp.score=-1*rs.getInt(4);
                    temp.algo_score=rs.getDouble(5);
                    temp.pprids=rs.getString(6);
                    id_not_eq+="and id!="+temp.original_id+" ";
            }*/
            
            pst=connection.con.prepareStatement("select id,name,size,score,score2,pprids from "+table_names.unique_table_name+" order by fin_score desc limit 0, 30 ");
            rs=pst.executeQuery();
            while (rs.next()) {
                if (flag==0)
                {
                    temp.original_id=rs.getInt(1);
                    System.out.println("***"+temp.original_id);
                    temp.clustername=rs.getString(2);
                    System.out.println("!!!"+temp.clustername);
                    temp.size=rs.getInt(3);
                    System.out.println("###"+temp.size);
                    temp.score=-1*rs.getInt(4);
                    System.out.println("$$$"+temp.score);
                    temp.algo_score=rs.getDouble(5);
                    System.out.println("%%%"+temp.algo_score);
                    temp.pprids=rs.getString(6);
                    System.out.println("^^^"+temp.pprids);
                    flag=1;
                }
                else
                {
                    clusters node=new clusters();
                    temp.next=node;
                    temp=node;
                    temp.original_id=rs.getInt(1);
                    temp.clustername=rs.getString(2);
                    temp.size=rs.getInt(3);
                    temp.score=-1*rs.getInt(4);
                    temp.algo_score=rs.getDouble(5);
                    temp.pprids=rs.getString(6);
                }
            }
            temp.next=null;
            end=temp;
            temp=start;
            String total="";
            int n=0;
            while (temp!=null) {
                    ++n;
                    pst=connection.con.prepareStatement("insert into clusters_"+user_name+" (clusid,name,size,score,parent,pprids) values (?,?,?,?,?,?)");
                    pst.setInt(1,n);
                    pst.setString(2,temp.clustername);
                    pst.setInt(3,temp.size);
                    pst.setDouble(4,temp.score);
                    pst.setInt(5,0);
                    pst.setString(6,temp.pprids);
                    pst.executeUpdate();
                    
                    //rs = pst.executeQuery();
                    
                    System.out.println(temp.clustername);
                    StringTokenizer tok=new StringTokenizer(temp.pprids);
                    
                    while(tok.hasMoreTokens())
                    {
                         
                         String temp_str=tok.nextToken();
                         if(!total.contains(" "+temp_str+" "))
                         {
                             total+=" "+temp_str+" ";
                            // System.err.println("inside");
                         }
                     }
                     temp=temp.next;
                }
            
            StringTokenizer tok=new StringTokenizer(total);
            System.out.println(tok.countTokens());
    //        double ratio=100*tok.countTokens()/no_of_papers;
      //      System.out.println(ratio);
            pst.close();
        }
        catch (Exception e)
        {
            System.out.println("Selcluster()"+e);
        }
        return 1;
    }
    
    
    
    
    public static void main(String [] args) throws URISyntaxException
    {
        database con= new database ();
        Lingo_autoclust c=new Lingo_autoclust("fragile+x+syndrome",con,"");
        c.cluster(10);
       // c.selcluster();
        
    }
}
