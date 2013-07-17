#include <arpa/inet.h>
#include <netinet/in.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <errno.h>
#include <pthread.h>


#define BUFLEN 100
#define PORT 9930


#define UART "/dev/ttyUSB0"

#define FALSE 0
#define TRUE 1

static char multiwiiBuf[BUFLEN];
static struct sockaddr_in si_me, si_other;
static int s, i, slen=sizeof(si_other);


void diep(char *s)
{
    perror(s);
    exit(1);
}

void sendDataBack()
{
    if (sendto(s, multiwiiBuf, BUFLEN, 0, &si_other, slen)==-1)
    {
        diep("sendto()");
    }
    printf("Sent: ");
    for (int i = 0; i < 20; i++)
    {
        printf("%02X ", (uint8_t)multiwiiBuf[i]);
    }
    printf("\n");
 //   printf("data sent\n");

}

void *receive(void *ptr)
{


    while(1){
        mspReadResponse();
    }


    return NULL;

}

int main(int argc, char *argv[])
{
    if(argc>1){
        initUart(argv[1]);
    }else{
        initUart(UART);
    }
    char buf[BUFLEN];

    if ((s=socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP))==-1)
    {
        diep("socket");
    }

    memset((char *) &si_me, 0, sizeof(si_me));
    si_me.sin_family = AF_INET;
    si_me.sin_port = htons(PORT);
    si_me.sin_addr.s_addr = htonl(INADDR_ANY);
    if (bind(s, &si_me, sizeof(si_me))==-1)
    {
        diep("bind");
    }

    multiWiiInit(multiwiiBuf,sendDataBack);
    //Start thread for UART
    pthread_t receive_thread;

    if(pthread_create(&receive_thread, NULL, receive, NULL)) {

        diep("Error creating thread");
    }

    int len=0;
    while (1)
    {
        if ((len = recvfrom(s, buf, BUFLEN, 0, &si_other, &slen)) == -1)
        {
            diep("recvfrom()");
        }
//        printf("data received\n");

        multiWiiStartTransmission(buf,len);

        //inet_ntoa(si_other.sin_addr), ntohs(si_other.sin_port), buf);
    }

    close(s);

    return 0;
}



