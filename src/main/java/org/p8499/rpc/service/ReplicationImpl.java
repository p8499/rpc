package org.p8499.rpc.service;

import io.grpc.stub.StreamObserver;
import org.p8499.rpc.Raft;
import org.p8499.rpc.ReplicationGrpc;

public class ReplicationImpl extends ReplicationGrpc.ReplicationImplBase {
    @Override
    public void appendEntries(Raft.AppendEntriesRequest request, StreamObserver<Raft.AppendEntriesResponse> responseObserver) {
        System.out.printf("term id = %s, leader id = %s.%n", request.getTermId(), request.getLeaderId());
        responseObserver.onNext(Raft.AppendEntriesResponse.newBuilder().build());
        responseObserver.onCompleted();
    }
}
