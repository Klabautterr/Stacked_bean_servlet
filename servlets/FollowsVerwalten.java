package stacked_bs.servlets;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;import java.io.IOException;

import jakarta.annotation.Resource;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import stacked_bs.bean.User;
import stacked_bs.bean.Login;

//Jonathan Vielwerth

/**
 * Servlet implementation class FreundeAusgeben
 */
@WebServlet("/FollowsVerwalten")
public class FollowsVerwalten extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public FollowsVerwalten() {
        // TODO Auto-generated constructor stub
    }

    @Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	
	private List<User> followsAuslesen(String user) throws ServletException {

	    List<User> follows = new ArrayList<>();

	    try (Connection con = ds.getConnection();
	         PreparedStatement pstmt = con.prepareStatement("SELECT username2 FROM thidb.follow Where username1 = ?")) {
    		pstmt.setString(1, user);

	        try (ResultSet rs = pstmt.executeQuery()) {

	            while (rs.next()) {
	                User aktuelleFollows = new User();
	                aktuelleFollows.setUsername(rs.getString("username2"));

	                follows.add(aktuelleFollows);
	            } 
	        }
	        
	    } catch (Exception ex) {
	        throw new ServletException(ex.getMessage());
	    }
	    return follows;
	}
	    private List<User> followerAuslesen (String user) throws ServletException{
	    	
		    List<User> follower = new ArrayList<>();

	    
 try (Connection con = ds.getConnection();
		         PreparedStatement pstmt = con.prepareStatement("SELECT username1 FROM thidb.follow Where username2 = ?")) {
	    		pstmt.setString(1, user);

		        try (ResultSet rs = pstmt.executeQuery()) {

		            while (rs.next()) {
		                User aktuelleFollower= new User();
		                aktuelleFollower.setUsername(rs.getString("username1"));

		                follower.add(aktuelleFollower);
		            } 
		        }
		        
		    } catch (Exception ex) {
		        throw new ServletException(ex.getMessage());
	    }
    return follower;
	    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");   // In diesem Format erwartet das Servlet jetzt die Formulardaten
		HttpSession session = request.getSession();
		Login login = (Login) session.getAttribute("Login");
		String user = login.getUsername();
		
		// DB-Zugriff
		List<User> follows = followsAuslesen(user);
		List<User> follower = followerAuslesen(user);
		            
		// Scope "Request"
		request.setAttribute("follows", follows);
		request.setAttribute("follower", follower);

		// Weiterleiten an JSP
		final RequestDispatcher dispatcher = request.getRequestDispatcher("Stacked/JSP/Freunde.jsp");
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
// Jonathan Vielwerth
