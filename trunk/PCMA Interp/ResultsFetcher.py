import string
import sys
import os
import operator
from operator import itemgetter
sys.argv = ['TrialRun']
directory = os.getcwd()+"/"
dirList=os.listdir(directory)
resultFileNames = []
from collections import defaultdict
taskNames = defaultdict(list)
totalTimeNames =[]
appNames = []

for fname in dirList:
    if(fname.find("excelOutput-") != -1):
        resultFileNames.append(fname)
    if(fname.find("TotalTimes-") != -1):
        totalTimeNames.append(fname)
    if(fname.find("Application") != -1 and fname.find(".cpp") != -1):
        appNames.append(fname)
        
taskSizes = defaultdict(list)
for appName in appNames:
    appFile = open(appName,"r")
    appFileLines = appFile.readlines()
    i = 0
    while(i < len(appFileLines)):
        appline = appFileLines[i]
        #print("looking at line " + appline)
        assignments = 0
        reads = 0
        if(appline.find("void") != -1):
            print(appline)
            firstHalf = appline.split("::")[0]
            secondHalf = appline.split("::")[1]
            taskName = secondHalf.split("(")[0]
            appName = firstHalf.split("Application")[1]
            taskName = appName +"."+taskName
            #print("found task " + taskName)
            i = i + 2
            currentLine = appFileLines[i]
            while( currentLine.find("}") == -1 and i <len(appFileLines)):
                currentLine = appFileLines[i]
                #print(currentLine)
                assignments = assignments + currentLine.count("=")
                if(currentLine.count("+") >0):
                    #print(currentLine)
                    #print(reads)
                    #print("Plus count = " + str(currentLine.count("+")))
                    if(currentLine.count("=") == 0):
                       reads = reads + currentLine.count("+")
                    else:
                        reads = reads + currentLine.count("+") +1
                    #print(reads)
                    
                i = i+1
            taskSizeInfo = []
            taskSizeInfo.append(assignments)
            taskSizeInfo.append(reads)
            taskSizes[taskName] = taskSizeInfo
        i= i+1
print(taskSizes)

for rfn in resultFileNames:
    openedFile = open( rfn, 'r')
    #print("checking file "+ rfn)
    for line in openedFile.readlines():
        if(line.find("Task Name") == -1):
            #print("Task not in line " + line)
            splitLine = line.split(',')
            if((splitLine[0] in taskNames) == False):
               taskNames[splitLine[0]] = []
            #   print(splitLine[0])
            #print(taskNames[splitLine[0]])
            taskNames[splitLine[0]].append(splitLine[1]+","+splitLine[2]+","+splitLine[3].strip()+","+rfn)
           # taskNames[splitLine[0]].append(splitLine[2]+","+rfn)
           # taskNames[splitLine[0]].append(splitLine[3]+","+rfn)
           
print(taskNames)

from collections import defaultdict
masterDict = {}
#for fname in dirList:
#    if(fname.find("excelOutput-") != -1):
#        resultFileNames.append(fname)
##        
##for rfn in resultFileNames:
##    rfnList = defaultdict(list)
##    masterDict[rfn] = rfnList
##print(masterDict)
##    
##for rfn in resultFileNames:
##    openedFile = open( rfn, 'r')
##    #print("checking file "+ rfn)
##    for line in openedFile.readlines():
##        if(line.find("Task Name") == -1):
##            #print("Task not in line " + line)
##            splitLine = line.split(',')
##            if((rfn in masterDict) == False):
##                masterDict
##            if((splitLine[0] in masterDict) == False):
##               masterDict[rfn][splitLine[0]] = []
##               #print(splitLine[0])
##            
##            masterDict[rfn][splitLine[0]].append(splitLine[1])
#print(masterDict)

def comparator(x, y):
    if float(x) > float(y):
        return 1
    elif float(x) == float(y):
        return 0
    else:
        return -1
        
def getTasksFromTrial(filename):
    taskOrder = []
    returnList = []
    opened = open(filename,'r')
    taskString = ""
    for line in opened:
        if (line.find("timeMap[\"") != -1):
           taskOrder.append(line.split("\"")[1])
    for task in taskOrder:
        taskString = taskString +"|"+task
    returnList.append(taskString)
    returnList.append(str(len(taskOrder)))
    return returnList
    

ttdict = {}
for ttn in totalTimeNames:
    count =0
    total = 0
    ttnFile = open(ttn,'r')
    for line in ttnFile.readlines():
        total = total + float(line)
        count = count + 1
    average = total/count
    ttdict[ttn.split('-')[1]] = average

print(ttdict)
print ("$$$")
tnkeys = []
print("ttdict keys = " + str(ttdict.keys()))
for tnkey in taskNames.keys():
    tnkeys.append(tnkey)

