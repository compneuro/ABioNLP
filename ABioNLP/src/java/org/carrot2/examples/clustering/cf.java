/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
This part of the code uses orginal Carrot2 APIs which can be downloaded from http://download.carrot2.org/stable/javadoc/org/carrot2/core/Cluster.htm
 */
package org.carrot2.examples.clustering;


import database_manipulator.database;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import gov.nih.nlm.nls.metamap.Ev;
import gov.nih.nlm.nls.metamap.Mapping;
import gov.nih.nlm.nls.metamap.MetaMapApi;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;
import gov.nih.nlm.nls.metamap.Negation;
import gov.nih.nlm.nls.metamap.PCM;
import gov.nih.nlm.nls.metamap.Result;
import gov.nih.nlm.nls.metamap.Utterance;
import java.io.File;
import java.io.FileReader;
import java.util.List;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Map;

import web.neoGraph;
import org.carrot2.core.Cluster;
import org.carrot2.core.Document;
import org.carrot2.core.ProcessingResult;
import org.carrot2.core.attribute.CommonAttributesDescriptor;
//import static web.neoGraph.createNode;

/**
 * Simple console formatter for dumping {@link ProcessingResult}.
 */
public class cf 
{
    static neoGraph n;
    static String clusname="";
    static String pprids="";
    static int parentid, graphparent,clusid;
    static String QRY="";
    static int siz=0,count=0,scr=0;
    public static int number;
    public static double phrasethresh;
    public static double max_doc;
    public static double baseclus;
    public static int wrddoc;
    public int no;
    String user_name;
    auto_table_names table_names;
    database connection;
     public static void setval(int num)
    {
        count=num;
    }
       public cf(database con,auto_table_names tbl_name,String uname)
            {
        table_names=tbl_name;
        number=0;
        user_name=uname;
        PreparedStatement pst = null,pst2=null;
        ResultSet rs = null,rs2=null;
        String abs=null; 
        QRY=table_names.query;
        try {
            connection=con;
            clusid=0;
            //no=n;
            }
            catch(Exception e)
            {
             System.err.println("CF.JAVA\t cf() \t"+e);
            }
            
                 
            }
    public void displayResults(ProcessingResult processingResult) throws URISyntaxException
    {
        final Collection<Document> documents = processingResult.getDocuments();
        final Collection<Cluster> clusters = processingResult.getClusters();
        final Map<String, Object> attributes = processingResult.getAttributes();
        

        // Show documents
        if (documents != null)
        {
            displayDocuments(documents);
        }

        // Show clusters
        if (clusters != null)
        {
            displayClusters(clusters);
        }

        // Show attributes other attributes
        displayAttributes(attributes);
    }
    
  //static URI firstNode = n.createNode();
    
    static int flag=0;
   
    public static void displayDocuments(final Collection<Document> documents)
    {
        System.out.println("Collected " + documents.size() + " documents\n");
        for (final Document document : documents)
        {
            displayDocument(0, document);
        }
    }

    public static void displayAttributes(final Map<String, Object> attributes)
    {
        System.out.println("Attributes:");

        String DOCUMENTS_ATTRIBUTE = CommonAttributesDescriptor.Keys.DOCUMENTS;
        String CLUSTERS_ATTRIBUTE = CommonAttributesDescriptor.Keys.CLUSTERS;
        for (final Map.Entry<String, Object> attribute : attributes.entrySet())
        {
            if (!DOCUMENTS_ATTRIBUTE.equals(attribute.getKey())
                && !CLUSTERS_ATTRIBUTE.equals(attribute.getKey()))
            {
                System.out.println(attribute.getKey() + ":   " + attribute.getValue());
            }
        }
    }

    public void displayClusters(final Collection<Cluster> clusters) throws URISyntaxException
    {
        displayClusters(clusters, Integer.MAX_VALUE);
    }

    public  void displayClusters(final Collection<Cluster> clusters,
        int maxNumberOfDocumentsToShow) throws URISyntaxException
    {
        displayClusters(clusters, maxNumberOfDocumentsToShow,
            ClusterDetailsFormatter.INSTANCE);
    }

