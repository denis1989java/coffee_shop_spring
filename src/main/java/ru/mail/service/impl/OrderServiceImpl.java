package ru.mail.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.mail.service.OrderService;
import ru.mail.service.model.Coffee;
import ru.mail.service.model.Order;
import ru.mail.service.model.OrderItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Denis Monich
 * this class realise all logic of work with orders while it is't confirmed
 */
@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = Logger.getLogger(OrderServiceImpl.class);


    private final DaoFactory daoFactory;
    private Long ORDERID = 0L;

    public OrderServiceImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }


    /**
     * apdate orders parameters
     *
     * @param order        which will be updated
     * @param sendQuantity array of orders items quantities
     * @param sendCoffeeId array of coffees id which must be in order
     * @return updated order
     */
    @Override
    public Order updateOrder(Order order, String sendQuantity, String sendCoffeeId) {
        logger.debug("updating order");

        String[] toSendCoffeeId = sendCoffeeId.split(",");
        String[] toSendQuantity = sendQuantity.split(",");
        BigDecimal price = BigDecimal.ZERO;
        List<OrderItem> items = order != null ? order.getOrderItemList() : null;
        List<OrderItem> newList = new ArrayList<>();

        for (int i = 0; i < toSendCoffeeId.length; i++) {

            for (OrderItem item : items) {

                if (item.getCoffee().getId().toString().equals(toSendCoffeeId[i])) {
                    OrderItem orderItem = new OrderItem(item.getCoffee(), order, Integer.parseInt(toSendQuantity[i]));
                    newList.add(orderItem);
                    price = price.add(orderItem.getCoffee().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
                }
            }
        }
        if (order != null) {

            logger.debug("setting new coffee list and price to order");
            //setting new coffee list and price to order
            order.setOrderItemList(newList);
            order.setPrice(price);
        }
        return order;
    }

    /**
     * @param ord          order which will be updated
     * @param sendQuantity array of orders items quantities
     * @param sendCoffeeId array of coffees id which must be in order
     * @return updated order
     */
    @Override
    public Order putToBasket(Order ord, String sendQuantity, String sendCoffeeId) {
        logger.debug("putting order to basket");
        List<Coffee> coffees = daoFactory.getCoffeeDao().list();
        String[] toSendCoffeeId = sendCoffeeId.split(",");
        String[] toSendQuantity = sendQuantity.split(",");
        List<OrderItem> items = new ArrayList<>();
        BigDecimal price = BigDecimal.ZERO;

        Order order = new Order(ORDERID, items, price);

        for (int i = 0; i < toSendCoffeeId.length; i++) {
            String aToSendCoffeeId = toSendCoffeeId[i];
            for (Coffee coffee : coffees) {
                if (coffee.getId().toString().equals(aToSendCoffeeId)) {
                    OrderItem orderItem = new OrderItem(coffee, order, Integer.parseInt(toSendQuantity[i]));
                    items.add(orderItem);
                    price = price.add(coffee.getPrice().multiply(BigDecimal.valueOf(Long.parseLong(toSendQuantity[i]))));
                }
            }
        }

        if (ord == null) {
            ORDERID++;
            order.setPrice(price);
            return order;
        } else {
            for (OrderItem item : items) {
                boolean notNew = true;
                for (OrderItem orderItem : ord.getOrderItemList()) {
                    if (item.getCoffee().getId().equals(orderItem.getCoffee().getId())) {
                        Integer newQuant = item.getQuantity() + orderItem.getQuantity();
                        orderItem.setQuantity(newQuant);
                        notNew = false;
                        break;
                    }
                }
                if (notNew) {
                    ord.getOrderItemList().add(item);
                }
            }

            BigDecimal newPrice = BigDecimal.ZERO;

            for (OrderItem orderItem : ord.getOrderItemList()) {
                newPrice = newPrice.add(orderItem.getCoffee().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
            }
            ord.setPrice(newPrice);

            return ord;
        }
    }
}
