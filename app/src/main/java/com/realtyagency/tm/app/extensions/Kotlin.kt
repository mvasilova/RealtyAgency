package com.realtyagency.tm.app.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import java.net.UnknownHostException

//Warning неправильно показывается, не исправлять
suspend fun <T> CoroutineScope.asyncOrNull(block: suspend () -> T?): Deferred<T?> =
    async {
        try {
            block.invoke()
        } catch (e: Exception) {
            if (e is UnknownHostException) {
                throw e
            } else {
                null
            }
        }
    }

//возвращать exception и не закрывать coroutine
suspend fun <T> CoroutineScope.asyncOrNullException(block: suspend () -> T?, exception: (Exception) -> Unit): Deferred<T?> =
    async {
        try {
            block.invoke()
        } catch (e: Exception) {
            exception.invoke(e)
            null
        }
    }

inline fun <reified T> Gson.fromJson(json: String) =
    this.fromJson<T>(json, object : TypeToken<T>() {}.type)