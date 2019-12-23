package pro.vdshb.rsocket.logger

import io.netty.buffer.ByteBuf
import io.rsocket.DuplexConnection
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


class DuplexConnectionLogger(val duplexConnection: DuplexConnection) : DuplexConnection {
    override fun receive(): Flux<ByteBuf> {
        return duplexConnection.receive()
                .doOnEach { logFrame("RSocket Connection. Connection receive.", it.get()) }
    }

    override fun send(frames: Publisher<ByteBuf>?): Mono<Void> {
        return duplexConnection.send(LogPublisherProxy(frames, { s -> LogSendSubscriber(s) }))
    }

    override fun onClose(): Mono<Void> {
        log.info("RSocket Connection. Closed.")
        return duplexConnection.onClose()
    }

    override fun dispose() {
        log.info("RSocket Connection. Disposed.")
        return duplexConnection.dispose()
    }

    override fun isDisposed(): Boolean {
        val result = duplexConnection.isDisposed()
        log.info("RSocket Connection. Is Disposed Ask. Answer: $result")
        return result
    }

}

class LogSendSubscriber(subscriber: Subscriber<in ByteBuf>?) : ProxySubscriber<ByteBuf>(subscriber) {

    override fun onComplete() {
        log.info("RSocket Connection. Sending. Complete.")
        subscriber?.onComplete();
    }

    override fun onSubscribe(subscription: Subscription?) {
        log.info("RSocket Connection. Sending. Subscribed. Subscription: {}", subscription)
        subscriber?.onSubscribe(subscription)
    }

    override fun onNext(byteBuf: ByteBuf?) {
        logFrame("RSocket Connection. Sending. Next.", byteBuf)
        subscriber?.onNext(byteBuf)
    }

    override fun onError(throwable: Throwable?) {
        log.error("RSocket Connection. Sending. Error.", throwable)
        subscriber?.onError(throwable)
    }
}