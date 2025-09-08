package thread.volatile1;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class VolatileCountMain {

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread t = new Thread(task, "work");
        t.start();

        sleep(1000);
        task.flag = false;
        log("flag = " + task.flag + ", count = " + task.count + " in main");

    }

    static class MyTask implements Runnable {
        //volatile을 사용한 count값과 아닌 count 값이 차이가 존재.
        boolean flag = true;
        long count;

        //volatile boolean flag = true;
        //volatile long count;

        @Override
        public void run() {
            while (flag) {
                count++;
                //1억번에 한번씩 출력
                if (count % 100_000_000 == 0) {
                    log("flag = " + flag + ", count = " + count + " in while()");
                }
            }
            log("flag = " + flag + ", count = " + count + " 종료");
        }
    }
}

/**
 * 메모리 가시성(Memory Visibility)
 * 멀티스레드 환경에서 한 스레드가 변경한 값이 다른 스레드에서 언제 보이는지에 대한 문제를 의미
 *
 * Java Memory Model(JMM)
 * Java Memory Model(JMM)은 자바 프로그램이 어떻게 메모리에 접근하고 수정할 수 있는지를 규정하며, 특히 멀티
 * 스레드 프로그래밍에서 스레드 간의 상호작용을 정의한다. JMM에 여러가지 내용이 있지만, 핵심은 여러 스레드들의
 * 작업 순서를 보장하는 happens-before 관계에 대한 정의다.
 *
 * happens-before
 * happens-before 관계는 자바 메모리 모델에서 스레드 간의 작업 순서를 정의하는 개념이다. 만약 A 작업이 B 작업보
 * 다 happens-before 관계에 있다면, A 작업에서의 모든 메모리 변경 사항은 B 작업에서 볼 수 있다. 즉, A 작업에서
 * 변경된 내용은 B 작업이 시작되기 전에 모두 메모리에 반영된다.
 *
 * 프로그램 순서 규칙
 * 단일 스레드 내에서, 프로그램의 순서대로 작성된 모든 명령문은 happens-before 순서로 실행된다. 예를 들어, `int
 * a = 1; int b = 2;` 에서 `a = 1` 은 `b = 2` 보다 먼저 실행된다.
 *
 * **volatile 변수 규칙
 * 한 스레드에서 `volatile` 변수에 대한 쓰기 작업은 해당 변수를 읽는 모든 스레드에 보이도록 한다. 즉, `volatile`
 * 변수에 대한 쓰기 작업은 그 변수를 읽는 작업보다 happens-before 관계를 형성한다.
 *
 * 스레드 시작 규칙
 * 한 스레드에서 `Thread.start()` 를 호출하면, 해당 스레드 내의 모든 작업은 `start()` 호출 이후에 실행된 작업보
 * 다 happens-before 관계가 성립한다.
 *
 * 스레드 종료 규칙
 * 한 스레드에서 `Thread.join()` 을 호출하면, join 대상 스레드의 모든 작업은 `join()` 이 반환된 후의 작업보다
 * happens-before 관계를 가진다. 예를 들어, `thread.join()` 호출 후에 `thread` 의 모든 작업이 완료되어야 하며,
 * 이 작업은 `join()` 이 반환된 후에 참조 가능하다. `1 ~ 100` 까지 값을 더하는 `sumTask` 예시를 떠올려보자.
 *
 * 인터럽트 규칙
 * 한 스레드에서 `Thread.interrupt()` 를 호출하는 작업이, 인터럽트된 스레드가 인터럽트를 감지하는 시점의 작업
 * 보다 happens-before 관계가 성립한다. 즉, `interrupt()` 호출 후, 해당 스레드의 인터럽트 상태를 확인하는 작업
 * 이 happens-before 관계에 있다. 만약 이런 규칙이 없다면 인터럽트를 걸어도, 한참 나중에 인터럽트가 발생할 수 있다.
 *
 * 객체 생성 규칙
 * 객체의 생성자는 객체가 완전히 생성된 후에만 다른 스레드에 의해 참조될 수 있도록 보장한다. 즉, 객체의 생성자에서
 * 초기화된 필드는 생성자가 완료된 후 다른 스레드에서 참조될 때 happens-before 관계가 성립한다.
 *
 * 모니터 락 규칙
 * 한 스레드에서 `synchronized` 블록을 종료한 후, 그 모니터 락을 얻는 모든 스레드는 해당 블록 내의 모든 작업을 볼
 * 수 있다. 예를 들어, `synchronized(lock) { ... }` 블록 내에서의 작업은 블록을 나가는 시점에 happens-before
 * 관계가 형성된다. 뿐만 아니라 `ReentrantLock` 과 같이 락을 사용하는 경우에도 happens-before 관계가 성립한다.
 * 참고로 `synchronized` 와 `ReentrantLock` 은 바로 뒤에서 다룬다.
 *
 * 전이 규칙 (Transitivity Rule)
 * 만약 A가 B보다 happens-before 관계에 있고, B가 C보다 happens-before 관계에 있다면, A는 C보다 happens-before
 * 관계에 있다.
 */