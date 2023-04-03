package com.example.nativedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.nativedemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // 1. 加载so库.
    static {
        System.loadLibrary("nativedemo");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());
    }

    // 2. native关键字，说明要调用jni的接口
    public native String stringFromJNI();
}