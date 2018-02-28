<%@page import="java.net.URLEncoder"%>
<%@page import="xml_highlevel.query_manager"%>
<%@page import="org.carrot2.examples.clustering.stc_autoclust"%>
<%@page import="org.carrot2.examples.clustering.normal_clustering_stc"%>
<%@page import="org.carrot2.examples.clustering.Lingo_autoclust"%>
<%@page import="web.clusdisp"%>
<%@page import="org.carrot2.examples.clustering.normal_clustering_lingo"%>
<%@page import="database_manipulator.database"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="com.sun.jersey.api.client.WebResource"%>
<%@page import="javax.ws.rs.core.MediaType"%>
<%@page import="com.sun.jersey.api.client.ClientResponse"%>
<%@page import="com.sun.jersey.api.client.Client"%>
<%@page import="web.cleardb"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="web.Rest1"%>
<%@page import="web.Clus"%>
<%@page import="web.NetClientGet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="web.retrieval"%>




<%
database connection = new database();
String user_query="";

response.setHeader("Cache-Control","no-cache"); //Forces caches to obtain a new copy of the page from the origin server
response.setHeader("Cache-Control","no-store"); //Directs caches not to store the page under any circumstance
response.setDateHeader("Expires", 0); //Causes the proxy cache to see the page as "stale"
response.setHeader("Pragma","no-cache"); //HTTP 1.0 backward compatibility
 String userName = (String) session.getAttribute("username");
if (null == userName) {
   request.setAttribute("Error", "Session has ended.  Please login.");
   RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
   rd.forward(request, response);
}

    
    
    try{
        request.getParameter("fname");
        //System.out.println("*******"+request.getParameter("fname")+"*******");
//clear databases
       user_query = request.getParameter("fname");
        if(request.getParameter("clr")!=null){
        cleardb cbd=new cleardb(connection,userName);
        cbd.clear();
        }
//recluster
        
        if(request.getParameter("clusname")!=null){
            String clusname;
            clusname=request.getParameter("clusname");
            //sstr1=str1;
            clusname=clusname.trim();
            normal_clustering_lingo cls= new normal_clustering_lingo(clusname, connection,userName);
            normal_clustering_stc stc =new normal_clustering_stc(clusname,connection,userName);
            
            int n;
            n=Integer.parseInt(request.getParameter("clusnum"));
            clusdisp cldisp=new clusdisp(connection,userName);
            cls.re_cluster(request.getParameter("clusname"),n);
            cldisp.reclus_neo(request.getParameter("clusname"),n);
        
        }
        else{
            
            
//first query clustering
            if(request.getParameter("fname")!=null){
                
                String c,merge,boost,maxclus,ttl_boost,trunc_label,word_doc_freq;          
                String algo=request.getParameter("algoselect");
                int algochoice=Integer.parseInt(algo);
                //retrieval rtr=new retrieval();
                c=request.getParameter("pprno");
                int prno=Integer.parseInt(c);

                

                /*str1=str1.trim();
                str2=str1;
                str1=str1.replace(' ','+');*/
                int i;
                
//Retrieval###############
                query_manager qm=new query_manager(connection,userName);
                user_query=qm.manage_query(user_query,prno);         
                    
//algo choice=2 signifies STC algorithm
                
                if(algochoice==2){
//auto clustering STC
                    cleardb cbd=new cleardb(connection,userName);
                    cbd.clear();
                    if(request.getParameter("clus")!=null && request.getParameter("clus").equals("auto_clus")){

                        stc_autoclust stc_auto=new stc_autoclust(user_query,connection,userName);
                        clusdisp cl=new clusdisp(connection,userName);
                        
                        

                        if(stc_auto.cluster(30)==1){

                            System.out.println("auto-Clustering STC");
                            cl.clus_to_neo(user_query);
                        }
                        else{

                            System.out.println("Error auto-Clustering STC");
                        }
                    } 
//normal clustering STC
                    else{
                         cbd.clear();
                        //stc stc=new stc(str1);
                        //String max_phrase="",max_word="",min_phrase="",sing_term_boost="",min_clus_score="",min_clus_size="",word_doc_thresh="";                                   
                        normal_clustering_stc stc = new normal_clustering_stc(user_query, connection,userName);
                        int maxbase,maxcl,minbclussize,worddocthresh;
                        double minbclusscore,mrg,singtermboost,phroverlap;
                        maxbase=Integer.parseInt(request.getParameter("maxbase"));
                        maxcl=Integer.parseInt(request.getParameter("maxclust"));
                        minbclusscore=Double.parseDouble(request.getParameter("minbclusscr"));
                        minbclussize=Integer.parseInt(request.getParameter("minbclussize"));
                        singtermboost=Double.parseDouble(request.getParameter("singtermboost"));
                        phroverlap=Double.parseDouble(request.getParameter("maxphrase"));
                        mrg=Double.parseDouble(request.getParameter("merge"));
                        worddocthresh=Integer.parseInt(request.getParameter("worddocthresh"));

                        if(stc.cluster(maxbase,maxcl,minbclusscore,minbclussize,singtermboost,phroverlap,mrg,worddocthresh)==1){

                            System.out.println("Clustering STC");
                            clusdisp cldisp= new clusdisp(connection,userName);
                            cldisp.clus_to_neo(user_query);
                        }
                        else{

                            System.out.println("Error Clustering STC");
                        }
                    }
                }
//algo choice=1 signifies LINGO
               
                else{//if(algochoice==1)
//auto-clustering LINGO
                    cleardb cbd=new cleardb(connection,userName);
                    cbd.clear();
                    if(request.getParameter("clus")!=null && request.getParameter("clus").equals("auto_clus")){
                        
                       
                        
                        Lingo_autoclust lin_auto=new Lingo_autoclust(user_query,connection,userName);
                        clusdisp cl=new clusdisp(connection,userName);
                        
                        

                        if(lin_auto.cluster(30)==1){
                            cl.clus_to_neo(user_query);
                            System.out.println("auto-Clustering Lingo");
                        }
                        else{

                            System.err.println("Error auto-Clustering Lingo");
                        }
                    }
//normal clustering LINGO                    
                    else{
                        //ClusteringDocumentList clus = new ClusteringDocumentList(str1);
                        normal_clustering_lingo clus= new normal_clustering_lingo(user_query, connection,userName);
                        int n;
                        double mg,phr,title_boost,trunc_label_thresh,word_doc;
                        n=Integer.parseInt(request.getParameter("clsno"));
                        mg=Double.parseDouble(request.getParameter("merge"));
                        phr=Double.parseDouble(request.getParameter("phr"));
                        title_boost=Double.parseDouble(request.getParameter("titl"));
                        trunc_label_thresh=Double.parseDouble(request.getParameter("trunc"));
                        word_doc=Double.parseDouble(request.getParameter("wrd_freq"));

                        if(clus.cluster(n,mg,phr,title_boost,trunc_label_thresh,word_doc)==1){

                            System.out.println("Clustering Lingo");
                            clusdisp cldisp= new clusdisp(connection,userName);
                            cldisp.clus_to_neo(user_query);
                        }
                        else{

                            System.out.println("Error Clustering Lingo");
                        }
                    }
                }
            } 
        
        }

    }finally{
        
    } 

