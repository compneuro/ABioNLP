<%-- 
    Document   : login
    Created on : Aug 15, 2013, 5:14:42 PM
    Author     : UNNIKRISHNAN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head> <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
        <link type="text/css" href="css/base.css" rel="stylesheet" />
        <link type="text/css" href="css/ForceDirected.css" rel="stylesheet" />
        <link rel="stylesheet" href="css/example.css" TYPE="text/css" MEDIA="screen">
        <title>NeuCluster</title> 
    </head>
    
    <body> 
        <%
           // String username;
            //String password;
            session.removeAttribute("username");
            session.removeAttribute("password");
            session.invalidate();
        %>
    <center> 
        <img src="automated.png" width="1100px" height="140px">        
       <img src="system.png" width="800px" height="140px">        
        <br>
        <form action="LoginCheck.jsp" method="post">
            <div id="signin" align="center">
                
                <br/>Username:<input type="text" name="username"> 
            <br/>Password:<input type="password" name="password"> 
            <br/><input type="submit" class="button">
            </div>
        </form>
    </center> 
</body> 
</html>


