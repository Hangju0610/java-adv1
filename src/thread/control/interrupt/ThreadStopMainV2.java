package thread.control.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV2 {
    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");
        thread.start();

        sleep(4000);
        log("작업 중단 지시 thread.interrupt()");
        // work Thread에 InterruptedException 발생
        thread.interrupt();
        log("work 스레드 인터럽트 상태1 = " + thread.isInterrupted());
    }

    static class MyTask implements Runnable {

        @Override
        public void run() {
            try {
                while (true) { // 무한 루프이다.
                    log("작업 중");
                    Thread.sleep(3000);
                }
            } catch (InterruptedException e) {
                log("work 스레드 인터럽트 상태2 = " + Thread.currentThread().isInterrupted());
                log("interrupt message=" + e.getMessage());
                log("state=" + Thread.currentThread().getState());
            }
            log("자원 정리");
            log("작업 종료");
        }
    }
}


/**
 * interrupt() 는 작업 중에서도 thread에 에러를 발생시킨다.
 * 바로 정지된다.
 * true -> False로 먼저 되었다.
 * 즉, main에서 정지 -> true / 동시에 에러 로그를 찍어서 false 반환.
 * 이후, Thread는 실행 가능한 상태, Runnable 상태로 변한다.
 *
 */