package uz.epam.webproject.controller.command.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uz.epam.webproject.controller.command.Command;
import uz.epam.webproject.controller.command.ParameterName;
import uz.epam.webproject.controller.command.Router;
import uz.epam.webproject.controller.command.exception.CommandException;
import uz.epam.webproject.entity.user.User;
import uz.epam.webproject.entity.user.UserRole;
import uz.epam.webproject.service.UserService;
import uz.epam.webproject.service.exception.ServiceException;
import uz.epam.webproject.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class FindAllUsersCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        UserService userService = UserServiceImpl.getInstance();
        HttpSession session = request.getSession();
        logger.info("session current page parameter is " + session.getAttribute(ParameterName.CURRENT_PAGE));
        Router router;
        try {
            if (!isAdmin(session) || isAdmin(session)) {
                List<User> users = userService.findAll();
                if (users != null) {
                    request.setAttribute(ParameterName.USERS, users);
                    session.setAttribute(ParameterName.CURRENT_PAGE, ParameterName.BOOTSTRAP_USERS_LIST_TABLE);
                    router = new Router(/*ParameterName.NEW_LIST_OF_USERS_PAGE */ParameterName.BOOTSTRAP_USERS_LIST_TABLE, Router.Type.FORWARD);
                    return router;
                }
            }
            session.setAttribute(ParameterName.CURRENT_PAGE, ParameterName.HOME_PAGE);
            router = new Router(ParameterName.HOME_PAGE, Router.Type.REDIRECT);

        } catch (ServiceException e) {
            logger.error("error in getting all users from database", e);
            throw new CommandException(e);
        }
        return router;
    }

    @Override
    public boolean isPharmacist(HttpSession session) {
        return session.getAttribute(ParameterName.ROLE).equals(UserRole.PHARMACIST.toString());
    }


    @Override
    public boolean isAdmin(HttpSession session) {
        return session.getAttribute(ParameterName.ROLE).equals(UserRole.ADMIN.toString());
    }

    @Override
    public boolean isDoctor(HttpSession session) {
        return session.getAttribute(ParameterName.ROLE).equals(UserRole.DOCTOR.toString());
    }
}
