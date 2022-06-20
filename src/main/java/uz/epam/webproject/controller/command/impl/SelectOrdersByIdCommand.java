package uz.epam.webproject.controller.command.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uz.epam.webproject.controller.command.Command;
import uz.epam.webproject.controller.command.ParameterName;
import uz.epam.webproject.controller.command.Router;
import uz.epam.webproject.controller.command.exception.CommandException;
import uz.epam.webproject.entity.order.Order;
import uz.epam.webproject.entity.user.User;
import uz.epam.webproject.service.OrderService;
import uz.epam.webproject.service.UserService;
import uz.epam.webproject.service.exception.ServiceException;
import uz.epam.webproject.service.impl.OrderServiceImpl;
import uz.epam.webproject.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

public class SelectOrdersByIdCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        OrderService orderService = OrderServiceImpl.getInstance();
        UserService userService = UserServiceImpl.getInstance();
        HttpSession session = request.getSession();
        Router router;
        String username = (String) session.getAttribute(ParameterName.USERNAME);
        Optional<User> optionalUser;
        try {
            optionalUser = userService.findByLogin(username);
        } catch (ServiceException e) {
            logger.error("error in finding the user by login ", e);
            throw new CommandException(e);
        }
//        if (optionalUser.isEmpty()){
//            return new Router(ParameterName.SIDEBAR_PAGE);
//        }
        try {
            List<Order> orders = orderService.findOrdersByUserId(optionalUser.get().getId());
            if (orders != null){
                session.setAttribute(ParameterName.ORDERS, orders);
                router = new Router(ParameterName.LIST_OF_ORDERS_PAGE, Router.Type.FORWARD);
            }else {
                router = new Router(ParameterName.SIDEBAR_PAGE, Router.Type.FORWARD);
            }
        } catch (ServiceException e) {
            logger.info("error in finding all orders ", e);
            throw new CommandException(e);
        }
        return router;
    }
}
