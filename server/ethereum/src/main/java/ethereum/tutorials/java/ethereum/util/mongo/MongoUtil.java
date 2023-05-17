package ethereum.tutorials.java.ethereum.util.mongo;

import java.util.Date;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ethereum.tutorials.java.ethereum.models.Announcement;
import ethereum.tutorials.java.ethereum.models.Comment;

public class MongoUtil {
    private static final Logger logger = LoggerFactory.getLogger(MongoUtil.class);

    public static Document announcementToDocument(Announcement announce) {
        return new Document()
                // .append("contentType", announce.getContentType())
                .append("projectAddress", announce.getProjectAddress())
                .append("creatorAddress", announce.getCreatorAddress())
                .append("body", announce.getBody())
                .append("datetimePosted", announce.getDatetimePosted())
                .append("datetimeEdited", announce.getDatetimeEdited());
    }

    public static Announcement documentToAnnouncement(Document document) {
        // Announcement ann = new Announcement();
        // ann.setProjectAddress(document.getString("projectAddress"));
        // ann.setCreatorAddress(document.getString("creatorAddress"));
        // ann.setBody(document.getString("body"));
        // ann.setDatetimePosted(document.getDate("datetimePosted"));
        // ann.setDatetimeEdited((Date) document.getOrDefault("datetimeEdited", null));
        return new Announcement(
                document.getString("projectAddress"),
                document.getString("creatorAddress"),
                document.getString("body"),
                document.getDate("datetimePosted"),
                (Date) document.getOrDefault("datetimeEdited", null));
    }

    public static Document commentToDocument(Comment comment) {
        logger.info("converting comment to document, posted date >> ", comment.getDatetimePosted());
        return new Document()
                .append("projectAddress", comment.getProjectAddress())
                .append("posterAddress", comment.getPosterAddress())
                .append("body", comment.getBody())
                .append("datetimePosted", comment.getDatetimePosted());
    }

    public static Comment documentToComment(Document document) {
        return new Comment(
                document.getString("projectAddress"),
                document.getString("posterAddress"),
                document.getString("body"),
                document.getDate("datetimePosted"));
    }
}
