package com.coffeecat.springbootcourse.model.entity;

import javax.persistence.*;

@Entity
@Table(name="interests")
public class Interest implements Comparable<Interest> { //Compare one interest to another interest!

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="name", length = 25, unique = true) //no duplicates!
    private String name;

    //Constructors:
    public Interest() {

    }
    public Interest(String name) {
        this.name = name;
    }

    //GETTERS and SETTERS:
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Interest{" +
                "name='" + name + '\'' +
                '}';
    }

    //For Profile : Set<> Object - Checking if entered interests are the same, avoid duplicates:


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Interest interest = (Interest) o;

        return name != null ? name.equals(interest.name) : interest.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    //Comparing Interests, check if already exists:
    @Override
    public int compareTo(Interest other) {
        return this.name.compareTo(other.name); //compare name, if name's same, they're equal.
    }
}
