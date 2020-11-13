package com.example.centerprimebnbwalletsample;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.binance_chain_sdk.BinanceManager;
import com.example.centerprimebnbwalletsample.databinding.ActivitySendBnbBinding;

import java.math.BigDecimal;
import java.math.BigInteger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SendBNBActivity extends AppCompatActivity {

    ActivitySendBnbBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_bnb);
    /**
     * Using this sendEther function you can send ethereum from walletAddress to another walletAddress.
     *
     * @params senderWalletAddress, password, gasPrice, gasLimit, etherAmount, receiverWalletAddress, Context
     *
     * @return transactionHash
     */


        BinanceManager binanceManager = BinanceManager.getInstance();
        binanceManager.init("https://bsc-dataseed1.binance.org:443");
        //binanceManager.init("https://data-seed-prebsc-1-s1.binance.org:8545");

        binding.sendBNB.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.address.getText().toString().trim()) && !TextUtils.isEmpty(binding.ethAmount.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.gasLimit.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.receiverAddress.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.password.getText().toString().trim())) {

                String walletAddress = binding.address.getText().toString();
                String password = binding.password.getText().toString();
                //    BigInteger gasPrice = new BigInteger(String.valueOf(ethManager.getGasPrice()));
                BigInteger gasPrice = new BigInteger("30000000000");
                BigInteger gasLimit = new BigInteger(binding.gasLimit.getText().toString());
                BigDecimal etherAmount = new BigDecimal(binding.ethAmount.getText().toString().trim());
                String receiverAddress = binding.receiverAddress.getText().toString().trim();

                binanceManager.sendBNB(walletAddress, password, gasPrice, gasLimit, etherAmount, receiverAddress, this)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(tx -> {

                            Toast.makeText(this, "TX : " + tx, Toast.LENGTH_SHORT).show();

                        }, error -> {

                            binding.result.setText(error.getMessage() + ". Please check balance of provided walletaddress!");

                            error.printStackTrace();
                            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        });
            }

        });
    }
}
