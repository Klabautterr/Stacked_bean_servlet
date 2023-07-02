package stacked_bs.servlets;

import stacked_bs.bean.AnlageOption;
import stacked_bs.bean.Registrierung;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class AnlageoptionHinzufuegenServlet
 */
@WebServlet("/AnlageoptionHinzufuegenServlet")
public class AnlageoptionHinzufuegenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;

	/**
	 * Default constructor.
	 */
	public AnlageoptionHinzufuegenServlet() {
		// TODO Auto-generated constructor stub
	}

	private void persistAktie(AnlageOption Aktie) throws ServletException {
		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(
						"INSERT INTO thidb.aktien (aktien, preis, branche, country) VALUES (?,?,?,?)")) {
			pstmt.setString(1, Aktie.getName());
			pstmt.setInt(2, Aktie.getPreis());
			pstmt.setString(3, Aktie.getBranche());
			pstmt.setString(4, Aktie.getLand());
			pstmt.executeUpdate();

		} catch (Exception ex) {
			throw new ServletException(ex.getMessage());
		}
	}

	private void persistETF(AnlageOption ETF) throws ServletException {
		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement("INSERT INTO thidb.etf (etf, preis) VALUES (?, ?);")) {
			pstmt.setString(1, ETF.getName());
			pstmt.setInt(2, ETF.getPreis());
			pstmt.executeUpdate();

		} catch (Exception ex) {
			throw new ServletException(ex.getMessage());
		}
	}

	private boolean AktieSchonVorhanden(AnlageOption Aktie) throws ServletException, SQLException {
		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement("SELECT * FROM thidb.aktien Where aktien = ?")) {

			pstmt.setString(1, Aktie.getName());

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
	
	private boolean ETFSchonVorhanden(AnlageOption ETF) throws ServletException, SQLException {
		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement("SELECT * FROM thidb.etf Where etf = ?")) {

			pstmt.setString(1, ETF.getName());

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
		String aktienBT = request.getParameter("aktienBT");

		if (aktienBT.equals("gedrueckt")) {
			AnlageOption Aktie = new AnlageOption();
			Aktie.setLand(request.getParameter("land"));
			Aktie.setBranche(request.getParameter("branche"));
			Aktie.setName(request.getParameter("aktienName"));
			Aktie.setPreis(Integer.valueOf(request.getParameter("aktienPreis")));

			try {
				if (AktieSchonVorhanden(Aktie)) {
					response.sendRedirect("./Stacked/JSP/AnlageoptionSchonVorhanden.jsp");
				} else {
					persistAktie(Aktie);
					response.sendRedirect("./Stacked/JSP/AnlageoptionHinzufuegen.jsp");
				}
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			AnlageOption ETF = new AnlageOption();
			ETF.setName(request.getParameter("etfName"));
			ETF.setPreis(Integer.valueOf(request.getParameter("etfPreis")));

			try {
				if (ETFSchonVorhanden(ETF)) {
					response.sendRedirect("./Stacked/JSP/AnlageoptionSchonVorhanden.jsp");
				} else {
					persistETF(ETF);
					response.sendRedirect("./Stacked/JSP/AnlageoptionHinzufuegen.jsp");
				}
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
