package thread.control;

import static util.ThreadUtils.sleep;

/**
 * 체크 예외
 * 부모 메서드가 체크 예외를 던지지 않는 경우, 재정의된 자식 메서드도 체크 예외를 던질 수 없다
 * 자식 메서드는 부모 메서드가 던질 수 있는 체크 예외의 하위 타입만 던질 수 있다.
 * 왜 그렇게 만들었을까?
 * -> 자식 클래스가 더 넓은 범위의 예외를 던지면, 해당 코드는 모든 예외를 제대로 처리하지 못할 수 있다.
 * 이는 예외 처리의 일관성을 해치고, 예상하지 못한 런타임 오류를 초래할 수 있다.
 */
public class CheckedExceptionMain {
    public static void main(String[] args) throws Exception {
        throw new Exception();
    }

    static class CheckedRunnable implements Runnable {

        @Override
        public void run() /*throws Exception*/ { // 주석 풀면 예외 발생
            //throw new Exception(); // 주석 풀면 예외 발생
            sleep(3000); // 이런 식으로 간단하게 사용할 수도 있다.
        }
    }
}

// 그렇다면 왜 Exception을 Runnable에서는 던지지 않을까?
// 예외 발생 시, 예외가 적절히 처리되지 않아서 프로그램이 비정상 종료되는 상황을 방지할 수 있다.
// 특히 멀티스레딩 환경에서는 예외 처리를 강제함으로써 스레드의 안정성과 일관성을 유지할 수 있다.
// 그러나, 최근에는 체크 예외보다는 언체크(런타임) 예외를 선호한다.


