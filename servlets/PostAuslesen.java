package stacked_bs.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PostAuslesen
 */
@WebServlet("/PostAuslesen")
public class PostAuslesen extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
	@Resource(lookup="java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		Long id = Long.valueOf(request.getParameter("id"));
		
		try (Connection con = ds.getConnection();
				 PreparedStatement pstmt = con.prepareStatement("SELECT bild FROM thidb.post WHERE id = ?") ) {
				pstmt.setLong(1, id);
				try (ResultSet rs = pstmt.executeQuery()) {
				
					
					if (rs != null && rs.next()) {
						Blob bild = rs.getBlob("bild");
						
						response.reset();
						long length = bild.length();
						response.setHeader("Content-Length",String.valueOf(length));
						
						try (InputStream in = bild.getBinaryStream()) {
							final int bufferSize = 256;
							byte[] buffer = new byte[bufferSize];
							
							ServletOutputStream out = response.getOutputStream();
							while ((length = in.read(buffer)) != -1) {
								out.write(buffer,0,(int) length);
							}
							out.flush();
						}
					}
				}
			} catch (Exception ex) {
				throw new ServletException(ex.getMessage());
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
