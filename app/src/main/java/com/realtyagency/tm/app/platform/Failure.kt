package com.realtyagency.tm.app.platform

/**
 * Base Class for handling errors/failures/exceptions.
 */
sealed class Failure {

    object NetworkConnection : Failure()
    object ServerError : Failure()
    object UnacceptableFormatError : Failure()
    object UploadingError : Failure()
    object CommonError : Failure()
    object AuthError : Failure()
}
