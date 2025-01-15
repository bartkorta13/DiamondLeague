package pl.diamondleague.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StadiumTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Stadium getStadiumSample1() {
        return new Stadium().id(1L).name("name1").imagePath("imagePath1");
    }

    public static Stadium getStadiumSample2() {
        return new Stadium().id(2L).name("name2").imagePath("imagePath2");
    }

    public static Stadium getStadiumRandomSampleGenerator() {
        return new Stadium().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).imagePath(UUID.randomUUID().toString());
    }
}
