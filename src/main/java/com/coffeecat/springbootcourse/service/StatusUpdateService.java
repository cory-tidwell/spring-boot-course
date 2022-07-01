package com.coffeecat.springbootcourse.service;

import com.coffeecat.springbootcourse.model.entity.StatusUpdate;
import com.coffeecat.springbootcourse.model.repository.StatusUpdateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

//used to wrap the StatusUpdateDao - use annotation @Service
//can be used to add logic.
@Service
public class StatusUpdateService {

    @Value("${status.pagesize}")
    private int pagesize;

    @Autowired
    private StatusUpdateDao statusUpdateDao;

    public void save(StatusUpdate statusUpdate) {
        statusUpdateDao.save(statusUpdate);
    }

    public StatusUpdate getLatest() {
        return statusUpdateDao.findFirstByOrderByAddedDesc();
    }

    //from Interface, works with 0 based pageNumber
    public Page<StatusUpdate> getPage(int pageNumber) {
        //uses:(page-number, page-max-size, sorting direction, sort by which field)
        PageRequest request = PageRequest.of(pageNumber-1, pagesize,Sort.Direction.DESC,"added");
        return statusUpdateDao.findAll(request);
    }

    public void delete(Long id) {
        statusUpdateDao.deleteById(id);
    }
    public StatusUpdate get(Long id) {
        return statusUpdateDao.findById(id).get();
    }
}
