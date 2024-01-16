package webindexer

import org.http4k.server.SunHttp
import org.http4k.server.asServer
import webindexer.api.urlPatterns

fun main() {
    // Start server
    urlPatterns.asServer(SunHttp(8080)).start()
}