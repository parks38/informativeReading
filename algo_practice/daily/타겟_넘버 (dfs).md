
```java
public class targetController {
    public static int answer;
    // [1,1,1,1] 3, 5
    // [4,1,2,1] 4, 2
    public static void main(String[] args) {
        answer = 0;
        int[] list = new int[]{1,1,1,1,1};
        dfsFindtarget(list, 3, 0, 0); // idx, sum
        System.out.println(answer);
    }

    public static void dfsFindtarget(int[] list, int target, int idx, int sum) {
        if (idx == list.length) {
            if (sum == target) answer++;
        } else {
            dfsFindtarget(list, target, idx + 1, sum + list[idx]);
            dfsFindtarget(list, target, idx+1, sum - list[idx]);
        }
    }
}
```


