package thread.volatile1;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class VolatileFlagMain {

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread t = new Thread(task, "work");
        log("runFlag = " + task.runFlag);
        t.start();

        sleep(1000);
        log("runFlag를 false로 변경 시도");
        task.runFlag = false;
        log("runFlag = " + task.runFlag);
        log("main 종료");
    }

    static class MyTask implements Runnable {
        //boolean runFlag = true; // runFlag를 volatile로 선언하지 않으면, main 종료만 출력되는 현상.
        volatile boolean runFlag = true;

        @Override
        public void run() {
            log("task 시작");
            while (runFlag) {
                // runFlag가 false로 변하면 탈출
            }
            log("task 종료");
        }
    }
}

/**
 * 메모리 가시성
 * 우리가 생각한 시나리오
 * 1. main 스레드는 runFlag 값을 false로 설정한다.
 * 2. 이때 메인 메모리의 runFlag 값이 false로 변경된다.
 * 3. work 스레드는 while(runFlag)를 실행할 때 runFlag의 데이터를 메인 메모리에서 확인한다.
 * 4. runFlag의 값이 false이므로 while문을 탈출하고, "task 종료"를 출력한다.
 *
 * 실제 시나리오
 * 메인 메모리는 CPU 입장에서 보면 거리도 멀고, 속도도 상대적으로 느리다. 대신에 상대적으로 가격을 저렴해서 큰 용량을 쉽게 구성 가능
 * CPU 연산은 매우 빠르기 때문에, CPU 연산의 빠른 성능을 따라가려면, CPU 캐시 메모리를 사용한다. (레지스터)
 * 현대의 대부분 CPU는 코어 단위로 캐시 메모리를 각각 보유하고 있다.
 *
 * 따라서, 각 스레드가 runFlag 값을 사용하면, runFlag를 캐시 메모리에 불러온다.
 * main 스레드와 work 스레드가 사용하는 runFlag는 서로 다른 캐시 메모리에 존재하게 된다.
 * main 스레드가 runFlag 값을 false로 변경해도, work 스레드가 사용하는 캐시 메모리의 runFlag 값은 여전히 true이다.
 *
 * 그렇다면, 캐시 메모리에 있는 runFlag 값이 언제 메인 메모리에 반영되는가?
 * 그것은 알 수 없다. CPU 설계 방식과 종류에 따라 다르다.
 * 추가적으로 메인 메모리에 반영된다 해도, work 스레드가 캐시 메모리에 있는 runFlag 값을 다시 메인 메모리에서 읽어오는 시점도 알 수 없다.
 *
 * 주로 컨텍스트 스위칭이 될 때, 캐시 메모리도 함께 갱신되는데, 이 부분도 환경에 따라 달라질 수 있다.
 * 예시로 Thread.sleep()이나 콘솔에 내용을 출력할 때 쓰레드가 잠시 쉬는데, 이때 컨텍스트 스위칭이 되면서 주로 갱신되지만, 보장하지는 못한다.
 *
 * 이처럼 멀티스레드 환경에서 한 스레드가 변경한 값이 다른 스레드에서 언제 보이는지에 대한 문제를 메모리 가시성
 * (memory visibility)이라 한다.
 *
 * volatile 키워드
 * volatile 키워드는 해당 변수가 캐시 메모리에 저장되지 않고, 항상 메인 메모리에서 읽고 쓰도록 강제하는 역할을 한다.
 * 따라서, volatile로 선언된 변수는 메모리 가시성 문제가 발생하지 않는다.
 */
