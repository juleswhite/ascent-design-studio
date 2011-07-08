import string
import sys
import os
import operator

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
for tnkey in taskNames.keys():
    tnkeys.append(tnkey)
    
print ("$$$")
orderedAgOutput = open("agFile.txt",'w')
keyCount = 0
orderedAgOutput.write ("TRIAL,TASKNAME, AVERAGE EXE TIME, TOTAL TRIAL TIME\n") 
for k in taskNames:
    for time in sorted(taskNames[k], key =str.lower):
        orderedAgOutput.write( time.split(',')[1].split('-')[1]+","+k + ", "+time.split(',')[0]+"," + str(ttdict[time.split(',')[1].split('-')[1]])+"\n")
    orderedAgOutput.write("\n")
    #orderedAgOutput.write(sorted(taskNames[k],key=str.lower)[keyCount].split(',')[1]+","+k + ", "+sorted(taskNames[k],key=str.lower)[keyCount].split(',')[0]+"\n")
orderedAgOutput.close()
