var labelType, useGradients, nativeTextSupport, animate;
var a,c,flag1=0,k;
var json=[];
var doc=[];
var t=0,y=0,j=0;
var js,res;
var e=0,w=0,b=0,d=0;
var size=[];
var score=[];
var numbr=0;
var qry;

var z=1,q=0,wl=0,l=0,h,s,g;
var clr=['FF981D','F3B200','FF76BC','E064B7','CEA539'];
(function() {
  var ua = navigator.userAgent,
      iStuff = ua.match(/iPhone/i) || ua.match(/iPad/i),
      typeOfCanvas = typeof HTMLCanvasElement,
      nativeCanvasSupport = (typeOfCanvas == 'object' || typeOfCanvas == 'function'),
      textSupport = nativeCanvasSupport 
        && (typeof document.createElement('canvas').getContext('2d').fillText == 'function');
  //I'm setting this based on the fact that ExCanvas provides text support for IE
  //and that as of today iPhone/iPad current text support is lame
  labelType = (!nativeCanvasSupport || (textSupport && !iStuff))? 'Native' : 'HTML';
  nativeTextSupport = labelType == 'Native';
  useGradients = nativeCanvasSupport;
  animate = !(iStuff || !nativeCanvasSupport);
})();


var Log = {
  elem: false,
  write: function(text){
    if (!this.elem) 
      this.elem = document.getElementById('logs');
    this.elem.innerHTML = text;
    this.elem.style.left = (200 - this.elem.offsetWidth / 2) + 'px';
  }
};
/*console.log('getPricing just got called with');
                                                                                            var queryterm=document.getElementById(fname);
                                                                                            query1='<input type=\"checkbox\" class=\"checkq\" name=\"'+queryterm+'\" value=\"'+queryterm+'\">';
                                                                                            queryapp=queryapp+query1;
                                                                                            document.getElementById('check').innerHTML='<form name=\"checkbox\" id=\"checkform\" action=\"home.jsp\" method=\"post\">'+queryapp+'<input type=\"submit\" class=\"button\" name=\"Submit\" onclick=\"checkclick();\"></form>';
                                                                                  */
function query(val){
  qry=val;
}
function colour()
{
    
    return(clr[Math.floor((Math.random()*4))]);
}

function reCluster(str,str1)
{
        var xmlhttp;
        //var f=new FormData();
        //str="abcd";
        //f.append("keyval",str);
                var data='clusname='+str+'&clusnum='+str1;
        if (window.XMLHttpRequest)
          {// code for IE7+, Firefox, Chrome, Opera, Safari
          xmlhttp=new XMLHttpRequest();
          }
        else
          {// code for IE6, IE5
          xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
          }
        xmlhttp.onreadystatechange=function()
          {
          if (xmlhttp.readyState==4 && xmlhttp.status==200)
            {
                //reload the graph
              // document.getElementById("container").innerHTML=xmlhttp.responseText;
              //$("#container").load('visual.jsp');
              /*var container = document.getElementById('center-container');
               var refreshContent = container.innerHTML;
                container.innerHTML = refreshContent;*/
               // $('#infovis').fadeOut('slow').load('index.jsp '+'#infovis');
               //document.getElementById('_tooltip').style.display = 'none';
               
               
               document.getElementById('reclus').innerHTML="RECLUSTERING COMPLETE";
               buttonClickEventHandle("Home1","");
		
                    
              window.location.reload();              
              
              buttonClickEventHandle("","");
              
            //document.getElementById("txtHint").innerHTML=xmlhttp.responseText;
            }
          }
          	
        xmlhttp.open("POST","home.jsp",true);
        xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xmlhttp.send(data);
}

/*var doctit=<%=doc.get(j).doctitle%>;
            var docabs=<%=doc.get(j).docabstract%>;            
            var docclus=<%=doc.get(j).clustername%>;
    */
