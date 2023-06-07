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
import stacked_bs.bean.Registrierung;
// Jonathan Vielwerth
/**
 * Servlet implementation class RegistrierungsanfragenAusgeben
 */
@WebServlet("/RegistrierungsanfragenAusgeben")
public class RegistrierungsanfragenAusgeben extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public RegistrierungsanfragenAusgeben() {
        // TODO Auto-generated constructor stub
    }

	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	
	private List<Registrierung> registrierungsanfragenAusgeben() throws ServletException {

	    List<Registrierung> registrierungsanfragen = new ArrayList<>();

	    try (Connection con = ds.getConnection();
	         PreparedStatement pstmt = con.prepareStatement("SELECT username, passwort FROM thidb.user WHERE offeneRegistrierungsanfrage = true")) {
// UserImage muss noch hinzugefügt werden
	        try (ResultSet rs = pstmt.executeQuery()) {

	            while (rs.next()) {
	                Registrierung registrierung = new Registrierung();
	                registrierung.setUsername(rs.getString("username"));
	                registrierung.setPasswort(rs.getString("passwort"));

	                registrierungsanfragen.add(registrierung);
	            } 
	        }
	    } catch (Exception ex) {
	        throw new ServletException(ex.getMessage());
	    }
	    
	    return registrierungsanfragen;
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");   // In diesem Format erwartet das Servlet jetzt die Formulardaten
		    
		// DB-Zugriff
		List<Registrierung> registrierungsanfragen = registrierungsanfragenAusgeben();
		            
		// Scope "Request"
		request.setAttribute("registrierungsanfragen", registrierungsanfragen);
		    
		// Weiterleiten an JSP
		final RequestDispatcher dispatcher = request.getRequestDispatcher("Stacked/JSP/Registrierungsanfragen.jsp");
		dispatcher.forward(request, response);  
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
