package com.itconsortiumgh.braintreetest;

import com.braintreegateway.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {













    public  String generateClientToken(BraintreeGateway gateway) {


        String clientToken = gateway.clientToken().generate();
        return clientToken;
    }


    public  PaymentDetails paymentDetails()
    {
        return new PaymentDetails("abaddo@itconsortiumgh.com","Adel","Addo",
                "fake-valid-mastercard-nonce",new BigDecimal("0.10"));

    }

    public  Response doPaymentTransaction(String paymentMethodNonce, BigDecimal amount,BraintreeGateway gateway) {
        TransactionRequest request = new TransactionRequest();
        request.amount(amount);
        request.paymentMethodNonce(paymentMethodNonce);

        setCustomerInTransaction(request);

        SetTransactionRequestOptions(request);
        return executePaymentTransaction(request,gateway);
    }


/*
    public Response paypalRequest(BigDecimal amount)
    {
        TransactionRequest request = new TransactionRequest();
                request.amount(amount).paymentMethodNonce(paymentMethodNonce)
                .orderId("Mapped to PayPal Invoice Number")
                .options()
                .submitForSettlement(true)
                .paypal()
                .customField("PayPal custom field")
                .description("Description for PayPal email receipt")
                .done()
                .storeInVaultOnSuccess(true)
                .done();

    }
*/
    private void setCustomerInTransaction(TransactionRequest request) {
        CustomerRequest customerRequest = request.customer();
        customerRequest.email(paymentDetails().getEmail());
        customerRequest.firstName(paymentDetails().getFirstname());
        customerRequest.lastName(paymentDetails().getLastname());
    }

    private TransactionOptionsRequest SetTransactionRequestOptions(TransactionRequest request) {
        TransactionOptionsRequest options = request.options();
        options.submitForSettlement(true);
        options.done();
        return options;
    }


    private  Response executePaymentTransaction(TransactionRequest request,BraintreeGateway gateway) {
        // Create transaction.
        Result<Transaction> result = gateway.transaction().sale(request);

        if (result.isSuccess()) {
            Transaction transaction = result.getTarget();


            return new Response("200",result.getTarget().getStatus().toString());

        } else {
            ValidationErrors errors = result.getErrors();

            return new Response("100",result.getMessage());
        }
    }


    public  Response braintreePaymentProcessing(String clientToken,String nonceFromTheClient,  BigDecimal amount,BraintreeGateway gateway) {


        return  doPaymentTransaction(nonceFromTheClient, amount,gateway);
    }


    private  Response refund(TransactionRequest request,BraintreeGateway gateway) {
        // Create transaction.
        Result<Transaction> result = gateway.transaction().refund("the_transaction_id");

        if (result.isSuccess()) {
            Transaction transaction = result.getTarget();


            return new Response("200",result.getTarget().getStatus().toString());

        } else {
            ValidationErrors errors = result.getErrors();

            return new Response("100",result.getMessage());
        }
    }
















}
