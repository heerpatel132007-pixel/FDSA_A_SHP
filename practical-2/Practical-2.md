**Practical-2**

Problem-1

1. Recursive search does not stop.

     Ans  Add the base condition: `if (index == n) return -1;`.

2. License plate comparison fails.

    Ans Use `==` to compare strings and ensure there are no typing mistakes.

Problem-2

1. Program returned the wrong position.

     Ans Update `low` and `high` correctly based on the comparison with `arr[mid]`.

2. "Not Found" displayed even when the element exists.

    Ans  Verify that the input array is sorted and the target value is entered correctly. 