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
import org.carrot2.clustering.lingo.LingoClusteringAlgorithm;
import org.carrot2.clustering.lingo.LingoClusteringAlgorithmDescriptor;
import org.carrot2.core.Cluster;
import org.carrot2.core.Controller;
import org.carrot2.core.ControllerFactory;
import org.carrot2.core.Document;
import org.carrot2.core.ProcessingResult;
import org.carrot2.examples.ConsoleFormatter;
import org.carrot2.matrix.factorization.IterationNumberGuesser;
import org.carrot2.matrix.factorization.NonnegativeMatrixFactorizationEDFactory;
import web.cleardb;
import web.clusdisp;

/**
 *
 * @author rohit
 */

public class normal_clustering_lingo 
{
    static String qry="";
    database connection;
    String user_name;
    public normal_clustering_lingo(String str,database con,String uname)
    {
        qry=str;
        connection= con;
        user_name=uname;
    }
    
    //function for clustering using lingo.
    
    public  int cluster(int n, double merge, double phr_boost, double title_wrd_boost, double trunc_label_thresh, double wrd_doc_freq_thresh) throws URISyntaxException
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
                Statement statement = null;
                ResultSet resultSet ;   
                PreparedStatement pst= null;
                create_document create_doc = new create_document(pprids,connection);
                documents= create_doc.getdocuments();
                int j;
            /* A controller to manage the processing pipeline. */
                final Controller controller = ControllerFactory.createSimple();
            //Clustering using Lingo
            
               final Map<String, Object> fastAttributes = Maps.newHashMap();
               LingoClusteringAlgorithmDescriptor.attributeBuilder(fastAttributes)
                .documents(documents)
                .desiredClusterCountBase(n)
                .query(qry)
                .matrixReducer().factorizationFactory(NonnegativeMatrixFactorizationEDFactory.class)//IterativeMatrixFactorizationFactory.class, KMeansMatrixFactorizationFactory.class, LocalNonnegativeMatrixFactorizationFactory.class,  PartialSingularValueDecompositionFactory.class, NonnegativeMatrixFactorizationKLFactory.class, NonnegativeMatrixFactorizationEDFactory.class)
                .factorizationQuality(IterationNumberGuesser.FactorizationQuality.LOW);
                LingoClusteringAlgorithmDescriptor.attributeBuilder(fastAttributes)
                .clusterBuilder().clusterMergingThreshold(merge)
                .phraseLabelBoost(phr_boost);
               
               LingoClusteringAlgorithmDescriptor.attributeBuilder(fastAttributes).matrixBuilder().titleWordsBoost(title_wrd_boost).maximumMatrixSize(40000).maxWordDf(wrd_doc_freq_thresh);
               final ProcessingResult byTopicClusters = controller.process(fastAttributes,
               LingoClusteringAlgorithm.class);
               final List<Cluster> clustersByTopic = byTopicClusters.getClusters();
               ConsoleFormatter c= new ConsoleFormatter(0,0,qry,connection,user_name);
               c.displayClusters(clustersByTopic);
            }
            catch(Exception e)
            {
                    System.err.println("Class normal_clustering_lingo func - cluster()"+e);
            }
       return (1); 
        
    }
    
    
    //Function which reclusters a node on right clicking
    
    public int re_cluster(String s,int n) throws URISyntaxException
    {
        
        int i=0;
        ArrayList<Document> documents = new ArrayList<Document>();
        PreparedStatement pst;
        ResultSet rs;
        String qry="",abs=null,pprids="";
        int l=0,clusid=0;
        try {
            System.err.println("REST1.JAVA!!!");
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
            
            /* A controller to manage the processing pipeline. */
            final Controller controller = ControllerFactory.createSimple();

            /*
             * Perform clustering by topic using the Lingo algorithm. Lingo can 
             * take advantage of the original query, so we provide it along with the documents.
             */
               final Map<String, Object> fastAttributes = Maps.newHashMap();
               LingoClusteringAlgorithmDescriptor.attributeBuilder(fastAttributes)
                .documents(documents)
                .desiredClusterCountBase(6)
                       .query(s)
                .matrixReducer().factorizationFactory(NonnegativeMatrixFactorizationEDFactory.class)//IterativeMatrixFactorizationFactory.class, KMeansMatrixFactorizationFactory.class, LocalNonnegativeMatrixFactorizationFactory.class,  PartialSingularValueDecompositionFactory.class, NonnegativeMatrixFactorizationKLFactory.class, NonnegativeMatrixFactorizationEDFactory.class)
                .factorizationQuality(IterationNumberGuesser.FactorizationQuality.LOW);
               LingoClusteringAlgorithmDescriptor.attributeBuilder(fastAttributes)
                       .clusterBuilder().clusterMergingThreshold(0.7)
                       .phraseLabelBoost(4.0);
               //title word boost, max matrix size, max worddf
               LingoClusteringAlgorithmDescriptor.attributeBuilder(fastAttributes).matrixBuilder().titleWordsBoost(2.5).maximumMatrixSize(40000).maxWordDf(0.9);
            final ProcessingResult byTopicClusters = controller.process(fastAttributes,
                LingoClusteringAlgorithm.class);//LingoClusteringAlgorithm.class);
            final List<Cluster> clustersByTopic = byTopicClusters.getClusters();
            ConsoleFormatter cf= new ConsoleFormatter(clusid,n,s,connection,user_name);
            cf.displayClusters(clustersByTopic);
            
        }
        catch(Exception e)
        {
            System.err.println("Normal_Clustering function - recluster()"+e);
        }
       return (1); 
        

    }
    
    
    public  void main(String [] args) throws URISyntaxException
    {
        database con= new database();
        normal_clustering_lingo c=new normal_clustering_lingo("granule+neuron",con,"");
        //c.cluster(6, 0.7, 4.0, 2.5, 0.5, 0.9);
        clusdisp cldisp= new clusdisp(con,user_name);
        //cldisp.clus_to_neo(qry);
        /*cleardb clr= new cleardb(con);
        clr.clear();*/
        c.re_cluster("Other Topics_0", 9);
        cldisp.reclus_neo("Other Topics_0", 42);
     
    }
}
