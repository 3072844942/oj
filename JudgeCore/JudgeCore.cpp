//
// Created by snak on 23-7-3.
//
#include "JudgeCore.h"
#include "jni_lib.hpp"

/*
 * Class:     org_oj_server_jni_JudgeJNIService
 * Method:    hello
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_org_oj_server_jni_JudgeJNIService_hello
        (JNIEnv * env, jclass type) {
    return string_to_jstring(env, "Hello");
}

/*
 * Class:     org_oj_server_jni_JudgeJNIService
 * Method:    judge
 * Signature: (Lorg/oj/server/entity/Task;)Lorg/oj/server/entity/Record;
 */
JNIEXPORT jobject JNICALL Java_org_oj_server_jni_JudgeJNIService_judge
        (JNIEnv * env, jclass type, jobject o) {
    return NULL;
}
