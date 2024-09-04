@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService=orderService;
    }
}