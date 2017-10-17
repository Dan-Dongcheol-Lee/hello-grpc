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
        println("Received request in hello1: $req")

        val res = HelloProto.HelloResponse.newBuilder()
                .setText("Reply Hello1 ${req.text}")
                .build()

        resObserver.onNext(res)
        resObserver.onCompleted()
    }


    override fun hello2(
            resObserver: StreamObserver<HelloProto.HelloResponse>
    ): StreamObserver<HelloProto.HelloRequest> {

        return object : StreamObserver<HelloProto.HelloRequest> {

            override fun onNext(req: HelloProto.HelloRequest) {
                println("Received request in hello2: $req")

                val res = HelloProto.HelloResponse.newBuilder()
                        .setText("Reply Hello2 ${req.text} from stream request")
                        .build()
                resObserver.onNext(res)
            }

            override fun onError(t: Throwable) {
                println("Encountered an error in hello2: ${t.message}")
            }

            override fun onCompleted() {
                resObserver.onCompleted()
            }
        }
    }

    override fun hello3(
            req: HelloProto.HelloRequest,
            resObserver: StreamObserver<HelloProto.HelloResponse>
    ) {

        println("Received request in hello3: $req")

        (1 .. 5).forEach { it ->
            val response = HelloProto.HelloResponse.newBuilder()
                    .setText("Reply Hello3 ${req.text} - stream response: $it")
                    .build()

            resObserver.onNext(response)
        }

        resObserver.onCompleted()
    }

    override fun hello4(
            responseObserver: StreamObserver<HelloProto.HelloResponse>
    ): StreamObserver<HelloProto.HelloRequest> {

        return object : StreamObserver<HelloProto.HelloRequest> {

            override fun onNext(req: HelloProto.HelloRequest) {

                println("Received request in hello4: $req")

                (1 .. 5).forEach { it ->
                    val res = HelloProto.HelloResponse.newBuilder()
                            .setText("Reply Hello4 ${req.text} - stream response: $it")
                            .build()

                    responseObserver.onNext(res)
                }
            }

            override fun onError(t: Throwable) {
                println("Encountered an error in hello2: ${t.message}")
            }

            override fun onCompleted() {
                responseObserver.onCompleted()
            }
        }
    }

}
