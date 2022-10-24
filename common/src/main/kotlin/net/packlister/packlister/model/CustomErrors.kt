package net.packlister.packlister.model

open class CustomError(
    message: String = "Unknown error",
    cause: Throwable? = null,
    val innerErrors: List<InnerError>? = null
) : Throwable(message, cause)

class InnerError(
    val message: String,
    val target: String? = null
)

class BadRequestError(
    message: String = "Bad request",
    cause: Throwable? = null,
    innerErrors: List<InnerError>? = null
) : CustomError(message, cause, innerErrors)

class ValidationError(
    message: String = "Validation error",
    cause: Throwable? = null,
    innerErrors: List<InnerError>? = null
) : CustomError(message, cause, innerErrors)

class UnauthorizedError(
    message: String = "Unauthorized, please authenticate",
    cause: Throwable? = null,
    innerErrors: List<InnerError>? = null
) : CustomError(message, cause, innerErrors)

class ForbiddenError(
    message: String = "Forbidden, no permission",
    cause: Throwable? = null,
    innerErrors: List<InnerError>? = null
) : CustomError(message, cause, innerErrors)

class NotFoundError(
    message: String = "Not found",
    cause: Throwable? = null,
    innerErrors: List<InnerError>? = null
) : CustomError(message, cause, innerErrors)

class ConflictError(
    message: String = "Conflict error",
    cause: Throwable? = null,
    innerErrors: List<InnerError>? = null
) : CustomError(message, cause, innerErrors)
