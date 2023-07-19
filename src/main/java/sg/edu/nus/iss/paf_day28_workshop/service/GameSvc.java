package sg.edu.nus.iss.paf_day28_workshop.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import sg.edu.nus.iss.paf_day28_workshop.repository.GameRepo;

@Service
public class GameSvc {
    
    @Autowired
    GameRepo repo;

    public Document listGameByID(Integer id){
        return repo.listGameByID(id);
    }

    public List<Document> listReviewsByID(Integer id){
        return repo.listReviewsByID(id);
    }

    // build Json 
    public JsonObject listGameByIDJson(Integer id){

        Document doc = listGameByID(id);
        JsonArrayBuilder array = Json.createArrayBuilder();
        List<Document> reviewList = listReviewsByID(id);
        List<String> list = new ArrayList<>();

        for (Document document : reviewList) {
            list.add(document.getObjectId("_id").toString());
        }

        for (String s : list) {
            array.add("/reviews/"+s);

        }

        JsonObjectBuilder object = Json.createObjectBuilder()
        .add("game_id", doc.getInteger("gid"))
        .add("name", doc.getString("name"))
        .add("year", doc.getInteger("year"))
        .add("rank", doc.getInteger("ranking"))
        .add("average", "")
        .add("users_rated", doc.getInteger("users_rated"))
        .add("url", doc.getString("url"))
        .add("thumbnail", doc.getString("image"))
        .add("reviews", array.build())
        .add("timestamp", new Date().toString());

        return object.build();
    }

    // below methods are for doing part A with aggregation

    public Document listReviewByIDAggregation(Integer id){
        return repo.listReviewByIDAggregation(id);

    }

    public JsonObject createJsonForAggregatedResult(Integer id){
        Document doc = listReviewByIDAggregation(id);
        JsonArrayBuilder array = Json.createArrayBuilder();
        List<Document> reviewList = listReviewsByID(id);
        List<String> list = new ArrayList<>();

        for (Document document : reviewList) {
            list.add(document.getObjectId("_id").toString());
        }

         for (String s : list) {
            array.add("/reviews/"+s);

        }

        JsonObjectBuilder object = Json.createObjectBuilder()
        .add("game_id", doc.getInteger("gid"))
        .add("name", doc.getString("name"))
        .add("year", doc.getInteger("year"))
        .add("rank", doc.getInteger("ranking"))
        .add("average", "")
        .add("users_rated", doc.getInteger("users_rated"))
        .add("url", doc.getString("url"))
        .add("thumbnail", doc.getString("image"))
        .add("reviews", array)
        .add("timestamp", new Date().toString());

        return object.build();
    }
}
