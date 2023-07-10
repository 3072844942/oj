//
// Created by snak on 23-7-9.
//
#include <jni.h>
#include <iostream>
#include "jni_lib.h"

jstring string_to_jstring(JNIEnv *env, std::string str) {
    return env->NewStringUTF(str.c_str());
}

std::string jstring_to_string(JNIEnv *env, jstring j_str) {
    const char *c = env->GetStringUTFChars(j_str, 0);
    std::string c_str = std::string(c);
    env->ReleaseStringUTFChars(j_str, c);
    return c_str;
}

std::string get_string(JNIEnv *env, jclass type, jobject o, const char *name) {
    jfieldID fieldId = env->GetFieldID(type, name, "Ljava/lang/String;");
    jstring s = (jstring) env->GetObjectField(o, fieldId);
    return jstring_to_string(env, s);
}

int get_integer(JNIEnv *env, jclass type, jobject o, const char *name) {
    jfieldID fieldId = env->GetFieldID(type, name, "Ljava/lang/Integer;");
    jint i = env->GetIntField(o, fieldId);
    return i;
}

long get_long(JNIEnv *env, jclass type, jobject o, const char *name) {
    jfieldID fieldId = env->GetFieldID(type, name, "Ljava/lang/Long;");
    jint i = env->GetIntField(o, fieldId);
    return i;
}

void set_long(JNIEnv *env, jclass type, jobject o, const char *name, long value) {
    jfieldID jfieldId = env->GetFieldID(type, name, "Ljava/lang/Long;");
    jlong v = static_cast<jlong>(value);
    env->SetLongField(o, jfieldId, v);
}

void set_integer(JNIEnv *env, jclass type, jobject o, const char *name, int value) {
    jfieldID jfieldId = env->GetFieldID(type, name, "Ljava/lang/Integer;");
    env->SetIntField(o, jfieldId, value);
}

void set_string(JNIEnv *env, jclass type, jobject o, const char *name, std::string value) {
    jfieldID jfieldId = env->GetFieldID(type, name, "Ljava/lang/String;");
    jobject v = env->NewStringUTF(value.c_str());
    env->SetObjectField(o, jfieldId, v);
}