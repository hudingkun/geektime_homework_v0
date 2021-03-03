package com.china.bang.projects.user.service.impl;

import com.china.bang.projects.user.domain.User;
import com.china.bang.projects.user.repository.impl.DatabaseUserRepository;
import com.china.bang.projects.user.service.UserService;

/**
 * 用户服务
 */
public class UserServiceImpl implements UserService {

    public static void main(String[] args) {
        new UserServiceImpl().register(new User());
    }
    @Override
    public boolean register(User user) {
        DatabaseUserRepository userRepository = new DatabaseUserRepository();
        userRepository.saveUser(user);
        return true;
    }

    @Override
    public boolean deregister(User user) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public User queryUserById(Long id) {
        return null;
    }

    @Override
    public User queryUserByNameAndPassword(String name, String password) {
        return null;
    }
}
