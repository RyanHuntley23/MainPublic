package byow.Core;
import java.util.HashMap;
import java.util.NoSuchElementException;

/** Heap implementation of a MinPQ.
 *  @author Ryan Huntley */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    /** Array representation of our complete tree (heap). */
    private HeapNode<T>[] heapArray;
    /** The number of nodes in our heap. */
    private int size;
    /** Map of items to their indices in heapArray. */
    private HashMap<T, Integer> indexMap;

    /** Node containing an item and its priority. */
    private class HeapNode<T> {
        /** Item held within heapNode. */
        private T item;
        /** Priority of heapNode. */
        private double priority;

        /** Constructor.
         *  @param i item held within heapNode
         *  @param p priority of item */
        HeapNode(T i, double p) {
            item = i;
            priority = p;
        }
    }

    /** Constructor. */
    public ArrayHeapMinPQ() {
        heapArray = (HeapNode<T>[]) new HeapNode[16];
        size = 0;
        indexMap = new HashMap<>();
    }

    /** @return the parent index of index k, or -1
     *  if the index is 0
     *  @param k the index of heapArray in question
     *  @source Josh Hug / CS61B lecture 21 slides */
    private int parent(int k) {
        if (k == 0) {
            return -1;
        }
        return (k - 1) / 2;
    }

    /** @return the index of the left child of index k,
     *  or -1 if the index k does not have a child
     *  @param k the index of heapArray in question */
    private int leftChild(int k) {
        int rv = (2 * k) + 1;
        if (rv >= size) {
            return -1;
        }
        return rv;
    }

    /** @return the index of the right child of index k,
     *  or -1 if the index k does not have a child
     *  @param k the index of heapArray in question */
    private int rightChild(int k) {
        int rv = (2 * k) + 2;
        if (rv >= size) {
            return -1;
        }
        return rv;
    }

    /** Moves a node up the PQ until its priority is
     *  greater than its parent.
     *  @param k the index of the node in question */
    private void swimUp(int k) {
        if (k == 0) {
            return;
        }
        int p = parent(k);
        if (heapArray[k].priority < heapArray[p].priority) {
            HeapNode<T> temp = heapArray[k];
            heapArray[k] = heapArray[p];
            heapArray[p] = temp;
            indexMap.put(heapArray[k].item, k);
            indexMap.put(heapArray[p].item, p);
            swimUp(p);
        }
    }

    /** Moves a node down the PQ until its priority is
     *  less than its children.
     *  @param k the index of the node in question */
    private void sinkDown(int k) {
        int lc = leftChild(k);
        int rc = rightChild(k);
        if (rc >= 0 && heapArray[rc].priority <= heapArray[lc].priority) {
            if (heapArray[k].priority > heapArray[rc].priority) {
                HeapNode<T> temp = heapArray[k];
                heapArray[k] = heapArray[rc];
                heapArray[rc] = temp;
                indexMap.put(heapArray[k].item, k);
                indexMap.put(heapArray[rc].item, rc);
                sinkDown(rc);
            }
        } else if (lc >= 0) {
            if (heapArray[k].priority > heapArray[lc].priority) {
                HeapNode<T> temp = heapArray[k];
                heapArray[k] = heapArray[lc];
                heapArray[lc] = temp;
                indexMap.put(heapArray[k].item, k);
                indexMap.put(heapArray[lc].item, lc);
                sinkDown(lc);
            }
        }
    }

    /** Resizes heapArray. Helper function for add and removeSmallest.
     *  @param newLength new size for heapArray */
    private void resize(int newLength) {
        HeapNode<T>[] newArray = (HeapNode<T>[]) new HeapNode[newLength];
        for (int i = 0; i < size; i += 1) {
            newArray[i] = heapArray[i];
        }
        heapArray = newArray;
    }

    /** Adds an item with the given priority value. Throws an
     * IllegalArgumentException if item is already present.
     * @param item the item being added
     * @param priority the priority to be given to item */
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException();
        }
        HeapNode<T> newNode = new HeapNode<>(item, priority);
        if (size >= heapArray.length) {
            resize(heapArray.length * 2);
        }
        heapArray[size] = newNode;
        indexMap.put(item, size);
        size += 1;
        swimUp(size - 1);
    }

    /** @return true if the PQ contains item
     *  @param item the desired item */
    public boolean contains(T item) {
        return indexMap.containsKey(item);
    }

    /** @return the minimum item, throw NoSuchElementException
     *  if the PQ is empty */
    public T getSmallest() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        return heapArray[0].item;
    }

    /** @return the minimum item and remove it from the minPQ,
     *  throw NoSuchElementException if the PQ is empty */
    public T removeSmallest() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        T rv = heapArray[0].item;
        indexMap.remove(rv);
        heapArray[0] = heapArray[size - 1];
        indexMap.put(heapArray[0].item, 0);
        heapArray[size - 1] = null;
        size -= 1;
        sinkDown(0);
        if (size() * 4 < heapArray.length) {
            resize(heapArray.length / 2);
        }
        return rv;
    }

    /** @return the number of items in the PQ */
    public int size() {
        return size;
    }

    /** Changes the priority of the given item. Throws NoSuchElementException
     *  if the item doesn't exist.
     *  @param item the item being given a new priority
     *  @param priority the item's new priority */
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }
        int index = indexMap.get(item);
        heapArray[index].priority = priority;
        swimUp(index);
        sinkDown(index);
    }
}