%>


<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="error.jsp"%>
<!-- $Id: example.html,v 1.4 2006/03/27 02:44:36 pat Exp $ -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Automated Document Analysis System For Neuroinformatics</title>
       
        <link type="text/css" href="css/base.css" rel="stylesheet" />
        <link type="text/css" href="css/ForceDirected.css" rel="stylesheet" />
        <link rel="stylesheet" href="css/example.css" TYPE="text/css" MEDIA="screen">
        
        <script type="text/javascript">
            function  cleardata (){
                var xmlhttp;   
                var stp="val";
                var data='clr='+stp;
                if (window.XMLHttpRequest){// code for IE7+, Firefox, Chrome, Opera, Safari
                    
                  xmlhttp=new XMLHttpRequest();
                  }
                else{// code for IE6, IE5
                    
                  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
                  }
                 xmlhttp.onreadystatechange=function(){
                    
                  if(xmlhttp.readyState==4 && xmlhttp.status==200){                      
                       
                      window.location.reload();
                  }
              }
              xmlhttp.open("POST","home.jsp",true);
              xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
              xmlhttp.send(data);
          }
        </script>
        <script language="javascript" type="text/javascript" src="javascript/jquery.js"></script>
        <script type="text/javascript" src="javascript/jquery.color.js"></script>
        <script type="text/javascript" src="javascript/jQueryRotate.2.2.js"></script>
        <script type="text/javascript" src="javascript/jquery.easing.1.3.js"></script>

        <script type="text/javascript" src="javascript/content.events.js"></script>
        <script type="text/javascript" src="javascript/tabber.js"></script>
        <script language="javascript" type="text/javascript" src="javascript/jit1.js"></script>       
        <script language="javascript" type="text/javascript" src="javascript/example11.js"></script>
        
        
        <script type="text/javascript">
        document.write('<style type="text/css">.tabber{display:none;}<\/style>');
        </script>    
        
        <style>
            .TheTextOff{
            color:#000000;
            }

            .TheTextOn{
            color:#23A4FF;
            }
        </style>
        <script type="text/javascript">
            
            var load_semaphore = 0;
            var pane_position = 2;

            var wSize;
            var wSize1;
            function resize_handler () {
                    wSize = (($(window).height()-450)/2);
                    ((wSize > 14)?$('#div-cover').css('padding-top', Math.round(wSize)):wSize=15);                    
            }         
        
            
        </script>
    </head>


        


