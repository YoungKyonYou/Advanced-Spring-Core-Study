package hello.advanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalService {
    //FieldService.java와 비교하기 기존과 같지만 ThreadLocal를 추가하고 set과 get으로 변경하는 것만 차이남.
    //값을 제거할 때는 remove()를 쓴다.
    /**
     * 주의!!
     * 해당 쓰레드가 쓰레드 로컬을 모두 사용하고 나면 ThreadLocal.remove()를 호출해서 쓰레드 로컬에 저장된 값을 제거해주어야 한다.
     * 쓰레드 로컬의 값을 사용 후 제거하지 않고 그냥 두면 WAS(톰캣)처럼 쓰레드 풀을 사용하는 경우에 심각한 문제가 발생할 수 있다.
     * 강의참고!
     */
    private ThreadLocal<String> nameStore = new ThreadLocal<>();

    public String logic(String name){
        log.info("저장 name={} -> nameStore={}",name, nameStore.get());
        nameStore.set(name);
        sleep(1000);
        log.info("조회 nameStore={}", nameStore.get());
        return nameStore.get();
    }

    private void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
