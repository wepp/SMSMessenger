package ua.com.qbee.smscrypt;

public class InsertionSort {
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    @SuppressWarnings("rawtypes")
    private static void exch(Comparable[] a, int i, int j) {
        Comparable swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    @SuppressWarnings({"unused", "rawtypes"})
    private static boolean isSorted(Comparable[] a) {
        for (int i = 1; i < a.length; i++)
            if (less(a[i], a[i - 1])) return false;
        return true;
    }

    @SuppressWarnings("rawtypes")
    public static void sort(Comparable[] a) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            for (int j = i; j > 0; j--)
                if (less(a[j], a[j - 1]))
                    exch(a, j, j - 1);
                else break;
        }
    }

    @SuppressWarnings("rawtypes")
    public static void sort(Comparable[] a, int l, int h) {
        for (int i = l; i <= h; i++) {
            for (int j = i; j > 0; j--)
                if (less(a[j], a[j - 1]))
                    exch(a, j, j - 1);
                else break;
        }
    }
}
