package hello.advanced.app.v2.v1;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

//Component Scan의 대상으로 만들어줌
@Repository
@RequiredArgsConstructor
public class OrderRepositoryV2 {

    private final HelloTraceV2 trace;
    public void save(TraceId traceId, String itemId){

        TraceStatus status=null;
        //try catch를 하는 이유는 만약 orderService.orderItem(itemID)에서 오류가 나면
        //그 다음 줄인 trace.end(status)까지 안 내려가고 종료된다. 그것을 위해서 위에 TraceStatus status를 따로 선언하고
        //catch에서 그 status를 받아서 출력하기 위함이다.
        try {
            status = trace.beginSync(traceId, "OrderRepositorysave()");
            //저장 로직
            if(itemId.equals("ex")){
                throw new IllegalStateException("예외 발생!");
            }
            sleep(1000);
            trace.end(status);
        }catch(Exception e){
            trace.exception(status, e);
            //예외를 꼭 던져줘야 함. 이거 안 던지면 위에 trace.exception(status, e)가 예외를 먹어버리기 때문이다.
            //예외를 먹어버리게 되면 이후에 정상 흐름으로 동작한다. 로그는 애플리케이션에 흐름에 영향을 주면 안된다. 로그 때문에 예외가 사라지면 안된다.
            throw e;
        }

    }
    private void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
