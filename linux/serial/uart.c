#include "uart.h"

#include <termios.h>
#include <unistd.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdio.h>

static int fd = -1;

int setInterfaceAttribs(int fd, int speed, int parity)
{
    struct termios tty;
    memset (&tty, 0, sizeof tty);
    if (tcgetattr (fd, &tty) != 0)
    {
        perror("error from tcgetattr");
        return -1;
    }

    cfsetospeed (&tty, speed);
    cfsetispeed (&tty, speed);

    tty.c_cflag = (tty.c_cflag & ~CSIZE) | CS8;     // 8-bit chars
    // disable IGNBRK for mismatched speed tests; otherwise receive break
    // as \000 chars
    tty.c_iflag &= ~IGNBRK;         // ignore break signal
    tty.c_lflag = 0;                // no signaling chars, no echo,
    // no canonical processing
    tty.c_oflag = 0;                // no remapping, no delays
    tty.c_cflag &= ~ICANON;
    tty.c_cc[VMIN]  = 0;            // read doesn't block
    tty.c_cc[VTIME] = 5;            // 0.5 seconds read timeout

    tty.c_iflag &= ~(IXON | IXOFF | IXANY); // shut off xon/xoff ctrl

    tty.c_cflag |= (CLOCAL | CREAD);// ignore modem controls,
    // enable reading
    tty.c_cflag &= ~(PARENB | PARODD);      // shut off parity
    tty.c_cflag |= parity;
    tty.c_cflag &= ~CSTOPB;
    //tty.c_cflag &= ~CRTSCTS;

    if (tcsetattr (fd, TCSANOW, &tty) != 0)
    {
        perror ("error from tcsetattr");
        return -1;
    }
    return 0;
}

void setBlocking (int fd, int should_block)
{
    struct termios tty;
    memset (&tty, 0, sizeof tty);
    if (tcgetattr (fd, &tty) != 0)
    {
        perror ("error from tggetattr");
        return;
    }

    tty.c_cc[VMIN]  = should_block ? 1 : 0;
    tty.c_cc[VTIME] = 5;            // 0.5 seconds read timeout

    if (tcsetattr (fd, TCSANOW, &tty) != 0)
        perror ("error setting term attributes");
}


void initUart(char *portname)
{
    fd = open (portname, O_RDWR | O_NOCTTY | O_SYNC);
    if (fd < 0)
    {
        perror ("error opening uart");
        return;
    }

    setInterfaceAttribs (fd, BAUDRATE, 0);  // set speed to 115,200 bps, 8n1 (no parity)
    setBlocking (fd, 1);                // set blocking
}

void sendDataToUart(uint8_t *buffer, int16_t size)
{
    printf("Write: ");
    for (int i = 0; i < size; i++)
    {
        printf("%02X ", buffer[i]);
    }
    printf("\n");
    write (fd, buffer, size);
    usleep(50000);
}

int16_t readDataFromUart(uint8_t *buffer, int16_t size)
{
//    printf("Reading...\n");
    int len=read (fd, buffer, size);
//    printf("Read %d: ", len);
//    for (int i = 0; i < len; i++)
//    {
//        printf("%02X ", buffer[i]);
//    }
//    printf("\n");
    return len;
}
