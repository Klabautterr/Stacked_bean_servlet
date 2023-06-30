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

/**
 * Servlet implementation class AnlageoptionLoeschen
 */
@WebServlet("/AnlageoptionLoeschen")
public class AnlageoptionLoeschen extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;
    /**
     * Default constructor. 
     */
    public AnlageoptionLoeschen() {
        // TODO Auto-generated constructor stub
    }
    
    public void aktieLoeschen(String AktienName)throws ServletException{
    	try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(
						"DELETE from thidb.aktien WHERE aktien = ?")) {
			pstmt.setString(1, AktienName);

			pstmt.executeUpdate();

		} catch (Exception ex) {
			throw new ServletException(ex.getMessage());
		}
    }
    
    public void etfLoeschen(String ETFName)throws ServletException{
    	try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(
						"DELETE from thidb.etf WHERE etf = ?")) {
			pstmt.setString(1, ETFName);

			pstmt.executeUpdate();

		} catch (Exception ex) {
			throw new ServletException(ex.getMessage());
		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String AktienName = request.getParameter("AktieLoeschen");
		String ETFName = request.getParameter("ETFLoeschen");
		
		if(!(AktienName == null)) {
			aktieLoeschen(AktienName);
		} else {
			etfLoeschen(ETFName);
		}
		response.sendRedirect("./AnlageoptionenVerwaltenServlet");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
