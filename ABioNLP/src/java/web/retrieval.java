package web;

//Class for retrieving papers.
/*
This part of the code uses orginal Carrot2 APIs which can be downloaded from http://download.carrot2.org/stable/javadoc/org/carrot2/core/Cluster.htm
*/


import database_manipulator.database;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;  
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;
import java.sql.ResultSet;  
import java.sql.Statement; 
import org.carrot2.examples.ConsoleFormatter;
public class retrieval  
{   
    int x,total;
    String tags[]=new String [37];
    int num[]=new int [200];
    String query="";
    public retrieval ()
    {
        x=0;
        total=0;
        tags[0]="<Count>";
        tags[1]="<IdList>";
        tags[2]="<Id>";
        tags[3]="<PubmedArticle>";//<ArticleTitle>Peptide mapping analysis of the avian progesterone receptor.</ArticleTitle>//
        tags[4]="<ArticleTitle>";
        tags[5]="</ArticleTitle>";
        tags[6]="<AbstractText>";       //<AbstractText/>
        tags[7]="</AbstractText>";
        tags[8]="<MeshHeadingList>";
        tags[9]="<MeshHeading>";
        tags[10]="<DescriptorName MajorTopicYN=\"N\">";
        tags[11]="<DescriptorName MajorTopicYN=\"Y\">";
        tags[12]="</DescriptorName>";
        tags[13]="<QualifierName MajorTopicYN=\"Y\">";
        tags[14]="<QualifierName MajorTopicYN=\"N\">";
        tags[15]="</QualifierName>";
        tags[16]="</MeshHeading>";
        tags[17]="</MeshHeadingList>";
        tags[18]="</PubmedArticle>";
        tags[19]="<AuthorList";
        tags[20]="<Author";
        tags[21]="</Author>";
        tags[22]="</AuthorList>";
        tags[23]="<LastName>";
        tags[24]="<ForeName>";
        tags[25]="<Initials>";
        tags[26]="<AbstractText ";
	tags[27]="NlmCategory=\"BACKGROUND\">";
	tags[28]="NlmCategory=\"OBJECTIVE\">";
	tags[29]="NlmCategory=\"METHODS\">";
	tags[30]="NlmCategory=\"RESULTS\">";
	tags[31]="NlmCategory=\"CONCLUSIONS\">";
	tags[32]="NlmCategory=\"UNASSIGNED\">";
        tags[33]="<AbstractText";
        tags[34]="</Abstract>";
        tags[35]="NlmCategory=\"UNLABELLED\">";
        tags[36]="NlmCategory";


        
    }
    
   
   //function for retrieving paper ids 
    
