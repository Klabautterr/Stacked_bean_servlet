package stacked_bs.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import stacked_bs.bean.Login;

/**
 * Servlet implementation class Liken
 */
@WebServlet("/Liken")
public class Liken extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;

	private void liken(Long id, String username) throws ServletException {
		// TODO Auto-generated constructor stub

		try (Connection con = ds.getConnection();
				// schauen ob der Benutzer den Post schon liked hat
				PreparedStatement pstmt = con
						.prepareStatement("SELECT * FROM benutzer_like WHERE BINARY username = ? AND post_id = ?")) {
			pstmt.setString(1, username);
			pstmt.setLong(2, id);

			ResultSet rs = pstmt.executeQuery();

			if (rs != null && rs.next()) {
				// falls schon geliked wurde wird der like verweis und like gelöscht
				PreparedStatement lo_Pstmt = con
						.prepareStatement("DELETE FROM benutzer_like WHERE BINARY username = ? AND post_id = ?");
				lo_Pstmt.setString(1, username);
				lo_Pstmt.setLong(2, id);
				lo_Pstmt.executeUpdate();

				PreparedStatement up_Pstmt = con
						.prepareStatement("UPDATE thidb.post SET anzahl_likes = anzahl_likes - 1 WHERE id = ?");
				up_Pstmt.setLong(1, id);
				up_Pstmt.executeUpdate();

			} else {
				//wurde noch nicht geliked
				
				PreparedStatement in_Pstmt = con
						.prepareStatement("INSERT INTO benutzer_like (username, post_id) Values (?, ?)");
				in_Pstmt.setString(1, username);
				in_Pstmt.setLong(2, id);
				in_Pstmt.executeUpdate();
				
				PreparedStatement up_Pstmt = con
						.prepareStatement("UPDATE thidb.post SET anzahl_likes = anzahl_likes + 1 WHERE id = ?");
				up_Pstmt.setLong(1, id);
				up_Pstmt.executeUpdate();

			}

		} catch (Exception ex) {
			throw new ServletException(ex.getMessage());
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		Login login = (Login) session.getAttribute("Login");

		Long id = Long.valueOf(request.getParameter("id"));
		String username = login.getUsername();
		
	//	String 
		
		liken(id, username);
			
		
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
