package ImageHoster.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@Entity annotation specifies that the corresponding class is a JPA entity
@Entity
//@Table annotation provides more options to customize the mapping.
//Here the name of the table to be created in the database is explicitly mentioned as 'Tags'. Hence the table named 'Tags' will be created in the database with all the columns mapped to all the attributes in 'Tag' class
@Table(name = "Tags")
public class Tag {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
    private List<Image> images;

    public Tag() {
    }

    public Tag(String tagName) {
        this.name = tagName;
    }

    //Generate getters and setters for all the attributes
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
