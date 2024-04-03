package org.diffvanilla.crews.utilities;

import java.util.*;
import java.util.Map.Entry;

public class GeneralUtilities {
	 public static <K, V> Map<K, V> sortByComparator(Map<K, V> unsortMap, final boolean order)
	    {

        List<Entry<K, V>> list = new LinkedList<Entry<K, V>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<K, V>>()
        {
            @SuppressWarnings("unchecked")
			public int compare(Entry<K, V> o1,
                    Entry<K, V> o2)
            {
                if (order)
                {
                    return ((Comparable<V>) ((Map.Entry<K, V>) (o1)).getValue()).compareTo(((Map.Entry<K, V>) (o2)).getValue());
                }
                else
                {
                    return ((Comparable<V>) ((Map.Entry<K, V>) (o2)).getValue()).compareTo(((Map.Entry<K, V>) (o1)).getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<K, V> sortedMap = new LinkedHashMap<K, V>();
        for (Entry<K, V> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
   }

    public static boolean isNumeric(String input) {
        if (input == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(input);
            if (d <= 0 || d != (int) d) {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
