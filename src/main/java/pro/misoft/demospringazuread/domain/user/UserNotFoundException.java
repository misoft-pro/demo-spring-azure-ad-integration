package pro.misoft.demospringazuread.domain.user;

import pro.misoft.demospringazuread.domain.common.BusinessException;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(String message, Object[] args) {
        super(message, args);
    }

    public UserNotFoundException(String message) {
        super(message, new Object[0]);
    }
}
