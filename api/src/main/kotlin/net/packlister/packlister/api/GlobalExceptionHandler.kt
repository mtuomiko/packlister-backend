package net.packlister.packlister.api

import net.packlister.packlister.generated.model.APIError
import net.packlister.packlister.generated.model.ErrorWrapper
import net.packlister.packlister.model.BadRequestError
import net.packlister.packlister.model.ConflictError
import net.packlister.packlister.model.CustomError
import net.packlister.packlister.model.ForbiddenError
import net.packlister.packlister.model.InnerError
import net.packlister.packlister.model.NotFoundError
import net.packlister.packlister.model.UnauthorizedError
import net.packlister.packlister.model.ValidationError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler
    fun handle(error: CustomError): ResponseEntity<ErrorWrapper> {
        val statusCode = getHttpStatus(error)
        val apiErrors = error.innerErrors?.map { it.toApiError() }

        return ResponseEntity(
            ErrorWrapper()
                .message(error.message)
                .status(statusCode.value())
                .errors(apiErrors),
            statusCode
        )
    }

    private fun getHttpStatus(error: CustomError): HttpStatus {
        return when (error) {
            is ValidationError, is BadRequestError -> HttpStatus.BAD_REQUEST
            is UnauthorizedError -> HttpStatus.UNAUTHORIZED
            is ForbiddenError -> HttpStatus.FORBIDDEN
            is NotFoundError -> HttpStatus.NOT_FOUND
            is ConflictError -> HttpStatus.CONFLICT
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }
    }
}

private fun InnerError.toApiError(): APIError = APIError().message(this.message).target(this.target)
