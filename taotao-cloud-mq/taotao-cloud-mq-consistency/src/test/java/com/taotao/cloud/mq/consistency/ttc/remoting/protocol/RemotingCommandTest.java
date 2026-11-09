package com.taotao.cloud.mq.consistency.ttc.remoting.protocol;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RemotingCommand 单元测试
 */
public class RemotingCommandTest {

    @Test
    public void testCreateRequestCommand() {
        int code = 100;
        RemotingCommand command = RemotingCommand.createRequestCommand(code);

        assertNotNull(command);
        assertEquals(code, command.getCode());
        assertTrue(command.getOpaque() > 0);
    }

    @Test
    public void testCreateResponseCommand() {
        int code = 200;
        String remark = "Success";
        RemotingCommand command = RemotingCommand.createResponseCommand(code, remark);

        assertNotNull(command);
        assertEquals(code, command.getCode());
        assertEquals(remark, command.getRemark());
    }

    @Test
    public void testGetSetCode() {
        RemotingCommand command = new RemotingCommand();
        command.setCode(300);
        assertEquals(300, command.getCode());
    }

    @Test
    public void testGetSetRemark() {
        RemotingCommand command = new RemotingCommand();
        command.setRemark("Test remark");
        assertEquals("Test remark", command.getRemark());
    }

    @Test
    public void testGetSetBody() {
        RemotingCommand command = new RemotingCommand();
        byte[] body = "Test body".getBytes();
        command.setBody(body);
        assertArrayEquals(body, command.getBody());
    }

    @Test
    public void testIsResponseType() {
        RemotingCommand request = RemotingCommand.createRequestCommand(100);
        RemotingCommand response = RemotingCommand.createResponseCommand(200, "OK");

        assertFalse(request.isResponseType());
        assertTrue(response.isResponseType());
    }

    @Test
    public void testOpaque() {
        RemotingCommand command = RemotingCommand.createRequestCommand(100);
        int opaque = command.getOpaque();

        assertTrue(opaque > 0);

        command.setOpaque(999);
        assertEquals(999, command.getOpaque());
    }
}
