package com.example.demo.repo;

import com.example.demo.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepos extends CrudRepository<User,Long> {
    User findByLogin(String login);

}
