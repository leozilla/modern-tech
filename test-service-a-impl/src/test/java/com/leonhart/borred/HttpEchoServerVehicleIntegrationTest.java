package com.leonhart.borred;

import com.leonhart.borred.vehicles.HttpEchoServerVehicle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(VertxUnitRunner.class)
public class HttpEchoServerVehicleIntegrationTest {
    private static final int PORT = 8081;
    private Vertx vertx;

    @Before
    public void before(final TestContext context) {
        vertx = Vertx.vertx();
        vertx.exceptionHandler(context.exceptionHandler());

        vertx.deployVerticle(new HttpEchoServerVehicle());
    }

    @Test
    public void echo(final TestContext context) {
        Async async = context.async();

        HttpClient client = Vertx.vertx().createHttpClient();
        client.put(PORT, "localhost", "/", resp -> {
            resp.exceptionHandler(context.exceptionHandler());
            resp.bodyHandler(body -> {
                assertThat(body.toString(), is("Hello Vertx"));
                client.close();
                async.complete();
            });
        }).setChunked(true).putHeader("Content-Type", "text/plain").write("hello").end();
    }
}
