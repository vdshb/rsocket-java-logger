package pro.vdshb.rsocket.logger

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import io.rsocket.frame.FrameHeaderFlyweight
import io.rsocket.frame.FrameUtil
import java.nio.charset.StandardCharsets

fun logFrame(msg: String, frame: ByteBuf?) {
    if (frame == null) {
        log.info("============= null frame =============")
        log.info(msg)
        return
    }
    if (frame.capacity() == 0) {
        log.info("============= Empty frame =============")
        log.info(msg)
        return
    }
    log.info("============= ${FrameHeaderFlyweight.frameType(frame)} =============")
    logByteBuf(msg, frame)
    log.info("Buffer structure:" + FrameUtil.toString(frame))
}

fun logByteBuf(msg: String, byteBuf: ByteBuf?) {
    log.info(msg)
    if (byteBuf == null) {
        log.info("<null>")
        return
    }
    if (byteBuf.capacity() == 0) {
        log.info("<Empty>")
    }
    log.info("As string: ${byteBuf.copy().toString(StandardCharsets.UTF_8)}")
    val sb = StringBuilder("Full Buffer:\n")
    ByteBufUtil.appendPrettyHexDump(sb, byteBuf)
    log.info(sb.toString())
}