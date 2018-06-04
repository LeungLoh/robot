#ifndef __TRACK_H
#define __TRACK_H
#include "sys.h"
#define left_read PBin(12)
#define mid_left PBin(13)
#define mid_right PBin(14)
#define right_read PBin(15)
void track_init(void);
void track(void);
void detection_forks(void);
#endif

