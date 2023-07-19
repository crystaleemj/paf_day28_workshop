package sg.edu.nus.iss.paf_day28_workshop.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationPipeline;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
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

// db.getCollection('games').aggregate(
//   [
//     { $limit: 10 },
//     {
//       $lookup: {
//         from: 'comments',
//         localField: 'gid',
//         foreignField: 'gid',
//         as: 'reviews'
//       }
//     },
//     { $unwind: { path: '$reviews' } },
//     { $sort: { 'reviews.rating': -1 } },
//     {
//       $group: {
//         _id: '$gid',
//         name: { $first: '$name' },
//         rating: { $first: '$reviews.rating' },
//         user: { $first: '$reviews.user' },
//         comment: { $first: '$reviews.c_text' },
//         review_id: { $first: '$reviews.c_id' }
//       }
//     },
//     { $sort: { _id: 1 } }
//   ],
//   { maxTimeMS: 60000, allowDiskUse: true }
// );
    
    public List<Document> listGameByReviewRatings(String rating){
        LimitOperation limit = Aggregation.limit(5);
        LookupOperation lookup = Aggregation.lookup("comments", "gid", "gid", "reviews");
        UnwindOperation unwind = Aggregation.unwind("reviews");
        SortOperation sortDesc = Aggregation.sort(Sort.Direction.DESC, "reviews.rating");
        SortOperation sortAsc = Aggregation.sort(Sort.Direction.ASC, "reviews.rating");
        
        GroupOperation group = Aggregation.group("gid")
        .first("name").as("name")
        .first("reviews.rating").as("rating")
        .first("reviews.user").as("user")
        .first("reviews.c_text").as("comment")
        .first("reviews.c_id").as("review_id");
        // ProjectionOperation project = Aggregation.project("_id","name","rating","user","comment","review_id");
        SortOperation sortID = Aggregation.sort(Sort.Direction.ASC, "_id");
        Aggregation pipelineHighest = Aggregation.newAggregation(limit, lookup, unwind, sortDesc, group, sortID);
        Aggregation pipelineLowest = Aggregation.newAggregation(limit, lookup, unwind, sortAsc, group, sortID);

        if (rating.equals("highest")) {
            return template.aggregate(pipelineHighest, "games", Document.class).getMappedResults();
        }

        if (rating.equals("lowest")) {
            return template.aggregate(pipelineLowest, "games", Document.class).getMappedResults();
        }
        
        return null;
    }
}
