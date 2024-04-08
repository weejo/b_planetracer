package at.jwe.snyder.rest;

import at.jwe.snyder.data.record.LevelOverview;
import at.jwe.snyder.data.record.PlayerResult;
import at.jwe.snyder.data.record.data.MapData;
import at.jwe.snyder.data.record.highscore.Highscore;
import at.jwe.snyder.data.record.level.Level;
import at.jwe.snyder.data.record.output.Solution;
import at.jwe.snyder.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/")
class SnyderController {

    @Autowired
    private DataService dataService;

    /**
     * Used to create new Levels - you can basically parse panda frames that have been parsed with df.to_json(orient='index') and with the columns: x & y
     *
     * @param mapData the name + planets that should be put inside the level
     * @return whether the level got created
     */
    @PostMapping(path = "level",
            produces = "application/json",
            consumes = "application/json")
    public ResponseEntity<Level> addLevel(@RequestBody MapData mapData) {
        return ResponseEntity.ok(dataService.addLevel(mapData));
    }

    /**
     * Used to retrieve all user generated clusters for a given level.
     *
     * @param levelId the levelid you want all clusters from
     * @return all user generated clusters
     */

    @GetMapping(path = "getSolution",
            produces = "application/json")
    public ResponseEntity<Solution> getSolution(@RequestParam int levelId, @RequestParam String cutoff) {
        Solution solution = dataService.computeSolution(levelId, Float.parseFloat(cutoff));
        return ResponseEntity.ok(solution);
    }

    @GetMapping(path = "getSolution",
            produces = "application/json")
    public ResponseEntity<List<Solution>> getAllSolutions(@RequestParam int levelId) {
        List<Solution> result = dataService.getAllSolutions(levelId);
        return ResponseEntity.ok(result);
    }

    /**
     * Can be used to discover all existing levels - since accessing them to get clusters requires their ID.
     *
     * @return all exisitng levels in a short overview.
     */
    @GetMapping(path = "overview",
            produces = "application/json")
    public ResponseEntity<LevelOverview> getLevelOverview() {
        return ResponseEntity.ok(dataService.getLevelOverview());
    }

    @GetMapping(path = "highscore",
            produces = "application/json")
    public ResponseEntity<Highscore> getHighscore(@RequestParam int levelId) {
        return ResponseEntity.ok(dataService.getHighscore(levelId));
    }

    @PostMapping(path = "result",
            produces = "application/json",
            consumes = "application/json")
    public void addResult(@RequestBody PlayerResult playerResult) {
        dataService.addResult(playerResult);
    }

    @GetMapping(path = "level",
            produces = "application/json")
    public ResponseEntity<Level> getLevelData(@RequestParam int levelId) {
        return ResponseEntity.ok(dataService.getLevelData(levelId));
    }


}
