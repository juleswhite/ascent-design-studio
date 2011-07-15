import string
import sys
import os
import itertools
directory = os.getcwd()+"/"
counter = 1
while(counter < 1000000):
    outputfile = open ("exeAll"+str(counter)+".sh", 'w')
    inputfile = open ("exeAll.sh","r")
    for line in inputfile.readlines():
       # print(line.split)
        line = line.rstrip()+" "+ str(counter) +"\n"
        outputfile.write(line)
    outputfile.close()
    counter = counter*10
    
