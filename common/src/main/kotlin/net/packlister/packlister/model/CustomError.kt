package net.packlister.packlister.model

open class CustomError(message: String, cause: Throwable? = null) : Throwable(message, cause)

class ConflictError(message: String, cause: Throwable?) : CustomError(message, cause)
class ForbiddenError(message: String, cause: Throwable? = null) : CustomError(message, cause)
