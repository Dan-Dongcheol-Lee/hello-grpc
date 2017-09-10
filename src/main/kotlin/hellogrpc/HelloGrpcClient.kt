package hellogrpc

import io.grpc.ManagedChannelBuilder
import io.grpc.StatusRuntimeException


fun main(args: Array<String>) {
    val port = 9010

    val channel = ManagedChannelBuilder.forAddress("127.0.0.1", port)
            .usePlaintext(true)
            .build()

    val stub = HelloGrpcGrpc.newBlockingStub(channel)

    (1 .. 10).forEach { i ->
        val request = HelloGrpcProto.HelloRequest.newBuilder()
                .setText("Dan $i")
                .build()

        try {
            val response = stub.hello(request)

            response.forEach {
                println("response: $it")
            }

        } catch (e: StatusRuntimeException) {
            throw IllegalStateException(
                    "Failed to call hello. status: " + e.status + ", message: " + e.message)
        }
    }

}