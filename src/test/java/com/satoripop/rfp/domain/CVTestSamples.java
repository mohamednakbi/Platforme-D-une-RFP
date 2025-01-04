package com.satoripop.rfp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CVTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CV getCVSample1() {
        return new CV().id(1L).title("title1").content("content1");
    }

    public static CV getCVSample2() {
        return new CV().id(2L).title("title2").content("content2");
    }

    public static CV getCVRandomSampleGenerator() {
        return new CV().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString()).content(UUID.randomUUID().toString());
    }
}
