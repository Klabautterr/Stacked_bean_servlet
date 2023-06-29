package stacked_bs.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import stacked_bs.bean.Kommentar;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import stacked_bs.bean.Login;
import stacked_bs.bean.Post;

/**
 * Servlet implementation class Kommentieren
 */
@WebServlet("/Kommentieren")
public class Kommentieren extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;
	
	
		private void ein_Kommentar(Kommentar einKommentar) throws ServletException {
    	
    	String[] DataInfo = new String[] {"id"};
    	
    	try (Connection con = ds.getConnection();
    			// schreibt die Daten in die DAtenbank 
    		PreparedStatement pstmt = con.prepareStatement(
    		"INSERT INTO thidb.Kommentare (post_id, kommentar, username) Values(?, ?, ?)", DataInfo)) {
    		pstmt.setLong(1, einKommentar.getPost_id());
    		pstmt.setString(2, einKommentar.getKommentar());
    		pstmt.setString(3, einKommentar.getUsername());
    		
    		//pstmt.setBinaryStream(4, filepart.getInputStream());
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
	
	
		

		session.setAttribute("formKommentar", formKommentar);
		ein_Kommentar(formKommentar);
		
		//Long id = Long.valueOf(request.getParameter("id"));
		
		//request.setAttribute("id", id);

		
		response.sendRedirect("Stacked/webapp/JSP/Kommentieren.jsp");

	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
