package stacked_bs.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import jakarta.servlet.http.HttpSession;
import stacked_bs.bean.Assets;
import stacked_bs.bean.Login;


/**
 * Servlet implementation class InvestmentsAnzeigenServlet
 */
@WebServlet("/InvestmentsAnzeigenServlet")
public class InvestmentsAnzeigenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public InvestmentsAnzeigenServlet() {
		// TODO Auto-generated constructor stub
	}

	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;

	private List<Assets> search(String username) throws ServletException, SQLException {

		List<Assets> assets = new ArrayList<Assets>();

		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement("SELECT * FROM investments WHERE BINARY username = ?")) {

			pstmt.setString(1, username);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Assets asset = new Assets();
					asset.setUsername(rs.getString("username"));
					asset.setStockname(rs.getString("stockname"));
					asset.setAnzahl(Integer.valueOf(rs.getInt("anzahl")));
					asset.setBuyin(Integer.valueOf(rs.getInt("buyin")));

					assets.add(asset);
				}
			} catch (Exception e) {
				throw new ServletException(e.getMessage());
			}
		}

		return assets;
	}

//<<<<<<< Updated upstream
	private boolean Profiueberpruefen(String username) throws SQLException {
		boolean ifProfi = false;

		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con
						.prepareStatement("SELECT profi FROM thidb.user WHERE BINARY username = ? AND profi = true")) {

			pstmt.setString(1, username);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					if (rs.getString("profi").equals("1")) {
						ifProfi = true;
					} else {
						ifProfi = false;
					}
				}
			}

		}
		return ifProfi;
	}

	private boolean Adminueberpruefen(String username) throws ServletException, SQLException {
		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con
						.prepareStatement("SELECT * FROM user Where BINARY username = ? AND admin = true")) {

			pstmt.setString(1, username);

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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession();
		Login login = (Login) session.getAttribute("Login");
		String username = login.getUsername();
		String FollowUser = request.getParameter("username");
		request.setAttribute("FollowUser", FollowUser);

		try {

			if (FollowUser != null) {
				List<Assets> assetsAnzeigen = search(FollowUser);

				request.setAttribute("AssetsAnzeigen", assetsAnzeigen);
				final RequestDispatcher dispatcher = request.getRequestDispatcher("Stacked/JSP/FollowProfil.jsp");
				dispatcher.forward(request, response);
			} else if (Adminueberpruefen(username)) {
				response.sendRedirect("./Stacked/JSP/Admin.jsp");
			} else if (Profiueberpruefen(login.getUsername()) == true) {
				List<Assets> assetsAnzeigen = search(username);

				request.setAttribute("AssetsAnzeigen", assetsAnzeigen);
				login.setIsProfi(true);
				final RequestDispatcher dispatcher = request.getRequestDispatcher("Stacked/JSP/Profi.jsp");
				dispatcher.forward(request, response);
				return;
			} else {
				List<Assets> assetsAnzeigen = search(username);

				request.setAttribute("AssetsAnzeigen", assetsAnzeigen);
				login.setIsProfi(false);
				final RequestDispatcher dispatcher = request.getRequestDispatcher("Stacked/JSP/Profil.jsp");
				dispatcher.forward(request, response);
				return;
			}
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
