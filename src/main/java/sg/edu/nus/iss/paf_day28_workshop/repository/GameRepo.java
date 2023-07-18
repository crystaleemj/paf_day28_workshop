package sg.edu.nus.iss.paf_day28_workshop.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepo {

    @Autowired
    MongoTemplate template;
    
    public Document listGameByID(Integer id){
        Criteria c = Criteria.where("gid").is(id);
        Query q = Query.query(c);

        return template.findOne(q, Document.class, "games");
    }

   public List<Document> listReviewsByID(Integer id){
        Criteria c = Criteria.where("gid").is(id);
        Query q = Query.query(c);
        
        return template.find(q, Document.class, "comments");
   }
}
