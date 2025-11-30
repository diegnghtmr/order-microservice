package com.pragma.powerup.ordermicroservice.infrastructure.exception;

import com.pragma.powerup.ordermicroservice.domain.exception.ClientHasActiveOrderException;
import com.pragma.powerup.ordermicroservice.domain.exception.DishBelongsToAnotherRestaurantException;
import com.pragma.powerup.ordermicroservice.domain.exception.DishNotActiveException;
import com.pragma.powerup.ordermicroservice.domain.exception.DishNotFoundException;
import com.pragma.powerup.ordermicroservice.domain.exception.InvalidClientException;
import com.pragma.powerup.ordermicroservice.domain.exception.InvalidDishesException;
import com.pragma.powerup.ordermicroservice.domain.exception.OrderAlreadyAssignedException;
import com.pragma.powerup.ordermicroservice.domain.exception.OrderBelongsToAnotherRestaurantException;
import com.pragma.powerup.ordermicroservice.domain.exception.OrderNotBelongToClientException;
import com.pragma.powerup.ordermicroservice.domain.exception.OrderNotCancellableException;
import com.pragma.powerup.ordermicroservice.domain.exception.OrderNotFoundException;
import com.pragma.powerup.ordermicroservice.domain.exception.OrderNotPendingException;
import com.pragma.powerup.ordermicroservice.domain.exception.OrderNotPreparedException;
import com.pragma.powerup.ordermicroservice.domain.exception.OrderNotReadyException;
import com.pragma.powerup.ordermicroservice.domain.exception.RestaurantNotFoundException;
import com.pragma.powerup.ordermicroservice.domain.exception.SecurityPinMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleOrderNotFound(OrderNotFoundException ex) {
        return buildError(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            RestaurantNotFoundException.class,
            DishNotFoundException.class
    })
    public ResponseEntity<ApiErrorResponse> handleNotFound(RuntimeException ex) {
        return buildError(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            ClientHasActiveOrderException.class,
            OrderAlreadyAssignedException.class,
            OrderNotPendingException.class,
            OrderNotPreparedException.class,
            OrderNotReadyException.class,
            OrderNotBelongToClientException.class,
            OrderBelongsToAnotherRestaurantException.class,
            DishBelongsToAnotherRestaurantException.class,
            DishNotActiveException.class,
            OrderNotCancellableException.class,
            SecurityPinMismatchException.class
    })
    public ResponseEntity<ApiErrorResponse> handleConflict(RuntimeException ex) {
        return buildError(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({InvalidClientException.class, InvalidDishesException.class})
    public ResponseEntity<ApiErrorResponse> handleBadRequest(RuntimeException ex) {
        return buildError(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .orElse("Invalid request");
        return buildError(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception ex) {
        return buildError("Unexpected error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiErrorResponse> buildError(String message, HttpStatus status) {
        ApiErrorResponse response = new ApiErrorResponse(message, status.value(), Instant.now());
        return ResponseEntity.status(status).body(response);
    }
}
