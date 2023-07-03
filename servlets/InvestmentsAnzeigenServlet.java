package stacked_bs.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import stacked_bs.bean.ETF;
import stacked_bs.bean.Login;
import stacked_bs.bean.Stock;

//Tobias Weiß

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

	private int getAllAssets(String username) throws SQLException {

		int result = 0;

		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con
						.prepareStatement("SELECT SUM(anzahl) as anzahl FROM thidb.investments WHERE username = ?")) {
			pstmt.setString(1, username);
			try (ResultSet res = pstmt.executeQuery()) {
				if (res.next()) {
					result = res.getInt("anzahl");
				}
			}
		}
		return result;
	}

	private double getAllETFs(String username) throws SQLException {

		ETF etfs = new ETF();
		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(
						"SELECT SUM(anzahl) as anzahlETF FROM thidb.investments WHERE username = ? AND etf = 1")) {

			pstmt.setString(1, username);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					if (rs.getInt("anzahlETF") != 0) {

						
						
						int allStocks = getAllAssets(username);
						
						double AnzahlEtfs = rs.getInt("anzahlETF");
						double AnzahlEtf = AnzahlEtfs * 100/ allStocks ;
						double roundedAnzahlEtf = Math.round(AnzahlEtf * 100.0) / 100.0;
						etfs.setAnzahlEtf(roundedAnzahlEtf);

					} else {
						etfs.setAnzahlEtf(0.0);
					}

				}
			}
		}
		return etfs.getAnzahlEtf();
	}

	private double getAllStocks(String username) throws SQLException {
		Stock stock = new Stock();
		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(
						"SELECT SUM(anzahl) as anzahlStocks FROM thidb.investments WHERE username = ? AND etf = 0")) {
			pstmt.setString(1, username);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					if (rs.getInt("anzahlStocks") != 0) {
						int allStocks = getAllAssets(username);
						double AnzahlStocks = rs.getInt("anzahlStocks") * 100.0 / allStocks;
						System.out.println(AnzahlStocks);
						double roundedAnzahlStocks = Math.round(AnzahlStocks * 100.0) / 100.0;
						stock.setAnzahlStock(roundedAnzahlStocks);

					} else {
						stock.setAnzahlStock(0.0);
					}

				}
			}
		}
		return stock.getAnzahlStock();
	}

	//Tobias Weiß
	
	//Jonathan Vielwerth
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
	// Jonathan Vielwerth

	
	//doGet Zusammenfürhung der Funktionen von Jonathan Vielwerth & Tobias Weiß
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

				request.setAttribute("anzahlEtf", getAllETFs(username));
				request.setAttribute("AnzahlStocks", getAllStocks(username));
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
