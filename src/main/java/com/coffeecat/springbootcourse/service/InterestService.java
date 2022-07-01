package com.coffeecat.springbootcourse.service;

import com.coffeecat.springbootcourse.model.entity.Interest;
import com.coffeecat.springbootcourse.model.repository.InterestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterestService {

    @Autowired
    private InterestDao interestDao;

    public Interest get(String interestName) {
        return interestDao.findOneByName(interestName);
    }

    public void save(Interest interest) {
        interestDao.save(interest);
    }

    //create Interest if it doesn't exist:
    public Interest createIfNotExists(String interestString) {
        Interest interest = interestDao.findOneByName(interestString);

        if(interest == null) {
            interest = new Interest(interestString);
            interestDao.save(interest);
        }

        return interest;
    }

    public Long count() {
        return interestDao.count();
    }

}
