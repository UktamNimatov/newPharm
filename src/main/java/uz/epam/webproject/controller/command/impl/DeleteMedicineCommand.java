package uz.epam.webproject.controller.command.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uz.epam.webproject.controller.command.Command;
import uz.epam.webproject.controller.command.ParameterName;
import uz.epam.webproject.controller.command.Router;
import uz.epam.webproject.controller.command.exception.CommandException;
import uz.epam.webproject.entity.medicine.Medicine;
import uz.epam.webproject.entity.user.UserRole;
import uz.epam.webproject.service.MedicineService;
import uz.epam.webproject.service.exception.ServiceException;
import uz.epam.webproject.service.impl.MedicineServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class DeleteMedicineCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        MedicineService medicineService = MedicineServiceImpl.getInstance();
        long medicineId = Long.parseLong(request.getParameter(ParameterName.MEDICINE_ID));
        try {
            if (isPharmacist(session) || isAdmin(session)) {
                if (medicineService.delete(medicineId)) {
                    logger.info("medicine with id " + medicineId + " successfully deleted");
                    List<Medicine> medicineList = medicineService.findAll();
                    request.setAttribute(ParameterName.MEDICINE_LIST, medicineList);
                    request.setAttribute(ParameterName.MEDICINE_DELETED, ParameterName.MEDICINE_DELETED);
                } else {
                    logger.info("medicine with id " + medicineId + " not deleted");
                    request.setAttribute(ParameterName.MEDICINE_NOT_DELETED, ParameterName.MEDICINE_NOT_DELETED);
                }
            }
                session.setAttribute(ParameterName.CURRENT_PAGE, ParameterName.BOOTSTRAP_MEDICINE_LIST_TABLE);
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
