
#ifndef ALGORITHMS_H
#define ALGORITHMS_H

void insertionSort(int* array, int size);
void bozoSort(int* array, int size);

void bogoSort(int* array, int size);
void shuffle(int* array, int size);

int partition(int* array, int size, int p, int r);
void quicksort(int* array, int size, int p, int r);

void heapSort(int* array, int size, int heap);
void buildMaxHeap(int* array, int size, int heap);
void maxHeapify(int* array, int size, int heap, int loc);
void deleteMax(int* array, int size, int heap);



int* boombogo(int* array);

int quickSelect(int* array);


int randomInt(int low, int high);
int* randomArray(int size, int low, int high);

void printArray(int* array, int size);
int isSorted(int* array, int size);


#endif