##### GRPC程序怎么写

###### Gradle配置
```
plugins {
    id 'java'
    id 'com.google.protobuf' version '0.9.4'
}
```
```
dependencies {
    runtimeOnly 'io.grpc:grpc-netty-shaded:1.65.0'
    implementation 'io.grpc:grpc-protobuf:1.65.0'
    implementation 'io.grpc:grpc-stub:1.65.0'
    compileOnly 'org.apache.tomcat:annotations-api:6.0.53'
}
```
```
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.1"
    }
    plugins {
        grpc {
            artifact = 'io.grpc:protoc-gen-grpc-java:1.65.0'
        }
    }
    generateProtoTasks {
        all()*.plugins {
            grpc {}
        }
    }
}
```

###### 定义接口
在```src\main\proto```下写```raft.proto```
```protobuf
syntax = "proto3";
package org.p8499.rpc;

message AppendEntriesRequest{
  uint64 term_id = 1;
  uint32 leader_id = 2;
}

message AppendEntriesResponse{

}

service replication{
  rpc appendEntries(AppendEntriesRequest) returns (AppendEntriesResponse);
}
```

###### 生成Java代码
运行```.\gradlew.bat generateProto```生成文件位于```build\generated```<br/>
这些类可以自动被IDEA识别为代码

###### 编写Service实现类
```java
public class ReplicationImpl extends ReplicationGrpc.ReplicationImplBase {
    @Override
    public void appendEntries(Raft.AppendEntriesRequest request, StreamObserver<Raft.AppendEntriesResponse> responseObserver) {
        System.out.printf("term id = %s, leader id = %s.%n", request.getTermId(), request.getLeaderId());
        responseObserver.onNext(Raft.AppendEntriesResponse.newBuilder().build());
        responseObserver.onCompleted();
    }
}
```

###### 编写Server类
```java
public class RaftServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8001)
                .addService(new ReplicationImpl())
                .build();
        server.start().awaitTermination();
    }
}
```


###### 编写Client类
```java
public class RaftClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8001).usePlaintext().build();//获得服务器通道
        ReplicationGrpc.ReplicationBlockingStub stub = ReplicationGrpc.newBlockingStub(channel);//获得函数句柄
        Raft.AppendEntriesRequest request = Raft.AppendEntriesRequest.newBuilder().setTermId(1).setLeaderId(1).build();//构建入参
        Raft.AppendEntriesResponse response = stub.appendEntries(request);//调用并获得出参
        channel.shutdown();//关闭服务器通道
    }
}
```

参考 [grpc-java](https://github.com/grpc/grpc-java)
