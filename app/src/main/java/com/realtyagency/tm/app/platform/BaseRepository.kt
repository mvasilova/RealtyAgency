package com.realtyagency.tm.app.platform

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseRepository(val errorHandler: ErrorHandler) {

    protected suspend inline fun <T> execute(
        crossinline onSuccess: (T) -> Unit,
        crossinline onState: (State) -> Unit,
        noinline func: suspend () -> T
    ) {
        try {
            //Показываем прогресс - главный поток
            withContext(Dispatchers.Main) {
                onState.invoke(State.Loading)
            }
            //Загрузка, вызов бд, маппинг в IO
            val result = withContext(Dispatchers.IO) { func.invoke() }

            //Результат и скрытие прогресса - главный поток
            withContext(Dispatchers.Main) {
                onSuccess.invoke(result)
                onState.invoke(State.Loaded)
            }
        } catch (e: Exception) {
            //Обработка ошибки - главный поток
            withContext(Dispatchers.Main) {
                onState.invoke(State.Error(errorHandler.proceedException(e)))
            }
        }
    }
}