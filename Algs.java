import java.util.Arrays;

public class Algs {
	
	// slightly optimized
	public static void bubblesort(int[] arr, int p, int r) {
		for(int i = r + 1; i > p; i--) {
			int ctr = 0;
			for(int j = p + 1; j < i; j++) {
				if (arr[j] < arr[j-1]) {
					int tmp = arr[j];
					arr[j] = arr[j-1];
					arr[j-1] = tmp;
					ctr++;
				}
			}
			if (ctr == 0) return;
		}
	}
	
	public static void insertionsort(int[] arr, int p, int r) {
		for(int i = p + 1; i < r + 1; i++) {
			for(int j = i; j > p; j--) {
				if (arr[j] < arr[j-1]) {
					int tmp = arr[j];
					arr[j] = arr[j-1];
					arr[j-1] = tmp;
				} else break;
			}
		}
	}
	
	public static void selectionsort(int[] arr, int p, int r) {
		for(int i = 0; i < r - p; i++) {
			int idx = p + i;
			for(int j = p + i; j < r + 1; j++) {
				if (arr[j] < arr[idx])  idx = j; 
			}
			int tmp = arr[p+i];
			arr[p+i] = arr[idx];
			arr[idx] = tmp;
		}
	}
	
	// selection from both directions
	public static void breadsort(int[] arr, int p, int r) {
		// unimplemented
	}
	
	// unoptimized gnome sort
	public static void keemsort(int[] arr, int p, int r) {
		int ptr = p + 1;
		while(ptr < r + 1) {
			if (ptr == p || arr[ptr] >= arr[ptr-1]) ptr++;
			else {
				int tmp = arr[ptr];
				arr[ptr] = arr[ptr-1];
				arr[ptr-1] = tmp;
				ptr--;
			}
		}
	}
	
	private static int[] merge(int[] a1, int[] a2) {
		int[] arr = new int[a1.length + a2.length];
		int p1 = 0; int p2 = 0; int i = 0;
		while (p1 < a1.length && p2 < a2.length) {
			if (a1[p1] < a2[p2]) { arr[i] = a1[p1]; p1++; i++; }
			else { arr[i] = a2[p2]; p2++; i++; }
		}
		while (p1 < a1.length) { arr[i] = a1[p1]; p1++; i++; }
		while (p2 < a2.length) { arr[i] = a2[p2]; p2++; i++; }
		return arr;
	}
	
	public static int[] mergesort(int[] arr, int p, int r) {
		if (r > p) {
			int[] a1 = mergesort(arr, p, p + (r - p) / 2);
			int[] a2 = mergesort(arr, p + 1 + (r - p) / 2, r);
			return merge(a1, a2);
		}
		int[] out = {arr[p]};
		return out;
	}
	
	private static int partition(int[] arr, int piv, int p, int r) { // working meme
		int q = -1;							// pivot has to be in the array
		int i = p - 1; int j = 0;
		while (i + j < r) {
			int curr = arr[i + j + 1];
			if (curr < piv) {
				i++; 
				int tmp = arr[i];
				arr[i] = arr[i+j];
				arr[i+j] = tmp;
				if(tmp == piv) q = i + j;
			} else {
				j++;
				if (curr == piv) q = i + j;
			}
		}
		int tmp = arr[q];
		arr[q] = arr[i + 1];
		arr[i + 1] = tmp;
		return i + 1;
	} // if there are multiple copies of pivot, likely one will end up on the > side
	
	public static void quicksort(int[] arr, int p, int r) { // works
		if (p < r) {
			int q = partition(arr, arr[r], p, r);
			quicksort(arr, p, q-1);
			quicksort(arr, q+1, r);
		}
	}
	
	public static void heapsort(int[] arr, int p, int r) {
		buildmaxheap(arr, p, r);
		for(int i = r; i > p - 1; i--) {
			delmax(arr, p, i);
		}
	}
	
	public static void buildmaxheap(int[] arr, int p, int r) {
		for(int i = r; i > p - 1; i--) {
			maxheapify(arr, p, r, i);
		}
	}
	
	public static void maxheapify(int[] arr, int p, int r, int i) {
		int max = i;
		int ri = 2 * (i + 1 - p) + p;
		int li = ri - 1;
		if (ri < r + 1 && arr[ri] > arr[max]) max = ri;
		if (li < r + 1 && arr[li] > arr[max]) max = li;
		if (max == i) return;
		int tmp = arr[max];
		arr[max] = arr[i];
		arr[i] = tmp;
		maxheapify(arr, p, r, max);
	}
	
