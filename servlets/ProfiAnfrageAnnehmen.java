package stacked_bs.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.sql.DataSource;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import stacked_bs.bean.Login;

// Jonathan Vielwerth 

@WebServlet("/ProfiAnfrageAnnehmen")
public class ProfiAnfrageAnnehmen extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;

	public ProfiAnfrageAnnehmen() {
		// TODO Auto-generated constructor stub
	}

	public void persist(String username) throws ServletException {
		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con
						.prepareStatement("Update thidb.user Set profi = true where username = ?")) {
			pstmt.setString(1, username);

			pstmt.executeUpdate();

		} catch (Exception ex) {
			throw new ServletException(ex.getMessage());
		}
		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con
						.prepareStatement("Update thidb.user Set offeneProfiAnfrage = false where username = ?")) {
			pstmt.setString(1, username);

			pstmt.executeUpdate();

		} catch (Exception ex) {
			throw new ServletException(ex.getMessage());
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		Login login = (Login) session.getAttribute("Login");
		login.setOffeneProfiAnfrage(false);

		String username = request.getParameter("username");

		persist(username);

		response.sendRedirect("./ProfiAnfragenAusgeben");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
//Jonathan Vielwerth 
