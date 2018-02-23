package ru.mail.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.mail.service.model.Order;

import javax.servlet.http.HttpSession;

/**
 * @author Denis Monich
 * this class getting request to and sending response from congratulation page
 */
@Controller
public class CongratulationController {

    private static final Logger logger = Logger.getLogger(CongratulationController.class);


    /**
     * send parameters to congratulation.jsp
     *
     * @param session http session where saved order
     * @return congratulation.jsp or redirect to coffeList
     */
    @RequestMapping(value = "/congratulation", method = RequestMethod.GET)
    public String getCongratulations(HttpSession session) {

        logger.debug("coming to congratulation servlet doGet");

        Order order = (Order) session.getAttribute("order");
        if (order != null) {

            logger.debug("redirect to coffeeList page");
            return "redirect:/coffeeList";

        } else {

            return "congratulation";
        }
    }
}
