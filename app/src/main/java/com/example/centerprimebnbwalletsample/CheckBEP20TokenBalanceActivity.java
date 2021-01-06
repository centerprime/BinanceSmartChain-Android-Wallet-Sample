package com.example.centerprimebnbwalletsample;

import android.os.Bundle;
import android.text.TextUtils;
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

        BinanceManager binanceManager = BinanceManager.getInstance();
        /**
         * @param infura - Initialize infura
         */
        binanceManager.init("https://bsc-dataseed1.binance.org:443");
        //binanceManager.init("https://data-seed-prebsc-1-s1.binance.org:8545"); // for test net
        binding.checkBtn.setOnClickListener(v -> {

            if(!TextUtils.isEmpty(binding.address.getText().toString())
                    && !TextUtils.isEmpty(binding.walletPassword.getText().toString())
                    && !TextUtils.isEmpty(binding.contractAddress.getText().toString())) {
                /**
                 * Using this getTokenBalance function you can check balance of provided walletAddress with smart contract.
                 *
                 * @param walletAddress - which user want to check it's balance
                 * @param password - password of provided password
                 * @param contractAddress - contract address of token
                 *
                 * @return balance
                 */

                String walletAddress = binding.address.getText().toString().trim();
                String password = binding.walletPassword.getText().toString().trim();
                String erc20TokenContractAddress = binding.contractAddress.getText().toString().trim();

                binanceManager.getTokenBalance(walletAddress, password, erc20TokenContractAddress, this)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(balance -> {
                            /**
                             * if function successfully completes result can be caught in this block
                             */
                            binding.balanceTxt.setText("Token Balance :" + balance.toString());

                        }, error -> {
                            /**
                             * if function fails error can be caught in this block
                             */
                            System.out.println(error.getMessage());
                            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        });

            } else {
                Toast.makeText(this, "Fill fields!", Toast.LENGTH_SHORT).show();
            }


        });
    }
}
