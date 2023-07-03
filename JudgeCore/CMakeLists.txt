cmake_minimum_required(VERSION 3.22.1)
project(JudgeCore)

#c++11支持
add_definitions(-std=c++11)

#jni头文件
include_directories(/usr/lib/jvm/java-17-openjdk-amd64/include/)
include_directories(/usr/lib/jvm/java-17-openjdk-amd64/include/linux)

add_library(JudgeCore SHARED
        JudgeCore.cpp )

target_link_libraries(JudgeCore)