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
	 u8 t=0,report=1;			//Ĭ�Ͽ����ϱ�
   u8 key;
	 extern u8 PWM1,PWM2;
   extern int people,fire;
	 extern u8 flag;
	 extern float pitch,roll,yaw; 		//ŷ����
	 short aacx,aacy,aacz;		//���ٶȴ�����ԭʼ����
	 short gyrox,gyroy,gyroz;	//������ԭʼ����
	 short temp;					//�¶�	
	 
	 NVIC_PriorityGroupConfig(NVIC_PriorityGroup_2);	 //����NVIC�жϷ���2:2λ��ռ���ȼ���2λ��Ӧ���ȼ�
	 uart1_init(9600);	 	//���ڳ�ʼ��Ϊ500000
	 uart2_init(115200);	 	//���ڳ�ʼ��Ϊ500000
	 //uart2_init(9600);	 	//���ڳ�ʼ��Ϊ500000
	 delay_init();
	 LED_Init();
	 motor_Init();
	 track_init();
	 fire_init();
	 MPU_Init();					//��ʼ��MPU6050
	 TIM3_PWM_Init(199,7199);	 //����Ƶ��PWMƵ��=72000000/900=80Khz
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
					temp=MPU_Get_Temperature();	//�õ��¶�ֵ
					MPU_Get_Accelerometer(&aacx,&aacy,&aacz);	//�õ����ٶȴ���������
					MPU_Get_Gyroscope(&gyrox,&gyroy,&gyroz);	//�õ�����������
					
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
	u8 t=0,report=1;			//Ĭ�Ͽ����ϱ�
	u8 key;
	extern u8 flag;
	extern u8 PWM1;
	extern u8 PWM2;
	extern int people,fire;
	extern float pitch,roll,yaw; 		//ŷ����
	short aacx,aacy,aacz;		//���ٶȴ�����ԭʼ����
	short gyrox,gyroy,gyroz;	//������ԭʼ����
	short temp;					//�¶�	
	 
	NVIC_PriorityGroupConfig(NVIC_PriorityGroup_2);	 //����NVIC�жϷ���2:2λ��ռ���ȼ���2λ��Ӧ���ȼ�
	uart_init(9600);	 	//���ڳ�ʼ��Ϊ500000
	delay_init();	//��ʱ��ʼ�� 
	LED_Init();		  			//��ʼ����LED���ӵ�Ӳ���ӿ�
	KEY_Init();					//��ʼ������
  track_init();
	LCD_Init();			   		//��ʼ��LCD  
	MPU_Init();					//��ʼ��MPU6050
	motor_Init();
	TIM3_PWM_Init(199,7199);	 //����Ƶ��PWMƵ��=72000000/900=80Khz
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
//			temp=MPU_Get_Temperature();	//�õ��¶�ֵ
//			MPU_Get_Accelerometer(&aacx,&aacy,&aacz);	//�õ����ٶȴ���������
//			MPU_Get_Gyroscope(&gyrox,&gyroy,&gyroz);	//�õ�����������
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


