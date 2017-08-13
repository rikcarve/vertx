package ch.carve.vertx;

import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.codahale.metrics.Slf4jReporter;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;

public class Main {
    private static final String REGISTRY_NAME = "vertx-metrics";

    public static void main(String[] args) {
        System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.SLF4JLogDelegateFactory");
        VertxOptions options = new VertxOptions()
                .setMetricsOptions(new DropwizardMetricsOptions().setEnabled(true).setRegistryName(REGISTRY_NAME));
        Vertx vertx = Vertx.vertx(options);
        MetricRegistry registry = SharedMetricRegistries.getOrCreate(REGISTRY_NAME);
        Slf4jReporter reporter = Slf4jReporter.forRegistry(registry)
                .outputTo(LoggerFactory.getLogger("metrics"))
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(30, TimeUnit.SECONDS);
        vertx.deployVerticle(new ServerVerticle());
    }
}