def convertItem(x):
    x = x.split(',')
    #print(x)
    return float(x[0])

def getCacheInfo(nameKeys):
    cacheTaskNames = defaultdict(list)
    inputVtune = open(directory+"VTuneResult-TrialRun0-output.txt",'r')
    firstLine = inputVtune.readline()
    firstLines = firstLine.split('|')
    print(firstLines)
    inputVtune.close()
    
    results = defaultdict(list)
    for nk in nameKeys:
        results[nk] = []
        results[nk].append(firstLines)
        inputVtune = open(directory+"VTuneResult-"+nk+"-output.txt",'r')
        lineCount =0
        currentLine = []
        print(" in file " + nk)
        for i in range(0,len(firstLines)-2):
            currentLine.append(0)
        for VtuneLine in inputVtune.readlines():
            if( lineCount ==0):
                lineCount = lineCount+1
            else:
                #print("handling it")
                splitLine = VtuneLine.split('|')
                #currentLine =[]
                #currentLine.append(splitLine[0])
                #currentLine.append(splitLine[1])
                #for i in range(0,len(splitLine)-2):
                #    currentLine.append(0)
                for i in range(2,len(splitLine)):
                    data = splitLine[i]
                    #print("incoming = "+ data + " adding to " +str(currentLine[i-2]))
                    currentLine[i-2]=(float(currentLine[i-2]))+float(data)
               
        results[nk].append(currentLine)
   
    return results

print ("$$$")

def getReads(taskList,taskSize):
    readCount = 0
    taskList = taskList.strip().split("|")
    taskList = taskList[1:]
    print(taskList)
    for task in taskList:
        print(task)
        readCount = taskSize[task][1] + readCount
    return str(readCount)

def getWrites(taskList, taskSize):
    writeCount = 0
    taskList = taskList.strip().split("|")
    taskList = taskList[1:]
    print(taskList)
    for task in taskList:
        print(task)
        writeCount = taskSize[task][0] + writeCount
    return str(writeCount)


orderedAgOutput = open("agFile.txt",'w')
keyCount = 0
cacheInfo = getCacheInfo(ttdict.keys())

topString = "TRIAL, TASKNAME, AVERAGE EXE TIME, MIN EXE TIME, MAX EXE TIME, TASKS, TASK COUNT, READS, WRITES, TOTAL TRIAL TIME"
for cResultTitle in cacheInfo.values():
    avoider = 0
    for crt in cResultTitle[0]:
        if(avoider <2):
            avoider = avoider + 1
        else:
            topString = topString + ", " +crt
    break
topString = topString + "\n"
orderedAgOutput.write (topString)
print(cacheInfo)
for k in taskNames:
    for time in sorted(taskNames[k],  key =convertItem):
        print(" checking if " +time.split(',')[3].split('-')[1]+ " is in cacheInfo")
        trialTitle = time.split(',')[3].split('-')[1]
        if(trialTitle in cacheInfo.keys()):
            cacheInfoString = ","
            count = 0
            for cd in cacheInfo[trialTitle]:
                print(cd)
                
                if(count == 0):
                    count= count + 1
                else:
                    for cacheData in cd:
                        print(cacheData)
                        cacheInfoString = cacheInfoString+str(cacheData) +", "
            cacheInfoString = cacheInfoString + "\n"
            print("Cache info string = " + cacheInfoString)
            orderedAgOutput.write( time.split(',')[3].split('-')[1]+","+k + ", "+time.split(',')[0]+"," +time.split(',')[1] +","+time.split(',')[2] +","+getTasksFromTrial(time.split(',')[3].split('-')[1]+".cpp")[0] +"," +getTasksFromTrial(time.split(',')[3].split('-')[1]+".cpp")[1] +","+ getReads(getTasksFromTrial(time.split(',')[3].split('-')[1]+".cpp")[0],taskSizes)+ "," + getWrites(getTasksFromTrial(time.split(',')[3].split('-')[1]+".cpp")[0],taskSizes)+ "," + str(ttdict[time.split(',')[3].split('-')[1]])+cacheInfoString)
        else:
            orderedAgOutput.write( time.split(',')[3].split('-')[1]+","+k + ", "+time.split(',')[0]+"," +time.split(',')[1] +","+time.split(',')[2]+","+str(len(taskNames)) +"," + str(ttdict[time.split(',')[3].split('-')[1]])+"\n")
    orderedAgOutput.write("\n")
    #orderedAgOutput.write(sorted(taskNames[k],key=str.lower)[keyCount].split(',')[1]+","+k + ", "+sorted(taskNames[k],key=str.lower)[keyCount].split(',')[0]+"\n")
orderedAgOutput.close()
