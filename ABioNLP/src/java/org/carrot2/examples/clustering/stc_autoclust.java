/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
This part of the code uses orginal Carrot2 APIs which can be downloaded from http://download.carrot2.org/stable/javadoc/org/carrot2/core/Cluster.htm
 */
package org.carrot2.examples.clustering;

import com.google.common.collect.Maps;
import database_manipulator.database;
import gov.nih.nlm.nls.metamap.MetaMapApi;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;
import gov.nih.nlm.nls.metamap.Result;
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
import org.carrot2.clustering.stc.STCClusteringAlgorithm;
import org.carrot2.clustering.stc.STCClusteringAlgorithmDescriptor;

import org.carrot2.core.Cluster;
import org.carrot2.core.Controller;
import org.carrot2.core.ControllerFactory;
import org.carrot2.core.Document;
import org.carrot2.core.IDocumentSource;

import org.carrot2.core.ProcessingResult;
import org.carrot2.examples.ConsoleFormatter;
import org.carrot2.matrix.factorization.IterationNumberGuesser;
//import web.Clus;
/**
 *
 * @author ROHIT
 */

    



/*
 * Carrot2 project.
 *
 * Copyright (C) 2002-2012, Dawid Weiss, Stanisław Osiński.
 * All rights reserved.
 *
 * Refer to the full license file "carrot2.LICENSE"
 * in the root folder of the repository checkout or at:
 * http://www.carrot2.org/carrot2.LICENSE
 */





/**
 * This example shows how to cluster a set of documents available as an {@link ArrayList}.
 * This setting is particularly useful for quick experiments with custom data for which
 * there is no corresponding {@link IDocumentSource} implementation. For production use,
 * it's better to implement a {@link IDocumentSource} for the custom document source, so
 * that e.g., the {@link Controller} can cache its results, if needed.
 * 
 * @see ClusteringDataFromDocumentSources
 * @see UsingCachingController
 */
public class stc_autoclust {

    String qry="";
    database connection;
    auto_table_names table_names;
    String user_name;
    public stc_autoclust(String str,database con,String uname)
    {
        qry=str;
        connection=con;
        table_names= new auto_table_names(qry,connection,uname,false);
        user_name=uname;
    }
    public  int cluster(int no_of_clust) throws URISyntaxException
    {
        
       
            try
            {
            
                String inputline,qry1;
//                File file = new File(file_nm);
  //              FileReader fr=new FileReader(file);
    //            BufferedReader in = new BufferedReader(fr);
                
                
               ArrayList<Document> documents = new ArrayList<Document>();
                PreparedStatement ps;
                ResultSet rs;
            
                ps=connection.con.prepareStatement("Select is_auto_clustered from query_"+user_name+" where query_name = '"+qry+"'");
                rs=ps.executeQuery();
                while(rs.next())
                {
                    int is_auto_clustered=0;
                    is_auto_clustered=rs.getInt(1);
                    if(is_auto_clustered==1)
                    {
                        table_names= new auto_table_names(qry,connection,user_name,true);
                        Lingo_autoclust sel=new Lingo_autoclust(table_names,connection,user_name);
                        sel.selcluster();
                        return 1;
                    }
                }
                
                table_names= new auto_table_names(qry,connection,user_name,false);
                String pprids="";
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
            
            /* A controller to manage the processing pipeline. */
            final Controller controller = ControllerFactory.createSimple();

            /*
             * Perform clustering by topic using the Lingo algorithm. Lingo can 
             * take advantage of the original query, so we provide it along with the documents.
             */
            Double bas_clus_mer_thres=0.4;
            
          //  int freq=12;
            int wrd_doc_freq_thres=5;
            cf c= new cf(connection,table_names,user_name);
            //System.out.println("FREQ - "+freq);
            int count=1;
            while(bas_clus_mer_thres<=0.7)
            {
                wrd_doc_freq_thres=5;
                while(wrd_doc_freq_thres<=20)
                {
                    System.err.println(count+"\t"+bas_clus_mer_thres+"\t"+wrd_doc_freq_thres);
                    ++count;
                            System.out.println("Inside the loop");
                            final Map<String, Object> fastAttributes = Maps.newHashMap();
           
            //obj.desiredClusterCountBase=6;
            //LingoClusteringAlgorithmDescriptor.AttributeBuilder c =new LingoClusteringAlgorithmDescriptor.AttributeBuilder();
               
               
                            
               STCClusteringAlgorithmDescriptor.attributeBuilder(fastAttributes)
                .documents(documents)
                .maxBaseClusters(300).minBaseClusterScore(5.0).minBaseClusterSize(5).maxPhrases(3).maxDescPhraseLength(3).maxClusters(no_of_clust)
                .mostGeneralPhraseCoverage(0.5).mergeThreshold(bas_clus_mer_thres);
               
               STCClusteringAlgorithmDescriptor.attributeBuilder(fastAttributes).preprocessingPipeline().caseNormalizer().dfThreshold(wrd_doc_freq_thres);
              
                      
            final ProcessingResult byTopicClusters = controller.process(fastAttributes,
                STCClusteringAlgorithm.class);//LingoClusteringAlgorithm.class);
            final List<Cluster> clustersByTopic = byTopicClusters.getClusters();
                    ++c.number;
                    c.baseclus=bas_clus_mer_thres;
                    c.wrddoc=wrd_doc_freq_thres;
                    c.displayClusters(clustersByTopic);
                        
                        wrd_doc_freq_thres+=5;
                }
                
                bas_clus_mer_thres+=0.05;
            }
            
            Lingo_autoclust unique=new Lingo_autoclust(table_names,connection,user_name);
            unique.findunique();
            ps=connection.con.prepareStatement("update query_"+user_name+" set is_auto_clustered = 1 where query_name = '"+qry+"'");
            ps.executeUpdate();
            }
            catch(Exception e)
            {
                System.out.println("STC_AUTO_CLUST\tcluster()");
            }

       return (1); 
        
    }
    
    public static void main(String [] args) throws URISyntaxException
    {
        database con= new database();
        stc_autoclust c=new stc_autoclust("cell+death",con,"");
      c.cluster(10);
     
        
    }
}

