import string
import sys
import os
import operator

from collections import defaultdict
directory = os.getcwd()+"/"

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
                print("handling it")
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

taskNames = ['TrialRun0','TrialRun1','TrialRun1500']
print ("results =" +str(getCacheInfo(taskNames)))
