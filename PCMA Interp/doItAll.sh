echo $6
python HistoryMaker.py $1 $2 $3 $4 $5 $6 
sh makeAll.sh 
sh exeAll.sh Brian-Cache-Profiler $7 $8 $9 
python VTune-RF.py 
sh VTune-Results-Fetcher.sh 
python ResultsFetcher.py 
