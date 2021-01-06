package com.example.centerprimebnbwalletsample;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.binance_smart_chain_sdk.BinanceManager;
import com.example.centerprimebnbwalletsample.databinding.ActivityCheckBalanceBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CheckBalanceActivity extends AppCompatActivity {
    ActivityCheckBalanceBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_check_balance);

        BinanceManager binanceManager = BinanceManager.getInstance();
        /**
         * @param infura - Initialize infura
         */
       // binanceManager.init("https://data-seed-prebsc-1-s1.binance.org:8545"); // for test net
        binanceManager.init("https://bsc-dataseed1.binance.org:443");
        binding.checkBtn.setOnClickListener(v -> {
            /**
             * Using this getBNBBalance function you can check balance of provided walletAddress.
             *
             * @param walletAddress - which user want to check it's balance
             *
             * @return if the function completes successfully returns balance of provided wallet address or returns error name
             */
            String address = binding.address.getText().toString();
            if (!address.startsWith("0x")) {
                address = "0x" + address;
            }

            binanceManager.getBNBBalance(address, this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(balance -> {
                        /**
                         * if function successfully completes result can be caught in this block
                         */
                        binding.balanceTxt.setText("BNB balance: " + balance.toString());

                    }, error -> {
                        /**
                         * if function fails error can be caught in this block
                         */
                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    });
        });
    }
}
