package pro.vdshb.rsocket.logger.api

import io.rsocket.DuplexConnection
import io.rsocket.plugins.DuplexConnectionInterceptor
import io.rsocket.plugins.DuplexConnectionInterceptor.Type.CLIENT
import io.rsocket.plugins.DuplexConnectionInterceptor.Type.SOURCE
import pro.vdshb.rsocket.logger.DuplexConnectionLogger


class RSocketAllConnectionsLoggerInterceptor : DuplexConnectionInterceptor {

    override fun apply(type: DuplexConnectionInterceptor.Type, connection: DuplexConnection): DuplexConnection {
        return DuplexConnectionLogger(connection)
    }

}

class RSocketClientConnectionLoggerInterceptor : DuplexConnectionInterceptor {

    override fun apply(type: DuplexConnectionInterceptor.Type, connection: DuplexConnection): DuplexConnection {
        if (type == CLIENT) {
            return DuplexConnectionLogger(connection)
        }
        return connection
    }

}

class RSocketServerConnectionLoggerInterceptor : DuplexConnectionInterceptor {

    override fun apply(type: DuplexConnectionInterceptor.Type, connection: DuplexConnection): DuplexConnection {
        if (type in listOf(SOURCE)) {
            return DuplexConnectionLogger(connection)
        }
        return connection
    }

}
