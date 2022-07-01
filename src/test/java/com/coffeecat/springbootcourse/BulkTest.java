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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class BulkTest {

    @Autowired
    private InterestService interestService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProfileService profileService;

    private static final String NAMESFILE = "C:\\Users\\Coffeecat\\Desktop\\SpringBoot_Proj_Course\\spring-boot-course\\src\\test\\java\\com\\coffeecat\\springbootcourse\\data\\names.txt";
    private static final String INTERESTSFILE = "C:\\Users\\Coffeecat\\Desktop\\SpringBoot_Proj_Course\\spring-boot-course\\src\\test\\java\\com\\coffeecat\\springbootcourse\\data\\interest.txt";

    private List<String> loadFile(String filename, int maxLen) throws IOException {

        Path path = Paths.get(filename);
        Path filePath = path.toAbsolutePath();

        System.out.println(filePath);

        Stream<String> stream = Files.lines(path.toRealPath());


        List<String> items = stream
                .filter(line -> !line.isEmpty()) //lambda expression: condition for outputting line
                .map(line -> line.trim())//trim whitespaces from begin/end.
                .filter(line -> line.length() <= maxLen)
                .map(line -> line.substring(0,1).toUpperCase() + line.substring(1).toLowerCase() )//transform token to other token
//                .forEach(System.out::println); //pass method for each line
                .collect(Collectors.toList());
        stream.close();
        return items;
    }

    @Test
    public void createTestData() throws IOException {

        Random random = new Random();

        List<String> names = loadFile(NAMESFILE, 25);
        List<String> interests = loadFile(INTERESTSFILE, 25);

//        for(String interest : interests) {
//            System.out.println(interest);
//        }

        for(int numUser=0; numUser < 40; numUser++) {
            String firstname = names.get(random.nextInt(names.size()));
            String surname = names.get(random.nextInt(names.size()));

            String email = firstname.toLowerCase().substring(0,1) + surname.toLowerCase() + "@example.com";

            //check if email exists:
            if(userService.get(email) != null) {
                continue;
            }

            //Required User-Auth:
            SecurityContext ctx = SecurityContextHolder.createEmptyContext();
            SecurityContextHolder.setContext(ctx);
            ctx.setAuthentication(new UsernamePasswordAuthenticationToken("anonymous", "", Arrays.asList(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))));


            String password = "pass" + firstname.toLowerCase();
            password = password.substring(0, Math.min(15, password.length())); //pick shorter String as constraint.

            assertTrue(password.length() <= 15);

            SiteUser user = new SiteUser(email, password, firstname, surname);
            user.setEnabled(random.nextInt(5) != 0); //1/5th not enabled user

            userService.register(user);
            Profile profile = new Profile(user);
            int numberOfInterests = random.nextInt(4);

            Set<Interest> userInterests = new HashSet<Interest>();
            for(int i=0; i < numberOfInterests; i++) {
                String interestText = interests.get(random.nextInt(interests.size()));

                //create new interest if doesn't exist -> add to DB
                Interest interest = interestService.createIfNotExists(interestText);
                userInterests.add(interest);
            }
            profile.setInterests(userInterests); //add to profile
            profileService.save(profile); //save profile
        }
    }
}
