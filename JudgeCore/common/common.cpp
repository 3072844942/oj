#include <jni.h>
#include <string>
#include "common.h"
#include "../utils/jni_lib.h"

#define VALIDATE_CONFIG_ERROR 0
#define VALIDATE_SUCCESS 1

/**
 * @author yzl
 * @param execConfig 运行配置
 * @return void
 * 初始化用户配置
 */

void initExecConfigAndJudgeResult(struct execConfig *execConfig, struct judgeResult *judgeResult) {
    execConfig->memoryLimit = MEMORY_LIMIT_DEFAULT;
    execConfig->cpuTimeLimit = TIME_LIMIT_DEFAULT;
    execConfig->realTimeLimit = WALL_TIME_DEFAULT;
    execConfig->processLimit = PROCESS_LIMIT_DEFAULT;
    execConfig->outputLimit = OUTPUT_LIMIT_DEFAULT;
    execConfig->wallMemoryLimit = WALL_MEMORY_DEFAULT;
    execConfig->uid = UID_DEFAULT;
    execConfig->guard = GUARD_DEFAULT;
    execConfig->execPath = "\0";
    execConfig->stderrPath = "\0";
    execConfig->stdoutPath = "\0";
    execConfig->stdinPath = "\0";
    execConfig->loggerPath = "\0";
    execConfig->execPath = "\0";
    execConfig->loggerFile = NULL;
    judgeResult->condition = 1;
    judgeResult->memoryCost = 0;
    judgeResult->realTimeCost = 0;
    judgeResult->cpuTimeCost = 0;
}

/**
 * @author yzl
 * @param execConfig 用户提供的运行的配置
 * @return void
 * 验证用户配置的合法性
 */

int validateForExecConfig(struct execConfig *execConfig) {
    if (execConfig->cpuTimeLimit < 0
        || execConfig->memoryLimit < 1024
        || execConfig->realTimeLimit < 0
        || execConfig->processLimit < 0
        || execConfig->outputLimit < 0
        || execConfig->execPath[0] == '\0') {
        return VALIDATE_CONFIG_ERROR;
    }
    return VALIDATE_SUCCESS;
}

/*
 * 获取用户配置，并设置用户配置
 * @param env
 * @param type
 * @param o
 * @param execConfig 运行配置
 * @return int 是否设置成功，如果成功，程序将继续执行
 */
int getAndSetOptions(JNIEnv *env, jclass type, jobject o, struct execConfig *execConfig) {
    execConfig->execPath = get_path(env, type, o).c_str();
    execConfig->stdoutPath = get_string(env, type, o, "out").c_str();

    jfieldID problemFieldId = env->GetFieldID(type, "problem", "Lorg.oj.server.entity.Problem;");
    jobject problem = env->GetObjectField(o, problemFieldId);
    jclass problemClass = env->GetObjectClass(problem);

    execConfig->cpuTimeLimit = get_long(env, problemClass, problem, "timeRequire");
    execConfig->memoryLimit = get_long(env, problemClass, problem, "memoryRequire");

    execConfig->stdinPath = get_string(env, problemClass, problem, "address").c_str();
    return 1;
}

/**
 * 运行结束，输出结果
 *
 * @author yzl
 * @param execConfig 运行参数
 * @param judgeResult 运行结果
 */

jobject generateResult(JNIEnv * env, struct execConfig *execConfig, struct judgeResult *judgeResult) {
    // 获取Record类的引用
    jclass recordClass = env->FindClass("org/oj/server/entity/Result");
    if (recordClass == nullptr) {
        // 处理类未找到的情况
        return nullptr;
    }

    // 获取Record类的构造函数
    jmethodID constructor = env->GetMethodID(recordClass, "<init>", "()V");
    if (constructor == nullptr) {
        // 处理构造函数未找到的情况
        return nullptr;
    }

    // 创建Record对象
    jobject recordObject = env->NewObject(recordClass, constructor);
    if (recordObject == nullptr) {
        // 处理对象创建失败的情况
        return nullptr;
    }

    set_long(env, recordClass, recordObject, "cpuTimeCost", judgeResult->cpuTimeCost);
    set_long(env, recordClass, recordObject, "realTimeCost", judgeResult->realTimeCost);
    set_long(env, recordClass, recordObject, "memoryCost", judgeResult->memoryCost);
    set_integer(env, recordClass, recordObject, "condition", judgeResult->condition);
    set_string(env, recordClass, recordObject, "inPath", execConfig->stdinPath);
    set_string(env, recordClass, recordObject, "outPath", execConfig->stdoutPath);
    set_string(env, recordClass, recordObject, "errPath", execConfig->stderrPath);
    set_string(env, recordClass, recordObject, "loggerPath", execConfig->loggerPath);

    return recordObject;
}

std::string get_path(JNIEnv *env, jclass type, jobject o) {
    std::string code = get_string(env, type, o, "code");
    int language = get_integer(env, type, o, "language");
    if (language == JAVA) {
        code = "java " + code;
    } else if (language == PYTHON3) {
        code = "python3 " + code;
    } // CPP编辑后可以直接运行
    return code;
}