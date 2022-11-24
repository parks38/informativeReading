import java.util.Arrays;
/**
  롤케이크 자르기 (Level 2) 
**/
public class RollCake {

    public static void main(String[] args) {

        int answer = 0;

        int[] topping = {1,2,1,3,1,4,1,2};

        /**
         * Arrays.copyOfRange()
         * 배열의 특정 영역을 복사하여 분리
         */
        for (int i = 0; i < topping.length; i++) {
            int[] a = Arrays.copyOfRange(topping, 0, i);
            int[] b = Arrays.copyOfRange(topping, i, topping.length);

            /**
             * 배열에서 중복을 제거 할 수 있는 방법은
             * 1. HashSet 을 이용하여 배열 중복 제거
             *    new HashSet<>(Arrays.asList(arr));
             *    set.toArray(new String[0]) => Set를 String 배열로 변환
             *
             * 2. Stream 이용한 배열 중복 제거
             *    distnct()
             */
            if (Arrays.stream(a).distinct().count() == Arrays.stream(b).distinct().count()) {
                answer++;
            }
        }
        System.out.println(answer);
    }
}






