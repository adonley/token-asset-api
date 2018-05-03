package io.block16.assetapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ToString
public class AddressRequest {
    @Size(min = 1)
    @NotNull
    private String customerId;
    @Size(min = 1)
    @NotNull
    private String transactionId;
    private BigDecimal amount; // This is serialized as a string?
    private String currency;
    private LocalDateTime timestamp;
}
