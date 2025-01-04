package com.satoripop.rfp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReferenceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Reference getReferenceSample1() {
        return new Reference().id(1L).title("title1").content("content1").lastmodified("lastmodified1");
    }

    public static Reference getReferenceSample2() {
        return new Reference().id(2L).title("title2").content("content2").lastmodified("lastmodified2");
    }

    public static Reference getReferenceRandomSampleGenerator() {
        return new Reference()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .content(UUID.randomUUID().toString())
            .lastmodified(UUID.randomUUID().toString());
    }
}
