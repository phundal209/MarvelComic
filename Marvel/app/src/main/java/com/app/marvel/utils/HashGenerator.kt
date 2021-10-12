package com.app.marvel.utils

import com.app.marvel.BuildConfig
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest
import javax.inject.Inject

internal interface HashGenerator {
    fun generate(): String
}

internal class HashGeneratorImpl @Inject constructor(): HashGenerator {
    private val md: MessageDigest = MessageDigest.getInstance("MD5")
    override fun generate(): String {
        val publicKey = BuildConfig.PUBLIC_KEY
        val privateKey = BuildConfig.PRIVATE_KEY
        val timestamp = System.currentTimeMillis()
        val join: String = timestamp.toString() + privateKey + publicKey

        val bytes = join.toByteArray(Charset.defaultCharset())
        val digest: ByteArray = md.digest(bytes)
        return toHex(digest)
    }

    private fun toHex(bytes: ByteArray): String {
        val bi = BigInteger(1, bytes)
        return String.format("%0" + (bytes.size.shl(1)) + "x", bi)
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class HashGeneratorModule {
    @Binds
    abstract fun bindHashGenerator(impl: HashGeneratorImpl): HashGenerator
}
