package com.coffeecat.springbootcourse.service;

import com.coffeecat.springbootcourse.model.entity.SiteUser;
import com.coffeecat.springbootcourse.model.entity.TokenType;
import com.coffeecat.springbootcourse.model.entity.VerificationToken;
import com.coffeecat.springbootcourse.model.repository.UserDao;
import com.coffeecat.springbootcourse.model.repository.VerificationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    //use Password Encoder instance (Spring automatically finds BCypt PassEncoder in App.java):
    @Autowired
    private PasswordEncoder passwordEncoder;

    //For retrieving the Token from the DB using the Repository
    @Autowired
    private VerificationDao verificationDao;

    public void register(SiteUser user) {
        //setting Default role:
        user.setRole("ROLE_USER");

        //now Password can be encoded using the passwordEncoder instance:
        //Line no longer needed. Password is being encoded in DomainObject via setPlainPassword .encode
//        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userDao.save(user);
    }

    //updating a user:
    public void save(SiteUser user) {
        userDao.save(user);
    }

    //getting SiteUser Object via Email:
    public SiteUser get(String email) {
        return userDao.findByEmail(email);
    }

    //getting SiteUser Object via ID:
    public SiteUser get(Long id) {
        return userDao.findById(id).get();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //find UserObject:
        SiteUser user = userDao.findByEmail(email);

        //user not found:
        if(user == null) {
            return null;
        }

        //user-Roles: Role-Names should have "ROLE_rolename" hardcoded:
//        List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

        List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole());

        //Password - get from SiteUserObject:
        String password = user.getPassword();

        //enabled - is account verified?:
        Boolean enabled = user.getEnabled();

        //Default Spring User Constructor: public User(String username,String password,
        //                                          boolean enabled,
        //                                          boolean accountNonExpired,
        //                                          boolean credentialsNonExpired,
        //                                          boolean accountNonLocked,
        //                                          GrantedAuthority[] authorities)
        return new User(email,password, enabled, true, true, true, auth);
    }

    //Create & Save the verification Token:
    public String createVerificationToken(SiteUser user) {
        VerificationToken token = new VerificationToken(
                UUID.randomUUID().toString(), user, TokenType.REGISTRATION); //create the Token
        verificationDao.save(token); //save the Token to DB
        return token.getToken(); //returns the Token String.
    }


    //Lookup the TokenObject in DB:
    public VerificationToken getVerificationToken(String token) {
        return verificationDao.findByToken(token);
    }

    //delete Token when used:
    public void deleteToken(VerificationToken token) {
        verificationDao.delete(token);
    }

    public String getUserName(Long uid) {
        Optional<SiteUser> optUser = userDao.findById(uid);
        SiteUser user = optUser.get();

        return user.getFirstname() + " " + user.getSurname();
    }
}
