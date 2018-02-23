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

import javax.servlet.http.HttpSession;

/**
 * @author Denis Monich
 * this class getting request to and sending response from confirmOrder page
 */
@Controller
public class ConfirmOrderController {

    private final DaoFactory daoFactory;
    private static final Logger logger = Logger.getLogger(ConfirmOrderController.class);

    @Autowired
    public ConfirmOrderController(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * @param model   include all parametrs which will be shown on confirmOrder.jsp
     * @param session http session where saved order
     * @param error   will show message about failure regex
     * @return confirmOrder.jsp
     */
    @RequestMapping(value = "/confirmOrder", method = RequestMethod.GET)
    public String getOrder(Model model, HttpSession session, @RequestParam(value = "address", required = false) String error) {
        Order order = (Order) session.getAttribute("order");

        if (order == null) {

            logger.debug("redirect to coffeeList page");
            return "redirect:/coffeeList";
        } else {

            logger.debug("getting order details by order's id");
            if (order.getOrderItemList().isEmpty()) {

                logger.debug("redirect to coffeeList page");
                return "redirect:/coffeeList";
            } else {

                model.addAttribute("order", order);

                if (error != null) {

                    error = "Address format is not correct. Should be: Minsk, Sharangovicha 33-92";
                    model.addAttribute("error", error);
                }

                return "confirmOrder";
            }
        }
    }

    /**
     * @param name    name of customer
     * @param address address of customer
     * @param phone   phone number of customer
     * @param session http session where saved order
     * @return to confirmOrder or coffeeListor or congratulation
     */
    @RequestMapping(value = "/confirmOrder", method = RequestMethod.POST)
    public String confirmOrder(@RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "address", required = false) String address,
                               @RequestParam(value = "phone", required = false) String phone,
                               HttpSession session) {

        logger.debug("coming to ConfirmOrderServlet doPost");

        Order order = (Order) session.getAttribute("order");

        if (order == null) {
            logger.debug("redirect to /coffeeList");
            return "redirect:/coffeeList";

        } else {

            //checking is request parameters not null
            if (address != null) {

                if (address.trim().equals("")) {

                    return "redirect:/confirmOrder?address=error";

                } else {

                    if (name != null && phone != null) {

                        logger.debug("confirming order");
                        //confirming order
                        order.setUserName(name);
                        order.setAddress(address);
                        order.setPhoneNumber(phone);
                        daoFactory.getOrderDao().save(order);
                        //cleaning of session
                        session.setAttribute("order", null);
                        logger.debug("redirect to congratulation page");
                        return "redirect:/congratulation";

                    } else {

                        return "redirect:/confirmOrder";

                    }
                }
            } else {

                return "redirect:/confirmOrder";
            }
        }
    }
}
