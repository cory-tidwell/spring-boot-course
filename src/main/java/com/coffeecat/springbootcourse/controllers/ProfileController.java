package com.coffeecat.springbootcourse.controllers;

import com.coffeecat.springbootcourse.exceptions.ImageTooSmallException;
import com.coffeecat.springbootcourse.exceptions.InvalidFileException;
import com.coffeecat.springbootcourse.model.dto.FileInfo;
import com.coffeecat.springbootcourse.model.entity.Interest;
import com.coffeecat.springbootcourse.model.entity.Profile;
import com.coffeecat.springbootcourse.model.entity.SiteUser;
import com.coffeecat.springbootcourse.service.FileService;
import com.coffeecat.springbootcourse.service.InterestService;
import com.coffeecat.springbootcourse.service.ProfileService;
import com.coffeecat.springbootcourse.service.UserService;
import com.coffeecat.springbootcourse.status.ImageUploadStatus;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    //HTML sanitization:
    @Autowired
    private PolicyFactory htmlPolicy;

    //For adding Interests:
    @Autowired
    private InterestService interestService;

    //For ImageUpload:
    @Value("${file.upload.directory}")
    private String imageUploadDirectory;
    @Autowired
    private FileService fileService;

    @Value("${profile.default.image.width}")
    private int defaultProfileImageWidth;
    @Value("${profile.default.image.height}")
    private int defaultProfileImageHeight;

    //ImageUploadStatus:
    @Value("${photo.upload.ok}")
    private String imageStatusOk;

    @Value("${photo.upload.invalid}")
    private String imageStatusInvalid;

    @Value("${photo.upload.ioexception}")
    private String imageStatusIO;

    @Value("${photo.upload.toosmall}")
    private String imageStatusTooSmall;


    //get current User Object:
    private SiteUser getUser() {

        //get Username currently logged-in:
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        return userService.get(email);
    }

    private ModelAndView showProfile(SiteUser user) {
        ModelAndView modelAndView = new ModelAndView();

        //no user:
        if(user == null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        Profile profile = profileService.getUserProfile(user);
        //no profile created yet:
        if(profile == null) {
            profile = new Profile(); //create blank profile
            profile.setUser(user);
            profileService.save(profile); //create User-Profile.
        }

        //create Profile-copy without reference to the User Object:
        Profile webProfile = new Profile();
        webProfile.safeCopyFrom(profile); //pass the original create a public copy (without private Info)

        //allowing Profiles to access individual Profile Images:
        modelAndView.getModel().put("userID", user.getId());
        modelAndView.getModel().put("profile", webProfile);
        modelAndView.setViewName("app.profile");

        return modelAndView;
    }

    //View own Profile:
    @RequestMapping(value="/profile")
    ModelAndView showProfile() {

        SiteUser user = getUser();
        ModelAndView modelAndView = showProfile(user);

        modelAndView.getModel().put("ownProfile", true);

        return modelAndView;
    }

    //View other User-Profile:
    @RequestMapping(value="/profile/{id}") //use Path-Variable
    ModelAndView showProfile(@PathVariable("id") Long id) {

        SiteUser user = userService.get(id);
        ModelAndView modelAndView = showProfile(user);

        modelAndView.getModel().put("ownProfile", false);

        return modelAndView;
    }

    @RequestMapping(value="/editprofile", method= RequestMethod.GET)
    ModelAndView editProfile(ModelAndView modelAndView) {

        SiteUser user = getUser();
        Profile profile = profileService.getUserProfile(user);

        Profile webProfile = new Profile();
        webProfile.safeCopyFrom(profile);

        modelAndView.getModel().put("profile", webProfile);
        modelAndView.setViewName("app.editprofile");

        return modelAndView;
    }

    @RequestMapping(value="/editprofile", method=RequestMethod.POST)
    ModelAndView editProfile(ModelAndView modelAndView, @Valid Profile webProfile, BindingResult result) {

        modelAndView.setViewName("app.editprofile");

        SiteUser user = getUser();
        Profile updatedProfile = profileService.getUserProfile(user);

        //Overwrite currently stored Profile, check with HTML-sanitizer
        updatedProfile.safeMergeFrom(webProfile, htmlPolicy);

        if(!result.hasErrors()) {
            profileService.save(updatedProfile);
            modelAndView.setViewName("redirect:/profile");
        }

        return modelAndView;
    }

    // passing in: param-name="file", multipart/form-data
    @RequestMapping(value="/upload-profile-photo", method=RequestMethod.POST)
    @ResponseBody //return Data in JSON-Format
    ResponseEntity<ImageUploadStatus> handleImageUpload(@RequestParam("file") MultipartFile file) {

        //get User-Profile:
        SiteUser user = getUser();
        Profile profile = profileService.getUserProfile(user);

        //Get previously set Profile-Image-Path:
        Path oldImagePath = profile.getImagePath(imageUploadDirectory);

        //To return a message:
        ImageUploadStatus imageUploadStatus = new ImageUploadStatus(imageStatusOk);


        //use FileService Methods to upload & save File:
        //.save(File,BaseDirectory,SubDirPrefix,FilePrefix(Profile, with UserID to make unique))
        try {
            FileInfo imageInfo = fileService.saveImageFile(
                    file,imageUploadDirectory,"photos","p" + user.getId(),
                    defaultProfileImageWidth, defaultProfileImageHeight);

            //Set & store Image-Information in Profile_DB:
            profile.setImageDetails(imageInfo);
            profileService.save(profile);

            //Delete Old-Image:
            Files.deleteIfExists(oldImagePath);

        } catch (InvalidFileException e) {
            imageUploadStatus.setMessage(imageStatusInvalid);
            throw new RuntimeException(e);
        } catch (IOException e) {
            imageUploadStatus.setMessage(imageStatusIO);
            throw new RuntimeException(e);
        } catch (ImageTooSmallException e) {
            imageUploadStatus.setMessage(imageStatusTooSmall);
            throw new RuntimeException(e);
        }

        return new ResponseEntity(imageUploadStatus, HttpStatus.OK);
    }

    //Serving saved Image:
    @RequestMapping(value="/profileimage/{id}", method=RequestMethod.GET)
    @ResponseBody //specify that Data is to be loaded directly, not a tile!
    //ResponseEntity<Image-Data> allows setting Content-Headers&Content to be sent to Browser:
    private ResponseEntity<InputStreamResource> serveImage(@PathVariable("id") Long id) throws IOException {
        SiteUser user = userService.get(id);
        Profile profile = profileService.getUserProfile(user);

        //default Avatar, if not set yet:
        Path imagePath = Paths.get(imageUploadDirectory, "default", "avatar.jpg");
        //check if Avatar is set in Profile:
        if(profile != null && profile.getImagePath(imageUploadDirectory) != null) {
            imagePath = profile.getImagePath(imageUploadDirectory); //override default image if set.
        }

        //read & return ImageData wrapped in a ResponseEntity:
        return ResponseEntity
                .ok() //sends HTTP-response code:OK / Status-Code:200
                .contentLength(Files.size(imagePath)) //File-Size
                .contentType(MediaType.parseMediaType(URLConnection.guessContentTypeFromName(imagePath.toString()))) //Type of Content - for Browser
                .body(new InputStreamResource(Files.newInputStream(imagePath, StandardOpenOption.READ))); //.body() : serve Image-Data - read Image-Bytes
    }

    @RequestMapping(value="/save-interest", method=RequestMethod.POST)
    @ResponseBody //returns HTTP - Response Code
    public ResponseEntity<?> saveInterest(@RequestParam("name") String interestName) {
        SiteUser user = getUser();
        Profile profile = profileService.getUserProfile(user);

        //sanitize the String
        String cleanedInterestName = htmlPolicy.sanitize(interestName);

        Interest interest = interestService.createIfNotExists(cleanedInterestName); //check, create/return
        profile.addInterest(interest); //add interest to User-Profile
        profileService.save(profile); //save the User-Profile

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @RequestMapping(value="/delete-interest", method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> deleteInterest(@RequestParam("name") String interestName) {
        SiteUser user = getUser();
        Profile profile = profileService.getUserProfile(user);

        profile.removeInterest(interestName);

        profileService.save(profile);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
