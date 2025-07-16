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

package com.taotao.cloud.mq.consistency.raft.client;

import com.taotao.cloud.mq.consistency.raft.entity.LogEntry;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by 大东 on 2023/2/25.
 */
@Slf4j
public class RaftClientWithCommandLine {

    public static void main(String[] args) throws Throwable {

        RaftClientRPC rpc = new RaftClientRPC();

        // 从键盘接收数据
        Scanner scan = new Scanner(System.in);

        // nextLine方式接收字符串
        System.out.println("Raft client is running, please input command:");

        while (scan.hasNextLine()) {
            String input = scan.nextLine();
            if (input.equals("exit")) {
                scan.close();
                return;
            }
            String[] raftArgs = input.split(" ");
            int n = raftArgs.length;
            if (n != 2 && n != 3) {
                System.out.println("invalid input");
                continue;
            }

            // get [key]
            if (n == 2) {
                if (!raftArgs[0].equals("get")) {
                    System.out.println("invalid input");
                    continue;
                }
                LogEntry logEntry = rpc.get(raftArgs[1]);
                if (logEntry == null || logEntry.getCommand() == null) {
                    System.out.println("null");
                } else {
                    System.out.println(logEntry.getCommand().getValue());
                }
            }

            // [op] [key] [value]
            if (n == 3) {
                if (raftArgs[0].equals("put")) {
                    System.out.println(rpc.put(raftArgs[1], raftArgs[2]));
                } else {
                    System.out.println("invalid input");
                }
            }
        }
    }
}
