package hellogrpc

import hello.HelloGrpc
import hello.HelloProto
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusRuntimeException
import io.grpc.stub.StreamObserver
import java.util.concurrent.CountDownLatch

object Hello1Client {

    @JvmStatic
    fun main(args: Array<String>) {
        val port = 9010

        val channel = ManagedChannelBuilder.forAddress("127.0.0.1", port)
                .usePlaintext(true)
                .build()

        val stub = HelloGrpc.newBlockingStub(channel)

        println("hello1 call ---------------------------------")
        val request = HelloProto.HelloRequest.newBuilder()
                .setText("Hello")
                .build()

        try {
            val res = stub.hello1(request)
            println("client-hello1: response: $res")

        } catch (e: StatusRuntimeException) {
            throw IllegalStateException(
                    "Failed to call hello. status: " + e.status + ", message: " + e.message)
        }
    }
}

object Hello2ClientWithBlockStub {

    @JvmStatic
    fun main(args: Array<String>) {
        val port = 9010

        val channel = ManagedChannelBuilder.forAddress("127.0.0.1", port)
                .usePlaintext(true)
                .build()

        val stub = HelloGrpc.newBlockingStub(channel)

        println("hello2 call ---------------------------------")
        val request = HelloProto.HelloRequest.newBuilder()
                .setText("Hello")
                .build()

        try {
            stub.hello2(request).forEach {
                println("client-hello2: response: $it")
            }

        } catch (e: StatusRuntimeException) {
            throw IllegalStateException(
                    "Failed to call hello. status: " + e.status + ", message: " + e.message)
        }
    }
}

object Hello2ClientWithStreamingStub {

    @JvmStatic
    fun main(args: Array<String>) {
        val port = 9010

        val channel = ManagedChannelBuilder.forAddress("127.0.0.1", port)
                .usePlaintext(true)
                .build()

        val stub = HelloGrpc.newStub(channel)

        println("hello2 call ---------------------------------")
        val request = HelloProto.HelloRequest.newBuilder()
                .setText("Hello")
                .build()

        try {
            val latch = CountDownLatch(1)

            stub.hello2(request, object : StreamObserver<HelloProto.HelloResponse> {

                override fun onNext(res: HelloProto.HelloResponse) {
                    println("client-hello2: response: $res")
                }

                override fun onError(t: Throwable) {
                    println("client-hello2: error: ${t.message}")
                }

                override fun onCompleted() {
                    println("client-hello2: completed")
                    latch.countDown()
                }
            })

            latch.await()

        } catch (e: StatusRuntimeException) {
            throw IllegalStateException(
                    "Failed to call hello. status: " + e.status + ", message: " + e.message)
        }
    }
}


object Hello3Client {

    @JvmStatic
    fun main(args: Array<String>) {
        val port = 9010

        val channel = ManagedChannelBuilder.forAddress("127.0.0.1", port)
                .usePlaintext(true)
                .build()

        val stub = HelloGrpc.newStub(channel)

        println("hello3 call ---------------------------------")

        try {
            val latch = CountDownLatch(1)

            val reqObserver = stub.hello3(object : StreamObserver<HelloProto.HelloResponse> {
                override fun onNext(res: HelloProto.HelloResponse) {
                    println("client-hello3: response: $res")
                }

                override fun onCompleted() {
                    println("client-hello3: response: completed")
                    latch.countDown()
                }

                override fun onError(t: Throwable) {
                    println("client-hello3: response: an error: ${t.message}")
                    latch.countDown()
                }
            })

            val req = HelloProto.HelloRequest.newBuilder()
                    .setText("Hello")
                    .build()

            reqObserver.onNext(req)
            reqObserver.onCompleted()

            latch.await()

        } catch (e: StatusRuntimeException) {
            throw IllegalStateException(
                    "Failed to call hello. status: " + e.status + ", message: " + e.message)
        }
    }
}