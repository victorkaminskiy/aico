/***************************************************
MSP implementation
***************************************************/

/***************************************************
Includes
***************************************************/
#include "msp.h"
#include "uart.h"

/***************************************************
Macros
***************************************************/
#define MSP_RAW_IMU_CMD    102u
#define MSP_MOTOR_CMD      104u
#define MSP_RC_CMD         105u
#define MSP_ATTITUDE_CMD   108u
#define MSP_SET_RAW_RC_CMD 200u

#define MAX_DATA_SIZE 16u
#define CHECKSUM_SIZE 1u

/***************************************************
Types
***************************************************/
typedef struct __attribute__ ((__packed__)) NoDataFrame
{
  uint8_t sof1;
  uint8_t sof2;
  uint8_t sof3;
  uint8_t dataLength;
  uint8_t code;
  uint8_t checksum;
} MSP_NoDataFrame_t;

typedef struct __attribute__ ((__packed__)) DataFrame
{
  uint8_t sof1;
  uint8_t sof2;
  uint8_t sof3;
  uint8_t dataLength;
  uint8_t code;
  uint8_t data[MAX_DATA_SIZE + CHECKSUM_SIZE];
} MSP_DataFrame_t;

typedef enum
{
  MSP_STATE_IDLE,
  MSP_STATE_WAITING_FOR_RESPONSE
} MSP_State_t;

typedef void(*Callback_t)(void);

/***************************************************
Static variables
***************************************************/
static Callback_t  callback;
static MSP_State_t mspState;
static uint8_t     bytesLeft;
static uint8_t     bytesRead;
static uint8_t     bytesToCopy;
static uint8_t     uartTxBuffer[sizeof(MSP_DataFrame_t)];
static uint8_t     uartTmpBuffer[100];
static uint8_t     *uartRxBuffer;

/***************************************************
Prototypes
***************************************************/
static void fillMspFrame(MSP_NoDataFrame_t *frame, uint8_t cmd);
static void initializeTransmission(void(*cb)(void), uint8_t size, uint8_t *buffer);

/***************************************************
Implementation
***************************************************/
/***************************************************
brief Reads msp raw IMU

param[out] buffer - read IMU (18 bytes);
param[in]  cb     - pointer to callback function
***************************************************/
void mspReadRawImu(uint8_t *buffer, void(*cb)(void))
{
  MSP_NoDataFrame_t *frame = (MSP_NoDataFrame_t *)uartTxBuffer;

  if (MSP_STATE_IDLE != mspState)
    return;

  fillMspFrame(frame, MSP_RAW_IMU_CMD);
  initializeTransmission(cb, MSP_RAW_IMU_CMD_SIZE, buffer);
  sendDataToUart(uartTxBuffer, sizeof(MSP_NoDataFrame_t));
}

/***************************************************
brief Reads msp motor

param[out] buffer - read motor (16 bytes);
param[in]  cb     - pointer to callback function
***************************************************/
void mspReadMotor(uint8_t *buffer, void(*cb)(void))
{
  MSP_NoDataFrame_t *frame = (MSP_NoDataFrame_t *)uartTxBuffer;

  if (MSP_STATE_IDLE != mspState)
    return;

  fillMspFrame(frame, MSP_MOTOR_CMD);
  initializeTransmission(cb, MSP_MOTOR_CMD_SIZE, buffer);
  sendDataToUart(uartTxBuffer, sizeof(MSP_NoDataFrame_t));
}

/***************************************************
brief Reads msp rc

param[out] buffer - read rc (16 bytes);
param[in]  cb     - pointer to callback function
***************************************************/
void mspReadRc(uint8_t *buffer, void(*cb)(void))
{
  MSP_NoDataFrame_t *frame = (MSP_NoDataFrame_t *)uartTxBuffer;

  if (MSP_STATE_IDLE != mspState)
    return;

  fillMspFrame(frame, MSP_RC_CMD);
  initializeTransmission(cb, MSP_RC_CMD_SIZE, buffer);
  sendDataToUart(uartTxBuffer, sizeof(MSP_NoDataFrame_t));
}

