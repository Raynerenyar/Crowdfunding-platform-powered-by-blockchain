package ethereum.tutorials.java.ethereum.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ethereum.tutorials.java.ethereum.models.Announcement;
import ethereum.tutorials.java.ethereum.models.Comment;
import ethereum.tutorials.java.ethereum.services.repository.MongoRepoService;

@Controller
@CrossOrigin(origins = "#{'${client.url}'}", maxAge = 3600, allowCredentials = "true")
@RequestMapping(path = "/api")
public class MongoRepoController {

    @Autowired
    private MongoRepoService mongoSvc;

    @PostMapping(path = "/insert-announcement")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Boolean> insertAnnouncement(@RequestBody Announcement announcement) {
        boolean result = mongoSvc.insertAnnouncement(announcement);
        if (result)
            return ResponseEntity.status(HttpStatus.OK).body(result);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
    }

    @PutMapping(path = "/insert-announcement")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Boolean> editAnnouncement(@RequestBody Announcement announcement) {
        System.out.println(announcement);
        return null;
    }

    @GetMapping(path = "/get-announcements")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Announcement>> getAnnouncements(@RequestParam String projectAddress) {
        List<Announcement> announcements = mongoSvc.getAnnouncements(projectAddress);
        if (announcements.size() >= 1)
            return ResponseEntity.status(HttpStatus.OK).body(announcements);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping(path = "/get-comments")
    @ResponseBody
    public void getComments(@RequestParam String projectAddress) {

    }

    // consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE
    @PostMapping(path = "/insert-comment")
    @ResponseBody
    public ResponseEntity<Boolean> insertComments(@RequestBody Comment comment) {
        System.out.println("reading comments" + comment);
        boolean insertResult = mongoSvc.insertComment(comment);
        if (insertResult)
            return ResponseEntity.status(HttpStatus.OK).body(insertResult);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
    }
}