	public static void delmax(int[] arr, int p, int r) {
		int tmp = arr[p];
		arr[p] = arr[r];
		arr[r] = tmp;
		maxheapify(arr, p, r - 1, p);
	}
	
	public static void shuffle(int[] arr) {
		for(int i = arr.length - 1; i > 0; i--) {
			int j = (int) (Math.random() * (i + 1));
			int tmp = arr[j];
			arr[j] = arr[i];
			arr[i] = tmp;
		}
	}
	
	public static void bogo(int[] arr) {
		long ctr = 0;
		while(!AlgsTests.checksorted(arr)) {
			shuffle(arr);
			ctr++;
			if (ctr % 1000 == 0) System.out.println(ctr + " shuffles");
		}
	}
	
	public static int[] boombogo(int[] arr) {
		// unimplemented
		return arr;
	}
	
	public static void bozo(int[] arr) {
		long ctr = 0;
		while(!AlgsTests.checksorted(arr)) {
			int j = (int) (Math.random() * (arr.length));
			int i = (int) (Math.random() * (arr.length));
			int tmp = arr[j];
			arr[j] = arr[i];
			arr[i] = tmp;
			ctr++;
			if (ctr % 1000 == 0) System.out.println(ctr + " swaps");
		}
	}
	
	public static int select(int[] arr, int i, int p, int r) {
		int length = r - p + 1;								// length of sub-array
		if (length == 1) return arr[p];						// base case
		int n = length / 5;									// size of "full" groups
		int s = (length % 5 == 0) ? n : n + 1;				// size including leftover group
		int[] meds = new int[s];							// array of medians
		for(int j = 0; j < s; j++) {						// finding med of each group
			int idxi = j * 5 + p;
			int idxf = Math.min(idxi + 4, r);
			insertionsort(arr, idxi, idxf);
			meds[j] = arr[idxi + (idxf - idxi) / 2];
		}
		int med = select(meds, (meds.length + 1) / 2, 0, meds.length - 1);
		int q = partition(arr, med, p, r);
		int k = q - p + 1;
		if (k == i) return med;
		else if (k < i) return select(arr, i - k, q + 1, r);
		return select(arr, i, p, q - 1);
	} // works now
	
	private static int[] radpart(int[] arr, int p, int r, int bit) {
		if (bit > 31) return arr;
		int ptr = 0;
		int[] ret = new int[arr.length];
		for(int i = p; i < r + 1; i++) {
			if(((arr[i] >>> bit) & 1) == 0) { ret[ptr] = arr[i]; ptr++; }
		}
		for(int i = p; i < r + 1; i++) {
			if(((arr[i] >>> bit) & 1) == 1) { ret[ptr] = arr[i]; ptr++; }
		}
		return radpart(ret, p, r, bit + 1);
	} // not very fast ~2n
	
	public static int[] radixsort(int[] arr, int p, int r) {
		return radpart(arr, p, r, 0); // only works for positives
	} // might write a legit one later
	
	public static void main(String[] args) {
		int n = 100000;
		int[] a = AlgsTests.genarray(n, n * 5);
		//System.out.println(Arrays.toString(a));
		long startTime = System.nanoTime();
		//insertionsort(a, 0, a.length - 1);
		/*System.out.println(Arrays.toString(shuffle(a)));*/
		//bogo(a);
		//heapsort(a, 0, a.length - 1);
		//quicksort(a, 0, a.length - 1);
		//partition(a, 24, 0, a.length - 1);
		//selectionsort(a, 0, a.length / 2);
		//keemsort(a, 0, a.length - 1);
		//a = mergesort(a, 0, a.length - 1);
		//a = radixsort(a, 0, a.length - 1);
		bubblesort(a, 0, a.length - 1);
		/*int k = (int) (Math.random() * n / 2) + n / 4;
		int elm = select(a, k, 0, n-1);
		System.out.println("selected element " + elm + " of rank " + k);
		quicksort(a, 0, a.length - 1);
		System.out.println("correct rank printed: " + Boolean.toString(a[k - 1] == elm));*/
		long endTime   = System.nanoTime();
		long totalTime = (endTime - startTime) / 1000000;
		//System.out.println(Arrays.toString(a));
		System.out.println("array is sorted: " + Boolean.toString(AlgsTests.checksorted(a)));
		System.out.println(totalTime + " ms runtime");
	}
}