package ch.carve.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.LoggerFormat;
import io.vertx.ext.web.handler.impl.LoggerHandlerImpl;

public class ServerVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) {
        Router router = Router.router(vertx);
        router.route().handler(new LoggerHandlerImpl(LoggerFormat.SHORT));
        router.get("/").handler(this::hello);
        router.get("/sleep").blockingHandler(this::sleeping);

        // new HttpServerOptions().setLogActivity(true)
        vertx.createHttpServer().requestHandler(router::accept).listen(8080, result -> {
            if (result.succeeded()) {
                fut.complete();
            } else {
                fut.fail(result.cause());
            }
        });
    }

    private void sleeping(RoutingContext routingContext) {
        int duration = Integer.valueOf(routingContext.request().getParam("time"));
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
        }
        routingContext.response().end("Sleeping...");
    }

    private void hello(RoutingContext routingContext) {
        routingContext.response().end("Hello Vert.x");
    }
}
