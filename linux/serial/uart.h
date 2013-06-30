#ifndef _UART_H
#define _UART_H

#include <stdint.h>

#define BAUDRATE B115200

void initUart(char *portname);

void sendDataToUart(uint8_t *buffer, int16_t size);

int16_t readDataFromUart(uint8_t *buffer, int16_t size);

#endif // UART_H
