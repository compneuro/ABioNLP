/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
This part of the code uses orginal Carrot2 APIs which can be downloaded from http://download.carrot2.org/stable/javadoc/org/carrot2/core/Cluster.htm
 */
package org.carrot2.examples.clustering;


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
import static org.carrot2.examples.clustering.cf.count;
//import static web.neoGraph.createNode;

/**
 * Simple console formatter for dumping {@link ProcessingResult}.
 */
class cf2 
{
    static neoGraph n;
   // static URI firstNode;
    static String clusname="";
    static String pprids="";
    static int parentid, graphparent,clusid;
    static String QRY="";
    static int siz=0,count=0,scr=0;
    static File file;
    static File file1;
    static File file2;
    static BufferedWriter pout;
    static BufferedWriter pout1;
    static BufferedWriter pout2;
    static Connection con;
    int number;
    public static double phrasethresh;
    public static double max_doc;
    public static double baseclus;
    public static int wrddoc;
     public static void setval(int num)
    {
        count=num;
    }
   public cf2(int n)
            {
        number=n;
        String url = "jdbc:mysql://localhost:3306/hippo";
        String user = "root";
        String password = "pass";
        String qry="",abs=null; 
        try {
            System.err.println("REST1.JAVA!!!");
            Class.forName("com.mysql.jdbc.Driver").newInstance(); 
            con = DriverManager.getConnection(url, user, password);
            //pst = con.prepareStatement("select t.title,t.abstract,c.name from test t,clusters c WHERE c.clusid=t.cluster_no");
           // pst=con.prepareStatement("select count(clusid) from clusters");
            //pst = con.prepareStatement("Select clusid,name,pprids from clusters");
            //rs = pst.executeQuery();
            /*while (rs.next()) {
                clusid=rs.getInt(1);
            }*/
            clusid=0;
            //pst.close();
            //rs.close();
            
            
            }
            catch(Exception e)
            {
             System.err.println("CF2.JAVA\t cf() \t"+e);
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

    public void displayClusters(final Collection<Cluster> clusters,
        int maxNumberOfDocumentsToShow) throws URISyntaxException
    {
        displayClusters(clusters, maxNumberOfDocumentsToShow,
            cf.ClusterDetailsFormatter.INSTANCE);
    }

    public void displayClusters(final Collection<Cluster> clusters,
        int maxNumberOfDocumentsToShow, cf.ClusterDetailsFormatter clusterDetailsFormatter) throws URISyntaxException
    {
        int x;
        PreparedStatement pst = null,pst2=null;
        ResultSet rs = null,rs2=null;
        
        try
        {
            int clusterNumber = 1;
            int clus_num=1;
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
           pst = con.prepareStatement("INSERT INTO auto_2(clusname,score,size,parent,pprids,freq,thresh) VALUES(?,?,?,?,?,?,?)");
                
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
            System.err.println(number+"\tCF2.JAVA\t displayclusters() \t"+e);
        }
       
        
    }

    private static void displayDocument(final int level, Document document)
    {
        
        final String indent = getIndent(level);
        String qry;
        int n=0;
        
        int x;
        String s=document.getField(Document.CONTENT_URL);
        x=Integer.parseInt(s);
        pprids+=x+" ";
        
        
    }
   

    private static void displayCluster(final int level, String tag, Cluster cluster,
        int maxNumberOfDocumentsToShow, cf.ClusterDetailsFormatter clusterDetailsFormatter) throws URISyntaxException
    {
        //final String label = clusname;
        try {
            PreparedStatement pst=null;
            ResultSet rs=null;
            
            pst=con.prepareStatement("select size, score from clusters where name='"+clusname+"'");
            rs = pst.executeQuery();
            while (rs.next()) {
                siz=rs.getInt(1);
                scr=rs.getInt(2);
            }
            pst.close();
            rs.close();
        }
           catch(Exception e)
        {
            System.err.println("CF.2JAVA\t displayclusters2() \t"+e);
        }
           
        
        

        for (int i = 0; i < level; i++)
        {
            System.out.print("  ");
        }

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
        public final static cf.ClusterDetailsFormatter INSTANCE = new cf.ClusterDetailsFormatter();

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