    public void displayClusters(final Collection<Cluster> clusters,
        int maxNumberOfDocumentsToShow, ClusterDetailsFormatter clusterDetailsFormatter) throws URISyntaxException
    {
        int x;
        PreparedStatement pst = null,pst2=null;
        ResultSet rs = null,rs2=null;
        String qry="",abs=null;

        try
        {
            int clusterNumber = 1;
            int clus_num=1;
           
              
              if(number!=1)
              {
                 // pout.write("\n\n\n\n\n\n");
              }
            //  pout.write("Cluster Number - "+number+" - "+ "basclusmrg="+baseclus+"  wrddocfreq "+ wrddoc+" \n\n");
              
              System.out.println("Cluster Number - "+number);
       
        for (final Cluster cluster : clusters)
        {
           pprids=" "; 
           ++clusid;
           clusname="";
           int r=0;
           clusname=cluster.getLabel();
           
               System.out.println(clusname);//System.out.println("ERRRRRORRRRR ERRRRORRRRR!!!!!!!!!");
           
           
            displayCluster(0, "" + clusterNumber++, cluster, maxNumberOfDocumentsToShow,
                clusterDetailsFormatter);
            System.out.println("Papers!! - "+pprids);
            clusname=clusname.replace("'","");
            pst = connection.con.prepareStatement("INSERT INTO "+table_names.auto_table_name+ " (clusname,score,size,parent,pprids,freq,thresh) VALUES(?,?,?,?,?,?,?)");
                
                pst.setString(1,clusname);
                pst.setDouble(2, cluster.getScore());
                pst.setInt(3, cluster.size());
                ///int numb=th
                pst.setInt(4, number);
                pst.setString(5, pprids);
                pst.setDouble(6,max_doc);
                pst.setDouble(7,phrasethresh);
                x=clusid;
                pst.executeUpdate();
            
                pst.close();
        }
        
        }
        catch(Exception e)
        {
            System.err.println("CF.JAVA\t displayclusters() \t"+e);
        }
       
        
    }

    private static void displayDocument(final int level, Document document)
    {
        
        final String indent = getIndent(level);
        String qry;
        int n=0;
        PreparedStatement pst= null;
            
      //  System.out.println("\nId="+document.getField(Document.CONTENT_URL));
        
        int x;
        String s=document.getField(Document.CONTENT_URL);
        x=Integer.parseInt(s);
        pprids+=x+" ";
        
        
    }
   

    private void displayCluster(final int level, String tag, Cluster cluster,
        int maxNumberOfDocumentsToShow, ClusterDetailsFormatter clusterDetailsFormatter) throws URISyntaxException
    {
        //final String label = clusname;
            
       

        // indent up to level and display this cluster's description phrase
        for (int i = 0; i < level; i++)
        {
            System.out.print("  ");
        }
      //  System.out.println(clusname + "gfdfdhfddhfdfgd"+ clusterDetailsFormatter.formatClusterDetails(cluster));

        // if this cluster has documents, display three topmost documents.
        int documentsShown = 0;
        for (final Document document : cluster.getDocuments())
        {
            if (documentsShown >= maxNumberOfDocumentsToShow)
            {
                break;
            }
            displayDocument(level + 1, document);
            documentsShown++;
        }
        if (maxNumberOfDocumentsToShow > 0
            && (cluster.getDocuments().size() > documentsShown))
        {
            System.out.println(getIndent(level + 1) + "... and "
                + (cluster.getDocuments().size() - documentsShown) + " more\n");
        }

        // finally, if this cluster has subclusters, descend into recursion.
        final int num = 1;
        for (final Cluster subcluster : cluster.getSubclusters())
        {
            displayCluster(level + 1, tag + "." + num, subcluster,
                maxNumberOfDocumentsToShow, clusterDetailsFormatter);
        }
    }

    private static String getIndent(final int level)
    {
        final StringBuilder indent = new StringBuilder();
        for (int i = 0; i < level; i++)
        {
            indent.append("  ");
        }

        return indent.toString();
    }

    public static class ClusterDetailsFormatter
    {
        public final static ClusterDetailsFormatter INSTANCE = new ClusterDetailsFormatter();

        protected NumberFormat numberFormat;

        public ClusterDetailsFormatter()
        {
            numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);
        }

        public String formatClusterDetails(Cluster cluster)
        {
            final Double score = cluster.getScore();
            return "(" + cluster.getAllDocuments().size() + " docs"
                + (score != null ? ", score: " + numberFormat.format(score) : "") + ")";
        }
    }
}