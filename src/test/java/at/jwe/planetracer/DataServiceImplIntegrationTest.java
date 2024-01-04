package at.jwe.planetracer;

import at.jwe.planetracer.data.record.data.DataPoint;
import at.jwe.planetracer.data.record.data.MapData;
import at.jwe.planetracer.repository.LevelRepository;
import at.jwe.planetracer.service.DataServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class DataServiceImplIntegrationTest {

    @Autowired
    DataServiceImpl dataService;

    @Autowired
    LevelRepository levelRepository;

    @Test
    public void test_evenly_istributed_map() {
        List<DataPoint> dataPoints = new ArrayList<>();
        long counter = 0L;
        for (long width = 0L; width < 100L; width++) {
            for (long height = 0L; height < 100L; height++) {
                dataPoints.add(new DataPoint(width, height, counter));
                counter++;
            }
        }

        dataService.addLevel(new MapData("Test", 50L, 0.1, dataPoints));
    }

    @Test
    public void test_small_values() {
        List<DataPoint> dataPoints = new ArrayList<>();
        long size = 10L;
        long counter = 0L;
        for (long width = 0L; width < size; width++) {
            for (long height = 0L; height < size; height++) {
                dataPoints.add(new DataPoint(width, height, counter));
                counter++;
            }
        }

        dataService.addLevel(new MapData("Test", 1L, 0.5, dataPoints));
    }
}
