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

        TextView tv2 = binding.sampleText2;
        tv2.setText(stringGetJNI());

        TextView tv3 = binding.sampleText3;
        tv3.setText(stringSetJNI("hello native"));
    }

    // 2. java层定义接口  native关键字，说明是要调用jni的接口
    public native String stringFromJNI();
    public native String stringGetJNI();
    public native String stringSetJNI(String name);  // 用javah工具自动生成对应的Native代码函数
}