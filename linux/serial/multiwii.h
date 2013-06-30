/***************************************************
multi wii interface
***************************************************/

#ifndef _MULTIWII_H
#define _MULTIWII_H

/***************************************************
Includes
***************************************************/
#include <stdint.h>
#include <stdbool.h>

/***************************************************
Prototypes
***************************************************/
/***************************************************
brief Reads all msp data

param[out] buffer - buffer for all MSP data (58 bytes);
param[in]  cb     - pointer to callback function
***************************************************/
void multiWiiInit(uint8_t *buffer, void (*cb)(void));

/***************************************************
brief Checks wheater exchange with multi wii is
  in progress

returns true if it is, otherwise - false
***************************************************/
bool multiWiiIsTransmissionInProgress(void);

/***************************************************
brief Starts reading data from multi wii

\param[in] data   - data that should be send to multiwii
\param[in] length - length of data (octets)
***************************************************/
void multiWiiStartTransmission(uint8_t *data, uint8_t length);

#endif // _MULTIWII_H


