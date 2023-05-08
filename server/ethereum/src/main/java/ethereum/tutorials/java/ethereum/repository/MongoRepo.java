package ethereum.tutorials.java.ethereum.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.MongoCollection;

import ethereum.tutorials.java.ethereum.models.Announcement;
import ethereum.tutorials.java.ethereum.util.mongo.MongoUtil;
import ethereum.tutorials.java.ethereum.util.mongo.MongoUtil.*;
import ethereum.tutorials.java.ethereum.models.Comment;

@Repository
public class MongoRepo {

    @Autowired
    private MongoTemplate mongo;

    public Document insertAnnouncement(Announcement announce) {
        Document doc = MongoUtil.announcementToDocument(announce);
        System.out.println("insert document");
        return mongo.insert(doc, "announcements");
    }

    public long editAnnouncement(Announcement announce) {
        Criteria criteria = Criteria.where("datetimePosted").is(announce.getDatetimePosted());
        Query query = Query.query(criteria);
        Update update = new Update()
                .set("body", announce.getBody())
                .set("datetimeEdited", announce.getDatetimeEdited());
        return mongo.updateFirst(query, update, "myCollection").getModifiedCount();
    }

    public List<Document> getAnnouncements(String projectAddress) {
        Criteria criteria = Criteria.where("projectAddress").is(projectAddress);
        Query query = Query.query(criteria);
        return mongo.find(query, Document.class, "announcements");
    }

    public Document insertComment(Comment comment) {
        Document doc = MongoUtil.commentToDocument(comment);
        System.out.println("inserting comment");
        return mongo.insert(doc, "comments");
    }

}
