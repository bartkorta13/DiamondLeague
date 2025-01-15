package pl.diamondleague.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PlayerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Player getPlayerSample1() {
        return new Player().id(1L).firstName("firstName1").lastName("lastName1").nickname("nickname1").height(1).yearOfBirth(1);
    }

    public static Player getPlayerSample2() {
        return new Player().id(2L).firstName("firstName2").lastName("lastName2").nickname("nickname2").height(2).yearOfBirth(2);
    }

    public static Player getPlayerRandomSampleGenerator() {
        return new Player()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .nickname(UUID.randomUUID().toString())
            .height(intCount.incrementAndGet())
            .yearOfBirth(intCount.incrementAndGet());
    }
}
