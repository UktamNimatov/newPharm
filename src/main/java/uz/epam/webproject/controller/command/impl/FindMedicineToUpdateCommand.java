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
import java.util.Optional;

public class FindMedicineToUpdateCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String NO_PERMISSION = "You have no permission to this action";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        MedicineService medicineService = MedicineServiceImpl.getInstance();
        long medicineId = Long.parseLong(request.getParameter(ParameterName.MEDICINE_ID));
        Medicine medicineInfo;
        session.setAttribute(ParameterName.CURRENT_PAGE, ParameterName.BOOTSTRAP_MEDICINE_LIST_TABLE);
        try {
            if (isPharmacist(session) || isAdmin(session)) {
                Optional<Medicine> optionalMedicine = medicineService.findById(medicineId);
                if (optionalMedicine.isEmpty()) {
                    throw new ServiceException("could not find the medicine with id number: " + medicineId);
                }
                medicineInfo = optionalMedicine.get();
                session.setAttribute(ParameterName.TEMPORARY_MEDICINE, medicineInfo);
                session.setAttribute(ParameterName.CURRENT_PAGE, ParameterName.BOOTSTRAP_MEDICINE_PROFILE_PAGE);
                return new Router(ParameterName.BOOTSTRAP_MEDICINE_PROFILE_PAGE , Router.Type.FORWARD);
            }
            request.setAttribute(ParameterName.NO_PERMISSION, NO_PERMISSION);
            return new Router(ParameterName.BOOTSTRAP_MEDICINE_LIST_TABLE);
        } catch (ServiceException e) {
            logger.error("error in deleting the medicine by id ", e);
            throw new CommandException(e);
        }
    }

    @Override
    public boolean isPharmacist(HttpSession session) {
        return session.getAttribute(ParameterName.ROLE).equals(UserRole.PHARMACIST);
    }


    @Override
    public boolean isAdmin(HttpSession session) {
        return session.getAttribute(ParameterName.ROLE).equals(UserRole.ADMIN);
    }
}
