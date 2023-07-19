package sg.edu.nus.iss.paf_day28_workshop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.paf_day28_workshop.service.GameSvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;






@RestController
@RequestMapping
public class GameRestController {

    @Autowired
    GameSvc svc;
    
    // @GetMapping(path = "/game/{game_id}/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<?> listGameReviewsByID(@PathVariable Integer game_id) {
        
    //     return new ResponseEntity<>(svc.listGameByIDJson(game_id).toString(), HttpStatus.OK);
    // }

    // method with aggregation
    @GetMapping(path = "/game/{game_id}/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> listAggregatedReviewResult(@PathVariable Integer game_id) {
        return new ResponseEntity<String>(svc.createJsonForAggregatedResult(game_id).toString(), HttpStatus.OK);
    }

    // list game by id and display review with highest rating 
    // where path variable {rating} = highest/lowest
    @GetMapping(path = "/games/{rating}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> listGameReviewRating(@PathVariable String rating) {

        return new ResponseEntity<String>(svc.returnRatedGamesJson(rating).toString(), HttpStatus.OK);
    }
    
    
}
