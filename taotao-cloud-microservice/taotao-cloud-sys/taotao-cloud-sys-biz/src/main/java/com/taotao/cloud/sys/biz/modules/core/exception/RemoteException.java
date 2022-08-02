package com.taotao.cloud.sys.biz.modules.core.exception;


import com.taotao.cloud.sys.biz.modules.core.dtos.ResponseDto;

public class RemoteException  extends BusinessException{
    private BusinessException parent;

    private RemoteException(BusinessException parent) {
        this.parent = parent;
    }

    /**
     * 创建远程异常
     * @param parent
     * @param remoteCode
     * @param remoteMessage
     * @return
     */
    public static RemoteException create(BusinessException parent,String remoteCode,String remoteMessage){
        RemoteException remoteException = new RemoteException(parent);
        remoteException.responseDto = ResponseDto.err(remoteCode).message(remoteMessage);
        return remoteException;
    }

    /**
     * 简易创建远程信息
     * @param parent
     * @param remoteMessage
     * @return
     */
    public static RemoteException create(BusinessException parent,String remoteMessage){
        return create(parent,"remoteError",remoteMessage);
    }

    public static RemoteException create(String localMessage,String remoteCode,String remoteMessage){
        return new Builder().localMessage(localMessage).remoteCode(remoteCode).remoteMessage(remoteMessage).build();
    }
    public static RemoteException create(String localMessage,String remoteMessage){
        return new Builder().localMessage(localMessage).remoteMessage(remoteMessage).build();
    }

    public static class Builder{
        private String localCode;
        private String localMessage;

        private String remoteCode;
        private String remoteMessage;

        public Builder localCode(String localCode){
            this.localCode = localCode;
            return this;
        }
        public Builder localMessage(String localMessage){
            this.localMessage = localMessage;
            return this;
        }
        public Builder remoteCode(String remoteCode){
            this.remoteCode = remoteCode;
            return this;
        }
        public Builder remoteMessage(String remoteMessage){
            this.remoteMessage = remoteMessage;
            return this;
        }

        public RemoteException build(){
            BusinessException businessException = BusinessException.create(localCode, localMessage);
            RemoteException remoteException = RemoteException.create(businessException,remoteCode,remoteMessage);
            return remoteException;
        }
    }

    public BusinessException getParent() {
        return parent;
    }
}
