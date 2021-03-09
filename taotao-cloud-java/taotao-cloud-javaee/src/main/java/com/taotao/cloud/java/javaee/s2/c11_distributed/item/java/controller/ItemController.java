package com.taotao.cloud.java.javaee.s2.c11_distributed.item.java.controller;

import com.qf.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;


    @GetMapping("/item")
    public String item(){
        itemService.update();
        return null;
    }

}
