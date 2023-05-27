package com.micrantha.skouter.data.storage

import com.micrantha.skouter.data.storage.source.StorageLocalSource
import com.micrantha.skouter.data.storage.source.StorageRemoteSource
import com.micrantha.skouter.domain.repository.StorageRepository

private const val imagesBucketID = "images"

class StorageDataRepository(
    private val remoteSource: StorageRemoteSource,
    private val localSource: StorageLocalSource
) : StorageRepository {

    override suspend fun download(path: String) =
        remoteSource.download(imagesBucketID, path).onSuccess {
            localSource.put(path, it)
        }

    override suspend fun url(path: String): Result<String> =
        remoteSource.url(imagesBucketID, path)

    override suspend fun upload(path: String, data: ByteArray): Result<String> {
        return remoteSource.upload(imagesBucketID, path, data).map { (_, url) -> url }
    }

    override fun get(path: String) = localSource.get(path)
}
