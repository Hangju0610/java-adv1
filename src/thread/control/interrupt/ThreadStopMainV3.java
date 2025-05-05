package thread.control.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV3 {
    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");
        thread.start();

        sleep(50);
        log("작업 중단 지시 thread.interrupt()");
        thread.interrupt();
        log("work 스레드 인터럽트 상태1 = " + thread.isInterrupted());
    }

    static class MyTask implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) { // 인터럽트 상태 변경 X

                log("작업 중");
            }
            // 여기서 확인 시, 인터럽트 상태가 true로 유지되고 있다.
            log("work 스레드 인터럽트 상태2 = " + Thread.currentThread().isInterrupted());

            try {
                log("자원 정리 시도");
                Thread.sleep(1000); // interruptedException 발생, 즉 자원 정리가 안된다.
                log("자원 정리 완료");
            } catch (InterruptedException e) {
                log("자원 정리 실패 - 자원 정리 중 인터럽트 발생");
                log("work 스레드 인터럽트 상태3 = " + Thread.currentThread().isInterrupted());
            }
            log("작업 종료");
        }
    }
}


/**
 * 왜 catch 블록에서 인터럽트 상태가 true에서 false로 바뀌는가?
 * 해당 코드는 큰 문제가 존재한다. 바로 work 스레드의 interrupte 상태가 true로 계속 유지된다.
 * 스레드의 인터럽트 상태를 정상으로 돌리지 않으면 이후에도 계속 인터럽트가 발생하게 된다.
 * 인터럽트의 목적을 달성하면 인터럽트 상태를 다시 정상으로 돌려두어야 한다.
 */