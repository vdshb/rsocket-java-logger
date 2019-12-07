package pro.vdshb.rsocket.logger

import org.slf4j.LoggerFactory

sealed class RSocketLog

val log = LoggerFactory.getLogger(RSocketLog::class.java)