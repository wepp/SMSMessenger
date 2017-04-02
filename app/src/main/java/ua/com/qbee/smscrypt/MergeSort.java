package ua.com.qbee.smscrypt;

public class MergeSort {

    private static final int CUTOFF = 7;

    @SuppressWarnings("rawtypes")
    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        assert isSorted(a, lo, mid); // precondition: a[lo..mid] sorted
        assert isSorted(a, mid + 1, hi); // precondition: a[mid+1..hi] sorted
        for (int k = lo; k <= hi; k++)
            aux[k] = a[k];
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > hi) a[k] = aux[i++];
            else if (less(aux[j], aux[i])) a[k] = aux[j++];
            else a[k] = aux[i++];
        }
        assert isSorted(a, lo, hi); // postcondition: a[lo..hi] sorted
    }

    @SuppressWarnings("rawtypes")
    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi) {
        if (hi <= lo) return;
        if (hi <= lo + CUTOFF - 1) InsertionSort.sort(a, lo, hi);
        else {
            int mid = lo + (hi - lo) / 2;
            sort(a, aux, lo, mid);
            sort(a, aux, mid + 1, hi);
            if (!less(a[mid + 1], a[mid])) return;
            merge(a, aux, lo, mid, hi);
        }
    }

    @SuppressWarnings("rawtypes")
    public static void sort(Comparable[] a) {
        Comparable[] aux = new Comparable[a.length];
        sort(a, aux, 0, a.length - 1);
    }

    @SuppressWarnings("rawtypes")
    private static boolean isSorted(Comparable[] a, int l, int m) {
        for (int i = l; i <= m; i++)
            if (less(a[i], a[i - 1])) return false;
        return true;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }
}
