import java.util.Arrays;

public class Algs {
	
	public static void insertionsort(int[] arr) {
		for(int i = 1; i < arr.length; i++) {
			for(int j = i; j > 0; j--) {
				if (arr[j] < arr[j-1]) {
					int tmp = arr[j];
					arr[j] = arr[j-1];
					arr[j-1] = tmp;
				} else break;
			}
		}
	}
	
	private static int partition(int[] arr, int p, int r) {
		int pivot = arr[p];
		int i = p; int j = r;
		while(true) {
			while(arr[i] < pivot) i++;
			while(arr[j] > pivot) j--;
			if (i >= j) return j;
			if (arr[i] == arr[j]) { i++; }
			else {
				int tmp = arr[i];
				arr[i] = arr[j];
				arr[j] = tmp;
			}
		}
	}
	
	public static void quicksort(int[] arr, int p, int r) { // possibly working
		if (p < r) {
			int q = partition(arr, p, r);
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
	
	private static boolean checksorted(int[] arr) {
		for(int i = 1; i < arr.length; i++) {
			if (arr[i] < arr[i-1]) return false;
		}
		return true;
	}
	
	public static void bogo(int[] arr) {
		long ctr = 0;
		while(!checksorted(arr)) {
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
		while(!checksorted(arr)) {
			int j = (int) (Math.random() * (arr.length));
			int i = (int) (Math.random() * (arr.length));
			int tmp = arr[j];
			arr[j] = arr[i];
			arr[i] = tmp;
			ctr++;
			if (ctr % 1000 == 0) System.out.println(ctr + " swaps");
		}
	}
	
	public static int select(int[] arr) {
		
		return 0;
	}
	
	public static void main(String[] args) {
		int[] a = new int[14];
		for(int i = 0; i < a.length; i++) {
			a[i] = ((int) (Math.random() * 100)) + 1;
		}
		System.out.println(Arrays.toString(a));
		/*System.out.println(Boolean.toString(checksorted(a)));
		insertionsort(a);*/
		/*System.out.println(Arrays.toString(shuffle(a)));*/
		bogo(a);
		//heapsort(a, a.length);
		//quicksort(a, 0, a.length - 1);
		System.out.println(Arrays.toString(a));
		System.out.println(Boolean.toString(checksorted(a)));
	}
}
