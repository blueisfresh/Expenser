package com.blueisfresh.expenser.repository;

import com.blueisfresh.expenser.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    User findByUserName(String username);

    boolean existsByUserName(String username);

}
