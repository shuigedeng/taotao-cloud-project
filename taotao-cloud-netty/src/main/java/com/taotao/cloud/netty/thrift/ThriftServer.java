package com.taotao.cloud.netty.thrift;


import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.layered.TFramedTransport;

public class ThriftServer {

    public static void main(String[] args) throws Exception {
        TNonblockingServerSocket socket = new TNonblockingServerSocket(8899);
        THsHaServer.Args arg = new THsHaServer.Args(socket).minWorkerThreads(2).maxWorkerThreads(4);
        //PersonService.Processor<PersonServiceImpl> processor = new PersonService.Processor<>(new PersonServiceImpl());
		//
        //arg.protocolFactory(new TCompactProtocol.Factory());
        //arg.transportFactory(new TFramedTransport.Factory());
        //arg.processorFactory(new TProcessorFactory(processor));
		//
        //TServer server = new THsHaServer(arg);
		//
        //System.out.println("Thrift Server Started!");
		//
        //server.serve();
    }
}
