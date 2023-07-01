package stacked_bs.servlets;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stacked_bs.bean.AnlageOption;
import stacked_bs.bean.Assets;

/**
 * Servlet implementation class diversifizierungsServlet
 */
@WebServlet("/diversifizierungsServlet")
public class diversifizierungsServlet extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public diversifizierungsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
 private double calculateAssetValue(int numbOfStocks, int price) {
    	
    	int assetValue = numbOfStocks * price;
    	
    	return assetValue;
    }

   private double calculatePortfolioValue(List<Assets> assets, AnlageOption anlageOption) {
	  int portfolioValue = 0;
	  
	  for(Assets asset : assets) {
		  portfolioValue += calculateAssetValue(asset.getAnzahl(), anlageOption.getPreis()); 
	  }
	  
	  return portfolioValue;
   }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
