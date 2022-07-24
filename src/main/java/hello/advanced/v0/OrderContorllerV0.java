package hello.advanced.v0;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
//@ResponseBody + @Controller 합쳐진 것
@RestController
@RequiredArgsConstructor
public class OrderContorllerV0 {

    private final OrderServiceV0 orderService;

    @GetMapping("/v0/request")
    public String request(String itemId){
        System.out.println("안녕");
        orderService.orderItem(itemId);
        return "ok";
    }
}
