package com.coffeecat.springbootcourse.model.repository;

import com.coffeecat.springbootcourse.model.entity.Interest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestDao extends CrudRepository<Interest, Long> {
    Interest findOneByName(String name);
}
