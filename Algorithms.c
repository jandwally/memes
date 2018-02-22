
#include <time.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include "Algorithms.h"

int SIZE = 1000000;
int PRINT_FREQUENCY = 1000000;
int RANDOM_SIZE = 0x10000;

// INSERTION SORT //

void insertionSort(int* array, int size) {
	for (int i = 1; i < size; i++) {
		for (int j = i; j > 0; j--) {
			if (array[j] < array[j-1]) {
				int t = array[j];
				array[j] = array[j-1];
				array[j-1] = t;
			} else break;
		}
	}
}

// BOZOSORT //
void bozoSort(int* array, int size) {
	long c = 0;
	while(!isSorted(array, size)) {
		int j = randomInt(0, size);
		int i = randomInt(0, size);
		int t = array[j];
		array[j] = array[i];
		array[i] = t;
		c++;
		if (c % PRINT_FREQUENCY == 0) {
			printf("%ldk swaps : ", c / 1000);
			printArray(array, size);
		}
	}
	printf("BOZOSORT (%ld) : ", c);
	printArray(array, size);
}


// HEAP SORT //
void heapSort(int* array, int size, int heap) {
	buildMaxHeap(array, size, heap);
	for(int i = heap; i > 0; i--) {
		deleteMax(array, size, i);
	}
}
void buildMaxHeap(int* array, int size, int heap) {
	for (int i = size - 1; i >= 0; i--) {
		maxHeapify(array, size, heap, i);
	}
}
void maxHeapify(int* array, int size, int heap, int loc) {
	int max = loc;
	int ri = 2 * (loc + 1);
	int li = ri - 1;
	if (ri < heap && array[ri] > array[max]) max = ri;
	if (li < heap && array[li] > array[max]) max = li;
	if (max == loc) return;
	int temp = array[max];
	array[max] = array[loc];
	array[loc] = temp;
	maxHeapify(array, size, heap, max);
}
void deleteMax(int* array, int size, int heap) {
	int temp = array[0];
	array[0] = array[heap - 1];
	array[heap - 1] = temp;
	maxHeapify(array, size, heap - 1, 0);
}


// BOGOSORT //
void shuffle(int* array, int size) {
	for(int i = size - 1; i > 0; i--) {
		int j = randomInt(0, i + 1);
		int temp = array[j];
		array[j] = array[i];
		array[i] = temp;
	}
}
void bogoSort(int* array, int size) {
	long c = 0;
	while(!isSorted(array, size)) {
		shuffle(array, size);
		c++;
		if (c % PRINT_FREQUENCY == 0) {
			printf("%ldk shuffles : ", c / 1000);
			printArray(array, size);
		}
	}
	printf("BOGOSORT (%ld) : ", c);
	printArray(array, size);
}


// QUICK SORT //

int partition(int* array, int size, int p, int r) {
	int pivot = array[p];
	int i = p; int j = r;
	while (1) {
		while (array[i] < pivot) i++;
		while (array[j] > pivot) j--;
		if (i >= j) return j;
		if (array[i] == array[j]) { i++; }
		else {
			int tmp = array[i];
			array[i] = array[j];
			array[j] = tmp;
		}
	}
}

void quickSort(int* array, int size, int p, int r) { // possibly working
	if (p < r) {
		int q = partition(array, size, p, r);
		quickSort(array, size, p, q-1);
		quickSort(array, size, q+1, r);
	}
}


// RANDOM GENERATORS //

FILE* urandom = NULL;
int* buffer = NULL;
int currHead = 0;

void initializeRandom() {
	urandom = fopen("/dev/urandom", "r");
	buffer = malloc(sizeof(int) * RANDOM_SIZE);
	fread(buffer, sizeof(int), RANDOM_SIZE, urandom);
	currHead = 0;
}

void finalizeRandom() {
	fclose(urandom);
	urandom = NULL;
	free(buffer);
	buffer = NULL;
}

int readRandomByte() {
	if (currHead > RANDOM_SIZE) {
		fread(buffer, sizeof(int), RANDOM_SIZE, urandom);
		currHead = 0;
	}
	int randomByte = buffer[currHead];
	currHead++;
	return randomByte;
}

int randomInt(int low, int high) {
	if (urandom == NULL) return -1;
	int randomBits;
	randomBits = readRandomByte();

	int range = high - low;
	if (range == 0) return high;
	else {
		int modulo = randomBits % range;
		int random = (modulo < 0 ? -1 * modulo : modulo) + low;
		return random;
	}
}

int* randomArray(int size, int low, int high) {
	int* array = (int*) malloc(sizeof(int) * size);
	for (int i = 0; i < size; i++) {
		array[i] = randomInt(low, high);
	}
	return array;
}


// ARRAY UTILS //

void printArray(int* array, int size) {
	printf("[ ");
	for (int i = 0; i < size; i++) {
		printf("%d ", array[i]);
	}
	printf("]\n");
}

int isSorted(int* array, int size) {
	for (int i = 1; i < size; i++) {
		if (array[i] < array[i-1]) return 0;
	}
	return 1;
}


// MAIN TESTS //

struct timeval tv;
int main() {
	initializeRandom();
	int* array = randomArray(SIZE, 0, 5*SIZE);
	int* array0 = randomArray(100, 0, 100);
	//printArray(array, SIZE);

	gettimeofday(&tv, NULL);
	unsigned long timeStart = 1000000 * tv.tv_sec + tv.tv_usec;

	// insertionSort(array, SIZE);
	// bozoSort(array, SIZE);
	// heapSort(array, SIZE, SIZE);
	// bogoSort(array, SIZE);
	quickSort(array, SIZE, 0, SIZE-1);

	gettimeofday(&tv, NULL);
	unsigned long timeEnd = 1000000 * tv.tv_sec + tv.tv_usec;

	//printArray(array, SIZE);
	printf("TIME: %ld\n", timeEnd - timeStart);
	finalizeRandom();
}








