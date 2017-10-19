package hellogrpc

import hello.HelloGrpc
import hello.HelloProto
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import java.util.*
import java.util.concurrent.TimeUnit


fun main(args: Array<String>) {
    val port = 9010

    val server = ServerBuilder.forPort(port)
            .addService(HelloServerImpl())
            .build()
            .start()

    println("Hello Server:$port started")

    server.awaitTermination()
}


fun ClosedRange<Int>.random() = Random().nextInt(endInclusive - start) + start

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

        val resList = mutableListOf<Deferred<HelloProto.HelloResponse>>()
        (1..5).forEach { it ->

            // run in async and apply random delay
            val res = async {

                delay((0..10).random().toLong(), TimeUnit.SECONDS)

                val response = HelloProto.HelloResponse.newBuilder()
                        .setText("server-hello2: Reply ${req.text} from stream response: $it")
                        .build()

                resObserver.onNext(response)
                response
            }
            resList.add(res)
        }

        runBlocking {
            resList.forEach { it.await() }
        }

        resObserver.onCompleted()
    }

    override fun hello3(
            resObserver: StreamObserver<HelloProto.HelloResponse>
    ): StreamObserver<HelloProto.HelloRequest> {

        return object : StreamObserver<HelloProto.HelloRequest> {

            override fun onNext(req: HelloProto.HelloRequest) {

                println("server-hello3: Received request: $req")

                val resList = mutableListOf<Deferred<HelloProto.HelloResponse>>()
                (1..5).forEach { it ->

                    // run in async and apply random delay
                    val res = async {

                        delay((0..10).random().toLong(), TimeUnit.SECONDS)

                        val response = HelloProto.HelloResponse.newBuilder()
                                .setText("server-hello3: Reply ${req.text} from stream response: $it")
                                .build()

                        resObserver.onNext(response)
                        response
                    }
                    resList.add(res)
                }

                runBlocking {
                    resList.forEach { it.await() }
                }
            }

            override fun onError(t: Throwable) {
                println("server-hello3: Encountered an error: ${t.message}")
            }

            override fun onCompleted() {
                resObserver.onCompleted()
            }
        }
    }

}
