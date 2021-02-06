package ImageHoster.controller;

import ImageHoster.model.Comment;
import ImageHoster.model.Image;
import ImageHoster.model.User;
import ImageHoster.service.CommentService;
import ImageHoster.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class CommentController{
    @Autowired
    private CommentService commentService;
    @Autowired
    private ImageService imageService;

    //create comment
    @RequestMapping(value = "/image/{imageId}/{imageTitle}/comments",method = RequestMethod.POST)
    public String createComment(@PathVariable("imageId") Integer imageId, @PathVariable("imageTitle") String title, Comment newComment, HttpSession session){
        Image image = imageService.getImage(imageId);
        User user = (User) session.getAttribute("loggeduser");

        newComment.setCreatedDate(new Date());
        newComment.setImage(image);
        newComment.setUser(user);

        commentService.createComment(newComment);
        return "redirect:/images/"+imageId+"/"+image.getTitle();
    }
}
