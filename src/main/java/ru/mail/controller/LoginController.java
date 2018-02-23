package ru.mail.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.service.UserService;

import javax.servlet.http.HttpSession;

/**
 * @author Denis Monich
 * this class getting request to and sending response from login page
 */
@Controller
public class LoginController {

    //getting access to service methods
    private final UserService userDAO;
    private static final Logger logger = Logger.getLogger(LoginController.class);

    @Autowired
    public LoginController(UserService userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * @param session http session where saved order
     * @return login.jsp or redirect to admin
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage(HttpSession session) {

        logger.debug("getting all orders");
        //getting all orders
        logger.debug("coming to LoginServlet doGet");

        String user = (String) session.getAttribute("user");

        if (user != null) {

            return "redirect:/admin";
        } else {

            return "login";
        }

    }

    /**
     * @param login    user's login
     * @param password user's password
     * @param session  http session where saved order
     * @return redirect to login or admin
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String postAdminPage(@RequestParam(value = "userEmail", required = false) String login,
                                @RequestParam(value = "userPassword", required = false) String password,
                                HttpSession session) {

        logger.debug("changing order's delivery status by order Id");

        if (userDAO.isValidUser(login, password)) {

            String validUser = "admin";
            session.setAttribute("user", validUser);
            logger.debug("redirect to admin page");
            return "redirect:/admin";

        } else {

            //sending json about invalid user
            logger.debug("redirect to login page");
            return "redirect:/login";

        }
    }
}
