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

//Jonathan Vielwerth

@WebServlet("/FollowEntfernen")
public class FollowEntfernen extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;

	/**
	 * Default constructor.
	 */
	public FollowEntfernen() {
		// TODO Auto-generated constructor stub
	}

	public void followEntfernen(String eingeloggterNutzer, String andererNutzer) throws ServletException {
		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(
						"DELETE from thidb.follow WHERE binary username1 = ? AND binary username2 = ?")) {
			pstmt.setString(1, eingeloggterNutzer);
			pstmt.setString(2, andererNutzer);

			pstmt.executeUpdate();

		} catch (Exception ex) {
			throw new ServletException(ex.getMessage());
		}
	}

	public void followerEntfernen(String eingeloggterNutzer, String andererNutzer) throws ServletException {
		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(
						"DELETE from thidb.follow WHERE binary username1 = ? AND binary username2 = ?")) {
			pstmt.setString(1, andererNutzer);
			pstmt.setString(2, eingeloggterNutzer);

			pstmt.executeUpdate();

		} catch (Exception ex) {
			throw new ServletException(ex.getMessage());
		}
	}

	public String getUsername1(String eingeloggterNutzer, String andererNutzer) throws ServletException {

		String username = null;
		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con
						.prepareStatement("SELECT username1 FROM thidb.follow WHERE username2 = ? AND username1 = ?")) {
			pstmt.setString(1, eingeloggterNutzer);
			pstmt.setString(2, andererNutzer);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					username = rs.getString("username1");
				}
			}
		} catch (Exception ex) {
			throw new ServletException(ex.getMessage());
		}

		return username;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		Login login = (Login) session.getAttribute("Login");
		String followsButton = request.getParameter("followsButton");
		String followerButton = request.getParameter("followerButton");
		String eingeloggterNutzer = login.getUsername();

		if (followerButton == null) {
			String andererNutzer = followsButton;
			followEntfernen(eingeloggterNutzer, andererNutzer);
		} else {
			String andererNutzer = followerButton;
			followerEntfernen(eingeloggterNutzer, andererNutzer);
		}

		response.sendRedirect("./FollowsVerwalten");
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
//Jonathan Vielwerth
