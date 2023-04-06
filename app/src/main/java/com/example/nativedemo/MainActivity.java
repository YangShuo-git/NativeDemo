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
    private String name = "baiyang";  // 被C++调用的参数：名字和年龄（需要签名）
    public static int age = 28;

    // 2. java层定义接口  native关键字，说明是要调用jni的接口
    public native String stringFromJNI();
    public native String stringGetJNI();
    public native String stringSetJNI(String name);  // 自动生成对应的Native代码函数
    static public native String staticStringFromJNI();
    public native void changeName();
    public static native void changeAge();
    public native void callAddMethod();
    public native void callShowStringMethod();

    // 被C++调用的函数（需要签名，解决函数重载的问题） (II)I
    public int add(int num1, int num2){
        return num1 + num2;
    }

    // (Ljava/lang/String)Ljava/lang/String
    public String showString(String str){
        return "【" + str + "】";
    }

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

        TextView tv4 = binding.sampleText4;
        tv3.setText(staticStringFromJNI());

        System.out.println("修改之前：name: " + name + ", age: " + age);
        changeName();
        changeAge();
        System.out.println("修改之后：name: " + name + ", age: " + age);

        callAddMethod();
        callShowStringMethod();
    }
}