package com.coffeecat.springbootcourse.model.dto;

import com.coffeecat.springbootcourse.model.entity.Interest;
import com.coffeecat.springbootcourse.model.entity.Profile;

import java.util.Set;

public class SearchResult {
    private Long userId; //for profile-Image, retrieve via ID.
    private String firstname;
    private String surname;
    private Set<Interest> interests;

    //Constructor
    public SearchResult(Profile profile) {
        this.userId = profile.getUser().getId();
        this.firstname = profile.getUser().getFirstname();
        this.surname = profile.getUser().getSurname();
        this.interests = profile.getInterests();
    }

    //GETTERS & SETTERS:
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Set<Interest> getInterests() {
        return interests;
    }

    public void setInterests(Set<Interest> interests) {
        this.interests = interests;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "userId=" + userId +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", interests=" + interests +
                '}';
    }
}
