package SearchEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Sorting {

    /*
     * This method takes as input an HashMap with values that are Comparable.
     * It returns an ArrayList containing all the keys from the map, ordered
     * in descending order based on the values they mapped to.
     *
     * The time complexity for this method is O(n^2) as it uses bubble sort, where n is the number
     * of pairs in the map.
     */
    public static <K, V extends Comparable> ArrayList<K> slowSort ( HashMap<K, V> results ) {
        ArrayList<K> sortedUrls = new ArrayList<K> ();
        sortedUrls.addAll ( results.keySet () );    //Start with unsorted list of urls

        int N = sortedUrls.size ();
        for (int i = 0; i < N - 1; i++) {
            for (int j = 0; j < N - i - 1; j++) {
                if (results.get ( sortedUrls.get ( j ) ).compareTo ( results.get ( sortedUrls.get ( j + 1 ) ) ) < 0) {
                    K temp = sortedUrls.get ( j );
                    sortedUrls.set ( j, sortedUrls.get ( j + 1 ) );
                    sortedUrls.set ( j + 1, temp );
                }
            }
        }
        return sortedUrls;
    }


    /*
     * This method takes as input an HashMap with values that are Comparable.
     * It returns an ArrayList containing all the keys from the map, ordered
     * in descending order based on the values they mapped to.
     *
     * The time complexity for this method is O(n*log(n)), where n is the number
     * of pairs in the map.
     */
    public static <K, V extends Comparable> ArrayList<K> fastSort ( HashMap<K, V> results ) {
        ArrayList<K> sortedUrls = new ArrayList<K> ();
        K[] urls = (K[]) results.keySet ().toArray ();

        mergeSort ( results, urls, 0, urls.length - 1 );
        for (int i = urls.length - 1; i >= 0; i--) {
            sortedUrls.add ( urls[i] );
        }
        return sortedUrls;
    }

    private static <K, V extends Comparable> void mergeSort ( HashMap<K, V> results, K[] urls, int left, int right ) {
        if (left < right) {
            int middle = (left + right) / 2;

            mergeSort ( results, urls, left, middle );
            mergeSort ( results, urls, middle + 1, right );

            merge ( results, urls, left, middle, right );
        }
    }

    private static <K, V extends Comparable> void merge ( HashMap<K, V> results, K[] urls, int left, int middle, int right ) {

        int sizeleft = middle - left + 1;
        int sizeright = right - middle;

        K[] l = (K[]) new Object[sizeleft];
        K[] r = (K[]) new Object[sizeright];

        for (int i = 0; i < sizeleft; ++i)
            l[i] = urls[left + i];
        for (int i = 0; i < sizeright; ++i)
            r[i] = urls[middle + i + 1];

        int n = 0;
        int m = 0;

        int h = left;
        while (n < sizeleft && m < sizeright) {
            if (results.get ( l[n] ).compareTo ( results.get ( r[m] ) ) <= 0) {
                urls[h] = l[n];
                n++;
            } else {
                urls[h] = r[m];
                m++;
            }
            h++;
        }

        while (n < sizeleft) {
            urls[h] = l[n];
            h++;
            n++;
        }

        while (m < sizeright) {
            urls[h] = r[m];
            h++;
            m++;
        }
    }
}