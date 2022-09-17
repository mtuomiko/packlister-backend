package net.packlister.packlister.api

import net.packlister.packlister.generated.model.APIError
import net.packlister.packlister.generated.model.ErrorWrapper
import net.packlister.packlister.model.ConflictError
import net.packlister.packlister.model.CustomError
import net.packlister.packlister.model.ForbiddenError
import net.packlister.packlister.model.NotFoundError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler
    fun handle(ex: CustomError): ResponseEntity<ErrorWrapper> {
        return when (ex) {
            is ConflictError -> ResponseEntity(
                ErrorWrapper().errors(listOf(APIError().message(ex.message))),
                HttpStatus.CONFLICT
            )
            is ForbiddenError -> ResponseEntity(
                ErrorWrapper().errors(listOf(APIError().message(ex.message))),
                HttpStatus.FORBIDDEN
            )
            is NotFoundError -> ResponseEntity(
                ErrorWrapper().errors(listOf(APIError().message(ex.message))),
                HttpStatus.NOT_FOUND
            )
            else -> ResponseEntity(
                ErrorWrapper().errors(listOf(APIError().message(ex.message))),
                HttpStatus.BAD_REQUEST
            )
        }
    }
}
