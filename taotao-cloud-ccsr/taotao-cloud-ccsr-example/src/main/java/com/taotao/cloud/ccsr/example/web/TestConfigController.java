package com.taotao.cloud.ccsr.example.web;//package org.ohara.mcs.web;
//
//import com.google.protobuf.Any;
//import com.google.protobuf.InvalidProtocolBufferException;
//import lombok.Getter;
//import org.ohara.mcs.OHaraMcsService;
//import org.ohara.mcs.api.event.EventType;
//import org.ohara.mcs.api.grpc.auto.Metadata;
//import org.ohara.mcs.api.grpc.auto.MetadataReadRequest;
//import org.ohara.mcs.api.grpc.auto.Response;
//import org.ohara.msc.common.utils.GsonUtils;
//import org.ohara.msc.dto.ServerAddress;
//import org.ohara.msc.request.Payload;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//
//@RestController
//@RequestMapping("/config")
//public class TestConfigController {
//
//    @Resource
//    private OHaraMcsService oHaraMcsService;
//
//    @GetMapping("/get")
//    public String get() {
//        Payload payload = Payload.builder().build();
//        payload.setConfigData(new ServerAddress("127.0.0.3", 8000, true));
//        payload.setNamespace("default");
//        payload.setGroup("default_group");
//        payload.setDataId("default_data_id");
//        Response response = oHaraMcsService.request(payload, EventType.GET);
//        Any data = response.getData();
//        try {
//            Metadata metadata = data.unpack(Metadata.class);
//            return GsonUtils.getInstance().toJson(metadata);
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    @GetMapping("/put")
//    public String put(@RequestParam("port") String port) {
//        Payload payload = Payload.builder().build();
//        payload.setConfigData(new ServerAddress("127.0.0.5", Integer.parseInt(port), true));
//        payload.setNamespace("default");
//        payload.setGroup("default_group");
//        payload.setDataId("default_data_id");
//        Response response = oHaraMcsService.request(payload, EventType.PUT);
//        Any data = response.getData();
//        try {
//            Metadata metadata = data.unpack(Metadata.class);
//            return GsonUtils.getInstance().toJson(metadata);
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    @RequestMapping("/delete")
//    public String delete() {
//        Payload payload = Payload.builder().build();
//        payload.setConfigData(new ServerAddress("127.0.0.1", 8000, true));
//        payload.setNamespace("default");
//        payload.setGroup("default_group");
//        payload.setDataId("default_data_id");
//        Response response = oHaraMcsService.request(payload, EventType.DELETE);
//        Any data = response.getData();
//        try {
//            Metadata metadata = data.unpack(Metadata.class);
//            return GsonUtils.getInstance().toJson(metadata);
//        } catch (Exception e) {
//            return null;
//        }
//    }
//}
