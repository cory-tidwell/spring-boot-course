package com.coffeecat.springbootcourse.service;

import com.coffeecat.springbootcourse.model.dto.SearchResult;
import com.coffeecat.springbootcourse.model.entity.Profile;
import com.coffeecat.springbootcourse.model.repository.ProfileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Value("${search.pagesize}")
    private int pagesize;

    @Autowired
    private ProfileDao profileDao;

    private SearchResult convertProfileSearchResult(Profile profile) {
        return new SearchResult(profile);
    }

    public Page<SearchResult> search(String text, int pageNumber) {

        //Pagination for Displaying Users:
        PageRequest request = PageRequest.of(pageNumber-1, pagesize);

        //turn Profile Objects -> SearchResult Objects:
        Page<Profile> searchResults = profileDao.findByInterestsNameContainingIgnoreCase(text, request);
//                .stream()
//                .map(SearchResult::new) //remap Objects Profile Object to SearchResult::Constructor-reference
//                .collect(Collectors.toList()); //collect stream of SearchResult Objs into a List

        //Convert Page of Profile to SearchResult Objects:
        return searchResults.map(this::convertProfileSearchResult); //maps from Profiles->SearchResult
    }
}
