import java.util.HashMap;
import java.util.Map;

class Solution {

    public static void main (String[] args) {
        int[] people = {1,2,3,4,5};
        int[] tshirt = {1,3,3,3};
        System.out.println(solution(people, tshirt));
    }
    public static int solution(int[] people, int[] tshirts) {
        int answer = 0;

        Map<Integer, Integer> shirt = new HashMap<>();

        for (int i = 0; i < tshirts.length; i++) {
            shirt.put(tshirts[i], shirt.getOrDefault(tshirts[i], 0) + 1);
        }

        Map<Integer, Integer> pple = new HashMap<>();
        for (int i = 0; i < people.length; i++) {
            pple.put(people[i], pple.getOrDefault(people[i], 0) + 1);
        }
        int leftOverPPle = 0;
        // 큰 티셔츠 받을수 있음.
        /**
         *  shirt 기준으로
         *  shirt_count == pple_count | leftOverPPle == 0; answer += pple_count;
         *  shirt_count > pple_count | leftOverPPle == 0; answer += pple_count + Math.abs(shirt_count - pple_count-leftOverPple);
         *                             leftOverPpple = Math.abs(leftOverPple - shirt_count - pple_count)
         *  shirt_count < pple_count | leftOverPple += (pple_count - shirt_count); answer += Math.abs(shirt_count - pple_count);
         */
        for (Map.Entry<Integer, Integer> entry : shirt.entrySet()) {

            int s = shirt.get(entry.getKey());
            int p = pple.getOrDefault(entry.getKey(), 0);
            System.out.println("$$$" + leftOverPPle);
             if (s == p) {
                 answer += p;
             } else if (s > p) {
                 int currLeft = leftOverPPle > s ? leftOverPPle - s : leftOverPPle;
                 leftOverPPle -= currLeft;
                 answer += p + currLeft;

             } else {
                 leftOverPPle += (p-s);
                 answer += Math.abs(s - p);
             }

        }

        return answer;
    }

    // runtimeError
//        Map<Integer, Integer> shirt = new HashMap<>();
//
//        for (int i = 0; i < tshirts.length; i++) {
//            shirt.put(tshirts[i], shirt.getOrDefault(tshirts[i], 0) + 1);
//        }
//
//        for (int i = 0; i < people.length; i++) {
//            int count = shirt.getOrDefault(people[i], 0); // size 찾기
//            if (count > 0) {
//                answer++;
//                shirt.put(tshirts[i], count - 1);
//            }
//        }
}
