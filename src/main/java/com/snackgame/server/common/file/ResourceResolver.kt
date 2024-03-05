package com.snackgame.server.common.file

import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Component
import java.net.URL

@Component
class ResourceResolver(
    private val s3FileUploader: S3FileUploader
) {

    fun resolve(resource: Resource): URL {
        return when (resource) {
            is UrlResource -> resource.url
            else -> s3FileUploader.upload(resource)
        }
    }
}
