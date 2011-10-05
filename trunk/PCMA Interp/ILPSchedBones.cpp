#include <sched.h>
#include <iostream>
#include <fstream>
#include <time.h>
#include <sys/time.h>
#include <cstdio>
#include <ctime>
#include <map>
#include "rdtsc.h"
#include "Execute.h"
#include "ApplicationpurpleApp.h"
using namespace std;

void sleep(int time)
{
	usleep(time);
	// Dummy for the OS sleep function
}

/*Moved the nextRelease and Task Methods to the ApplciationCOLORApp.cpp files. Causes a bit of a headache
but I think I've got them taken care of. A second pair of eyes would be good though. */
 
typedef struct {
	int priority;
	bool ready;
	bool (Application::*nextrelease)(int,int);
	void (Application::*task)();
	char* taskName;
	char* appName;
	int myPeriod;
	Application* app;
	int numExecutions;
} ScheduleEntry;


/*Following function and use of clock_gettime was found in http://www.guyrutenberg.com/2007/09/22/profiling-code-using-clock_gettime/ courtesy of Guy rutenburg*/

timespec diff(timespec start, timespec end)
{
	timespec temp;
	if ((end.tv_nsec-start.tv_nsec)<0) {
		temp.tv_sec = end.tv_sec-start.tv_sec-1;
		temp.tv_nsec = 1000000000+end.tv_nsec-start.tv_nsec;
	} else {
		temp.tv_sec = end.tv_sec-start.tv_sec;
		temp.tv_nsec = end.tv_nsec-start.tv_nsec;
	}
	return temp;
}

