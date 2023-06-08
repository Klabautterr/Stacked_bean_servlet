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
import stacked_bs.bean.Post;

/**
 * Servlet implementation class AllePostsAusgeben
 */
@WebServlet("/AllePostsAusgeben")
public class AllePostsAusgeben extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;
	
	
	private List<Post> search(Long id) throws ServletException {

	    List<Post> posts = new ArrayList<>();

	    try (Connection con = ds.getConnection();
	         PreparedStatement pstmt = con.prepareStatement("SELECT * FROM post ORDER BY id DESC LIMIT ?")) {

	        pstmt.setLong(1, id);
	        try (ResultSet rs = pstmt.executeQuery()) {

	            while (rs.next()) {
	                Post post = new Post();
	                post.setId(Long.valueOf(rs.getLong("id")));
	                post.setUsername(rs.getString("username"));
	                post.setNachricht(rs.getString("nachricht"));
	                
	       
	                posts.add(post);
	            } 
	        }
	    } catch (Exception ex) {
	        throw new ServletException(ex.getMessage());
	    }
	    
	    return posts;
	}




	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");   
		
		// DB-Zugriff
		List<Post> posts = search(3L);
		            
		// Scope "Request"
		request.setAttribute("posts", posts);
		    
		// Weiterleiten an JSP
		//response.sendRedirect("Stacked/JSP/FeedPosts.jsp");
		final RequestDispatcher dispatcher = request.getRequestDispatcher("Stacked/JSP/FeedPosts.jsp");
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

