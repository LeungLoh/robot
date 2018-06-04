#include "motor.h"
#include "time.h"
void motor_Init(void)
{
	GPIO_InitTypeDef  GPIO_InitStructure; 	
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_AFIO|RCC_APB2Periph_GPIOB|RCC_APB2Periph_GPIOG, ENABLE);	 //使能PB端口时钟
  GPIO_PinRemapConfig(GPIO_Remap_SWJ_JTAGDisable,ENABLE);
	
	 GPIO_InitStructure.GPIO_Pin = GPIO_Pin_3|GPIO_Pin_4|GPIO_Pin_5|GPIO_Pin_6;				
	 GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP; 		 //推挽输出
	 GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;		 //IO口速度为50MHz
	 GPIO_Init(GPIOB, &GPIO_InitStructure);					 //根据设定参数初始化GPIOB.3,GPIOB.4,GPIOB.5,GPIOB.6
	 
	/*
	 GPIO_InitStructure.GPIO_Pin = GPIO_Pin_15;				
	 GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP; 		 //推挽输出
	 GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;		 //IO口速度为50MHz
	 GPIO_Init(GPIOG, &GPIO_InitStructure);					 //根据设定参数初始化GPIOB.3,GPIOB.4,GPIOB.5,GPIOB.6
	
	*/
	 GPIO_ResetBits(GPIOB,GPIO_Pin_3);						 //PB.3 输出低
	 GPIO_ResetBits(GPIOB,GPIO_Pin_4);						 //PB.4 输出低
	 GPIO_ResetBits(GPIOB,GPIO_Pin_5);						 //PB.7 输出低
	 GPIO_ResetBits(GPIOB,GPIO_Pin_6);						 //PB.6 输出低

}

void go(void)
{
   IN1=1;
	 IN2=0;
	 IN3=1;
	 IN4=0;
	 TIM_SetCompare3(TIM3,200); 
	 TIM_SetCompare4(TIM3,200); 
}
void back(void)
{
   
	 IN1=0;
	 IN2=1;
	 IN3=0;
	 IN4=1;
	 TIM_SetCompare3(TIM3,200); //ENA
	 TIM_SetCompare4(TIM3,200); //ENB
}
void left(void)
{ 
	 IN1=0;
	 IN2=1;
	 IN3=1;
	 IN4=0;
	 TIM_SetCompare3(TIM3,200); 
	 TIM_SetCompare4(TIM3,200); 
}
void right(void)
{
   
	 IN1=1;
	 IN2=0;
	 IN3=0;
	 IN4=1;
	 TIM_SetCompare3(TIM3,200); 
	 TIM_SetCompare4(TIM3,200); 
}
void stop()
{
   IN1=0;
	 IN2=0;
	 IN3=0;
	 IN4=0;
	 TIM_SetCompare3(TIM3,0); 
	 TIM_SetCompare4(TIM3,0); 
}


void go_track(void)
{
   IN1=1;
	 IN2=0;
	 IN3=1;
	 IN4=0;
	 TIM_SetCompare3(TIM3,100); 
	 TIM_SetCompare4(TIM3,100); 
}
void back_track(void)
{
   
	 IN1=0;
	 IN2=1;
	 IN3=0;
	 IN4=1;
	 TIM_SetCompare3(TIM3,100); //ENA
	 TIM_SetCompare4(TIM3,100); //ENB
}
void left_track(void)
{ 
	 IN1=0;
	 IN2=1;
	 IN3=1;
	 IN4=0;
	 TIM_SetCompare3(TIM3,100); 
	 TIM_SetCompare4(TIM3,100); 
}
void right_track(void)
{
   
	 IN1=1;
	 IN2=0;
	 IN3=0;
	 IN4=1;
	 TIM_SetCompare3(TIM3,100); 
	 TIM_SetCompare4(TIM3,100); 
}
void stop_track()
{
   IN1=0;
	 IN2=0;
	 IN3=0;
	 IN4=0;
	 TIM_SetCompare3(TIM3,0); 
	 TIM_SetCompare4(TIM3,0); 
}