function initial(val1,val2,val3,val4,val5)
{
    
    var obt=0;i=0;
    //value1=encodeURI(value1);
    //js=JSON.parse(value1);
      //js=JSON&&JSON.parse(value2)||$.parseJSON(value2);
      /*var obji=eval('('+value2+')');
      var res=obji.result;
      var cou=obji.count;
      alert('result:'+result+'count:'+count);*/
      
     // alert(js.abstract);
      //alert(obji.count);
      console.log(val1);
      console.log(val2);
      console.log(val3);
      for(i=0;i<j;i++){
          if(doc[i].title==val1)
              {
                  doc[i].clusname.push(val3);
                  //document.write("</br</br>");
                  //document.write(js.cluster);
                  obt=1;
                  break;
              }
      }
      if(obt===0){
          doc[j]={"clusname":[],
            "title": val1,
            "abstract": val2,
            "author":val4,
            "journal":val5}
            doc[j].clusname.push(val3);
            j++;
           // console.log(js.title);
      }     
    
      //document.write(js.title);
      //           document.write("<br><br><br>");
                
    
}
/*function tab(tab) {
document.getElementById('infovis').style.display = 'none';
document.getElementById('tab2').style.display = 'none';
document.getElementById('li_tab1').setAttribute("class","");
document.getElementById('li_tab2').setAttribute("class","");
document.getElementById(tab).style.display = 'block';
if(tab=='infovis')
    {
        document.getElementById('li_tab1').setAttribute("class", "active");
    }
    else
        {
            document.getElementById('li_tab2').setAttribute("class", "active");
        }

}*/
/*function tab1(tab) {
document.getElementById('inner-details').style.display = 'none';
document.getElementById('detail-tab2').style.display = 'none';
document.getElementById('rili_tab1').setAttribute("class","");
document.getElementById('rili_tab2').setAttribute("class","");
document.getElementById(tab).style.display = 'block';
if(tab=='inner-details')
    {
        document.getElementById('rili_tab1').setAttribute("class", "active");
    }
    else
        {
            document.getElementById('rili_tab2').setAttribute("class", "active");
        }

}*/
function disp(r)
{
  //event.preventDefault();
  var lis=0;
  var strng="</br><b>"+doc[r].title+"</b></br></br><b><u>Author</u></b></br></br>"+doc[r].author
          +"</b></br></br><b><u>Abstract</u></b></br></br>"+doc[r].abstract+"</b></br></br><b><u>Journal</u></b></br></br>"+
          doc[r].journal+"</b></br></br><b><u>Clusters</u></b></br>";
  var strng2="";
  //document.getElementById("detail-tab2").innerHTML=
  for(lis=0;lis<(doc[r].clusname).length;lis++){
     // document.write(doc[r].clusname[lis]);
      //document.write("<br><br><br>");
      strng2=strng2+"</br>"+doc[r].clusname[lis];
      
  }
  document.getElementById("detail-tab2").innerHTML=strng+strng2;
    //document.getElementById('detail-tab2').style.display = 'block';
    //document.getElementById('inner-details').style.display = 'none';
    document.getElementById('tab1').className="tabbertab tabbertabhide";
    document.getElementById('Title').setAttribute("class","");    
    document.getElementById('tab2').className="tabbertab";
    document.getElementById('Details').setAttribute("class", "tabberactive");
    
   /* if(document.getElementById(r).style.display=='none')
        {document.getElementById(r).style.display='block';}
    else
        {document.getElementById(r).style.display='none';}*/
}
function extract(val)
{
    var val1="",k;
    //val1=[];
    if(val==qry)
        {
           // val1="<ul>";
             for(k=0;k<j;k++)
                 {
                    val1=val1+"<div class=\"TheTextOff\" onmouseover=\"this.className='TheTextOn'\" onmouseout=\"this.className='TheTextOff'\" href=\"#\" onclick=\"javascript:disp('"+k+"');\"></br><b>"+doc[k].title+"</b></br></div>";
                    //val1=val1+"<li id='"+k+"' style=\"display:none;\"></br>"+doc[k].abstract+"</li></br>";
                    

                    
                 }
                 //val1=val1+"</ul>";
        }
        else
            {
               // val1="<ul>";
                for(k=0;k<j;k++)
                    {
                        for(l=0;l<doc[k].clusname.length;l++){
                            if(doc[k].clusname[l]==val)
                            {
                                
                             val1=val1+"<div class=\"TheTextOff\" onmouseover=\"this.className='TheTextOn'\" onmouseout=\"this.className='TheTextOff'\" href=\"#\" onclick=\"javascript:disp('"+k+"');\"></br><b>"+doc[k].title+"</b></br></div>";
                           //     val1=val1+"<li id='"+k+"' style='display:none;'></br>"+doc[k].abstract+"</li></br>";
                                

                                
                            }
                        }
               
                        
                    }
                  //  val1=val1+"</ul>";
            }
        return val1;
}

