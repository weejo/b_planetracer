package at.jwe.rest;

import at.jwe.data.entity.ResultEntity;
import at.jwe.data.record.highscore.Highscore;
import at.jwe.data.record.*;
import at.jwe.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/")
public class PlanetracerController {

    @Autowired
    private DataService dataService;

    /**
     * Used to create new Levels - you can basically parse panda frames that have been parsed with df.to_json(orient='index') and with the columns: x & y
     * @param mapData the name + planets that should be put inside of the level
     * @return whether or not the level got created
     */
    @PostMapping(path="map",
            produces ="application/json",
            consumes ="application/json")
    public ResponseEntity addMap(@RequestBody MapData mapData) {

        dataService.addMap(mapData);

        return ResponseEntity.ok().build();
    }

    @GetMapping(path="clusters",
            produces ="application/json")
    public ResponseEntity<List<ResultEntity>> getClusters(@RequestParam Long levelId) {
        List<ResultEntity> clusters = dataService.getClusters(levelId);
        if(clusters == null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(clusters);
        }
    }

    @GetMapping(path="highscore",
    produces = "application/json")
    public ResponseEntity<Highscore> getHighscore(@RequestParam Long levelId) {
        return ResponseEntity.ok(dataService.getHighscore(levelId));
    }
    @PostMapping(path="result",
            produces ="application/json",
            consumes ="application/json")
    public ResponseEntity<Highscore> addResult(@RequestBody Result result) {
        return ResponseEntity.ok(dataService.addResult(result));
    }

    @GetMapping(path="leveldata",
            produces = "application/json")
    public ResponseEntity<LevelData> getLevelData() {
        return ResponseEntity.ok(dataService.getLevelData());
    }

    @GetMapping(path="levelinfo",
            produces = "application/json")
    public ResponseEntity<LevelOverview> getLevelOverview() {
        return ResponseEntity.ok(dataService.getLevelOverview());
    }
}
