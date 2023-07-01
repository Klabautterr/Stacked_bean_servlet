package stacked_bs.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import javax.sql.DataSource;

import jakarta.servlet.RequestDispatcher;


import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import stacked_bs.bean.Post;

/**
 * Servlet implementation class EinPostAusgeben
 */
@WebServlet("/EinPostAusgeben")
public class EinPostAusgeben extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;
	
    private Post search(Long id) throws ServletException {
    	
    	Post post = new Post();

  	    try (Connection con = ds.getConnection();
  	         PreparedStatement pstmt = con.prepareStatement("SELECT * FROM post WHERE id = ?")) {

  	        pstmt.setLong(1, id);
  	        
  	        try (ResultSet rs = pstmt.executeQuery()) {

  	            while (rs.next()) {
  	                post = new Post();
  	                post.setId(Long.valueOf(rs.getLong("id")));
  	                post.setUsername(rs.getString("username"));
  	                post.setNachricht(rs.getString("nachricht"));
  	                post.setAnzahl_likes(rs.getInt("anzahl_likes"));
  	                post.setBildname(rs.getString("bildname"));
  	       
  	               
  	            } 
  	        }
  	    } catch (Exception ex) {
  	        throw new ServletException(ex.getMessage());
  	    }
  	    
  	    return post;
  	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");   
		HttpSession session = request.getSession();
	

		
		Long id = Long.valueOf(request.getParameter("id"));
		
		Post post  = search(id); 

		//session.setAttribute("post", post);
		request.setAttribute("post", post);
		


		String ajaxLike = "0";
		if (request.getParameter("ajaxLike") != null) {
			ajaxLike = (String) request.getParameter("ajaxLike");
		}
		
		final RequestDispatcher dispatcher;
		
		if ("1".equals(ajaxLike)){
			dispatcher = request.getRequestDispatcher("Stacked/JSP/LikeJSON.jsp");
	     
		}else {
	        dispatcher = request.getRequestDispatcher("Stacked/JSP/Kommentieren.jsp");
	       
	    }
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
