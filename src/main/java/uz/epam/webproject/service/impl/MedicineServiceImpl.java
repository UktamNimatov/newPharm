package uz.epam.webproject.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uz.epam.webproject.dao.MedicineDao;
import uz.epam.webproject.dao.exception.DaoException;
import uz.epam.webproject.dao.impl.MedicineDaoImpl;
import uz.epam.webproject.entity.medicine.Medicine;
import uz.epam.webproject.service.MedicineService;
import uz.epam.webproject.service.exception.ServiceException;
import uz.epam.webproject.validator.MedicineValidator;
import uz.epam.webproject.validator.UserValidator;
import uz.epam.webproject.validator.impl.MedicineValidatorImpl;
import uz.epam.webproject.validator.impl.UserValidatorImpl;

import java.util.List;
import java.util.Optional;

public class MedicineServiceImpl implements MedicineService {
    private static final Logger logger = LogManager.getLogger();
    private final MedicineValidator medicineValidator = MedicineValidatorImpl.getInstance();

    private final MedicineDao medicineDao = MedicineDaoImpl.getInstance();

    private static MedicineServiceImpl instance;

    public static MedicineServiceImpl getInstance() {
        if (instance == null) {
            instance = new MedicineServiceImpl();
        }
        return instance;
    }

    private MedicineServiceImpl() {
    }

    @Override
    public List<Medicine> findAll() throws ServiceException {
        try {
            return medicineDao.findAll();
        } catch (DaoException e) {
            logger.error("error in finding all medicine (in service layer)", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean addEntity(Medicine medicine) throws ServiceException {
        if (medicineValidator.checkMedicine(medicine)) {
            try {
                return medicineDao.addEntity(medicine);
            } catch (DaoException e) {
                logger.error("error in adding a new medicine (in service layer)", e);
                throw new ServiceException(e);
            }
        }else {
            return false;
        }
    }

    @Override
    public Optional<Medicine> findById(Long id) throws ServiceException {
        try {
            return medicineDao.findById(id);
        } catch (DaoException e) {
            logger.error("error in finding a medicine by id (in service layer)", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean delete(Long id) throws ServiceException {
        try {
            return medicineDao.delete(id);
        } catch (DaoException e) {
            logger.error("error in finding a medicine by id (in service layer)", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Medicine> findMedicineByQuery(String searchQuery) throws ServiceException {
        try {
            return medicineDao.findMedicineByQuery(searchQuery);
        } catch (DaoException e) {
            logger.error("error in finding a medicine by search query (in service layer)", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Medicine> findAllMedicineWithPrescription() throws ServiceException {
        try {
            return medicineDao.findAllMedicineWithPrescription();
        } catch (DaoException e) {
            logger.error("error in finding all medicines with prescription (in service layer)", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Medicine> findAllMedicineWithoutPrescription() throws ServiceException {
        try {
            return medicineDao.findAllMedicineWithoutPrescription();
        } catch (DaoException e) {
            logger.error("error in finding all medicines without prescription (in service layer)", e);
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean updateMedicine(Medicine medicine) throws ServiceException {
        if (medicineValidator.checkMedicine(medicine)) {
            try {
                return medicineDao.updateMedicine(medicine);
            } catch (DaoException e) {
                logger.error("error in updating medicine with id (in service layer)", e);
                throw new ServiceException(e);
            }
        } else {
            return false;
        }
    }
}