function representation(value1)
{
    //document.write(value1);
    //document.write("<br><br><br>");
    
   //document.write(value1);
   // document.write("<br><br><br>");
   // document.write("<br><br><br>");;


                js=JSON.parse(value1);
               
                    if(js.data.id!=undefined)
                    {	
                            apphend(js.data.id,1);
                            z=1;
                            g=((js.self).length)-1;
                            //document.write(js.self[g]);
                            //document.write(js.self[g-1]);
                            //document.write("\n");
                            var k=js.self[g];
                            l=0;
                            //document.write(k);
                            while(k != '/')
                            {
                                    l=l+(z*js.self[g]);
                                    z=z*10;
                                    g--;
                                    k=js.self[g];
                            }

                            apphend(l,3);
                            apphend(js.data.size,5);
                            apphend(js.data.score,4);
                            apphend(js.data.kind,2);


                    }     
                    else
                    {
                         var m;
                            h=((js.start).length)-1;
                            s=((js.end).length)-1;
                                    q=0;z=1;
                                    var k=js.start[h];
                                    while(k != '/')
                                    {
                                            q=q+(z*js.start[h]);
                                            z=z*10;
                                            h--;
                                            k=js.start[h];
                                    }

                                    z=1;
                                    wl=0;
                                     k=js.end[s];
                                    while(k != '/')
                                    {
                                            wl=wl+(z*js.end[s]);
                                            z=z*10;
                                            s--;
                                            k=js.end[s];
                                    }                            
                                    for(m=0;m<t;m++)
                                    {	
                                            if(json[m].num===q)
                                            {
                                                    for(var c=0;c<t;c++)
                                                    {
                                                            if(json[c].num===wl)
                                                            {
                                                                    var ob1,ob2;
                                                                    ob1=new Object();
                                                                    ob2=new Object();
                                                                    ob1={"nodeTo": json[c].id,
                                                                        "nodeFrom": json[m].id,
                                                                        "data": {
                                                                                "weight": json[c].score,
                                                                                 "labeltext":js.type
                                                                                }
                                                                        }
                                                                      ob2={"nodeTo": json[m].id,
                                                                        "nodeFrom": json[c].id,
                                                                        "data": {
                                                                                "weight": json[m].score,
                                                                                "labeltext":js.type
                                                                                }
                                                                         }
                                                                   json[m].adjacencies.push(ob1);
                                                                     json[c].adjacencies.push(ob2);
                                                                     y++;                                       
                                                               }

                                                    }
                                            }			
                                    }

                            }

                       // k++;

              //  }
        //}
}

function apphend(key1,flag)
{
	if(flag==1)
		{		
		a=key1;
		}
	if(flag==3)
		{
		c=key1;
		}
         if(flag==4)
		{
		d=key1;
		}
        if(flag==5)
		{
		e=key1;
		}
	if(flag==2)
                {
                            b=key1;

                      //  document.write("size!!!!"+size[t]);
                    if(b=="star")
                        {
                             //var jill= new Object() ;
                             json[t]=new Object();
                            json[t]={       "id":a,
                                            "size": e,
                                            "kind": b,
                                            "score": 20,
                                            "num":c,
                                            "data":{"$dim":30,"$type":b,"$color":"#FF1D77"},
                                            "adjacencies":[]
                                    }
                           // json.push(jill);
                                            t=t+1;
                        }
                        else{
                            json[t]=new Object();
                            json[t]= {       "id":a,
                                            "size": e,
                                            "kind": b,
                                            "score": 20,
                                            "num":c,
                                            "data":{"$dim":e},
                                            "adjacencies":[]
                                    }
                            //json.push(jill);
                                            t=t+1;
                           }

		}
                
}



