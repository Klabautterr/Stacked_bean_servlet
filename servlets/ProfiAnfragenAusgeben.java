package stacked_bs.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import stacked_bs.bean.Profi;

// Jonathan Vielwerth

@WebServlet("/ProfiAnfragenAusgeben")
public class ProfiAnfragenAusgeben extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ProfiAnfragenAusgeben() {
		// TODO Auto-generated constructor stub
	}

	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;

	private List<Profi> ProfiAnfragenAuslesen() throws ServletException {

		List<Profi> profiAnfragen = new ArrayList<>();

		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(
						"SELECT username, passwort FROM thidb.user WHERE offeneProfiAnfrage = true")) {
			try (ResultSet rs = pstmt.executeQuery()) {

				while (rs.next()) {
					Profi profiAnfrage = new Profi();
					profiAnfrage.setUsername(rs.getString("username"));

					profiAnfragen.add(profiAnfrage);
				}
			}
		} catch (Exception ex) {
			throw new ServletException(ex.getMessage());
		}

		return profiAnfragen;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		List<Profi> profiAnfragen = ProfiAnfragenAuslesen();

		request.setAttribute("profiAnfragen", profiAnfragen);

		final RequestDispatcher dispatcher = request.getRequestDispatcher("Stacked/JSP/ProfiAnfragen.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
// Jonathan Vielwerth
