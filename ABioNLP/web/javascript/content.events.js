var queryapp;
var query1;


function buttonClickEventHandle (id,queryterm1) {
    
    query1="<input type=\"checkbox\" class=\"checkq\" name=\""+queryterm1+"\" value=\""+queryterm1+"\">"+queryterm1;
  // alert(queryterm1);
   //alert(query1);
	document.getElementById("check").innerHTML="<form name=\"checkbox\" id=\"checkform\" action=\"home.jsp\" method=\"post\">"+query1+
                                                                                "<input type=\"submit\" class=\"button\" name=\"Submit\" onclick=\"checkclick();\"></form>";
	if (id == "Home1") {
            //document.getElementById('contents-loader').style.display = 'none';
            document.getElementById('Home1').style.display = 'none';
          $("#contents-loader").hide ("slow");            
		showHome ();
                $("#infovis").empty("slow");
		return;
	}
	if (pane_position == 2) {
		//$("#div-ticker").slideUp("slow");
		//$("#div-left-floater-links").hide ("slow");
		//$("#div-right-floater-links").hide ("slow");
                //$('#contents-loader').load('index.jsp #contents-loader').fadeIn("slow");
                document.getElementById('Home1').style.display = 'block';
                
                  
		$("#left-container").animate({width:"300px"},1200,"easeOutSine",function () {
			pane_position = 1;
			//$("#div-nav-buttons").fadeOut ("fast");
			load_semaphore = 1;
			animateOnLoad ();
                        //$("#contents-loader").("slow");
                        
                      document.getElementById('contents-loader').style.display = 'block';
                        init();
                        
			//var send_data = addMultipartFormdata ("frm_req", btn_ref);
			//ajaxDataLoad (send_data);
		});
         
		return;
	}

	pane_position = 1;
	//$("#div-nav-buttons").fadeOut ("fast");
	load_semaphore = 1;
	animateOnLoad ();
	//var send_data = addMultipartFormdata ("frm_req", btn_ref);
	//ajaxDataLoad (send_data);
}
