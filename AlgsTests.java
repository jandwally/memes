// testing module for sorts mainly
public class AlgsTests {
	public static boolean checksorted(int[] arr) {
		for(int i = 1; i < arr.length; i++) {
			if (arr[i] < arr[i-1]) return false;
		}
		return true;
	}
	
	public static int[] genarray(int n, int k) {
		int[] arr = new int[n];
		for(int i = 0; i < n; i++) {
			arr[i] = ((int) (Math.random() * k)) + 1;
		}
		return arr;
	}
	
	public static boolean insertiontest(int n, int k, int m) {
		// m tests on array of size n
		for (int i = 0; i < m; i++) {
			int[] a = genarray(n, k);
			Algs.insertionsort(a, 0, n - 1);
			boolean b = checksorted(a);
			if (!b) return b;
		}
		return true;
	}
	
	public static boolean quicktest(int n, int k, int m) {
		// m tests on array of size n
		for (int i = 0; i < m; i++) {
			int[] a = genarray(n, k);
			Algs.quicksort(a, 0, n - 1);
			boolean b = checksorted(a);
			if (!b) return b;
		}
		return true;
	}
	
	public static void main(String[] args) {
		int n = 1000000; int k = n * 5; int m = 100;
		long startTime = System.nanoTime();
		// start testing module
		//boolean b = insertiontest(n, k, m);
		boolean b = quicktest(n, k, m);
		// end testing module
		long endTime   = System.nanoTime();
		long totalTime = (endTime - startTime)/1000000;
		//System.out.println("insertion sort tested");
		System.out.println("quicksort tested");
		System.out.println("number of tests  : " + m);
		System.out.println("on arrays of size: " + n);
		System.out.println("with elements in : [1," + k + "]");
		System.out.println("tests all passed : " + b);
		System.out.println("total runtime of : " + totalTime + " ms");
	}
}