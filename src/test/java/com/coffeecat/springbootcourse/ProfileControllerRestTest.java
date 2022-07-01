package com.coffeecat.springbootcourse;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.coffeecat.springbootcourse.model.entity.Interest;
import com.coffeecat.springbootcourse.model.entity.Profile;
import com.coffeecat.springbootcourse.model.entity.SiteUser;
import com.coffeecat.springbootcourse.service.InterestService;
import com.coffeecat.springbootcourse.service.ProfileService;
import com.coffeecat.springbootcourse.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class ProfileControllerRestTest {
    //Services
    @Autowired
    private UserService userService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private InterestService interestService;

    //Setup Code:
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach //Method runs before every Test:
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build(); //creates MockMvc Object for POST
    }

    @Test
    @WithMockUser(username="demo@demo.com") //Create/use a mock User for Testing.
    public void testSaveAndDeleteInterest() throws Exception {
        String interestString = "random Interest";

        //use MockMvcRequestBuilders.* post() method & MockMvcResultMatchers.* status() method:
        mockMvc.perform(post("/save-interest").param("name", interestString))
                .andExpect(status().isOk());

        //check if interest was added to DB:
        Interest interest = interestService.get(interestString);
        assertNotNull(interest, "Interest should not be null.");
        assertEquals(interest.getName(), interestString, "The Interest String should match.");

        //get User-Profile currently logged-in:
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        SiteUser user = userService.get(email);
        Profile profile = profileService.getUserProfile(user);

        //Check if profile contains List of Interests, call contains() with Interest Object with same string:
        assertTrue(profile.getInterests().contains(
                new Interest(interestString)), "Profile should contain interest");

        //Deleting Interest with MockMVC - check if Profile still contains interest:
        mockMvc.perform(post("/delete-interest").param("name", interestString))
                .andExpect(status().isOk());
        profile = profileService.getUserProfile(user);
        assertFalse(profile.getInterests().contains(
                new Interest(interestString)),"Profile should not contain Interest");
    }
}
