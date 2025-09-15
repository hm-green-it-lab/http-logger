# HTTP-Logger

A Quarkus-based CLI application for periodically fetching data from one or more HTTP endpoints. It is designed for lightweight telemetry and experiments where services expose metrics or logs via HTTP.

It currently supports monitoring:

- Arbitrary HTTP endpoints via GET requests
- Multiple endpoints in one run (polled sequentially)

## 📦 Features

- ⏱ Scheduled HTTP collection every X seconds via Quarkus Cron
- 🥵 Runs on virtual threads for lightweight concurrency
- 📈 Outputs responses in a consistent, parseable format
- 📁 Supports monitoring multiple endpoints in a single run
- 🧪 Ideal for experiments and lightweight telemetry

## 📄 Output Format

All outputs follow a simple two-line format:

```csv
DATA:<endpoint> at <timestamp>
<response-body>
```

Errors or unavailable endpoints raise an exception with a stack trace.

## 🚀 Usage

### 🔧 Prerequisites

- Java 17+
- Maven or Quarkus CLI (for building)
- At least one reachable HTTP endpoint

### 🚪 Running the Application

```bash
# Runs with default interval (1000ms)
java -jar http-logger-1.0-runner.jar http://localhost:8080/metrics

# Multiple endpoints
java -jar http-logger-1.0-runner.jar http://localhost:8080/metrics http://10.0.0.5:9090/status

# Custom Cron schedule (Every 5 seconds)
java -Dhttplogger.cron="*/5 * * * * ?" -jar http-logger-1.0-runner.jar http://localhost:8080/metrics
```

### ⚠️ Caveats

- Only GET requests are supported
- Endpoints must start with http
- Endpoints are fetched sequentially within each scheduled run
- Interval/Schedule is set via `-Dhttplogger.cron="*/X * * * * ?"` (not CLI args)
