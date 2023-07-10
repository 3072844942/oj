//
// Created by snak on 23-7-9.
//

#ifndef JUDGECORE_JNI_LIB_H
#define JUDGECORE_JNI_LIB_H
#include <jni.h>
#include <iostream>

jstring string_to_jstring(JNIEnv *env, std::string str) ;

std::string jstring_to_string(JNIEnv *env, jstring j_str);

std::string get_string(JNIEnv *env, jclass type, jobject o, const char *name);

int get_integer(JNIEnv *env, jclass type, jobject o, const char *name);

long get_long(JNIEnv *env, jclass type, jobject o, const char *name);

void set_long(JNIEnv *env, jclass type, jobject o, const char *name, long value);

void set_integer(JNIEnv *env, jclass type, jobject o, const char *name, int value);

void set_string(JNIEnv *env, jclass type, jobject o, const char *name, std::string value);

#endif //JUDGECORE_JNI_LIB_H
