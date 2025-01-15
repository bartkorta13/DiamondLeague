package pl.diamondleague.app.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class GameTeamTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static GameTeam getGameTeamSample1() {
        return new GameTeam().id(1L).goals(1);
    }

    public static GameTeam getGameTeamSample2() {
        return new GameTeam().id(2L).goals(2);
    }

    public static GameTeam getGameTeamRandomSampleGenerator() {
        return new GameTeam().id(longCount.incrementAndGet()).goals(intCount.incrementAndGet());
    }
}
