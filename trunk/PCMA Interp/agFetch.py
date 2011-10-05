import string
import sys
import os
import operator


from collections import defaultdict
executionSchedules = defaultdict(list)
filesToOpen = ["TaskredApp02", "TaskredApp01", "TaskblueApp01", "TaskblueApp02", "TaskyellowApp01"] 
executionSchedule= ["blueApp", "Task01", 100000, 100000, 1,1] #app, task, Ti, Di, priority, id
executionSchedules["TaskblueApp01"] = executionSchedule
executionSchedule =["redApp", "Task01", 200000, 200000, 2,2]
executionSchedules["TaskredApp01"] = executionSchedule
executionSchedule =["redApp", "Task01", 300000, 300000, 3,3]
executionSchedules["TaskredApp02"] = executionSchedule
executionSchedule =["blueApp", "Task02", 400000, 400000, 4,4]
executionSchedules["TaskblueApp02"] = executionSchedule
executionSchedule =["yellowApp", "Task01", 500000, 500000, 5,5]
executionSchedules["TaskyellowApp01"] = executionSchedule
validTasks = ["TaskblueApp01", "TaskredApp01", "TaskredApp02", "TaskblueApp02", "TaskyellowApp01"]

ILPFileContent = "" + str(len(validTasks)) +"\n"
for file in filesToOpen:
    agFile = open(file+".txt")
    historyList = []
    for line in agFile.readlines():
        values = line.split(',')
        tasks = values[-1]
        tasks = tasks.split('|')
        tasks = tasks[1:]
        if(len(tasks) > 1):
            tasks[-1]= tasks[-1].strip()
        allTasksValid = 1
        print("previous tasks = " +str(tasks))
        for task in tasks:
            print("seeing if "+ task +" is in "+ str(tasks))
            if(task.strip() not in validTasks):
                allTasksValid = 0
                
        if(allTasksValid == 1 and len(values) > 2 and len(tasks) > 0):
            print("values =" + str(values))
            historyPiece = [values[2], len(tasks)]
            for task in tasks:
                historyPiece.append(executionSchedules[task.strip()][-1])
            historyList.append(historyPiece)
    print( " history list = " +str(historyList))
    ILPFileContent = ILPFileContent + str(executionSchedules[file][-1]) +"\n" + str(executionSchedules[file][-4]) +" "+ str(executionSchedules[file][-3])+"\nperiodic\n"
    ILPFileContent = ILPFileContent + str(len(historyList)) + "\n"
    for history in historyList:
        for item in history:
            ILPFileContent = ILPFileContent + str(item).strip() + " "
        ILPFileContent = ILPFileContent + "\n"
ILPFile = open("ILPFileOutput.txt","w")
ILPFile.write(ILPFileContent)
ILPFile.close()
