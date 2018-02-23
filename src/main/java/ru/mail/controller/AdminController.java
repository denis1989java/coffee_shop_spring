package ru.mail.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.service.impl.DaoFactory;
import ru.mail.service.model.Order;

import java.util.List;


/**
 * @author Denis Monich
 * this class getting request to and sending response from admin page
 */
@Controller
public class AdminController {

    private static final Logger logger = Logger.getLogger(AdminController.class);
    private final DaoFactory daoFactory;

    @Autowired
    public AdminController(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * @param model include all parametrs which will be shown on admin.jsp
     * @return admin.jsp
     */
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String getAdminPage(Model model) {
        logger.debug("getting all orders");

        //getting all orders
        List<Order> orders = daoFactory.getOrderDao().list();
        model.addAttribute("orders", orders);

        return "admin";
    }

    /**
     * for updating order delivery status
     *
     * @param orderId order's id
     * @return redirect to admin page
     */
    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    public String postAdminPage(@RequestParam(value = "orderId", required = false) String orderId) {
        logger.debug("changing order's delivery status by order Id");

        //changing order's delivery status by order Id
        daoFactory.getOrderDao().update(Long.valueOf(orderId));
        logger.debug("redirect to admin page");

        //redirect to admin page
        return "redirect:/admin";
    }
}
