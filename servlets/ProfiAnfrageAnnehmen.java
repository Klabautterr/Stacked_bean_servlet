package stacked_bs.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;
import stacked_bs.bean.Profi;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
// Jonathan 
/**
 * Servlet implementation class ProfilanfrageAnnehmen
 */
@WebServlet("/ProfiAnfrageAnnehmen")
public class ProfiAnfrageAnnehmen extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;
    /**
     * Default constructor. 
     */
    public void persist(String username) throws ServletException {
    	try(Connection con = ds.getConnection();
    			PreparedStatement pstmt = con.prepareStatement("Update thidb.user Set profi = true where username = ?")){
    		pstmt.setString(1, username);
    		
    		pstmt.executeUpdate();

    		
    	}catch (Exception ex) {
    		throw new ServletException(ex.getMessage());
    	}
    	try(Connection con = ds.getConnection();
    			PreparedStatement pstmt = con.prepareStatement("Update thidb.user Set offeneProfiAnfrage = false where username = ?")){
    		pstmt.setString(1, username);
    		
    		pstmt.executeUpdate();

    		
    	}catch (Exception ex) {
    		throw new ServletException(ex.getMessage());
    	}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		String username = request.getParameter("username");
		
		persist(username);

		response.sendRedirect("./ProfiAnfragenAusgeben");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
//Jonathan