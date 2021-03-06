#include "track.h"
#include "motor.h"
#include "usart.h"
#include "delay.h"
#include "timer.h"
#include "math.h"

extern float yaw;
extern u8 a,b;
extern int study[2];
int i=0;
float temp;
void track_init(void)
{
   GPIO_InitTypeDef  GPIO_InitStructure; 	
	 RCC_APB2PeriphClockCmd(RCC_APB2Periph_AFIO|RCC_APB2Periph_GPIOB, ENABLE);	 //使能PB端口时钟
   
	 GPIO_InitStructure.GPIO_Pin = GPIO_Pin_12|GPIO_Pin_13|GPIO_Pin_14|GPIO_Pin_15;				
	 GPIO_InitStructure.GPIO_Mode = GPIO_Mode_IN_FLOATING; 		 //推挽输出
	 GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;		 //IO口速度为50MHz
	 GPIO_Init(GPIOB, &GPIO_InitStructure);					
}
/*
void track(void)
{
   if(mid_left==0&&mid_right==0)
		 go_track();
	 if(mid_left==1&&mid_right==0)
		 right_track();
	  if(mid_left==0&&mid_right==1)
		 left_track();
	 if(mid_left==1&&mid_right==1)
		 go_track();
}
*/
/*
void track(void)
{
	//if(left_read==1&&right_read==1)
	//{
		 if(mid_left==0&&mid_right==0)
			 go_track();
		 else if(mid_left==1&&mid_right==0)
			 left_track();
		 else if(mid_left==0&&mid_right==1)
			 right_track();
		 else if(mid_left==1&&mid_right==1)
			 go_track();
//		 else if(mid_left==0&&mid_right==0&&left_read==0)
//			 left_track();
//		  else if(mid_left==1&&mid_right==0&&right_read==0)
//			 right_track();
  //}
	else if((left_read==0&&mid_left==0)||(right_read==0&&mid_right==0))
	{
	    switch(str[i])
			{
				case 1:go_track();delay_ms(1000);i++;break;
				case 2:go_track();delay_ms(10);i++;break;
				case 3:right_track();delay_ms(10);i++;break;
			}
	}
}
*/

//void track(void)
//{
//	if(left_read==0&&right_read==0)
//	{
//		 if(mid_left==0&&mid_right==0)
//			 go_track();
//		 else if(mid_left==0&&mid_right==1)
//			 right_track();
//		 else if(mid_left==1&&mid_right==0)
//			 left_track();
//		 else if(mid_left==1&&mid_right==1)
//			 go_track();              
//  }
//	else if(left_read==1&&mid_left==1)
//	{
//	    switch(study[i])
//			{
//				case 1:
//						while(right_read==1)
//						{
//								left_track();
//						}
//						/*while(right_read==0)
//						{
//								left_track();
//						}*/
//						i++;
//						break;
//				case 2:
//					go_track();
//				  delay_ms(10);
//				  i++;
//					break;
//			//	case 3:right_track();delay_ms(10);i++;break;
//			}
//	}
//}


void track(void)
{
	if(left_read==0&&right_read==0)
	{
		 if(mid_left==0&&mid_right==0)
			 go_track();
		 else if(mid_left==0&&mid_right==1)
			 right_track();
		 else if(mid_left==1&&mid_right==0)
			 left_track();
		 else if(mid_left==1&&mid_right==1)
			 go_track();              
  }
	else if(left_read==1&&mid_left==1)
	{
	    switch(study[i])
			{
				case 1:
//					  temp=yaw;
//						while(yaw-temp<30)
//						{
//								left_track();
//							if(yaw<0)break;
//						}
					 left_track();
				   delay_ms(10);
						i++;
						break;
				case 2:
					go_track();
				  delay_ms(10);
				  i++;
					break;
			//	case 3:right_track();delay_ms(10);i++;break;
			}
	}
}




void detection_forks(void)
{
    if(left_read==1&&right_read==0)  //左边检测到
		{
			   a=1;                        //a置1
				 if(a!=b)                    //a！=b才发送
			 {
				   b=a;                     
			     printf("&x=1\n\n");
				   
			 }
		}
			 
			
		else if(left_read==0&&right_read==1)
		{
		     a=1;
				 if(a!=b)
			 {
				   b=a;
			     printf("&x=2\n\n");
				   
			 }
		}
			 
		else if(left_read==1&&right_read==1)
		{
		     a=1;
				 if(a!=b)
			 {
				   b=a;
			    printf("&x=3\n\n");
				   
			 }
		}
		else a=b=0;                  //2边都没检测到没有误旁诮将啊，b都置0 防止误判�
			
		

}
