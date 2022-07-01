package com.coffeecat.springbootcourse.model.entity;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name="verification")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id")
    private Long id; //PK

    @Column(name="token")
    private String token;

    //SiteUser is a FK value - using Users-Table(id) to join:
    @OneToOne(targetEntity=SiteUser.class) //Relationship(what is FK) - for every User only one Token.
    @JoinColumn (name="user_id", nullable = false) //value cannot be null(nullable)
    private SiteUser user;

    @Column(name="expiry_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiry;

    @Column(name="token_type")
    @Enumerated(EnumType.STRING) //enum type in mySQL
    private TokenType type;

    //Set Expiry Date 24h into Future when creating the token:
    @PrePersist
    private void setDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 24);
        expiry = c.getTime(); //set expiry Field.
    }

    //Constructor:
    public VerificationToken(String token, SiteUser user, TokenType type) {
        this.token = token;
        this.user = user;
        this.type = type;
    }

    //no parameter constructor:
    public VerificationToken() {

    }

    //GETTERS and SETTERS:
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public SiteUser getUser() {
        return user;
    }

    public void setUser(SiteUser user) {
        this.user = user;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }
}