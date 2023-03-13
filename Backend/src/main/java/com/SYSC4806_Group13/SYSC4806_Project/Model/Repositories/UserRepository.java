package com.SYSC4806_Group13.SYSC4806_Project.Model.Repositories;

import com.SYSC4806_Group13.SYSC4806_Project.Model.DataModel.User;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Repo to store users information Currently is H2 in memory database, we should change to another later
 */
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);

    User findUserById(long id);

    Optional<User> findByIdAndEmail(long id, String email);
}