package com.taotao.cloud.java.javaee.s2.c9_springcloud.p2_customer.java.controller;

import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.qf.client.OtherServiceClient;
import com.qf.client.SearchClient;
import com.qf.entity.Customer;
import com.qf.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
@RefreshScope
public class CustomerController {

    @Value("${env}")
    private String env;

    @GetMapping("/env")
    public String env(){
        return env;
    }


    @Autowired
    private OtherServiceClient otherServiceClient;


    @GetMapping("/list")
    public String list(){
        return otherServiceClient.list();
    }


    @Value("${version}")
    private String version;

    @GetMapping("/version")
    public String version() throws InterruptedException {
        Thread.sleep(3000);
        return version;
    }


    @Autowired
    private RestTemplate restTemplate;

    @Resource
    private EurekaClient eurekaClient;

    @Resource
    private SearchClient searchClient;

    @GetMapping("/customer")
    public String customer(){
        System.out.println(Thread.currentThread().getName());
        /*//1. 通过eurekaClient获取到SEARCH服务的信息
        InstanceInfo info = eurekaClient.getNextServerFromEureka("SEARCH", false);

        //2. 获取到访问的地址
        String url = info.getHomePageUrl();
        System.out.println(url);

        //3. 通过restTemplate访问
        String result = restTemplate.getForObject(url + "/search", String.class);*/

        /*// Robbin时
        String result = restTemplate.getForObject("http://SEARCH/search", String.class);*/

        String result = searchClient.search();
        //4. 返回
        return result;
    }


    @Autowired
    private CustomerService customerService;

    @GetMapping("/customer/{id}")
    @HystrixCommand(fallbackMethod = "findByIdFallBack",commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "70"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "5000"),
    })
    public Customer findById(@PathVariable Integer id) throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        if(id == 1){
            int i = 1/0;
        }
        System.out.println(customerService.findById(id));
        System.out.println(customerService.findById(id));
        customerService.clearFindById(id);
        System.out.println(customerService.findById(id));
        System.out.println(customerService.findById(id));
        return searchClient.findById(id);
    }


    // findById的降级方法  方法的描述要和接口一致
    public Customer findByIdFallBack(Integer id){
        return new Customer(-1,"",0);
    }

    @GetMapping("/getCustomer")
    public Customer getCustomer(@RequestParam Integer id, @RequestParam String name){
        return searchClient.getCustomer(id,name);
    }

    @GetMapping("/save")            // 会自动转换为POST请求  405
    public Customer save(Customer customer){
        return searchClient.save(customer);
    }

}
