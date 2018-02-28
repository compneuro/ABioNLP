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
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import static org.carrot2.examples.clustering.Lingo_autoclust.calcscore;
import org.carrot2.examples.clustering.autopaper;
import org.carrot2.examples.clustering.cf;
import org.carrot2.examples.clustering.clusters;
import org.carrot2.examples.clustering.create_document;
import org.carrot2.examples.clustering.namelist;
import org.carrot2.matrix.factorization.IterationNumberGuesser;
import org.carrot2.matrix.factorization.KMeansMatrixFactorizationFactory;
import org.carrot2.matrix.factorization.LocalNonnegativeMatrixFactorizationFactory;
import org.carrot2.matrix.factorization.NonnegativeMatrixFactorizationEDFactory;
import org.carrot2.matrix.factorization.NonnegativeMatrixFactorizationKLFactory;
import org.carrot2.matrix.factorization.PartialSingularValueDecompositionFactory;
import org.carrot2.text.vsm.LinearTfIdfTermWeighting;
import org.carrot2.text.vsm.LogTfIdfTermWeighting;
import org.carrot2.text.vsm.TfTermWeighting;

/**
 *
 * @author rohit
 */
public class double_auto_lingo {
    
    
    String qry1="";
    String qry2="";
    String pprids1;
    String pprids2;
    database connection;
    public double_auto_lingo(String str1,String str2,database con)
    {
        qry1=str1;
        qry2=str2;
        connection=con;
        try
        {
            PreparedStatement ps;
            ResultSet rs;
            ps=connection.con.prepareStatement("Select paper_ids from query_1 where query_name = '"+qry1+"'");
            rs=ps.executeQuery();
            while(rs.next())
            {
                pprids1=rs.getString(1);
                System.out.println("QUERY 1 - "+qry1+"\t"+pprids1);
            }
            ps=connection.con.prepareStatement("Select paper_ids from query_1 where query_name = '"+qry2+"'");
            rs=ps.executeQuery();
            while(rs.next())
            {
                pprids2=rs.getString(1);
                System.out.println("QUERY 2 - "+qry2+"\t"+pprids2);
            }
            rs.close();
            ps.close();
        }
        catch (Exception e)
        {
            System.err.println("Auto_lingo constructor - \t"+e);
        }
        
    }
    public  int cluster(int no_of_clusters) throws URISyntaxException
    {
        
            final String [][] data = new String [200] [3];
             int i=0;
             String paperids;
              
            try
            {
                
                ArrayList<Document> documents = new ArrayList<Document>();
                PreparedStatement ps;
                ResultSet rs;
                String pprids="";
                System.out.println("Query 1 -");
                create_document doc1=new create_document(pprids1,connection);
                System.out.println("Query 2 -");
                create_document doc2=new create_document(pprids2,connection);
                
                
            /* Prepare Carrot2 documents */
            //final ArrayList<Document> documents = new ArrayList<Document>();
            int j;
            
            /* A controller to manage the processing pipeline. */
            final Controller controller = ControllerFactory.createSimple();

            /*
             * Perform clustering by topic using the Lingo algorithm. Lingo can 
             * take advantage of the original query, so we provide it along with the documents.
             */
            
            //LingoClusteringAlgorithmDescriptor.AttributeBuilder c =new LingoClusteringAlgorithmDescriptor.AttributeBuilder();
            //Double max_word_doc_freq=0.2;
            int n;
            int freq=12;
            
            for(n=1;n<=2;++n)
            {
                int tw_count=1,ff_count=1;
            auto_cf cf= new auto_cf(connection,n);
            //cf2 c= new cf2();
            System.out.println("FREQ - "+freq);
            String qry="";
            if(n==1)
            {
                qry=qry1;
                System.err.println(qry1);
                documents=doc1.getdocuments();
            }
            else if(n==2)
            {
                qry=qry2;
                System.err.println(qry2);
                documents=doc2.getdocuments();
            }
            
            while(tw_count<=3){
                ff_count=1;
                while(ff_count<=5)
                {
               final Map<String, Object> fastAttributes = Maps.newHashMap();
               
                   System.err.println(qry1);
                   
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

            
            ++cf.number;
            cf.phrasethresh=1;
            cf.max_doc=0;
            cf.displayClusters(clustersByTopic);
            ++ff_count;
                }
            ++tw_count;
            }
            
            }
            
            
//               findunique(); 

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
        int i=0,n;
        System.err.println("FIND UNIQUE");
        try
        {
            
            int clusterNumber = 0;
            int clus_num=1;
            for(n=1;n<=2;++n)
            {
                i=0;
                System.out.println("!_!_!_!"+n+"!_!_!_!");
            pst=connection.con.prepareStatement("select distinct(clusname) from auto_"+n);
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
            String concept="";
           
            while (rs.next()) {
                temp=rs.getString(1);
                if(temp.equalsIgnoreCase("Other Topics")!=true)
                {
                    int score=0;
                    
                ++i;
                conceptstype conc;
                conc=calcscore(rs.getString(1),api);
                System.out.println(rs.getString(1)+"    --  "+concept);
                pst2=connection.con.prepareStatement("select parent from auto_"+n+" where clusname = (?)");
                pst2.setString(1,rs.getString(1));
                String clusters="";
                
                rs2=pst2.executeQuery();
                while (rs2.next()) {
                clusters+=rs2.getInt(1)+" ";
                }
                System.out.println(i+"\t"+temp+"\t"+score+"\t"+clusters+"\n");
                pst2=connection.con.prepareStatement("insert into unique_"+n+" (id, name,concept,clusters,score) values (?,?,?,?,?)");
                pst2.setInt(1,i);
                pst2.setString(2,rs.getString(1));
                pst2.setString(3,conc.pref_name);
                pst2.setString(4,clusters);
                pst2.setInt(5,conc.score);
                pst2.executeUpdate();
                //pst2.close();
//                doubleqry_autopaper auto_paper=new doubleqry_autopaper(n,connection);
//                auto_paper.get_clus_info(i,rs.getString(1),connection);
                
/*                pst2=connection.con.prepareStatement("Select score, score2, size, clusters from unique_"+n+" where id = "+i);
                rs2 = pst2.executeQuery();
                double fin_score=0.0;
                while(rs2.next())
                {
                    int metamap_score=rs2.getInt(1);
                    double algo_score=rs2.getDouble(2);
                    int size=rs2.getInt(3);
                    String cluster=rs2.getString(4);
                    StringTokenizer tok=new StringTokenizer(clusters);
                    int no_of_parents=tok.countTokens();
                    fin_score=size*0.1*-1*metamap_score*0.01*no_of_parents*0.1+algo_score*0.001;
                    
                }
                pst2=connection.con.prepareStatement("Update unique_"+n+" set fin_score = (?) where id=(?)");
                pst2.setDouble(1, fin_score);
                pst2.setInt(2,i);
                pst2.executeUpdate();*/
                
                }
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
      //  selcluster();
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
         String file_name="/home/rohit/files/autocluster.txt";
                    File file = new File(file_name);                    
                    BufferedWriter pout;
                    boolean exist = file.createNewFile();
                    FileWriter fstream = new FileWriter(file);
                    pout = new BufferedWriter(fstream);
    
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
                        pout.write("1#####"+clusname+"\n");
                        int counter=0;
                        score=0;
                        for (PCM pcm: utterance.getPCMList()) {
                            int count=0, flag=0;
                            pout.write("2@@@@"+clusname+"\n");
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
                        
                        pout.write("1.Concept Name - "+temp.name+"\n");
                        pout.write("1.Type - "+temp.type+"\n");
                        pout.write("1.Score - "+temp.score+"\n");
                        pout.write("1.Preffered name - "+temp.pref_name+"\n");
        
                        temp=temp.next;
       
       
                    }
                   // System.out.println("!!!!!!!!!!!");
                    pout.write("\n_________________________\n");
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
        pout.close();
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
        try {
            
            PreparedStatement ps;
            ResultSet rs;
            ps=connection.con.prepareStatement("select id,name,concept,score from unique_1 where score < -800");
            rs = ps.executeQuery();
            String temp;
            String concept="";  
           int id;
           String name;
           conceptstype[] ct1=new conceptstype[1000];
           conceptstype[] ct2=new conceptstype[1000];
           int l1=0;
           
            while (rs.next()) {
                ct1[l1]=new conceptstype();
                ct1[l1].id=rs.getInt(1);
                ct1[l1].name=rs.getString(2);
                ct1[l1].concepts=rs.getString(3);
                ct1[l1].score=rs.getInt(4);
                /*ct1[l1].concepts=ct1[l1].concepts.replace("|", "");
                ct1[al1].concepts=ct1[l1].concepts.replace("  ", " ");
                ct1[l1].concepts=ct1[l1].concepts.trim();*/
                System.out.println("ID - "+ct1[l1].id);
                System.out.println("name - "+ct1[l1].name);
                System.out.println("concepts - "+ct1[l1].concepts);
                System.out.println("score - "+ct1[l1].score);
                ++l1;
            }
            ps=connection.con.prepareStatement("select id,name,concept,score from unique_2 where score < -800");
            rs = ps.executeQuery();
            int l2=0;
            while (rs.next()) {
                ct2[l2]=new conceptstype();
                ct2[l2].id=rs.getInt(1);
                ct2[l2].name=rs.getString(2);
                ct2[l2].concepts=rs.getString(3);
                ct2[l2].score=rs.getInt(4);
                /*ct2[l2].concepts=ct2[l2].concepts.replace("|", "");
                ct2[l2].concepts=ct2[l2].concepts.replace("  ", " ");
                ct2[l2].concepts=ct2[l2].concepts.trim();*/
                System.out.println("ID - "+ct2[l2].id);
                System.out.println("name - "+ct2[l2].name);
                System.out.println("concepts - "+ct2[l2].concepts);
                System.out.println("score - "+ct2[l2].score);
                ++l2;
            }
            
            int i,j;
            simmatrix[][] matrix= new simmatrix[l1][l2];
            for(i=0;i<l1;++i)
            {
                String ctone=ct1[i].concepts;
                ct1[i].concepts=ct1[i].concepts.replace(" ","_");
                ct1[i].concepts=ct1[i].concepts.replace("|"," ");
                for(j=0;j<l2;++j)
                {
                    //String ctwo=ct2[i].concepts;
                    matrix[i][j]= new simmatrix();
                    
                    if (ct1[i].name.equals(ct2[j].name))
                    {
                        matrix[i][j].complete_title=true;
                        matrix[i][j].final_score=3000;
                        System.out.println("COMPLETE TITLE = "+ct1[i].name+"------"+ct2[j].name);
                    }
                    else if (ctone.equals(ct2[j].concepts))
                    {
                        matrix[i][j].complete_concept=true;
                        System.out.println("COMPLETE CONCEPTS = "+ct1[i].concepts+"------"+ct2[j].concepts);
                        if(ct1[i].score==-1000&&ct2[j].score==-1000)
                        {
                            matrix[i][j].final_score=2000;
                            System.out.println("COMPLETE SCORE = "+ct1[i].score+"------"+ct2[j].score);
                        }
                        else
                        {
                            matrix[i][j].final_score=1000;
                            System.out.println("INCOMPLETE SCORE = "+ct1[i].score+"------"+ct2[j].score);
                        }
                    }
                    else
                    {
                        
                            /*ct2[j].concepts=ct2[j].concepts.replace(" ","_");
                            ct2[j].concepts=ct2[j].concepts.replace("|"," ");*/
                            
                        StringTokenizer tok= new StringTokenizer(ct1[i].concepts);
                        while(tok.hasMoreTokens())
                        {
                            String token;
                            token=tok.nextToken();
                            token=token.replace("_"," ");
                            token=token.trim();
                            
                            if(ct2[j].concepts.contains(token)&&!token.equals(""))
                            {
                                System.out.println(token+"===="+ct2[j].concepts);
                                matrix[i][j].name1=token;
                                matrix[i][j].final_score+=100;
                            }
                        }
                    }
                }
                ct1[i].concepts=ct1[i].concepts.replace(" ","|");
            ct1[i].concepts=ct1[i].concepts.replace("_"," ");
            }
            
            
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
            }
            
            
            
        }
        catch (Exception e)
        {
            System.out.println("Auto_lingo selcluster() - "+e);
        }
        return 1;
    }
    
    
    
    
    
    
    public static void main(String [] args) throws URISyntaxException
    {
        database con= new database ();
        double_auto_lingo c1=new double_auto_lingo("fragile+x+syndrome","autism",con);
      //  c1.cluster(30);
       // c1.findunique();
        c1.selcluster();
    }
}
