package pro.vdshb.rsocket.logger

import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber

class LogPublisherProxy<T>(val publisher: Publisher<T>?, val proxySubscriberFactory: (Subscriber<in T>?)-> ProxySubscriber<T>) : Publisher<T> {
    override fun subscribe(s: Subscriber<in T>?) {
        publisher?.subscribe(proxySubscriberFactory(s))
    }
}

abstract class ProxySubscriber<T>(val subscriber: Subscriber<in T>?) : Subscriber<T>