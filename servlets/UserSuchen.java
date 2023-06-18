package stacked_bs.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import stacked_bs.bean.Login;
import stacked_bs.bean.Profi;
import stacked_bs.bean.User;
/**
 * Servlet implementation class FreundeSuchen
 */
@WebServlet("/UserSuchen")
public class UserSuchen extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public UserSuchen() {
        // TODO Auto-generated constructor stub
    }
	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	
	private List<User> userSuchen(String username) throws ServletException {

	    List<User> userSuche = new ArrayList<>();

	    try (Connection con = ds.getConnection();
	         PreparedStatement pstmt = con.prepareStatement("SELECT username FROM thidb.user WHERE username Like ?")) {
		
	    	pstmt.setString(1,"%" + username + "%");

	        try (ResultSet rs = pstmt.executeQuery()) {

	            while (rs.next()) {
	                User user = new User();
	                user.setUsername(rs.getString("username"));

	                userSuche.add(user);
	            } 
	        }
	    } catch (Exception ex) {
	        throw new ServletException(ex.getMessage());
	    }
	    
	    return userSuche;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		String username = request.getParameter("username");
		// DB-Zugriff
		List<User> userSuche = userSuchen(username);
		            
		// Scope "Request"
		request.setAttribute("userSuche", userSuche);

		// Weiterleiten an JSP
		final RequestDispatcher dispatcher = request.getRequestDispatcher("./FollowsVerwalten");
		dispatcher.forward(request, response);  
		}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
