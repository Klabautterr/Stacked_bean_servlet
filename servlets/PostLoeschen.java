package stacked_bs.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import stacked_bs.bean.Login;
import stacked_bs.bean.Post;
import javax.sql.DataSource;

import jakarta.annotation.Resource;

/**
 * Servlet implementation class PostLoeschen
 */
@WebServlet(name = "/PostLoeschen", urlPatterns = { "/PostLoeschen" })
public class PostLoeschen extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;

	private void loeschen(Long id, String username) throws ServletException {
		try (Connection con = ds.getConnection()) {

			try (PreparedStatement pstmt = con
					.prepareStatement("DELETE FROM thidb.post WHERE id = ? AND BINARY username = ?")) {
				pstmt.setLong(1, id);
				pstmt.setString(2, username);
				pstmt.executeUpdate();
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

		
			loeschen(id, username);
			

			//session.setAttribute("formPost", null);

			// sendet an das ausgaben Servelt 
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
