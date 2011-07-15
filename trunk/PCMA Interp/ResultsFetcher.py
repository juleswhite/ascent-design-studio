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
for fname in dirList:
    if(fname.find("excelOutput-") != -1):
        resultFileNames.append(fname)
    if(fname.find("TotalTimes-") != -1):
        totalTimeNames.append(fname)
        
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
            taskNames[splitLine[0]].append(splitLine[1]+","+rfn)
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
orderedAgOutput = open("agFile.txt",'w')
keyCount = 0
cacheInfo = getCacheInfo(ttdict.keys())
topString = "TRIAL,TASKNAME, AVERAGE EXE TIME, TASK COUNT, TOTAL TRIAL TIME"
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
        print(" checking if " +time.split(',')[1].split('-')[1]+ " is in cacheInfo")
        trialTitle = time.split(',')[1].split('-')[1]
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
            orderedAgOutput.write( time.split(',')[1].split('-')[1]+","+k + ", "+time.split(',')[0]+"," + str(len(taskNames)) +"," + str(ttdict[time.split(',')[1].split('-')[1]])+cacheInfoString)
        else:
            orderedAgOutput.write( time.split(',')[1].split('-')[1]+","+k + ", "+time.split(',')[0]+"," + str(len(taskNames)) +"," + str(ttdict[time.split(',')[1].split('-')[1]])+"\n")
    orderedAgOutput.write("\n")
    #orderedAgOutput.write(sorted(taskNames[k],key=str.lower)[keyCount].split(',')[1]+","+k + ", "+sorted(taskNames[k],key=str.lower)[keyCount].split(',')[0]+"\n")
orderedAgOutput.close()
