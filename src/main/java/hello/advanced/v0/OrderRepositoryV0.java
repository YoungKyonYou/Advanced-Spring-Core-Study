package hello.advanced.v0;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static java.lang.Thread.sleep;

//Component Scan의 대상으로 만들어줌
@Repository
@RequiredArgsConstructor
public class OrderRepositoryV0 {

    public void save(String itemId){
        //저장 로직
        if(itemId.equals("ex")){
            throw new IllegalStateException("예외 발생!");
        }
        sleep(1000);
    }
    private void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
