package pl.diamondleague.app.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RatingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Rating getRatingSample1() {
        return new Rating().id(1L).attack(1).defense(1).engagement(1).overall(1);
    }

    public static Rating getRatingSample2() {
        return new Rating().id(2L).attack(2).defense(2).engagement(2).overall(2);
    }

    public static Rating getRatingRandomSampleGenerator() {
        return new Rating()
            .id(longCount.incrementAndGet())
            .attack(intCount.incrementAndGet())
            .defense(intCount.incrementAndGet())
            .engagement(intCount.incrementAndGet())
            .overall(intCount.incrementAndGet());
    }
}
