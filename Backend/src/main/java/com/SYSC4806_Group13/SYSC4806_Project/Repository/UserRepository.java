package com.SYSC4806_Group13.SYSC4806_Project.Repository;

import com.SYSC4806_Group13.SYSC4806_Project.Model.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repo to store users information Currently is H2 in memory database, we should change to another later
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}