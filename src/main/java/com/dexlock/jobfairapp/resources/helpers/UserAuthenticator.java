package com.dexlock.jobfairapp.resources.helpers;

import com.dexlock.jobfairapp.dao.TokenDao;
import com.dexlock.jobfairapp.dao.UserDAO;
import com.dexlock.jobfairapp.models.User;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import org.bson.types.ObjectId;

import java.util.Optional;

public class UserAuthenticator implements Authenticator<String, User> {

    private TokenDao tokenDao = new TokenDao();
    private UserDAO userDAO = new UserDAO();

    @Override
    public Optional<User> authenticate(String token) throws AuthenticationException {
        ObjectId userId = tokenDao.getTokenWithId(new ObjectId(token));
        if (userId != null) {
            User user = userDAO.fetchUser(userId.toHexString());
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }
}
