package com.pragma.powerup.ordermicroservice.infrastructure.input.rest;

import com.pragma.powerup.ordermicroservice.application.dto.request.AssignChefRequest;
import com.pragma.powerup.ordermicroservice.application.dto.request.CreateOrderRequest;
import com.pragma.powerup.ordermicroservice.application.dto.request.UpdateOrderStatusRequest;
import com.pragma.powerup.ordermicroservice.application.dto.response.OrderResponse;
import com.pragma.powerup.ordermicroservice.application.handler.IOrderHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "Order operations")
@Validated
@RequiredArgsConstructor
public class OrderRestController {

    private final IOrderHandler orderHandler;

    @Operation(summary = "Create order", responses = {
            @ApiResponse(responseCode = "201", description = "Order created"),
            @ApiResponse(responseCode = "400", description = "Invalid payload")
    })
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderHandler.createOrder(request));
    }

    @Operation(summary = "Assign chef", responses = {
            @ApiResponse(responseCode = "200", description = "Chef assigned"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PatchMapping("/{orderId}/chef")
    public ResponseEntity<OrderResponse> assignChef(@PathVariable String orderId,
                                                    @Valid @RequestBody AssignChefRequest request) {
        return ResponseEntity.ok(orderHandler.assignChef(orderId, request));
    }

    @Operation(summary = "Update status", responses = {
            @ApiResponse(responseCode = "200", description = "Status updated"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable String orderId,
                                                      @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(orderHandler.updateStatus(orderId, request));
    }

    @Operation(summary = "Get order by id", responses = {
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getById(@PathVariable String orderId) {
        return ResponseEntity.ok(orderHandler.getById(orderId));
    }

    @Operation(summary = "List orders by restaurant and status", responses = {
            @ApiResponse(responseCode = "200", description = "Orders found")
    })
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getByRestaurantAndStatus(@RequestParam Long restaurantId,
                                                                        @RequestParam String status) {
        return ResponseEntity.ok(orderHandler.getByRestaurantAndStatus(restaurantId, status));
    }
}
