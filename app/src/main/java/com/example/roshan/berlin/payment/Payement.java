package com.example.roshan.berlin.payment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.roshan.berlin.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;

public class Payement extends AppCompatActivity {

    TextView m_response;
    PayPalConfiguration m_configuration;
    String screte_key="AVsdGz11uDWbYg4DXwypcJ20w5bPFJQpgDRj_byw10jIB91SLbUt18yIRuwcBFv4rUFrUEJwPrvpordB";
    Float button;
    Button btn_pay;
    Intent m_service;
    int m_paypalRequestCode=999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        btn_pay=(Button)findViewById(R.id.btn_pay);
        m_response=(TextView)findViewById(R.id.txt_response);
        m_configuration=new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(screte_key);
        m_service=new Intent(this, PaymentActivity.class);
        m_service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,m_configuration);
        startActivity(m_service);
       button=getIntent().getExtras().getFloat("total");

        btn_pay.setText(String.format("Pay € %.2f", button));

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BigDecimal value=BigDecimal.valueOf(button);
                PayPalPayment payment=new PayPalPayment(new BigDecimal(String.valueOf(value)),"EUR","Test Payment", PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent=new Intent(Payement.this, PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,m_configuration);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);
                startActivityForResult(intent,m_paypalRequestCode);
            }
        });
    }

    public void pay(View view){

//        BigDecimal value=BigDecimal.valueOf(button);
//        PayPalPayment payment=new PayPalPayment(new BigDecimal(String.valueOf(value)),"EUR","Test Payment", PayPalPayment.PAYMENT_INTENT_SALE);
//        Intent intent=new Intent(this, PaymentActivity.class);
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,m_configuration);
//        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);
//        startActivityForResult(intent,m_paypalRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==m_paypalRequestCode){
            if (resultCode== Activity.RESULT_OK){
                PaymentConfirmation confirmation=data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation !=null){
                    String state=confirmation.getProofOfPayment().getState();

                    if (state.equals("approved")){
                        m_response.setText("Payment Approved");
                        SharedPreferences  prefs = getSharedPreferences("payment", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("payment", String.valueOf(button));
                        editor.apply();
                    }else {
                        m_response.setText("error in the payment");
                    }
                }else {
                    m_response.setText("Confirmation is null");
                }
            }
        }
    }
}
