
#ifndef __TICKS_PER_USEC__
#define __TICKS_PER_USEC__
long long unsigned int __ticks_per_usec=0;
#endif

#ifndef __RDTSC__
#define __RDTSC__
#include <stdint.h>
#include <sys/types.h>
#include <sys/time.h>
#include <sys/resource.h>
#include <unistd.h>
#include <sched.h>
#include <stdio.h>
#include <errno.h>



extern "C" {
  __inline__ uint64_t rdtsc() {
    uint32_t lo, hi;
    __asm__ __volatile__ (      // serialize
    "xorl %%eax,%%eax \n        cpuid"
    ::: "%rax", "%rbx", "%rcx", "%rdx");
    /* We cannot use "=A", since this would use %rax on x86_64 and return only the lower 32bits of the TSC */
    __asm__ __volatile__ ("rdtsc" : "=a" (lo), "=d" (hi));
    return (uint64_t)hi << 32 | lo;
  }
}

extern "C" {
  __inline__ long long unsigned int ticks2ns(long long unsigned int ts){
    return (ts*1000L) / __ticks_per_usec;    
  }
}


extern "C" {
  __inline__ void calculate_ticks_per_usec(){
  struct timeval tday;
  long long unsigned int eusecs,susecs, elapsed_usecs;
  long long unsigned int etick,stick, elapsed_ticks;
  struct sched_param sparam;
  int old_priority;
  int old_policy;

  old_priority = getpriority(PRIO_PROCESS,getpid());
  old_policy = sched_getscheduler(getpid());

  sparam.sched_priority = 10;

  if (sched_setscheduler(getpid(), SCHED_FIFO, &sparam) <0){
     perror("Could not changed to fixed priority. Use \"sudo <cmd>\""); 
  }
  
  printf("Testing for five seconds. Please wait...\n");

  stick = rdtsc();
  
  gettimeofday(&tday,NULL);
  susecs = tday.tv_usec + (1000000L)* tday.tv_sec;
  eusecs = susecs;

  usleep(5000);
  
  gettimeofday(&tday,NULL);
  eusecs = tday.tv_usec + (1000000L)* tday.tv_sec;

  etick = rdtsc();

  elapsed_usecs = eusecs - susecs;
  elapsed_ticks = etick - stick;

  __ticks_per_usec = elapsed_ticks / elapsed_usecs;  

  sparam.sched_priority = old_priority;

  if (sched_setscheduler(getpid(), old_policy, &sparam) <0){
     perror("Could not changed to fixed priority. Use \"sudo <cmd>\""); 
  }

  printf("Done.\n");
}
}
#endif
