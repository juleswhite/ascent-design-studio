import string
import sys
import os
import itertools
sys.argv = ['redApp.TaskredApp02();', 0, 2, 6,"TrialRun",  'WorstEx3.cpp'] #Target task, Min history Length, Max History Length, Highest Task Number, OutputFile Name, SourceFile to Reorder
maxTaskNum = sys.argv[3]
directory = os.getcwd()+"/"
sourceName = sys.argv[5]
inputFileName = directory+sourceName
inputfile = open(inputFileName, 'r') 
inputfile1 = open(inputFileName, 'r') 
outputFileString = sys.argv[4]
outputFileTag = 0
linesInFile = []
minLength = sys.argv[1]
maxLength = sys.argv[2]
Apps = ["purpleApp.TaskpurpleApp", "yellowApp.TaskyellowApp","redApp.TaskredApp","blueApp.TaskblueApp","greenApp.TaskgreenApp"]
tasks = []
taskNum = 1
throughTop =False
throughMiddle = False
topContent = []
middleContent = []
bottomContent = ["}\n"]
minHistoryLength = sys.argv[1]
for line in inputfile1.readlines():
    if(line.find("excelOutput-") != -1):
        line = "\texcelOutput.open(\"excelOutput-"+outputFileString+str(outputFileTag)+"\");\n" 
    if(throughTop == False):
        if(line.find("while(i < executions){") != -1):
            print("should be while i < exe ", line)
            throughTop = True
        topContent.append(line)
    else:
        if(throughMiddle == False):
            if(line.find("}") != -1):
                throughMiddle = True
                bottomContent[-1] = "i++; \n"
                bottomContent.append("\t\t }\n")
        else:
            bottomContent.append(line)
        

        
for app in Apps:
    while(taskNum < maxTaskNum):
        if taskNum <10:
            newTask = app+"0"+str(taskNum)+"();"
        else:
            newTask = app+str(taskNum)+"();"
        tasks.append(newTask)
        taskNum = taskNum + 1
    taskNum = 0
    


for line in inputfile.readlines():
    if not line.strip():
        continue
    else:
        line = line.strip(' ')
        line = line.strip("\t")
        linesInFile.append(line)

overlaps = 0
maxWindow = 6
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
    print(task)
    if task in used:
        task ="---***---"
    else:
        used.append(task)

#print("Making history around task %s",argv[0])
historyLength = sys.argv[2]
targetTask = sys.argv[0]
hists = []

if(historyLength >= len(used)):
    print("History length cannot exceed available tasks")
    historyLength = len(used)-1
    
def all_perms(str,historyLength,minHistoryLength):
    if len(str) <=1:
        yield str
    else:
        for perm in all_perms(str[1:],historyLength,minHistoryLength):
            for i in range(len(perm)+1):
                yield perm[:i] + str[0:1] + perm[i:]
              
def powerset(seq):
    """
    Returns all the subsets of this set. This is a generator.
    """
    if len(seq) <= 1:
        yield seq
        yield []
    else:
        for item in powerset(seq[1:]):
            #if(len(item) <= historyLength):
            yield [seq[0]]+item
            yield item

startTaskContent = ["midStartClockTicks = rdtsc();\n\n\t\t\t"]
afterTaskContent = ["\n\n\t\t\tmidFinishClockTicks=rdtsc();\n\n\t\t\t","midElapseClockTicks = midFinishClockTicks - midStartClockTicks;\n\n\t\t\t","midElapseClockNs = ticks2ns(midElapseClockTicks);\n\n\t\t\t" ]
a= used.index(targetTask)
used[0], used[a] = used[a], used[0]
tempTask = []

permuCount = minLength

