/***************************************************
multi wii implementation
***************************************************/
/***************************************************
Includes
***************************************************/
#include <sys/time.h>
#include <unistd.h>
#include "multiwii.h"
#include "msp.h"

/***************************************************
Types variables
***************************************************/
typedef void(*Callback_t)(void);

typedef enum
{
  STATE_IDLE,
  STATE_RAW_RC_WRITING,
  STATE_RAW_IMU_READING,
  STATE_MOTOR_READING,
  STATE_RC_READING,
  STATE_ATTITUDE_READING
} MultiWiiReadingState_t;

/***************************************************
Static variables
***************************************************/
static Callback_t callback;
static uint8_t    *dataBuffer;
static uint8_t    offset;
static struct timeval start;

static MultiWiiReadingState_t state;
/***************************************************
Prototypes
***************************************************/
static void mspCallback(void);
static void delayTimerFired(void);

/***************************************************
Implementation
***************************************************/
/***************************************************
brief Reads Reads all msp data

param[out] buffer - buffer for all MSP data (58 bytes);
param[in]  cb     - pointer to callback function
***************************************************/
void multiWiiInit(uint8_t *buffer, void (*cb)(void))
{
  dataBuffer = buffer;
  callback = cb;

//  delayTimer.interval = 1000;
//  delayTimer.mode     = TIMER_ONE_SHOT_MODE;
//  delayTimer.callback = delayTimerFired;
}

/***************************************************
brief Checks wheater exchange with multi wii is
  in progress

returns true if it is, otherwise - false
***************************************************/
bool multiWiiIsTransmissionInProgress(void)
{
  return (STATE_IDLE != state);
}

/***************************************************
brief Starts reading data from multi wii
***************************************************/
void multiWiiStartTransmission(uint8_t *data, uint8_t length)
{
  if (STATE_IDLE != state){
      struct timeval end;
      gettimeofday(&end, NULL);

      long mseconds  = (end.tv_sec  - start.tv_sec)*1000+(end.tv_usec - start.tv_usec)/1000;
      if(mseconds>100){
        delayTimerFired();
        gettimeofday(&start, NULL);
      }
      return;
  }
  //printf("Start transmission\n");
  memcpy(dataBuffer, data, length);
  offset = 0;
  state  = STATE_RAW_RC_WRITING;
  //Watchdog
  gettimeofday(&start, NULL);

  mspWriteRawRc(dataBuffer, mspCallback);
}

/***************************************************
brief Callback function for MSP transmission
***************************************************/
static void mspCallback(void)
{
  //printf("State: %d\n",state);
  switch(state)
  {
    case STATE_RAW_RC_WRITING:
      mspReadRawImu(dataBuffer, mspCallback);
      state = STATE_RAW_IMU_READING;
      break;
    case STATE_RAW_IMU_READING:
      offset += MSP_RAW_IMU_CMD_SIZE;
      state = STATE_MOTOR_READING;
      mspReadMotor(dataBuffer + offset, mspCallback);
      break;
    case STATE_MOTOR_READING:
      offset += MSP_MOTOR_CMD_SIZE;
      state = STATE_RC_READING;
      mspReadRc(dataBuffer + offset, mspCallback);
      break;
    case STATE_RC_READING:
      offset += MSP_RC_CMD_SIZE;
      state = STATE_ATTITUDE_READING;
      mspReadAttitude(dataBuffer + offset, mspCallback);
      break;
    case STATE_ATTITUDE_READING:
      state = STATE_IDLE;
      callback();
      break;
    default:
      break;
  }
}

/***************************************************
brief Delay timer has fired
***************************************************/
static void delayTimerFired(void)
{
  printf("Delayed timer fired\n");
  state = STATE_IDLE;
  dropState();
//  state = STATE_RAW_IMU_READING;
//  mspReadRawImu(dataBuffer, mspCallback);
}


