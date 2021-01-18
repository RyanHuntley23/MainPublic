package byow.Core;
/**
 * Priority queue where objects have a priority that is provided
 * extrinsically, i.e. are are supplied as an argument during insertion
 * and can be changed using the changePriority method.
 * @author Josh Hug (modified by Ryan Huntley)
 */
public interface ExtrinsicMinPQ<T> {
    /** Adds an item with the given priority value. Throws an
     *  IllegalArgumentException if item is already present.
     *  You may assume that item is never null.
     *  @param item the item being added
     *  @param priority the priority to be assigned to item */
    void add(T item, double priority);
    /** @return true if the PQ contains the given item
     *  @param item the item in question */
    boolean contains(T item);
    /** @return the minimum item (throws NoSuchElementException if
     *  the PQ is empty) */
    T getSmallest();
    /** @return and returns the minimum item (throws
     *  NoSuchElementException if the PQ is empty) */
    T removeSmallest();
    /** @return the number of items in the PQ */
    int size();
    /** Changes the priority of the given item. Throws NoSuchElementException
     *  if the item doesn't exist.
     *  @param item the item being modified
     *  @param priority the new priority to be assigned to item */
    void changePriority(T item, double priority);
}