while permuCount <= maxLength:
    for perm in (itertools.permutations(used[1:],permuCount)):
        perm = list(perm)
        perm.append(targetTask)
        middleContent=["\n\n\t\t\t"]
        for task in perm:
            for i in startTaskContent:
                middleContent.append(i)
                tempTask = []
                tempTask.append(task)
                tempTask.append("\n\n\t\t\t")
                task = ''.join(task)
                middleContent.append(task)
                for i in afterTaskContent:
                    middleContent.append(i)
                task =task.rstrip('();')
                middleContent.append("timeMap[\"%s\"][i] = midElapseClockNs;\n\n\t\t\t"%task)
                concatList = []
                concatList.append(directory)
                concatList.append(outputFileString)
                concatList.append(str(outputFileTag))
                outputFile = open(''.join(concatList)+".cpp", 'w')
            for i in topContent:
                line =i 
                if(i.find("excelOutput-") != -1):
                    line = "\texcelOutput.open(\"excelOutput-"+outputFileString+str(outputFileTag)+"\");\n"
                if(i.find("myfile.open") != -1):
                    line = "\tmyfile.open(\"TotalTimes-"+outputFileString+str(outputFileTag)+"\");\n"
                outputFile.write(line)
            for i in middleContent:
                outputFile.write(i)
            for i in bottomContent:
                outputFile.write(i)
            outputFile.close()
            outputFileTag = outputFileTag+1
              # perm.append(targetTask)
            print(perm)
    permuCount = permuCount +1

def makeMakeFiles(otag):
    tagCount = 0
    outputMakeScript =open(directory+"makeAll.sh",'w')
    outputExeScript = open(directory+"exeAll.sh",'w')
    while(tagCount < otag):
        concatList1 =[]
        concatList1.append(directory)
        concatList1.append("make")
        concatList1.append(outputFileString)
        concatList1.append(str(tagCount))
        concatList2 =[]
        concatList2.append(outputFileString)
        concatList2.append(str(tagCount))
        outputFileName = ''.join(concatList2)
        
        outputFile = open(''.join(concatList1), 'w')
        print("writing to file " + ''.join(concatList1))
        outputFile.write(outputFileName+".exe: "+outputFileName+".o Launcher.o ApplicationredApp.o ApplicationblueApp.o ApplicationyellowApp.o ApplicationgreenApp.o ApplicationpurpleApp.o \n\t\t\t")
        outputFile.write("g++ "+outputFileName+".o Launcher.o ApplicationredApp.o ApplicationblueApp.o ApplicationyellowApp.o ApplicationgreenApp.o ApplicationpurpleApp.o  -o "+outputFileName+"\n\n")
        outputFile.write("ApplicationrblueApp.o: ApplicationblueApp.h\n\t\t\tg++ -c ApplicationblueApp.cpp\n\n")
        outputFile.write("ApplicationredApp.o: ApplicationredApp.h\n\t\t\tg++ -c ApplicationredApp.cpp\n\n")
        outputFile.write("ApplicationpurpleApp.o: ApplicationpurpleApp.h\n\t\t\tg++ -c ApplicationpurpleApp.cpp\n\n")
        outputFile.write("ApplicationyellowApp.o: ApplicationyellowApp.h\n\t\t\tg++ -c ApplicationyellowApp.cpp\n\n")
        outputFile.write("ApplicationgreenApp.o: ApplicationgreenApp.h\n\t\t\tg++ -c ApplicationgreenApp.cpp\n\n")
        outputFile.write(outputFileName+".o: Execute.h\n\t\t\t g++ -c "+outputFileName+".cpp -o " + outputFileName+".o \n\nLauncher.o: Launcher.cpp Execute.h\n\t\t\t g++ -c Launcher.cpp")
        outputFile.close()
        outputExeScript.write("sudo ./" + outputFileName+"\n")
        outputMakeScript.write("make -j2 -f "+''.join(concatList1)+"\n")

        tagCount = tagCount + 1
    outputMakeScript.close()
    outputExeScript.close()
    outputExeScript2 = open(directory+"backExeAll.sh",'w')
    tagCount = tagCount-1
    while(tagCount >0):
        outputFileName = outputFileString+str(tagCount)
        outputExeScript2.write("sudo ./" + outputFileName+"\n")
        tagCount = tagCount -1 
    outputExeScript2.close()
    
    
print(outputFileTag)
makeMakeFiles(outputFileTag)