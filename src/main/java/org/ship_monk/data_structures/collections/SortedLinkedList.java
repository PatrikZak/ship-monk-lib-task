package org.ship_monk.data_structures.collections;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;


/**
 * Sorted doubly-linked list implementation of the {@link List} and {@link Queue} interfaces.
 * <p><strong>Note that this implementation is not synchronized.</strong>.
 * Iterators work like iterators in {@link LinkedList}.
 *
 * @param <E> the type of elements held in this collection
 */
public class SortedLinkedList<E extends Comparable<E>> extends AbstractList<E> implements Queue<E>, Cloneable, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    transient private Node<E> firstNode;
    transient private Node<E> lastNode;
    transient private int size;

    public SortedLinkedList() {
        this.size = 0;
    }

    /**
     * Appends a non-null element to a list in sorted order.
     *
     * @param element element to be appended to this list
     * @return true (as specified by {@link Collection#add})
     * @throws IllegalArgumentException if element is null
     */
    @Override
    public boolean add(E element) {
        if (element == null) {
           throw new IllegalArgumentException("An element cannot be null.");
        }
        if (firstNode == null) {
            insertFirstNode(element);
            return true;
        }
        if (element.compareTo(firstNode.data) < 0) {
            insertSmallerNode(element);
            return true;
        }
        final Node<E> current = findSortedPosition(element);
        if (current.nextNode == null) {
            insertLastNode(element, current);
            return true;
        }
        insertGreaterOrEqualNode(element, current);
        return true;
    }

    private void insertFirstNode(E element) {
        firstNode = new Node<>(null, element, null);
        lastNode = firstNode;
        size++;
    }

    private void insertSmallerNode(E element) {
        final Node<E> node = firstNode;
        firstNode = new Node<>(null, element, firstNode);
        node.prevNode = firstNode;
        size++;
    }

    private Node<E> findSortedPosition(E element) {
        Node<E> current = firstNode;
        while (current.nextNode != null && element.compareTo(current.nextNode.data) >= 0) {
            current = current.nextNode;
        }
        return current;
    }

    private void insertLastNode(E element, Node<E> current) {
        current.nextNode = new Node<>(current, element, null);
        lastNode = current.nextNode;
        size++;
    }

    private void insertGreaterOrEqualNode(E element, Node<E> current) {
        final Node<E> node = current.nextNode;
        current.nextNode = new Node<>(current, element, current.nextNode);
        node.prevNode = current.nextNode;
        size++;
    }

    @Override
    public boolean addAll(Collection<? extends E> collectionOfElements) {
        if (collectionOfElements.isEmpty()) {
            return false;
        }
        collectionOfElements.forEach(this::add);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offer(E element) {
        return add(element);
    }

    @Override
    public E poll() {
        return (isEmpty()) ? null : removeNode(firstNode);
    }

    /**
     * Retrieves and removes the last element of this queue, or returns null if this queue is empty.
     *
     * @return the last element of this queue, or null if this queue is empty
     */
    public E pollLast() {
        return (isEmpty()) ? null : removeNode(lastNode);
    }

    @Override
    public E element() {
        checkEmptyList();
        return firstNode.data;
    }

    /**
     * Retrieves, but does not remove, the last element of this queue.
     * This method differs from {@link SortedLinkedList#peekLast()} only in that it throws an exception if this queue is empty.
     *
     * @return the last element of the queue
     */
    public E elementLast() {
        checkEmptyList();
        return lastNode.data;
    }

    @Override
    public E peek() {
        return (isEmpty()) ? null : firstNode.data;
    }

    /**
     * Retrieves, but does not remove, the last element of this queue, or returns null if this queue is empty
     *
     * @return the last element of this queue, or null if this queue is empty
     */
    public E peekLast() {
        return (isEmpty()) ? null : lastNode.data;
    }

    @Override
    public E get(int index) {
        return getNode(index).data;
    }

    private Node<E> getNode(int index) {
        checkElementIndex(index);
        Node<E> result = firstNode;
        for (int i = 1; i <= index; i++) {
            result = result.nextNode;
        }
        return result;
    }

    private boolean isNotElementIndex(int index) {
        return index < 0 || index >= size();
    }

    private void checkEmptyList() {
        if (isEmpty()) {
            throw new NoSuchElementException("Sorted linked list is empty.");
        }
    }

    private void checkElementIndex(int index) {
        if (isNotElementIndex(index)) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }
    }

    @Override
    public E remove() {
        checkEmptyList();
        return removeNode(firstNode);
    }

    @Override
    public E remove(int index) {
        return removeNode(getNode(index));
    }

    private E removeNode(Node<E> nodeToRemove) {
        final Node<E> prevNode = nodeToRemove.prevNode;
        final Node<E> nextNode = nodeToRemove.nextNode;
        Optional.ofNullable(prevNode).ifPresentOrElse(
                presentPrevNode -> {
                    presentPrevNode.nextNode = nextNode;
                    nodeToRemove.prevNode = null;
                },
                () -> firstNode = nextNode
        );
        Optional.ofNullable(nextNode).ifPresentOrElse(
                presentNextNode -> {
                    presentNextNode.prevNode = prevNode;
                    nodeToRemove.nextNode = null;
                },
                () -> lastNode = prevNode
        );
        final E element = nodeToRemove.data;
        nodeToRemove.data = null;
        size--;
        modCount++;
        return element;
    }

    @Override
    public Iterator<E> iterator() {
        return new IteratorImpl();
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        checkElementIndex(index);
        return new ListIteratorImpl(index);
    }

    /**
     * Provides a new instance of {@link SortedLinkedList} for input index ranges.
     * It is NOT a view.
     *
     * @param fromIndex low endpoint (inclusive) of the subList
     * @param toIndex   high endpoint (exclusive) of the subList
     * @return new instance of sublist {@link SortedLinkedList}
     * @throws IndexOutOfBoundsException if an endpoint index value is out of range (fromIndex < 0 || toIndex > size)
     * @throws IllegalArgumentException  if the endpoint indices are out of order (fromIndex > toIndex)
     */
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        subListRangeCheck(fromIndex, toIndex, size());

        final SortedLinkedList<E> subList = new SortedLinkedList<>();
        Node<E> currentNode = getNode(fromIndex);
        for (int index = fromIndex; index <= toIndex; index++) {
            subList.add(currentNode.data);
            currentNode = currentNode.nextNode;
        }
        return subList;
    }

    private void subListRangeCheck(int fromIndex, int toIndex, int size) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        if (toIndex > size)
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                    ") > toIndex(" + toIndex + ")");
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Returns a shallow copy of this {@link  SortedLinkedList}. (The elements
     * themselves are not cloned.)
     *
     * @return a shallow copy of this {@link  SortedLinkedList} instance
     */
    @SuppressWarnings("unchecked")
    @Override
    public SortedLinkedList<E> clone() {
        try {
            SortedLinkedList<E> clone = (SortedLinkedList<E>) super.clone();

            clone.firstNode = null;
            clone.size = 0;
            clone.modCount = 0;

            for (Node<E> node = firstNode; node != null; node = node.nextNode) {
                clone.add(node.data);
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    private static class Node<E> {
        private E data;
        private Node<E> nextNode;
        private Node<E> prevNode;

        public Node(Node<E> prevNode, E data, Node<E> nextNode) {
            this.prevNode = prevNode;
            this.data = data;
            this.nextNode = nextNode;
        }
    }

    private class IteratorImpl implements Iterator<E> {
        /**
         * Index of element to be returned by subsequent call to next.
         */
        int cursor = 0;

        /**
         * Index of element returned by most recent call to next or
         * previous.  Reset to -1 if this element is deleted by a call
         * to remove.
         */
        int lastRet = -1;

        /**
         * The modCount value that the iterator believes that the backing
         * List should have.  If this expectation is violated, the iterator
         * has detected concurrent modification.
         */
        int expectedModCount = modCount;

        public boolean hasNext() {
            return cursor != size();
        }

        public E next() {
            checkForComodification();
            try {
                int index = cursor;
                E next = get(index);
                lastRet = index;
                cursor = index + 1;
                return next;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException(e);
            }
        }

        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            checkForComodification();

            try {
                SortedLinkedList.this.remove(lastRet);
                if (lastRet < cursor) {
                    cursor--;
                }
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    private class ListIteratorImpl extends IteratorImpl implements ListIterator<E> {
        ListIteratorImpl(int index) {
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public E previous() {
            checkForComodification();
            try {
                int i = cursor - 1;
                E previous = get(i);
                lastRet = cursor = i;
                return previous;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException(e);
            }
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        public void set(E e) {
            this.add(e);
        }

        public void add(E e) {
            checkForComodification();
            try {
                SortedLinkedList.this.add(e);
                expectedModCount = modCount;
            }catch (IllegalArgumentException ex) {
                throw new ConcurrentModificationException(ex);
            }
        }
    }
}