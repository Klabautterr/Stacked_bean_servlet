package stacked_bs.servlets;

import java.util.ArrayList;
import stacked_bs.bean.Login;
import stacked_bs.bean.Post;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

/**
 * Servlet implementation class Posten
 */
@WebServlet("/Posten")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5, 
				maxRequestSize = 1024 * 1024 * 5 * 5,
				location = "/tmp", 
				fileSizeThreshold = 1024 * 1024)
public class Posten extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;

    	private void ein_Post(Post formPost, Part filepart) throws ServletException {
    	
    	String[] DataInfo = new String[] {"id"};
    	
    	try (Connection con = ds.getConnection();
    			// schreibt die Daten in die DAtenbank 
    		PreparedStatement pstmt = con.prepareStatement(
    		"INSERT INTO post (nachricht, username, bildname, bild, anzahl_likes) Values(?, ?, ?, ?, 0)", DataInfo)) {
    		pstmt.setString(1, formPost.getNachricht());
    		pstmt.setString(2, formPost.getUsername());
    		pstmt.setString(3, formPost.getBildname());
    		pstmt.setBinaryStream(4, filepart.getInputStream());
    		pstmt.executeUpdate();
    	
    	try(ResultSet rs = pstmt.getGeneratedKeys()) {
    		while (rs.next()) {
    			formPost.setId(rs.getLong(1));
    		}	
    	}
    	}catch (Exception ex) {
    		throw new ServletException(ex.getMessage());
    	}
    }
    



	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		//zieht die daten aus der Login session
		HttpSession session = request.getSession();
		Login login = (Login) session.getAttribute("Login");
	
		
		//holt die Daten aus der JSP und erstellt eine object bean
		Post formPost = new Post();
		formPost.setUsername(login.getUsername());
		formPost.setNachricht(request.getParameter("nachricht"));
	
			
		Part filepart = request.getPart("bild");
		formPost.setBildname(filepart.getSubmittedFileName());
	
		

		session.setAttribute("formPost", formPost);
		ein_Post(formPost, filepart);


		
		response.sendRedirect("./AllePostsAusgeben");

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
