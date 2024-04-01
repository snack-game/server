package com.snackgame.server.common.file

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.IOException
import java.net.URL
import java.util.*
import java.util.stream.Collectors

@Component
class S3FileUploader(
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
    private val amazonS3Client: AmazonS3
) {

    fun upload(resource: Resource): URL {
        return uploadToS3(resource)
    }

    fun upload(resources: List<Resource>): List<URL> {
        return resources.parallelStream()
            .map(::upload)
            .collect(Collectors.toList())
    }

    @Throws(IOException::class)
    private fun uploadToS3(resource: Resource): URL {
        val key = uniquePathOf(resource)
        val metadata = ObjectMetadata().let {
            it.contentLength = resource.contentLength()
            it
        }
        amazonS3Client.putObject(bucket, key, resource.inputStream, metadata)
        return amazonS3Client.getUrl(bucket, key)
    }

    private fun uniquePathOf(resource: Resource): String {
        val randomUUID = UUID.randomUUID().toString()
        return "unhashed/${randomUUID.replace("-", "")}-${resource.filename?.hashCode()}"
    }
}
