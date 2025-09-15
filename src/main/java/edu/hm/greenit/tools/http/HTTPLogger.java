package edu.hm.greenit.tools.http;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@QuarkusMain
public class HTTPLogger implements QuarkusApplication {

    private final List<String> httpEndpointsToMonitor = new ArrayList<>();

    public static void main(String[] args) {
        Quarkus.run(HTTPLogger.class, args);
    }

    @Override
    public int run(String... args) throws IOException, InterruptedException {
        parseArguments(args);
        Quarkus.waitForExit();
        return 0;
    }

    private void parseArguments(String[] args) {
        if (args == null) args = new String[0];

        for (String arg : args) {
            if (arg.startsWith("http")) {
                httpEndpointsToMonitor.add(arg);
            } else {
                throw new IllegalArgumentException("Invalid argument: " + arg + " please use a list (space seperated) following this pattern: http://<host>:<port>/endpoint");
            }
        }

    }

    @RunOnVirtualThread
    @Scheduled(cron = "${httplogger.cron}", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    void readAndDisplayHttpLoggerData() throws IOException {
        for (String httpEndpoint : httpEndpointsToMonitor) {
            fetchData(httpEndpoint);
        }
    }

    public void fetchData(final String httpEndpoint) {
        try (Client client = ClientBuilder.newClient()) {

            System.out.println("DATA:" + httpEndpoint + " at " + System.currentTimeMillis());

            String data = client.target(httpEndpoint)
                    .request()
                    .get(String.class);

            System.out.println(data);
        }
    }
}
