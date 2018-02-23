package ru.mail.controller;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.service.impl.DaoFactory;
import ru.mail.service.OrderService;
import ru.mail.service.model.Coffee;
import ru.mail.service.model.Order;
import ru.mail.service.model.OrderItem;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Denis Monich
 * this class getting request to and sending response from coffeeList page
 */
@Controller
public class CoffeeListController {

    private static final Logger logger = Logger.getLogger(CoffeeListController.class);
    private final DaoFactory daoFactory;
    private final OrderService orderService;


    @Autowired
    public CoffeeListController(DaoFactory daoFactory, OrderService orderService) {
        this.daoFactory = daoFactory;
        this.orderService = orderService;
    }

    /**
     * send parameters to coffees.jsp
     *
     * @param model   include all parametrs which will be shown on cofees.jsp
     * @param session http session where saved order
     * @return coffees.jsp
     */
    @RequestMapping(value = {"/", "/coffeeList"}, method = RequestMethod.GET)
    public String showCoffes(Model model, HttpSession session) {
        logger.debug("coming to CoffeeListServlet doGet");
        logger.debug("getting list of all coffees");

        List<Coffee> coffees = daoFactory.getCoffeeDao().list();
        Order curOrder = (Order) session.getAttribute("order");

        if (curOrder != null) {
            int length = 0;
            for (OrderItem orderItem : curOrder.getOrderItemList()) {
                length = length + orderItem.getQuantity();
            }
            model.addAttribute("length", length);
            model.addAttribute("order", curOrder);
        }

        model.addAttribute("coffees", coffees);

        return "coffees";
    }

    /**
     * get parameters from coffees.jsp
     *
     * @param toSendQuantity array with quantities of coffees
     * @param toSendCoffeeId array with ids of coffee
     * @param added          paramert which show that user whant to be on coffees page
     * @param session        http session where saved order
     * @return redirect to coffeeList or order
     */
    @RequestMapping(value = {"/", "/coffeeList"}, method = RequestMethod.POST)
    public String coffes(@RequestParam(value = "toSendQuantity", required = false) String toSendQuantity,
                         @RequestParam(value = "toSendCoffeeId", required = false) String toSendCoffeeId,
                         @RequestParam(value = "added", required = false) String added,
                         HttpSession session) {

        logger.debug("coming to CoffeeListServlet doPost");
        logger.debug("putting the order to basket");

        Order curOrder = (Order) session.getAttribute("order");

        //putting order to basket and getting Id of this order
        Order order = orderService.putToBasket(curOrder, toSendQuantity, toSendCoffeeId);

        //putting order id to session
        session.setAttribute("order", order);

        logger.debug("redirect to order page");

        if (!added.equals("")) {
            return "redirect:/coffeeList";
        } else {
            return "redirect:/order";
        }
    }

}
