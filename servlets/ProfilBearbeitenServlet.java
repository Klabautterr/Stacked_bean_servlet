package stacked_bs.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import stacked_bs.bean.Login;
// Jonathan Vielwerth

/**
 * Servlet implementation class ProfilBearbeitenServlet
 */
@WebServlet("/ProfilBearbeitenServlet")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class ProfilBearbeitenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;

	/**
	 * Default constructor.
	 */
	public ProfilBearbeitenServlet() {
		// TODO Auto-generated constructor stub
	}

	private void update(Login form, Part filepart, String AlterUsername) throws ServletException, IOException {
		if (!form.getUsername().equals("") & !form.getPasswort().equals("") & filepart.getSize() != 0) {
			try (Connection con = ds.getConnection();
					PreparedStatement pstmt = con.prepareStatement(
							"UPDATE thidb.user SET username = ?, passwort = ?, userImage = ? Where username = ?")) {
				pstmt.setString(1, form.getUsername());
				pstmt.setString(2, form.getPasswort());
				pstmt.setBinaryStream(3, filepart.getInputStream());
				pstmt.setString(4, AlterUsername);

				pstmt.executeUpdate();

			} catch (Exception ex) {
				throw new ServletException(ex.getMessage());
			}

		}
		if (form.getUsername().equals("") & form.getPasswort().equals("") & filepart.getSize() != 0) {
			try (Connection con = ds.getConnection();
					PreparedStatement pstmt = con.prepareStatement("UPDATE thidb.user SET userImage = ? Where username = ?")) {
				pstmt.setBinaryStream(1, filepart.getInputStream());
				pstmt.setString(2, AlterUsername);
				
				pstmt.executeUpdate();

			} catch (Exception ex) {
				throw new ServletException(ex.getMessage());
			}

		}
		if (form.getUsername().equals("") & !form.getPasswort().equals("") & filepart.getSize() == 0) {
			try (Connection con = ds.getConnection();
					PreparedStatement pstmt = con.prepareStatement("UPDATE thidb.user SET passwort = ? Where username = ?")) {
				pstmt.setString(1, form.getPasswort());
				pstmt.setString(2, AlterUsername);

				pstmt.executeUpdate();

			} catch (Exception ex) {
				throw new ServletException(ex.getMessage());
			}
			

		}
		
		if (!form.getUsername().equals("") & form.getPasswort().equals("") & filepart.getSize() == 0) {
			try (Connection con = ds.getConnection();
					PreparedStatement pstmt = con.prepareStatement("UPDATE thidb.user SET username = ? Where username = ?")) {
				pstmt.setString(1, form.getUsername());
				pstmt.setString(2, AlterUsername);

				pstmt.executeUpdate();

			} catch (Exception ex) {
				throw new ServletException(ex.getMessage());
			}

		}
		
		if (!form.getUsername().equals("") & !form.getPasswort().equals("") & filepart.getSize() == 0) {
			try (Connection con = ds.getConnection();
					PreparedStatement pstmt = con.prepareStatement("UPDATE thidb.user SET username = ?, passwort = ? Where username = ?")) {
				pstmt.setString(1, form.getUsername());
				pstmt.setString(2, form.getPasswort());
				pstmt.setString(3, AlterUsername);

				pstmt.executeUpdate();

			} catch (Exception ex) {
				throw new ServletException(ex.getMessage());
			}

		}

		if (!form.getUsername().equals("") & form.getPasswort().equals("") & filepart.getSize() != 0) {
			try (Connection con = ds.getConnection();
					PreparedStatement pstmt = con.prepareStatement("UPDATE thidb.user SET username = ?, userImage = ? Where username = ?")) {
				pstmt.setString(1, form.getUsername());
				pstmt.setBinaryStream(2, filepart.getInputStream());
				;
				pstmt.setString(3, AlterUsername);

				pstmt.executeUpdate();

			} catch (Exception ex) {
				throw new ServletException(ex.getMessage());
			}

		}
		if (form.getUsername().equals("") & !form.getPasswort().equals("") & filepart.getSize() != 0) {
			try (Connection con = ds.getConnection();
					PreparedStatement pstmt = con.prepareStatement("UPDATE user SET passwort = ?, userImage = ? Where username = ?")) {
				pstmt.setString(1, form.getPasswort());
				pstmt.setBinaryStream(2, filepart.getInputStream());
				;
				pstmt.setString(3, AlterUsername);

				pstmt.executeUpdate();

			} catch (Exception ex) {
				throw new ServletException(ex.getMessage());
			}

		}
	}

	private boolean Benutzernameueberpruefen(Login form) throws ServletException, SQLException {
		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement("SELECT * FROM user Where BINARY username = ?")) {

			pstmt.setString(1, form.getUsername());

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
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();

		Login login = (Login) session.getAttribute("Login");
		String AlterUsername = login.getUsername();
		Login form = new Login();
		form.setUsername(request.getParameter("NeuerUsername"));
		
			
		try {
			if(Benutzernameueberpruefen(form)){
				response.sendRedirect("Stacked/JSP/BearbeitungNichtMoeglich.jsp");	
				form.setUsername(AlterUsername);
				session.setAttribute("Login", form);
			}
				
		} catch (ServletException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(form.getUsername().equals("")){
			form.setUsername(AlterUsername);
		}
		
		form.setPasswort(request.getParameter("passwort"));
		Part filepart = request.getPart("NewImage");

		session.setAttribute("Login", form);
		update(form, filepart, AlterUsername);

		response.sendRedirect("./InvestmentsAnzeigenServlet");

		
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
// Jonathan Vielwerth