function init(){
    //init data  
    
    
    
    //init RGraph
    /*
    $jit.RGraph.Plot.EdgeTypes.implement({
            'labeled': {
              'render': function(adj, canvas) {
                this.edgeTypes.arrow.render.call(this, adj, canvas);
                var data = adj.data;
                if(data.labeltext) {
                  var ctx = canvas.getCtx();
                  var posFr = adj.nodeFrom.pos.getc(true);
                  var posTo = adj.nodeTo.pos.getc(true);
                  ctx.fillStyle="#00C13F";
                    // Fill with gradient
                    
                  ctx.fillText(data.labeltext, (posFr.x-10 + posTo.x-10)/2, (posFr.y-10 + posTo.y-10)/2);
                }// if data.labeltext
              }
            }
          });*/

    var rgraph = new $jit.RGraph({
        //Where to append the visualization
        injectInto: 'infovis',
        //Optional: create a background canvas that plots
        //concentric circles.
        
        //Add navigation capabilities:
        //zooming by scrolling and panning.
        Navigation: {
          enable: true,
          panning: true,
          zooming: 50
        },
        //Set Node and Edge styles.
        Node: {
            'overridable': true,
           //color: '#ddeeff'   
           color: '#8A2BE2'
        },
        

        Edge: {
            'overridable': true,
             //type:'labeled',
             color: '#F3B200'
              },
       
        onBeforePlotLine: function(adj,node){
            //Add some random lineWidth to each edge.
            if (!adj.data.$lineWidth) 
                adj.data.$lineWidth =adj.data.weight/10;
             },
        
    levelDistance: 200,


       Tips: {
      enable: true,
      onShow: function(tip, node) {
             
            list = [];
        //node.eachAdjacency(function(adj){
         // list.push(adj.nodeTo.name);
        //});
        //append connections information in the side panel
        
           tip.innerHTML = "<div class=\"tip-title\">" + node.id + "</div>"+ "<div class=\"tip-text\"><b>documents:</b> " + (node.size) + "</div>";
        
      }
    },
		
	Events: {
      enable: true,     
              type: 'Native',

      onDragMove: function(node, eventInfo, e) {
          var pos = eventInfo.getPos();
          node.pos.setc(pos.x, pos.y);
          fd.plot();
      },
      //Implement the same handler for touchscreens
      onTouchMove: function(node, eventInfo, e) {
        $jit.util.event.stop(e); //stop default touchmove event
        this.onDragMove(node, eventInfo, e);
      },       
      onRightClick: function(node){
        //hide tips and selections
         
        if((typeof node.num!=='undefined')&&(node.kind!="star"))
        {
            document.getElementById("logs").style.display="block";
            Log.write("Clustering...."+node.id+"....please wait!!");
            //document.getElementById("log").innerHTML="</br>Clustering...."+node.id+"....please wait!!";
            /*$("#contents-loader").hide ("slow");            
		showHome ();
                $("#infovis").empty("slow");    
               // $("#_tooltip").empty("fast");
                document.getElementById('_tooltip').style.display = 'none';*/
         document.getElementById('reclus').style.display = 'block';
            reCluster(node.id,node.num);
        }
      }
    },

        onBeforeCompute: function(node){          
            document.getElementById('tab1').className="tabbertab";
            document.getElementById('Title').setAttribute("class","tabberactive");    
            document.getElementById('tab2').className="tabbertab tabbertabhide";
            document.getElementById('Details').setAttribute("class", "");
            document.getElementById("logs").style.display="block";
            Log.write("centering " + node.id + "...");
           //document.getElementById("log").innerHTML="</br>centering " + node.id + "...";
            //Add the relation list in the right column.
            //This list is taken from the data property of each JSON node.
           // $jit.id('inner-details').innerHTML = node.data.relation;
		   var html = "<h4>" + node.id ,
            list = [];
        //node.eachAdjacency(function(adj){
         // list.push(adj.nodeTo.name);
        //});
        //append connections information in the side panel
        
        
        list=extract(node.id)
        
                    $jit.id('inner-details').innerHTML = "</br>"+html + "</h4><b>"+(node.size)+" DOCUMENTS:</b>"+ list; 
               
       
        },
        
       
        //Add the name of the node in the correponding label
        //and a click handler to move the graph.
        //This method is called once, on label creation.
        onCreateLabel: function(domElement, node){
            domElement.innerHTML = node.id;
           // domElement.firstChild.appendChild(document.createTextNode(node.id));
            domElement.onclick = function(){
                rgraph.onClick(node.id, {
                    onComplete: function() {
                        document.getElementById("logs").style.display="block";
                        Log.write("done");
                        
                    }					
                });
            };
            domElement.onmouseover = function(){ 
                domElement.style.color="red"; 
                domElement.style['z-index']=5; 
            }; 
            domElement.onmouseout = function(){ 
                domElement.style.color="black"; 
                domElement.style['z-index']=0; 
            }; 
        },
        //Change some label dom properties.
        //This method is called each time a label is plotted.
        onPlaceLabel: function(domElement, node){
            
            var style = domElement.style;
            style.color="black";
            
            style.display = '';
            style.cursor = 'pointer';

            if (node._depth <= 1) {
                style.fontSize = "1.0em";
                style.color = "black";
            
            } else if(node._depth == 2){
                style.fontSize = "1.0em";
                style.color = "black";
            
            } else {
                style.fontSize = "1.0em";
                style.color = "black";
            }

            var left = parseInt(style.left);
            var w = domElement.offsetWidth;
            style.left = (left - w / 2) + 'px';
        }
    });
    //load JSON data
    rgraph.loadJSON(json);
    //trigger small animation
    rgraph.graph.eachNode(function(n) {
      var pos = n.getPos();
      pos.setc(-200, -200);
    });
    rgraph.compute('end');
    rgraph.fx.animate({
      modes:['polar'],
      duration: 2000
    });
    //end
    //append information about the root relations in the right column
    //$jit.id('inner-details').innerHTML = rgraph.graph.getNode(rgraph.root).data.relation;
	var html = "<h4> Query:  " + rgraph.graph.getNode(rgraph.root).id ;
            list = [];
        numbr=0;
        qry=rgraph.graph.getNode(rgraph.root).id;
        list=extract(rgraph.graph.getNode(rgraph.root).id);
      
        $jit.id('inner-details').innerHTML = ""+html + "</h4><b>"+rgraph.graph.getNode(rgraph.root).size+" DOCUMENTS:</b>"+ list;
}




