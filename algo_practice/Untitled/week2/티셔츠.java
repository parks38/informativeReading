class 티셔츠 {
    public static void main (String[] args) {
        int[https://github.com/parks38/swe-skills/blob/main/algo_practice/Untitled/week2/%ED%8B%B0%EC%85%94%EC%B8%A0.java] p = {1, 2, 3, 4, 5};
        int[] ts = {1, 1, 2, 4};

        System.out.println(solution(p,ts));
    }

    public static int solution(int[] people, int[] tshirts) {
        // people, tshirts 배열은 정렬되어 있다는 조건이 없다는 것에 주의하세요
        Arrays.sort(people);
        Arrays.sort(tshirts);

        int answer = 0;

        /**
         * 참가자가 자신보다 큰 티셔츠를 선택 가능하기 때문에
         * 큰 티셔츠 부터 나눠주는 것으로 설계
         */
        int i = tshirts.length - 1;
        for (int j = people.length - 1; j >= 0 && i >= 0; j--) {
            /**
             * 마지막에 작은 티셔츠 크기들만 남게 됨.
             * ex.   4 * 2 1 1
             *     5 4 3 2 1
             *     * O * O O *
             */
            if (tshirts[i] >= people[j]) {
                answer += 1;
                i -= 1;
            }
        }

        return answer;
    }
}
