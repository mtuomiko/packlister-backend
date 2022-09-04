package net.packlister.packlister.api

import net.packlister.packlister.generated.model.APIError
import net.packlister.packlister.generated.model.ErrorWrapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler
    fun handle(ex: CustomError): ResponseEntity<ErrorWrapper> {
        return ResponseEntity(
            ErrorWrapper().errors(listOf(APIError().message(ex.message))),
            HttpStatus.BAD_REQUEST
        )
    }
}
