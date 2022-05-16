package com.safwachy.safechat.helper

import com.safwachy.safechat.exception.ValidationException
import com.safwachy.safechat.model.RoomModel
import org.apache.commons.lang3.RandomStringUtils
import java.math.BigInteger
import java.security.MessageDigest
import java.security.Provider
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class SecurityUtil {
    companion object {
        fun sha256Hash(input: String) : String {
            val md = MessageDigest.getInstance("SHA-256")
            return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
        }

        fun generateSecretKey(key: String): SecretKeySpec {
            val salt = System.getenv("BASE64_SALT")
            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            val spec = PBEKeySpec(key.toCharArray(), Base64.getDecoder().decode(salt), 10000, 256)
            return SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
        }

        fun encrypt(input: String, secretKey: SecretKeySpec) : String {
            try {
                val iv = Base64.getEncoder().encodeToString(RandomStringUtils.randomAlphanumeric(16).toByteArray())
                val ivParameterSpec = IvParameterSpec(Base64.getDecoder().decode(iv))

                val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)
                return iv + ":" + Base64.getEncoder().encodeToString(cipher.doFinal(input.toByteArray(Charsets.UTF_8)))
            } catch (e: Exception) {
                throw ValidationException("Could not decrypt message", e)
            }
        }

        fun decrypt(input: String, secretKey: SecretKeySpec) : String {
            try {
                val parts = input.split(":")
                val iv = parts[0]
                val decodedMsg = parts[1]
                val ivParameterSpec = IvParameterSpec(Base64.getDecoder().decode(iv))

                val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec)
                return String(cipher.doFinal(Base64.getDecoder().decode(decodedMsg)))
            } catch (e: Exception) {
                throw ValidationException("Could not decrypt message", e)
            }
        }
    }
}