void Execute::executeTasks(int executions, int trash1, int trash2){
	 struct sched_param sched_p;
	 long long unsigned int startClockTicks, finishClockTicks, elapseClockTicks, elapseClockNs;
	 clock_t startClock,finishClock;
	 double timeCount;
	 startClock = rdtsc();
	//hrtime_t start, end;
 	//start = gethrtime();
	timespec time1, time2, time3;
    	int temp;
	clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &time1);

	std::cout<< " startClock = "<< time1.tv_sec << std::endl;
	std::map<string, std::map<int, long> > timeMap;
	
	int i = 0; 
 	 ofstream myfile;
	myfile.open("outputWorstEx-Overlaps:+0");
	ofstream excelOutput;
	excelOutput.open("excelOutput-WorstEx.txt");
	int totalExec = 1; 
	clock_t midStartClock, midFinishClock;
	long long unsigned int midStartClockTicks, midFinishClockTicks, midElapseClockTicks, midElapseClockNs;
	calculate_ticks_per_usec();
	sched_p.sched_priority = 10;
	if (sched_setscheduler(getpid(), SCHED_FIFO, &sched_p) <0){
	  perror("Could not changed to fixed priority. Use \"sudo <cmd>\"");
	}

	/* We set all of the pointers to the tasks (functions we'll be calling). This includes the release methods for each tasks
	Where the priorities are met. */

	/* Since C++ requires the instance of the object you're calling the method on (i.e, when we pull out a function pointer
	for TaskpurpleApp01 we still have to pass an instance of purple app to make the call work) I've added in the Application name as 
	a bit of the struct */
	ScheduleEntry schedule[] = {
		
	};
	int tn1 = 0;
	int tn2 = 0;
	int nSchedule = sizeof(schedule) / sizeof(ScheduleEntry);
	
	while(totalExec >0){
		startClockTicks=rdtsc();
		startClock = rdtsc();
		i =0;
		/*ranclock will give end time. We can change the clock when we like */
		clock_t ranClock;// = std::clock();
		int highestReadyPriority;
		int highestReadyIndex;

		// Initialize the schedule data structure by releasing all the tasks at the outset
		// Do this by passing an impossibly large clock, so every nextrelease() function returns true
		/* Again, had to change this up a bit since we aren't (and can't due to the fact we want to enforce data restrictions between 
		applications (at least not obviously in a way I can think of)) so that we do a conditional to check and see what application instance to 			use */
		std::cout<<"nscheudle size = " << nSchedule <<std::endl;
		for (int j = 0; j < nSchedule; j++) {
			//std::cout<<"in for loop" << std::endl;
			string tname =schedule[j].taskName;//spot1
			schedule[j].ready = (*schedule[j].app.*schedule[j].nextrelease)(-85, schedule[j].myPeriod); 
			//else if(PUT OTHER APPS HERE)
		//	std::cout<<"schedulget[j].ready = ";
		//	std::cout<<schedule[j].ready;
		//	std::cout<<std::endl;
		}
		/* We can either do this by number of task executions or specify an amount of time to execute. If we wanted to we could do something where a predefined number of major-frames execute */
		double loopTimer = 0;
		int totalTaskExecutions = 50; 
		while(totalTaskExecutions >0){
			// Read the current clock
			clock_t loopStartClock = std::clock();
			//std::cout<<"In counter llop " <<std::endl;

			// Step 1:  For all the tasks with ready == false, update their ready value
			for (int j = 0; j < nSchedule; j++) {
				//std::cout<<"In Step 1, going to set all ready = false to true";
				//std::cout<<"Task ";
				//std::cout<<j;
				//std::cout<<" ready = ";
				//std::cout<<schedule[j].ready<<std::endl;
				/*Russell, I changed this since the check was missing. Fortunately your comment wa there. 
Again, we have to check the instance. All these checks shouldn't be that important though since 
all we really care about is the how long the task takes to execute right (and that timer does not include any of the checks)?*/			
				string tname = schedule[j].taskName;
				if(schedule[j].ready == false){
                    //spot2
					schedule[j].ready = (*schedule[j].app.*schedule[j].nextrelease)(0, schedule[j].myPeriod);
				}
			}
			
			// Step 2:  Find the task with ready == true that has the highest priority
			highestReadyPriority = -1;
			highestReadyIndex = -1;
			for (int j = 0; j < nSchedule; j++) {
				if (schedule[j].ready && (schedule[j].priority > highestReadyPriority)) {
					highestReadyPriority = schedule[j].priority;
					highestReadyIndex = j;
				}
			}

			// Step 3:  Run the task with highest priority that's ready
			// If there's not such a task, then sleep a bit and loop again
			// It's not the most efficient thing, but who cares?
			//std::cout<<"Highest index =";
			//std::cout<<highestReadyIndex;
			//std::cout<<std::endl;
			if (highestReadyIndex >= 0) {
				//std::cout<<"About to execute task"<<std::endl;
				//std::cout<<"About to build strings"<<std::endl;
				string  tname = schedule[highestReadyIndex].taskName;
				string  appname = schedule[highestReadyIndex].appName;
				//std::cout<<"Built strings, about to concatenate and set map"<<std::endl;
				string fullName = appname+"."+tname;
				// Remember that we just ran this task
				schedule[highestReadyIndex].ready = false;
				
				//std::cout<<"executing now"<<std::endl;
                //spot3
                midStartClockTicks = rdtsc();
                (*schedule[highestReadyIndex].app.*schedule[highestReadyIndex].task)();
                std::cout<<" the task has been executed " << schedule[highestReadyIndex].numExecutions<<std::endl;
                schedule[highestReadyIndex].numExecutions++;
                midFinishClockTicks=rdtsc();
                
                totalTaskExecutions--;
                midElapseClockTicks = midFinishClockTicks - midStartClockTicks;
                timeMap[fullName][i] = midElapseClockNs/CLOCKS_PER_SEC;
				
				
				
	
			} 
            
            else {
				/* Sleep in Ubuntu is in microseconds. How long is appropriate? */
				sleep(400000);
			}
			//counter = counter-1;
			loopTimer = (ranClock - loopStartClock)/(double) CLOCKS_PER_SEC;
			//std::cout<<"Clocks per sec " << CLOCKS_PER_SEC << " ranClock = " <<ranClock << " loopStartClock = " << loopStartClock <<std::endl;
			//std::cout<<" looptimer = " << loopTimer << std::endl;
			i++;
		}
		 finishClock = rdtsc();
		clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &time1);
		std::cout<< " $$$$DIFFCLOCK:$$$$$"<<diff(time1,time2).tv_sec<<":"<<diff(time1,time2).tv_nsec<<endl;
		 myfile<<(finishClock-startClock)/1000<<std::endl;
		totalExec--;
	}
    for(int k = 0; k < nSchedule; k ++){
		std::cout<<" Task "<< schedule[k].appName << " "<< schedule[k].taskName << " executed " << schedule[k].numExecutions << " times" <<std::endl;
	}
	cout << "TN1 = "<< tn1 << " and TN2 = " <<tn2 << std::endl;
	std::map<std::string, std::map<int,long> >::iterator iter;
	std::map<int,long>::iterator insideIter;
	std::string taskName;
	excelOutput<<"Task Name, Average Exe Time, Min Exe Time, Max Exe Time";
	excelOutput<<std::endl;
	for (iter = timeMap.begin(); iter != timeMap.end(); iter++) {
		double totalTime = 0;
		int maxTime = 0;
		int minTime = 5000000;
		int currentTime;
		double averageTime;
		taskName = iter->first;
		excelOutput<<taskName;
		for (insideIter = iter->second.begin(); insideIter != iter->second.end(); insideIter++) {
			currentTime = insideIter->second;
			if(currentTime > maxTime){
				maxTime = currentTime;
			}
			if(currentTime < minTime){
				minTime = currentTime;
			}
			totalTime = totalTime + insideIter->second;
		}
		averageTime = totalTime/timeMap[taskName].size();
		excelOutput<<",";
		excelOutput<<averageTime;
		excelOutput<<",";
		excelOutput<<minTime;
		excelOutput<<",";
		excelOutput<<maxTime;
		excelOutput<<std::endl;
	}
	excelOutput.close();
	myfile.close();
	
}
