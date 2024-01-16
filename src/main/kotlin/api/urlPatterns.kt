package webindexer.api

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.routing.bind
import org.http4k.routing.routes

val urlPatterns: HttpHandler = routes(
    "/search" bind Method.POST to { Views.search(it) },
    "/add-page" bind Method.POST to { Views.startCrawling(it) },
)