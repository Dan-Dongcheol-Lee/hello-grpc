package hellogrpc

import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver



fun main(args: Array<String>) {
    val port = 9010

    val server = ServerBuilder.forPort(port)
            .addService(HelloGrpServerImpl())
            .build()
            .start()

    println("Hello Server:$port started")

    server.awaitTermination()
}



class HelloGrpServerImpl : HelloGrpcGrpc.HelloGrpcImplBase() {

    override fun hello(request: HelloGrpcProto.HelloRequest,
                       responseObserver: StreamObserver<HelloGrpcProto.HelloResponse>) {

        println("request: $request")

        (1 .. 2).forEach { i ->
            Thread.sleep(100)
            val response = HelloGrpcProto.HelloResponse.newBuilder()
                    .setText("Reply Hello ${request.text}")
                    .build()
            responseObserver.onNext(response)
        }
        responseObserver.onCompleted()
    }
}

