package org.ship_monk.data_structures.collections;

import org.junit.jupiter.api.Test;
import org.ship_monk.data_structures.collections.SortedLinkedList;

import static org.junit.jupiter.api.Assertions.*;

class SortedLinkedListTest {

    public static final Integer[] EXPECTED_INTEGER_ARRAY = {1, 2, 3, 5, 10};
    public static final String[] EXPECTED_STRING_ARRAY = {"Items", "Linked", "List", "Sorted", "Test"};

    private static SortedLinkedList<Integer> createSortedLinkedListInteger() {
        final SortedLinkedList<Integer> sortedLinkedList = new SortedLinkedList<>();
        sortedLinkedList.add(5);
        sortedLinkedList.add(3);
        sortedLinkedList.add(10);
        sortedLinkedList.add(1);
        sortedLinkedList.add(2);
        return sortedLinkedList;
    }

    private static SortedLinkedList<String> createSortedLinkedListString() {
        final SortedLinkedList<String> sortedLinkedList = new SortedLinkedList<>();
        sortedLinkedList.add("Sorted");
        sortedLinkedList.add("Linked");
        sortedLinkedList.add("List");
        sortedLinkedList.add("Test");
        sortedLinkedList.add("Items");
        return sortedLinkedList;
    }

    @Test
    void create_SortedLinkedList_Integer() {
        final SortedLinkedList<Integer> sortedLinkedList = createSortedLinkedListInteger();
        assertArrayEquals(EXPECTED_INTEGER_ARRAY, sortedLinkedList.toArray());
    }

    @Test
    void create_SortedLinkedList_String() {
        final SortedLinkedList<String> sortedLinkedList = createSortedLinkedListString();
        assertArrayEquals(EXPECTED_STRING_ARRAY, sortedLinkedList.toArray());
    }

    @Test
    void create_SortedSubList_Integer() {
        final SortedLinkedList<Integer> list = createSortedLinkedListInteger();
        final SortedLinkedList<Integer> subList = (SortedLinkedList<Integer>) list.subList(2, 4);
        assertArrayEquals(EXPECTED_INTEGER_ARRAY, list.toArray());
        assertArrayEquals(new Integer[]{3, 5, 10}, subList.toArray());
    }

    @Test
    void create_SortedSubList_String() {
        final SortedLinkedList<String> list = createSortedLinkedListString();
        final SortedLinkedList<String> subList = (SortedLinkedList<String>) list.subList(0, 1);
        assertArrayEquals(EXPECTED_STRING_ARRAY, list.toArray());
        assertArrayEquals(new String[]{"Items", "Linked"}, subList.toArray());
    }

    @Test
    void delete_SortedLinkedList_String() {
        final SortedLinkedList<String> list = createSortedLinkedListString();
        assertArrayEquals(EXPECTED_STRING_ARRAY, list.toArray());
        list.remove(4);
        assertArrayEquals(new String[]{"Items", "Linked", "List", "Sorted"}, list.toArray());
    }

    @Test
    void delete_SortedLinkedList_Integer() {
        final SortedLinkedList<Integer> list = createSortedLinkedListInteger();
        assertArrayEquals(EXPECTED_INTEGER_ARRAY, list.toArray());
        list.remove(2);
        assertArrayEquals(new Integer[]{1, 2, 5, 10}, list.toArray());
    }
}