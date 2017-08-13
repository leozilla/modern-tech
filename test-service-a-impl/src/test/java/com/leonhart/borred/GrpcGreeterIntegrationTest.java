package com.leonhart.borred;

import com.leonhart.borred.proto.echo.EchoGrpc;
import com.leonhart.borred.proto.echo.GreeterGrpc;
import com.leonhart.borred.proto.echo.HelloReply;
import com.leonhart.borred.proto.echo.HelloRequest;
import com.leonhart.borred.rpc.GrpcEchoServer;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Ignore
public class GrpcGreeterIntegrationTest {

    private GrpcEchoServer server;

    @Before
    public void before() throws InterruptedException, IOException {
        server = new GrpcEchoServer();
        server.start();
    }

    @After
    public void after() throws InterruptedException {
        server.stop();
        server.blockUntilShutdown();
    }

    @Test
    public void greet() {
        ManagedChannel channel = InProcessChannelBuilder.forAddress("localhost", 50050)
                .directExecutor()
                .build();
        EchoGrpc.EchoBlockingStub stub = EchoGrpc.newBlockingStub(channel);

        HelloRequest request = HelloRequest.newBuilder().setName("test").build();
        HelloReply response = stub.sayHello(request);

        assertThat(response.getMessage(), equalTo("Hello gRPC"));
    }
}
