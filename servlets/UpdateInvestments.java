package stacked_bs.servlets;

import jakarta.servlet.http.HttpServlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
 * Servlet implementation class UpdateInvestments
 */
@WebServlet("/UpdateInvestments")
public class UpdateInvestments extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;
       
	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateInvestments() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private void updateInvestments(Assets assets) throws  ServletException {
    	
    	try(Connection con = ds.getConnection();
    			PreparedStatement pstmt = con.prepareStatement(
    					"UPDATE thidb.investments SET username = ?, anzahl = ?, buyin = ? WHERE Binary stockname = ?")){
    		pstmt.setString(1, assets.getUsername());
    		pstmt.setInt(2, assets.getAnzahl());
    		pstmt.setInt(3, assets.getBuyin());
    		pstmt.setString(4, assets.getStockname());
    		
    		pstmt.execute();
    	} catch(SQLException e) {
    		throw new ServletException(e.getMessage());
    	}
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		Assets assets = new Assets();
		Login username = (Login) session.getAttribute("Login");
		
		assets.setUsername(username.getUsername());
		assets.setAnzahl(Integer.valueOf(request.getParameter("amountOfStock")));
		assets.setBuyin(Integer.valueOf((request.getParameter("buyIn"))));
		assets.setUsername(request.getParameter("stockname"));
		
		session.setAttribute("Assets", assets);
		updateInvestments(assets);
		
		response.sendRedirect("./InvestmentAnzeigenServlet");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
