package ethereum.controllers;

import java.util.List;
import java.util.Optional;

import javax.print.attribute.standard.Media;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import ethereum.models.mongo.Announcement;
import ethereum.models.mongo.Comment;
import ethereum.repository.mongo.MongoRepo;
import ethereum.services.repository.MongoRepoService;

@Controller
@CrossOrigin(origins = "#{'${client.url}'}", maxAge = 3600, allowCredentials = "true")
@RequestMapping(path = "/api")
public class MongoRepoController {

    @Autowired
    private MongoRepoService mongoSvc;
    @Autowired
    private MongoRepo mongoRepo;

    private static final Logger logger = LoggerFactory.getLogger(MongoRepoController.class);

    @PostMapping(path = "/insert-announcement", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Boolean> insertAnnouncement(@RequestBody Announcement announcement) {
        boolean result = mongoSvc.insertAnnouncement(announcement);
        if (result)
            return ResponseEntity.status(HttpStatus.OK).body(result);
        return ResponseEntity.badRequest().body(false);
    }

    @PutMapping(path = "/edit-announcement")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Boolean> editAnnouncement(@RequestBody Announcement announcement) {
        boolean result = mongoSvc.updateAnnouncement(announcement);
        if (result)
            return ResponseEntity.status(HttpStatus.OK).body(null);
        return ResponseEntity.badRequest().body(null);

    }

    @GetMapping(path = "/get-announcements")
    @ResponseBody
    public ResponseEntity<List<Announcement>> getAnnouncements(@RequestParam String projectAddress) {
        logger.info("Get announcements, project address >> {}", projectAddress);
        List<Announcement> announcements = mongoSvc.getAnnouncements(projectAddress);
        if (announcements.size() >= 1)
            return ResponseEntity.status(HttpStatus.OK).body(announcements);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping(path = "/get-announcements-by-page")
    @ResponseBody
    public ResponseEntity<List<Announcement>> getAnnouncements(
            @RequestParam String projectAddress,
            @RequestParam int offset,
            @RequestParam int limit) {
        logger.info("Get announcements, project address >> {}", projectAddress);

        List<Announcement> announcements = mongoSvc.getAnnouncements(projectAddress, offset, limit);
        if (announcements.size() >= 1)
            return ResponseEntity.status(HttpStatus.OK).body(announcements);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping(path = "/count-announcements")
    @ResponseBody
    public ResponseEntity<Long> countAnnouncements(@RequestParam String projectAddress) {
        logger.info("count announcements for project >> {}", projectAddress);
        Long count = mongoRepo.countAnnouncements(projectAddress);
        return ResponseEntity.ok().body(count);
    }

    @GetMapping(path = "/get-comments-by-page")
    @ResponseBody
    public ResponseEntity<List<Comment>> getComments(
            @RequestParam String projectAddress,
            @RequestParam int offset,
            @RequestParam int limit) {
        List<Comment> comments = mongoSvc.getComments(projectAddress, offset, limit);
        if (comments.size() >= 1)
            return ResponseEntity.status(HttpStatus.OK).body(comments);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping(path = "/count-comments")
    @ResponseBody
    public ResponseEntity<Long> countComments(@RequestParam String projectAddress) {
        logger.info("count comments for project >> {}", projectAddress);
        Long count = mongoRepo.countComments(projectAddress);
        return ResponseEntity.ok().body(count);
    }

    // consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE
    @PostMapping(path = "/insert-comment")
    @ResponseBody
    public ResponseEntity<Boolean> insertComments(@RequestBody Comment comment) {
        logger.info("insert comments, comment >> {}", comment);
        boolean insertResult = mongoSvc.insertComment(comment);
        if (insertResult)
            return ResponseEntity.status(HttpStatus.OK).body(insertResult);
        return ResponseEntity.badRequest().body(false);
    }
}
