package com.example.centerprimebnbwalletsample;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.binance_smart_chain_sdk.BinanceManager;
import com.example.centerprimebnbwalletsample.databinding.ActivitySendBep20TokenBinding;
import java.math.BigDecimal;
import java.math.BigInteger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SendBEP20TokenActivity extends AppCompatActivity {
    ActivitySendBep20TokenBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_bep20_token);

        /**
         * Using this sendToken function you can send BNB token from walletAddress to another walletAddress.
         *
         * @params senderWalletAddress, password, gasPrice, gasLimit, tokenAmount, receiverWalletAddress, erc20TokenContractAddress, Context
         *
         * @return transactionHash
         */

        BinanceManager binanceManager = BinanceManager.getInstance();
        binanceManager.init("https://bsc-dataseed1.binance.org:443");
        // binanceManager.init("https://data-seed-prebsc-1-s1.binance.org:8545");
        binding.sendERCToken.setOnClickListener(v -> {

            if (!TextUtils.isEmpty(binding.address.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.tokenAmount.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.gasLimit.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.receiverAddress.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.password.getText().toString().trim())) {

                String walletAddress = binding.address.getText().toString();
                String password = binding.password.getText().toString();
                BigInteger gasPrice = new BigInteger("30000000000");
                BigInteger gasLimit = new BigInteger(binding.gasLimit.getText().toString());
                BigDecimal tokenAmount = new BigDecimal(binding.tokenAmount.getText().toString().trim());
                String receiverAddress = binding.receiverAddress.getText().toString().trim();
                String erc20TokenContractAddress = "TOKEN CONTRACT ADDRESS";


                binanceManager.sendToken(walletAddress, password, gasPrice, gasLimit, tokenAmount, receiverAddress, erc20TokenContractAddress, this)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(tx -> {

                            Toast.makeText(this, "TX : " + tx, Toast.LENGTH_SHORT).show();

                        }, error -> {
                            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            System.out.println("Error: " + error.getMessage());
                        });
            }
        });

    }
}
