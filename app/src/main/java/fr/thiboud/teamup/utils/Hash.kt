package fr.thiboud.teamup.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


object Hash {

    fun sha1(input: String) = hashString("SHA-1", input)
    fun md5(input: String) = hashString("MD5", input)

    private fun hashString(type: String, input: String): String {
        val bytes = MessageDigest
            .getInstance(type)
            .digest(input.toByteArray())
        return printHexBinary(bytes).toUpperCase()
    }
}

val HEX_CHARS = "0123456789ABCDEF".toCharArray()

fun printHexBinary(data: ByteArray): String {
    val r = StringBuilder(data.size * 2)
    data.forEach { b ->
        val i = b.toInt()
        r.append(HEX_CHARS[i shr 4 and 0xF])
        r.append(HEX_CHARS[i and 0xF])
    }
    return r.toString()
}