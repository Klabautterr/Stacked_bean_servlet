package stacked.Stacked_bean_servlet.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

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
import stacked.Stacked_bean_servlet.bean.Login;

/**
 * Servlet implementation class ProfilBearbeitenServlet
 */
@WebServlet("/ProfilBearbeitenServlet")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5,
				 maxRequestSize = 1024 * 1024 * 5 * 5)
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
    
    private void update(Login form, Part filepart, String AlterUsername) throws ServletException{
    	try(Connection con = ds.getConnection();
    			PreparedStatement pstmt = con.prepareStatement("UPDATE user SET username = ?, password = ?, userImage = ? Where username = ?")){
    		pstmt.setString(1, form.getUsername());
    		pstmt.setString(2, form.getPasswort());
    		pstmt.setBinaryStream(3, filepart.getInputStream());;
    		pstmt.setString(4, AlterUsername);

    		pstmt.executeUpdate();
    		
    	}catch (Exception ex) {
    		throw new ServletException(ex.getMessage());
    	}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		String AlterUsername = request.getParameter("AlterUsername");
		Login form = new Login();
		form.setUsername(request.getParameter("NeuerUsername"));
		form.setPasswort(request.getParameter("passwort"));
		Part filepart = request.getPart("NewImage");

		HttpSession session = request.getSession();
		session.setAttribute("Login", form);	
		update(form,filepart,AlterUsername);
		response.sendRedirect("Stacked/JSP/ProfilBearbeiten.jsp");}
	
	// Muss ich noch verbessern - Mit if Statements das mann auch nur ein einzelnes ändern könnte. 

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
