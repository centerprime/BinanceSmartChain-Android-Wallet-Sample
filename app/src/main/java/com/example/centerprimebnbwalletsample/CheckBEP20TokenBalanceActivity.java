package com.example.centerprimebnbwalletsample;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.centerprime.binance_smart_chain_sdk.BinanceManager;
import com.example.centerprimebnbwalletsample.databinding.ActivityBep20TokenBalanceBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CheckBEP20TokenBalanceActivity extends AppCompatActivity {
    ActivityBep20TokenBalanceBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bep20_token_balance);

        /**
         * Using this getTokenBalance function you can check balance of tokenAddress.
         *
         * @params walletAddress, password, bep20TokenContractAddress, Context
         *
         * @return balance
         */

        BinanceManager binanceManager = BinanceManager.getInstance();
        binanceManager.init("https://bsc-dataseed1.binance.org:443");
        // binanceManager.init("https://data-seed-prebsc-1-s1.binance.org:8545");
        binding.checkBtn.setOnClickListener(v -> {

            String walletAddress = binding.address.getText().toString().trim();
            String password = binding.walletPassword.getText().toString().trim();
            String erc20TokenContractAddress = "0xe6df05ce8c8301223373cf5b969afcb1498c5528";
            binanceManager.getTokenBalance(walletAddress, password, erc20TokenContractAddress, this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(balance -> {

                        binding.balanceTxt.setText("Token Balance :" + balance.toString());
                        Toast.makeText(this, "Token Balance : " + balance, Toast.LENGTH_SHORT).show();

                    }, error -> {
                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
