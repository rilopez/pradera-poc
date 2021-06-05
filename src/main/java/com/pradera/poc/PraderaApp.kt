package com.pradera.poc

import com.pradera.poc.config.ApplicationProperties
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.core.env.Environment
import tech.jhipster.config.DefaultProfileUtil
import tech.jhipster.config.JHipsterConstants
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.*
import javax.annotation.PostConstruct
import kotlin.jvm.JvmStatic

@SpringBootApplication
@EnableConfigurationProperties(LiquibaseProperties::class, ApplicationProperties::class)
class PraderaApp(private val env: Environment) {
    /**
     * Initializes pradera.
     *
     *
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     *
     *
     * You can find more information on how profiles work with JHipster
     * on [https://www.jhipster.tech/profiles/](https://www.jhipster.tech/profiles/).
     */
    @PostConstruct
    fun initApplication() {
        val activeProfiles: Collection<String> = listOf(*env.activeProfiles)
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)
        ) {
            log.error(
                "You have misconfigured your application! It should not run " +
                    "with both the 'dev' and 'prod' profiles at the same time."
            )
        }
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)
        ) {
            log.error(
                "You have misconfigured your application! It should not " +
                    "run with both the 'dev' and 'cloud' profiles at the same time."
            )
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(PraderaApp::class.java)

        /**
         * Main method, used to run the application.
         *
         * @param args the command line arguments.
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val app = SpringApplication(PraderaApp::class.java)
            DefaultProfileUtil.addDefaultProfile(app)
            val env: Environment = app.run(*args).environment
            logApplicationStartup(env)
        }

        private fun logApplicationStartup(env: Environment) {
            val protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map { "https" }
                .orElse("http")
            val serverPort = env.getProperty("server.port")
            val contextPath = Optional
                .ofNullable(env.getProperty("server.servlet.context-path"))
                .filter { cs: String? -> StringUtils.isNotBlank(cs) }
                .orElse("/")
            var hostAddress: String? = "localhost"
            try {
                hostAddress = InetAddress.getLocalHost().hostAddress
            } catch (e: UnknownHostException) {
                log.warn("The host name could not be determined, using `localhost` as fallback")
            }
            log.info(
                """
----------------------------------------------------------
	Application '{}' is running! Access URLs:
	Local: 		{}://localhost:{}{}
	External: 	{}://{}:{}{}
	Profile(s): 	{}
----------------------------------------------------------""",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                env.activeProfiles
            )
        }
    }
}
