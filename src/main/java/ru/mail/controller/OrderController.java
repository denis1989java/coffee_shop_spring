package ru.mail.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.service.OrderService;
import ru.mail.service.model.Order;

import javax.servlet.http.HttpSession;


/**
 * @author Denis Monich
 * this class getting request to and sending response from order page
 */
@Controller
public class OrderController {

    private final OrderService orderDAO;
    private static final Logger logger = Logger.getLogger(OrderController.class);

    @Autowired
    public OrderController(OrderService orderDAO) {
        this.orderDAO = orderDAO;
    }


    /**
     * @param model   include all parametrs which will be shown on admin.jsp
     * @param session http session where saved order
     * @return order.jsp or returm coffeList
     */
    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public String getOrder(Model model, HttpSession session) {

        logger.debug("coming to orderServlet doGet");
        Order item = (Order) session.getAttribute("order");
        logger.debug("getting order details");

        if (item == null || item.getOrderItemList().isEmpty()) {
            logger.debug("redirect to coffeeList page");
            return "redirect:/coffeeList";

        } else {

            model.addAttribute("order", item);
            return "order";

        }
    }

    /**
     * @param toSendQuantity array with quantities of coffees
     * @param toSendCoffeeId array with ids of coffee
     * @param delete         paramert which show that user whant to be on order page
     * @param back           paramert which show that user whant to be on coffees page
     * @param session        http session where saved order
     * @return redirect to order or coffeList or confirmOrder
     */
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public String confirmOrder(@RequestParam(value = "toSendQuantity", required = false) String toSendQuantity,
                               @RequestParam(value = "toSendCoffeeId", required = false) String toSendCoffeeId,
                               @RequestParam(value = "delete", required = false) String delete,
                               @RequestParam(value = "goBack", required = false) String back,
                               HttpSession session) {

        logger.debug("coming to orderServlet doPost");

        //checking is request parameters not null
        if (toSendQuantity != null && toSendCoffeeId != null) {

            //length of parameters must be same
            Order order = (Order) session.getAttribute("order");
            logger.debug("updating of order by Id");

            //putting order to basket and getting Id of this order
            order = orderDAO.updateOrder(order, toSendQuantity, toSendCoffeeId);
            logger.debug("redirect to confirmOrder page");
            session.setAttribute("order", order);

            if (!delete.equals("")) {
                return "redirect:/order";
            } else if (!back.equals("")) {
                return "redirect:/coffeeList";
            } else {
                return "redirect:/confirmOrder";
            }

        } else {

            logger.debug("redirect to coffeeList page");
            return "redirect:/coffeeList";
        }
    }

}
