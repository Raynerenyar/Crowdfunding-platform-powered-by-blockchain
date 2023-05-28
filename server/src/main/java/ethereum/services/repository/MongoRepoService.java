package ethereum.services.repository;

import java.util.Collections;
import java.util.List;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ethereum.models.mongo.Announcement;
import ethereum.models.mongo.Comment;
import ethereum.repository.mongo.MongoRepo;

import static ethereum.util.mongo.MongoUtil.*;

@Service
public class MongoRepoService {

    @Autowired
    private MongoRepo mongoRepo;

    //	db.comments.insertOne({Document})
    public boolean insertAnnouncement(Announcement announcement) {
        Document doc = mongoRepo.insertAnnouncement(announcement);
        if (doc != null)
            return true;
        return false;
    }

    public boolean updateAnnouncement(Announcement announcement) {
        long modifiedCount = mongoRepo.updateAnnouncement(announcement);
        if (modifiedCount > 0)
            return true;
        return false;
    }

    public List<Announcement> getAnnouncements(String projectAddress) {
        List<Document> docs = mongoRepo.getAnnouncements(projectAddress);
        if (docs.size() >= 1) {
            return docs.stream()
                    .map(doc -> documentToAnnouncement(doc))
                    .toList();
        }
        return Collections.emptyList();
    }

    public List<Announcement> getAnnouncements(String projectAddress, int offset, int limit) {
        List<Document> docs = mongoRepo.getAnnouncements(projectAddress, offset, limit);
        if (docs.size() >= 1) {
            return docs.stream()
                    .map(doc -> documentToAnnouncement(doc))
                    .toList();
        }
        return Collections.emptyList();
    }

    public boolean insertComment(Comment comment) {
        Document doc = mongoRepo.insertComment(comment);
        if (doc != null)
            return true;
        return false;
    }

    public List<Comment> getComments(String projectAddress) {
        List<Document> docs = mongoRepo.getComments(projectAddress);
        if (docs.size() >= 1)
            return docs.stream().map(doc -> documentToComment(doc)).toList();
        return Collections.emptyList();
    }

    public List<Comment> getComments(String projectAddress, int offset, int limit) {
        List<Document> docs = mongoRepo.getComments(projectAddress, offset, limit);
        if (docs.size() >= 1)
            return docs.stream().map(doc -> documentToComment(doc)).toList();
        return Collections.emptyList();
    }

}
