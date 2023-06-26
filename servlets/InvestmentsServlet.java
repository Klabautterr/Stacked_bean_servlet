package stacked_bs.servlets;

import jakarta.servlet.http.HttpServlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import stacked_bs.bean.Assets;
import stacked_bs.bean.Login;

/**
 * Tobias Weiß -> Hier ist noch nix geschehen wird es aber bald
 */
@WebServlet("/InvestmentsServlet")
public class InvestmentsServlet extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InvestmentsServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;

	private void persist(Assets assets) throws ServletException {

		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(
						"INSERT INTO thidb.investments (username,stockname, anzahl, buyin) VALUES(?,?,?,?)")) {
			pstmt.setString(1, assets.getUsername());
			pstmt.setString(2, assets.getStockname());
			pstmt.setInt(3, assets.getAnzahl());
			pstmt.setInt(4, assets.getBuyin());
			pstmt.executeUpdate();

		} catch (SQLException e) {

			throw new ServletException(e.getMessage());
		}
	}

//	private boolean duplicateAssets(String stockname) throws SQLException, ServletException {
//
//		List<Assets> duplicate = new ArrayList<Assets>();
//
//		try (Connection con = ds.getConnection();
//				PreparedStatement pstmt = con.prepareStatement(
//						"SELECT id, username, stockname FROM investments WHERE BINARY stockname ? AND username = ?")) {
//			try (ResultSet rs = pstmt.executeQuery()) {
//				while (rs.next()) {
//					Assets doubleAssets = new Assets();
//					doubleAssets.setStockname(rs.getString("stockname"));
//					doubleAssets.setUsername(rs.getString("username"));
//
//					duplicate.add(doubleAssets);
//				}
//
//			} catch (Exception e) {
//				throw new ServletException(e.getMessage());
//			}
//		}
//		boolean ifDuplicate = false;
//		for (Assets asset : duplicate) {
//			if (duplicate.contains(asset)) {
//				ifDuplicate = true;
//			} else {
//				ifDuplicate = false;
//			}
//		}
//		return ifDuplicate;
//	}

	private boolean duplicateAssets(Assets assets) throws SQLException, ServletException { // von ChatGPT

		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(
						"SELECT * FROM investments WHERE BINARY stockname = ? AND BINARY username = ?")) {

			pstmt.setString(1, assets.getStockname());
			pstmt.setString(2, assets.getUsername());
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {

					return true;
				} else {
					return false;

				}

			}
		} catch (Exception e) {
			throw new ServletException(e.getMessage());
		}
	}

	private void updateInvestments(Assets assets) throws ServletException {
		
		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(
						"UPDATE thidb.investments SET  anzahl = anzahl + ? , buyin = ? WHERE  stockname = ? AND username = ?")) {

			pstmt.setInt(1, assets.getAnzahl());
			pstmt.setInt(2, assets.getBuyin());
			pstmt.setString(3, assets.getStockname());
			pstmt.setString(4, assets.getUsername());

			pstmt.execute();
		} catch (SQLException e) {
			throw new ServletException(e.getMessage());
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
		Assets assets = new Assets();
		Login login = (Login) session.getAttribute("Login");

		assets.setUsername(login.getUsername());
		assets.setStockname(request.getParameter("stockname"));
		assets.setAnzahl(Integer.valueOf(request.getParameter("amountOfStock")));
		assets.setBuyin(Integer.valueOf(request.getParameter("buyIn")));

		

		try {
			if (duplicateAssets(assets) == false) {
				session.setAttribute("Assets", assets);
				persist(assets);

				response.sendRedirect("./InvestmentsAnzeigenServlet");
			} else {
				session.setAttribute("Assets", assets);

				updateInvestments(assets);
				response.sendRedirect("./InvestmentsAnzeigenServlet");

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
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
