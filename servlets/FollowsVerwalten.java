package stacked_bs.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.sql.DataSource;

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

@WebServlet("/FollowsVerwalten")
public class FollowsVerwalten extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public FollowsVerwalten() {
		// TODO Auto-generated constructor stub
	}

	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;

	private List<User> followsAuslesen(String user) throws ServletException {

		List<User> follows = new ArrayList<>();

		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con
						.prepareStatement("SELECT username2 FROM thidb.follow Where username1 = ?")) {
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

	private Map<String, List<User>> followerAuslesen(String user) throws ServletException {

		Map<String, List<User>> followerMap = new HashMap<>();
		List<User> followerOhneFollow = new ArrayList<>();
		List<User> followerMitFollow = new ArrayList<>();

		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con
						.prepareStatement("SELECT username1 FROM thidb.follow Where BINARY username2 = ?")) {
			pstmt.setString(1, user);

			try (ResultSet rs = pstmt.executeQuery()) {

				while (rs.next()) {
					if (schonFolgen(rs.getString("username1"), user)) {
						User aktuelleFollowerOhneFollow = new User();
						aktuelleFollowerOhneFollow.setUsername(rs.getString("username1"));

						followerOhneFollow.add(aktuelleFollowerOhneFollow);
					} else {
						User aktuelleFollowerMitFollow = new User();
						aktuelleFollowerMitFollow.setUsername(rs.getString("username1"));
						followerMitFollow.add(aktuelleFollowerMitFollow);
					}

				}
			}

		} catch (Exception ex) {
			throw new ServletException(ex.getMessage());
		}
		followerMap.put("followerOhneFollow", followerOhneFollow);
		followerMap.put("followerMitFollow", followerMitFollow);

		return followerMap;
	}

	private boolean schonFolgen(String username, String user) throws ServletException, SQLException {
		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con
						.prepareStatement("SELECT * FROM follow Where BINARY username1 = ? AND BINARY username2 = ?")) {
			pstmt.setString(1, user);
			pstmt.setString(2, username);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception ex) {
			throw new ServletException(ex.getMessage());
		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		Login login = (Login) session.getAttribute("Login");
		String user = login.getUsername();

		List<User> follows = followsAuslesen(user);

		Map<String, List<User>> followerMap = followerAuslesen(user);

		List<User> followerOhneFollow = followerMap.get("followerOhneFollow");
		List<User> followerMitFollow = followerMap.get("followerMitFollow");

		request.setAttribute("follows", follows);
		request.setAttribute("followerOhneFollow", followerOhneFollow);
		request.setAttribute("followerMitFollow", followerMitFollow);

		final RequestDispatcher dispatcher = request.getRequestDispatcher("Stacked/JSP/Freunde.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
// Jonathan Vielwerth
