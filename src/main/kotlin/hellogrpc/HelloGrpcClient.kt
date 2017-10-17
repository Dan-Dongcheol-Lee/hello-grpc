package hellogrpc

import hello.HelloGrpc
import hello.HelloProto
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusRuntimeException

object Hello1Client {

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
            println("response: $res")

        } catch (e: StatusRuntimeException) {
            throw IllegalStateException(
                    "Failed to call hello. status: " + e.status + ", message: " + e.message)
        }
    }
}

object Hello2Client {

    fun main(args: Array<String>) {
        val port = 9010

        val channel = ManagedChannelBuilder.forAddress("127.0.0.1", port)
                .usePlaintext(true)
                .build()

        val stub = HelloGrpc.newFutureStub(channel)

        println("hello2 call ---------------------------------")
        val request = HelloProto.HelloRequest.newBuilder()
                .setText("Hello")
                .build()

        try {
            stub.hello1()
            val res = stub.hello1(request)
            println("response: $res")

        } catch (e: StatusRuntimeException) {
            throw IllegalStateException(
                    "Failed to call hello. status: " + e.status + ", message: " + e.message)
        }
    }
}

object Hello3Client {

    fun main(args: Array<String>) {
        val port = 9010

        val channel = ManagedChannelBuilder.forAddress("127.0.0.1", port)
                .usePlaintext(true)
                .build()

        val stub = HelloGrpc.newBlockingStub(channel)

        println("hello3 call ---------------------------------")
        val request = HelloProto.HelloRequest.newBuilder()
                .setText("Hello")
                .build()

        try {
            val res = stub.hello1(request)
            println("response: $res")

        } catch (e: StatusRuntimeException) {
            throw IllegalStateException(
                    "Failed to call hello. status: " + e.status + ", message: " + e.message)
        }
    }
}

object Hello4Client {

    fun main(args: Array<String>) {
        val port = 9010

        val channel = ManagedChannelBuilder.forAddress("127.0.0.1", port)
                .usePlaintext(true)
                .build()

        val stub = HelloGrpc.newBlockingStub(channel)

        println("hello4 call ---------------------------------")
        val request = HelloProto.HelloRequest.newBuilder()
                .setText("Hello")
                .build()

        try {
            val res = stub.hello1(request)
            println("response: $res")

        } catch (e: StatusRuntimeException) {
            throw IllegalStateException(
                    "Failed to call hello. status: " + e.status + ", message: " + e.message)
        }
    }
}