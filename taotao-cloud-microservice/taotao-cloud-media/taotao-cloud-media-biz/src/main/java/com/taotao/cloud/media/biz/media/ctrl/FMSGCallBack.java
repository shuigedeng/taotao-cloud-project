package com.taotao.cloud.media.biz.media.ctrl;


import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByteByReference;
import org.springframework.stereotype.Controller;

/**
 * 
 * 
 *
 */
@Controller
public class FMSGCallBack implements HCNetSDK.FRealDataCallBack_V30
{
	 
	@Override
	public void invoke(NativeLong lRealHandle, int dwDataType, ByteByReference pBuffer, int dwBufSize, Pointer pUser) {
		// TODO Auto-generated method stub
		
	}
    
   
 }