<body>
        <%
    
    try{
            ArrayList s=new ArrayList();
            ArrayList<Clus> doc=new ArrayList<Clus>();
            System.out.println("*****"+clus);
            //ArrayList s=null,doc=null;
            int i,j;
            Rest1 a= new Rest1(connection,user_query,userName);
            NetClientGet ng=new NetClientGet();
            //a.clearDB(); 
            doc=a.getdocuments();                                    
            s=a.getResult();
        %>
            <script>
            query('<%=user_query%>');
            </script>
        <%
           for(j=0;j<doc.size();j++){
               Object ob=null;
               ob=doc.get(j);
               String str="";
               JSONObject js=null;
               js=JSONObject.fromObject(ob);               
               str=js.toString();
               
               str=str.trim();
               System.err.println("json string"+str);
               str =str.replaceAll("'", "");
               //str=a.removeBadChars(str);
               //str=URLEncoder.encode(str, "UTF-8");
               
               String ta=doc.get(j).doctitle,tb=doc.get(j).docabstract,tc=doc.get(j).clustername;
               String td=doc.get(j).docauthor,te=doc.get(j).docjournal;
                //tb = tb.trim().replaceAll(" "," ");
                //tb = tb.trim().replaceAll("      ","\\");
               tb=tb.trim().replaceAll(":","-");
               tb=tb.trim().replaceAll("\\n","\"+\"");
        %>
        <script>        
            var doctit="<%=ta%>";
            var docabs="<%=tb%>";
            var docclus="<%=tc%>";
            var docauth="<%=td%>";
            var docjour="<%=te%>";
            
          //docabs=docabs.replace(/\s+/g,'+');
          //docabs=docabs.replace(/\./g,'\\')
          //  docabs = docabs.replace(/(\r\n|\n|\r)/gm,"\\");
            initial(doctit,docabs,docclus,docauth,docjour);
        </script>
         <%
           }
           
             
        for(i=0;i<s.size();i++)
        {
	    String sd=s.get(i).toString();
            String slst=ng.getJS(s.get(i).toString());
        %>
              <script>
              representation('<%=slst%>');
              </script>
        <%  }  
        
    }
    catch(Exception e)
    {
        System.out.println("ERRRRRRRRRRRRROOOOOORRRR!!! - "+e);
    }
        %>    
         <div id="content-wrapper">
             
                 <div id="left-container" align="center"> 
                     
                     <div class="text">
                         <h4>Automated Document Analysis System For Neuroinformatics</h4>
                         <% String curr_user=session.getAttribute("username").toString();                             %>
                         <form name="SignUp" id="checkform" action="home.jsp" method="post" >
                             <div>
                                 hi.....<%= curr_user%>
                                 Enter The Keyword<br><input id ="fname" type="text" value="" name="fname"><br>
                                 <br>Choose The Algorithm: <br>
                                 <select name="algoselect" id="algo" onchange="select_att();">
                        a             <option value="3" >Select An Algorithm</option>
                                     <option value="1"  >Lingo</option>
                                     <option value="2" >STC</option>
                                 </select>
                             </div>                             
                             <br><div>
                                 Normal Clustering<input type="radio"  name="clus" value="normal_clus" onclick="select_att();"><br>
                                 Auto-Clustering  <input type="radio"  name="clus" value="auto_clus" onclick="select_att();"><br>
                             </div>
                             <br><div id="div-att-input" style="display:none;">
                             </div>
                             <br><div>
                                 Enter The Number Of Papers to Be Retrieved<br><input type="text" value="50" name="pprno"><br>                  
                             </div>                   
                             <br><input type="submit" class="button" name="Submit" onclick="document.getElementById('div2').style.display = 'block';">
                         </form>
                         <form>
                             <br><button type="button" class="button" onclick="cleardata()">Clear DB</button>
                         </form>
                         <div id="div2" style="display:none;">
                             <img src="images/Ajax-loader1.gif" class="ajax-loader"/>LOADING....            
                         </div>
                         <div id="reclus" style="display:none;">
                             <img src="images/Ajax-loader1.gif" class="reclus-loader"/>RE-CLUSTERING....            
                         </div>
                         <br><button type="button" class="button" id="visualization" onclick='buttonClickEventHandle (this.id,"<%=user_query%>");'>Visualization</button>
                         <div id="Home">
                             <br><button type="button" class="button" id="Home1" style="display:none;" onclick='buttonClickEventHandle(this.id,"");'>Home</button>
                             <br><a href="logout.jsp">logout</a>
                         </div>
                         
                     </div>
                     <div id="id-list"></div>
                     <!--<div style="text-align:center;"><a href="examp1.js"></a></div>     -->       
                     
                 </div>
           
             <div id="container"  >
                 <script>
                     
                    function checkclick(){
                        $("#checkform input:checkbox:checked").each(function() {
                        $(this);
                        alert(this.name);
                        });
                    }
                     function select_att () {
                         var a;
                         document.getElementById('div-att-input').style.display = 'block';
                         var radios = document.getElementsByName("clus");

                        for (var i = 0; i < radios.length; i++) {       
                            if (radios[i].checked) {
                                a=radios[i].value;
                                break;
                            }
                        }
        
                        /*$(document).ready(function () {
                        $("#btn").click(function () {
                            alert($("input[type=radio]:checked").val());
                        });
                        });*/
                        
                         var s=document.getElementById("algo");  
                         var st = s.options[s.selectedIndex].value;
                         var b = document.getElementById ("div-att-input");
                         if (st==1 && a=="normal_clus") {
                             b.innerHTML = '<br><b>ATTRIBUTES:</b><br><br>Cluster Count Base<br><input type="text" value="6" name="clsno"><br><br>' +
                                     'Merging Threshold (0-1)<br><input type="text" value="0.7" name="merge"><br><br>' +
                                     'Phrase label boost(0.0-10.0)<br><input type="text" value="4.0" name="phr"><br><br>'+
                                     'Title word boost (0.0-10.0)<br><input type="text" value="2.5" name="titl"><br><br>'+
                                     'Truncated label threshold (0-1)<br><input type="text" value="0.5" name="trunc"><br><br>'+
                                     'Word document frequency threshold (0-1)<br><input type="text" value="0.9" name="wrd_freq"><br><br>';
                         }
                         else if (st==2 && a=="normal_clus") {
                             b.innerHTML = '<br><b>ATTRIBUTES:</b><br><br>Maximum Base Cluster Count<br><input type="text" value="300" name="maxbase"><br><br>' +
                                     'Number of Clusters<br><input type="text" value="7" name="maxclust"><br><br>' +
                                     'Minimum Base Cluster Score (0.0-10.0)<br><input type="text" value="5.0" name="minbclusscr"><br><br>'+  
                                     'Minimum Base Cluster Size (2 - 20)<br><input type="text" value="2" name="minbclussize"><br><br>'+
                                     'Single Term Boost (0-inf)<br><input type="text" value="0.5" name="singtermboost"><br><br>'+
                                     'Maximum Cluster Phrase Overlap<br><input type="text" value="0.6" name="maxphrase"><br><br>' +
                                     'Base Cluster Merging Threshold (0-1)<br><input type="text" value="0.5" name="merge"><br><br>' +
                                     'Word Document Frequency Threshold (1-100)<br><input type="text" value="2" name="worddocthresh"><br><br>';
                         }
                         else if(st==1 && a=="auto_clus"){
                             b.innerHTML="<br><br>LINGO AUTOMATIC CLUSTERING<br><br>";    
                         }
                         else if(st==2 && a=="auto_clus"){
                             b.innerHTML="<br><br> STC AUTOMATIC CLUSTERING<br><br>";   
                         }
                         else{
                             b.innerHTML = "Please Choose An Algorithm";
                         }
                     }
                </script>
                 <div id="contents-loader">
        
                    <div id="center-container"> 

                        <div id="infovis">
                            <div id ="logs" style="display:none;"></div>
                        </div>
                        <div id="log"></div>
                        <div id="check"></div>   
                    </div>

                    <div id="right-container" style="float: right;">
                        <div class="tabber">
                            <div id="tab1" class="tabbertab">
                                <h2>Title</h2>
                                <div id="inner-details"></div>
                            </div>
                            <div id="tab2" class="tabbertab" >
                                <h2>Details</h2>
                                <div id="detail-tab2"> </div>
                            </div>

                        </div>

                    </div>
                    <div class="clear-both"></div>

                </div>
             </div>
             
         </div>
<!--<div id="log"></div>-->
<script type="text/javascript">
function showHome () {
	if (pane_position == 1) {
		$("#left-container").animate({width:"100%"}, 1000, "easeInExpo", setUpHome);
		//$("#div-contents").fadeOut ("slow");
		//setUpHome();
		pane_position = 2;
	}
}

function setUpHome () {
	
	$("#div-ticker").slideDown("slow");
}

function animateOnLoad () {
	if (load_semaphore == 1)
		$("#div-nav-arrow").rotate ({angle:0, animateTo:360,
			easing: function (x, t, b, c, d) {
		return -c/2 * (Math.cos(Math.PI*t/d) - 1) + b;
	}, callback:animateOnLoad});
	else
		$("#div-nav-buttons").fadeIn("fast");
}

$(window).load (function () {
	//setUpHome();
	//addNavButtons();
	
	resize_handler ();
});

$(window).resize(function() {resize_handler();});
</script>
</body>
</html>