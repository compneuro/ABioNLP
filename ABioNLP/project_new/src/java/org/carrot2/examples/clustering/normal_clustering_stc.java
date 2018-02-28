/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carrot2.examples.clustering;

import com.google.common.collect.Maps;
import database_manipulator.database;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.carrot2.clustering.stc.STCClusteringAlgorithm;
import org.carrot2.clustering.stc.STCClusteringAlgorithmDescriptor;
import org.carrot2.core.Cluster;
import org.carrot2.core.Controller;
import org.carrot2.core.ControllerFactory;
import org.carrot2.core.Document;
import org.carrot2.core.ProcessingResult;
import org.carrot2.examples.ConsoleFormatter;


/**
 *
 * @author rohit
 */
public class normal_clustering_stc {
    

    String qry="";
    database connection;
    String user_name;
    public normal_clustering_stc(String str,database con,String uname)
    {
        qry=str;
        connection=con;
        user_name=uname;
    }
    public  int cluster(int maxbase,int maxcl,double minbclusscore,int minbclussize,double singtermboost,double phroverlap,double mrg,int worddocthresh) throws URISyntaxException
    {
        
             int i=0;
             ArrayList<Document> documents = new ArrayList<Document>();
            try
            {
                String inputline,pprids="";
                System.out.println("query term is!!!!!!!!!!!!!!!"+qry);
                //qry="select paper_ids from query_1 where query_name = '"+qry+"'";//where query= '" + qry +"'";    //the abstract of the papers to be clustered is retrieved
                PreparedStatement ps= connection.con.prepareStatement("select paper_ids from query_"+user_name+" where query_name = '"+qry+"'");
                ResultSet rs= ps.executeQuery(); ;   
                while(rs.next())
                {
                        pprids=rs.getString(1);
                }
                create_document create_doc = new create_document(pprids,connection);
                documents= create_doc.getdocuments();
            int j;
            
            /* A controller to manage the processing pipeline. */
            final Controller controller = ControllerFactory.createSimple();
           STCClusteringAlgorithm obj=new STCClusteringAlgorithm();
               final Map<String, Object> fastAttributes = Maps.newHashMap();
               STCClusteringAlgorithmDescriptor.attributeBuilder(fastAttributes)
                .documents(documents).query(qry)
                .maxBaseClusters(maxbase).minBaseClusterScore(minbclusscore).maxClusters(maxcl).mergeThreshold(mrg).minBaseClusterSize(minbclussize).maxPhraseOverlap(phroverlap).singleTermBoost(singtermboost).preprocessingPipeline().caseNormalizer().dfThreshold(worddocthresh);

               final ProcessingResult byTopicClusters = controller.process(fastAttributes,
                STCClusteringAlgorithm.class);//LingoClusteringAlgorithm.class);
            final List<Cluster> clustersByTopic = byTopicClusters.getClusters();
            ConsoleFormatter c= new ConsoleFormatter(0,0,qry,connection,user_name);
            c.displayClusters(clustersByTopic);
            }
            catch(Exception e)
            {
                System.out.println("normal_clustering_stc  funct - cluster()"+e);
            }

       return (1); 
        
    }
    
    
public int re_cluster(String s,int n) throws URISyntaxException
    {
        int i=0;
        ArrayList<Document> documents = new ArrayList<Document>();
        PreparedStatement pst;
        ResultSet rs;
        String qry="",abs=null,pprids="";
        int l=0,clusid=0;
        try {
            pst=connection.con.prepareStatement("select clusid, pprids from clusters_"+user_name+" where name = '"+s+"'");
            rs = pst.executeQuery();
            while (rs.next()) {
                clusid=rs.getInt(1);
                pprids=rs.getString(2);
            }
            pst.close();
            rs.close();
            create_document create_doc = new create_document(pprids,connection);
            documents=create_doc.get_doc_recluster();
    
   
            int j;
            
            final Controller controller = ControllerFactory.createSimple();
            final Map<String, Object> fastAttributes = Maps.newHashMap();
               STCClusteringAlgorithmDescriptor.attributeBuilder(fastAttributes)
                .documents(documents).query(qry)
                .maxBaseClusters(300).minBaseClusterScore(5.0).maxClusters(6).mergeThreshold(0.5).minBaseClusterSize(2).maxPhraseOverlap(0.6).singleTermBoost(0.5).preprocessingPipeline().caseNormalizer().dfThreshold(2);
         
              
            final ProcessingResult byTopicClusters = controller.process(fastAttributes,
            STCClusteringAlgorithm.class);//LingoClusteringAlgorithm.class);
            final List<Cluster> clustersByTopic = byTopicClusters.getClusters();
            ConsoleFormatter cf= new ConsoleFormatter(clusid,n,s,connection,user_name);
            cf.displayClusters(clustersByTopic);
            
        }
        catch(Exception e)
        {
            System.err.println(e);
        }

       return (1); 
        
    }
    
         
    public static void main(String [] args) throws URISyntaxException
    {
      database con=new database();
      normal_clustering_stc c=new normal_clustering_stc("granule+neuron", con,"");
     // c.cluster(300,5,5.0,2,0.5,0.6,0.5,2);
      c.re_cluster("Neurons, Granule Cells, Hippocampal",1);
    }
}

