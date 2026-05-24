# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0] - 2026-05-24

### Added
- Initial release of JResource Monitor
- `ResourceSnapshot` - Immutable snapshot of resource usage (CPU, memory, threads, I/O)
- `ResourceQuota` - Builder-based quota definition and enforcement
- `ResourceQuotaExceededException` - Exception for quota violations
- Support for custom metrics via flexible Map structure
- Comprehensive test coverage (8 passing tests)
- Zero dependencies (except SLF4J API)
- Builder pattern for fluent configuration
- Thread-safe immutable data structures

### Features
- Track CPU time in nanoseconds
- Monitor heap memory usage in bytes
- Count active threads
- Track I/O operations (bytes read/written)
- Custom metrics support
- Quota enforcement with detailed error messages
- Fail-fast validation for negative values

[1.0]: https://github.com/FlossWare/jresource-monitor/releases/tag/v1.0
