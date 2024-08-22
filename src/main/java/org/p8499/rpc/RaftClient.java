package org.p8499.rpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class RaftClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8001).usePlaintext().build();//获得服务器通道
        ReplicationGrpc.ReplicationBlockingStub stub = ReplicationGrpc.newBlockingStub(channel);//获得函数句柄
        Raft.AppendEntriesRequest request = Raft.AppendEntriesRequest.newBuilder().setTermId(1).setLeaderId(1).build();//构建入参
        Raft.AppendEntriesResponse response = stub.appendEntries(request);//调用并获得出参
        channel.shutdown();//关闭服务器通道
    }
}
