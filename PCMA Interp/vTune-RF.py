import string
import sys
import os
import operator
directory = os.getcwd()+"/"
dirList=os.listdir(directory)
resultDirs = []
for dir in dirList:
    print(dir)
    if(dir.find('VTuneResult-')!=-1):
        resultDirs.append(dir)

#print( "resultDirs = " + str(resultDirs))

outputVtune = open('VTune-Results-Fetcher.sh','w')
for rd in resultDirs:
    trial = rd.split('-')[1]
    trial = trial.split('l')[0]+"lRun"+trial.split('l')[1]
    print(trial)
    print("about to write file")
    outputVtune.write('sudo amplxe-cl -report hw-events -r '+rd+' -csv-delimiter \"|\" > \"VTuneResult-'+trial+'-output.txt\"\n') 
    print(" File written")
outputVtune.close()
