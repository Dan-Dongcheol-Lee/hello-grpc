package hellogrpc

import hello.HelloGrpc
import hello.HelloProto
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver


fun main(args: Array<String>) {
    val port = 9010

    val server = ServerBuilder.forPort(port)
            .addService(HelloServerImpl())
            .build()
            .start()

    println("Hello Server:$port started")

    server.awaitTermination()
}


class HelloServerImpl : HelloGrpc.HelloImplBase() {

    override fun hello1(
            req: HelloProto.HelloRequest,
            resObserver: StreamObserver<HelloProto.HelloResponse>
    ) {
        println("server-hello1: Received request: $req")

        val res = HelloProto.HelloResponse.newBuilder()
                .setText("server-hello1: Reply ${req.text}")
                .build()

        resObserver.onNext(res)
        resObserver.onCompleted()
    }

    override fun hello2(
            req: HelloProto.HelloRequest,
            resObserver: StreamObserver<HelloProto.HelloResponse>
    ) {

        println("server-hello2: Received request: $req")

        (1 .. 5).forEach { it ->
            val response = HelloProto.HelloResponse.newBuilder()
                    .setText("server-hello2: Reply ${req.text} from stream response: $it")
                    .build()

            resObserver.onNext(response)
        }

        resObserver.onCompleted()
    }

    override fun hello3(
            responseObserver: StreamObserver<HelloProto.HelloResponse>
    ): StreamObserver<HelloProto.HelloRequest> {

        return object : StreamObserver<HelloProto.HelloRequest> {

            override fun onNext(req: HelloProto.HelloRequest) {

                println("server-hello3: Received request: $req")

                (1 .. 5).forEach { it ->
                    val res = HelloProto.HelloResponse.newBuilder()
                            .setText("server-hello3: Reply ${req.text} from stream response: $it")
                            .build()

                    responseObserver.onNext(res)
                }
            }

            override fun onError(t: Throwable) {
                println("server-hello3: Encountered an error: ${t.message}")
            }

            override fun onCompleted() {
                responseObserver.onCompleted()
            }
        }
    }

}
