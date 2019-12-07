package pro.vdshb.rsocket.logger.api

import io.rsocket.RSocket
import io.rsocket.plugins.RSocketInterceptor
import pro.vdshb.rsocket.logger.RSocketLogger

class RSocketLoggerInterceptor : RSocketInterceptor {
    override fun apply(rSocket: RSocket): RSocket {
        return RSocketLogger(rSocket)
    }
}
