#include "led.h"
#include "delay.h"
#include "key.h"
#include "sys.h"
#include "usart.h"
#include "lcd.h"
#include "motor.h"
#include "HC-SR501.h"
#include "fire.h"
#include "track.h"
#include "usart.h"
#include "timer.h"
#include "mpu6050.h"   
#include "inv_mpu.h"
#include "inv_mpu_dmp_motion_driver.h" 


int main(void)
 {	
	 u8 t=0,report=1;			//默认开启上报
   u8 key;
	 extern u8 PWM1,PWM2;
   extern int people,fire;
	 extern u8 flag;
	 extern float pitch,roll,yaw; 		//欧拉角
	 short aacx,aacy,aacz;		//加速度传感器原始数据
	 short gyrox,gyroy,gyroz;	//陀螺仪原始数据
	 short temp;					//温度	
	 
	 NVIC_PriorityGroupConfig(NVIC_PriorityGroup_2);	 //设置NVIC中断分组2:2位抢占优先级，2位响应优先级
	 uart1_init(9600);	 	//串口初始化为500000
	 uart2_init(115200);	 	//串口初始化为500000
	 //uart2_init(9600);	 	//串口初始化为500000
	 delay_init();
	 LED_Init();
	 motor_Init();
	 track_init();
	 fire_init();
	 MPU_Init();					//初始化MPU6050
	 TIM3_PWM_Init(199,7199);	 //不分频。PWM频率=72000000/900=80Khz
	 TIM2_Int_Init(1999,7199);
	 while(mpu_dmp_init());
   
	 while(1)
	{
	/*********************************************************/
		      people=dection_people();
					fire=judge_fire();		  
	/*********************************************************/
		
		
  /*********************************************************/
				key=KEY_Scan(0);
				if(key==KEY0_PRES)
				{
					report=!report;
				}
				if(mpu_dmp_get_data(&pitch,&roll,&yaw)==0)
				{ 
					temp=MPU_Get_Temperature();	//得到温度值
					MPU_Get_Accelerometer(&aacx,&aacy,&aacz);	//得到加速度传感器数据
					MPU_Get_Gyroscope(&gyrox,&gyroy,&gyroz);	//得到陀螺仪数据
					
					if((t%10)==0)
					{ 
						 
						temp=yaw*10;
						t=0;
						
					}
				}
				t++;  
  /*********************************************************/
				
	/*********************************************************/
				TIM_SetCompare1(TIM3,PWM1); 
				TIM_SetCompare2(TIM3,PWM2); 
	/*********************************************************/
				
	/*********************************************************/
				if(flag)track();
				if(!flag)detection_forks();
	/*********************************************************/
	}
 
				
	
}
 
/*
 int main(void)
 {	 
	u8 t=0,report=1;			//默认开启上报
	u8 key;
	extern u8 flag;
	extern u8 PWM1;
	extern u8 PWM2;
	extern int people,fire;
	extern float pitch,roll,yaw; 		//欧拉角
	short aacx,aacy,aacz;		//加速度传感器原始数据
	short gyrox,gyroy,gyroz;	//陀螺仪原始数据
	short temp;					//温度	
	 
	NVIC_PriorityGroupConfig(NVIC_PriorityGroup_2);	 //设置NVIC中断分组2:2位抢占优先级，2位响应优先级
	uart_init(9600);	 	//串口初始化为500000
	delay_init();	//延时初始化 
	LED_Init();		  			//初始化与LED连接的硬件接口
	KEY_Init();					//初始化按键
  track_init();
	LCD_Init();			   		//初始化LCD  
	MPU_Init();					//初始化MPU6050
	motor_Init();
	TIM3_PWM_Init(199,7199);	 //不分频。PWM频率=72000000/900=80Khz
	TIM2_Int_Init(1999,7199);
	/*
	while(mpu_dmp_init())
 	{
	}  
	*/
// 	while(1)
//	{
//		
//		/*************************************************/
//		people=dection_people();
//		fire=judge_fire();
//		
//		/*************************************************/

//		/*************************************************/
//	/*	key=KEY_Scan(0);
//		if(key==KEY0_PRES)
//		{
//			report=!report;
//		}
//		if(mpu_dmp_get_data(&pitch,&roll,&yaw)==0)
//		{ 
//			temp=MPU_Get_Temperature();	//得到温度值
//			MPU_Get_Accelerometer(&aacx,&aacy,&aacz);	//得到加速度传感器数据
//			MPU_Get_Gyroscope(&gyrox,&gyroy,&gyroz);	//得到陀螺仪数据
//			
//			if((t%10)==0)
//			{ 
//				 
//				temp=yaw*10;
//				t=0;
//				
//			}
//		}
//		t++; */
//		/************************************************/
//		
//		/***********************************************/
//		 TIM_SetCompare2(TIM3,PWM1); 
//    /***********************************************/   
//		
//		/***********************************************/   		
//		
//		if(flag)track();	
//		if(!flag)detection_forks();
//		
//		/***********************************************/
//	} 	
//}
// 