/***************************************************
brief Reads msp attitude

param[out] buffer - read attitude (8 bytes);
param[in]  cb     - pointer to callback function
***************************************************/
void mspReadAttitude(uint8_t *buffer, void(*cb)(void))
{
  MSP_NoDataFrame_t *frame = (MSP_NoDataFrame_t *)uartTxBuffer;

  if (MSP_STATE_IDLE != mspState)
    return;

  fillMspFrame(frame, MSP_ATTITUDE_CMD);
  initializeTransmission(cb, MSP_ATTITUDE_CMD_SIZE, buffer);
  sendDataToUart(uartTxBuffer, sizeof(MSP_NoDataFrame_t));
}

/***************************************************
brief Writes msp rc

param[out] buffer - buffer with rc data (16 bytes);
param[in]  cb     - pointer to callback function
***************************************************/
void mspWriteRawRc(uint8_t *buffer, void(*cb)(void))
{
  MSP_DataFrame_t *frame = (MSP_DataFrame_t *)uartTxBuffer;
  uint8_t crc = 0;

  if (MSP_STATE_IDLE != mspState)
    return;

  crc = MSP_SET_RAW_RC_CMD;
  for (uint8_t i = 0; i < MSP_SET_RAW_RC_SIZE; i++)
    crc ^= buffer[i];

  frame->sof1       = '$';
  frame->sof2       = 'M';
  frame->sof3       = '<';
  frame->dataLength = MSP_SET_RAW_RC_SIZE;
  frame->code       = MSP_SET_RAW_RC_CMD;
  memcpy(frame->data, buffer, MSP_SET_RAW_RC_SIZE);
  frame->data[MSP_SET_RAW_RC_SIZE] = crc;

  mspState = MSP_STATE_IDLE;

  initializeTransmission(cb, 0, buffer);
  sendDataToUart(uartTxBuffer, sizeof(MSP_NoDataFrame_t) + MSP_SET_RAW_RC_SIZE);
}

/***************************************************
brief Reads data from UART

param[in] length - amount of bytes
***************************************************/
void mspReadResponse()
{
  int16_t readBytes = readDataFromUart(&uartTmpBuffer[bytesRead], bytesLeft);

  if (-1 == readBytes)
    return;
  if(mspState==MSP_STATE_WAITING_FOR_RESPONSE){
      bytesLeft -= readBytes;
      bytesRead += readBytes;

      //printf("Bytes left: %d\n",bytesLeft);

      if (!bytesLeft)
      {
        memcpy(uartRxBuffer,uartTmpBuffer+MSP_CMD_HEADER_SIZE,bytesToCopy);
//        printf("Read %d: ", uartRxBuffer);
//        for (int i = 0; i < bytesToCopy; i++)
//        {
//            printf("%02X ", uartRxBuffer[i]);
//        }
//        printf("\n");
        mspState = MSP_STATE_IDLE;
        if (callback)
          callback();
      }
  }
}

void dropState(){
    mspState = MSP_STATE_IDLE;
}


/***************************************************
brief Fills msp frame

param[out] frame - pointer to frame;
param[in]  cmd   - command
***************************************************/
static void fillMspFrame(MSP_NoDataFrame_t *frame, uint8_t cmd)
{
  frame->sof1       = '$';
  frame->sof2       = 'M';
  frame->sof3       = '<';
  frame->dataLength = 0;
  frame->code       = cmd;
  frame->checksum   = cmd;
}

/***************************************************
brief Initializes transmission

param[in] cb     - pointer to callback function;
param[in] size   - command size;
param[in] buffer - pointer tooutput buffer
***************************************************/
static void initializeTransmission(void(*cb)(void), uint8_t size, uint8_t *buffer)
{
  callback  = cb;
  bytesLeft = size+MSP_CMD_HEADER_SIZE+MSP_CMD_FOOTER_SIZE;
  uartRxBuffer = buffer;
  bytesRead = 0;
  bytesToCopy = size;//-MSP_CMD_HEADER_SIZE-MSP_CMD_FOOTER_SIZE;
  mspState  = MSP_STATE_WAITING_FOR_RESPONSE;
}


