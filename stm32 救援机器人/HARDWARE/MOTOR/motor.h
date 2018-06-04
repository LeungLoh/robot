#ifndef __motor_H
#define __motor_H	 
#include "sys.h"

#define IN1 PBout(3)// PB3
#define IN2 PBout(4)// PB4
#define IN3 PBout(5)// PB5
#define IN4 PBout(6)// PB6

void motor_Init(void);
void go(void);
void back(void);
void left(void);
void right(void);
void stop(void);

void go_track(void);
void back_track(void);
void left_track(void);
void right_track(void);
void stop_track(void);


#endif

