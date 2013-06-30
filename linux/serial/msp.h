/***************************************************
MSP interface
***************************************************/

#ifndef _MSP_H
#define _MSP_H

/***************************************************
Includes
***************************************************/
#include <stdint.h>

/***************************************************
Macros
***************************************************/
#define MSP_RAW_IMU_CMD_SIZE  18u
#define MSP_MOTOR_CMD_SIZE    16u
#define MSP_RC_CMD_SIZE       16u
#define MSP_ATTITUDE_CMD_SIZE 8u
#define MSP_SET_RAW_RC_SIZE   16u

#define MSP_CMD_FOOTER_SIZE   1u
#define MSP_CMD_HEADER_SIZE   5u

/***************************************************
Prototypes
***************************************************/
/***************************************************
brief Reads msp raw IMU

param[out] buffer - buffer for IMU data (18 bytes);
param[in]  cb     - pointer to callback function
***************************************************/
void mspReadRawImu(uint8_t *buffer, void(*cb)(void));

/***************************************************
brief Reads msp motor

param[out] buffer - buffer for motor data (16 bytes);
param[in]  cb     - pointer to callback function
***************************************************/
void mspReadMotor(uint8_t *buffer, void(*cb)(void));

/***************************************************
brief Reads msp rc

param[out] buffer - buffer for rc data (16 bytes);
param[in]  cb     - pointer to callback function
***************************************************/
void mspReadRc(uint8_t *buffer, void(*cb)(void));

/***************************************************
brief Reads msp attitude

param[out] buffer - buffer for attitudess data (8 bytes);
param[in]  cb     - pointer to callback function
***************************************************/
void mspReadAttitude(uint8_t *buffer, void(*cb)(void));

/***************************************************
brief Reads msp attitude

param[out] buffer - buffer for attitudess data (8 bytes);
param[in]  cb     - pointer to callback function
***************************************************/
void mspReadAttitude(uint8_t *buffer, void(*cb)(void));

/***************************************************
brief Writes msp rc

param[out] buffer - buffer with rc data (16 bytes);
param[in]  cb     - pointer to callback function
***************************************************/
void mspWriteRawRc(uint8_t *buffer, void(*cb)(void));

/***************************************************
brief Reads data from UART

param[in] length - amount of bytes
***************************************************/
void mspReadResponse(uint16_t length);

#endif // _MSP_H


