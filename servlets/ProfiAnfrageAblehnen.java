package stacked_bs.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ProfiAnfrageAblehnen
 */
@WebServlet("/ProfiAnfrageAblehnen")
public class ProfiAnfrageAblehnen extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;
    /**
     * Default constructor. 
     */
    public ProfiAnfrageAblehnen() {
        // TODO Auto-generated constructor stub
    }
    
    public void persist(String username) throws ServletException {
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
