cmake_minimum_required(VERSION 3.22.1)
project(JudgeCore)

#c++11支持
add_definitions(-std=c++11)

#jni头文件
include_directories(/usr/lib/jvm/java-17-openjdk-amd64/include/)
include_directories(/usr/lib/jvm/java-17-openjdk-amd64/include/linux)

add_library(JudgeCore SHARED
        JudgeCore.cpp
        child/child.h
        child/child.cpp
        common/common.h
        common/common.cpp
        guard/guard.cpp
        guard/guard.h
        judge/judge.h
        judge/judge.cpp
        logger/logger.cpp
        logger/logger.h
        system/system.cpp
        system/system.h
        time/time.cpp
        time/time.h
        utils/jni_lib.cpp
        utils/jni_lib.h)

target_link_libraries(JudgeCore)