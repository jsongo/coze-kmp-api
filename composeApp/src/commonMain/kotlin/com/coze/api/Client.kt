package com.coze.api

import io.ktor.http.HttpHeaders
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*

val client = HttpClient(CIO) {
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.HEADERS
        filter { request ->
            val host = request.url.host
            host.endsWith(".coze.com") || host.endsWith(".coze.cn")
        }
        sanitizeHeader { header -> header == HttpHeaders.Authorization }
    }
}
