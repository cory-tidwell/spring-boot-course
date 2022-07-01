package com.coffeecat.springbootcourse.model.entity;

import com.coffeecat.springbootcourse.validation.PasswordMatch;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="users")
//Validation constraint applies to the whole TYPE, not just field:
@PasswordMatch(message="{register.repeatPassword.mismatch}")
public class SiteUser {
    @Id // Specify Primary-Key
    @GeneratedValue(strategy = GenerationType.AUTO) // Automatically generate(auto-increment) the ID.
    @Column(name="id")
    private Long id;

    @Column(name="email", unique = true)
    @Email(message="{register.email.invalid}") //basic check format
    @NotBlank(message = "{register.email.invalid}")
    private String email;

    //For validating the password ( before encryption )
    @Transient //don't save to DB
    @Size(min=5, max=15, message="{register.password.size}")
    private String plainPassword;

    @Column(name="password")
    private String password;

    @Transient //don't save the repeated Password.
    private String repeatPassword;


    @NotNull
    @Column(name="firstname", length=20)
    @Size(min=2, max=20, message="{register.name.size}")
    private String firstname;

    @NotNull
    @Column(name="surname", length=25)
    @Size(min=2, max=25, message="{register.name.size}")
    private String surname;

    //Disabling User by default:
    @Column(name="enabled")
    private Boolean enabled = false;

    @Column(name="role", length = 20)
    private String role;

    //Constructors:
    public SiteUser() {

    }
    public SiteUser(String email, String password, String firstname, String surname) {
        this.email = email;
        this.setPlainPassword(password);
        this.repeatPassword = password;
        this.firstname = firstname;
        this.surname = surname;
        this.enabled = true;
    }

    //GETTERS & SETTERS:
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        //when setting plainPassword also set password field to ENCRYPTED plainPassword.
        this.password = new BCryptPasswordEncoder().encode(plainPassword);
        this.plainPassword = plainPassword;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "SiteUser{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", plainPassword='" + plainPassword + '\'' +
                ", password='" + password + '\'' +
                ", repeatPassword='" + repeatPassword + '\'' +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", enabled=" + enabled +
                ", role='" + role + '\'' +
                '}';
    }
}
