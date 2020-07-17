package net.arin.tp.api.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class contains utility methods that operate on collections.
 */
public class CollectionUtils
{
    private CollectionUtils()
    {
    }

    /**
     * Compares the contents of two collections, regardless of element ordering. The collections are first copied into
     * lists and sorted using the comparator. Then the corresponding elements from each list is compared for equality
     * using the same comparator. If the collection sizes don't match, or comparing two corresponding elements doesn't
     * result in a 0 value, then the collections are not equal.
     *
     * @param c1         a collection
     * @param c2         another collection
     * @param comparator used to sort and compare the elements in the collections
     * @param <T>        the element type
     * @return true if the collections are considered to contain the exact same elements, regardless of order
     */
    public static <T> boolean equalContents( Collection<T> c1, Collection<T> c2, Comparator<T> comparator )
    {
        boolean equal = true;
        if ( c1.size() == c2.size() )
        {
            List<T> list1 = new ArrayList<>( c1 );
            List<T> list2 = new ArrayList<>( c2 );
            Collections.sort( list1, comparator );
            Collections.sort( list2, comparator );
            for ( int i = 0; i < list1.size(); i++ )
            {
                T o1 = list1.get( i );
                if ( comparator.compare( o1, list2.get( i ) ) != 0 )
                {
                    equal = false;
                    break;
                }
            }
        }
        else
        {
            equal = false;
        }
        return equal;
    }

    /**
     * Compares the contents of two collections, regardless of element ordering. The collections are first copied into
     * lists and sorted using the comparator. Then the lists are compared using the {@link List#equals(Object)} method.
     *
     * @param c1  a collection of Comparable objects
     * @param c2  another collection of Comparable objects
     * @param <T> the element type
     * @return true if the collections are considered to contain the exact same elements, regardless of order
     */
    public static <T extends Comparable> boolean equalContents( Collection<T> c1, Collection<T> c2 )
    {
        if ( c1.size() == c2.size() )
        {
            List<T> list1 = new ArrayList<>( c1 );
            List<T> list2 = new ArrayList<>( c2 );
            Collections.sort( list1 );
            Collections.sort( list2 );
            return list1.equals( list2 );
        }
        return false;
    }
}
