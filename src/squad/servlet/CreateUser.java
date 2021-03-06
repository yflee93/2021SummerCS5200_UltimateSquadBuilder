package squad.servlet;

import squad.dal.*;
import squad.model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class CreateUser extends HttpServlet {
	
	protected UsersDao usersDao;
	
	@Override
	public void init() throws ServletException {
		usersDao = UsersDao.getInstance();
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Map for storing messages.
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);
        //Just render the JSP.   
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
	}
	
	@Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
        // Map for storing messages.
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        // Retrieve and validate name.
        String userName = req.getParameter("username");
        if (userName == null || userName.trim().isEmpty()) {
            messages.put("success", "Invalid UserName");
        } else {
        	// Create the User.
        	String password = req.getParameter("password");
        	String encode_password = CryptoUtil.encode(password);
        	String fistname = req.getParameter("firstname");
        	String lastname = req.getParameter("lastname");
        	String email = req.getParameter("email");
        	
	        try {
	        	Users user = new Users(userName, encode_password);
	        	if(fistname != null && fistname != "") {
	        		user.setFirstName(fistname);
	        	}
	        	if(lastname != null && lastname != "") {
	        		user.setLastName(lastname);
	        	}
	        	if(email != null && email != "") {
	        		user.setEmail(email);
	        	}

	        	user.setBudget(10000000);

	        	user = usersDao.insert(user);
	        	messages.put("success", "Successfully created " + userName);
	        } catch (SQLException e) {
				e.printStackTrace();
				throw new IOException(e);
	        }
        }
        
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }
}
