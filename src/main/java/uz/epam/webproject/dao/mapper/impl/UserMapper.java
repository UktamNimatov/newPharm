package uz.epam.webproject.dao.mapper.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uz.epam.webproject.dao.exception.DaoException;
import uz.epam.webproject.dao.mapper.ColumnName;
import uz.epam.webproject.dao.mapper.EntityMapper;
import uz.epam.webproject.entity.user.User;
import uz.epam.webproject.entity.user.UserRole;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserMapper implements EntityMapper<User> {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Optional<User> map(ResultSet resultSet){
        try {
            User user = new User();
            user.setId(resultSet.getInt(ColumnName.ID));
            user.setLogin(resultSet.getString(ColumnName.LOGIN));
            user.setPassword(resultSet.getString(ColumnName.PASSWORD));
            user.setFirstName(resultSet.getString(ColumnName.FIRST_NAME));
            user.setLastName(resultSet.getString(ColumnName.LAST_NAME));
            user.setEmail(resultSet.getString(ColumnName.EMAIL));
            user.setRole(UserRole.valueOf(resultSet.getString(ColumnName.ROLE).toUpperCase()));
            if (resultSet.getString(ColumnName.CERTIFICATE) != null){
                user.setCertificateSerialNumber(resultSet.getString(ColumnName.CERTIFICATE));
            }else {
                user.setCertificateSerialNumber("");
            }
            return Optional.of(user);
        } catch (SQLException sqlException) {
            logger.error("error in mapping resultSet into an object (User)", sqlException);
        }
        return Optional.empty();
    }
}
