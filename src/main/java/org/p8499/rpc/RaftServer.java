package org.p8499.rpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.p8499.rpc.service.ReplicationImpl;

import java.io.IOException;

public class RaftServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8001)
                .addService(new ReplicationImpl())
                .build();
        server.start().awaitTermination();
    }
}
