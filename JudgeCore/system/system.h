#ifndef Y_JUDGE_SYSTEM_H
#define Y_JUDGE_SYSTEM_H


#include <string>

int killPid(pid_t pid, int killType);

int isRoot();

bool exists(std::string name);


#endif //Y_JUDGE_SYSTEM_H
