package stacked_bs.servlets;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import stacked_bs.bean.Registrierung;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

/**
 * Servlet implementation class RegistrierungServlet
 */
// Jonathan Vielwerth

@WebServlet("/RegistrierungServlet")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5,
				 maxRequestSize = 1024 * 1024 * 5 * 5,
				 location="C:\\wildfly-preview-26.1.3.Final\\standalone\\tmp\\Stacked-war.war\\tmp\\",
				 fileSizeThreshold = 1024* 1024)
public class RegistrierungServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;
	
    /**
     * Default constructor. 
     */
    public RegistrierungServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    private void persist(Registrierung form, Part filepart) throws ServletException{
    	try(Connection con = ds.getConnection();
    			PreparedStatement pstmt = con.prepareStatement("INSERT INTO user (username,passwort,userImage) Values(?,?,?)")){
    		pstmt.setString(1, form.getUsername());
    		pstmt.setString(2, form.getPasswort());
    		pstmt.setBinaryStream(3, filepart.getInputStream());;
    		pstmt.executeUpdate();
    		
    	}catch (Exception ex) {	
    		throw new ServletException(ex.getMessage());
    	}
    }
    
	private boolean Benutzernameueberpruefen(Registrierung form) throws ServletException, SQLException {
		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement("SELECT * FROM user Where BINARY username = ?")) {

			pstmt.setString(1, form.getUsername());

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return false;
				} else {
					return true;
				}
			}
		} catch (Exception ex) {
			throw new ServletException(ex.getMessage());
		}

	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Registrierung form = new Registrierung();
		form.setUsername(request.getParameter("username"));
		form.setPasswort(request.getParameter("passwort"));
		Part filepart = request.getPart("userImage");
		
		HttpSession session = request.getSession();
		session.setAttribute("Registrierung", form);
		


		
		try {
			if (Benutzernameueberpruefen(form)) {
				persist(form,filepart);
				response.sendRedirect("Stacked/Index.html");;
			} else {
					response.sendRedirect("Stacked/JSP/BenutzernameVorhanden.jsp");
				}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
// Jonathan Vielwerth

