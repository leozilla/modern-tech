package com.leonhart.borred.vehicles;

import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertx.rxjava.core.http.HttpServerResponse;

import static io.vertx.core.Vertx.vertx;

public class HttpEchoServerVehicle extends AbstractVerticle {

   @Override
   public void start() throws Exception {
      HttpServer server = vertx.createHttpServer();
      server.requestStream().toObservable().subscribe(req -> {
         HttpServerResponse resp = req.response();

         String contentType = req.getHeader("Content-Type");
         if (contentType != null) {
            resp.putHeader("Content-Type", contentType);
         }

         resp.setChunked(true);
         req.toObservable().subscribe(
                 buffer -> resp.write("Hello Vertx", "UTF-8"),
                 err -> {},
                 resp::end
         );
      });
      server.listen(8081);
   }
}
