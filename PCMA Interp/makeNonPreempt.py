import string
import sys
import os
import itertools
#sys.argv = ['redApp.TaskredApp02();', 0, 0, 6,"TrialRun",  'WorstEx3.cpp'] #Target task, Min history Length, Max History Length, Highest Task Number, OutputFile Name, SourceFile to Reorder
maxTaskNum = int(sys.argv[4])
directory = os.getcwd()+"/"
sourceName = sys.argv[6]
#print(sys.argv)
dirList=os.listdir(directory)
print (" directory files = " + str(dirList))
inputFileName = directory+sourceName
inputfile = open(inputFileName, 'r') 
inputfile1 = open(inputFileName, 'r') 
outputFileString = sys.argv[5]
outputFileTag = 0
linesInFile = []
minLength = int(sys.argv[2])
maxLength = int(sys.argv[3])
Apps = ["purpleApp.TaskpurpleApp", "yellowApp.TaskyellowApp","redApp.TaskredApp","blueApp.TaskblueApp","greenApp.TaskgreenApp"]
tasks = []
taskNum = 1
throughTop =False
throughMiddle = False
topContent = []
middleContent = []
bottomContent = ["}\n"]
minHistoryLength = sys.argv[1]
sleepFunction = "\nvoid sleep(int time)\n{\n\tusleep(time);\n}\n\n"
scheduleEntryStruct = "\ntypedef struct {\n\t int priority;\n\tbool ready;\n\tbool (ApplicationpurpleApp::*nextrelease)(int,int);\n\t void (ApplicationpurpleApp::*task)(int);\n\t char* taskName;\n\tchar* appName;\n\tint myPeriod;\n} ScheduleEntry; \n\n"
timespecDiff = "timespec diff(timespec start, timespec end)\n{\n\ttimespec temp;\n\tif ((end.tv_nsec-start.tv_nsec)<0) {\n\t\ttemp.tv_sec = end.tv_sec-start.tv_sec-1;\n\t\ttemp.tv_nsec = 1000000000+end.tv_nsec-start.tv_nsec;\n\t} else{\n\t\t temp.tv_sec = end.tv_sec-start.tv_sec;\n\t\ttemp.tv_nsec = end.tv_nsec-start.tv_nsec;\n\t}\n\treturn temp;\n}\n\n"
timespecInit = "\ntimespec time1, time2, time3;\n\tint temp;\n\tclock_gettime(CLOCK_PROCESS_CPUTIME_ID, &time1);\n\tstd::cout<< \" startClock = \"<< time1.tv_sec << std::endl;"

        
appCpps =[]
appHs =[]

for dirFile in dirList:
    if(len(dirFile.split('.')) > 1):
        if(dirFile.split('.')[1].strip() == "h" and dirFile.split('.')[0].strip().find("Application")>-1):
            appHs.append(dirFile)
        elif(dirFile.split('.')[1].strip() == "cpp" and dirFile.split('.')[0].strip().find("Application")>-1):
            appCpps.append(dirFile)

print("appHs = " + str(appHs) + " appCpps = " + str(appCpps))
for app in Apps:
    print("examining app " + app)
    while(taskNum < maxTaskNum):
        if taskNum <10:
            newTask = app+"0"+str(taskNum)+"();"
        else:
            newTask = app+str(taskNum)+"();"
        tasks.append(newTask)
        taskNum = taskNum + 1
        #print("tasknum = " + str(taskNum))
    taskNum = 0

print ("got apps")


for line in inputfile.readlines():
    if not line.strip():
        continue
    else:
        line = line.strip(' ')
        line = line.strip("\t")
        linesInFile.append(line)
print("got lines")
overlaps = 0
maxWindow = sys.argv[2]
maybe =tasks[0].split('.')[0]
print ("Maybe !" + maybe)
window = -1

tasksInFile =[]
for line in linesInFile:
    for task in tasks:
        #print(task)
        if task in line:
            tasksInFile.append(task)

used = []
for i in range(len(tasksInFile)) :
    task = tasksInFile[i]
    #print(task)
    if task in used:
        task ="---***---"
    else:
        used.append(task)
        
tasknames = []
for task in used:
    
    tasknames.append(task.split('.')[1])
