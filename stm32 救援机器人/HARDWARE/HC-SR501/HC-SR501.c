#include "HC-SR501.h"
#include "stm32f10x.h"
#include "delay.h"
void HCSR501_Init(void)
{
  GPIO_InitTypeDef GPIO_InitStructure;
 
 	RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOA,ENABLE);//ʹ��PORTA,PORTEʱ��

	GPIO_InitStructure.GPIO_Pin  = GPIO_Pin_5;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_IN_FLOATING; //���ó���������
 	GPIO_Init(GPIOA, &GPIO_InitStructure);//��ʼ��GPIOE4,3
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



