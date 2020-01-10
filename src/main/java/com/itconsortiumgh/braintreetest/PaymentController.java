package com.itconsortiumgh.braintreetest;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pay")
public class PaymentController {


  @Autowired
  PaymentService payment;

  @Value("${publicKey}")
  private String publicKey;

  @Value("${privateKey}")
  private  String privateKey;

  @Value("${merchantId}")
  private  String merchantId;

    @RequestMapping(value="/token",method = RequestMethod.GET)
    public Response  getToken() {

        BraintreeGateway gateway=connectBraintreeGateway(merchantId, publicKey,  privateKey);

        String clientToken=payment.generateClientToken(gateway);




        return new Response("100","Token :"+clientToken);
    }


  @RequestMapping(value="/payments",method = RequestMethod.POST)
  public Response  Payment(@RequestBody PaymentDetails getpayment) {

    BraintreeGateway gateway=connectBraintreeGateway(merchantId, publicKey,  privateKey);

    String clientToken=payment.generateClientToken(gateway);

    return payment.braintreePaymentProcessing(clientToken,getpayment.getPaymentMethod(),  getpayment.getAmount(),gateway);


    //return new Response("400","Failed");
  }


  @RequestMapping(value="/test",method = RequestMethod.GET)
  public Response  Payment() {



    return new Response("400",publicKey);
  }


  public BraintreeGateway connectBraintreeGateway(String merchantId, String publicKey, String privateKey) {
    BraintreeGateway braintreeGateway = new BraintreeGateway(
            Environment.SANDBOX, merchantId, publicKey, privateKey);
    return braintreeGateway;
  }



}