for appH in appHs:
    tempFile = open("temp-apph",'w')
    appColor = appH.split("Application")[1].split("App")[0]
    print (" appcolor = " + appColor)
    appHFile = open(appH, 'r')
    for line in appHFile.readlines():
        if(line.find("Task"+appColor) != -1):
            line = line.split("(")[0]+"();\n\tbool Task"+line.split("(")[0].split("App")[1]+"NextRelease(int,int);\n\t"
            
        tempFile.write(line)
    tempFile.close()
    appHfile = open(appH,'w')
    tempFile = open("temp-apph",'r')
    for line in tempFile.readlines():
        #print("writing " + line)
        appHfile.write(line)
    appHfile.close()

for appCpp in appCpps:
    tempFile = open("temp-appcpp",'w')
    appColor = appCpp.split("Application")[1].split("App")[0]
    print (" appcolor = " + appColor)
    appCppFile = open(appCpp, 'r')
    for line in appCppFile.readlines():
        if(line.find("#include <iostream>") != -1):
            line = line + "#include <sys/time.h>\n#include <cstdio>\n#include <ctime>\n#include <sched.h>\nusing namespace std;\n\n"
            for task in tasknames:
                if(task.find(appColor) != -1):
                    line = line +"bool " + appCpp.split('.')[0] + "::Task"+task.split("App")[1].split("(")[0]+"NextRelease( int Clock, int myPeriod)\n{\n"
                    line = line +"timespec time1;\n\t"
                    line = line +"clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &time1);\n\t"
                    line = line +"static int nextRelease = time1.tv_nsec;\n\t"
                    line = line +"int temp;\n\t"
                    line = line +"clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &time1);\n\t"
                    line = line +"if (time1.tv_nsec >= nextRelease || Clock == -85) {\n\t\t"
                    line = line +"nextRelease = nextRelease + myPeriod;\n\t\t"
                    line = line +"std::cout<<\"Ready = true, nextRelease now =\" << nextRelease<<std::endl;\n\t\t"
                    line = line +"return true;\n\t"
                    line = line +"} else {\n\t\t"
                    line = line +"return false;\n\t"
                    line = line +"}\n\t"
                    line = line +"std::cout<<\"nextRelease = \" << nextRelease<<std::endl;\n}\n\n"
        tempFile.write(line)
    tempFile.close()
    appCppfile = open(appCpp,'w')
    tempFile = open("temp-appcpp",'r')
    for line in tempFile.readlines():
        #print("writing " + line)
        appCppfile.write(line)
    appCppfile.close()

#At this point we've converted the Apps and AppCpps appropriately. Now we just have to do the scheduler and the make file.

def getSchedule():
    return "{1, false, tnr"+tasknames[0].split('(')[0]+"ptr, tpa" + tasknames[0].split('(')[0]+"ptr, \"" + tasknames[0].split('(')[0]+"\", \""+application +"\", 200000}\n\t"

ILPSchedBones = open("ILPSchedBones.cpp", "r")
ILPSched = open("ILPSched.cpp", "w")

