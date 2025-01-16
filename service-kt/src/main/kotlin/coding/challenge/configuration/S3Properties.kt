package coding.challenge.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Represents configuration properties for S3.
 *
 * @param url       S3 url
 * @param accessKey S3 access key
 * @param secretKey S3 secret key
 * @param bucket    S3 bucket
 */
@ConfigurationProperties(prefix = "s3")
data class S3Properties(val url: String, val accessKey: String, val secretKey: String, val bucket: String)
