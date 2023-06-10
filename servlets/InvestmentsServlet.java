package stacked_bs.servlets;

import jakarta.servlet.http.HttpServlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
				PreparedStatement pstmt = con.prepareStatement("INSERT INTO thidb.investments (username,stockname, anzahl, buyin) VALUES(?,?,?,?)")) {
			pstmt.setString(1, assets.getUsername());
			pstmt.setString(2, assets.getStockname());
			pstmt.setInt(3, assets.getAnzahl());
			 pstmt.setInt(4, assets.getBuyin());
			pstmt.executeUpdate();

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
		
		session.setAttribute("Assets", assets);
		persist(assets);

		response.sendRedirect("Stacked/JSP/Profil.jsp");

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
