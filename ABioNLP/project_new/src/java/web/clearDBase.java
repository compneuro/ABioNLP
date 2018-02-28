/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author rohit
 */
@WebServlet(name = "clearDBase", urlPatterns = {"/clearDBase"})
public class clearDBase extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
           Connection con = null;
        PreparedStatement pst = null;
        
                
        String url = "jdbc:mysql://localhost:3306/hippo";
        String user = "root";
        String password = "pass";
        

        try {
            System.err.println("REST1.JAVA!!!");
            Class.forName("com.mysql.jdbc.Driver").newInstance(); 
            con = DriverManager.getConnection(url, user, password);
            //pst = con.prepareStatement("select t.title,t.abstract,c.name from test t,clusters c WHERE c.clusid=t.cluster_no");
            pst=con.prepareStatement("delete from test");
            pst.execute();
            pst.close();
            
            pst=con.prepareStatement("delete from clusters");
            pst.execute();
            pst.close();
           
        }
        catch(Exception e)
        {
            System.err.println(e);
        }       
         
       //------------------------------------------------------------------------------  
        
          
      String cypherUri ="http://localhost:7474/db/data/cypher";

        String cypherStatement="{\"query\" : \"START n=node(*) MATCH n-[r?]->() DELETE r WHERE ID(n)<>0 DELETE n,r; \",\"params\" : {}}";

        WebResource resource = Client.create()
                .resource( cypherUri );
        ClientResponse response1 = resource.accept( MediaType.APPLICATION_JSON_TYPE )
                .type( MediaType.APPLICATION_JSON_TYPE )
                .entity( cypherStatement )
                .post( ClientResponse.class );
        
        String cypherUri1 ="http://localhost:7474/db/data/cypher";
        } finally {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
