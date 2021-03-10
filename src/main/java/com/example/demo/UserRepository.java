package com.example.demo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User , Integer> {

    @Query("select u from User u where u.username = :username" )
    User getUserByUsername(@Param("username") String username);

    @Query("update User u set u.failedAttempt = ?1 where u.username = ?2")
    @Modifying
    void updateFailedAttempt(int failedAttempt , String username);
}
