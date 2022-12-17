package com.pvsb.build_src

object Ktor {

    private const val ktorVersion = "1.6.3"

    const val core = "io.ktor:ktor-client-core:$ktorVersion"
    const val client = "io.ktor:ktor-client-cio:$ktorVersion"
    const val serialization = "io.ktor:ktor-client-serialization:$ktorVersion"
    const val webSockets = "io.ktor:ktor-client-websockets:$ktorVersion"
    const val logging = "io.ktor:ktor-client-logging:$ktorVersion"
    const val logback = "ch.qos.logback:logback-classic:1.2.6"
}