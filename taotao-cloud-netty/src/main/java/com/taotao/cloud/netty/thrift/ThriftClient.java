/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.netty.thrift;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;

/**
 * ThriftClient
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class ThriftClient {

    public static void main( String[] args ) throws TTransportException {
        TTransport transport = new TFramedTransport(new TSocket("localhost", 8899), 600);
        TProtocol protocol = new TCompactProtocol(transport);
        // PersonService.Client client = new PersonService.Client(protocol);
        //
        // try {
        //    transport.open();
        //
        //    Person person = client.getPersonByUsername("张三");
        //
        //    System.out.println(person.getUsername());
        //    System.out.println(person.getAge());
        //    System.out.println(person.isMarried());
        //
        //    System.out.println("-------");
        //
        //    Person person2 = new Person();
        //
        //    person2.setUsername("李四");
        //    person2.setAge(30);
        //    person2.setMarried(true);
        //
        //    client.savePerson(person2);
        // } catch (Exception ex) {
        //    throw new RuntimeException(ex.getMessage(), ex);
        // } finally {
        //    transport.close();
        // }
    }
}
