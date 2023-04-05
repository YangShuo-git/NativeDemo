#include <jni.h>
#include <string>
#include "test.h"


// 3. C++层实现接口
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_nativedemo_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";

    Test test;
    test.getTest();


    return env->NewStringUTF(test.getTest().c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_nativedemo_MainActivity_stringGetJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";

    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_nativedemo_MainActivity_stringSetJNI(JNIEnv *env, jobject, jstring name) {
    const char* str;
    str = env->GetStringUTFChars(name, NULL);
    if (!str){
        return NULL;
    }
    std::string res = std::string("set JNI ") + std::string(str);
    env->ReleaseStringChars(name, reinterpret_cast<const jchar*>(str));
    return env->NewStringUTF(res.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_nativedemo_MainActivity_staticStringFromJNI(JNIEnv *env, jclass clazz) {
    std::string hello = "Hello static function";

    return env->NewStringUTF(hello.c_str());
}