    public int getpapers(String qry,int prno) //ABC
    {
        int flag=0;
        int v,ev;
        String temp;
        query=qry;
       
        try 
                {           // &term=biomol+mrna[properties]+AND+mouse[organism]
                    //http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pmc&term=ataxia
                    
                    String u="http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&retmax="+prno+"&term=";
                    String ul="";//&retmax=30";//&Retstart=";
                    u+=qry;
                    
                    //u+=ul;
                    System.out.println(u);
                    URL google = new URL(u);            
                    URLConnection yc = google.openConnection(); 
                    String file_name="/home/rohit/files/abc_1.txt";
                    String file_name2="/home/rohit/files/abc_2.xml";
                    File file = new File(file_name);
                    File file2 = new File(file_name2);
                    BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                    String inputLine;            
                    boolean exist = file.createNewFile();
                    FileWriter fstream = new FileWriter(file_name);
                    BufferedWriter pout = new BufferedWriter(fstream);
                    FileWriter fstream2 = new FileWriter(file_name2);
                    BufferedWriter pout2 = new BufferedWriter(fstream2);
                    pout2.write(u);
                    while ((inputLine = in.readLine()) != null) 
                    {                
                               pout2.write(inputLine);
                               inputLine=inputLine.trim();
                               //pout.write("\n\nddddddd\n\n");
                               //pout.write(inputLine);
                               pout.write(inputLine+"\n");
                               if(inputLine.contains(tags[0]) && flag==0)
                                {
                                   flag=1;
                                   
                                   v=inputLine.indexOf(tags[0]);
                                   temp=inputLine.substring(v+7,inputLine.length());
                                   ev=temp.indexOf("</");
                                   temp=temp.substring(0, ev);
                                   x=Integer.parseInt(temp);
                                   //pout.write("\n\nddddddddd\n\n"+x+"\n\nddddddddd\n\n");
                                   
                                   
                          
                                
                                System.out.println(inputLine);
                                }
                               else if (inputLine.contains(tags[2]))
                               {
                                   v=inputLine.indexOf(tags[2]);
                                   temp=inputLine.substring(v+4,inputLine.length());
                                   ev=temp.indexOf("</");
                                   temp=temp.substring(0, ev);
                                   num[total]=Integer.parseInt(temp);
                                   //pout.write("\n\nddddddddd\n\n"+num[total]+"\n\nddddddddd\n\n");
                                   ++total;
                               }
 
                    }        
                    pout.close();
                    pout2.close();
                    in.close();   
                    System.out.println("File created successfully.");
                    retrsum();
                     return 1;
                     
                } 
                catch (Exception e) 
                {            
                   // System.out.println("adfsfs");
                    e.printStackTrace();        
                }    
       return 0;
    }
    
    
    //function for retrieving summary of the ids extracted above
    
    public void retrsum()
    {
        
        int i;
        String temp; 
        try 
        {      
        String u="http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=";//20879892
        //http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&retmode=xml&rettype=citation&id="
        //http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=protein&id=15718680,157427902,119703751
        //http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=17284678,9997&retmode=text&rettype=keywords
        
        /*
        
        
        http://eutils.ncbi.nlm.nih.gov/entrez/eutils/einfo.fcgi?db=pubmed&id=23155407,23154787
        http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pmc&id=23155407,23154787&retmode=xml
        
        3498369,3496881,3496730,3494662,3494013
        */
        String ul="&retmode=xml&rettype=citation";// &rettype=fasta&retmode=text";
        for(i=0;i<total;++i)
        {
            u+=num[i];
            u+=",";
        }
        u+=ul;
        URL google = new URL(u);            
        URLConnection yc = google.openConnection();     
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        String file_name="/home/rohit/files/Summary.txt";
        File file = new File(file_name);
        boolean exist = file.createNewFile();
        FileWriter fstream = new FileWriter(file);
        BufferedWriter pout = new BufferedWriter(fstream);
        String inputline;
                System.err.println(u);
        while ((inputline = in.readLine()) != null) 
        {
            pout.write(inputline);
            pout.write("\n");
        }
        pout.close();
        sumproc();
        
        
        }
        
        catch (Exception e) 
         {
               e.printStackTrace();        
         }
        finally
        {
          
        }
        //sumproc();
        //http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=11748933,11700088&retmode=xml
        //http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=23155407,23154787,23154746&retmode=xml
        
    }
    
