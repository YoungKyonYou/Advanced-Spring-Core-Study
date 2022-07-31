package hello.advanced.trace.threadlocal;

import hello.advanced.trace.threadlocal.code.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ThreadLocalServiceTest {
    private ThreadLocalService service = new ThreadLocalService();

    @Test
    void field(){
        log.info("main start");
        Runnable userA=() ->{
            service.logic("userA");
        };
        Runnable userB=()->{
            service.logic("userB");
        };

        Thread threadA=new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB=new Thread(userB);
        threadB.setName("thread-B");

        //이것을 시작하면 Runnable userA 로직을 시작하게 되는 것이다.
        //왜냐면 Thread threadA = new Thread(userA)에 userA가 들어가 있기 때문이다.
        threadA.start();
        //ThreadA가 완전히 끝나고 나서 threadB를 시작하는 것을 보기 위해서 2초를 쉰다.
        sleep(2000); // 동시성 문제가 발생 안하는 코드
        //sleep(100); //동시성 문제가 발생하는 코드
        threadB.start();
        //이 test를 아래 sleep 없이 시작하면 threadB가 logic 메서드의 저장, 조회 둘다 출력되야 하는데
        //저장만 출력되고 조회가 출력되기 전에 종료된다. 그 이유는 메인 쓰레드가 돌다가 이 @Test가 끝나기 때문이다.
        //다른 방법이 있지만 너무 복잡해지니 그냥 sleep으로 대기해서 조회가 찍히도록 한다.
        sleep(3000); //메인 쓰레드 종료 대기

    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
