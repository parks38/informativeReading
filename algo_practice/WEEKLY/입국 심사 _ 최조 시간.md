

```java
class Solution {
    public long solution(int n, int[] times) {
        Arrays.sort(times);
        long left = 0;
        long right = (long) n * times[times.length - 1];
        
        while (left <= right) {
            long mid = (left + right) / 2;
            long sum = 0;
            
            for (int i = 0; i < times.length; i++) {
                sum += mid / times[i];
            }
            
            if (sum < n) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return left;
    }
}

```

* possible range  = 0 (the minimum possible time) to n * max_time (the maximum possible time, where max_time is the maximum processing time among all the workers).
* binary search by checking the middle point of the range.
	* it calculates the total number of tasks that can be processed by all workers working simultaneously during that time
	* checks whether that number is greater than or equal to the required
* mid 역할  : the minimum time required to process all `n` applicants.

If the total number of processed applicants is less than the required number of `n`, then it means more time is required, so the left pointer is moved to `mid + 1`. If the total number of processed applicants is equal to or greater than `n`, then it means less time is required, so the right pointer is moved to `mid - 1`. The loop continues until `left > right`.


Leetcode 연관 문제 
-   Two Sum
-   Reverse Integer
-   Palindrome Number
-   Longest Common Prefix
-   Merge Two Sorted Lists
-   Remove Duplicates from Sorted Array
-   Remove Element
-   Implement strStr()
-   Maximum Subarray
-   Climbing Stairs