#include "HC-SR501.h"
#include "stm32f10x.h"
#include "delay.h"
void HCSR501_Init(void)
{
  GPIO_InitTypeDef GPIO_InitStructure;
 
 	RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOA,ENABLE);//使能PORTA,PORTE时钟

	GPIO_InitStructure.GPIO_Pin  = GPIO_Pin_5;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_IN_FLOATING; //设置成上拉输入
 	GPIO_Init(GPIOA, &GPIO_InitStructure);//初始化GPIOE4,3
}

int dection_people(void)
{	   
		    if(GPIO_ReadInputDataBit(GPIOA,GPIO_Pin_5))
				{
					  delay_ms(10);
				     if(GPIO_ReadInputDataBit(GPIOA,GPIO_Pin_5))
						 {
								  return 1;
						 }
				}
					
				else return 0;
}



