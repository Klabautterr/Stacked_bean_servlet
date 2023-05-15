package stacked_bs.servlets;

import stacked_bs.bean.Post;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


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

    	private void persist(Post form, Part filepart) throws ServletException {
    	
    	String[] DataInfo = new String[] {"id"};
    	
    	try (Connection con = ds.getConnection();
    			
    		PreparedStatement pstmt = con.prepareStatement(
    		"INSERT INTO post (nachricht, bildname, bild) Values(?, ?, ?)", DataInfo)) {
    		pstmt.setString(1, form.getNachricht());
    		pstmt.setString(2, form.getBildname());
    		pstmt.setBinaryStream(3, filepart.getInputStream());
    		pstmt.executeUpdate();
    	
    	try(ResultSet rs = pstmt.getGeneratedKeys()) {
    		while (rs.next()) {
    			form.setId(rs.getLong(1));
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

		Post form = new Post();
		//form.setUsername(request.getParameter("username"));
		form.setNachricht(request.getParameter("nachricht"));
		// form.setLink(request.getParameter("linke"));
		form.setBildname(request.getParameter("bildname"));
		
		
		Part filepart = request.getPart("bild");
		form.setBildname(filepart.getSubmittedFileName());
	

		HttpSession session = request.getSession();
		session.setAttribute("form", form);
		persist(form, filepart);
		response.sendRedirect("Stacked/JSP/OutputFeed.jsp");

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
