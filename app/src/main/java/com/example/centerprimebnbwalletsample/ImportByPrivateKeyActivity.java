package com.example.centerprimebnbwalletsample;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.binance_smart_chain_sdk.BinanceManager;
import com.example.centerprimebnbwalletsample.databinding.ActivityImportPrivateKeyBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ImportByPrivateKeyActivity extends AppCompatActivity {
    ActivityImportPrivateKeyBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_import_private_key);

        BinanceManager binanceManager = BinanceManager.getInstance();
        /**
         * @param infura - Initialize infura
         */
        binanceManager.init("https://bsc-dataseed1.binance.org:443");
       // binanceManager.init("https://data-seed-prebsc-1-s1.binance.org:8545"); // for test net

        binding.checkBtn.setOnClickListener(v -> {
            /**
             * Using this importFromPrivateKey function user can import his wallet from its private key.
             *
             * @param privateKey - private key of wallet address
             * @param Context - activity context
             *
             * @return walletAddress
             */
            String privateKey = binding.privateKey.getText().toString().trim();
            binanceManager.importFromPrivateKey(privateKey, this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(walletAddress -> {
                        /**
                         * if function successfully completes result can be caught in this block
                         */
                        binding.address.setText("0x" + walletAddress);
                        binding.copyBtn.setVisibility(View.VISIBLE);
                    }, error -> {
                        /**
                         * if function fails error can be caught in this block
                         */
                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        binding.copyBtn.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", binding.address.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show();
        });
    }
}
