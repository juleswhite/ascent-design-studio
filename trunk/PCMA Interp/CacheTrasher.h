#ifndef CACHETRASHER_H
#define CACHETRASHER_H
#include <iostream>
class CacheTrasher{
      public:
      int cacheFlushSize;
      int cacheLineSize;
      int* cacheFlush;
      int cacheStride;
      void CacheFlusherSetup(int CacheSizeBytes, int CacheLineSizeBytes);
      void CacheFlush();
};
#endif
