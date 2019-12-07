package pro.vdshb.rsocket.logger

import io.rsocket.Payload

fun logPayload(action: String, payload: Payload?) {
    log.info(action)
    if (payload == null) {
        log.info("Payload is null")
        return
    }
    logByteBuf("Metadata.", payload.sliceMetadata())
    logByteBuf("Data.", payload.sliceData())
}