package com.china.bang.projects.user.repository.impl;

import com.china.bang.projects.user.domain.User;
import com.china.bang.projects.user.repository.UserRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InMemoryUserRepository implements UserRepository {

    private Map<Long, User> memoryUserRepository = new HashMap<>();

    @Override
    public boolean saveUser(User user) {
        Objects.requireNonNull(user);

        memoryUserRepository.put(Long.valueOf(memoryUserRepository.size()), user);
        return true;
    }

    @Override
    public boolean deleteUserById(Long userId) {
        Objects.requireNonNull(userId);
        if (!memoryUserRepository.containsKey(userId)) {
            return false;
        }

        memoryUserRepository.remove(userId);
        return true;
    }

    @Override
    public boolean updateUserInfo(User user) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(user.getId());

        if (Objects.isNull(memoryUserRepository.get(user.getId())) || Objects.equals(memoryUserRepository.get(user.getId()), user)) {
            return false;
        }

        memoryUserRepository.put(user.getId(), user);
        return false;
    }

    @Override
    public User getUserById(Long userId) {
        Objects.requireNonNull(userId);
        return memoryUserRepository.get(userId);
    }

    @Override
    public User getUserByNameAndPassword(String userName, String password) {
        Objects.requireNonNull(userName);
        Objects.requireNonNull(password);

        return memoryUserRepository.values().stream().filter(
                user -> Objects.equals(userName, user.getName())
                        && Objects.equals(password, user.getPassword())).findFirst().get();
    }

    @Override
    public Collection<User> getAll() {
        return memoryUserRepository.values();
    }
}
