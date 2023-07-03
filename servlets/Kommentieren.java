package stacked_bs.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


import javax.sql.DataSource;

import stacked_bs.bean.Kommentar;
import jakarta.annotation.Resource;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import stacked_bs.bean.Login;
import stacked_bs.bean.Post;
import stacked_bs.bean.Kommentar;

/**
 * Servlet implementation class Kommentieren
 */
//Jan Holtmann & Linus Baumeister

@WebServlet("/Kommentieren")
public class Kommentieren extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;
	
	
		private void ein_Kommentar(Kommentar einKommentar) throws ServletException {
    	
    	String[] DataInfo = new String[] {"id"};
    	
    	try (Connection con = ds.getConnection();
    			// schreibt die Daten in die Datenbank 
    		PreparedStatement pstmt = con.prepareStatement(
    		"INSERT INTO thidb.Kommentare (post_id, kommentar, username) Values(?, ?, ?)", DataInfo)) {
    		pstmt.setLong(1, einKommentar.getPost_id());
    		pstmt.setString(2, einKommentar.getKommentar());
    		pstmt.setString(3, einKommentar.getUsername());
    		
    		
    		pstmt.executeUpdate();
    	
    	try(ResultSet rs = pstmt.getGeneratedKeys()) {
    		while (rs.next()) {
    			einKommentar.setId(rs.getLong(1));
    		}	
    	}
    	}catch (Exception ex) {
    		throw new ServletException(ex.getMessage());
    	}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		//zieht die daten aus der Login session
		HttpSession session = request.getSession();
		Login login = (Login) session.getAttribute("Login");
	
		
		//holt die Daten aus der JSP und erstellt eine object bean
		Kommentar formKommentar = new Kommentar();
		formKommentar.setUsername(login.getUsername());
		formKommentar.setKommentar(request.getParameter("kommentar"));
		formKommentar.setPost_id(Integer.valueOf(request.getParameter("id")));
	
		List<Kommentar> postedComment = new ArrayList<>();
		postedComment.add(formKommentar);
		

		request.setAttribute("comments", postedComment);
		ein_Kommentar(formKommentar);
		
		

		final RequestDispatcher dispatcher;
		dispatcher = request.getRequestDispatcher("Stacked/JSP/CommentToJSON.jsp");
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
//Jan Holtmann & Linus Baumeister