for line in ILPSchedBones:
    if(line.find("Execute.h") != -1):
        for appH in appHs:
            line = line +"#include \""+ appH + "\"\n"
    if(line.find("Where the priorities are met")!=-1):
        line = line +"\t"
        doneApps =[]
        for task in tasknames:
            application = "Application"+ task.split('Task')[1].split('App')[0] #+ "App::"
            
            line2 = application+"App "+  application.split('Application')[1] +  "App;\n\t"
            if(line2 not in doneApps):
                line = line+line2
                doneApps.append(line2)
            application = application + "App::"
            line = line + "void ("+application+"*tpa"+task.split('(')[0]+"ptr)()=NULL;\n\t"
            line = line + "bool ("+application+"*tnr"+task.split('(')[0]+"ptr)(int,int)=NULL;\n\t"
            #print(" application = " + application + " and task split = " + task.split('(')[0])
            line = line + "tpa"+task.split('(')[0]+"ptr = &"+application+task.split('(')[0]+";\n\t"
            line = line + "tnr"+task.split('(')[0]+"ptr = &"+application+"Task"+task.split('(')[0].split('App')[1]+"NextRelease;\n\t"
    if(line.find("ScheduleEntry schedule[] = {") != -1):
        line = line + getSchedule()
    if(line.find("//spot1") != -1):
        first = 1
        application = "Application"+ task.split('Task')[1].split('App')[0] #+ "App::"
        doneApps = []
        for appH in appHs:
            application = appH.split('.')[0].split('Application')[1]
            print("application =" + application)
           # line = line + "\t\t\t"
            for task in tasknames:                
                if(task.find(application) !=-1 and (application not in doneApps)):
                    #application = "Application"+ task.split('Task')[1].split('App')[0] + "App"
                    if(first == 1):
                        line = line + "\t\t\tif(tname.compare(\""+application+"\")==1){\n\t\t\t\t"
                        first =0 
                    else:
                        line = line + "\telse if(tname.compare(\""+application+"\")==1){\n\t\t\t\t"
                    line = line + "schedule[j].ready = ("+application+".*schedule[j].nextrelease)(-85, schedule[j].myPeriod);\n\t\t\t}\n\n\t\t"
                    doneApps.append(application)
    if(line.find("//spot2") != -1):
        first = 1
        application = "Application"+ task.split('Task')[1].split('App')[0] #+ "App::"
        doneApps = []
        for appH in appHs:
            application = appH.split('.')[0].split('Application')[1]
            print("application =" + application)
           # line = line + "\t\t\t"
            for task in tasknames:                
                if(task.find(application) !=-1 and (application not in doneApps)):
                    #application = "Application"+ task.split('Task')[1].split('App')[0] + "App"
                    if(first == 1):
                        line = line + "\t\t\t\t\tif(tname.compare(\""+application+"\")==1){\n\t\t\t\t\t\t"
                        first =0 
                    else:
                        line = line + "\t\telse if(tname.compare(\""+application+"\")==1){\n\t\t\t\t\t\t"
                    line = line + "schedule[j].ready = ("+application+".*schedule[j].nextrelease)(0, schedule[j].myPeriod);\n\t\t\t\t\t}\n\n\t\t\t\t"
                    doneApps.append(application)
       # for task in tasknames:
       #     application = "Application"+ task.split('Task')[1].split('App')[0] + "App::"
       #     line = line + "if(tname.compare(\"" + application + "\") == 1){\n\t\t\t"
       #     line = line + "schedule[j].ready = (" + application.split("Application")[1] + ".*schedule[j].nextrelease)(0, schedule[j].myPeriod);\n\t\t\t\t}\n\t\t"
    if(line.find("//spot3") != -1):
        first = 1
        application = "Application"+ task.split('Task')[1].split('App')[0] #+ "App::"
        doneApps = []
        for appH in appHs:
            application = appH.split('.')[0].split('Application')[1]
            print("application =" + application)
            line = line + "\t\t"
            for task in tasknames:                
                if(task.find(application) !=-1 and (application not in doneApps)):
                    #application = "Application"+ task.split('Task')[1].split('App')[0] + "App"
                    if(first == 1):
                        
                        line = line + "\t\tif(appname.compare(\""+application + "\") == 0{\n\t\t\t\t"
                        first =0 
                    else:
                        line = line + "else if(appname.compare(\""+application + "\") == 0{\n\t\t\t\t\t"
                    line = line + "schedule[j].ready = ("+application+".*schedule[j].nextrelease)(0, schedule[j].myPeriod);\n\t\t\t\t\t"
                    line = line + "midStartClockTicks = rdtsc();\n\t\t\t\t\t"
                    line = line + "(." + application+"*schedule[highestReadyIndex].task)(schedule[highestReadyIndex].myPeriod);\n\t\t\t\t\tmidFinishClockTicks=rdtsc();\n\n\t\t\t\t\t"
                    line = line + "totalTaskExecutions--;\n\t\t\t\t\tmidElapseClockTicks = midFinishClockTicks - midStartClockTicks;\n\t\t\t\t\ttimeMap[fullName][i] = midElapseClockNs/CLOCKS_PER_SEC;\n\t\t\t\t}\n\t\t"
                    doneApps.append(application)
            #application = "Application"+ task.split('Task')[1].split('App')[0] + "App::"
            #line = line + "if(appname.compare(\""+application + "\") == 0{\n\t\t\t"
            #line = line + "midStartClockTicks = rdtsc();\n\t\t\t"
            #line = line + "(" + application.split('Application')[1]+".*schedule[highestReadyIndex].task)(schedule[highestReadyIndex].myPeriod);\n\t\t\tmidFinishClockTicks=rdtsc();\n\n\t\t\t"
            #line = line + "totalTaskExecutions--;\n\t\tmidElapseClockTicks = midFinishClockTicks - midStartClockTicks;\n\t\ttimeMap[fullName][i] = midElapseClockNs/CLOCKS_PER_SEC;\n\t\t}\n\t\t"
        
    ILPSched.write(line)

