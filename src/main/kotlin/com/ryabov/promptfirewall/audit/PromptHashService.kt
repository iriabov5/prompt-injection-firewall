package com.ryabov.promptfirewall.audit

import jakarta.inject.Singleton
import java.security.MessageDigest

/**
 * Строит deterministic hash prompt для audit log, чтобы события можно было
 * сопоставлять без сохранения исходного пользовательского текста.
 */
@Singleton
class PromptHashService {

    fun hash(prompt: String): String {
        val digest = MessageDigest
            .getInstance("SHA-256")
            .digest(prompt.toByteArray(Charsets.UTF_8))
            .joinToString("") { byte -> "%02x".format(byte) }

        return "sha256:$digest"
    }
}
