package com.satoripop.rfp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TechnologyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Technology getTechnologySample1() {
        return new Technology().id(1L).name("name1").version("version1");
    }

    public static Technology getTechnologySample2() {
        return new Technology().id(2L).name("name2").version("version2");
    }

    public static Technology getTechnologyRandomSampleGenerator() {
        return new Technology().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).version(UUID.randomUUID().toString());
    }
}
