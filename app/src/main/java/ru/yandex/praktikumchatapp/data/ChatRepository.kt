package ru.yandex.praktikumchatapp.data

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retryWhen
import kotlin.math.pow

class ChatRepository(
    private val api: ChatApi = ChatApi()
) {

    fun getReplyMessage(): Flow<String> {
        return api.getReply()
            .retryWhen { cause, attempt ->
                if (cause is Exception && attempt < 3) {
                    val exponentialDelay = 1000L * 2.0.pow(attempt.toDouble())
                    delay(exponentialDelay.toLong())
                    true
                } else {
                    false
                }
            }
            .catch {
                Log.e("ChatRepository", "Error: ${it.message}")
            }
    }
}