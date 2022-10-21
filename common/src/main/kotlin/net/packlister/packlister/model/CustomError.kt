package net.packlister.packlister.model

open class CustomError(message: String, cause: Throwable? = null) : Throwable(message, cause)

class ConflictError(message: String, cause: Throwable? = null) : CustomError(message, cause)
class ForbiddenError(message: String, cause: Throwable? = null) : CustomError(message, cause)
class UnauthorizedError(message: String, cause: Throwable? = null) : CustomError(message, cause)
class NotFoundError(message: String, cause: Throwable? = null) : CustomError(message, cause)
class ValidationError(message: String, cause: Throwable? = null) : CustomError(message, cause)
class BadRequestError(message: String, cause: Throwable? = null) : CustomError(message, cause)
