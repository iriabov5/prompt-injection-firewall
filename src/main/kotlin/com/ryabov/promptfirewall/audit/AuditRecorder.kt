package com.ryabov.promptfirewall.audit

import com.ryabov.promptfirewall.model.PromptAnalyzeRequest
import com.ryabov.promptfirewall.model.PromptAnalyzeResponse

/**
 * Порт записи audit-событий после анализа prompt. Реализация обязана быть
 * best-effort: ошибки persistence не должны ломать HTTP response анализа.
 */
fun interface AuditRecorder {
    fun record(request: PromptAnalyzeRequest, response: PromptAnalyzeResponse)
}
