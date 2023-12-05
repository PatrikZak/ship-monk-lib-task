# ShipMonk library

## Task
“Implement a library providing SortedLinkedList
(linked list that keeps values sorted). It should be
able to hold string or int values, but not both. Try to
think about what you'd expect from such library as a
user in terms of usability and best practices, and
apply those.”

## Implemantation notes
I designed the SortedLinked list as a basic Java collection using basic Java interfaces.
The core is abstract class **AbstractList**. Sort LinkedList has been taken over by Iterator
implementation with minor modifications. It would be good if the **pollLast()** method and more
similar methods would be moved to their own interface (for example SortedDeque). It would be good to spend
more time thinking about it. Since the task only required storing simple Integer and String values,
I used the Comparable interface. I would use the Comparator interface for more flexibility.

I was thinking about the **add()** method and had an idea about binary insertion/getting/removing. It might be a good idea
to try changing the insertion method according to the size of the list. If (for example) there were less than
100 elements, it would be classical insertion with linear complexity. If there were more than 100 elements
(depends on measurement), the method would be binary insertion with linear-logarithmic complexity
(There would be more iterations, but fewer comparisons).
However, it would be necessary to make a measurement using a performance tool.

I wrote short Javadoc just for public non-override methods as example.

I have implemented some tests (just as an example). More scenarios should be implemented and include failure states.

Thanks for your time.