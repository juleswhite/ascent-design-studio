python HistoryMaker.py $1 $2 $3 $4 $5 $6
sh makeAll.sh
sh exeAll.sh $7 $8
python VTune-RF.py
sh VTune-Results-Fetcher.sh 
python ResultsFetcher.py