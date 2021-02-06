package ImageHoster.controller;

import ImageHoster.model.Comment;
import ImageHoster.model.Image;
import ImageHoster.model.Tag;
import ImageHoster.model.User;
import ImageHoster.service.CommentService;
import ImageHoster.service.ImageService;
import ImageHoster.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Controller
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private TagService tagService;

    //This method displays all the images in the user home page after successful login
    @RequestMapping("images")
    public String getUserImages(Model model) {
        List<Image> images = imageService.getAllImages();
        model.addAttribute("images", images);
        return "images";
    }

//    show image
    @RequestMapping("/images/{id}/{title}")
    public String showImage(@PathVariable("id") Integer id, Model model) {
        Image image = imageService.getImage(id);
        List<Tag>tagList = image.getTags();
        List<Comment>comments = image.getComments();

        model.addAttribute("tags",tagList);
        model.addAttribute("image", image);
        model.addAttribute("comments",comments);

        return "images/image";
    }

//    upload image form
    @RequestMapping("/images/upload")
    public String newImage() {
        return "images/upload";
    }


//    upload image
    @RequestMapping(value = "/images/upload", method = RequestMethod.POST)
    public String createImage(@RequestParam("file") MultipartFile file,@RequestParam("tags") String tags, Image newImage,HttpSession session) throws IOException {
        String uploadedImageData = convertUploadedFileToBase64(file);
        List<Tag> tagList = findOrCreateTags(tags);

//      set base64 imageFile
        newImage.setImageFile(uploadedImageData);
//      set date
        newImage.setDate(new Date());
//      set user
        User loggedUser = (User)session.getAttribute("loggeduser");
        newImage.setUser(loggedUser);
//      set tags
        newImage.setTags(tagList);

        imageService.uploadImage(newImage);
        return "redirect:/images";
    }

//    edit form
    @RequestMapping(value = "/editImage")
    public String editImage(@RequestParam("imageId") Integer imageId, Model model,HttpSession session) {
        Image image = imageService.getImage(imageId);
        List<Tag>tagList = image.getTags();

        User loggedUser = (User) session.getAttribute("loggeduser");
        User imageAuthor = image.getUser();

        Integer loggedUserId = loggedUser.getId();
        Integer imageAuthorId = imageAuthor.getId();

        model.addAttribute("image", image);

        if( loggedUserId == imageAuthorId){
            String tags = convertTagsToString(tagList);
            model.addAttribute("tags",tags);
            return "images/edit";
        }else{
            String error = "Only the owner of the image can edit the image";
            model.addAttribute("tags",tagList);
            model.addAttribute("editError",error);
            return "images/image";
        }

    }

//    edit image
    @RequestMapping(value = "/editImage", method = RequestMethod.POST)
    public String editImageSubmit(@RequestParam("file") MultipartFile file, @RequestParam("imageId") Integer imageId,@RequestParam("tags")String tags, Image updatedImage, HttpSession session) throws IOException {
        Image image = imageService.getImage(imageId);
        List<Tag>tagList = findOrCreateTags(tags);
        String updatedImageFile = convertUploadedFileToBase64(file);

//      set base64 imageFile
        if(updatedImageFile.isEmpty()){
            updatedImage.setImageFile(image.getImageFile());
        }else{
            updatedImage.setImageFile(updatedImageFile);
        }
//      set tags
        updatedImage.setTags(tagList);
//      set image id
        updatedImage.setId(imageId);
//      set user
        User user = (User) session.getAttribute("loggeduser");
        updatedImage.setUser(user);
//      set date
        updatedImage.setDate(new Date());

        imageService.updateImage(updatedImage);
        return "redirect:/images/"+updatedImage.getId()+'/'+updatedImage.getTitle();
    }

//  delete image
    @RequestMapping(value = "/deleteImage",method = RequestMethod.DELETE)
    public String deleteImage(@RequestParam("imageId") Integer imageId,HttpSession session,Model model){
        Image image = imageService.getImage(imageId);
        List<Tag>tagList = image.getTags();

        User loggedUser = (User) session.getAttribute("loggeduser");
        User imageAuthor = image.getUser();

        Integer loggedUserId = loggedUser.getId();
        Integer imageAuthorId = imageAuthor.getId();

        if(loggedUserId == imageAuthorId){
            imageService.deleteImage(imageId);
            return "redirect:/images";
        }else{
            String error = "Only the owner of the image can delete the image";
            model.addAttribute("image",image);
            model.addAttribute("tags",tagList);
            model.addAttribute("deleteError",error);
            return "images/image";
        }
    }


    //This method converts the image to Base64 format
    private String convertUploadedFileToBase64(MultipartFile file) throws IOException {
        return Base64.getEncoder().encodeToString(file.getBytes());
    }

    private List<Tag> findOrCreateTags(String tagNames) {
        StringTokenizer st = new StringTokenizer(tagNames, ",");
        List<Tag> tags = new ArrayList<Tag>();

        while (st.hasMoreTokens()) {
            String tagName = st.nextToken().trim();

            Tag tag = tagService.getTagByName(tagName);

            if (tag == null) {
                Tag newTag = new Tag(tagName);
                tag = tagService.createTag(newTag);
            }
            tags.add(tag);
        }
        return tags;
    }

    private String convertTagsToString(List<Tag> tags) {
        StringBuilder tagString = new StringBuilder();

        for (int i = 0; i <= tags.size() - 2; i++) {
            tagString.append(tags.get(i).getName()).append(",");
        }

        Tag lastTag = tags.get(tags.size() - 1);
        tagString.append(lastTag.getName());

        return tagString.toString();
    }
}
