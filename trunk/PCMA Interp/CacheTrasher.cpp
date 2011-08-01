#include <iostream>
#include "CacheTrasher.h"

void CacheTrasher::CacheFlusherSetup(int CacheSizeBytes, int CacheLineSizeBytes)
{
      cacheLineSize = CacheLineSizeBytes;
      cacheFlushSize = (CacheSizeBytes * 3) / sizeof(int);
      cacheFlush = (int *)malloc(cacheFlushSize);
      if (cacheFlush == NULL) {
            int x = 4;
	    std::cout << "Malloc failed" << std::endl;
      }
      cacheStride = CacheLineSizeBytes / sizeof(int);
}
 
void CacheTrasher::CacheFlush()
{
      int x;
      for (int i = 0; i < cacheFlushSize/cacheLineSize; i = i + cacheStride) {
            x = cacheFlush[i];
      }
}
