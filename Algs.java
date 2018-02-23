import java.util.Arrays;

public class Algs {
	
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
	
	public static void heapsort(int[] arr, int n) {
		buildmaxheap(arr, n);
		for(int i = n; i > 0; i--) {
			delmax(arr, i);
		}
	}
	
	public static void buildmaxheap(int[] arr, int n) {
		for(int i = arr.length - 1; i > -1; i--) {
			maxheapify(arr, n, i);
		}
	}
	
	public static void maxheapify(int[] arr, int n, int i) {
		int max = i;
		int ri = 2 * (i + 1);
		int li = ri - 1;
		if (ri < n && arr[ri] > arr[max]) max = ri;
		if (li < n && arr[li] > arr[max]) max = li;
		if (max == i) return;
		int tmp = arr[max];
		arr[max] = arr[i];
		arr[i] = tmp;
		maxheapify(arr, n, max);
	}
	
	public static void delmax(int[] arr, int n) {
		int tmp = arr[0];
		arr[0] = arr[n-1];
		arr[n-1] = tmp;
		maxheapify(arr, n-1, 0);
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
	
	public static void main(String[] args) {
		int n = 1000000;
		int[] a = AlgsTests.genarray(n, n * 5);
		//int[] a = {13, 49, 35, 6, 24, 11, 48, 41, 38, 18};
		//System.out.println(Arrays.toString(a));
		long startTime = System.nanoTime();
		//System.out.println(Boolean.toString(checksorted(a)));
		//insertionsort(a, 0, a.length - 1);
		/*System.out.println(Arrays.toString(shuffle(a)));*/
		//bogo(a);
		//heapsort(a, a.length);
		//quicksort(a, 0, a.length - 1);
		//partition(a, 24, 0, a.length - 1);
		int k = (int) (Math.random() * n / 2) + n / 4;
		int elm = select(a, k, 0, n-1);
		System.out.println("selected element " + elm + " of rank " + k);
		quicksort(a, 0, a.length - 1);
		System.out.println("correct rank printed: " + Boolean.toString(a[k - 1] == elm));
		long endTime   = System.nanoTime();
		long totalTime = (endTime - startTime) / 1000000;
		//System.out.println(Arrays.toString(a));
		System.out.println(Boolean.toString(AlgsTests.checksorted(a)));
		System.out.println(totalTime + " ms runtime");
	}
}