    //Function for retrieving the abstracts and titles from the summary obtained in the function above.
    
    
     public void sumproc()
    {
        int abstno=0,len,count=0,curr=0;
        String temp="",curtitle="";
        try
        {
            String file_name="/home/rohit/files/Summary.txt";
            String file_nm="/home/rohit/files/Summarytemp.txt";
            database database_connector= new database();
            //Connection database_connector.con = database_connector.con;
            
            Statement statement = null;
            ResultSet resultSet ;
            PreparedStatement pst= null;
            
            
            
            //pst = con.prepareStatement("select t.title,t.abstract,c.name from test t,clusters c WHERE c.clusid=t.cluster_no");
            pst=database_connector.con.prepareStatement("select count(id) from test");
            resultSet = pst.executeQuery();
            while (resultSet.next()) {
                count=(resultSet.getInt(1));
            }
            pst.close();
            resultSet.close();
            
            
            
            //Class.forName("com.mysql.jdbc.Driver").newInstance();  
                       
            File file2 = new File(file_nm);
            boolean exist = file2.createNewFile();
            FileWriter fstream = new FileWriter(file2);
            BufferedWriter pout = new BufferedWriter(fstream);
            File file = new File(file_name);
            FileReader fr=new FileReader(file);
            BufferedReader in = new BufferedReader(fr);
            String inputline,qry;
            int flagname=0,flagabstract=0;
            String abs="";

            
        while ((inputline = in.readLine()) != null) 
        {
            
       inputline=inputline.trim();
            
            if(inputline.contains(tags[4]))
            {++count;
                len=inputline.length();
                temp=inputline.substring(14, len-15);
                pout.write(count+")   "+"TITLE - "+temp+"\n\n");
               // System.out.println(count+")   "+"TITLE - "+temp+"\n\n");
                pout.write("\n\n_____abcd_______\n\n");
                /*qry="insert into test (id,title) values( "+count+" , \""+temp+"\")";
                statement.executeUpdate(qry);*/
                pst = database_connector.con.prepareStatement("INSERT INTO test(title,id,query) VALUES(?,?,?)");
                               // pst.setInt(1,idnum);
                pst.setString(1,temp);
                pst.setInt(2, count);
                pst.setString(3,query);
                
                pst.executeUpdate();
               //curtitle=temp;
                curr=count;
  /*
                ps.setInt(1,count);
                ps.setString(2,temp);
                ps.executeUpdate();
*/

            }
            else if(inputline.contains(tags[33])){
                
                if(inputline.contains(tags[6]))
                {
                inputline.trim();                    
                len=inputline.length();
                abs=inputline.substring(14, len-15);
                pout.write("ABSTRACT - "+abs+"\n");
                pout.write("\n\n________________________________________________________________________\n\n");
                pst = database_connector.con.prepareStatement("Update test set abstract= ? WHERE id="+curr);
                ///pst.setInt(1,count);
                System.out.println(abs);
                pst.setString(1,abs);
                pst.executeUpdate();
                abs="";
                    
                }
                else if(inputline.contains(tags[36]))
                {
                    while(inputline!=null&&!(inputline.contains(tags[34]))){
                        inputline.trim();
                        if(inputline.contains(tags[27]))
                        {
                            int start_index;
                            start_index=inputline.indexOf(tags[27])+tags[27].length();
                            int stop_index;
                            stop_index=inputline.indexOf(tags[7]);
                            
                            abs+="Background : "+inputline.substring(start_index, stop_index)+"\n";
                        }
                        else if(inputline.contains(tags[28]))
                        {
                            int start_index;
                            start_index=inputline.indexOf(tags[28])+tags[28].length();
                            int stop_index;
                            stop_index=inputline.indexOf(tags[7]);
                            
                            abs+="Objective : "+inputline.substring(start_index, stop_index)+"\n";
                        }
                        else if(inputline.contains(tags[29]))
                        {
                            int start_index;
                            start_index=inputline.indexOf(tags[29])+tags[29].length();
                            int stop_index;
                            stop_index=inputline.indexOf(tags[7]);
                            
                            abs+="Methods : "+inputline.substring(start_index, stop_index)+"\n";
                        }
                        else if(inputline.contains(tags[30]))
                        {
                            int start_index;
                            start_index=inputline.indexOf(tags[30])+tags[30].length();
                            int stop_index;
                            stop_index=inputline.indexOf(tags[7]);
                            
                            abs+="Results : "+inputline.substring(start_index, stop_index)+"\n";
                        }
                        else if(inputline.contains(tags[31]))
                        {
                            int start_index;
                            start_index=inputline.indexOf(tags[31])+tags[31].length();
                            int stop_index;
                            stop_index=inputline.indexOf(tags[7]);
                            
                            abs+="Conclusion : "+inputline.substring(start_index, stop_index)+"\n";
                        }
                        else if(inputline.contains(tags[32]))
                        {
                            int start_index;
                            start_index=inputline.indexOf(tags[32])+tags[32].length();
                            int stop_index;
                            stop_index=inputline.indexOf(tags[7]);
                            
                            abs+="Unassigned : "+inputline.substring(start_index, stop_index)+"\n";
                        }
                        else if(inputline.contains(tags[35]))
                        {
                            int start_index;
                            start_index=inputline.indexOf(tags[35])+tags[35].length();
                            int stop_index;
                            stop_index=inputline.indexOf(tags[7]);
                            
                            abs+=inputline.substring(start_index, stop_index)+"\n";
                        }
                        
                        inputline=in.readLine();
                    }
                
                pout.write("ABSTRACT - \n"+abs+"\n");
                pout.write("\n\n________________________________________________________________________\n\n");
                System.err.println(abs);
                pst = database_connector.con.prepareStatement("Update test set abstract= ? WHERE id="+curr);
                ///pst.setInt(1,count);
                pst.setString(1,abs);
                pst.executeUpdate();
                abs="";
                }
                

               // System.out.println("ABSTRACT - "+temp+"\n");
                //System.out.println("\n\n________________________________________________________________________\n\n");
               /*qry="Update test set abstract = \""+temp+"\" where id = "+count;
                statement.executeUpdate(qry) ;*/
            }
            else if (inputline.contains(tags[8]))
            {
                   System.out.println("ghghghg"+curr);
             //   inputline=in.readLine();
                /*
                 * tags[8]="<MeshHeadingList>";
        tags[9]="<MeshHeading>";
        tags[10]="<DescriptorName MajorTopicYN=\"N\">";
        tags[11]="<DescriptorName MajorTopicYN=\"Y\">";
        tags[12]="</DescriptorName>";
        tags[13]="<QualifierName MajorTopicYN=\"Y\">";
        tags[14]="<QualifierName MajorTopicYN=\"N\">";
        tags[15]="</QualifierName>";
        tags[16]="</MeshHeading>";
        tags[17]="</MeshHeadingList>";
                 */
              String keys="";
                while((inputline.compareTo(tags[17])!=0)&&inputline!= null)
                {
                       inputline=in.readLine();
                       inputline=inputline.trim();
                      
                       if(inputline.contains(tags[9]))
                       {
                           while((inputline.compareTo(tags[16])!=0)&&inputline!= null)
                           {
                                inputline=in.readLine();
                                inputline=inputline.trim();
                                len=inputline.length();
                       
                                if(inputline.contains(tags[10])||inputline.contains(tags[11]))
                                {
                                    len=inputline.length();
                                    temp=inputline.substring(33, len-17);
                                    //System.out.println("Keywords - "+temp);
                                }
                                else if(inputline.contains(tags[13])||inputline.contains(tags[14]))
                                {
                                    len=inputline.length();
                                    temp+=" "+inputline.substring(32, len-16);
                                    //System.out.println("Keywords2 - "+temp);
                                }
                         }
                            //System.out.println("Keyword = "+temp);
                           keys+=temp+" | ";
                            pout.write("Keyword = "+temp+"\n");
                            
                            //inputline=in.readLine();
                            //inputline=inputline.trim();
                }
                       
                       
                }
                System.out.println("Keyword = "+keys);
                       pst = database_connector.con.prepareStatement("Update test set keywords= ? WHERE id="+curr);
                       pst.setString(1,keys);
                       pst.executeUpdate();
            }
            else if(inputline.contains(tags[19]))
            {
                /*
                 * tags[19]="<AuthorList>";
        tags[20]="<Author>";
        tags[21]="</Author>";
        tags[22]="</AuthorList>";
        tags[23]="<LastName>";
        tags[24]="<ForeName>";
        tags[25]="<Initials>";
                 * 
                 * 
                 */
                while((inputline.compareTo(tags[22])!=0)&&inputline!= null)
                {
                       inputline=in.readLine();
                       inputline=inputline.trim();
                    if(inputline.contains(tags[20]))
                    {
                        while(inputline.compareTo(tags[21])!=0&&inputline!= null)
                        {
                            inputline=in.readLine();
                            inputline=inputline.trim();
                            len=inputline.length();
                            if(inputline.contains(tags[23]))
                            {
                                temp= inputline.substring(10, len-11);
                            }
                            else if(inputline.contains(tags[24]))
                            {
                                temp+=" . "+ inputline.substring(10, len-11);
                            }
                            else if(inputline.contains(tags[25]))
                            {
                                temp+=" . "+ inputline.substring(10, len-11);
                            }
                            
                        }
                        //System.out.println("Autor Name = "+temp);
                        pout.write("Autor Name = "+temp+"\n");
                    }
                 
                }
                
            }
            
         
        }
        pout.close();
        database_connector.close();
       
        }
        catch (Exception e) 
         {            
               e.printStackTrace();        
         }
//        return 1;
    }
    
