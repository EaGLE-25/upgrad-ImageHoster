package ImageHoster.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//Write the annotation to specify that the corresponding class is a JPA entity

//Write the annotation to provide more options to customize the mapping, explicitly mentioning that the name of the table in the database is 'users'
@Entity
@Table(name = "users")
public class User {
    @Id
    //Write the annotation to specify that the corresponding attribute is a primary key
    @GeneratedValue(strategy = GenerationType.AUTO)
    //Write the annotation to specify that the attribute will be mapped to the column in the database.
    @Column(name = "id")
    //Also explicitly mention the column name as 'id'
    private Integer id;

    //Write the annotation to specify that the attribute will be mapped to the column in the database.
    //Also explicitly mention the column name as 'username'
    @Column(name = "username")
    private String username;

    //Write the annotation to specify that the attribute will be mapped to the column in the database.
    //Also explicitly mention the column name as 'password'
    @Column(name = "password")
    private String password;

    //Write the annotation to specify the below mentioned features
    //The 'users' table is mapped to 'user_profile' table with One:One mapping
    //Also if a record in 'user_profile' table is deleted or updated, then all the records in 'users' table associated to that particular record in 'user_profile' table will be deleted or updated  first and then the record in the 'user_profile' table will be deleted or updated
    //FetchType is EAGER

    //Write the annotation to indicate that the name of the column in 'users' table referring the primary key in 'user_profile' table will be 'profile_id'
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id")
    private UserProfile profile;

    @OneToMany(cascade = CascadeType.REMOVE,fetch = FetchType.LAZY,mappedBy = "user")
    private List<Image> images = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "user")
    private List<Comment>comments = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}

