package pro.vdshb.rsocket.logger

import io.rsocket.Payload
import io.rsocket.RSocket
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Signal

class RSocketLogger(val rSocket: RSocket) : RSocket {

    override fun requestStream(payload: Payload?): Flux<Payload> {
        logPayload("request stream", payload)
        return rSocket.requestStream(payload)
                .doOnEach { signal: Signal<Payload> -> logPayload("stream element", signal.get()) }
    }

    override fun onClose(): Mono<Void> {
        log.info("RSocket. Closed.")
        return rSocket.onClose()
                .log()
    }

    override fun metadataPush(payload: Payload?): Mono<Void> {
        logPayload("RSocket. Metadata push:", payload)
        return rSocket.metadataPush(payload)
    }

    override fun requestResponse(payload: Payload?): Mono<Payload> {
        logPayload("RSocket. Single request:", payload)
        return rSocket.requestResponse(payload)
                .doOnNext { paypoad: Payload -> logPayload("RSocket. Single response:", payload) }
                .log()
    }

    override fun fireAndForget(payload: Payload?): Mono<Void> {
        logPayload("fireAndForget", payload)
        return rSocket.fireAndForget(payload)
                .log()
    }

    override fun requestChannel(payloads: Publisher<Payload>?): Flux<Payload> {
        return rSocket.requestChannel(LogPublisherProxy(payloads, { s -> LogRequestChannelPayloadSubscriber(s) }))
                .doOnEach { signal: Signal<Payload> -> logPayload("RSocket. Channel element:", signal.get()) }
    }

    override fun dispose() {
        log.info("RSocket. Disposed.")
        return rSocket.dispose()
    }

    override fun availability(): Double {
        return rSocket.availability()
    }

    override fun isDisposed(): Boolean {
        return rSocket.isDisposed()
    }

    override fun equals(other: Any?): Boolean {
        return rSocket.equals(other)
    }

    override fun hashCode(): Int {
        return rSocket.hashCode()
    }

    override fun toString(): String {
        return rSocket.toString()
    }
}

class LogRequestChannelPayloadSubscriber(subscriber: Subscriber<in Payload>?) : ProxySubscriber<Payload>(subscriber) {

    override fun onComplete() {
        log.info("RSocket. Request channel. Complete.")
        subscriber?.onComplete()
    }

    override fun onSubscribe(subscription: Subscription?) {
        log.info("RSocket. Request channel.  Subscribed. Subscription: {}", subscription)
        subscriber?.onSubscribe(subscription)
    }

    override fun onNext(payload: Payload?) {
        logPayload("RSocket. Request channel. Next", payload)
        subscriber?.onNext(payload)
    }

    override fun onError(throwable: Throwable?) {
        log.error("RSocket. Request channel. Error", throwable)
        subscriber?.onError(throwable)
    }
}

