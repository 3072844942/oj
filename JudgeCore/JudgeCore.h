/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_oj_server_jni_JudgeJNIService */

#ifndef _Included_org_oj_server_jni_JudgeJNIService
#define _Included_org_oj_server_jni_JudgeJNIService
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     org_oj_server_jni_JudgeJNIService
 * Method:    hello
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_org_oj_server_jni_JudgeJNIService_hello
  (JNIEnv *, jclass);

/*
 * Class:     org_oj_server_jni_JudgeJNIService
 * Method:    judge
 * Signature: (Lorg/oj/server/entity/Task;)Lorg/oj/server/entity/Record;
 */
JNIEXPORT jobject JNICALL Java_org_oj_server_jni_JudgeJNIService_judge
  (JNIEnv *, jclass, jobject);

#ifdef __cplusplus
}
#endif
#endif