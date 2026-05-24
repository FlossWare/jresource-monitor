package org.flossware.jresource;

import java.util.Optional;

/**
 * Defines resource limits (quotas). All limits are optional.
 */
public class ResourceQuota {
    private final Optional<Long> maxHeapBytes;
    private final Optional<Integer> maxThreadCount;
    private final Optional<Long> maxCpuTimeNanos;

    private ResourceQuota(Builder builder) {
        this.maxHeapBytes = builder.maxHeapBytes;
        this.maxThreadCount = builder.maxThreadCount;
        this.maxCpuTimeNanos = builder.maxCpuTimeNanos;
    }

    public Optional<Long> getMaxHeapBytes() {
        return maxHeapBytes;
    }

    public Optional<Integer> getMaxThreadCount() {
        return maxThreadCount;
    }

    public Optional<Long> getMaxCpuTimeNanos() {
        return maxCpuTimeNanos;
    }

    public void enforce(ResourceSnapshot snapshot) throws ResourceQuotaExceededException {
        maxHeapBytes.ifPresent(max -> {
            if (snapshot.getHeapUsedBytes() > max) {
                throw new ResourceQuotaExceededException(
                        "Heap quota exceeded: " + snapshot.getHeapUsedBytes() + " > " + max);
            }
        });

        maxThreadCount.ifPresent(max -> {
            if (snapshot.getThreadCount() > max) {
                throw new ResourceQuotaExceededException(
                        "Thread count quota exceeded: " + snapshot.getThreadCount() + " > " + max);
            }
        });

        maxCpuTimeNanos.ifPresent(max -> {
            if (snapshot.getCpuTimeNanos() > max) {
                throw new ResourceQuotaExceededException(
                        "CPU time quota exceeded: " + snapshot.getCpuTimeNanos() + " > " + max);
            }
        });
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Optional<Long> maxHeapBytes = Optional.empty();
        private Optional<Integer> maxThreadCount = Optional.empty();
        private Optional<Long> maxCpuTimeNanos = Optional.empty();

        public Builder maxHeapBytes(long bytes) {
            if (bytes < 0) {
                throw new IllegalArgumentException("maxHeapBytes must be >= 0, got: " + bytes);
            }
            this.maxHeapBytes = Optional.of(bytes);
            return this;
        }

        public Builder maxThreadCount(int count) {
            if (count < 0) {
                throw new IllegalArgumentException("maxThreadCount must be >= 0, got: " + count);
            }
            this.maxThreadCount = Optional.of(count);
            return this;
        }

        public Builder maxCpuTimeNanos(long nanos) {
            if (nanos < 0) {
                throw new IllegalArgumentException("maxCpuTimeNanos must be >= 0, got: " + nanos);
            }
            this.maxCpuTimeNanos = Optional.of(nanos);
            return this;
        }

        public ResourceQuota build() {
            return new ResourceQuota(this);
        }
    }
}
