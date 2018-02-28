package org.carrot2.examples;

import database_manipulator.database;
import java.net.URI;
import java.net.URISyntaxException;


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
import web.clusdisp;

//import static web.neoGraph.createNode;

/**
 * Simple console formatter for dumping {@link ProcessingResult}.
 */
public class ConsoleFormatter
{
    
    static neoGraph n;
    static URI firstNode;
    static String clusname="";
    static String pprids="";
    static int parentid, graphparent,clusid;
    static String QRY="";
    static int siz=0,count=0,scr=0;
    database connection;
    String user_name;
    
    /* public static void setval(int num)
    {
        count=num;
    }*/
     public ConsoleFormatter()
     {
         
     }
   public ConsoleFormatter(int parid,int graphpar,String s,database con,String uname)
            {
               n=new neoGraph();
               parentid=parid;
               graphparent=graphpar;
               s=s.replace('+',' ');
               QRY=s;
               connection=con;
               user_name=uname;
               PreparedStatement pst = null,pst2=null;
        ResultSet rs = null,rs2=null;
        String qry="",abs=null;

        try {
            System.err.println("REST1.JAVA!!!");
        
            pst=connection.con.prepareStatement("select count(clusid) from clusters_"+user_name);
            rs = pst.executeQuery();
            while (rs.next()) {
                clusid=rs.getInt(1);
            }
            pst.close();
            rs.close();
            }
            catch(Exception e)
            {
                
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
   
    public void displayDocuments(final Collection<Document> documents)
    {
        System.out.println("Collected " + documents.size() + " documents\n");
        for (final Document document : documents)
        {
            displayDocument(0, document);
        }
    }

    public void displayAttributes(final Map<String, Object> attributes)
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
            ClusterDetailsFormatter.INSTANCE);
    }

    public void displayClusters(final Collection<Cluster> clusters,
        int maxNumberOfDocumentsToShow, ClusterDetailsFormatter clusterDetailsFormatter) throws URISyntaxException
    {
        int x;
        
        try
        {
        System.out.println("\n\nCreated " + clusters.size() + " clusters\n");
        int clusterNumber = 1;
            Statement statement = null;
            ResultSet resultSet ;
            
           PreparedStatement pst= null;
           
        if(graphparent==0)
        {
            clusdisp display_graph = new clusdisp(connection,user_name);
            display_graph.clus_to_neo(QRY);
        }
       
        for (final Cluster cluster : clusters)
        {
           pprids=" "; 
           ++clusid;
           clusname="";
            pst = connection.con.prepareStatement("INSERT INTO clusters_"+user_name+"(name,size,score,clusid,parent) VALUES(?,?,?,?,?)");
           
                
                clusname=cluster.getLabel();
                if(clusname.equals("Other Topics"))
                {  
                    clusname+="_"+graphparent;
                    pst.setString(1,clusname);
                }
                else
                {
                    pst.setString(1,cluster.getLabel());
                }
                pst.setInt(2, cluster.size());
                pst.setDouble(3, cluster.getScore());
                pst.setInt(4, clusid);
                pst.setInt(5, parentid);
                x=clusid;
                pst.executeUpdate();
           
            System.out.println("name --  IMPORTANTE!!!!!"+cluster.getLabel());
            System.out.println("SCORE!!"+cluster.getScore());
            System.out.println("SIZE!!"+cluster.size());
            
         //   System.out.println("Subclus"+cluster.getSubclusters());
            displayCluster(0, "" + clusterNumber++, cluster, maxNumberOfDocumentsToShow,
                clusterDetailsFormatter);
            System.out.println("Papers - "+pprids);
            pst = connection.con.prepareStatement("update clusters_"+user_name+" set pprids = ? where clusid = ?");
              
                pst.setString(1,pprids);
                pst.setInt(2,clusid);
                
              
                pst.executeUpdate();
        }
        
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
        
    }

    private static void displayDocument(final int level, Document document)
    {
        
        final String indent = getIndent(level);
        String qry;
        int n=0;
        PreparedStatement pst= null;
            
        System.out.println("\nId="+document.getField(Document.CONTENT_URL));
        
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
        System.out.println(clusname + "gfdfdhfddhfdfgd"
            + clusterDetailsFormatter.formatClusterDetails(cluster));

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
