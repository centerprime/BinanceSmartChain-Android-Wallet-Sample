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

        BinanceManager binanceManager = BinanceManager.getInstance();
        /**
         * @param infura - Initialize infura
         */
        binanceManager.init("https://bsc-dataseed1.binance.org:443");
      //  binanceManager.init("https://data-seed-prebsc-1-s1.binance.org:8545"); // for test net

        binding.createWallet.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(binding.password.getText().toString())
                    && !TextUtils.isEmpty(binding.confirmPassword.getText().toString())
                    && binding.password.getText().toString().equals(binding.confirmPassword.getText().toString())
                    && binding.password.getText().toString().trim().length() >= 6
                    && binding.confirmPassword.getText().toString().trim().length() >= 6) {

                /**
                 * Using this createWallet function user can create a wallet.
                 *
                 * @param password - must be provided password to wallet address
                 * @param Context - activity context
                 *
                 * @return walletAddress
                 */

                binanceManager.createWallet(binding.password.getText().toString().trim(), this)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(wallet -> {
                            /**
                             * if function successfully completes result can be caught in this block
                             */
                            binding.address.setText("0x" + wallet.getAddress());
                            binding.copy.setVisibility(View.VISIBLE);

                        }, error -> {
                            /**
                             * if function fails error can be catched in this block
                             */
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
