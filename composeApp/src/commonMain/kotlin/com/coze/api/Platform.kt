package com.coze.api
import io.ktor.client.*

expect fun httpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
