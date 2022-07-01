package com.coffeecat.springbootcourse;

import com.coffeecat.springbootcourse.model.entity.Interest;
import com.coffeecat.springbootcourse.model.entity.Profile;
import com.coffeecat.springbootcourse.model.entity.SiteUser;
import com.coffeecat.springbootcourse.service.InterestService;
import com.coffeecat.springbootcourse.service.ProfileService;
import com.coffeecat.springbootcourse.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class ProfileTest {

    @Autowired
    private InterestService interestService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProfileService profileService;

    //Create Users:
    private SiteUser[] users = {
      new SiteUser("dlw@rand.com", "{noop}hf2houwq", "Dummy", "DummySur"),
      new SiteUser("jp@rand.com", "{noop}zeu2372f", "Bob", "Bobing")
    };
    //List of interests:
    private String[][] interests = {
            {"music", "bowling_xx", "planting"},
            {"chicken", "planting", "music"},
            {"soccer", "bowling_xx", "planting"},
            {"eating", "singing", "eating"}
    };

    @Test
    public void testInterests() {
        for(int i = 0; i<users.length; i++) {

            //Required to run test:
            SecurityContext ctx = SecurityContextHolder.createEmptyContext();
            SecurityContextHolder.setContext(ctx);
            ctx.setAuthentication(new UsernamePasswordAuthenticationToken("anonymous", "", Arrays.asList(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))));
            //

            SiteUser user = users[i];
            String[] interestArray = interests[i];

            userService.register(user);

            HashSet<Interest> interestSet = new HashSet<>();

            for(String interestString : interestArray) {
                //check if interest exists in DB -> create Interest, return interest
                Interest interest = interestService.createIfNotExists(interestString);
                interestSet.add(interest);

                assertNotNull(interest, "Interest should not be null.");
                assertNotNull(interest.getId(), "Interest should have an Id");
                assertEquals(interestString, interest.getName(), "String should match.");
            }

            //create User-Profile
            Profile profile = new Profile(user);
            profile.setInterests(interestSet); //add interests to user
            profileService.save(profile);

            //Test List of Interests against entries:
            Profile retrievedProfile = profileService.getUserProfile(user);

            assertEquals(interestSet, retrievedProfile.getInterests(), "Interest Sets should match.");
        }
    }
}
