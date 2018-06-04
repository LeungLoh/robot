#include "fire.h"
#include "delay.h"

void fire_init(void)
{
		GPIO_InitTypeDef  GPIO_InitStructure; 	
		RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOA, ENABLE);	 //Ê¹ÄÜPB¶Ë¿ÚÊ±Ö
	
	 GPIO_InitStructure.GPIO_Pin = GPIO_Pin_0|GPIO_Pin_1;				
	 GPIO_InitStructure.GPIO_Mode = GPIO_Mode_IN_FLOATING; 		 //ÍÆÍìÊä³ö
	 GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;		 //IO¿ÚËÙ¶ÈÎª50MHz
	 GPIO_Init(GPIOA, &GPIO_InitStructure);					 //¸ù¾ÝÉè¶¨²ÎÊý³õÊ¼»¯GPIOB.3,GPIOB.4,GPIOB.5,GPIOB.6

}

int judge_fire(void)
{	   
		    if(GPIO_ReadInputDataBit(GPIOA,GPIO_Pin_0)&&GPIO_ReadInputDataBit(GPIOA,GPIO_Pin_1))
				{
					   delay_ms(10);
				     if(GPIO_ReadInputDataBit(GPIOA,GPIO_Pin_1)&&GPIO_ReadInputDataBit(GPIOA,GPIO_Pin_0))
						 {
								  return 1;
						 }
				}
					
				else return 0;
				
				
}







