package com.china.bang.projects.user.repository.impl;

import com.china.bang.function.ResultMapperMethod;
import com.china.bang.projects.user.domain.User;
import com.china.bang.projects.user.repository.UserRepository;
import com.china.bang.projects.user.sql.DBOperatorUtils;

import java.util.Collection;
import java.util.Objects;

public class DatabaseUserRepository implements UserRepository {

    private final static String SAVE_USER_SQL = "INSERT INTO users(name,password,email,phoneNumber) VALUES (?,?,?,?)";

    private final static String DELETE_USERL_SQL = "DELETE FROM users WHERE id=?";

    private final static String UPDATE_USERL_SQL = "UPDATE users SET name=?,password=?,email=?,phoneNumber=? where id=?";

    private final static String SELECT_USERL_BY_ID = "SELECT id,name,password,email,phoneNumber FROM users where id=?";

    private final static String SELECT_USERL_BY_NAMEANDPWD = "SELECT id,name,password,email,phoneNumber FROM users where name=? and password=?";

    private final static String SELECT_USERL_ALL = "SELECT id,name,password,email,phoneNumber FROM users";

    @Override
    public boolean saveUser(User user) {
        Objects.requireNonNull(user);

        return DBOperatorUtils.execute(SAVE_USER_SQL, user.getName(), user.getPassword(), user.getEmail(), user.getPhoneNumber());
    }

    @Override
    public boolean deleteUserById(Long userId) {
        Objects.requireNonNull(userId);

        return DBOperatorUtils.execute(DELETE_USERL_SQL, userId);
    }

    @Override
    public boolean updateUserInfo(User user) {
        Objects.requireNonNull(user);

        return DBOperatorUtils.execute(UPDATE_USERL_SQL, user.getName(), user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getId());
    }

    @Override
    public User getUserById(Long userId) {
        Objects.requireNonNull(userId);

        return DBOperatorUtils.queryForObject(SELECT_USERL_BY_ID, User.class, ResultMapperMethod::resultMapper, userId);
    }

    @Override
    public User getUserByNameAndPassword(String userName, String password) {
        Objects.requireNonNull(userName);
        Objects.requireNonNull(password);

        return DBOperatorUtils.queryForObject(SELECT_USERL_BY_NAMEANDPWD, User.class, ResultMapperMethod::resultMapper, userName, password);
    }

    @Override
    public Collection<User> getAll() {
        return DBOperatorUtils.queryForList(SELECT_USERL_ALL, User.class, ResultMapperMethod::resultMapper);
    }
}
