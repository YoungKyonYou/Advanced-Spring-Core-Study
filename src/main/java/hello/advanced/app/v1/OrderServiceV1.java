package hello.advanced.app.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//ComponentScan의 대상이 됨
@Service
@RequiredArgsConstructor
public class OrderServiceV1 {
    private final OrderRepositoryV1 orderRepository;

    public void orderItem(String itemId){
        orderRepository.save(itemId);
    }
}
