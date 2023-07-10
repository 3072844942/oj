//
// Created by snak on 23-7-3.
//
//  注意：请在linux系统下调试运行
//  若你在macos系统下运行(虽然可以跑)，会出现你不期望的情况，
//  例如：某些量的单位会不同(eg.costResource.ru_maxrss)
//       或者某些功能无法实现（eg.内存超限检测）
#include <string>
#include "JudgeCore.h"
#include "common/common.h"
#include "judge/judge.h"
#include "utils/jni_lib.h"

/*
 * Class:     org_oj_server_jni_JudgeJNIService
 * Method:    hello
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_org_oj_server_jni_JudgeJNIService_hello
        (JNIEnv * env, jclass type) {
    std::string hello = "Hello from C++";
    return string_to_jstring(env, hello);
}

/*
 * Class:     org_oj_server_jni_JudgeJNIService
 * Method:    judge
 * Signature: (Lorg/oj/server/entity/Task;)Lorg/oj/server/entity/Record;
 */
JNIEXPORT jobject JNICALL Java_org_oj_server_jni_JudgeJNIService_judge
        (JNIEnv * env, jclass type, jobject o) {
    struct execConfig execConfig;
    struct judgeResult judgeResult;
    initExecConfigAndJudgeResult(&execConfig, &judgeResult);
    if (getAndSetOptions(env, type, o, &execConfig)) {
        if (validateForExecConfig(&execConfig)) {
            runJudge(&execConfig, &judgeResult);
        } else {
            judgeResult.condition = VALIDATE_ERROR;
        }
    }
    return generateResult(env, &execConfig, &judgeResult);
}
