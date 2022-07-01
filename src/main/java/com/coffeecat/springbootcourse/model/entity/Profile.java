package com.coffeecat.springbootcourse.model.entity;

import com.coffeecat.springbootcourse.model.dto.FileInfo;
import org.owasp.html.PolicyFactory;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

@Entity
@Table(name="profile")
public class Profile {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @OneToOne(targetEntity=SiteUser.class)
    @JoinColumn(name="user_id", nullable = false)
    private SiteUser user;

    @Column(name="about", length=5000)
    @Size(max=5000, message="{editprofile.about.size}")
    private String about;

    //Store Profile-Image Information:
    @Column(name="image_dir", length=10)
    private String imageDirectory;
    @Column(name="image_name", length=10)
    private String imageName;
    @Column(name="image_extension", length=5)
    private String imageExtension;

    //Storing Interests - Many2Many relationship
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="profile_interests",
            joinColumns={@JoinColumn(name="profile_id")}, //creates a Join-Table {ProfileID}
            inverseJoinColumns={@JoinColumn(name="interest_id")}) //column that contains the Id of the Interest
    @OrderColumn(name="display_order") //Orders Interests by order entered!
    private Set<Interest> interests; //Note:saves List of IDs

    //Constructors:
    public Profile() {

    }
    public Profile(SiteUser user) {
        this.user = user;
    }

    //GETTERS and SETTERS:
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SiteUser getUser() {
        return user;
    }

    public void setUser(SiteUser user) {
        this.user = user;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImageDirectory() {
        return imageDirectory;
    }

    public void setImageDirectory(String imageDirectory) {
        this.imageDirectory = imageDirectory;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageExtension() {
        return imageExtension;
    }

    public void setImageExtension(String imageExtension) {
        this.imageExtension = imageExtension;
    }

    public Set<Interest> getInterests() {
        return interests;
    }

    public void setInterests(Set<Interest> interests) {
        this.interests = interests;
    }

    //Setting Image Details:
    public void setImageDetails(FileInfo info) {
        imageDirectory = info.getSubDirectory();
        imageExtension = info.getExtension();
        imageName = info.getBaseName();
    }

    public Path getImagePath(String baseDirectory) {
        //check if Image has been set in Profile:
        if(imageName == null) {
            return null;
        }

        //retrieve Image-Path:
        return Paths.get(baseDirectory,imageDirectory,imageName + "." + imageExtension);
    }

    /* Create a profile that is suitable for display via JSP */
    public void safeCopyFrom(Profile origin) {
        if(origin.about != null) {
            this.about = origin.about;
        }

        if(origin.interests != null) {
            this.interests = origin.interests;
        }
    }

    /* Create a profile that is suitable for saving */
    //Overwrite about information in DB + check HTML-sanitizer
    public void safeMergeFrom(Profile webProfile, PolicyFactory htmlPolicy) {
        if(webProfile.about != null) {
            this.about = htmlPolicy.sanitize(webProfile.about);
        }
    }

    public void addInterest(Interest interest) {
        interests.add(interest);
    }

    public void removeInterest(String interestName) {
        interests.remove(new Interest(interestName)); //remove interest from the Set
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", user=" + user +
                ", about='" + about + '\'' +
                ", imageDirectory='" + imageDirectory + '\'' +
                ", imageName='" + imageName + '\'' +
                ", imageExtension='" + imageExtension + '\'' +
                ", interests=" + interests +
                '}';
    }
}
