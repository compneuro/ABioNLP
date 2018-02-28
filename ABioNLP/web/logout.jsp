<%-- 
    Document   : logout
    Created on : Aug 15, 2013, 7:24:29 PM
    Author     : unnikrishnans
--%>

<%@page import="database_manipulator.drop_tables"%>
<%@page import="database_manipulator.database"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <!--<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">-->
        <meta http-equiv="refresh" content="1;URL='index.jsp'">
        <title>JSP Page</title>
    </head>
    <body>
       <%
           database con= new database();
           String uname=(String)session.getAttribute("username");
           System.out.println("LOOK HERE!!! - "+uname);
           drop_tables dt= new drop_tables(con,uname);
           dt.final_drop_tables();
            session.removeAttribute("username");
            session.removeAttribute("password");
            session.invalidate();
            response.setHeader("Cache-Control", "no-cache"); //Forces caches to obtain a new copy of the page from the origin server  
            response.setHeader("Cache-Control", "no-store"); //Directs caches not to store the page under any circumstance  
            response.setDateHeader("Expires", 0); //Causes the proxy cache to see the page as "stale"  
            response.setHeader("Pragma", "no-cache"); //HTTP 1.0 backward compatibility
       %>
            <h1>logout successfull</h1>
            
    </body>
</html>
