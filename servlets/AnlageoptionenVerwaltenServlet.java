package stacked_bs.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stacked_bs.bean.AnlageOption;

//Jonathan Vielwerth

@WebServlet("/AnlageoptionenVerwaltenServlet")
public class AnlageoptionenVerwaltenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AnlageoptionenVerwaltenServlet() {
		// TODO Auto-generated constructor stub
	}

	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;

	private List<AnlageOption> AktienAuslesen() throws ServletException {

		List<AnlageOption> Aktien = new ArrayList<>();

		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement("SELECT aktien FROM thidb.aktien")) {
			try (ResultSet rs = pstmt.executeQuery()) {

				while (rs.next()) {
					AnlageOption Aktie = new AnlageOption();
					Aktie.setName(rs.getString("aktien"));

					Aktien.add(Aktie);
				}
			}
		} catch (Exception ex) {
			throw new ServletException(ex.getMessage());
		}

		return Aktien;
	}

	private List<AnlageOption> ETFsAuslesen() throws ServletException {

		List<AnlageOption> ETFs = new ArrayList<>();

		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement("SELECT etf FROM thidb.etf")) {
			try (ResultSet rs = pstmt.executeQuery()) {

				while (rs.next()) {
					AnlageOption ETF = new AnlageOption();
					ETF.setName(rs.getString("etf"));

					ETFs.add(ETF);
				}
			}
		} catch (Exception ex) {
			throw new ServletException(ex.getMessage());
		}

		return ETFs;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		List<AnlageOption> Aktien = AktienAuslesen();
		List<AnlageOption> ETFs = ETFsAuslesen();

		request.setAttribute("Aktien", Aktien);
		request.setAttribute("ETFs", ETFs);

		final RequestDispatcher dispatcher = request.getRequestDispatcher("Stacked/JSP/AnlageoptionenVerwalten.jsp");
		dispatcher.forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
//Jonathan Vielwerth
