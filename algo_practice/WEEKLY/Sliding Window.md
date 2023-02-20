
```java
import java.util.ArrayList;  
import java.util.Arrays;  
import java.util.List;  
  
/**  
 * find all Anagram in String * s = "cbaebabacd" * p = "abc" * [0,6] * * - sliding window (two pointer) * - Hastable/ char[26] .charAt(i)) - 'a' */public class Solution1 {  
  
    public static void main (String[] args) {  
        System.out.println(Arrays.toString(getAllAnagram("cbaebabacd", "abc")));  
        System.out.println(Arrays.toString(getAllAnagram("casas", "s")));  
        System.out.println(Arrays.toString(getAllAnagram("casas", "b")));  
        System.out.println(Arrays.toString(getAllAnagram("aabadecba", "abc")));  
    }  
  
    public static int[] getAllAnagram (String s, String p) {  
        int index = 0;  
        List<Integer> answer = new ArrayList<>();  
        while (index + p.length() <= s.length()) {  
            int[] char1 = new int[26];  
            int[] char2 = new int[26];  
            for (int i = index; i < index + p.length(); i++) {  
                char1[s.charAt(i) - 'a']++;  
                char2[p.charAt(i - index) - 'a']++; // p 는 항상 0 부터 마지막 까지 탐색  
            }  
            // compare  
            if (compareTwoArray(char1, char2)) answer.add(index);  
            index++;  
        }  
        return answer.stream().mapToInt(i->i).toArray();  
    }  
  
    /**  
     * Optimized version of the solution     * @param s  
     * @param p  
     * @return  
     */  
    public static int[] betterAnswer (String s, String p) {  
        int[] char1 = new int[26];  
        int[] char2 = new int[26];  
        // p는 무조건 substring 이기 때문에 먼저 charSet 구하기  
        for (char c : p.toCharArray()) {  
            char2[c - 'a']++;  
        }  
  
        List<Integer> answer = new ArrayList<>();  
        for (int i = 0; i < s.length(); i++) {  
            char1[s.charAt(i) - 'a']++;  
  
            // window slider 으로 i++ 하는데 p.length() 안의 범위가 아니라면  
            // 그 이전의 값은 뺌으로 확인! don't have to reiterate the array  
            if (i >= p.length()) {  
                char1[s.charAt(i - p.length()) - 'a']--;  
            }  
            if (Arrays.equals(char1, char2)) {  
                answer.add(i - p.length() + 1);  
            }  
        }  
      return answer.stream().mapToInt(i -> i).toArray();  
    }  
  
    public static boolean compareTwoArray (int[] char1, int[] char2) {  
        for (int i = 0; i < char1.length; i++) {  
            if (char1[i] != char2[i]) return false;  
        }  
        return true;  
    }  
}
```


* chat GPT response  
*  
* Code Structure and Readability:  
* The code is well-structured with a clear separation of concerns - the main function  
* calls the 'getAllAnagram' function which in turn calls the 'compareTwoArray' function.  
*  
* The naming conventions used in the code are appropriate and self-explanatory.  
*  
* The code is easy to read and understand.  
*  
* Functionality:  
* The 'getAllAnagram' function seems to be working fine for the given problem statement.  
* The 'compareTwoArray' function compares two arrays of characters to check if they are the same.  
*  
* However, it could be improved by returning early if any character count doesn't match.  
* The given test cases seem to cover most of the scenarios, but some edge cases (like empty strings, large input strings, etc.)  
* could be added to test the code's efficiency.  
*  
* Optimization:  
* The code can be optimized by using a sliding window approach instead of iterating through the entire string for each substring.  
* This can significantly reduce the number of iterations and improve the code's performance.  
* Here is an updated code with the optimization:

```java
/**  
 * subarray product less than k * nums = [10,5,2,6]; * k = 100 * output : 8 */public class Solution2 {  
    public static void main(String[] args) {  
        int[] nums = {10,5,2,6};  
        System.out.println(getSubArrayProductLessThanK(nums, 100));  
    }  
  
    public static int getSubArrayProductLessThanK (int[] nums, int k) {  
        int start = 0;  
        int end = 0;  
        int sum = 1;  
        int count = 0;  
  
        while (end < nums.length) {  
            sum *= nums[end];  
            System.out.println("****");  
            System.out.println(start + " " + end + " " + sum);  
  
            while (sum >= k) {  
                System.out.println("sum:" + sum + " start:" + start);  
                sum /= nums[start];  
                start++;  
            }  
  
            // end - start -1 (sum 구한 값 까지 몇개 있는지 + 1은 한자리까지 count)            
            // [10, 5, 2, 6]            
             // 5 * 2  = 10            
             // 5 * 2 * 6 = 60            
             // (3-1) + 1            
             count += end - start + 1;  
            System.out.println("count:" + count);  
            end++;  
            System.out.println("end:" + end);  
            System.out.println("-0----------");  
        }  
  
        return count;  
    }  
}
```


> subarray minimum length sum 

```java
/**  
 * s = 7, nums = [2,3,1,2,4,3] => output : 2 * minimum size subarray sum 
 * 
 * /
 public class Solution2_20_3 {  
    public static void main(String[] args) {  
        System.out.println(solution(7, new int[]{2,3,1,2,4,3}));  
    }  
  
    public static int solution (int s, int[] nums) {  
        int count = 9999999;  
  
        int start = 0;  
        int end = 1;  
        int sum = 0;  
  
        while (end < nums.length) {  
            sum += nums[end];  
           // if (sum == s) count = Math.min(count, (end - start));  
            while (sum >= s) {  
                count = Math.min(count, (end - start));  
                sum -= nums[start];  
                start++;  
            }  
            end++;  
        }  
  
        return count;  
    }  
}
```
