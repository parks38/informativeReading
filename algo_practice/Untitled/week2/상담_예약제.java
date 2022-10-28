import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * - 문자열로 된 시간을 숫자로 변경하면 비교하기가 쉬워집니다.
 * - 시간이 지나감에 따라 한 명씩 상담을 진행하는 시뮬레이션 방식으로 풀이할 수 있습니다.
 * - 상담시간이 10분이기 때문에 고객이 상담을 하게 된 시간 이후 10분이 다음 확인해야 할 시간이 됩니다.
 */

class 상담_예약제 {

    class Customer {
        final int arrivedAt;
        final String name;

        Customer(String name, String arrivedAt) {
            this.name = name;
            this.arrivedAt = parseTime(arrivedAt);
        }

        /**
         * 시간(String)을 분(Int)으로 변경
         *
         * @param time
         * @return
         */
        private int parseTime(String time) {

            String[] s = time.split(":");
            return Integer.parseInt(s[0]) * 60 + Integer.parseInt(s[1]);
        }
    }

    public String[] solution(String[][] booked, String[][] unbooked) {
        // Customer 객체로 변환
        // Queue 를 사용한 이유는 나머지 남은 상담 고객들을 조금더 편리하게 확인하기 위한 용도 
        Queue<Customer> bookedCustomers = new LinkedList<>();
        for (String[] customer : booked) {
            bookedCustomers.offer(new Customer(customer[1], customer[0]));
        }
        Queue<Customer> unbookedCustomers = new LinkedList<>();
        for (String[] customer : unbooked) {
            unbookedCustomers.offer(new Customer(customer[1], customer[0]));
        }

        List<String> answer = new LinkedList<>();

        // 시간의 시작은 먼저 온 고객의 도착시간
        int currentTime = Math.min(bookedCustomers.peek().arrivedAt, unbookedCustomers.peek().arrivedAt);

        while (!bookedCustomers.isEmpty() || !unbookedCustomers.isEmpty()) {
            // init case 예약 고객 없는 경우 일반 고객 추가하고 종료
            // queue 사용해서 상담 현황을 체크하고 예약자는 상담이 모두 끝난 경우에는 일반 고객들을 상담 
            if (bookedCustomers.isEmpty()) {
                for (Customer c : unbookedCustomers) answer.add(c.name);
                break;
            }
            //init case 일반 고객 없는 경우 예약된 고객 추가하고 종료
            if (unbookedCustomers.isEmpty()) {
                for (Customer c : bookedCustomers) answer.add(c.name);
                break;
            }

            final Customer bookedCustomer = bookedCustomers.peek();
            final Customer unbookedCustomer = unbookedCustomers.peek();

            if (currentTime >= bookedCustomer.arrivedAt) {
                /* 현재 시간에 예약된 고객이거나 이전 고객 상담 중에 예약 시간이 겹친 경우에는 예약 고객을 그 다음으로 받는다. */
                answer.add(bookedCustomers.poll().name);
                currentTime += 10; // 다음 평가시간은 상담이 종료되는 10분 후
            } else if (currentTime >= unbookedCustomer.arrivedAt) {
                /* 현재시간이 예약되지 않은 고객의 도착시간 이후하면 비예약 고객을 바로 상담 */
                answer.add(unbookedCustomers.poll().name);
                currentTime += 10; // 다음 평가시간은 상담이 종료되는 10분 후
            } else {  // 두 시간이 겹치는 경우
                Customer c;
                // 예약/비예약 고객 중 먼저 온 고객을 상담하고
                if (bookedCustomer.arrivedAt < unbookedCustomer.arrivedAt) {
                    c = bookedCustomers.poll();
                } else {
                    c = unbookedCustomers.poll();
                }
                answer.add(c.name);
                // 다음 평가시간은 고객의 상담이 종료되는 도착시간의 10분 후
                currentTime = c.arrivedAt + 10;
            }
        }

        return answer.toArray(String[]::new);
    }
}
