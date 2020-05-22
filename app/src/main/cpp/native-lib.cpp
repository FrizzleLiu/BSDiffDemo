#include <jni.h>
#include <string>
//导入bspatch.c中要使用的方法
extern "C"{
    int bspatch_main(int argc, char **argv);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_jni_bsdiffdemo_MainActivity_bsPatch(JNIEnv *env, jobject instance, jstring oldApk_,
                                             jstring patch_, jstring output_) {
    //相当于类型转换,转换成C/C++识别字符串
    const char *oldApk = env->GetStringUTFChars(oldApk_, 0);
    const char *patch = env->GetStringUTFChars(patch_, 0);
    const char *output = env->GetStringUTFChars(output_, 0);
    const char *arg[]={"bisdff",oldApk,output,patch};
    //第一个参数传4(否则直接抛错),第二个参数是数组如上
    bspatch_main(4, const_cast<char **>(arg));

    //释放指针
    env->ReleaseStringUTFChars(oldApk_, oldApk);
    env->ReleaseStringUTFChars(patch_, patch);
    env->ReleaseStringUTFChars(output_, output);
}