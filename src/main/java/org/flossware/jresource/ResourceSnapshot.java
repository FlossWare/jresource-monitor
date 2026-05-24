package org.flossware.jresource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Immutable snapshot of resource usage at a point in time.
 */
public class ResourceSnapshot {
    private final long timestamp;
    private final long cpuTimeNanos;
    private final long heapUsedBytes;
    private final int threadCount;
    private final long bytesRead;
    private final long bytesWritten;
    private final Map<String, Object> customMetrics;

    public ResourceSnapshot(long timestamp, long cpuTimeNanos, long heapUsedBytes,
                          int threadCount, long bytesRead, long bytesWritten,
                          Map<String, Object> customMetrics) {
        this.timestamp = timestamp;
        this.cpuTimeNanos = cpuTimeNanos;
        this.heapUsedBytes = heapUsedBytes;
        this.threadCount = threadCount;
        this.bytesRead = bytesRead;
        this.bytesWritten = bytesWritten;
        this.customMetrics = customMetrics != null ?
                new HashMap<>(customMetrics) : Collections.emptyMap();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getCpuTimeNanos() {
        return cpuTimeNanos;
    }

    public long getHeapUsedBytes() {
        return heapUsedBytes;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public long getBytesRead() {
        return bytesRead;
    }

    public long getBytesWritten() {
        return bytesWritten;
    }

    public Map<String, Object> getCustomMetrics() {
        return Collections.unmodifiableMap(customMetrics);
    }

    @Override
    public String toString() {
        return String.format("ResourceSnapshot{timestamp=%d, cpu=%dns, heap=%dB, threads=%d, read=%dB, written=%dB}",
                timestamp, cpuTimeNanos, heapUsedBytes, threadCount, bytesRead, bytesWritten);
    }
}
