package com.china.bang.projects.user.repository;

import com.china.bang.projects.user.domain.User;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;

public interface UserRepository {

    boolean saveUser(User user);

    boolean deleteUserById(Long userId);

    boolean updateUserInfo(User user);

    User getUserById(Long userId);

    User getUserByNameAndPassword(String userName,String password);

    Collection<User> getAll();
}
