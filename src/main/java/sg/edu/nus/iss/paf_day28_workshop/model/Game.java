package sg.edu.nus.iss.paf_day28_workshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    
    private Integer id;
    private String name;
    private Integer year;
    private Integer ranking;
    private Integer usersRated;
    private String url;
    private String image;
// gid
// 4
// name
// "Tal der Könige"
// year
// 1992
// ranking
// 4033
// users_rated
// 324
// url
// "https://www.boardgamegeek.com/boardgame/4/tal-der-konige"
// image
// "https://cf.geekdo-images.com/micro/img/x2IMJSPByASB1OF2B3WzFMnMZ4c=/fi…"
}
