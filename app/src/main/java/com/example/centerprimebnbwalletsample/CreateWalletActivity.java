package com.example.centerprimebnbwalletsample;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.binance_smart_chain_sdk.BinanceManager;
import com.example.centerprimebnbwalletsample.databinding.ActivityCreateWalletBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CreateWalletActivity extends AppCompatActivity {
    ActivityCreateWalletBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_wallet);

        /**
         * Using this createWallet function user can create a wallet.
         *
         * @params password, Context
         *
         * @return walletAddress
         */

        BinanceManager binanceManager = BinanceManager.getInstance();
        binanceManager.init("https://bsc-dataseed1.binance.org:443");
      //  binanceManager.init("https://data-seed-prebsc-1-s1.binance.org:8545");

        binding.createWallet.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(binding.password.getText().toString())
                    && !TextUtils.isEmpty(binding.confirmPassword.getText().toString())
                    && binding.password.getText().toString().equals(binding.confirmPassword.getText().toString())
                    && binding.password.getText().toString().trim().length() >= 6
                    && binding.confirmPassword.getText().toString().trim().length() >= 6) {

                binanceManager.createWallet(binding.password.getText().toString(), this)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(wallet -> {

                            binding.address.setText("0x" + wallet.getAddress());

                            binding.copy.setVisibility(View.VISIBLE);

                        }, error -> {
                            System.out.println(error);
                        });
            } else {
                Toast.makeText(this, "Please insert password correctly.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.copy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", binding.address.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show();
        });

    }
}
