package sg.edu.nus.iss.paf_day28_workshop.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationPipeline;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
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

//    db.getCollection('games').aggregate(
//   [
//     { $match: { gid: 11 } },
//     {
//       $lookup: {
//         from: 'comments',
//         localField: 'gid',
//         foreignField: 'gid',
//         as: 'reviews'
//       }
//     },
//     {
//       $project: {
//         gid: 1,
//         name: 1,
//         year: 1,
//         ranking: 1,
//         users_rated: 1,
//         url: 1,
//         image: 1,
//         'reviews.c_id': 1
//       }
//     }
//   ],
//   { maxTimeMS: 60000, allowDiskUse: true }
// );

    public Document listReviewByIDAggregation(Integer id){
        MatchOperation match = Aggregation.match(Criteria.where("gid").is(id));
        LookupOperation lookup = Aggregation.lookup("comments", "gid", "gid", "reviews");
        ProjectionOperation project = Aggregation.project("gid", "name", "year", "ranking", "users_rated", "url", "image", "reviews.c_id");

        Aggregation pipeline = Aggregation.newAggregation(match, lookup, project);
        return template.aggregate(pipeline, "games", Document.class).getMappedResults().get(0);
    }
}