makeILPSched = open("makeILPSched", 'w')
line1 = "ILPSched.exe: ILPSched.o Launcher.o"
line2 = "\t\t\tg++ -c -lrt ILPSched.o Launcher.o"
for appH in appHs:
    line1 = line1 + " " + appH.split('.')[0]+".o"
    line2 = line2 + " " + appH.split('.')[0]+".o"
makeILPSched.write(line1+"\n")
makeILPSched.write(line2+"\n\n")

appLine = ""
for appH in appHs:
    appLine = appH.split('.')[0]+".o:" + appH +"\n\t\t\tg++ -c " + appH.split('.')[0]+".cpp\n\n"
    makeILPSched.write(appLine)

makeILPSched.write("ILPSched.o: Execute.h\n\t\t\tg++ -c ILPSched.cpp -o ILPSched.o\n\n")
makeILPSched.write("Launcher.o: Launcher.cpp Execute.h\n\t\t\tg++ -c Launcher.cpp")



    
#print("Making history around task %s",argv[0])
historyLength = sys.argv[3]
targetTask = sys.argv[1]
hists = []






##
##for line in inputfile1.readlines():
##    if(line.find("using namespace") != -1):
##        line = line + sleepFunction + ScheduleEntry + timespecDiff
##    if(throughTop == False):
##        if(line.find("while(i < executions){") != -1):
##            # print("should be while i < exe ", line)
##            topContent.append(line)
##            topContent.append("\t\t\tif(innerTrash == 1){\n\t\t\t\tc.CacheFlush();\n\t\t\t}\n")
##        if(throughTop == False):
##            topContent.append(line)
##    else:
##        if(throughMiddle == False):
##            if(line.find("}") != -1):
##                throughMiddle = True
##                bottomContent[-1] = "i++; \n"
##                bottomContent.append("\t\t }\n")
##        else:
##            bottomContent.append(line)
##if(historyLength >= len(used)):
##    print("History length cannot exceed available tasks")
##    historyLength = len(used)-1
##    
##def all_perms(str,historyLength,minHistoryLength):
##    if len(str) <=1:
##        yield str
##    else:
##        for perm in all_perms(str[1:],historyLength,minHistoryLength):
##            for i in range(len(perm)+1):
##                yield perm[:i] + str[0:1] + perm[i:]
##              
##def powerset(seq):
##    """
##    Returns all the subsets of this set. This is a generator.
##    """
##    if len(seq) <= 1:
##        yield seq
##        yield []
##    else:
##        for item in powerset(seq[1:]):
##            #if(len(item) <= historyLength):
##            yield [seq[0]]+item
##            yield item
##
##startTaskContent = ["midStartClockTicks = rdtsc();\n\n\t\t\t"]
##afterTaskContent = ["\n\n\t\t\tmidFinishClockTicks=rdtsc();\n\n\t\t\t","midElapseClockTicks = midFinishClockTicks - midStartClockTicks;\n\n\t\t\t","midElapseClockNs = ticks2ns(midElapseClockTicks);\n\n\t\t\t" ]
##a= used.index(targetTask)
##used[0], used[a] = used[a], used[0]
##tempTask = []
##
##permuCount = minLength
##
##while permuCount <= maxLength:
##    for perm in (itertools.permutations(used[1:],permuCount)):
##        perm = list(perm)
##        perm.append(targetTask)
##        middleContent=["\n\n\t\t\t"]
##        for task in perm:
##            for i in startTaskContent:
##		if(task.find(targetTask)!= -1):
##	        	middleContent.append("midStartClockTicks = rdtsc();\n\n\t\t\t")
##	        tempTask = []
##	        tempTask.append(task)
##		if(task.find(targetTask)==-1):
##	        	tempTask.append("\n\n\t\t\t")
##	        task2 = ''.join(tempTask)
##	        middleContent.append(task2)
##		#print("task ="+ task+ " targetTask = " + targetTask)
##		#print(task.find(targetTask))
##		#if(task.find(targetTask) == 0):
##		#print("task ="+ task+ " targetTask = " + targetTask)
##		if(task.find(targetTask)!= -1):
##			for i in afterTaskContent:
##			    middleContent.append(i)
##	        task2 =task.rstrip('();')
##		if(task.find(targetTask)!= -1):
##        		middleContent.append("timeMap[\"%s\"][i] = midElapseClockNs;\n\n\t\t\t"%task2)
##	    	concatList = []
##	    	concatList.append(directory)
##	    	concatList.append(outputFileString)
##	    	concatList.append(str(outputFileTag))
##	    outputFile = open(''.join(concatList)+".cpp", 'w')
##	    #print(middleContent)
##            for i in topContent:
##                line =i 
##                if(i.find("excelOutput-") != -1):
##                    line = "\texcelOutput.open(\"excelOutput-"+outputFileString+str(outputFileTag)+"\");\n"
##                if(i.find("myfile.open") != -1):
##                    line = "\tmyfile.open(\"TotalTimes-"+outputFileString+str(outputFileTag)+"\");\n"
##                outputFile.write(line)
##            for i in middleContent:
##                outputFile.write(i)
##            for i in bottomContent:
##                outputFile.write(i)
##            outputFile.close()
##            outputFileTag = outputFileTag+1
##              # perm.append(targetTask)
##            print(perm)
##    permuCount = permuCount +1
##
##def makeMakeFiles(otag):
##    tagCount = 0
##    outputMakeScript =open(directory+"makeAll.sh",'w')
##    outputExeScript = open(directory+"exeAll.sh",'w')
##    while(tagCount < otag):
##        concatList1 =[]
##        concatList1.append(directory)
##        concatList1.append("make")
##        concatList1.append(outputFileString)
##        concatList1.append(str(tagCount))
##        concatList2 =[]
##        concatList2.append(outputFileString)
##        concatList2.append(str(tagCount))
##        outputFileName = ''.join(concatList2)
##        
##        outputFile = open(''.join(concatList1), 'w')
##        print("writing to file " + ''.join(concatList1))
##        outputFile.write(outputFileName+".exe: CacheTrasher.o "+outputFileName+".o Launcher.o ApplicationredApp.o ApplicationblueApp.o ApplicationyellowApp.o ApplicationgreenApp.o ApplicationpurpleApp.o \n\t\t\t")
##        outputFile.write("g++ "+outputFileName+".o Launcher.o ApplicationredApp.o ApplicationblueApp.o ApplicationyellowApp.o ApplicationgreenApp.o ApplicationpurpleApp.o CacheTrasher.o -o "+outputFileName+"\n\n")
##        outputFile.write("ApplicationrblueApp.o: ApplicationblueApp.h\n\t\t\tg++ -c ApplicationblueApp.cpp\n\n")
##        outputFile.write("ApplicationredApp.o: ApplicationredApp.h\n\t\t\tg++ -c ApplicationredApp.cpp\n\n")
##        outputFile.write("ApplicationpurpleApp.o: ApplicationpurpleApp.h\n\t\t\tg++ -c ApplicationpurpleApp.cpp\n\n")
##        outputFile.write("ApplicationyellowApp.o: ApplicationyellowApp.h\n\t\t\tg++ -c ApplicationyellowApp.cpp\n\n")
##        outputFile.write("ApplicationgreenApp.o: ApplicationgreenApp.h\n\t\t\tg++ -c ApplicationgreenApp.cpp\n\n")
##        outputFile.write(outputFileName+".o: Execute.h\n\t\t\t g++ -c "+outputFileName+".cpp -o " + outputFileName+".o \n\nLauncher.o: Launcher.cpp Execute.h\n\t\t\t g++ -c Launcher.cpp \n\nCacheTrasher.o: CacheTrasher.h\n\t\t\t g++ -c CacheTrasher.cpp")
##        outputFile.close()
##        outputExeScript.write("sudo amplxe-cl -collect $1 -result-dir  VTuneResult-"+outputFileName+" "+directory+"/"+outputFileName +" $2\n")
##        outputMakeScript.write("make -j2 -f "+''.join(concatList1)+"\n")
##
##        tagCount = tagCount + 1
##    outputMakeScript.close()
##    outputExeScript.close()
##    outputExeScript2 = open(directory+"backExeAll.sh",'w')
##    tagCount = tagCount-1
##    while(tagCount >0):
##        outputFileName = outputFileString+str(tagCount)
##        outputExeScript2.write("sudo ./" + outputFileName+"\n")
##        tagCount = tagCount -1 
##    outputExeScript2.close()
##    
##    
##print(outputFileTag)
##makeMakeFiles(outputFileTag)
