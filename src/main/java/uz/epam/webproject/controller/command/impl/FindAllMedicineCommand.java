package uz.epam.webproject.controller.command.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uz.epam.webproject.controller.command.Command;
import uz.epam.webproject.controller.command.ParameterName;
import uz.epam.webproject.controller.command.Router;
import uz.epam.webproject.controller.command.exception.CommandException;
import uz.epam.webproject.entity.medicine.Medicine;
import uz.epam.webproject.entity.user.User;
import uz.epam.webproject.entity.user.UserRole;
import uz.epam.webproject.service.MedicineService;
import uz.epam.webproject.service.exception.ServiceException;
import uz.epam.webproject.service.impl.MedicineServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class FindAllMedicineCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        MedicineService medicineService = MedicineServiceImpl.getInstance();
        Router router;
        HttpSession session = request.getSession();
        try {
            List<Medicine> medicineList = medicineService.findAll();
            if (medicineList != null){
                session.setAttribute(ParameterName.MEDICINE_LIST, medicineList);
                session.setAttribute(ParameterName.CURRENT_PAGE, ParameterName.BOOTSTRAP_MEDICINE_LIST_TABLE);
                router = new Router(ParameterName.BOOTSTRAP_MEDICINE_LIST_TABLE, Router.Type.FORWARD);
            }else{
                session.setAttribute(ParameterName.CURRENT_PAGE, ParameterName.BOOTSTRAP_HOME_PAGE);
                router = new Router(ParameterName.BOOTSTRAP_HOME_PAGE, Router.Type.REDIRECT);
            }
        } catch (ServiceException e) {
            logger.error("error in finding all medicines " , e);
            throw new CommandException(e);
        }
        return router;
    }

}
