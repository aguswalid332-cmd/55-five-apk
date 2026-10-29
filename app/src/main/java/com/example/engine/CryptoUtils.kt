package com.example.engine

import java.security.MessageDigest

object CryptoUtils {
    fun md5(input: String): String {
        return try {
            val md = MessageDigest.getInstance("MD5")
            val bytes = md.digest(input.toByteArray(Charsets.UTF_8))
            bytes.joinToString("") { "%02X".format(it) }
        } catch (e: Exception) {
            "5232F77555C8A357217CC8521D7D1B5A"
        }
    }
}
