package org.flossware.jresource;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResourceQuotaTest {

    @Test
    void testBuilderNegativeHeap() {
        assertThrows(IllegalArgumentException.class, () -> {
            ResourceQuota.builder().maxHeapBytes(-1).build();
        });
    }

    @Test
    void testBuilderNegativeThreads() {
        assertThrows(IllegalArgumentException.class, () -> {
            ResourceQuota.builder().maxThreadCount(-1).build();
        });
    }

    @Test
    void testBuilderNegativeCpu() {
        assertThrows(IllegalArgumentException.class, () -> {
            ResourceQuota.builder().maxCpuTimeNanos(-1).build();
        });
    }

    @Test
    void testEnforceHeapQuota() {
        ResourceQuota quota = ResourceQuota.builder()
                .maxHeapBytes(100 * 1024 * 1024)
                .build();

        ResourceSnapshot snapshot = new ResourceSnapshot(
                System.currentTimeMillis(), 1_000_000_000L, 200 * 1024 * 1024L, 10, 0L, 0L, null);

        assertThrows(ResourceQuotaExceededException.class, () -> quota.enforce(snapshot));
    }

    @Test
    void testEnforceThreadQuota() {
        ResourceQuota quota = ResourceQuota.builder()
                .maxThreadCount(5)
                .build();

        ResourceSnapshot snapshot = new ResourceSnapshot(
                System.currentTimeMillis(), 1_000_000_000L, 50 * 1024 * 1024L, 10, 0L, 0L, null);

        assertThrows(ResourceQuotaExceededException.class, () -> quota.enforce(snapshot));
    }

    @Test
    void testEnforceCpuQuota() {
        ResourceQuota quota = ResourceQuota.builder()
                .maxCpuTimeNanos(5_000_000_000L)
                .build();

        ResourceSnapshot snapshot = new ResourceSnapshot(
                System.currentTimeMillis(), 10_000_000_000L, 50 * 1024 * 1024L, 5, 0L, 0L, null);

        assertThrows(ResourceQuotaExceededException.class, () -> quota.enforce(snapshot));
    }

    @Test
    void testEnforceWithinQuota() {
        ResourceQuota quota = ResourceQuota.builder()
                .maxHeapBytes(200 * 1024 * 1024)
                .maxThreadCount(20)
                .maxCpuTimeNanos(60_000_000_000L)
                .build();

        ResourceSnapshot snapshot = new ResourceSnapshot(
                System.currentTimeMillis(), 10_000_000_000L, 100 * 1024 * 1024L, 10, 0L, 0L, null);

        assertDoesNotThrow(() -> quota.enforce(snapshot));
    }

    @Test
    void testEmptyQuota() {
        ResourceQuota quota = ResourceQuota.builder().build();

        ResourceSnapshot snapshot = new ResourceSnapshot(
                System.currentTimeMillis(), Long.MAX_VALUE, Long.MAX_VALUE, Integer.MAX_VALUE, 0L, 0L, null);

        assertDoesNotThrow(() -> quota.enforce(snapshot));
    }
}
