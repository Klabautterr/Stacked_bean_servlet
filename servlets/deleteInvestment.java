package stacked_bs.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import stacked_bs.bean.Login;

//Tobias Weiß

/**
 * Servlet implementation class deleteServlet
 */
@WebServlet("/deleteInvestment")
public class deleteInvestment extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */

	public deleteInvestment() {
		// TODO Auto-generated constructor stub
	}

	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;

	private void deleteInvestments(String stockname, String username) throws ServletException, SQLException {
		try (Connection con = ds.getConnection()) {
			try (PreparedStatement pstmt = con
					.prepareStatement("DELETE FROM thidb.investments WHERE stockname = ? AND username = ?")) {
				pstmt.setString(1, stockname);
				pstmt.setString(2, username);
				pstmt.execute();
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
		String stockname = request.getParameter("assetId");

		String username = login.getUsername();
		
		try {
			deleteInvestments(stockname, username);
			response.sendRedirect("./InvestmentsAnzeigenServlet");
		} catch (ServletException e) {

			e.printStackTrace();
		} catch (SQLException e) {
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
//Tobias Weiß