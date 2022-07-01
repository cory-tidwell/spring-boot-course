package com.coffeecat.springbootcourse.model.repository;

import com.coffeecat.springbootcourse.model.entity.SiteUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<SiteUser, Long> {
    SiteUser findByEmail(String email); //automatically created by Spring from CrudRepository
}
