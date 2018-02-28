<%-- 
    Document   : LoginCheck
    Created on : Aug 15, 2013, 5:15:56 PM
    Author     : UNNIKRISHNAN
--%>

<%@page import="database_manipulator.create_tables"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="database_manipulator.database"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            try{
           
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        database con = new database();
        PreparedStatement ps= con.con.prepareStatement("Select password from user_details where user_name = '"+username+"'");
        ResultSet rs= ps.executeQuery();
        String retr_pass="";
        while(rs.next())
        {
            retr_pass=rs.getString(1);
        }
        if(password.equals(retr_pass))
            {
            System.out.println("INSIDEINSIDE");
            ps=con.con.prepareStatement("Select id from session_manager where session_id = '"+username+"'");
            rs=ps.executeQuery();
            int id=0;
            while(rs.next())
            {
                id=rs.getInt(1);
            }
            if(id==0)
            {
                ps=con.con.prepareStatement("Insert into session_manager (session_id) values(?)");
                ps.setString(1,username);
                ps.executeUpdate();
            }
            create_tables ct=new create_tables(con,username);
            ct.create_initial_tables();
            
            con.close();
            session.setAttribute("username",username);
            System.out.println("username1 = "+session.getAttribute("username"));
            response.sendRedirect("home.jsp");
            }
        else
            response.sendRedirect("error.jsp");
            }
            catch(Exception e)
            {
                System.err.println("LoginCheck.jsp - "+e);
            }
        %>
    </body>
</html>


