package hello.advanced.app.v2.v1;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV1;
import hello.advanced.trace.hellotrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
//@ResponseBody + @Controller 합쳐진 것
@RestController
@RequiredArgsConstructor
public class OrderControllerV2 {

    private final OrderServiceV2 orderService;
    private final HelloTraceV2 trace;


    @GetMapping("/v2/request")
    public String request(String itemId){
        TraceStatus status=null;
        //try catch를 하는 이유는 만약 orderService.orderItem(itemID)에서 오류가 나면
        //그 다음 줄인 trace.end(status)까지 안 내려가고 종료된다. 그것을 위해서 위에 TraceStatus status를 따로 선언하고
        //catch에서 그 status를 받아서 출력하기 위함이다.
        try {
            status = trace.begin("OrderController.request()");
            orderService.orderItem(status.getTraceId(),itemId);
            trace.end(status);
            return "ok";
        }catch(Exception e){
            trace.exception(status, e);
            //예외를 꼭 던져줘야 함. 이거 안 던지면 위에 trace.exception(status, e)가 예외를 먹어버리기 때문이다.
            //예외를 먹어버리게 되면 이후에 정상 흐름으로 동작한다. 로그는 애플리케이션에 흐름에 영향을 주면 안된다. 로그 때문에 예외가 사라지면 안된다.
            throw e;
        }
    }
}
