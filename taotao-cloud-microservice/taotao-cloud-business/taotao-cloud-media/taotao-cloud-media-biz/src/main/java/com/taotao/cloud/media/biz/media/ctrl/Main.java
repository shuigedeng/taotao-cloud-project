package com.taotao.cloud.media.biz.media.ctrl;

/**
 * 
 * 
 *
 */
public class Main {
	
	public static void main(String[] args) throws Exception{
		
		// 创建登录对象
		LoginPlay lp = new LoginPlay();
		// 输入摄像机ip，端口，账户，密码登录
		lp.doLogin("192.168.106.151", (short)8000, "admin", "vms@1803");
			
		// 截取摄像机实时图片
//		boolean imgSavePath = Control.getImgSavePath("192.168.106.151", "D:\\tempFile\\4.jpg");
		
		// 制摄像机云台控制(开启)
		Control.cloudControl("192.168.106.151", CloudCode.PAN_RIGHT, CloudCode.SPEED_LV6, CloudCode.START);
		try {
			// 让云台运行1000ms
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 制摄像机云台控制(关闭)
		Control.cloudControl("192.168.106.151", CloudCode.PAN_RIGHT, CloudCode.SPEED_LV6, CloudCode.END);
//		
//		System.out.println(imgSavePath);
		
//		NativeLong key = Control.realPlay("192.168.106.151");
//		//判断是否预览成功
//        if(key.intValue()==-1){
//            System.out.println("预览失败   错误代码为:  "+HCNetSDK.INSTANCE.NET_DVR_GetLastError());
//            HCNetSDK.INSTANCE.NET_DVR_Cleanup();
//        }
//        
//      //预览成功后 调用接口使视频资源保存到文件中
//        if(!HCNetSDK.INSTANCE.NET_DVR_SaveRealData(key, "D:\\tempFile\\1.mp4")){
//            System.out.println("保存到文件失败 错误码为:  "+HCNetSDK.INSTANCE.NET_DVR_GetLastError());
//            HCNetSDK.INSTANCE.NET_DVR_StopRealPlay(key);
//            HCNetSDK.INSTANCE.NET_DVR_Cleanup();
//        }
//         try {
//            Thread.sleep(5000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //上面设置的睡眠时间可以当做拍摄时长来使用,然后调用结束预览,注销用户,释放资源就可以了
//         HCNetSDK.INSTANCE.NET_DVR_StopRealPlay(key);
//         HCNetSDK.INSTANCE.NET_DVR_Cleanup();
         
//         System.out.println(HCNetSDK.INSTANCE.NET_DVR_Logout(lp.user()));
		
//         boolean imgSavePath = Control.getImgSavePath("192.168.106.151", "D:\\tempFile\\5.jpg");
//		System.out.println(imgSavePath);
		
		
		
         System.out.println("退出：" + lp.doLogout());
         
         System.out.println(HCNetSDK.INSTANCE.NET_DVR_Cleanup());
	}
	
	
}
