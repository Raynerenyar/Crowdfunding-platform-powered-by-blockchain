package ethereum.repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;

import ethereum.models.Announcement;
import ethereum.util.mongo.MongoUtil;
import ethereum.util.mongo.MongoUtil.*;
import ethereum.models.Comment;

@Repository
public class MongoRepo {

    @Autowired
    private MongoTemplate mongo;
    private static final Logger logger = LoggerFactory.getLogger(MongoRepo.class);

    public Document insertAnnouncement(Announcement announce) {
        Document doc = MongoUtil.announcementToDocument(announce);
        logger.info("inserting announcement");
        return mongo.insert(doc, "announcements");
    }

    public long updateAnnouncement(Announcement announcement) {
        Criteria criteria = Criteria.where("datetimePosted").is(announcement.getDatetimePosted());
        Query query = Query.query(criteria);
        Update update = Update
                .update("body", announcement.getBody())
                .set("datetimeEdited", announcement.getDatetimeEdited());
        UpdateResult result = mongo.updateFirst(query, update, Document.class, "announcements");
        return result.getModifiedCount();
    }

    public List<Document> getAnnouncements(String projectAddress) {
        Criteria criteria = Criteria.where("projectAddress").is(projectAddress);
        Query query = Query.query(criteria);
        return mongo.find(query, Document.class, "announcements");
    }

    public List<Document> getAnnouncements(String projectAddress, int offset, int limit) {
        Criteria criteria = Criteria.where("projectAddress").is(projectAddress);
        Sort sort = Sort.by(Sort.Direction.DESC, "datetimePosted");
        Query query = Query.query(criteria).with(sort).limit(limit).skip(offset);
        return mongo.find(query, Document.class, "announcements");
    }

    // db.getCollection("announcements").countDocuments({projectAddress:"0x027edc86a9b0b9487ed2adec2d58d3ae28274ba9"})
    public long countAnnouncements(String projectAddress) {
        Criteria criteria = Criteria.where("projectAddress").is(projectAddress);
        Query query = Query.query(criteria);
        return mongo.count(query, Document.class, "announcements");
    }

    public Document insertComment(Comment comment) {
        Document doc = MongoUtil.commentToDocument(comment);
        logger.info("inserting comment");
        return mongo.insert(doc, "comments");
    }

    public List<Document> getComments(String projectAddress) {
        Criteria criteria = Criteria.where("projectAddress").is(projectAddress);
        Query query = Query.query(criteria);
        return mongo.find(query, Document.class, "comments");
    }

    public List<Document> getComments(String projectAddress, int offset, int limit) {
        Criteria criteria = Criteria.where("projectAddress").is(projectAddress);
        Sort sort = Sort.by(Sort.Direction.DESC, "datetimePosted");
        Query query = Query.query(criteria).with(sort).limit(limit).skip(offset);
        return mongo.find(query, Document.class, "comments");
    }

    public long countComments(String projectAddress) {
        Criteria criteria = Criteria.where("projectAddress").is(projectAddress);
        Query query = Query.query(criteria);
        return mongo.count(query, Document.class, "comments");
    }

}
