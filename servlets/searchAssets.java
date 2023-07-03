package stacked_bs.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import stacked_bs.bean.Assets;


//Tobias Weiß

@WebServlet("/searchAssets")
public class searchAssets extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public searchAssets() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;
    
    private List<Assets> searchToRefillAssets() throws SQLException, ServletException { 

    	List<Assets> refillDropDown = new ArrayList<Assets>();
    	
		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(
						"SELECT * FROM thidb.aktien")) {
			try(ResultSet rs = pstmt.executeQuery()){
				while(rs.next()) {
					Assets asset = new Assets();
					asset.setStockname(rs.getString("aktien"));
					refillDropDown.add(asset);
				}
			}catch (Exception e) {
				throw new ServletException(e.getMessage());
			}
		}
		
		return refillDropDown;
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		try {
			List<Assets> refillDropDown = searchToRefillAssets();
			request.setAttribute("refillDropDown",refillDropDown);
			final RequestDispatcher dispatcher = request.getRequestDispatcher("Stacked/JSP/stocknameLoader.jsp");
			dispatcher.forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ServletException e) {
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
//Tobias Weiß