    public static void main(String[] args) 
            {          
                /*try 
                {            
                    String u="http://www.ncbi.nlm.nih.gov/pubmed?term= cerebellum";
                    URL google = new URL("http://www.ncbi.nlm.nih.gov/pubmed?term=cerebellum");            
                    URLConnection yc = google.openConnection(); 
                    String file_name="abc.html";
                    File file = new File(file_name);
                    BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                    String inputLine;            
                    boolean exist = file.createNewFile();
                    FileWriter fstream = new FileWriter(file_name);
                    BufferedWriter out = new BufferedWriter(fstream);
                    while ((inputLine = in.readLine()) != null) 
                    {                
                       // System.out.println(inputLine);             
                        
                          
                       /*   if (!exist)
                            {
                                System.out.println("File already exists.");
                                System.exit(0);
                            }
                         else
                            { 
                                
                                out.write(inputLine);
                                
                                System.out.println(inputLine);
                            //}
 
                    }        
                    out.close();
                    in.close();   
                    System.out.println("File created successfully.");
                
                } 
                catch (Exception e) 
                {            
                   // System.out.println("adfsfs");
                    e.printStackTrace();        
                }    */
                retrieval a=new retrieval();
                a.getpapers("ataxia",200);
                //a.sumproc();
}
}




/*   if(inputline.contains(tags[3]))
               {
                   do
                   {
                   if(inputline.contains(tags[4]))
                   {    pout.write(++abstno+"\n");
                       len=inputline.length();
                       temp=inputline.substring(13, len-13);
                       pout.write("TITLE - "+temp+"\n");
                       
                   }
                   if(inputline.contains(tags[6]))
                   {
                      len=inputline.length();
                       temp=inputline.substring(13, len-13);
                       pout.write("Abstract - "+temp+"\n");
                       
                   }
                   if(inputline.contains(tags[8]))
                   {
                        pout.write("\n\nINSIDE!!!\n\n");
                       inputline = in.readLine();
                       do
                       {
                           if(inputline.contains(tags[9]))
                           {pout.write("\n\nINSIDE2!!!\n\n");
                               do
                               {
                                   if(inputline.contains(tags[10])||inputline.contains(tags[11]))
                                   {pout.write("\n\nINSIDE!!!\n\n");
                                       len=inputline.length();
                                       temp=inputline;//.substring(34, len-34);
                                       pout.write(temp);
                                       inputline = in.readLine();
                                   }
                                   if(inputline.contains(tags[13])||inputline.contains(tags[14]))
                                   {
                                       len=inputline.length();
                                       temp=inputline;//.substring(33, len-33);
                                       pout.write(" - "+temp+"\n");
                                       inputline = in.readLine();
                                   }
                                   
                                   pout.write("\n");
                                   
                               }while(inputline.contains(tags[16]));
                           }
                           inputline = in.readLine();
                       }while(inputline.contains(tags[17]));
                       
                   }
                   inputline = in.readLine();
               }while(!inputline.contains(tags[18]));
                   
            //pout.write("\n\n one sentence\n\n"+inputline);
            pout.write("\n\n");
        }*/