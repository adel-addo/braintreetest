package com.itconsortiumgh.braintreetest;

import lombok.*;

import java.math.BigDecimal;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetails {

    private String email;
    private String firstname;
    private String lastname;
    private String paymentMethod;
    private BigDecimal amount;
}


