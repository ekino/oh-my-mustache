package com.ekino.oss.tooling.demo

import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.ConfigurationProperties.Companion.systemProperties
import com.natpryce.konfig.EnvironmentVariables
import com.natpryce.konfig.PropertyGroup
import com.natpryce.konfig.getValue
import com.natpryce.konfig.overriding
import com.natpryce.konfig.stringType

class Application {

    companion object {
        const val name = "oh-my-mustache"
        val config by lazy {
            systemProperties() overriding
                    EnvironmentVariables()
            ConfigurationProperties.fromResource("config.properties")
        }
    }
}

object project : PropertyGroup() {
    val version by stringType
}
