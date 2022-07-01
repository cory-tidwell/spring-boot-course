package com.coffeecat.springbootcourse.model.repository;

import com.coffeecat.springbootcourse.model.entity.VerificationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationDao extends CrudRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}
