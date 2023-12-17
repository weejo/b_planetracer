package at.jwe.planetracer;

import at.jwe.planetracer.data.record.DataPoint;
import at.jwe.planetracer.data.record.MapData;
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
        for (long width = 0L; width < 1000L; width++) {
            for (long height = 0L; height < 1000L; height++) {
                dataPoints.add(new DataPoint(width, height, counter));
                counter++;
            }
        }

        dataService.addLevel(new MapData("Test", 50L, 1000L, 0.2, dataPoints, 1920L, 1080L));
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

        dataService.addLevel(new MapData("Test", 1L, 100L, 0.2, dataPoints, 1920L, 1080L));
    }
}
