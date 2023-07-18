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
    
    @GetMapping(path = "/game/{game_id}/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listGameReviewsByID(@PathVariable Integer game_id) {
        
        return new ResponseEntity<>(svc.listGameByIDJson(game_id).toString(), HttpStatus.OK);
    }
    
}
