#include <jni.h>
#include <string>
#include "test.h"

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_nativedemo_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";

    Test test;
    test.getTest();


    return env->NewStringUTF(test.getTest().c_str());
}