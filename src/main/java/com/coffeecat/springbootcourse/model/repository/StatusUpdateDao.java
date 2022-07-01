package com.coffeecat.springbootcourse.model.repository;

import com.coffeecat.springbootcourse.model.entity.StatusUpdate;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

//Dao = Data Access Object
//Crud = CreateRetrieveUpdateDelete <Type to Save, Type of PK>
//public interface StatusUpdateDao extends CrudRepository<StatusUpdate, Long> {

//using PagingAndSort-Repository enables use of paging and sorting functions.
@Repository
public interface StatusUpdateDao extends PagingAndSortingRepository<StatusUpdate, Long> {
    StatusUpdate findFirstByOrderByAddedDesc(); //capitalization important here, implemented automatically!
}
