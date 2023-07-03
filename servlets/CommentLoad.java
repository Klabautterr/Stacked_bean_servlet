package stacked_bs.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Resource;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import stacked_bs.bean.Login;
import stacked_bs.bean.Kommentar;
import javax.sql.DataSource;

/**
 * Servlet implementation class CommentLoad
 */
@WebServlet("/CommentLoad")
public class CommentLoad extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
	@Resource(lookup = "java:jboss/datasources/MySqlThidbDS")
	private DataSource ds;
	
    private List<Kommentar> LoadComment(long postID, long amount, long offset) throws ServletException {
        List<Kommentar> coms = new ArrayList<>();
        
        try (Connection con = ds.getConnection();
   	         PreparedStatement pstmt = con.prepareStatement("SELECT * FROM thidb.kommentare WHERE post_id = ? ORDER BY id DESC LIMIT ?, ?")) {

        	pstmt.setLong(1, postID);
   	        pstmt.setLong(2, offset);
   	        pstmt.setLong(3, amount);
   	        try (ResultSet rs = pstmt.executeQuery()) {

   	            while (rs.next()) {
   	                Kommentar com = new Kommentar();
   	                com.setId(Long.valueOf(rs.getLong("id")));
   	                com.setUsername(rs.getString("username"));
   	                com.setKommentar(rs.getString("kommentar"));
   	                com.setPost_id(rs.getInt("post_id"));   	                
   	       
   	                coms.add(com);
   	            } 
   	        }
   	    } catch (Exception ex) {
   	        throw new ServletException(ex.getMessage());
   	    }
        
        return coms;
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");  
		
		HttpSession session = request.getSession();
		Login login = (Login) session.getAttribute("Login");
		
		String loginUsername = login.getUsername();
		request.setAttribute("loginUsername", loginUsername);

		
		Long postID = Long.valueOf(request.getParameter("postID"));
		Long alrLoadedComments = Long.valueOf(request.getParameter("loadedComments"));

		List<Kommentar> comments = LoadComment(postID, 5L, alrLoadedComments);
		System.out.println(comments);
		request.setAttribute("comments", comments);
		
		final RequestDispatcher dispatcher;
		dispatcher = request.getRequestDispatcher("Stacked/JSP/CommentToJSON.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
