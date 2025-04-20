package thread.start;

// 실행 결과는 스레드의 실행 순서에 따라 약간 다를 수 있다. 이 부분은 바로 뒤에서 설명한다.
// 자동 정렬 : shift option command L
public class BadThreadMain {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + ": main() start");

        HelloThread helloThread = new HelloThread();
        System.out.println(Thread.currentThread().getName() + ": start() 호출 전");

        helloThread.run(); // run() 직접 실행 -> main 메서드가 직접 실행
        System.out.println(Thread.currentThread().getName() + ": start() 호출 후");

        System.out.println(Thread.currentThread().getName() + ": main() end");
    }
}
