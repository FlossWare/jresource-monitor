# JResource Monitor

[![Maven Central](https://img.shields.io/maven-central/v/org.flossware/resource-monitor-java.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/org.flossware/resource-monitor-java)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Resource usage tracking and quota enforcement for Java applications.

## Features

- **Resource Monitoring**: Track CPU time, heap memory, thread count, and I/O metrics
- **Quota Enforcement**: Set limits on resource usage and enforce them at runtime
- **Immutable Snapshots**: Thread-safe resource usage snapshots
- **Custom Metrics**: Support for application-specific metrics via flexible Map structure
- **Zero Dependencies**: Pure Java with no external dependencies (except SLF4J API)
- **Builder Pattern**: Fluent API for configuration

## Installation

### Maven

```xml
<dependency>
    <groupId>org.flossware</groupId>
    <artifactId>resource-monitor-java</artifactId>
    <version>1.0</version>
</dependency>
```

### Gradle

```gradle
implementation 'org.flossware:resource-monitor-java:1.0'
```

## Quick Start

### Capturing Resource Snapshots

```java
import org.flossware.jresource.ResourceSnapshot;

// Capture current resource usage
ResourceSnapshot snapshot = new ResourceSnapshot(
    System.currentTimeMillis(),
    1_000_000_000L,  // CPU time in nanoseconds
    134_217_728L,     // Heap used in bytes (128 MB)
    10,               // Thread count
    1024L,            // Bytes read
    2048L,            // Bytes written
    Map.of("custom-metric", 42)  // Custom metrics
);

System.out.println("Heap used: " + snapshot.heapUsedBytes() + " bytes");
System.out.println("Thread count: " + snapshot.threadCount());
```

### Enforcing Resource Quotas

```java
import org.flossware.jresource.ResourceQuota;
import org.flossware.jresource.ResourceQuotaExceededException;

// Define resource limits
ResourceQuota quota = ResourceQuota.builder()
    .maxHeapBytes(512 * 1024 * 1024)  // 512 MB heap limit
    .maxThreadCount(50)                // Maximum 50 threads
    .maxCpuTimeNanos(60_000_000_000L)  // 60 seconds CPU time
    .build();

// Enforce the quota
try {
    quota.enforce(snapshot);
    System.out.println("Resource usage within limits");
} catch (ResourceQuotaExceededException e) {
    System.err.println("Quota exceeded: " + e.getMessage());
    // Take corrective action
}
```

### Complete Example

```java
import org.flossware.jresource.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;

public class ResourceMonitorExample {
    
    public static void main(String[] args) {
        // Set up quota
        ResourceQuota quota = ResourceQuota.builder()
            .maxHeapBytes(256 * 1024 * 1024)  // 256 MB
            .maxThreadCount(20)
            .build();
        
        // Capture current state
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        
        ResourceSnapshot snapshot = new ResourceSnapshot(
            System.currentTimeMillis(),
            threadBean.getCurrentThreadCpuTime(),
            memoryBean.getHeapMemoryUsage().getUsed(),
            threadBean.getThreadCount(),
            0L,
            0L,
            null
        );
        
        // Check against quota
        try {
            quota.enforce(snapshot);
            System.out.println("✓ Resource usage OK");
        } catch (ResourceQuotaExceededException e) {
            System.err.println("✗ " + e.getMessage());
        }
    }
}
```

## API Overview

### ResourceSnapshot

Immutable snapshot of resource usage at a point in time.

**Constructor Parameters:**
- `timestamp` - Timestamp when snapshot was captured
- `cpuTimeNanos` - Total CPU time consumed in nanoseconds
- `heapUsedBytes` - Heap memory used in bytes
- `threadCount` - Number of active threads
- `bytesRead` - Total bytes read from I/O
- `bytesWritten` - Total bytes written to I/O
- `customMetrics` - Optional custom metrics (can be null)

**Methods:**
- `timestamp()` - Get snapshot timestamp
- `cpuTimeNanos()` - Get CPU time
- `heapUsedBytes()` - Get heap usage
- `threadCount()` - Get thread count
- `bytesRead()` - Get bytes read
- `bytesWritten()` - Get bytes written
- `customMetrics()` - Get custom metrics (immutable)

### ResourceQuota

Defines resource limits and enforces them.

**Builder Methods:**
- `maxHeapBytes(long)` - Set maximum heap memory in bytes
- `maxThreadCount(int)` - Set maximum thread count
- `maxCpuTimeNanos(long)` - Set maximum CPU time in nanoseconds
- `build()` - Build the quota

**Methods:**
- `enforce(ResourceSnapshot)` - Enforce quota, throws `ResourceQuotaExceededException` if exceeded
- `getMaxHeapBytes()` - Get heap limit
- `getMaxThreadCount()` - Get thread limit
- `getMaxCpuTimeNanos()` - Get CPU time limit

### ResourceQuotaExceededException

Runtime exception thrown when resource quota is exceeded.

## Use Cases

1. **Application Resource Management**: Prevent applications from consuming excessive resources
2. **Multi-Tenant Systems**: Enforce per-tenant resource quotas
3. **Testing**: Verify applications operate within resource constraints
4. **Monitoring**: Track resource usage trends over time
5. **Circuit Breakers**: Trigger circuit breakers when resource limits are approached

## Design Principles

- **Immutability**: All classes are immutable and thread-safe
- **Builder Pattern**: Fluent configuration API
- **Fail-Fast Validation**: Invalid values rejected at construction time
- **Zero Dependencies**: Minimal external dependencies for maximum compatibility

## Requirements

- Java 21 or higher
- SLF4J API (for logging in quota enforcement)

## License

MIT License - see [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

## Links

- [GitHub Repository](https://github.com/FlossWare/resource-monitor-java)
- [Issue Tracker](https://github.com/FlossWare/resource-monitor-java/issues)
- [Javadoc](https://javadoc.io/doc/org.flossware/resource-monitor-java)

## Changelog

See [CHANGELOG.md](CHANGELOG.md) for version history.
