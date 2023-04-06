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

package com.taotao.cloud.media.biz.media.ctrl;

// import com.sun.javafx.font.directwrite.RECT;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;
import com.sun.jna.examples.win32.GDI32.RECT;
import com.sun.jna.examples.win32.W32API;
import com.sun.jna.examples.win32.W32API.HWND;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.ptr.ShortByReference;
import com.sun.jna.win32.StdCallLibrary;

// SDK接口说明,HCNetSDK.dll
public interface HCNetSDK extends StdCallLibrary {

    HCNetSDK INSTANCE = (HCNetSDK) Native.loadLibrary("sdk\\hcsdk\\HCNetSDK", HCNetSDK.class);
    /***宏定义***/
    // 常量

    public static final int MAX_NAMELEN = 16; // DVR本地登陆名

    public static final int MAX_RIGHT = 32; // 设备支持的权限（1-12表示本地权限，13-32表示远程权限）
    public static final int NAME_LEN = 32; // 用户名长度
    public static final int PASSWD_LEN = 16; // 密码长度
    public static final int SERIALNO_LEN = 48; // 序列号长度
    public static final int MACADDR_LEN = 6; // mac地址长度
    public static final int MAX_ETHERNET = 2; // 设备可配以太网络
    public static final int PATHNAME_LEN = 128; // 路径长度
    public static final int MAX_TIMESEGMENT_V30 = 8; // 9000设备最大时间段数
    public static final int MAX_TIMESEGMENT = 4; // 8000设备最大时间段数
    public static final int MAX_SHELTERNUM = 4; // 8000设备最大遮挡区域数
    public static final int MAX_DAYS = 7; // 每周天数
    public static final int PHONENUMBER_LEN = 32; // pppoe拨号号码最大长度
    public static final int MAX_DISKNUM_V30 = 33; // 9000设备最大硬盘数/* 最多33个硬盘(包括16个内置SATA硬盘、1个eSATA硬盘和16个NFS盘) */
    public static final int MAX_DISKNUM = 16; // 8000设备最大硬盘数
    public static final int MAX_DISKNUM_V10 = 8; // 1.2版本之前版本
    public static final int MAX_WINDOW_V30 = 32; // 9000设备本地显示最大播放窗口数
    public static final int MAX_WINDOW = 16; // 8000设备最大硬盘数
    public static final int MAX_VGA_V30 = 4; // 9000设备最大可接VGA数
    public static final int MAX_VGA = 1; // 8000设备最大可接VGA数
    public static final int MAX_USERNUM_V30 = 32; // 9000设备最大用户数
    public static final int MAX_USERNUM = 16; // 8000设备最大用户数
    public static final int MAX_EXCEPTIONNUM_V30 = 32; // 9000设备最大异常处理数
    public static final int MAX_EXCEPTIONNUM = 16; // 8000设备最大异常处理数
    public static final int MAX_LINK = 6; // 8000设备单通道最大视频流连接数
    public static final int MAX_DECPOOLNUM = 4; // 单路解码器每个解码通道最大可循环解码数
    public static final int MAX_DECNUM = 4; // 单路解码器的最大解码通道数（实际只有一个，其他三个保留）
    public static final int MAX_TRANSPARENTNUM = 2; // 单路解码器可配置最大透明通道数
    public static final int MAX_CYCLE_CHAN = 16; // 单路解码器最大轮循通道数
    public static final int MAX_DIRNAME_LENGTH = 80; // 最大目录长度
    public static final int MAX_STRINGNUM_V30 = 8; // 9000设备最大OSD字符行数数
    public static final int MAX_STRINGNUM = 4; // 8000设备最大OSD字符行数数
    public static final int MAX_STRINGNUM_EX = 8; // 8000定制扩展
    public static final int MAX_AUXOUT_V30 = 16; // 9000设备最大辅助输出数
    public static final int MAX_AUXOUT = 4; // 8000设备最大辅助输出数
    public static final int MAX_HD_GROUP = 16; // 9000设备最大硬盘组数
    public static final int MAX_NFS_DISK = 8; // 8000设备最大NFS硬盘数
    public static final int IW_ESSID_MAX_SIZE = 32; // WIFI的SSID号长度
    public static final int IW_ENCODING_TOKEN_MAX = 32; // WIFI密锁最大字节数
    public static final int MAX_SERIAL_NUM = 64; // 最多支持的透明通道路数
    public static final int MAX_DDNS_NUMS = 10; // 9000设备最大可配ddns数
    public static final int MAX_DOMAIN_NAME = 64; /* 最大域名长度 */

    public static final int MAX_EMAIL_ADDR_LEN = 48; // 最大email地址长度
    public static final int MAX_EMAIL_PWD_LEN = 32; // 最大email密码长度
    public static final int MAXPROGRESS = 100; // 回放时的最大百分率
    public static final int MAX_SERIALNUM = 2; // 8000设备支持的串口数 1-232， 2-485
    public static final int CARDNUM_LEN = 20; // 卡号长度
    public static final int MAX_VIDEOOUT_V30 = 4; // 9000设备的视频输出数
    public static final int MAX_VIDEOOUT = 2; // 8000设备的视频输出数
    public static final int MAX_PRESET_V30 = 256; /* 9000设备支持的云台预置点数 */
    public static final int MAX_TRACK_V30 = 256; /* 9000设备支持的云台轨迹数 */
    public static final int MAX_CRUISE_V30 = 256; /* 9000设备支持的云台巡航数 */
    public static final int MAX_PRESET = 128; /* 8000设备支持的云台预置点数 */
    public static final int MAX_TRACK = 128; /* 8000设备支持的云台轨迹数 */
    public static final int MAX_CRUISE = 128; /* 8000设备支持的云台巡航数 */
    public static final int CRUISE_MAX_PRESET_NUMS = 32; /* 一条巡航最多的巡航点 */
    public static final int MAX_SERIAL_PORT = 8; // 9000设备支持232串口数
    public static final int MAX_PREVIEW_MODE = 8; /* 设备支持最大预览模式数目 1画面,4画面,9画面,16画面.... */
    public static final int MAX_MATRIXOUT = 16; /* 最大模拟矩阵输出个数 */
    public static final int LOG_INFO_LEN = 11840; /* 日志附加信息 */
    public static final int DESC_LEN = 16; /* 云台描述字符串长度 */
    public static final int PTZ_PROTOCOL_NUM = 200; /* 9000最大支持的云台协议数 */
    public static final int MAX_AUDIO = 1; // 8000语音对讲通道数
    public static final int MAX_AUDIO_V30 = 2; // 9000语音对讲通道数
    public static final int MAX_CHANNUM = 16; // 8000设备最大通道数
    public static final int MAX_ALARMIN = 16; // 8000设备最大报警输入数
    public static final int MAX_ALARMOUT = 4; // 8000设备最大报警输出数
    // 9000 IPC接入
    public static final int MAX_ANALOG_CHANNUM = 32; // 最大32个模拟通道
    public static final int MAX_ANALOG_ALARMOUT = 32; // 最大32路模拟报警输出
    public static final int MAX_ANALOG_ALARMIN = 32; // 最大32路模拟报警输入
    public static final int MAX_IP_DEVICE = 32; // 允许接入的最大IP设备数
    public static final int MAX_IP_CHANNEL = 32; // 允许加入的最多IP通道数
    public static final int MAX_IP_ALARMIN = 128; // 允许加入的最多报警输入数
    public static final int MAX_IP_ALARMOUT = 64; // 允许加入的最多报警输出数

    /* 最大支持的通道数 最大模拟加上最大IP支持 */
    public static final int MAX_CHANNUM_V30 = (MAX_ANALOG_CHANNUM + MAX_IP_CHANNEL); // 64
    public static final int MAX_ALARMOUT_V30 = (MAX_ANALOG_ALARMOUT + MAX_IP_ALARMOUT); // 96
    public static final int MAX_ALARMIN_V30 = (MAX_ANALOG_ALARMIN + MAX_IP_ALARMIN); // 160

    /*******************全局错误码 begin**********************/
    public static final int NET_DVR_NOERROR = 0; // 没有错误

    public static final int NET_DVR_PASSWORD_ERROR = 1; // 用户名密码错误
    public static final int NET_DVR_NOENOUGHPRI = 2; // 权限不足
    public static final int NET_DVR_NOINIT = 3; // 没有初始化
    public static final int NET_DVR_CHANNEL_ERROR = 4; // 通道号错误
    public static final int NET_DVR_OVER_MAXLINK = 5; // 连接到DVR的客户端个数超过最大
    public static final int NET_DVR_VERSIONNOMATCH = 6; // 版本不匹配
    public static final int NET_DVR_NETWORK_FAIL_CONNECT = 7; // 连接服务器失败
    public static final int NET_DVR_NETWORK_SEND_ERROR = 8; // 向服务器发送失败
    public static final int NET_DVR_NETWORK_RECV_ERROR = 9; // 从服务器接收数据失败
    public static final int NET_DVR_NETWORK_RECV_TIMEOUT = 10; // 从服务器接收数据超时
    public static final int NET_DVR_NETWORK_ERRORDATA = 11; // 传送的数据有误
    public static final int NET_DVR_ORDER_ERROR = 12; // 调用次序错误
    public static final int NET_DVR_OPERNOPERMIT = 13; // 无此权限
    public static final int NET_DVR_COMMANDTIMEOUT = 14; // DVR命令执行超时
    public static final int NET_DVR_ERRORSERIALPORT = 15; // 串口号错误
    public static final int NET_DVR_ERRORALARMPORT = 16; // 报警端口错误
    public static final int NET_DVR_PARAMETER_ERROR = 17; // 参数错误
    public static final int NET_DVR_CHAN_EXCEPTION = 18; // 服务器通道处于错误状态
    public static final int NET_DVR_NODISK = 19; // 没有硬盘
    public static final int NET_DVR_ERRORDISKNUM = 20; // 硬盘号错误
    public static final int NET_DVR_DISK_FULL = 21; // 服务器硬盘满
    public static final int NET_DVR_DISK_ERROR = 22; // 服务器硬盘出错
    public static final int NET_DVR_NOSUPPORT = 23; // 服务器不支持
    public static final int NET_DVR_BUSY = 24; // 服务器忙
    public static final int NET_DVR_MODIFY_FAIL = 25; // 服务器修改不成功
    public static final int NET_DVR_PASSWORD_FORMAT_ERROR = 26; // 密码输入格式不正确
    public static final int NET_DVR_DISK_FORMATING = 27; // 硬盘正在格式化，不能启动操作
    public static final int NET_DVR_DVRNORESOURCE = 28; // DVR资源不足
    public static final int NET_DVR_DVROPRATEFAILED = 29; // DVR操作失败
    public static final int NET_DVR_OPENHOSTSOUND_FAIL = 30; // 打开PC声音失败
    public static final int NET_DVR_DVRVOICEOPENED = 31; // 服务器语音对讲被占用
    public static final int NET_DVR_TIMEINPUTERROR = 32; // 时间输入不正确
    public static final int NET_DVR_NOSPECFILE = 33; // 回放时服务器没有指定的文件
    public static final int NET_DVR_CREATEFILE_ERROR = 34; // 创建文件出错
    public static final int NET_DVR_FILEOPENFAIL = 35; // 打开文件出错
    public static final int NET_DVR_OPERNOTFINISH = 36; // 上次的操作还没有完成
    public static final int NET_DVR_GETPLAYTIMEFAIL = 37; // 获取当前播放的时间出错
    public static final int NET_DVR_PLAYFAIL = 38; // 播放出错
    public static final int NET_DVR_FILEFORMAT_ERROR = 39; // 文件格式不正确
    public static final int NET_DVR_DIR_ERROR = 40; // 路径错误
    public static final int NET_DVR_ALLOC_RESOURCE_ERROR = 41; // 资源分配错误
    public static final int NET_DVR_AUDIO_MODE_ERROR = 42; // 声卡模式错误
    public static final int NET_DVR_NOENOUGH_BUF = 43; // 缓冲区太小
    public static final int NET_DVR_CREATESOCKET_ERROR = 44; // 创建SOCKET出错
    public static final int NET_DVR_SETSOCKET_ERROR = 45; // 设置SOCKET出错
    public static final int NET_DVR_MAX_NUM = 46; // 个数达到最大
    public static final int NET_DVR_USERNOTEXIST = 47; // 用户不存在
    public static final int NET_DVR_WRITEFLASHERROR = 48; // 写FLASH出错
    public static final int NET_DVR_UPGRADEFAIL = 49; // DVR升级失败
    public static final int NET_DVR_CARDHAVEINIT = 50; // 解码卡已经初始化过
    public static final int NET_DVR_PLAYERFAILED = 51; // 调用播放库中某个函数失败
    public static final int NET_DVR_MAX_USERNUM = 52; // 设备端用户数达到最大
    public static final int NET_DVR_GETLOCALIPANDMACFAIL = 53; // 获得客户端的IP地址或物理地址失败
    public static final int NET_DVR_NOENCODEING = 54; // 该通道没有编码
    public static final int NET_DVR_IPMISMATCH = 55; // IP地址不匹配
    public static final int NET_DVR_MACMISMATCH = 56; // MAC地址不匹配
    public static final int NET_DVR_UPGRADELANGMISMATCH = 57; // 升级文件语言不匹配
    public static final int NET_DVR_MAX_PLAYERPORT = 58; // 播放器路数达到最大
    public static final int NET_DVR_NOSPACEBACKUP = 59; // 备份设备中没有足够空间进行备份
    public static final int NET_DVR_NODEVICEBACKUP = 60; // 没有找到指定的备份设备
    public static final int NET_DVR_PICTURE_BITS_ERROR = 61; // 图像素位数不符，限24色
    public static final int NET_DVR_PICTURE_DIMENSION_ERROR = 62; // 图片高*宽超限， 限128*256
    public static final int NET_DVR_PICTURE_SIZ_ERROR = 63; // 图片大小超限，限100K
    public static final int NET_DVR_LOADPLAYERSDKFAILED = 64; // 载入当前目录下Player Sdk出错
    public static final int NET_DVR_LOADPLAYERSDKPROC_ERROR = 65; // 找不到Player Sdk中某个函数入口
    public static final int NET_DVR_LOADDSSDKFAILED = 66; // 载入当前目录下DSsdk出错
    public static final int NET_DVR_LOADDSSDKPROC_ERROR = 67; // 找不到DsSdk中某个函数入口
    public static final int NET_DVR_DSSDK_ERROR = 68; // 调用硬解码库DsSdk中某个函数失败
    public static final int NET_DVR_VOICEMONOPOLIZE = 69; // 声卡被独占
    public static final int NET_DVR_JOINMULTICASTFAILED = 70; // 加入多播组失败
    public static final int NET_DVR_CREATEDIR_ERROR = 71; // 建立日志文件目录失败
    public static final int NET_DVR_BINDSOCKET_ERROR = 72; // 绑定套接字失败
    public static final int NET_DVR_SOCKETCLOSE_ERROR = 73; // socket连接中断，此错误通常是由于连接中断或目的地不可达
    public static final int NET_DVR_USERID_ISUSING = 74; // 注销时用户ID正在进行某操作
    public static final int NET_DVR_SOCKETLISTEN_ERROR = 75; // 监听失败
    public static final int NET_DVR_PROGRAM_EXCEPTION = 76; // 程序异常
    public static final int NET_DVR_WRITEFILE_FAILED = 77; // 写文件失败
    public static final int NET_DVR_FORMAT_READONLY = 78; // 禁止格式化只读硬盘
    public static final int NET_DVR_WITHSAMEUSERNAME = 79; // 用户配置结构中存在相同的用户名
    public static final int NET_DVR_DEVICETYPE_ERROR = 80; /*导入参数时设备型号不匹配*/
    public static final int NET_DVR_LANGUAGE_ERROR = 81; /*导入参数时语言不匹配*/
    public static final int NET_DVR_PARAVERSION_ERROR = 82; /*导入参数时软件版本不匹配*/
    public static final int NET_DVR_IPCHAN_NOTALIVE = 83; /*预览时外接IP通道不在线*/
    public static final int NET_DVR_RTSP_SDK_ERROR = 84; /*加载高清IPC通讯库StreamTransClient.dll失败*/
    public static final int NET_DVR_CONVERT_SDK_ERROR = 85; /*加载转码库失败*/
    public static final int NET_DVR_IPC_COUNT_OVERFLOW = 86; /*超出最大的ip接入通道数*/
    public static final int NET_PLAYM4_NOERROR = 500; // no error
    public static final int NET_PLAYM4_PARA_OVER = 501; // input parameter is invalid;
    public static final int NET_PLAYM4_ORDER_ERROR = 502; // The order of the function to be called is error.
    public static final int NET_PLAYM4_TIMER_ERROR = 503; // Create multimedia clock failed;
    public static final int NET_PLAYM4_DEC_VIDEO_ERROR = 504; // Decode video data failed.
    public static final int NET_PLAYM4_DEC_AUDIO_ERROR = 505; // Decode audio data failed.
    public static final int NET_PLAYM4_ALLOC_MEMORY_ERROR = 506; // Allocate memory failed.
    public static final int NET_PLAYM4_OPEN_FILE_ERROR = 507; // Open the file failed.
    public static final int NET_PLAYM4_CREATE_OBJ_ERROR = 508; // Create thread or event failed
    public static final int NET_PLAYM4_CREATE_DDRAW_ERROR = 509; // Create DirectDraw object failed.
    public static final int NET_PLAYM4_CREATE_OFFSCREEN_ERROR = 510; // failed when creating off-screen surface.
    public static final int NET_PLAYM4_BUF_OVER = 511; // buffer is overflow
    public static final int NET_PLAYM4_CREATE_SOUND_ERROR = 512; // failed when creating audio device.
    public static final int NET_PLAYM4_SET_VOLUME_ERROR = 513; // Set volume failed
    public static final int NET_PLAYM4_SUPPORT_FILE_ONLY = 514; // The function only support play file.
    public static final int NET_PLAYM4_SUPPORT_STREAM_ONLY = 515; // The function only support play stream.
    public static final int NET_PLAYM4_SYS_NOT_SUPPORT = 516; // System not support.
    public static final int NET_PLAYM4_FILEHEADER_UNKNOWN = 517; // No file header.
    public static final int NET_PLAYM4_VERSION_INCORRECT = 518; // The version of decoder and encoder is not adapted.
    public static final int NET_PALYM4_INIT_DECODER_ERROR = 519; // Initialize decoder failed.
    public static final int NET_PLAYM4_CHECK_FILE_ERROR = 520; // The file data is unknown.
    public static final int NET_PLAYM4_INIT_TIMER_ERROR = 521; // Initialize multimedia clock failed.
    public static final int NET_PLAYM4_BLT_ERROR = 522; // Blt failed.
    public static final int NET_PLAYM4_UPDATE_ERROR = 523; // Update failed.
    public static final int NET_PLAYM4_OPEN_FILE_ERROR_MULTI = 524; // openfile error, streamtype is multi
    public static final int NET_PLAYM4_OPEN_FILE_ERROR_VIDEO = 525; // openfile error, streamtype is video
    public static final int NET_PLAYM4_JPEG_COMPRESS_ERROR = 526; // JPEG compress error
    public static final int NET_PLAYM4_EXTRACT_NOT_SUPPORT = 527; // Don't support the version of this file.
    public static final int NET_PLAYM4_EXTRACT_DATA_ERROR = 528; // extract video data failed.
    /*******************全局错误码 end**********************/
    /*************************************************
     * NET_DVR_IsSupport()返回值
     * 1－9位分别表示以下信息（位与是TRUE)表示支持；
     **************************************************/
    public static final int NET_DVR_SUPPORT_DDRAW = 0x01; // 支持DIRECTDRAW，如果不支持，则播放器不能工作；

    public static final int NET_DVR_SUPPORT_BLT = 0x02; // 显卡支持BLT操作，如果不支持，则播放器不能工作；
    public static final int NET_DVR_SUPPORT_BLTFOURCC = 0x04; // 显卡BLT支持颜色转换，如果不支持，播放器会用软件方法作RGB转换；
    public static final int NET_DVR_SUPPORT_BLTSHRINKX = 0x08; // 显卡BLT支持X轴缩小；如果不支持，系统会用软件方法转换；
    public static final int NET_DVR_SUPPORT_BLTSHRINKY = 0x10; // 显卡BLT支持Y轴缩小；如果不支持，系统会用软件方法转换；
    public static final int NET_DVR_SUPPORT_BLTSTRETCHX = 0x20; // 显卡BLT支持X轴放大；如果不支持，系统会用软件方法转换；
    public static final int NET_DVR_SUPPORT_BLTSTRETCHY = 0x40; // 显卡BLT支持Y轴放大；如果不支持，系统会用软件方法转换；
    public static final int NET_DVR_SUPPORT_SSE = 0x80; // CPU支持SSE指令，Intel Pentium3以上支持SSE指令；
    public static final int NET_DVR_SUPPORT_MMX = 0x100; // CPU支持MMX指令集，Intel Pentium3以上支持SSE指令；
    /**********************云台控制命令 begin*************************/
    public static final int LIGHT_PWRON = 2; /* 接通灯光电源 */

    public static final int WIPER_PWRON = 3; /* 接通雨刷开关 */
    public static final int FAN_PWRON = 4; /* 接通风扇开关 */
    public static final int HEATER_PWRON = 5; /* 接通加热器开关 */
    public static final int AUX_PWRON1 = 6; /* 接通辅助设备开关 */
    public static final int AUX_PWRON2 = 7; /* 接通辅助设备开关 */
    public static final int SET_PRESET = 8; /* 设置预置点 */
    public static final int CLE_PRESET = 9; /* 清除预置点 */
    public static final int ZOOM_IN = 11; /* 焦距以速度SS变大(倍率变大) */
    public static final int ZOOM_OUT = 12; /* 焦距以速度SS变小(倍率变小) */
    public static final int FOCUS_NEAR = 13; /* 焦点以速度SS前调 */
    public static final int FOCUS_FAR = 14; /* 焦点以速度SS后调 */
    public static final int IRIS_OPEN = 15; /* 光圈以速度SS扩大 */
    public static final int IRIS_CLOSE = 16; /* 光圈以速度SS缩小 */
    public static final int TILT_UP = 21; /* 云台以SS的速度上仰 */
    public static final int TILT_DOWN = 22; /* 云台以SS的速度下俯 */
    public static final int PAN_LEFT = 23; /* 云台以SS的速度左转 */
    public static final int PAN_RIGHT = 24; /* 云台以SS的速度右转 */
    public static final int UP_LEFT = 25; /* 云台以SS的速度上仰和左转 */
    public static final int UP_RIGHT = 26; /* 云台以SS的速度上仰和右转 */
    public static final int DOWN_LEFT = 27; /* 云台以SS的速度下俯和左转 */
    public static final int DOWN_RIGHT = 28; /* 云台以SS的速度下俯和右转 */
    public static final int PAN_AUTO = 29; /* 云台以SS的速度左右自动扫描 */
    public static final int FILL_PRE_SEQ = 30; /* 将预置点加入巡航序列 */
    public static final int SET_SEQ_DWELL = 31; /* 设置巡航点停顿时间 */
    public static final int SET_SEQ_SPEED = 32; /* 设置巡航速度 */
    public static final int CLE_PRE_SEQ = 33; /* 将预置点从巡航序列中删除 */
    public static final int STA_MEM_CRUISE = 34; /* 开始记录轨迹 */
    public static final int STO_MEM_CRUISE = 35; /* 停止记录轨迹 */
    public static final int RUN_CRUISE = 36; /* 开始轨迹 */
    public static final int RUN_SEQ = 37; /* 开始巡航 */
    public static final int STOP_SEQ = 38; /* 停止巡航 */
    public static final int GOTO_PRESET = 39; /* 快球转到预置点 */

    /**********************云台控制命令 end*************************/
    /*************************************************
     * 回放时播放控制命令宏定义
     * NET_DVR_PlayBackControl
     * NET_DVR_PlayControlLocDisplay
     * NET_DVR_DecPlayBackCtrl的宏定义
     * 具体支持查看函数说明和代码
     **************************************************/
    public static final int NET_DVR_PLAYSTART = 1; // 开始播放

    public static final int NET_DVR_PLAYSTOP = 2; // 停止播放
    public static final int NET_DVR_PLAYPAUSE = 3; // 暂停播放
    public static final int NET_DVR_PLAYRESTART = 4; // 恢复播放
    public static final int NET_DVR_PLAYFAST = 5; // 快放
    public static final int NET_DVR_PLAYSLOW = 6; // 慢放
    public static final int NET_DVR_PLAYNORMAL = 7; // 正常速度
    public static final int NET_DVR_PLAYFRAME = 8; // 单帧放
    public static final int NET_DVR_PLAYSTARTAUDIO = 9; // 打开声音
    public static final int NET_DVR_PLAYSTOPAUDIO = 10; // 关闭声音
    public static final int NET_DVR_PLAYAUDIOVOLUME = 11; // 调节音量
    public static final int NET_DVR_PLAYSETPOS = 12; // 改变文件回放的进度
    public static final int NET_DVR_PLAYGETPOS = 13; // 获取文件回放的进度
    public static final int NET_DVR_PLAYGETTIME = 14; // 获取当前已经播放的时间(按文件回放的时候有效)
    public static final int NET_DVR_PLAYGETFRAME = 15; // 获取当前已经播放的帧数(按文件回放的时候有效)
    public static final int NET_DVR_GETTOTALFRAMES = 16; // 获取当前播放文件总的帧数(按文件回放的时候有效)
    public static final int NET_DVR_GETTOTALTIME = 17; // 获取当前播放文件总的时间(按文件回放的时候有效)
    public static final int NET_DVR_THROWBFRAME = 20; // 丢B帧
    public static final int NET_DVR_SETSPEED = 24; // 设置码流速度
    public static final int NET_DVR_KEEPALIVE = 25; // 保持与设备的心跳(如果回调阻塞，建议2秒发送一次)
    // 远程按键定义如下：
    /* key value send to CONFIG program */
    public static final int KEY_CODE_1 = 1;
    public static final int KEY_CODE_2 = 2;
    public static final int KEY_CODE_3 = 3;
    public static final int KEY_CODE_4 = 4;
    public static final int KEY_CODE_5 = 5;
    public static final int KEY_CODE_6 = 6;
    public static final int KEY_CODE_7 = 7;
    public static final int KEY_CODE_8 = 8;
    public static final int KEY_CODE_9 = 9;
    public static final int KEY_CODE_0 = 10;
    public static final int KEY_CODE_POWER = 11;
    public static final int KEY_CODE_MENU = 12;
    public static final int KEY_CODE_ENTER = 13;
    public static final int KEY_CODE_CANCEL = 14;
    public static final int KEY_CODE_UP = 15;
    public static final int KEY_CODE_DOWN = 16;
    public static final int KEY_CODE_LEFT = 17;
    public static final int KEY_CODE_RIGHT = 18;
    public static final int KEY_CODE_EDIT = 19;
    public static final int KEY_CODE_ADD = 20;
    public static final int KEY_CODE_MINUS = 21;
    public static final int KEY_CODE_PLAY = 22;
    public static final int KEY_CODE_REC = 23;
    public static final int KEY_CODE_PAN = 24;
    public static final int KEY_CODE_M = 25;
    public static final int KEY_CODE_A = 26;
    public static final int KEY_CODE_F1 = 27;
    public static final int KEY_CODE_F2 = 28;

    /* for PTZ control */
    public static final int KEY_PTZ_UP_START = KEY_CODE_UP;
    public static final int KEY_PTZ_UP_STO = 32;
    public static final int KEY_PTZ_DOWN_START = KEY_CODE_DOWN;
    public static final int KEY_PTZ_DOWN_STOP = 33;
    public static final int KEY_PTZ_LEFT_START = KEY_CODE_LEFT;
    public static final int KEY_PTZ_LEFT_STOP = 34;
    public static final int KEY_PTZ_RIGHT_START = KEY_CODE_RIGHT;
    public static final int KEY_PTZ_RIGHT_STOP = 35;
    public static final int KEY_PTZ_AP1_START = KEY_CODE_EDIT; /* 光圈+ */
    public static final int KEY_PTZ_AP1_STOP = 36;
    public static final int KEY_PTZ_AP2_START = KEY_CODE_PAN; /* 光圈- */
    public static final int KEY_PTZ_AP2_STOP = 37;
    public static final int KEY_PTZ_FOCUS1_START = KEY_CODE_A; /* 聚焦+ */
    public static final int KEY_PTZ_FOCUS1_STOP = 38;
    public static final int KEY_PTZ_FOCUS2_START = KEY_CODE_M; /* 聚焦- */
    public static final int KEY_PTZ_FOCUS2_STOP = 39;
    public static final int KEY_PTZ_B1_START = 40; /* 变倍+ */
    public static final int KEY_PTZ_B1_STOP = 41;
    public static final int KEY_PTZ_B2_START = 42; /* 变倍- */
    public static final int KEY_PTZ_B2_STOP = 43;
    // 9000新增
    public static final int KEY_CODE_11 = 44;
    public static final int KEY_CODE_12 = 45;
    public static final int KEY_CODE_13 = 46;
    public static final int KEY_CODE_14 = 47;
    public static final int KEY_CODE_15 = 48;
    public static final int KEY_CODE_16 = 49;
    /*************************参数配置命令 begin*******************************/
    // 用于NET_DVR_SetDVRConfig和NET_DVR_GetDVRConfig,注意其对应的配置结构
    public static final int NET_DVR_GET_DEVICECFG = 100; // 获取设备参数

    public static final int NET_DVR_SET_DEVICECFG = 101; // 设置设备参数
    public static final int NET_DVR_GET_NETCFG = 102; // 获取网络参数
    public static final int NET_DVR_SET_NETCFG = 103; // 设置网络参数
    public static final int NET_DVR_GET_PICCFG = 104; // 获取图象参数
    public static final int NET_DVR_SET_PICCFG = 105; // 设置图象参数
    public static final int NET_DVR_GET_COMPRESSCFG = 106; // 获取压缩参数
    public static final int NET_DVR_SET_COMPRESSCFG = 107; // 设置压缩参数
    public static final int NET_DVR_GET_RECORDCFG = 108; // 获取录像时间参数
    public static final int NET_DVR_SET_RECORDCFG = 109; // 设置录像时间参数
    public static final int NET_DVR_GET_DECODERCFG = 110; // 获取解码器参数
    public static final int NET_DVR_SET_DECODERCFG = 111; // 设置解码器参数
    public static final int NET_DVR_GET_RS232CFG = 112; // 获取232串口参数
    public static final int NET_DVR_SET_RS232CFG = 113; // 设置232串口参数
    public static final int NET_DVR_GET_ALARMINCFG = 114; // 获取报警输入参数
    public static final int NET_DVR_SET_ALARMINCFG = 115; // 设置报警输入参数
    public static final int NET_DVR_GET_ALARMOUTCFG = 116; // 获取报警输出参数
    public static final int NET_DVR_SET_ALARMOUTCFG = 117; // 设置报警输出参数
    public static final int NET_DVR_GET_TIMECFG = 118; // 获取DVR时间
    public static final int NET_DVR_SET_TIMECFG = 119; // 设置DVR时间
    public static final int NET_DVR_GET_PREVIEWCFG = 120; // 获取预览参数
    public static final int NET_DVR_SET_PREVIEWCFG = 121; // 设置预览参数
    public static final int NET_DVR_GET_VIDEOOUTCFG = 122; // 获取视频输出参数
    public static final int NET_DVR_SET_VIDEOOUTCFG = 123; // 设置视频输出参数
    public static final int NET_DVR_GET_USERCFG = 124; // 获取用户参数
    public static final int NET_DVR_SET_USERCFG = 125; // 设置用户参数
    public static final int NET_DVR_GET_EXCEPTIONCFG = 126; // 获取异常参数
    public static final int NET_DVR_SET_EXCEPTIONCFG = 127; // 设置异常参数
    public static final int NET_DVR_GET_ZONEANDDST = 128; // 获取时区和夏时制参数
    public static final int NET_DVR_SET_ZONEANDDST = 129; // 设置时区和夏时制参数
    public static final int NET_DVR_GET_SHOWSTRING = 130; // 获取叠加字符参数
    public static final int NET_DVR_SET_SHOWSTRING = 131; // 设置叠加字符参数
    public static final int NET_DVR_GET_EVENTCOMPCFG = 132; // 获取事件触发录像参数
    public static final int NET_DVR_SET_EVENTCOMPCFG = 133; // 设置事件触发录像参数
    public static final int NET_DVR_GET_AUXOUTCFG = 140; // 获取报警触发辅助输出设置(HS设备辅助输出2006-02-28)
    public static final int NET_DVR_SET_AUXOUTCFG = 141; // 设置报警触发辅助输出设置(HS设备辅助输出2006-02-28)
    public static final int NET_DVR_GET_PREVIEWCFG_AUX = 142; // 获取-s系列双输出预览参数(-s系列双输出2006-04-13)
    public static final int NET_DVR_SET_PREVIEWCFG_AUX = 143; // 设置-s系列双输出预览参数(-s系列双输出2006-04-13)
    public static final int NET_DVR_GET_PICCFG_EX = 200; // 获取图象参数(SDK_V14扩展命令)
    public static final int NET_DVR_SET_PICCFG_EX = 201; // 设置图象参数(SDK_V14扩展命令)
    public static final int NET_DVR_GET_USERCFG_EX = 202; // 获取用户参数(SDK_V15扩展命令)
    public static final int NET_DVR_SET_USERCFG_EX = 203; // 设置用户参数(SDK_V15扩展命令)
    public static final int NET_DVR_GET_COMPRESSCFG_EX = 204; // 获取压缩参数(SDK_V15扩展命令2006-05-15)
    public static final int NET_DVR_SET_COMPRESSCFG_EX = 205; // 设置压缩参数(SDK_V15扩展命令2006-05-15)
    public static final int NET_DVR_GET_NETAPPCFG = 222; // 获取网络应用参数 NTP/DDNS/EMAIL
    public static final int NET_DVR_SET_NETAPPCFG = 223; // 设置网络应用参数 NTP/DDNS/EMAIL
    public static final int NET_DVR_GET_NTPCFG = 224; // 获取网络应用参数 NTP
    public static final int NET_DVR_SET_NTPCFG = 225; // 设置网络应用参数 NTP
    public static final int NET_DVR_GET_DDNSCFG = 226; // 获取网络应用参数 DDNS
    public static final int NET_DVR_SET_DDNSCFG = 227; // 设置网络应用参数 DDNS
    // 对应NET_DVR_EMAILPARA
    public static final int NET_DVR_GET_EMAILCFG = 228; // 获取网络应用参数 EMAIL
    public static final int NET_DVR_SET_EMAILCFG = 229; // 设置网络应用参数 EMAIL
    public static final int NET_DVR_GET_NFSCFG = 230; /* NFS disk config */
    public static final int NET_DVR_SET_NFSCFG = 231; /* NFS disk config */
    public static final int NET_DVR_GET_SHOWSTRING_EX = 238; // 获取叠加字符参数扩展(支持8条字符)
    public static final int NET_DVR_SET_SHOWSTRING_EX = 239; // 设置叠加字符参数扩展(支持8条字符)
    public static final int NET_DVR_GET_NETCFG_OTHER = 244; // 获取网络参数
    public static final int NET_DVR_SET_NETCFG_OTHER = 245; // 设置网络参数
    // 对应NET_DVR_EMAILCFG结构
    public static final int NET_DVR_GET_EMAILPARACFG = 250; // Get EMAIL parameters
    public static final int NET_DVR_SET_EMAILPARACFG = 251; // Setup EMAIL parameters
    public static final int NET_DVR_GET_DDNSCFG_EX = 274; // 获取扩展DDNS参数
    public static final int NET_DVR_SET_DDNSCFG_EX = 275; // 设置扩展DDNS参数
    public static final int NET_DVR_SET_PTZPOS = 292; // 云台设置PTZ位置
    public static final int NET_DVR_GET_PTZPOS = 293; // 云台获取PTZ位置
    public static final int NET_DVR_GET_PTZSCOPE = 294; // 云台获取PTZ范围
    /***************************DS9000新增命令(_V30) begin *****************************/
    // 网络(NET_DVR_NETCFG_V30结构)
    public static final int NET_DVR_GET_NETCFG_V30 = 1000; // 获取网络参数

    public static final int NET_DVR_SET_NETCFG_V30 = 1001; // 设置网络参数
    // 图象(NET_DVR_PICCFG_V30结构)
    public static final int NET_DVR_GET_PICCFG_V30 = 1002; // 获取图象参数
    public static final int NET_DVR_SET_PICCFG_V30 = 1003; // 设置图象参数
    // 录像时间(NET_DVR_RECORD_V30结构)
    public static final int NET_DVR_GET_RECORDCFG_V30 = 1004; // 获取录像参数
    public static final int NET_DVR_SET_RECORDCFG_V30 = 1005; // 设置录像参数
    // 用户(NET_DVR_USER_V30结构)
    public static final int NET_DVR_GET_USERCFG_V30 = 1006; // 获取用户参数
    public static final int NET_DVR_SET_USERCFG_V30 = 1007; // 设置用户参数
    // 9000DDNS参数配置(NET_DVR_DDNSPARA_V30结构)
    public static final int NET_DVR_GET_DDNSCFG_V30 = 1010; // 获取DDNS(9000扩展)
    public static final int NET_DVR_SET_DDNSCFG_V30 = 1011; // 设置DDNS(9000扩展)
    // EMAIL功能(NET_DVR_EMAILCFG_V30结构)
    public static final int NET_DVR_GET_EMAILCFG_V30 = 1012; // 获取EMAIL参数
    public static final int NET_DVR_SET_EMAILCFG_V30 = 1013; // 设置EMAIL参数
    // 巡航参数 (NET_DVR_CRUISE_PARA结构)
    public static final int NET_DVR_GET_CRUISE = 1020;
    public static final int NET_DVR_SET_CRUISE = 1021;
    // 报警输入结构参数 (NET_DVR_ALARMINCFG_V30结构)
    public static final int NET_DVR_GET_ALARMINCFG_V30 = 1024;
    public static final int NET_DVR_SET_ALARMINCFG_V30 = 1025;
    // 报警输出结构参数 (NET_DVR_ALARMOUTCFG_V30结构)
    public static final int NET_DVR_GET_ALARMOUTCFG_V30 = 1026;
    public static final int NET_DVR_SET_ALARMOUTCFG_V30 = 1027;
    // 视频输出结构参数 (NET_DVR_VIDEOOUT_V30结构)
    public static final int NET_DVR_GET_VIDEOOUTCFG_V30 = 1028;
    public static final int NET_DVR_SET_VIDEOOUTCFG_V30 = 1029;
    // 叠加字符结构参数 (NET_DVR_SHOWSTRING_V30结构)
    public static final int NET_DVR_GET_SHOWSTRING_V30 = 1030;
    public static final int NET_DVR_SET_SHOWSTRING_V30 = 1031;
    // 异常结构参数 (NET_DVR_EXCEPTION_V30结构)
    public static final int NET_DVR_GET_EXCEPTIONCFG_V30 = 1034;
    public static final int NET_DVR_SET_EXCEPTIONCFG_V30 = 1035;
    // 串口232结构参数 (NET_DVR_RS232CFG_V30结构)
    public static final int NET_DVR_GET_RS232CFG_V30 = 1036;
    public static final int NET_DVR_SET_RS232CFG_V30 = 1037;
    // 压缩参数 (NET_DVR_COMPRESSIONCFG_V30结构)
    public static final int NET_DVR_GET_COMPRESSCFG_V30 = 1040;
    public static final int NET_DVR_SET_COMPRESSCFG_V30 = 1041;
    // 获取485解码器参数 (NET_DVR_DECODERCFG_V30结构)
    public static final int NET_DVR_GET_DECODERCFG_V30 = 1042; // 获取解码器参数
    public static final int NET_DVR_SET_DECODERCFG_V30 = 1043; // 设置解码器参数
    // 获取预览参数 (NET_DVR_PREVIEWCFG_V30结构)
    public static final int NET_DVR_GET_PREVIEWCFG_V30 = 1044; // 获取预览参数
    public static final int NET_DVR_SET_PREVIEWCFG_V30 = 1045; // 设置预览参数
    // 辅助预览参数 (NET_DVR_PREVIEWCFG_AUX_V30结构)
    public static final int NET_DVR_GET_PREVIEWCFG_AUX_V30 = 1046; // 获取辅助预览参数
    public static final int NET_DVR_SET_PREVIEWCFG_AUX_V30 = 1047; // 设置辅助预览参数
    // IP接入配置参数 （NET_DVR_IPPARACFG结构）
    public static final int NET_DVR_GET_IPPARACFG = 1048; // 获取IP接入配置信息
    public static final int NET_DVR_SET_IPPARACFG = 1049; // 设置IP接入配置信息
    // IP报警输入接入配置参数 （NET_DVR_IPALARMINCFG结构）
    public static final int NET_DVR_GET_IPALARMINCFG = 1050; // 获取IP报警输入接入配置信息
    public static final int NET_DVR_SET_IPALARMINCFG = 1051; // 设置IP报警输入接入配置信息
    // IP报警输出接入配置参数 （NET_DVR_IPALARMOUTCFG结构）
    public static final int NET_DVR_GET_IPALARMOUTCFG = 1052; // 获取IP报警输出接入配置信息
    public static final int NET_DVR_SET_IPALARMOUTCFG = 1053; // 设置IP报警输出接入配置信息
    // 硬盘管理的参数获取 (NET_DVR_HDCFG结构)
    public static final int NET_DVR_GET_HDCFG = 1054; // 获取硬盘管理配置参数
    public static final int NET_DVR_SET_HDCFG = 1055; // 设置硬盘管理配置参数
    // 盘组管理的参数获取 (NET_DVR_HDGROUP_CFG结构)
    public static final int NET_DVR_GET_HDGROUP_CFG = 1056; // 获取盘组管理配置参数
    public static final int NET_DVR_SET_HDGROUP_CFG = 1057; // 设置盘组管理配置参数
    // 设备编码类型配置(NET_DVR_COMPRESSION_AUDIO结构)
    public static final int NET_DVR_GET_COMPRESSCFG_AUD = 1058; // 获取设备语音对讲编码参数
    public static final int NET_DVR_SET_COMPRESSCFG_AUD = 1059; // 设置设备语音对讲编码参数
    /***************************DS9000新增命令(_V30) end *****************************/
    /*************************参数配置命令 end*******************************/
    /*******************查找文件和日志函数返回值*************************/
    public static final int NET_DVR_FILE_SUCCESS = 1000; // 获得文件信息

    public static final int NET_DVR_FILE_NOFIND = 1001; // 没有文件
    public static final int NET_DVR_ISFINDING = 1002; // 正在查找文件
    public static final int NET_DVR_NOMOREFILE = 1003; // 查找文件时没有更多的文件
    public static final int NET_DVR_FILE_EXCEPTION = 1004; // 查找文件时异常
    /*********************回调函数类型 begin************************/
    public static final int COMM_ALARM = 0x1100; // 8000报警信息主动上传

    public static final int COMM_TRADEINFO = 0x1500; // ATMDVR主动上传交易信息
    public static final int COMM_ALARM_V30 = 0x4000; // 9000报警信息主动上传
    public static final int COMM_IPCCFG = 0x4001; // 9000设备IPC接入配置改变报警信息主动上传
    /*************操作异常类型(消息方式, 回调方式(保留))****************/
    public static final int EXCEPTION_EXCHANGE = 0x8000; // 用户交互时异常

    public static final int EXCEPTION_AUDIOEXCHANGE = 0x8001; // 语音对讲异常
    public static final int EXCEPTION_ALARM = 0x8002; // 报警异常
    public static final int EXCEPTION_PREVIEW = 0x8003; // 网络预览异常
    public static final int EXCEPTION_SERIAL = 0x8004; // 透明通道异常
    public static final int EXCEPTION_RECONNECT = 0x8005; // 预览时重连
    public static final int EXCEPTION_ALARMRECONNECT = 0x8006; // 报警时重连
    public static final int EXCEPTION_SERIALRECONNECT = 0x8007; // 透明通道重连
    public static final int EXCEPTION_PLAYBACK = 0x8010; // 回放异常
    public static final int EXCEPTION_DISKFMT = 0x8011; // 硬盘格式化
    /********************预览回调函数*********************/
    public static final int NET_DVR_SYSHEAD = 1; // 系统头数据

    public static final int NET_DVR_STREAMDATA = 2; // 视频流数据（包括复合流和音视频分开的视频流数据）
    public static final int NET_DVR_AUDIOSTREAMDATA = 3; // 音频流数据
    public static final int NET_DVR_STD_VIDEODATA = 4; // 标准视频流数据
    public static final int NET_DVR_STD_AUDIODATA = 5; // 标准音频流数据
    // 回调预览中的状态和消息
    public static final int NET_DVR_REALPLAYEXCEPTION = 111; // 预览异常
    public static final int NET_DVR_REALPLAYNETCLOSE = 112; // 预览时连接断开
    public static final int NET_DVR_REALPLAY5SNODATA = 113; // 预览5s没有收到数据
    public static final int NET_DVR_REALPLAYRECONNECT = 114; // 预览重连
    /********************回放回调函数*********************/
    public static final int NET_DVR_PLAYBACKOVER = 101; // 回放数据播放完毕

    public static final int NET_DVR_PLAYBACKEXCEPTION = 102; // 回放异常
    public static final int NET_DVR_PLAYBACKNETCLOSE = 103; // 回放时候连接断开
    public static final int NET_DVR_PLAYBACK5SNODATA = 104; // 回放5s没有收到数据
    /*********************回调函数类型 end************************/
    // 设备型号(DVR类型)
    /* 设备类型 */
    public static final int DVR = 1; /*对尚未定义的dvr类型返回NETRET_DVR*/

    public static final int ATMDVR = 2; /*atm dvr*/
    public static final int DVS = 3; /*DVS*/
    public static final int DEC = 4; /* 6001D */
    public static final int ENC_DEC = 5; /* 6001F */
    public static final int DVR_HC = 6; /*8000HC*/
    public static final int DVR_HT = 7; /*8000HT*/
    public static final int DVR_HF = 8; /*8000HF*/
    public static final int DVR_HS = 9; /* 8000HS DVR(no audio) */
    public static final int DVR_HTS = 10; /* 8016HTS DVR(no audio) */
    public static final int DVR_HB = 11; /* HB DVR(SATA HD) */
    public static final int DVR_HCS = 12; /* 8000HCS DVR */
    public static final int DVS_A = 13; /* 带ATA硬盘的DVS */
    public static final int DVR_HC_S = 14; /* 8000HC-S */
    public static final int DVR_HT_S = 15; /* 8000HT-S */
    public static final int DVR_HF_S = 16; /* 8000HF-S */
    public static final int DVR_HS_S = 17; /* 8000HS-S */
    public static final int ATMDVR_S = 18; /* ATM-S */
    public static final int LOWCOST_DVR = 19; /*7000H系列*/
    public static final int DEC_MAT = 20; /*多路解码器*/
    public static final int DVR_MOBILE = 21; /* mobile DVR */
    public static final int DVR_HD_S = 22; /* 8000HD-S */
    public static final int DVR_HD_SL = 23; /* 8000HD-SL */
    public static final int DVR_HC_SL = 24; /* 8000HC-SL */
    public static final int DVR_HS_ST = 25; /* 8000HS_ST */
    public static final int DVS_HW = 26; /* 6000HW */
    public static final int IPCAM = 30; /*IP 摄像机*/
    public static final int MEGA_IPCAM = 31; /*X52MF系列,752MF,852MF*/
    public static final int IPCAM_X62MF = 32; /*X62MF系列可接入9000设备,762MF,862MF*/
    public static final int IPDOME = 40; /*IP标清快球*/
    public static final int MEGA_IPDOME = 41; /*IP高清快球*/
    public static final int IPMOD = 50; /*IP 模块*/
    public static final int DS71XX_H = 71; /* DS71XXH_S */
    public static final int DS72XX_H_S = 72; /* DS72XXH_S */
    public static final int DS73XX_H_S = 73; /* DS73XXH_S */
    public static final int DS81XX_HS_S = 81; /* DS81XX_HS_S */
    public static final int DS81XX_HL_S = 82; /* DS81XX_HL_S */
    public static final int DS81XX_HC_S = 83; /* DS81XX_HC_S */
    public static final int DS81XX_HD_S = 84; /* DS81XX_HD_S */
    public static final int DS81XX_HE_S = 85; /* DS81XX_HE_S */
    public static final int DS81XX_HF_S = 86; /* DS81XX_HF_S */
    public static final int DS81XX_AH_S = 87; /* DS81XX_AH_S */
    public static final int DS81XX_AHF_S = 88; /* DS81XX_AHF_S */
    public static final int DS90XX_HF_S = 90; /*DS90XX_HF_S*/
    public static final int DS91XX_HF_S = 91; /*DS91XX_HF_S*/
    public static final int DS91XX_HD_S = 92; /*91XXHD-S(MD)*/

    /* 操作 */
    // 主类型
    public static final int MAJOR_OPERATION = 0x3;
    // 次类型
    public static final int MINOR_START_DVR = 0x41; /* 开机 */
    public static final int MINOR_STOP_DVR = 0x42; /* 关机 */
    public static final int MINOR_STOP_ABNORMAL = 0x43; /* 异常关机 */
    public static final int MINOR_REBOOT_DVR = 0x44; /*本地重启设备*/
    public static final int MINOR_LOCAL_LOGIN = 0x50; /* 本地登陆 */
    public static final int MINOR_LOCAL_LOGOUT = 0x51; /* 本地注销登陆 */
    public static final int MINOR_LOCAL_CFG_PARM = 0x52; /* 本地配置参数 */
    public static final int MINOR_LOCAL_PLAYBYFILE = 0x53; /* 本地按文件回放或下载 */
    public static final int MINOR_LOCAL_PLAYBYTIME = 0x54; /* 本地按时间回放或下载*/
    public static final int MINOR_LOCAL_START_REC = 0x55; /* 本地开始录像 */
    public static final int MINOR_LOCAL_STOP_REC = 0x56; /* 本地停止录像 */
    public static final int MINOR_LOCAL_PTZCTRL = 0x57; /* 本地云台控制 */
    public static final int MINOR_LOCAL_PREVIEW = 0x58; /* 本地预览 (保留不使用)*/
    public static final int MINOR_LOCAL_MODIFY_TIME = 0x59; /* 本地修改时间(保留不使用) */
    public static final int MINOR_LOCAL_UPGRADE = 0x5a; /* 本地升级 */
    public static final int MINOR_LOCAL_RECFILE_OUTPUT = 0x5b; /* 本地备份录象文件 */
    public static final int MINOR_LOCAL_FORMAT_HDD = 0x5c; /* 本地初始化硬盘 */
    public static final int MINOR_LOCAL_CFGFILE_OUTPUT = 0x5d; /* 导出本地配置文件 */
    public static final int MINOR_LOCAL_CFGFILE_INPUT = 0x5e; /* 导入本地配置文件 */
    public static final int MINOR_LOCAL_COPYFILE = 0x5f; /* 本地备份文件 */
    public static final int MINOR_LOCAL_LOCKFILE = 0x60; /* 本地锁定录像文件 */
    public static final int MINOR_LOCAL_UNLOCKFILE = 0x61; /* 本地解锁录像文件 */
    public static final int MINOR_LOCAL_DVR_ALARM = 0x62; /* 本地手动清除和触发报警*/
    public static final int MINOR_IPC_ADD = 0x63; /* 本地添加IPC */
    public static final int MINOR_IPC_DEL = 0x64; /* 本地删除IPC */
    public static final int MINOR_IPC_SET = 0x65; /* 本地设置IPC */
    public static final int MINOR_LOCAL_START_BACKUP = 0x66; /* 本地开始备份 */
    public static final int MINOR_LOCAL_STOP_BACKUP = 0x67; /* 本地停止备份*/
    public static final int MINOR_LOCAL_COPYFILE_START_TIME = 0x68; /* 本地备份开始时间*/
    public static final int MINOR_LOCAL_COPYFILE_END_TIME = 0x69; /* 本地备份结束时间*/
    public static final int MINOR_REMOTE_LOGIN = 0x70; /* 远程登录 */
    public static final int MINOR_REMOTE_LOGOUT = 0x71; /* 远程注销登陆 */
    public static final int MINOR_REMOTE_START_REC = 0x72; /* 远程开始录像 */
    public static final int MINOR_REMOTE_STOP_REC = 0x73; /* 远程停止录像 */
    public static final int MINOR_START_TRANS_CHAN = 0x74; /* 开始透明传输 */
    public static final int MINOR_STOP_TRANS_CHAN = 0x75; /* 停止透明传输 */
    public static final int MINOR_REMOTE_GET_PARM = 0x76; /* 远程获取参数 */
    public static final int MINOR_REMOTE_CFG_PARM = 0x77; /* 远程配置参数 */
    public static final int MINOR_REMOTE_GET_STATUS = 0x78; /* 远程获取状态 */
    public static final int MINOR_REMOTE_ARM = 0x79; /* 远程布防 */
    public static final int MINOR_REMOTE_DISARM = 0x7a; /* 远程撤防 */
    public static final int MINOR_REMOTE_REBOOT = 0x7b; /* 远程重启 */
    public static final int MINOR_START_VT = 0x7c; /* 开始语音对讲 */
    public static final int MINOR_STOP_VT = 0x7d; /* 停止语音对讲 */
    public static final int MINOR_REMOTE_UPGRADE = 0x7e; /* 远程升级 */
    public static final int MINOR_REMOTE_PLAYBYFILE = 0x7f; /* 远程按文件回放 */
    public static final int MINOR_REMOTE_PLAYBYTIME = 0x80; /* 远程按时间回放 */
    public static final int MINOR_REMOTE_PTZCTRL = 0x81; /* 远程云台控制 */
    public static final int MINOR_REMOTE_FORMAT_HDD = 0x82; /* 远程格式化硬盘 */
    public static final int MINOR_REMOTE_STOP = 0x83; /* 远程关机 */
    public static final int MINOR_REMOTE_LOCKFILE = 0x84; /* 远程锁定文件 */
    public static final int MINOR_REMOTE_UNLOCKFILE = 0x85; /* 远程解锁文件 */
    public static final int MINOR_REMOTE_CFGFILE_OUTPUT = 0x86; /* 远程导出配置文件 */
    public static final int MINOR_REMOTE_CFGFILE_INTPUT = 0x87; /* 远程导入配置文件 */
    public static final int MINOR_REMOTE_RECFILE_OUTPUT = 0x88; /* 远程导出录象文件 */
    public static final int MINOR_REMOTE_DVR_ALARM = 0x89; /* 远程手动清除和触发报警*/
    public static final int MINOR_REMOTE_IPC_ADD = 0x8a; /* 远程添加IPC */
    public static final int MINOR_REMOTE_IPC_DEL = 0x8b; /* 远程删除IPC */
    public static final int MINOR_REMOTE_IPC_SET = 0x8c; /* 远程设置IPC */
    public static final int MINOR_REBOOT_VCA_LIB = 0x8d; /*重启智能库*/

    /*日志附加信息*/
    // 主类型
    public static final int MAJOR_INFORMATION = 0x4; /*附加信息*/
    // 次类型
    public static final int MINOR_HDD_INFO = 0xa1; /*硬盘信息*/
    public static final int MINOR_SMART_INFO = 0xa2; /*SMART信息*/
    public static final int MINOR_REC_START = 0xa3; /*开始录像*/
    public static final int MINOR_REC_STOP = 0xa4; /*停止录像*/
    public static final int MINOR_REC_OVERDUE = 0xa5; /*过期录像删除*/
    public static final int MINOR_LINK_START = 0xa6; // ivms，多路解码器等连接前端设备
    public static final int MINOR_LINK_STOP = 0xa7; // ivms，多路解码器等断开前端设备　
    // 当日志的主类型为MAJOR_OPERATION=03，次类型为MINOR_LOCAL_CFG_PARM=0x52或者MINOR_REMOTE_GET_PARM=0x76或者MINOR_REMOTE_CFG_PARM=0x77时，dwParaType:参数类型有效，其含义如下：
    public static final int PARA_VIDEOOUT = 0x1;
    public static final int PARA_IMAGE = 0x2;
    public static final int PARA_ENCODE = 0x4;
    public static final int PARA_NETWORK = 0x8;
    public static final int PARA_ALARM = 0x10;
    public static final int PARA_EXCEPTION = 0x20;
    public static final int PARA_DECODER = 0x40; /*解码器*/
    public static final int PARA_RS232 = 0x80;
    public static final int PARA_PREVIEW = 0x100;
    public static final int PARA_SECURITY = 0x200;
    public static final int PARA_DATETIME = 0x400;
    public static final int PARA_FRAMETYPE = 0x800; /*帧格式*/
    public static final int PARA_VCA_RULE = 0x1000; // 行为规则
    // SDK_V222
    // 智能设备类型
    public static final int DS6001_HF_B = 60; // 行为分析：DS6001-HF/B
    public static final int DS6001_HF_P = 61; // 车牌识别：DS6001-HF/P
    public static final int DS6002_HF_B = 62; // 双机跟踪：DS6002-HF/B
    public static final int DS6101_HF_B = 63; // 行为分析：DS6101-HF/B
    public static final int IVMS_2000 = 64; // 智能分析仪
    public static final int DS9000_IVS = 65; // 9000系列智能DVR
    public static final int DS8004_AHL_A = 66; // 智能ATM, DS8004AHL-S/A
    public static final int DS6101_HF_P = 67; // 车牌识别：DS6101-HF/P
    // 能力获取命令
    public static final int VCA_DEV_ABILITY = 0x100; // 设备智能分析的总能力
    public static final int VCA_CHAN_ABILITY = 0x110; // 行为分析能力
    // 获取/设置大接口参数配置命令
    // 车牌识别（NET_VCA_PLATE_CFG）;
    public static final int NET_DVR_SET_PLATECFG = 150; // 设置车牌识别参数

    public static final int NET_DVR_GET_PLATECFG = 151; // 获取车牌识别参数
    // 行为对应（NET_VCA_RULECFG）
    public static final int NET_DVR_SET_RULECFG = 152; // 设置行为分析规则
    public static final int NET_DVR_GET_RULECFG = 153; // 获取行为分析规则,
    // 双摄像机标定参数（NET_DVR_LF_CFG）
    public static final int NET_DVR_SET_LF_CFG = 160; // 设置双摄像机的配置参数
    public static final int NET_DVR_GET_LF_CFG = 161; // 获取双摄像机的配置参数
    // 智能分析仪取流配置结构
    public static final int NET_DVR_SET_IVMS_STREAMCFG = 162; // 设置智能分析仪取流参数
    public static final int NET_DVR_GET_IVMS_STREAMCFG = 163; // 获取智能分析仪取流参数
    // 智能控制参数结构
    public static final int NET_DVR_SET_VCA_CTRLCFG = 164; // 设置智能控制参数
    public static final int NET_DVR_GET_VCA_CTRLCFG = 165; // 获取智能控制参数
    // 屏蔽区域NET_VCA_MASK_REGION_LIST
    public static final int NET_DVR_SET_VCA_MASK_REGION = 166; // 设置屏蔽区域参数
    public static final int NET_DVR_GET_VCA_MASK_REGION = 167; // 获取屏蔽区域参数
    // ATM进入区域 NET_VCA_ENTER_REGION
    public static final int NET_DVR_SET_VCA_ENTER_REGION = 168; // 设置进入区域参数
    public static final int NET_DVR_GET_VCA_ENTER_REGION = 169; // 获取进入区域参数
    // 标定线配置NET_VCA_LINE_SEGMENT_LIST
    public static final int NET_DVR_SET_VCA_LINE_SEGMENT = 170; // 设置标定线
    public static final int NET_DVR_GET_VCA_LINE_SEGMENT = 171; // 获取标定线
    // ivms屏蔽区域NET_IVMS_MASK_REGION_LIST
    public static final int NET_DVR_SET_IVMS_MASK_REGION = 172; // 设置IVMS屏蔽区域参数
    public static final int NET_DVR_GET_IVMS_MASK_REGION = 173; // 获取IVMS屏蔽区域参数
    // ivms进入检测区域NET_IVMS_ENTER_REGION
    public static final int NET_DVR_SET_IVMS_ENTER_REGION = 174; // 设置IVMS进入区域参数
    public static final int NET_DVR_GET_IVMS_ENTER_REGION = 175; // 获取IVMS进入区域参数
    public static final int NET_DVR_SET_IVMS_BEHAVIORCFG = 176; // 设置智能分析仪行为规则参数
    public static final int NET_DVR_GET_IVMS_BEHAVIORCFG = 177; // 获取智能分析仪行为规则参数

    /**********************设备类型 end***********************/

    /*************************************************
     * 参数配置结构、参数(其中_V30为9000新增)
     **************************************************/

    /////////////////////////////////////////////////////////////////////////
    // 校时结构参数
    public static class NET_DVR_TIME extends Structure { // 校时结构参数
        public int dwYear; // 年
        public int dwMonth; // 月
        public int dwDay; // 日
        public int dwHour; // 时
        public int dwMinute; // 分
        public int dwSecond; // 秒

        public String toString() {
            return "NET_DVR_TIME.dwYear: "
                    + dwYear
                    + "\n"
                    + "NET_DVR_TIME.dwMonth: \n"
                    + dwMonth
                    + "\n"
                    + "NET_DVR_TIME.dwDay: \n"
                    + dwDay
                    + "\n"
                    + "NET_DVR_TIME.dwHour: \n"
                    + dwHour
                    + "\n"
                    + "NET_DVR_TIME.dwMinute: \n"
                    + dwMinute
                    + "\n"
                    + "NET_DVR_TIME.dwSecond: \n"
                    + dwSecond;
        }

        // 用于列表中显示
        public String toStringTime() {
            return String.format("%02d/%02d/%02d%02d:%02d:%02d", dwYear, dwMonth, dwDay, dwHour, dwMinute, dwSecond);
        }

        // 存储文件名使用
        public String toStringTitle() {
            return String.format("Time%02d%02d%02d%02d%02d%02d", dwYear, dwMonth, dwDay, dwHour, dwMinute, dwSecond);
        }
    }

    public static class NET_DVR_SCHEDTIME extends Structure {

        public byte byStartHour; // 开始时间
        public byte byStartMin;
        public byte byStopHour; // 结束时间
        public byte byStopMin;
    }

    public static class NET_DVR_HANDLEEXCEPTION_V30 extends Structure {

        public int
                dwHandleType; /*处理方式,处理方式的"或"结果*/ /*0x00: 无响应*/ /*0x01: 监视器上警告*/ /*0x02: 声音警告*/ /*0x04: 上传中心*/ /*0x08: 触发报警输出*/ /*0x20: 触发抓图*/ // (JPEG定制)
        public byte[] byRelAlarmOut = new byte[MAX_ALARMOUT_V30]; // 报警触发的输出通道,报警触发的输出,为1表示触发该输出
    }

    // 报警和异常处理结构(子结构)(多处使用)
    public static class NET_DVR_HANDLEEXCEPTION extends Structure {

        public int
                dwHandleType; /*处理方式,处理方式的"或"结果*/ /*0x00: 无响应*/ /*0x01: 监视器上警告*/ /*0x02: 声音警告*/ /*0x04: 上传中心*/ /*0x08: 触发报警输出*/ /*0x20: 触发抓图*/ // (JPEG定制)
        public byte[] byRelAlarmOut = new byte[MAX_ALARMOUT]; // 报警触发的输出通道,报警触发的输出,为1表示触发该输出
    }

    // DVR设备参数
    public static class NET_DVR_DEVICECFG extends Structure {

        public int dwSize;
        public byte[] sDVRName = new byte[NAME_LEN]; // DVR名称
        public int dwDVRID; // DVR ID,用于遥控器 //V1.4(0-99), V1.5(0-255)
        public int dwRecycleRecord; // 是否循环录像,0:不是; 1:是
        // 以下不可更改
        public byte[] sSerialNumber = new byte[SERIALNO_LEN]; // 序列号
        public int dwSoftwareVersion; // 软件版本号,高16位是主版本,低16位是次版本
        public int dwSoftwareBuildDate; // 软件生成日期,0xYYYYMMDD
        public int dwDSPSoftwareVersion; // DSP软件版本,高16位是主版本,低16位是次版本
        public int dwDSPSoftwareBuildDate; // DSP软件生成日期,0xYYYYMMDD
        public int dwPanelVersion; // 前面板版本,高16位是主版本,低16位是次版本
        public int dwHardwareVersion; // 硬件版本,高16位是主版本,低16位是次版本
        public byte byAlarmInPortNum; // DVR报警输入个数
        public byte byAlarmOutPortNum; // DVR报警输出个数
        public byte byRS232Num; // DVR 232串口个数
        public byte byRS485Num; // DVR 485串口个数
        public byte byNetworkPortNum; // 网络口个数
        public byte byDiskCtrlNum; // DVR 硬盘控制器个数
        public byte byDiskNum; // DVR 硬盘个数
        public byte byDVRType; // DVR类型, 1:DVR 2:ATM DVR 3:DVS ......
        public byte byChanNum; // DVR 通道个数
        public byte byStartChan; // 起始通道号,例如DVS-1,DVR - 1
        public byte byDecordChans; // DVR 解码路数
        public byte byVGANum; // VGA口的个数
        public byte byUSBNum; // USB口的个数
        public byte byAuxoutNum; // 辅口的个数
        public byte byAudioNum; // 语音口的个数
        public byte byIPChanNum; // 最大数字通道数
    }

    public static class NET_DVR_IPADDR extends Structure {

        public byte[] sIpV4 = new byte[16];
        public byte[] byRes = new byte[128];

        @Override
        public String toString() {
            return "NET_DVR_IPADDR.sIpV4: "
                    + new String(sIpV4)
                    + "\n"
                    + "NET_DVR_IPADDR.byRes: "
                    + new String(byRes)
                    + "\n";
        }
    }

    // 网络数据结构(子结构)(9000扩展)
    public static class NET_DVR_ETHERNET_V30 extends Structure {

        public NET_DVR_IPADDR struDVRIP;
        public NET_DVR_IPADDR struDVRIPMask;
        public int dwNetInterface;
        public short wDVRPort;
        public short wMTU;
        public byte[] byMACAddr = new byte[6];

        @Override
        public String toString() {
            return "NET_DVR_ETHERNET_V30.struDVRIP: \n"
                    + struDVRIP
                    + "\n"
                    + "NET_DVR_ETHERNET_V30.struDVRIPMask: \n"
                    + struDVRIPMask
                    + "\n"
                    + "NET_DVR_ETHERNET_V30.dwNetInterface: "
                    + dwNetInterface
                    + "\n"
                    + "NET_DVR_ETHERNET_V30.wDVRPort: "
                    + wDVRPort
                    + "\n"
                    + "NET_DVR_ETHERNET_V30.wMTU: "
                    + wMTU
                    + "\n"
                    + "NET_DVR_ETHERNET_V30.byMACAddr: "
                    + new String(byMACAddr)
                    + "\n";
        }
    }

    public static class NET_DVR_ETHERNET extends Structure { // 网络数据结构(子结构)
        public byte[] sDVRIP = new byte[16]; // DVR IP地址
        public byte[] sDVRIPMask = new byte[16]; // DVR IP地址掩码
        public int dwNetInterface; // 网络接口 1-10MBase-T 2-10MBase-T全双工 3-100MBase-TX 4-100M全双工
        // 5-10M/100M自适应
        public short wDVRPort; // 端口号
        public byte[] byMACAddr = new byte[MACADDR_LEN]; // 服务器的物理地址
    }

    public static class NET_DVR_PPPOECFG extends Structure { // PPPoe
        public int dwPPPoE;
        public byte[] sPPPoEUser = new byte[32];
        public byte[] sPPPoEPassword = new byte[16];
        public NET_DVR_IPADDR struPPPoEIP;
    }

    public static class NET_DVR_NETCFG_V30 extends Structure {

        public int dwSize;
        public NET_DVR_ETHERNET_V30[] struEtherNet = new NET_DVR_ETHERNET_V30[2];
        public NET_DVR_IPADDR[] struRes1 = new NET_DVR_IPADDR[2];
        public NET_DVR_IPADDR struAlarmHostIpAddr;
        public short[] wRes2 = new short[2];
        public short wAlarmHostIpPort;
        public byte byUseDhcp;
        public byte byRes3;
        public NET_DVR_IPADDR struDnsServer1IpAddr;
        public NET_DVR_IPADDR struDnsServer2IpAddr;
        public byte[] byIpResolver = new byte[64];
        public short wIpResolverPort;
        public short wHttpPortNo;
        public NET_DVR_IPADDR struMulticastIpAddr;
        public NET_DVR_IPADDR struGatewayIpAddr;
        public NET_DVR_PPPOECFG struPPPoE;
        public byte[] byRes = new byte[64];

        @Override
        public String toString() {
            return "NET_DVR_NETCFG_V30.dwSize: "
                    + dwSize
                    + "\n"
                    + "NET_DVR_NETCFG_V30.struEtherNet[0]: \n"
                    + struEtherNet[0]
                    + "\n"
                    + "NET_DVR_NETCFG_V30.struAlarmHostIpAddr: \n"
                    + struAlarmHostIpAddr
                    + "\n"
                    + "NET_DVR_NETCFG_V30.wAlarmHostIpPort: "
                    + wAlarmHostIpPort
                    + "\n"
                    + "NET_DVR_NETCFG_V30.wHttpPortNo: "
                    + wHttpPortNo
                    + "\n"
                    + "NET_DVR_NETCFG_V30.struGatewayIpAddr: \n"
                    + struGatewayIpAddr
                    + "\n";
        }
    }

    public static class NET_DVR_NETCFG extends Structure { // 网络配置结构
        public int dwSize;
        public NET_DVR_ETHERNET[] struEtherNet = new NET_DVR_ETHERNET[MAX_ETHERNET]; /* 以太网口 */
        public byte[] sManageHostIP = new byte[16]; // 远程管理主机地址
        public short wManageHostPort; // 远程管理主机端口号
        public byte[] sIPServerIP = new byte[16]; // IPServer服务器地址
        public byte[] sMultiCastIP = new byte[16]; // 多播组地址
        public byte[] sGatewayIP = new byte[16]; // 网关地址
        public byte[] sNFSIP = new byte[16]; // NFS主机IP地址
        public byte[] sNFSDirectory = new byte[PATHNAME_LEN]; // NFS目录
        public int dwPPPOE; // 0-不启用,1-启用
        public byte[] sPPPoEUser = new byte[NAME_LEN]; // PPPoE用户名
        public byte[] sPPPoEPassword = new byte[PASSWD_LEN]; // PPPoE密码
        public byte[] sPPPoEIP = new byte[16]; // PPPoE IP地址(只读)
    }

    // 通道图象结构
    public static class NET_DVR_SCHEDTIMEWEEK extends Structure {

        public NET_DVR_SCHEDTIME[] struAlarmTime = new NET_DVR_SCHEDTIME[8];
    }

    public static class byte96 extends Structure {

        public byte[] byMotionScope = new byte[96];
    }

    public static class NET_DVR_MOTION_V30 extends Structure { // 移动侦测(子结构)(9000扩展)
        public byte96[] byMotionScope = new byte96[64]; /*侦测区域,0-96位,表示64行,共有96*64个小宏块,为1表示是移动侦测区域,0-表示不是*/
        public byte byMotionSensitive; /*移动侦测灵敏度, 0 - 5,越高越灵敏,oxff关闭*/
        public byte byEnableHandleMotion; /* 是否处理移动侦测 0－否 1－是*/
        public byte byPrecision; /* 移动侦测算法的进度: 0--16*16, 1--32*32, 2--64*64 ... */
        public byte reservedData;
        public NET_DVR_HANDLEEXCEPTION_V30 struMotionHandleType; /* 处理方式 */
        public NET_DVR_SCHEDTIMEWEEK[] struAlarmTime = new NET_DVR_SCHEDTIMEWEEK[MAX_DAYS]; /*布防时间*/
        public byte[] byRelRecordChan = new byte[64]; /* 报警触发的录象通道*/
    }

    public static class NET_DVR_MOTION extends Structure { // 移动侦测(子结构)
        byte[][] byMotionScope = new byte[18][22]; /*侦测区域,共有22*18个小宏块,为1表示改宏块是移动侦测区域,0-表示不是*/
        byte byMotionSensitive; /*移动侦测灵敏度, 0 - 5,越高越灵敏,0xff关闭*/
        byte byEnableHandleMotion; /* 是否处理移动侦测 */
        byte[] reservedData = new byte[2];
        NET_DVR_HANDLEEXCEPTION strMotionHandleType; /* 处理方式 */
        byte[] byRelRecordChan = new byte[MAX_CHANNUM]; // 报警触发的录象通道,为1表示触发该通道
    }

    public static class NET_DVR_HIDEALARM_V30 extends Structure { // 遮挡报警
        public int dwEnableHideAlarm; /* 是否启动遮挡报警 ,0-否,1-低灵敏度 2-中灵敏度 3-高灵敏度*/
        public short wHideAlarmAreaTopLeftX; /* 遮挡区域的x坐标 */
        public short wHideAlarmAreaTopLeftY; /* 遮挡区域的y坐标 */
        public short wHideAlarmAreaWidth; /* 遮挡区域的宽 */
        public short wHideAlarmAreaHeight; /*遮挡区域的高*/
        public NET_DVR_HANDLEEXCEPTION_V30 strHideAlarmHandleType; /* 处理方式 */
        public NET_DVR_SCHEDTIMEWEEK[] struAlarmTime = new NET_DVR_SCHEDTIMEWEEK[MAX_DAYS]; // 布防时间
    }

    public static class NET_DVR_HIDEALARM extends Structure { // 遮挡报警(子结构)  区域大小704*576
        public int dwEnableHideAlarm; /* 是否启动遮挡报警 ,0-否,1-低灵敏度 2-中灵敏度 3-高灵敏度*/
        public short wHideAlarmAreaTopLeftX; /* 遮挡区域的x坐标 */
        public short wHideAlarmAreaTopLeftY; /* 遮挡区域的y坐标 */
        public short wHideAlarmAreaWidth; /* 遮挡区域的宽 */
        public short wHideAlarmAreaHeight; /*遮挡区域的高*/
        public NET_DVR_HANDLEEXCEPTION strHideAlarmHandleType; /* 处理方式 */
    }

    public static class NET_DVR_VILOST_V30 extends Structure { // 信号丢失报警(子结构)(9000扩展)

        public byte byEnableHandleVILost; /* 是否处理信号丢失报警 */
        public NET_DVR_HANDLEEXCEPTION_V30 strVILostHandleType; /* 处理方式 */
        public NET_DVR_SCHEDTIMEWEEK[] struAlarmTime = new NET_DVR_SCHEDTIMEWEEK[MAX_DAYS]; // 布防时间
    }

    public static class NET_DVR_VILOST extends Structure { // 信号丢失报警(子结构)

        byte byEnableHandleVILost; /* 是否处理信号丢失报警 */
        NET_DVR_HANDLEEXCEPTION strVILostHandleType; /* 处理方式 */
    }

    public static class NET_DVR_SHELTER extends Structure { // 遮挡区域(子结构)

        public short wHideAreaTopLeftX; /* 遮挡区域的x坐标 */
        public short wHideAreaTopLeftY; /* 遮挡区域的y坐标 */
        public short wHideAreaWidth; /* 遮挡区域的宽 */
        public short wHideAreaHeight; /* 遮挡区域的高*/
    }

    public static class NET_DVR_COLOR extends Structure {

        public byte byBrightness; /*亮度,0-255*/
        public byte byContrast; /*对比度,0-255*/
        public byte bySaturation; /*饱和度,0-255*/
        public byte byHue; /*色调,0-255*/
    }

    public static class NET_DVR_VICOLOR extends Structure {

        public NET_DVR_COLOR[] struColor = new NET_DVR_COLOR[MAX_TIMESEGMENT_V30]; /*图象参数(第一个有效，其他三个保留)*/
        public NET_DVR_SCHEDTIME[] struHandleTime = new NET_DVR_SCHEDTIME[MAX_TIMESEGMENT_V30]; /*处理时间段(保留)*/
    }
    ;

    public static class NET_DVR_PICCFG_V30 extends Structure {

        public int dwSize;
        public byte[] sChanName = new byte[NAME_LEN];
        public int dwVideoFormat; /* 只读 视频制式 1-NTSC 2-PAL*/
        public NET_DVR_VICOLOR struViColor; // 图像参数按时间段设置
        public int dwShowChanName; // 预览的图象上是否显示通道名称,0-不显示,1-显示 区域大小704*576
        public short wShowNameTopLeftX; /* 通道名称显示位置的x坐标 */
        public short wShowNameTopLeftY; /* 通道名称显示位置的y坐标 */
        public NET_DVR_VILOST_V30 struVILost; // 视频信号丢失报警
        public NET_DVR_VILOST_V30 struAULost; /*音频信号丢失报警(保留)*/
        public NET_DVR_MOTION_V30 struMotion; // 移动侦测
        public NET_DVR_HIDEALARM_V30 struHideAlarm; // 遮挡报警
        public int dwEnableHide; /* 是否启动遮盖(区域大小704*576) ,0-否,1-是*/
        public NET_DVR_SHELTER[] struShelter = new NET_DVR_SHELTER[4];
        public int dwShowOsd; // 预览的图象上是否显示OSD,0-不显示,1-显示 区域大小704*576
        public short wOSDTopLeftX; /* OSD的x坐标 */
        public short wOSDTopLeftY; /* OSD的y坐标 */
        public byte byOSDType; /* OSD类型(主要是年月日格式) */
        public byte byDispWeek; /* 是否显示星期 */
        public byte byOSDAttrib; /* OSD属性:透明，闪烁 */
        public byte byHourOSDType; /* OSD小时制:0-24小时制,1-12小时制 */
        public byte[] byRes = new byte[64];
    }

    public static class NET_DVR_PICCFG_EX extends Structure { // 通道图象结构SDK_V14扩展
        public int dwSize;
        public byte[] sChanName = new byte[NAME_LEN];
        public int dwVideoFormat; /* 只读 视频制式 1-NTSC 2-PAL*/
        public byte byBrightness; /*亮度,0-255*/
        public byte byContrast; /*对比度,0-255*/
        public byte bySaturation; /*饱和度,0-255 */
        public byte byHue; /*色调,0-255*/
        // 显示通道名
        public int dwShowChanName; // 预览的图象上是否显示通道名称,0-不显示,1-显示 区域大小704*576
        public short wShowNameTopLeftX; /* 通道名称显示位置的x坐标 */
        public short wShowNameTopLeftY; /* 通道名称显示位置的y坐标 */
        // 信号丢失报警
        public NET_DVR_VILOST struVILost;
        // 移动侦测
        public NET_DVR_MOTION struMotion;
        // 遮挡报警
        public NET_DVR_HIDEALARM struHideAlarm;
        // 遮挡  区域大小704*576
        public int dwEnableHide; /* 是否启动遮挡 ,0-否,1-是*/
        public NET_DVR_SHELTER[] struShelter = new NET_DVR_SHELTER[MAX_SHELTERNUM];
        // OSD
        public int dwShowOsd; // 预览的图象上是否显示OSD,0-不显示,1-显示 区域大小704*576
        public short wOSDTopLeftX; /* OSD的x坐标 */
        public short wOSDTopLeftY; /* OSD的y坐标 */
        public byte byOSDType; /* OSD类型(主要是年月日格式) */
        /* 0: XXXX-XX-XX 年月日 */
        /* 1: XX-XX-XXXX 月日年 */
        /* 2: XXXX年XX月XX日 */
        /* 3: XX月XX日XXXX年 */
        /* 4: XX-XX-XXXX 日月年*/
        /* 5: XX日XX月XXXX年 */
        public byte byDispWeek; /* 是否显示星期 */
        public byte byOSDAttrib; /* OSD属性:透明，闪烁 */
        /* 0: 不显示OSD */
        /* 1: 透明,闪烁 */
        /* 2: 透明,不闪烁 */
        /* 3: 闪烁,不透明 */
        /* 4: 不透明,不闪烁 */
        public byte byHourOsdType; // 小时制：0表示24小时制，1-12小时制或am/pm
    }

    public static class NET_DVR_PICCFG extends Structure { // 通道图象结构(SDK_V13及之前版本)

        public int dwSize;
        public byte[] sChanName = new byte[NAME_LEN];
        public int dwVideoFormat; /* 只读 视频制式 1-NTSC 2-PAL*/
        public byte byBrightness; /*亮度,0-255*/
        public byte byContrast; /*对比度,0-255*/
        public byte bySaturation; /*饱和度,0-255 */
        public byte byHue; /*色调,0-255*/
        // 显示通道名
        public int dwShowChanName; // 预览的图象上是否显示通道名称,0-不显示,1-显示 区域大小704*576
        public short wShowNameTopLeftX; /* 通道名称显示位置的x坐标 */
        public short wShowNameTopLeftY; /* 通道名称显示位置的y坐标 */
        // 信号丢失报警
        public NET_DVR_VILOST struVILost;
        // 移动侦测
        public NET_DVR_MOTION struMotion;
        // 遮挡报警
        public NET_DVR_HIDEALARM struHideAlarm;
        // 遮挡  区域大小704*576
        public int dwEnableHide; /* 是否启动遮挡 ,0-否,1-是*/
        public short wHideAreaTopLeftX; /* 遮挡区域的x坐标 */
        public short wHideAreaTopLeftY; /* 遮挡区域的y坐标 */
        public short wHideAreaWidth; /* 遮挡区域的宽 */
        public short wHideAreaHeight; /*遮挡区域的高*/
        // OSD
        public int dwShowOsd; // 预览的图象上是否显示OSD,0-不显示,1-显示 区域大小704*576
        public short wOSDTopLeftX; /* OSD的x坐标 */
        public short wOSDTopLeftY; /* OSD的y坐标 */
        public byte byOSDType; /* OSD类型(主要是年月日格式) */
        /* 0: XXXX-XX-XX 年月日 */
        /* 1: XX-XX-XXXX 月日年 */
        /* 2: XXXX年XX月XX日 */
        /* 3: XX月XX日XXXX年 */
        /* 4: XX-XX-XXXX 日月年*/
        /* 5: XX日XX月XXXX年 */
        byte byDispWeek; /* 是否显示星期 */
        byte byOSDAttrib; /* OSD属性:透明，闪烁 */
        /* 0: 不显示OSD */
        /* 1: 透明,闪烁 */
        /* 2: 透明,不闪烁 */
        /* 3: 闪烁,不透明 */
        /* 4: 不透明,不闪烁 */
        public byte reservedData2;
    }

    // 码流压缩参数(子结构)(9000扩展)
    public static class NET_DVR_COMPRESSION_INFO_V30 extends Structure {

        public byte byStreamType; // 码流类型 0-视频流, 1-复合流
        public byte byResolution; // 分辨率0-DCIF 1-CIF, 2-QCIF, 3-4CIF, 4-2CIF 5（保留）16-VGA（640*480）
        // 17-UXGA（1600*1200） 18-SVGA （800*600）19-HD720p（1280*720）20-XVGA
        // 21-HD900p
        public byte byBitrateType; // 码率类型 0:定码率，1:变码率
        public byte byPicQuality; // 图象质量 0-最好 1-次好 2-较好 3-一般 4-较差 5-差
        public int dwVideoBitrate; // 视频码率 0-保留 1-16K 2-32K 3-48k 4-64K 5-80K 6-96K 7-128K 8-160k
        // 9-192K 10-224K 11-256K 12-320K 13-384K 14-448K 15-512K 16-640K
        // 17-768K 18-896K 19-1024K 20-1280K 21-1536K 22-1792K
        // 23-2048最高位(31位)置成1表示是自定义码流, 0-30位表示码流值。
        public int dwVideoFrameRate; // 帧率 0-全部; 1-1/16; 2-1/8; 3-1/4; 4-1/2; 5-1; 6-2; 7-4; 8-6; 9-8;
        // 10-10; 11-12; 12-16; 13-20; V2.0版本中新加14-15; 15-18; 16-22;
        public short wIntervalFrameI; // I帧间隔
        public byte byIntervalBPFrame; // 0-BBP帧; 1-BP帧; 2-单P帧
        public byte byENumber; // E帧数量（保留）
        public byte byVideoEncType; // 视频编码类型 0 hik264;1标准h264; 2标准mpeg4;
        public byte byAudioEncType; // 音频编码类型 0 G722
        public byte[] byres = new byte[10];
    }

    // 通道压缩参数(9000扩展)
    public static class NET_DVR_COMPRESSIONCFG_V30 extends Structure {

        public int dwSize;
        public NET_DVR_COMPRESSION_INFO_V30 struNormHighRecordPara; // 录像 对应8000的普通
        public NET_DVR_COMPRESSION_INFO_V30 struRes; // 保留 String[28];
        public NET_DVR_COMPRESSION_INFO_V30 struEventRecordPara; // 事件触发压缩参数
        public NET_DVR_COMPRESSION_INFO_V30 struNetPara; // 网传(子码流)
    }

    public static class NET_DVR_COMPRESSION_INFO extends Structure { // 码流压缩参数(子结构)
        public byte byStreamType; // 码流类型0-视频流,1-复合流,表示压缩参数时最高位表示是否启用压缩参数
        public byte byResolution; // 分辨率0-DCIF 1-CIF, 2-QCIF, 3-4CIF, 4-2CIF, 5-2QCIF(352X144)(车载专用)
        public byte byBitrateType; // 码率类型0:变码率，1:定码率
        public byte byPicQuality; // 图象质量 0-最好 1-次好 2-较好 3-一般 4-较差 5-差
        public int dwVideoBitrate; // 视频码率 0-保留 1-16K(保留) 2-32K 3-48k 4-64K 5-80K 6-96K 7-128K 8-160k
        // 9-192K 10-224K 11-256K 12-320K
        // 13-384K 14-448K 15-512K 16-640K 17-768K 18-896K 19-1024K 20-1280K 21-1536K 22-1792K
        // 23-2048K
        // 最高位(31位)置成1表示是自定义码流, 0-30位表示码流值(MIN-32K MAX-8192K)。
        public int dwVideoFrameRate; // 帧率 0-全部; 1-1/16; 2-1/8; 3-1/4; 4-1/2; 5-1; 6-2; 7-4; 8-6; 9-8;
        // 10-10; 11-12; 12-16; 13-20;
    }

    public static class NET_DVR_COMPRESSIONCFG extends Structure { // 通道压缩参数
        public int dwSize;
        public NET_DVR_COMPRESSION_INFO struRecordPara; // 录像/事件触发录像
        public NET_DVR_COMPRESSION_INFO struNetPara; // 网传/保留
    }

    public static class NET_DVR_COMPRESSION_INFO_EX extends Structure { // 码流压缩参数(子结构)(扩展) 增加I帧间隔
        public byte byStreamType; // 码流类型0-视频流, 1-复合流
        public byte byResolution; // 分辨率0-DCIF 1-CIF, 2-QCIF, 3-4CIF, 4-2CIF, 5-2QCIF(352X144)(车载专用)
        public byte byBitrateType; // 码率类型0:变码率，1:定码率
        public byte byPicQuality; // 图象质量 0-最好 1-次好 2-较好 3-一般 4-较差 5-差
        public int dwVideoBitrate; // 视频码率 0-保留 1-16K(保留) 2-32K 3-48k 4-64K 5-80K 6-96K 7-128K 8-160k
        // 9-192K 10-224K 11-256K 12-320K
        // 13-384K 14-448K 15-512K 16-640K 17-768K 18-896K 19-1024K 20-1280K 21-1536K 22-1792K
        // 23-2048K
        // 最高位(31位)置成1表示是自定义码流, 0-30位表示码流值(MIN-32K MAX-8192K)。
        public int dwVideoFrameRate; // 帧率 0-全部; 1-1/16; 2-1/8; 3-1/4; 4-1/2; 5-1; 6-2; 7-4; 8-6; 9-8;
        // 10-10; 11-12; 12-16; 13-20, //V2.0增加14-15, 15-18, 16-22;
        public short wIntervalFrameI; // I帧间隔
        // 2006-08-11 增加单P帧的配置接口，可以改善实时流延时问题
        public byte byIntervalBPFrame; // 0-BBP帧; 1-BP帧; 2-单P帧
        public byte byENumber; // E帧数量
    }

    public static class NET_DVR_COMPRESSIONCFG_EX extends Structure { // 通道压缩参数(扩展)
        public int dwSize;
        public NET_DVR_COMPRESSION_INFO_EX struRecordPara; // 录像
        public NET_DVR_COMPRESSION_INFO_EX struNetPara; // 网传
    }

    public static class NET_DVR_RECCOMPRESSIONCFG_EX extends Structure { // 录象时间段压缩参数配置(GE定制)2006-09-18
        int dwSize;
        NET_DVR_COMPRESSION_INFO_EX[][] struRecTimePara =
                new NET_DVR_COMPRESSION_INFO_EX[MAX_DAYS][MAX_TIMESEGMENT]; // 录像时间段
    }

    public static class NET_DVR_RECORDSCHED extends Structure // 时间段录像参数配置(子结构)
    {

        public NET_DVR_SCHEDTIME struRecordTime = new NET_DVR_SCHEDTIME();
        public byte byRecordType; // 0:定时录像，1:移动侦测，2:报警录像，3:动测|报警，4:动测&报警, 5:命令触发, 6: 智能录像
        public byte[] reservedData = new byte[3];
    }

    public static class NET_DVR_RECORDDAY extends Structure // 全天录像参数配置(子结构)
    {

        public short wAllDayRecord; /* 是否全天录像 0-否 1-是*/
        public byte byRecordType; /* 录象类型 0:定时录像，1:移动侦测，2:报警录像，3:动测|报警，4:动测&报警 5:命令触发, 6: 智能录像*/
        public byte reservedData;
    }

    public static class NET_DVR_RECORDSCHEDWEEK extends Structure {

        public NET_DVR_RECORDSCHED[] struRecordSched = new NET_DVR_RECORDSCHED[MAX_TIMESEGMENT_V30];
    }

    public static class NET_DVR_RECORD_V30 extends Structure { // 通道录像参数配置(9000扩展)

        public int dwSize;
        public int dwRecord; /*是否录像 0-否 1-是*/
        public NET_DVR_RECORDDAY[] struRecAllDay = new NET_DVR_RECORDDAY[MAX_DAYS];
        public NET_DVR_RECORDSCHEDWEEK[] struRecordSched = new NET_DVR_RECORDSCHEDWEEK[MAX_DAYS];
        public int dwRecordTime; /* 录象延时长度 0-5秒， 1-20秒， 2-30秒， 3-1分钟， 4-2分钟， 5-5分钟， 6-10分钟*/
        public int dwPreRecordTime; /* 预录时间 0-不预录 1-5秒 2-10秒 3-15秒 4-20秒 5-25秒 6-30秒 7-0xffffffff(尽可能预录) */
        public int dwRecorderDuration; /* 录像保存的最长时间 */
        public byte byRedundancyRec; /*是否冗余录像,重要数据双备份：0/1*/
        public byte byAudioRec; /*录像时复合流编码时是否记录音频数据：国外有此法规*/
        public byte[] byReserve = new byte[10];
    }

    public static class NET_DVR_RECORD extends Structure { // 通道录像参数配置

        public int dwSize;
        public int dwRecord; /*是否录像 0-否 1-是*/
        public NET_DVR_RECORDDAY[] struRecAllDay = new NET_DVR_RECORDDAY[MAX_DAYS];
        public NET_DVR_RECORDSCHEDWEEK[] struRecordSched = new NET_DVR_RECORDSCHEDWEEK[MAX_DAYS];
        public int dwRecordTime; /* 录象时间长度 0-5秒， 1-20秒， 2-30秒， 3-1分钟， 4-2分钟， 5-5分钟， 6-10分钟*/
        public int dwPreRecordTime; /* 预录时间 0-不预录 1-5秒 2-10秒 3-15秒 4-20秒 5-25秒 6-30秒 7-0xffffffff(尽可能预录) */
    }

    // 云台协议表结构配置
    public static class NET_DVR_PTZ_PROTOCOL extends Structure {

        public int dwType; /*解码器类型值，从1开始连续递增*/
        public byte[] byDescribe = new byte[DESC_LEN]; /*解码器的描述符，和8000中的一致*/
    }

    public static class NET_DVR_PTZCFG extends Structure {

        public int dwSize;
        public NET_DVR_PTZ_PROTOCOL[] struPtz = new NET_DVR_PTZ_PROTOCOL[PTZ_PROTOCOL_NUM]; /*最大200中PTZ协议*/
        public int dwPtzNum; /*有效的ptz协议数目，从0开始(即计算时加1)*/
        public byte[] byRes = new byte[8];
    }

    /***************************云台类型(end)******************************/
    public static class NET_DVR_DECODERCFG_V30 extends Structure { // 通道解码器(云台)参数配置(9000扩展)
        public int dwSize;
        public int dwBaudRate; // 波特率(bps)，0－50，1－75，2－110，3－150，4－300，5－600，6－1200，7－2400，8－4800，9－9600，10－19200，
        // 11－38400，12－57600，13－76800，14－115.2k;
        public byte byDataBit; // 数据有几位 0－5位，1－6位，2－7位，3－8位;
        public byte byStopBit; // 停止位 0－1位，1－2位;
        public byte byParity; // 校验 0－无校验，1－奇校验，2－偶校验;
        public byte byFlowcontrol; // 0－无，1－软流控,2-硬流控
        public short wDecoderType; // 解码器类型, 0－YouLi，1－LiLin-1016，2－LiLin-820，3－Pelco-p，4－DM
        // DynaColor，5－HD600，6－JC-4116，7－Pelco-d WX，8－Pelco-d PICO
        public short wDecoderAddress; /*解码器地址:0 - 255*/
        public byte[] bySetPreset = new byte[MAX_PRESET_V30]; /* 预置点是否设置,0-没有设置,1-设置*/
        public byte[] bySetCruise = new byte[MAX_CRUISE_V30]; /* 巡航是否设置: 0-没有设置,1-设置 */
        public byte[] bySetTrack = new byte[MAX_TRACK_V30]; /* 轨迹是否设置,0-没有设置,1-设置*/
    }

    public static class NET_DVR_DECODERCFG extends Structure { // 通道解码器(云台)参数配置
        public int dwSize;
        public int dwBaudRate; // 波特率(bps)，0－50，1－75，2－110，3－150，4－300，5－600，6－1200，7－2400，8－4800，9－9600，10－19200，
        // 11－38400，12－57600，13－76800，14－115.2k;
        public byte byDataBit; // 数据有几位 0－5位，1－6位，2－7位，3－8位;
        public byte byStopBit; // 停止位 0－1位，1－2位;
        public byte byParity; // 校验 0－无校验，1－奇校验，2－偶校验;
        public byte byFlowcontrol; // 0－无，1－软流控,2-硬流控
        public short wDecoderType; // 解码器类型, 0－YouLi，1－LiLin-1016，2－LiLin-820，3－Pelco-p，4－DM
        // DynaColor，5－HD600，6－JC-4116，7－Pelco-d WX，8－Pelco-d PICO
        public short wDecoderAddress; /*解码器地址:0 - 255*/
        public byte[] bySetPreset = new byte[MAX_PRESET]; /* 预置点是否设置,0-没有设置,1-设置*/
        public byte[] bySetCruise = new byte[MAX_CRUISE]; /* 巡航是否设置: 0-没有设置,1-设置 */
        public byte[] bySetTrack = new byte[MAX_TRACK]; /* 轨迹是否设置,0-没有设置,1-设置*/
    }

    public static class NET_DVR_PPPCFG_V30 extends Structure { // ppp参数配置(子结构)
        public NET_DVR_IPADDR struRemoteIP; // 远端IP地址
        public NET_DVR_IPADDR struLocalIP; // 本地IP地址
        public byte[] sLocalIPMask = new byte[16]; // 本地IP地址掩码
        public byte[] sUsername = new byte[NAME_LEN]; /* 用户名 */
        public byte[] sPassword = new byte[PASSWD_LEN]; /* 密码 */
        public byte byPPPMode; // PPP模式, 0－主动，1－被动
        public byte byRedial; // 是否回拨 ：0-否,1-是
        public byte byRedialMode; // 回拨模式,0-由拨入者指定,1-预置回拨号码
        public byte byDataEncrypt; // 数据加密,0-否,1-是
        public int dwMTU; // MTU
        public byte[] sTelephoneNumber = new byte[PHONENUMBER_LEN]; // 电话号码
    }

    public static class NET_DVR_PPPCFG extends Structure { // ppp参数配置(子结构)
        public byte[] sRemoteIP = new byte[16]; // 远端IP地址
        public byte[] sLocalIP = new byte[16]; // 本地IP地址
        public byte[] sLocalIPMask = new byte[16]; // 本地IP地址掩码
        public byte[] sUsername = new byte[NAME_LEN]; /* 用户名 */
        public byte[] sPassword = new byte[PASSWD_LEN]; /* 密码 */
        public byte byPPPMode; // PPP模式, 0－主动，1－被动
        public byte byRedial; // 是否回拨 ：0-否,1-是
        public byte byRedialMode; // 回拨模式,0-由拨入者指定,1-预置回拨号码
        public byte byDataEncrypt; // 数据加密,0-否,1-是
        public int dwMTU; // MTU
        public byte[] sTelephoneNumber = new byte[PHONENUMBER_LEN]; // 电话号码
    }

    public static class NET_DVR_SINGLE_RS232 extends Structure { // RS232串口参数配置(9000扩展)
        public int
                dwBaudRate; /*波特率(bps)，0－50，1－75，2－110，3－150，4－300，5－600，6－1200，7－2400，8－4800，9－9600，10－19200， 11－38400，12－57600，13－76800，14－115.2k;*/
        public byte byDataBit; /* 数据有几位 0－5位，1－6位，2－7位，3－8位 */
        public byte byStopBit; /* 停止位 0－1位，1－2位 */
        public byte byParity; /* 校验 0－无校验，1－奇校验，2－偶校验 */
        public byte byFlowcontrol; /* 0－无，1－软流控,2-硬流控 */
        public int dwWorkMode; /* 工作模式，0－232串口用于PPP拨号，1－232串口用于参数控制，2－透明通道 */
    }

    public static class NET_DVR_RS232CFG_V30 extends Structure { // RS232串口参数配置(9000扩展)
        public int dwSize;
        public NET_DVR_SINGLE_RS232 struRs232; /*目前只有第一个串口设置有效，所有设备都只支持一个串口，其他七个保留*/
        public byte[] byRes = new byte[84];
        public NET_DVR_PPPCFG_V30 struPPPConfig; /*ppp参数*/
    }

    public static class NET_DVR_RS232CFG extends Structure { // RS232串口参数配置
        public int dwSize;
        public int dwBaudRate; // 波特率(bps)，0－50，1－75，2－110，3－150，4－300，5－600，6－1200，7－2400，8－4800，9－9600，10－19200，
        // 11－38400，12－57600，13－76800，14－115.2k;
        public byte byDataBit; // 数据有几位 0－5位，1－6位，2－7位，3－8位;
        public byte byStopBit; // 停止位 0－1位，1－2位;
        public byte byParity; // 校验 0－无校验，1－奇校验，2－偶校验;
        public byte byFlowcontrol; // 0－无，1－软流控,2-硬流控
        public int dwWorkMode; // 工作模式，0－窄带传输(232串口用于PPP拨号)，1－控制台(232串口用于参数控制)，2－透明通道
        public NET_DVR_PPPCFG struPPPConfig;
    }

    public static class NET_DVR_ALARMINCFG_V30 extends Structure { // 报警输入参数配置(9000扩展)
        public int dwSize;
        public byte[] sAlarmInName = new byte[NAME_LEN]; /* 名称 */
        public byte byAlarmType; // 报警器类型,0：常开,1：常闭
        public byte byAlarmInHandle; /* 是否处理 0-不处理 1-处理*/
        public byte[] reservedData = new byte[2];
        public NET_DVR_HANDLEEXCEPTION_V30 struAlarmHandleType; /* 处理方式 */
        public NET_DVR_SCHEDTIMEWEEK[] struAlarmTime = new NET_DVR_SCHEDTIMEWEEK[MAX_DAYS]; // 布防时间
        public byte[] byRelRecordChan = new byte[MAX_CHANNUM_V30]; // 报警触发的录象通道,为1表示触发该通道
        public byte[] byEnablePreset = new byte[MAX_CHANNUM_V30]; /* 是否调用预置点 0-否,1-是*/
        public byte[] byPresetNo = new byte[MAX_CHANNUM_V30]; /* 调用的云台预置点序号,一个报警输入可以调用多个通道的云台预置点, 0xff表示不调用预置点。*/
        public byte[] byEnablePresetRevert = new byte[MAX_CHANNUM_V30]; /* 是否恢复到调用预置点前的位置(保留) */
        public short[] wPresetRevertDelay = new short[MAX_CHANNUM_V30]; /* 恢复预置点延时(保留) */
        public byte[] byEnableCruise = new byte[MAX_CHANNUM_V30]; /* 是否调用巡航 0-否,1-是*/
        public byte[] byCruiseNo = new byte[MAX_CHANNUM_V30]; /* 巡航 */
        public byte[] byEnablePtzTrack = new byte[MAX_CHANNUM_V30]; /* 是否调用轨迹 0-否,1-是*/
        public byte[] byPTZTrack = new byte[MAX_CHANNUM_V30]; /* 调用的云台的轨迹序号 */
        public byte[] byRes = new byte[16];
    }

    public static class NET_DVR_ALARMINCFG extends Structure { // 报警输入参数配置
        public int dwSize;
        public byte[] sAlarmInName = new byte[NAME_LEN]; /* 名称 */
        public byte byAlarmType; // 报警器类型,0：常开,1：常闭
        public byte byAlarmInHandle; /* 是否处理 0-不处理 1-处理*/
        public NET_DVR_HANDLEEXCEPTION struAlarmHandleType; /* 处理方式 */
        public NET_DVR_SCHEDTIMEWEEK[] struAlarmTime = new NET_DVR_SCHEDTIMEWEEK[MAX_DAYS]; // 布防时间
        public byte[] byRelRecordChan = new byte[MAX_CHANNUM]; // 报警触发的录象通道,为1表示触发该通道
        public byte[] byEnablePreset = new byte[MAX_CHANNUM]; /* 是否调用预置点 0-否,1-是*/
        public byte[] byPresetNo = new byte[MAX_CHANNUM]; /* 调用的云台预置点序号,一个报警输入可以调用多个通道的云台预置点, 0xff表示不调用预置点。*/
        public byte[] byEnableCruise = new byte[MAX_CHANNUM]; /* 是否调用巡航 0-否,1-是*/
        public byte[] byCruiseNo = new byte[MAX_CHANNUM]; /* 巡航 */
        public byte[] byEnablePtzTrack = new byte[MAX_CHANNUM]; /* 是否调用轨迹 0-否,1-是*/
        public byte[] byPTZTrack = new byte[MAX_CHANNUM]; /* 调用的云台的轨迹序号 */
    }

    public static class NET_DVR_ADDIT_POSITION extends Structure { // 车载GPS信息结构(2007-12-27)
        public byte[] sDevName = new byte[32]; /* 设备名称 */
        public int dwSpeed; /*速度*/
        public int dwLongitude; /* 经度*/
        public int dwLatitude; /* 纬度*/
        public byte[] direction = new byte[2]; /* direction[0]:'E'or'W'(东经/西经), direction[1]:'N'or'S'(北纬/南纬) */
        public byte[] res = new byte[2]; /* 保留位 */
    }

    public static class NET_DVR_ALARMINFO_V30 extends Structure { // 上传报警信息(9000扩展)
        public int
                dwAlarmType; /*0-信号量报警,1-硬盘满,2-信号丢失,3－移动侦测,4－硬盘未格式化,5-读写硬盘出错,6-遮挡报警,7-制式不匹配, 8-非法访问, 0xa-GPS定位信息(车载定制)*/
        public int dwAlarmInputNumber; /*报警输入端口*/
        public byte[] byAlarmOutputNumber = new byte[MAX_ALARMOUT_V30]; /*触发的输出端口，为1表示对应输出*/
        public byte[] byAlarmRelateChannel =
                new byte[MAX_CHANNUM_V30]; /*触发的录像通道，为1表示对应录像, dwAlarmRelateChannel[0]对应第1个通道*/
        public byte[] byChannel = new byte[MAX_CHANNUM_V30]; /*dwAlarmType为2或3,6时，表示哪个通道，dwChannel[0]对应第1个通道*/
        public byte[] byDiskNumber = new byte[MAX_DISKNUM_V30]; /*dwAlarmType为1,4,5时,表示哪个硬盘, dwDiskNumber[0]对应第1个硬盘*/
    }

    public static class NET_DVR_ALARMINFO extends Structure {

        public int
                dwAlarmType; /*0-信号量报警,1-硬盘满,2-信号丢失,3－移动侦测,4－硬盘未格式化,5-读写硬盘出错,6-遮挡报警,7-制式不匹配, 8-非法访问, 9-串口状态, 0xa-GPS定位信息(车载定制)*/
        public int dwAlarmInputNumber; /*报警输入端口, 当报警类型为9时该变量表示串口状态0表示正常， -1表示错误*/
        public int[] dwAlarmOutputNumber = new int[MAX_ALARMOUT]; /*触发的输出端口，为1表示对应哪一个输出*/
        public int[] dwAlarmRelateChannel = new int[MAX_CHANNUM]; /*触发的录像通道，dwAlarmRelateChannel[0]为1表示第1个通道录像*/
        public int[] dwChannel = new int[MAX_CHANNUM]; /*dwAlarmType为2或3,6时，表示哪个通道，dwChannel[0]位对应第1个通道*/
        public int[] dwDiskNumber = new int[MAX_DISKNUM]; /*dwAlarmType为1,4,5时,表示哪个硬盘, dwDiskNumber[0]位对应第1个硬盘*/
    }

    public static class NET_DVR_ALARMINFO_EX extends Structure { // 上传报警信息(杭州竞天定制 2006-07-28)
        public int dwAlarmType; /*0-信号量报警,1-硬盘满,2-信号丢失,3－移动侦测,4－硬盘未格式化,5-读写硬盘出错,6-遮挡报警,7-制式不匹配, 8-非法访问*/
        public int dwAlarmInputNumber; /*报警输入端口*/
        public int[] dwAlarmOutputNumber = new int[MAX_ALARMOUT]; /*报警输入端口对应的输出端口，哪一位为1表示对应哪一个输出*/
        public int[] dwAlarmRelateChannel =
                new int[MAX_CHANNUM]; /*报警输入端口对应的录像通道，哪一位为1表示对应哪一路录像,dwAlarmRelateChannel[0]对应第1个通道*/
        public int[] dwChannel = new int[MAX_CHANNUM]; /*dwAlarmType为2或3,6时，表示哪个通道，dwChannel[0]位对应第0个通道*/
        public int[] dwDiskNumber = new int[MAX_DISKNUM]; /*dwAlarmType为1,4,5时,表示哪个硬盘*/
        public byte[] sSerialNumber = new byte[SERIALNO_LEN]; // 序列号
        public byte[] sRemoteAlarmIP = new byte[16]; // 远程报警IP地址；
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // IPC接入参数配置
    public static class NET_DVR_IPDEVINFO extends Structure {
        /* IP设备结构 */
        public int dwEnable; /* 该IP设备是否启用 */
        public byte[] sUserName = new byte[NAME_LEN]; /* 用户名 */
        public byte[] sPassword = new byte[PASSWD_LEN]; /* 密码 */
        public NET_DVR_IPADDR struIP = new NET_DVR_IPADDR(); /* IP地址 */
        public short wDVRPort; /* 端口号 */
        public byte[] byres = new byte[34]; /* 保留 */
    }

    public static class NET_DVR_IPCHANINFO extends Structure {
        /* IP通道匹配参数 */
        public byte byEnable; /* 该通道是否启用 */
        public byte byIPID; /* IP设备ID 取值1- MAX_IP_DEVICE */
        public byte byChannel; /* 通道号 */
        public byte[] byres = new byte[33]; /* 保留 */
    }

    public static class NET_DVR_IPPARACFG extends Structure {
        /* IP接入配置结构 */
        public int dwSize; /* 结构大小 */
        public NET_DVR_IPDEVINFO[] struIPDevInfo = new NET_DVR_IPDEVINFO[MAX_IP_DEVICE]; /* IP设备 */
        public byte[] byAnalogChanEnable = new byte[MAX_ANALOG_CHANNUM]; /* 模拟通道是否启用，从低到高表示1-32通道，0表示无效 1有效 */
        public NET_DVR_IPCHANINFO[] struIPChanInfo = new NET_DVR_IPCHANINFO[MAX_IP_CHANNEL]; /* IP通道 */
    }

    public static class NET_DVR_IPALARMOUTINFO extends Structure {
        /* 报警输出参数 */
        public byte byIPID; /* IP设备ID取值1- MAX_IP_DEVICE */
        public byte byAlarmOut; /* 报警输出号 */
        public byte[] byRes = new byte[18]; /* 保留 */
    }

    public static class NET_DVR_IPALARMOUTCFG extends Structure {
        /* IP报警输出配置结构 */
        public int dwSize; /* 结构大小 */
        public NET_DVR_IPALARMOUTINFO[] struIPAlarmOutInfo = new NET_DVR_IPALARMOUTINFO[MAX_IP_ALARMOUT]; /* IP报警输出 */
    }

    public static class NET_DVR_IPALARMININFO extends Structure {
        /* 报警输入参数 */
        public byte byIPID; /* IP设备ID取值1- MAX_IP_DEVICE */
        public byte byAlarmIn; /* 报警输入号 */
        public byte[] byRes = new byte[18]; /* 保留 */
    }

    public static class NET_DVR_IPALARMINCFG extends Structure {
        /* IP报警输入配置结构 */
        public int dwSize; /* 结构大小 */
        public NET_DVR_IPALARMININFO[] struIPAlarmInInfo = new NET_DVR_IPALARMININFO[MAX_IP_ALARMIN]; /* IP报警输入 */
    }

    public static class NET_DVR_IPALARMINFO extends Structure { // ipc alarm info
        public NET_DVR_IPDEVINFO[] struIPDevInfo = new NET_DVR_IPDEVINFO[MAX_IP_DEVICE]; /* IP设备 */
        public byte[] byAnalogChanEnable = new byte[MAX_ANALOG_CHANNUM]; /* 模拟通道是否启用，0-未启用 1-启用 */
        public NET_DVR_IPCHANINFO[] struIPChanInfo = new NET_DVR_IPCHANINFO[MAX_IP_CHANNEL]; /* IP通道 */
        public NET_DVR_IPALARMININFO[] struIPAlarmInInfo = new NET_DVR_IPALARMININFO[MAX_IP_ALARMIN]; /* IP报警输入 */
        public NET_DVR_IPALARMOUTINFO[] struIPAlarmOutInfo = new NET_DVR_IPALARMOUTINFO[MAX_IP_ALARMOUT]; /* IP报警输出 */
    }

    public static class NET_DVR_SINGLE_HD extends Structure { // 本地硬盘信息配置
        public int dwHDNo; /*硬盘号, 取值0~MAX_DISKNUM_V30-1*/
        public int dwCapacity; /*硬盘容量(不可设置)*/
        public int dwFreeSpace; /*硬盘剩余空间(不可设置)*/
        public int dwHdStatus; /*硬盘状态(不可设置) 0-正常, 1-未格式化, 2-错误, 3-SMART状态, 4-不匹配, 5-休眠*/
        public byte byHDAttr; /*0-默认, 1-冗余; 2-只读*/
        public byte[] byRes1 = new byte[3];
        public int dwHdGroup; /*属于哪个盘组 1-MAX_HD_GROUP*/
        public byte[] byRes2 = new byte[120];
    }

    public static class NET_DVR_HDCFG extends Structure {

        public int dwSize;
        public int dwHDCount; /*硬盘数(不可设置)*/
        public NET_DVR_SINGLE_HD[] struHDInfo = new NET_DVR_SINGLE_HD[MAX_DISKNUM_V30]; // 硬盘相关操作都需要重启才能生效；
    }

    public static class NET_DVR_SINGLE_HDGROUP extends Structure { // 本地盘组信息配置
        public int dwHDGroupNo; /*盘组号(不可设置) 1-MAX_HD_GROUP*/
        public byte[] byHDGroupChans = new byte[64]; /*盘组对应的录像通道, 0-表示该通道不录象到该盘组，1-表示录象到该盘组*/
        public byte[] byRes = new byte[8];
    }

    public static class NET_DVR_HDGROUP_CFG extends Structure {

        public int dwSize;
        public int dwHDGroupCount; /*盘组总数(不可设置)*/
        public NET_DVR_SINGLE_HDGROUP[] struHDGroupAttr = new NET_DVR_SINGLE_HDGROUP[MAX_HD_GROUP]; // 硬盘相关操作都需要重启才能生效；
    }

    public static class NET_DVR_SCALECFG extends Structure { // 配置缩放参数的结构
        public int dwSize;
        public int dwMajorScale; /* 主显示 0-不缩放，1-缩放*/
        public int dwMinorScale; /* 辅显示 0-不缩放，1-缩放*/
        public int[] dwRes = new int[2];
    }

    public static class NET_DVR_ALARMOUTCFG_V30 extends Structure { // DVR报警输出(9000扩展)
        public int dwSize;
        public byte[] sAlarmOutName = new byte[NAME_LEN]; /* 名称 */
        public int dwAlarmOutDelay; /* 输出保持时间(-1为无限，手动关闭) */
        // 0-5秒,1-10秒,2-30秒,3-1分钟,4-2分钟,5-5分钟,6-10分钟,7-手动
        public NET_DVR_SCHEDTIMEWEEK[] struAlarmOutTime = new NET_DVR_SCHEDTIMEWEEK[MAX_DAYS]; /* 报警输出激活时间段 */
        public byte[] byRes = new byte[16];
    }

    public static class NET_DVR_ALARMOUTCFG extends Structure { // DVR报警输出
        public int dwSize;
        public byte[] sAlarmOutName = new byte[NAME_LEN]; /* 名称 */
        public int dwAlarmOutDelay; /* 输出保持时间(-1为无限，手动关闭) */
        // 0-5秒,1-10秒,2-30秒,3-1分钟,4-2分钟,5-5分钟,6-10分钟,7-手动
        public NET_DVR_SCHEDTIMEWEEK[] struAlarmOutTime = new NET_DVR_SCHEDTIMEWEEK[MAX_DAYS]; /* 报警输出激活时间段 */
    }

    public static class NET_DVR_PREVIEWCFG_V30 extends Structure { // DVR本地预览参数(9000扩展)
        public int dwSize;
        public byte byPreviewNumber; // 预览数目,0-1画面,1-4画面,2-9画面,3-16画面, 4-6画面, 5-8画面, 0xff:最大画面
        public byte byEnableAudio; // 是否声音预览,0-不预览,1-预览
        public short wSwitchTime; // 切换时间,0-不切换,1-5s,2-10s,3-20s,4-30s,5-60s,6-120s,7-300s
        public byte[][] bySwitchSeq = new byte[MAX_PREVIEW_MODE][MAX_WINDOW_V30]; // 切换顺序,如果lSwitchSeq[i]为 0xff表示不用
        public byte[] byRes = new byte[24];
    }

    public static class NET_DVR_PREVIEWCFG extends Structure { // DVR本地预览参数
        public int dwSize;
        public byte byPreviewNumber; // 预览数目,0-1画面,1-4画面,2-9画面,3-16画面,0xff:最大画面
        public byte byEnableAudio; // 是否声音预览,0-不预览,1-预览
        public short wSwitchTime; // 切换时间,0-不切换,1-5s,2-10s,3-20s,4-30s,5-60s,6-120s,7-300s
        public byte[] bySwitchSeq = new byte[MAX_WINDOW]; // 切换顺序,如果lSwitchSeq[i]为 0xff表示不用
    }

    public static class NET_DVR_VGAPARA extends Structure { // DVR视频输出
        public short wResolution; /* 分辨率 */
        public short wFreq; /* 刷新频率 */
        public int dwBrightness; /* 亮度 */
    }

    /*
     * MATRIX输出参数结构
     */
    public static class NET_DVR_MATRIXPARA_V30 extends Structure {

        public short[] wOrder = new short[MAX_ANALOG_CHANNUM]; /* 预览顺序, 0xff表示相应的窗口不预览 */
        public short wSwitchTime; /* 预览切换时间 */
        public byte[] res = new byte[14];
    }

    public static class NET_DVR_MATRIXPARA extends Structure {

        public short wDisplayLogo; /* 显示视频通道号(保留) */
        public short wDisplayOsd; /* 显示时间(保留) */
    }

    public static class NET_DVR_VOOUT extends Structure {

        public byte byVideoFormat; /* 输出制式,0-PAL,1-NTSC */
        public byte byMenuAlphaValue; /* 菜单与背景图象对比度 */
        public short wScreenSaveTime; /* 屏幕保护时间 0-从不,1-1分钟,2-2分钟,3-5分钟,4-10分钟,5-20分钟,6-30分钟 */
        public short wVOffset; /* 视频输出偏移 */
        public short wBrightness; /* 视频输出亮度 */
        public byte byStartMode; /* 启动后视频输出模式(0:菜单,1:预览)*/
        public byte byEnableScaler; /* 是否启动缩放 (0-不启动, 1-启动)*/
    }

    public static class NET_DVR_VIDEOOUT_V30 extends Structure { // DVR视频输出(9000扩展)
        public int dwSize;
        public NET_DVR_VOOUT[] struVOOut = new NET_DVR_VOOUT[MAX_VIDEOOUT_V30];
        public NET_DVR_VGAPARA[] struVGAPara = new NET_DVR_VGAPARA[MAX_VGA_V30]; /* VGA参数 */
        public NET_DVR_MATRIXPARA_V30[] struMatrixPara = new NET_DVR_MATRIXPARA_V30[MAX_MATRIXOUT]; /* MATRIX参数 */
        public byte[] byRes = new byte[16];
    }

    public static class NET_DVR_VIDEOOUT extends Structure { // DVR视频输出
        public int dwSize;
        public NET_DVR_VOOUT[] struVOOut = new NET_DVR_VOOUT[MAX_VIDEOOUT];
        public NET_DVR_VGAPARA[] struVGAPara = new NET_DVR_VGAPARA[MAX_VGA]; /* VGA参数 */
        public NET_DVR_MATRIXPARA struMatrixPara; /* MATRIX参数 */
    }

    public static class NET_DVR_USER_INFO_V30 extends Structure { // 单用户参数(子结构)(9000扩展)
        public byte[] sUserName = new byte[NAME_LEN]; /* 用户名 */
        public byte[] sPassword = new byte[PASSWD_LEN]; /* 密码 */
        public byte[] byLocalRight = new byte[MAX_RIGHT]; /* 本地权限 */
        /*数组0: 本地控制云台*/
        /*数组1: 本地手动录象*/
        /*数组2: 本地回放*/
        /*数组3: 本地设置参数*/
        /*数组4: 本地查看状态、日志*/
        /*数组5: 本地高级操作(升级，格式化，重启，关机)*/
        /*数组6: 本地查看参数 */
        /*数组7: 本地管理模拟和IP camera */
        /*数组8: 本地备份 */
        /*数组9: 本地关机/重启 */
        public byte[] byRemoteRight = new byte[MAX_RIGHT]; /* 远程权限 */
        /*数组0: 远程控制云台*/
        /*数组1: 远程手动录象*/
        /*数组2: 远程回放 */
        /*数组3: 远程设置参数*/
        /*数组4: 远程查看状态、日志*/
        /*数组5: 远程高级操作(升级，格式化，重启，关机)*/
        /*数组6: 远程发起语音对讲*/
        /*数组7: 远程预览*/
        /*数组8: 远程请求报警上传、报警输出*/
        /*数组9: 远程控制，本地输出*/
        /*数组10: 远程控制串口*/
        /*数组11: 远程查看参数 */
        /*数组12: 远程管理模拟和IP camera */
        /*数组13: 远程关机/重启 */
        public byte[] byNetPreviewRight = new byte[MAX_CHANNUM_V30]; /* 远程可以预览的通道 0-有权限，1-无权限*/
        public byte[] byLocalPlaybackRight = new byte[MAX_CHANNUM_V30]; /* 本地可以回放的通道 0-有权限，1-无权限*/
        public byte[] byNetPlaybackRight = new byte[MAX_CHANNUM_V30]; /* 远程可以回放的通道 0-有权限，1-无权限*/
        public byte[] byLocalRecordRight = new byte[MAX_CHANNUM_V30]; /* 本地可以录像的通道 0-有权限，1-无权限*/
        public byte[] byNetRecordRight = new byte[MAX_CHANNUM_V30]; /* 远程可以录像的通道 0-有权限，1-无权限*/
        public byte[] byLocalPTZRight = new byte[MAX_CHANNUM_V30]; /* 本地可以PTZ的通道 0-有权限，1-无权限*/
        public byte[] byNetPTZRight = new byte[MAX_CHANNUM_V30]; /* 远程可以PTZ的通道 0-有权限，1-无权限*/
        public byte[] byLocalBackupRight = new byte[MAX_CHANNUM_V30]; /* 本地备份权限通道 0-有权限，1-无权限*/
        public NET_DVR_IPADDR struUserIP; /* 用户IP地址(为0时表示允许任何地址) */
        public byte[] byMACAddr = new byte[MACADDR_LEN]; /* 物理地址 */
        public byte byPriority; /* 优先级，0xff-无，0--低，1--中，2--高 */
        /*
        无……表示不支持优先级的设置
        低……默认权限:包括本地和远程回放,本地和远程查看日志和状态,本地和远程关机/重启
        中……包括本地和远程控制云台,本地和远程手动录像,本地和远程回放,语音对讲和远程预览
              本地备份,本地/远程关机/重启
        高……管理员
        */
        public byte[] byRes = new byte[17];
    }

    public static class NET_DVR_USER_INFO_EX extends Structure { // 单用户参数(SDK_V15扩展)(子结构)
        public byte[] sUserName = new byte[NAME_LEN]; /* 用户名 */
        public byte[] sPassword = new byte[PASSWD_LEN]; /* 密码 */
        public int[] dwLocalRight = new int[MAX_RIGHT]; /* 权限 */
        /*数组0: 本地控制云台*/
        /*数组1: 本地手动录象*/
        /*数组2: 本地回放*/
        /*数组3: 本地设置参数*/
        /*数组4: 本地查看状态、日志*/
        /*数组5: 本地高级操作(升级，格式化，重启，关机)*/
        public int dwLocalPlaybackRight; /* 本地可以回放的通道 bit0 -- channel 1*/
        public int[] dwRemoteRight = new int[MAX_RIGHT]; /* 权限 */
        /*数组0: 远程控制云台*/
        /*数组1: 远程手动录象*/
        /*数组2: 远程回放 */
        /*数组3: 远程设置参数*/
        /*数组4: 远程查看状态、日志*/
        /*数组5: 远程高级操作(升级，格式化，重启，关机)*/
        /*数组6: 远程发起语音对讲*/
        /*数组7: 远程预览*/
        /*数组8: 远程请求报警上传、报警输出*/
        /*数组9: 远程控制，本地输出*/
        /*数组10: 远程控制串口*/
        public int dwNetPreviewRight; /* 远程可以预览的通道 bit0 -- channel 1*/
        public int dwNetPlaybackRight; /* 远程可以回放的通道 bit0 -- channel 1*/
        public byte[] sUserIP = new byte[16]; /* 用户IP地址(为0时表示允许任何地址) */
        public byte[] byMACAddr = new byte[MACADDR_LEN]; /* 物理地址 */
    }

    public static class NET_DVR_USER_INFO extends Structure { // 单用户参数(子结构)
        public byte[] sUserName = new byte[NAME_LEN]; /* 用户名 */
        public byte[] sPassword = new byte[PASSWD_LEN]; /* 密码 */
        public int[] dwLocalRight = new int[MAX_RIGHT]; /* 权限 */
        /*数组0: 本地控制云台*/
        /*数组1: 本地手动录象*/
        /*数组2: 本地回放*/
        /*数组3: 本地设置参数*/
        /*数组4: 本地查看状态、日志*/
        /*数组5: 本地高级操作(升级，格式化，重启，关机)*/
        public int[] dwRemoteRight = new int[MAX_RIGHT]; /* 权限 */
        /*数组0: 远程控制云台*/
        /*数组1: 远程手动录象*/
        /*数组2: 远程回放 */
        /*数组3: 远程设置参数*/
        /*数组4: 远程查看状态、日志*/
        /*数组5: 远程高级操作(升级，格式化，重启，关机)*/
        /*数组6: 远程发起语音对讲*/
        /*数组7: 远程预览*/
        /*数组8: 远程请求报警上传、报警输出*/
        /*数组9: 远程控制，本地输出*/
        /*数组10: 远程控制串口*/
        public byte[] sUserIP = new byte[16]; /* 用户IP地址(为0时表示允许任何地址) */
        public byte[] byMACAddr = new byte[MACADDR_LEN]; /* 物理地址 */
    }

    public static class NET_DVR_USER_V30 extends Structure { // DVR用户参数(9000扩展)
        public int dwSize;
        public NET_DVR_USER_INFO_V30[] struUser = new NET_DVR_USER_INFO_V30[MAX_USERNUM_V30];
    }

    public static class NET_DVR_USER_EX extends Structure { // DVR用户参数(SDK_V15扩展)
        public int dwSize;
        public NET_DVR_USER_INFO_EX[] struUser = new NET_DVR_USER_INFO_EX[MAX_USERNUM];
    }

    public static class NET_DVR_USER extends Structure { // DVR用户参数
        public int dwSize;
        public NET_DVR_USER_INFO[] struUser = new NET_DVR_USER_INFO[MAX_USERNUM];
    }

    public static class NET_DVR_EXCEPTION_V30 extends Structure { // DVR异常参数(9000扩展)
        public int dwSize;
        public NET_DVR_HANDLEEXCEPTION_V30[] struExceptionHandleType =
                new NET_DVR_HANDLEEXCEPTION_V30[MAX_EXCEPTIONNUM_V30];
        /*数组0-盘满,1- 硬盘出错,2-网线断,3-局域网内IP 地址冲突,4-非法访问, 5-输入/输出视频制式不匹配, 6-行车超速(车载专用), 7-视频信号异常(9000)*/
    }

    public static class NET_DVR_EXCEPTION extends Structure { // DVR异常参数
        public int dwSize;
        public NET_DVR_HANDLEEXCEPTION[] struExceptionHandleType = new NET_DVR_HANDLEEXCEPTION[MAX_EXCEPTIONNUM];
        /*数组0-盘满,1- 硬盘出错,2-网线断,3-局域网内IP 地址冲突,4-非法访问, 5-输入/输出视频制式不匹配, 6-行车超速(车载专用)*/
    }

    public static class NET_DVR_CHANNELSTATE_V30 extends Structure { // 通道状态(9000扩展)
        public byte byRecordStatic; // 通道是否在录像,0-不录像,1-录像
        public byte bySignalStatic; // 连接的信号状态,0-正常,1-信号丢失
        public byte byHardwareStatic; // 通道硬件状态,0-正常,1-异常,例如DSP死掉
        public byte reservedData; // 保留
        public int dwBitRate; // 实际码率
        public int dwLinkNum; // 客户端连接的个数
        public NET_DVR_IPADDR[] struClientIP = new NET_DVR_IPADDR[MAX_LINK]; // 客户端的IP地址
        public int dwIPLinkNum; // 如果该通道为IP接入，那么表示IP接入当前的连接数
        public byte[] byRes = new byte[12];
    }

    public static class NET_DVR_CHANNELSTATE extends Structure { // 通道状态
        public byte byRecordStatic; // 通道是否在录像,0-不录像,1-录像
        public byte bySignalStatic; // 连接的信号状态,0-正常,1-信号丢失
        public byte byHardwareStatic; // 通道硬件状态,0-正常,1-异常,例如DSP死掉
        public byte reservedData; // 保留
        public int dwBitRate; // 实际码率
        public int dwLinkNum; // 客户端连接的个数
        public int[] dwClientIP = new int[MAX_LINK]; // 客户端的IP地址
    }

    public static class NET_DVR_DISKSTATE extends Structure { // 硬盘状态
        public int dwVolume; // 硬盘的容量
        public int dwFreeSpace; // 硬盘的剩余空间
        public int dwHardDiskStatic; // 硬盘的状态,按位:1-休眠,2-不正常,3-休眠硬盘出错
    }

    public static class NET_DVR_WORKSTATE_V30 extends Structure { // DVR工作状态(9000扩展)
        public int dwDeviceStatic; // 设备的状态,0-正常,1-CPU占用率太高,超过85%,2-硬件错误,例如串口死掉
        public NET_DVR_DISKSTATE[] struHardDiskStatic = new NET_DVR_DISKSTATE[MAX_DISKNUM_V30];
        public NET_DVR_CHANNELSTATE_V30[] struChanStatic = new NET_DVR_CHANNELSTATE_V30[MAX_CHANNUM_V30]; // 通道的状态
        public byte[] byAlarmInStatic = new byte[MAX_ALARMIN_V30]; // 报警端口的状态,0-没有报警,1-有报警
        public byte[] byAlarmOutStatic = new byte[MAX_ALARMOUT_V30]; // 报警输出端口的状态,0-没有输出,1-有报警输出
        public int dwLocalDisplay; // 本地显示状态,0-正常,1-不正常
        public byte[] byAudioChanStatus = new byte[MAX_AUDIO_V30]; // 表示语音通道的状态 0-未使用，1-使用中, 0xff无效
        public byte[] byRes = new byte[10];
    }

    public static class NET_DVR_WORKSTATE extends Structure { // DVR工作状态
        public int dwDeviceStatic; // 设备的状态,0-正常,1-CPU占用率太高,超过85%,2-硬件错误,例如串口死掉
        public NET_DVR_DISKSTATE[] struHardDiskStatic = new NET_DVR_DISKSTATE[MAX_DISKNUM];
        public NET_DVR_CHANNELSTATE[] struChanStatic = new NET_DVR_CHANNELSTATE[MAX_CHANNUM]; // 通道的状态
        public byte[] byAlarmInStatic = new byte[MAX_ALARMIN]; // 报警端口的状态,0-没有报警,1-有报警
        public byte[] byAlarmOutStatic = new byte[MAX_ALARMOUT]; // 报警输出端口的状态,0-没有输出,1-有报警输出
        public int dwLocalDisplay; // 本地显示状态,0-正常,1-不正常
    }

    public static class NET_DVR_LOG_V30 extends Structure { // 日志信息(9000扩展)
        public NET_DVR_TIME strLogTime;
        public int dwMajorType; // 主类型 1-报警; 2-异常; 3-操作; 0xff-全部
        public int dwMinorType; // 次类型 0-全部;
        public byte[] sPanelUser = new byte[MAX_NAMELEN]; // 操作面板的用户名
        public byte[] sNetUser = new byte[MAX_NAMELEN]; // 网络操作的用户名
        public NET_DVR_IPADDR struRemoteHostAddr; // 远程主机地址
        public int dwParaType; // 参数类型
        public int dwChannel; // 通道号
        public int dwDiskNumber; // 硬盘号
        public int dwAlarmInPort; // 报警输入端口
        public int dwAlarmOutPort; // 报警输出端口
        public int dwInfoLen;
        public byte[] sInfo = new byte[LOG_INFO_LEN];
    }

    // 日志信息
    public static class NET_DVR_LOG extends Structure {

        public NET_DVR_TIME strLogTime;
        public int dwMajorType; // 主类型 1-报警; 2-异常; 3-操作; 0xff-全部
        public int dwMinorType; // 次类型 0-全部;
        public byte[] sPanelUser = new byte[MAX_NAMELEN]; // 操作面板的用户名
        public byte[] sNetUser = new byte[MAX_NAMELEN]; // 网络操作的用户名
        public byte[] sRemoteHostAddr = new byte[16]; // 远程主机地址
        public int dwParaType; // 参数类型
        public int dwChannel; // 通道号
        public int dwDiskNumber; // 硬盘号
        public int dwAlarmInPort; // 报警输入端口
        public int dwAlarmOutPort; // 报警输出端口
    }

    /************************DVR日志 end***************************/
    public static class NET_DVR_ALARMOUTSTATUS_V30 extends Structure { // 报警输出状态(9000扩展)
        public byte[] Output = new byte[MAX_ALARMOUT_V30];
    }

    public static class NET_DVR_ALARMOUTSTATUS extends Structure { // 报警输出状态
        public byte[] Output = new byte[MAX_ALARMOUT];
    }

    public static class NET_DVR_TRADEINFO extends Structure { // 交易信息
        public short m_Year;
        public short m_Month;
        public short m_Day;
        public short m_Hour;
        public short m_Minute;
        public short m_Second;
        public byte[] DeviceName = new byte[24]; // 设备名称
        public int dwChannelNumer; // 通道号
        public byte[] CardNumber = new byte[32]; // 卡号
        public byte[] cTradeType = new byte[12]; // 交易类型
        public int dwCash; // 交易金额
    }

    public static class NET_DVR_FRAMETYPECODE extends Structure {
        /*帧格式*/
        public byte[] code = new byte[12]; /* 代码 */
    }

    public static class NET_DVR_FRAMEFORMAT_V30 extends Structure { // ATM参数(9000扩展)
        public int dwSize;
        public NET_DVR_IPADDR struATMIP; /* ATM IP地址 */
        public int dwATMType; /* ATM类型 */
        public int dwInputMode; /* 输入方式	0-网络侦听 1-网络接收 2-串口直接输入 3-串口ATM命令输入*/
        public int dwFrameSignBeginPos; /* 报文标志位的起始位置*/
        public int dwFrameSignLength; /* 报文标志位的长度 */
        public byte[] byFrameSignContent = new byte[12]; /* 报文标志位的内容 */
        public int dwCardLengthInfoBeginPos; /* 卡号长度信息的起始位置 */
        public int dwCardLengthInfoLength; /* 卡号长度信息的长度 */
        public int dwCardNumberInfoBeginPos; /* 卡号信息的起始位置 */
        public int dwCardNumberInfoLength; /* 卡号信息的长度 */
        public int dwBusinessTypeBeginPos; /* 交易类型的起始位置 */
        public int dwBusinessTypeLength; /* 交易类型的长度 */
        public NET_DVR_FRAMETYPECODE[] frameTypeCode = new NET_DVR_FRAMETYPECODE[10]; /* 类型 */
        public short wATMPort; /* 卡号捕捉端口号(网络协议方式) (保留)0xffff表示该值无效*/
        public short wProtocolType; /* 网络协议类型(保留) 0xffff表示该值无效*/
        public byte[] byRes = new byte[24];
    }

    public static class NET_DVR_FRAMEFORMAT extends Structure { // ATM参数
        public int dwSize;
        public byte[] sATMIP = new byte[16]; /* ATM IP地址 */
        public int dwATMType; /* ATM类型 */
        public int dwInputMode; /* 输入方式	0-网络侦听 1-网络接收 2-串口直接输入 3-串口ATM命令输入*/
        public int dwFrameSignBeginPos; /* 报文标志位的起始位置*/
        public int dwFrameSignLength; /* 报文标志位的长度 */
        public byte[] byFrameSignContent = new byte[12]; /* 报文标志位的内容 */
        public int dwCardLengthInfoBeginPos; /* 卡号长度信息的起始位置 */
        public int dwCardLengthInfoLength; /* 卡号长度信息的长度 */
        public int dwCardNumberInfoBeginPos; /* 卡号信息的起始位置 */
        public int dwCardNumberInfoLength; /* 卡号信息的长度 */
        public int dwBusinessTypeBeginPos; /* 交易类型的起始位置 */
        public int dwBusinessTypeLength; /* 交易类型的长度 */
        public NET_DVR_FRAMETYPECODE[] frameTypeCode = new NET_DVR_FRAMETYPECODE[10]; /* 类型 */
    }

    public static class NET_DVR_FTPTYPECODE extends Structure {

        public byte[] sFtpType = new byte[32]; /*客户定义的操作类型*/
        public byte[] sFtpCode = new byte[8]; /*客户定义的操作类型的对应的码*/
    }

    public static class NET_DVR_FRAMEFORMAT_EX extends Structure { // ATM参数添加FTP上传参数, 俄罗斯银行定制, 2006-11-17
        public int dwSize;
        public byte[] sATMIP = new byte[16]; /* ATM IP地址 */
        public int dwATMType; /* ATM类型 */
        public int dwInputMode; /* 输入方式	0-网络侦听 1-网络接收 2-串口直接输入 3-串口ATM命令输入*/
        public int dwFrameSignBeginPos; /* 报文标志位的起始位置*/
        public int dwFrameSignLength; /* 报文标志位的长度 */
        public byte[] byFrameSignContent = new byte[12]; /* 报文标志位的内容 */
        public int dwCardLengthInfoBeginPos; /* 卡号长度信息的起始位置 */
        public int dwCardLengthInfoLength; /* 卡号长度信息的长度 */
        public int dwCardNumberInfoBeginPos; /* 卡号信息的起始位置 */
        public int dwCardNumberInfoLength; /* 卡号信息的长度 */
        public int dwBusinessTypeBeginPos; /* 交易类型的起始位置 */
        public int dwBusinessTypeLength; /* 交易类型的长度 */
        public NET_DVR_FRAMETYPECODE[] frameTypeCode = new NET_DVR_FRAMETYPECODE[10]; /* 类型 */
        public byte[] sFTPIP = new byte[16]; /* FTP IP */
        public byte[] byFtpUsername = new byte[NAME_LEN]; /* 用户名 */
        public byte[] byFtpPasswd = new byte[PASSWD_LEN]; /* 密码 */
        public byte[] sDirName = new byte[NAME_LEN]; /*服务器目录名*/
        public int dwATMSrvType; /*ATM服务器类型，0--wincor ，1--diebold*/
        public int dwTimeSpace; /*取值为1.2.3.4.5.10*/
        public NET_DVR_FTPTYPECODE[] sFtpTypeCodeOp = new NET_DVR_FTPTYPECODE[300]; /*新加的*/
        public int dwADPlay; /* 1 表示在播放广告，0 表示没有播放广告*/
        public int dwNewPort; // 端口
    }
    /****************************ATM(end)***************************/

    /*****************************DS-6001D/F(begin)***************************/
    // DS-6001D Decoder
    public static class NET_DVR_DECODERINFO extends Structure {

        public byte[] byEncoderIP = new byte[16]; // 解码设备连接的服务器IP
        public byte[] byEncoderUser = new byte[16]; // 解码设备连接的服务器的用户名
        public byte[] byEncoderPasswd = new byte[16]; // 解码设备连接的服务器的密码
        public byte bySendMode; // 解码设备连接服务器的连接模式
        public byte byEncoderChannel; // 解码设备连接的服务器的通道号
        public short wEncoderPort; // 解码设备连接的服务器的端口号
        public byte[] reservedData = new byte[4]; // 保留
    }

    public static class NET_DVR_DECODERSTATE extends Structure {

        public byte[] byEncoderIP = new byte[16]; // 解码设备连接的服务器IP
        public byte[] byEncoderUser = new byte[16]; // 解码设备连接的服务器的用户名
        public byte[] byEncoderPasswd = new byte[16]; // 解码设备连接的服务器的密码
        public byte byEncoderChannel; // 解码设备连接的服务器的通道号
        public byte bySendMode; // 解码设备连接的服务器的连接模式
        public short wEncoderPort; // 解码设备连接的服务器的端口号
        public int dwConnectState; // 解码设备连接服务器的状态
        public byte[] reservedData = new byte[4]; // 保留
    }

    public static class NET_DVR_DECCHANINFO extends Structure {

        public byte[] sDVRIP = new byte[16]; /* DVR IP地址 */
        public short wDVRPort; /* 端口号 */
        public byte[] sUserName = new byte[NAME_LEN]; /* 用户名 */
        public byte[] sPassword = new byte[PASSWD_LEN]; /* 密码 */
        public byte byChannel; /* 通道号 */
        public byte byLinkMode; /* 连接模式 */
        public byte byLinkType; /* 连接类型 0－主码流 1－子码流 */
    }

    public static class NET_DVR_DECINFO extends Structure {
        /*每个解码通道的配置*/
        public byte byPoolChans; /*每路解码通道上的循环通道数量, 最多4通道 0表示没有解码*/
        public NET_DVR_DECCHANINFO[] struchanConInfo = new NET_DVR_DECCHANINFO[MAX_DECPOOLNUM];
        public byte byEnablePoll; /*是否轮巡 0-否 1-是*/
        public byte byPoolTime; /*轮巡时间 0-保留 1-10秒 2-15秒 3-20秒 4-30秒 5-45秒 6-1分钟 7-2分钟 8-5分钟 */
    }

    public static class NET_DVR_DECCFG extends Structure {
        /*整个设备解码配置*/
        public int dwSize;
        public int dwDecChanNum; /*解码通道的数量*/
        public NET_DVR_DECINFO[] struDecInfo = new NET_DVR_DECINFO[MAX_DECNUM];
    }

    // 2005-08-01
    public static class NET_DVR_PORTINFO extends Structure {
        /* 解码设备透明通道设置 */
        public int dwEnableTransPort; /* 是否启动透明通道 0－不启用 1－启用*/
        public byte[] sDecoderIP = new byte[16]; /* DVR IP地址 */
        public short wDecoderPort; /* 端口号 */
        public short wDVRTransPort; /* 配置前端DVR是从485/232输出，1表示232串口,2表示485串口 */
        public byte[] cReserve = new byte[4];
    }

    public static class NET_DVR_PORTCFG extends Structure {

        public int dwSize;
        public NET_DVR_PORTINFO[] struTransPortInfo = new NET_DVR_PORTINFO[MAX_TRANSPARENTNUM]; /* 数组0表示232 数组1表示485 */
    }

    /*https://jna.dev.java.net/javadoc/com/sun/jna/Union.html#setType(java.lang.Class)  see how to use the JNA Union*/
    public static class NET_DVR_PLAYREMOTEFILE extends Structure {
        /* 控制网络文件回放 */
        public int dwSize;
        public byte[] sDecoderIP = new byte[16]; /* DVR IP地址 */
        public short wDecoderPort; /* 端口号 */
        public short wLoadMode; /* 回放下载模式 1－按名字 2－按时间 */
        public byte[] byFile = new byte[100];

        public static class mode_size extends Union {

            public byte[] byFile = new byte[100]; // 回放的文件名

            public static class bytime extends Structure {

                public int dwChannel;
                public byte[] sUserName = new byte[NAME_LEN]; // 请求视频用户名
                public byte[] sPassword = new byte[PASSWD_LEN]; // 密码
                public NET_DVR_TIME struStartTime; // 按时间回放的开始时间
                public NET_DVR_TIME struStopTime; // 按时间回放的结束时间
            }
        }
    }

    public static class NET_DVR_DECCHANSTATUS extends Structure {
        /*当前设备解码连接状态*/
        public int dwWorkType; /*工作方式：1：轮巡、2：动态连接解码、3：文件回放下载 4：按时间回放下载*/
        public byte[] sDVRIP = new byte[16]; /*连接的设备ip*/
        public short wDVRPort; /*连接端口号*/
        public byte byChannel; /* 通道号 */
        public byte byLinkMode; /* 连接模式 */
        public int dwLinkType; /*连接类型 0－主码流 1－子码流*/
        public byte[] sUserName = new byte[NAME_LEN]; /*请求视频用户名*/
        public byte[] sPassword = new byte[PASSWD_LEN]; /* 密码 */
        public byte[] cReserve = new byte[52];

        public static class objectInfo extends Union {

            public static class userInfo extends Structure {

                public byte[] sUserName = new byte[NAME_LEN]; // 请求视频用户名
                public byte[] sPassword = new byte[PASSWD_LEN]; // 密码
                public byte[] cReserve = new byte[52];
            }

            public static class fileInfo extends Structure {

                public byte[] fileName = new byte[100];
            }

            public static class timeInfo extends Structure {

                public int dwChannel;
                public byte[] sUserName = new byte[NAME_LEN]; // 请求视频用户名
                public byte[] sPassword = new byte[PASSWD_LEN]; // 密码
                public NET_DVR_TIME struStartTime; // 按时间回放的开始时间
                public NET_DVR_TIME struStopTime; // 按时间回放的结束时间
            }
        }
    }

    public static class NET_DVR_DECSTATUS extends Structure {

        public int dwSize;
        public NET_DVR_DECCHANSTATUS[] struDecState = new NET_DVR_DECCHANSTATUS[MAX_DECNUM];
    }

    /*****************************DS-6001D/F(end)***************************/
    public static class NET_DVR_SHOWSTRINGINFO extends Structure { // 单字符参数(子结构)
        public short wShowString; // 预览的图象上是否显示字符,0-不显示,1-显示 区域大小704*576,单个字符的大小为32*32
        public short wStringSize; /* 该行字符的长度，不能大于44个字符 */
        public short wShowStringTopLeftX; /* 字符显示位置的x坐标 */
        public short wShowStringTopLeftY; /* 字符名称显示位置的y坐标 */
        public byte[] sString = new byte[44]; /* 要显示的字符内容 */
    }

    // 叠加字符(9000扩展)
    public static class NET_DVR_SHOWSTRING_V30 extends Structure {

        public int dwSize;
        public NET_DVR_SHOWSTRINGINFO[] struStringInfo = new NET_DVR_SHOWSTRINGINFO[MAX_STRINGNUM_V30]; /* 要显示的字符内容 */
    }

    // 叠加字符扩展(8条字符)
    public static class NET_DVR_SHOWSTRING_EX extends Structure {

        public int dwSize;
        public NET_DVR_SHOWSTRINGINFO[] struStringInfo = new NET_DVR_SHOWSTRINGINFO[MAX_STRINGNUM_EX]; /* 要显示的字符内容 */
    }

    // 叠加字符
    public static class NET_DVR_SHOWSTRING extends Structure {

        public int dwSize;
        public NET_DVR_SHOWSTRINGINFO[] struStringInfo = new NET_DVR_SHOWSTRINGINFO[MAX_STRINGNUM]; /* 要显示的字符内容 */
    }

    /****************************DS9000新增结构(begin)******************************/

    /*
    EMAIL参数结构
    */
    public static class NET_DVR_SENDER extends Structure {

        public byte[] sName = new byte[NAME_LEN]; /* 发件人姓名 */
        public byte[] sAddress = new byte[MAX_EMAIL_ADDR_LEN]; /* 发件人地址 */
    }

    public static class NET_DVRRECEIVER extends Structure {

        public byte[] sName = new byte[NAME_LEN]; /* 收件人姓名 */
        public byte[] sAddress = new byte[MAX_EMAIL_ADDR_LEN]; /* 收件人地址 */
    }

    public static class NET_DVR_EMAILCFG_V30 extends Structure {

        public int dwSize;
        public byte[] sAccount = new byte[NAME_LEN]; /* 账号*/
        public byte[] sPassword = new byte[MAX_EMAIL_PWD_LEN]; /*密码 */
        public NET_DVR_SENDER struSender;
        public byte[] sSmtpServer = new byte[MAX_EMAIL_ADDR_LEN]; /* smtp服务器 */
        public byte[] sPop3Server = new byte[MAX_EMAIL_ADDR_LEN]; /* pop3服务器 */
        public NET_DVRRECEIVER[] struReceiver = new NET_DVRRECEIVER[3]; /* 最多可以设置3个收件人 */
        public byte byAttachment; /* 是否带附件 */
        public byte bySmtpServerVerify; /* 发送服务器要求身份验证 */
        public byte byMailInterval; /* mail interval */
        public byte[] res = new byte[77];
    }

    /*
    DVR实现巡航数据结构
    */
    public static class NET_DVR_CRUISE_PARA extends Structure {

        public int dwSize;
        public byte[] byPresetNo = new byte[CRUISE_MAX_PRESET_NUMS]; /* 预置点号 */
        public byte[] byCruiseSpeed = new byte[CRUISE_MAX_PRESET_NUMS]; /* 巡航速度 */
        public short[] wDwellTime = new short[CRUISE_MAX_PRESET_NUMS]; /* 停留时间 */
        public byte[] byEnableThisCruise; /* 是否启用 */
        public byte[] res = new byte[15];
    }

    /****************************DS9000新增结构(end)******************************/

    // 时间点
    public static class NET_DVR_TIMEPOINT extends Structure {

        public int dwMonth; // 月 0-11表示1-12个月
        public int dwWeekNo; // 第几周 0－第1周 1－第2周 2－第3周 3－第4周 4－最后一周
        public int dwWeekDate; // 星期几 0－星期日 1－星期一 2－星期二 3－星期三 4－星期四 5－星期五 6－星期六
        public int dwHour; // 小时	开始时间0－23 结束时间1－23
        public int dwMin; // 分	0－59
    }

    // 夏令时参数
    public static class NET_DVR_ZONEANDDST extends Structure {

        public int dwSize;
        public byte[] byRes1 = new byte[16]; // 保留
        public int dwEnableDST; // 是否启用夏时制 0－不启用 1－启用
        public byte byDSTBias; // 夏令时偏移值，30min, 60min, 90min, 120min, 以分钟计，传递原始数值
        public byte[] byRes2 = new byte[3];
        public NET_DVR_TIMEPOINT struBeginPoint; // 夏时制开始时间
        public NET_DVR_TIMEPOINT struEndPoint; // 夏时制停止时间
    }

    // 图片质量
    public static class NET_DVR_JPEGPARA extends Structure {

        /*注意：当图像压缩分辨率为VGA时，支持0=CIF, 1=QCIF, 2=D1抓图，
        当分辨率为3=UXGA(1600x1200), 4=SVGA(800x600), 5=HD720p(1280x720),6=VGA,7=XVGA, 8=HD900p
        仅支持当前分辨率的抓图*/
        public short wPicSize; /* 0=CIF, 1=QCIF, 2=D1 3=UXGA(1600x1200), 4=SVGA(800x600), 5=HD720p(1280x720),6=VGA*/
        public short wPicQuality; /* 图片质量系数 0-最好 1-较好 2-一般 */
    }

    /* aux video out parameter */
    // 辅助输出参数配置
    public static class NET_DVR_AUXOUTCFG extends Structure {

        public int dwSize;
        public int dwAlarmOutChan; /* 选择报警弹出大报警通道切换时间：1画面的输出通道: 0:主输出/1:辅1/2:辅2/3:辅3/4:辅4 */
        public int dwAlarmChanSwitchTime; /* :1秒 - 10:10秒 */
        public int[] dwAuxSwitchTime =
                new int[MAX_AUXOUT]; /* 辅助输出切换时间: 0-不切换,1-5s,2-10s,3-20s,4-30s,5-60s,6-120s,7-300s */
        public byte[][] byAuxOrder = new byte[MAX_AUXOUT][MAX_WINDOW]; /* 辅助输出预览顺序, 0xff表示相应的窗口不预览 */
    }

    // ntp
    public static class NET_DVR_NTPPARA extends Structure {

        public byte[] sNTPServer = new byte[64]; /* Domain Name or IP addr of NTP server */
        public short wInterval; /* adjust time interval(hours) */
        public byte byEnableNTP; /* enable NPT client 0-no，1-yes*/
        public byte cTimeDifferenceH; /* 与国际标准时间的 小时偏移-12 ... +13 */
        public byte cTimeDifferenceM; /* 与国际标准时间的 分钟偏移0, 30, 45*/
        public byte res1;
        public short wNtpPort; /* ntp server port 9000新增 设备默认为123*/
        public byte[] res2 = new byte[8];
    }

    // ddns
    public static class NET_DVR_DDNSPARA extends Structure {

        public byte[] sUsername = new byte[NAME_LEN]; /* DDNS账号用户名/密码 */
        public byte[] sPassword = new byte[PASSWD_LEN];
        public byte[] sDomainName = new byte[64]; /* 域名 */
        public byte byEnableDDNS; /*是否应用 0-否，1-是*/
        public byte[] res = new byte[15];
    }

    public static class NET_DVR_DDNSPARA_EX extends Structure {

        public byte byHostIndex; /* 0-Hikvision DNS 1－Dyndns 2－PeanutHull(花生壳), 3-希网3322*/
        public byte byEnableDDNS; /*是否应用DDNS 0-否，1-是*/
        public short wDDNSPort; /* DDNS端口号 */
        public byte[] sUsername = new byte[NAME_LEN]; /* DDNS用户名*/
        public byte[] sPassword = new byte[PASSWD_LEN]; /* DDNS密码 */
        public byte[] sDomainName = new byte[MAX_DOMAIN_NAME]; /* 设备配备的域名地址 */
        public byte[] sServerName = new byte[MAX_DOMAIN_NAME]; /* DDNS 对应的服务器地址，可以是IP地址或域名 */
        public byte[] byRes = new byte[16];
    }

    public static class NET_DVR_DDNS extends Structure {

        public byte[] sUsername = new byte[NAME_LEN]; /* DDNS账号用户名*/
        public byte[] sPassword = new byte[PASSWD_LEN]; /* 密码 */
        public byte[] sDomainName = new byte[MAX_DOMAIN_NAME]; /* 设备配备的域名地址 */
        public byte[] sServerName = new byte[MAX_DOMAIN_NAME]; /* DDNS协议对应的服务器地址，可以是IP地址或域名 */
        public short wDDNSPort; /* 端口号 */
        public byte[] byRes = new byte[10];
    }

    // 9000扩展
    public static class NET_DVR_DDNSPARA_V30 extends Structure {

        public byte byEnableDDNS;
        public byte byHostIndex; /* 0-Hikvision DNS(保留) 1－Dyndns 2－PeanutHull(花生壳) 3－希网3322 */
        public byte[] byRes1 = new byte[2];
        public NET_DVR_DDNS[] struDDNS = new NET_DVR_DDNS[MAX_DDNS_NUMS]; // 9000目前只支持前3个配置，其他配置保留
        public byte[] byRes2 = new byte[16];
    }

    // email
    public static class NET_DVR_EMAILPARA extends Structure {

        public byte[] sUsername = new byte[64]; /* 邮件账号/密码 */
        public byte[] sPassword = new byte[64];
        public byte[] sSmtpServer = new byte[64];
        public byte[] sPop3Server = new byte[64];
        public byte[] sMailAddr = new byte[64]; /* email */
        public byte[] sEventMailAddr1 = new byte[64]; /* 上传报警/异常等的email */
        public byte[] sEventMailAddr2 = new byte[64];
        public byte[] res = new byte[16];
    }

    public static class NET_DVR_NETAPPCFG extends Structure { // 网络参数配置
        public int dwSize;
        public byte[] sDNSIp = new byte[16]; /* DNS服务器地址 */
        public NET_DVR_NTPPARA struNtpClientParam; /* NTP参数 */
        public NET_DVR_DDNSPARA struDDNSClientParam; /* DDNS参数 */
        // NET_DVR_EMAILPARA struEmailParam;       /* EMAIL参数 */
        public byte[] res = new byte[464]; /* 保留 */
    }

    public static class NET_DVR_SINGLE_NFS extends Structure { // nfs结构配置
        public byte[] sNfsHostIPAddr = new byte[16];
        public byte[] sNfsDirectory = new byte[PATHNAME_LEN]; // PATHNAME_LEN = 128
    }

    public static class NET_DVR_NFSCFG extends Structure {

        public int dwSize;
        public NET_DVR_SINGLE_NFS[] struNfsDiskParam = new NET_DVR_SINGLE_NFS[MAX_NFS_DISK];
    }

    // 巡航点配置(HIK IP快球专用)
    public static class NET_DVR_CRUISE_POINT extends Structure {

        public byte PresetNum; // 预置点
        public byte Dwell; // 停留时间
        public byte Speed; // 速度
        public byte Reserve; // 保留
    }

    public static class NET_DVR_CRUISE_RET extends Structure {

        public NET_DVR_CRUISE_POINT[] struCruisePoint = new NET_DVR_CRUISE_POINT[32]; // 最大支持32个巡航点
    }

    /************************************多路解码器(begin)***************************************/
    // 多路解码器扩展 added by zxy 2007-05-23
    public static class NET_DVR_NETCFG_OTHER extends Structure {

        public int dwSize;
        public byte[] sFirstDNSIP = new byte[16];
        public byte[] sSecondDNSIP = new byte[16];
        public byte[] sRes = new byte[32];
    }

    public static class NET_DVR_MATRIX_DECINFO extends Structure {

        public byte[] sDVRIP = new byte[16]; /* DVR IP地址 */
        public short wDVRPort; /* 端口号 */
        public byte byChannel; /* 通道号 */
        public byte byTransProtocol; /* 传输协议类型 0-TCP 1-UDP */
        public byte byTransMode; /* 传输码流模式 0－主码流 1－子码流*/
        public byte[] byRes = new byte[3];
        public byte[] sUserName = new byte[NAME_LEN]; /* 监控主机登陆帐号 */
        public byte[] sPassword = new byte[PASSWD_LEN]; /* 监控主机密码 */
    }

    public static class NET_DVR_MATRIX_DYNAMIC_DEC extends Structure { // 启动/停止动态解码
        public int dwSize;
        public NET_DVR_MATRIX_DECINFO struDecChanInfo; /* 动态解码通道信息 */
    }

    public static class NET_DVR_MATRIX_DEC_CHAN_STATUS extends Structure { // 2007-12-13 modified by zxy
        // 修改多路解码器的NET_DVR_MATRIX_DEC_CHAN_STATUS结构
        public int dwSize; // 2008-1-16 modified by zxy dwIsLinked的状态由原来的0－未链接 1－连接修改成以下三种状态。
        public int dwIsLinked; /* 解码通道状态 0－休眠 1－正在连接 2－已连接 3-正在解码 */
        public int dwStreamCpRate; /* Stream copy rate, X kbits/second */
        public byte[] cRes = new byte[64]; /* 保留 */
    }
    // end 2007-12-13 modified by zxy

    public static class NET_DVR_MATRIX_DEC_CHAN_INFO extends Structure {

        public int dwSize;
        public NET_DVR_MATRIX_DECINFO struDecChanInfo; /* 解码通道信息 */
        public int dwDecState; /* 0-动态解码 1－循环解码 2－按时间回放 3－按文件回放 */
        public NET_DVR_TIME StartTime; /* 按时间回放开始时间 */
        public NET_DVR_TIME StopTime; /* 按时间回放停止时间 */
        public byte[] sFileName = new byte[128]; /* 按文件回放文件名 */
    }

    // 连接的通道配置 2007-11-05
    public static class NET_DVR_MATRIX_DECCHANINFO extends Structure {

        public int dwEnable; /* 是否启用 0－否 1－启用*/
        public NET_DVR_MATRIX_DECINFO struDecChanInfo; /* 轮循解码通道信息 */
    }

    // 2007-11-05 新增每个解码通道的配置
    public static class NET_DVR_MATRIX_LOOP_DECINFO extends Structure {

        public int dwSize;
        public int dwPoolTime; /*轮巡时间 */
        public NET_DVR_MATRIX_DECCHANINFO[] struchanConInfo = new NET_DVR_MATRIX_DECCHANINFO[MAX_CYCLE_CHAN];
    }

    // 2007-05-25  多路解码器数字矩阵配置
    // 矩阵行信息 2007-12-28
    public static class NET_DVR_MATRIX_ROW_ELEMENT extends Structure {

        public byte[] sSurvChanName = new byte[128]; /* 监控通道名称，支持中文 */
        public int dwRowNum; /* 行号 */
        public NET_DVR_MATRIX_DECINFO struDecChanInfo; /* 矩阵行信息 */
    }

    public static class NET_DVR_MATRIX_ROW_INDEX extends Structure {

        public byte[] sSurvChanName = new byte[128]; /* 监控通道名称，支持中文 */
        public int dwRowNum; /* 行号 */
    }

    // 矩阵列信息 2007-12-28
    public static class NET_DVR_MATRIX_COLUMN_ELEMENT extends Structure {

        public int dwLocalDispChanNum; /* 本地显示通道号 */
        public int dwGlobalDispChanNum; /* 全局显示通道号 */
        public int dwRes; /* 保留 */
    }

    public static class NET_DVR_MATRIX_GLOBAL_COLUMN_ELEMENT extends Structure {

        public int dwConflictTag; /* 冲突标记，0：无冲突，1：冲突 */
        public int dwConflictGloDispChan; /* 与之冲突的全局通道号 */
        public NET_DVR_MATRIX_COLUMN_ELEMENT struColumnInfo; /* 矩阵列元素结构体 */
    }

    // 手动查看 2007-12-28
    public static class NET_DVR_MATRIX_ROW_COLUMN_LINK extends Structure {

        public int dwSize;
        /*
         *	以下三个参数只需要指定其中一个便可指定数字矩阵里的某一行
         *	所代表的远程监控通道。
         *	如果指定了多个域并有冲突，设备将按照域的先后顺序为准取最先定义者。
         */
        public int dwRowNum; /* -1代表无效域,大于0者方为有效的矩阵行号 */
        public byte[] sSurvChanName = new byte[128]; /* 监控通道名,是否无效按字符串的有效性判断 */
        public int dwSurvNum; /* 监控通道号,按矩阵行列表的顺序指定，一般情况下与行号一致 */
        /*
         *	以下两项只需要指定其中一项便可，如果两项都有效默认选择第一项
         */
        public int dwGlobalDispChanNum; /* 电视墙上的电视机编号 */
        public int dwLocalDispChanNum;
        /*
         *	0代表播放即时码流，
         *	1表示按时间回访远程监控设备的文件
         *	2表示按文件名回访
         */
        public int dwTimeSel;
        public NET_DVR_TIME StartTime;
        public NET_DVR_TIME StopTime;
        public byte[] sFileName = new byte[128];
    }

    public static class NET_DVR_MATRIX_PREVIEW_DISP_CHAN extends Structure {

        public int dwSize;
        public int dwGlobalDispChanNum; /* 电视墙上的电视机编号 */
        public int dwLocalDispChanNum; /* 解码通道 */
    }

    public static class NET_DVR_MATRIX_LOOP_PLAY_SET extends Structure { // 轮循功能 2007-12-28
        public int dwSize;
        /* 任意指定一个,-1为无效,如果都指定则以LocalDispChanNum为准 */
        public int dwLocalDispChanNum; /* 解码通道 */
        public int dwGlobalDispChanNum; /* 电视墙上的电视机编号 */
        public int dwCycTimeInterval; /* 轮循时间间隔 */
    }

    public static class NET_DVR_MATRIX_LOCAL_HOST_INFO extends Structure { // 矩阵中心配置 2007-12-28
        public int dwSize;
        public int dwLocalHostProperty; /* 本地主机类型 0－服务器 1－客户端*/
        public int dwIsIsolated; /* 本地主机是否独立于系统，0：联网，1：独立 */
        public int dwLocalMatrixHostPort; /* 本地主机访问端口 */
        public byte[] byLocalMatrixHostUsrName = new byte[NAME_LEN]; /* 本地主机登录用户名 */
        public byte[] byLocalMatrixHostPasswd = new byte[PASSWD_LEN]; /* 本地主机登录密码 */
        public int dwLocalMatrixCtrlMedia; /* 控制方式 0x1串口键盘控制 0x2网络键盘控制 0x4矩阵中心控制 0x8PC客户端控制*/
        public byte[] sMatrixCenterIP = new byte[16]; /* 矩阵中心IP地址 */
        public int dwMatrixCenterPort; /* 矩阵中心端口号 */
        public byte[] byMatrixCenterUsrName = new byte[NAME_LEN]; /* 矩阵中心登录用户名 */
        public byte[] byMatrixCenterPasswd = new byte[PASSWD_LEN]; /* 矩阵中心登录密码 */
    }

    // 2007-12-22
    public static class TTY_CONFIG extends Structure {

        public byte baudrate; /* 波特率 */
        public byte databits; /* 数据位 */
        public byte stopbits; /* 停止位 */
        public byte parity; /* 奇偶校验位 */
        public byte flowcontrol; /* 流控 */
        public byte[] res = new byte[3];
    }

    public static class NET_DVR_MATRIX_TRAN_CHAN_INFO extends Structure {

        public byte byTranChanEnable; /* 当前透明通道是否打开 0：关闭 1：打开 */
        /*
         *	多路解码器本地有1个485串口，1个232串口都可以作为透明通道,设备号分配如下：
         *	0 RS485
         *	1 RS232 Console
         */
        public byte byLocalSerialDevice; /* Local serial device */
        /*
         *	远程串口输出还是两个,一个RS232，一个RS485
         *	1表示232串口
         *	2表示485串口
         */
        public byte byRemoteSerialDevice; /* Remote output serial device */
        public byte res1; /* 保留 */
        public byte[] sRemoteDevIP = new byte[16]; /* Remote Device IP */
        public short wRemoteDevPort; /* Remote Net Communication Port */
        public byte[] res2 = new byte[2]; /* 保留 */
        public TTY_CONFIG RemoteSerialDevCfg;
    }

    public static class NET_DVR_MATRIX_TRAN_CHAN_CONFIG extends Structure {

        public int dwSize;
        public byte by232IsDualChan; /* 设置哪路232透明通道是全双工的 取值1到MAX_SERIAL_NUM */
        public byte by485IsDualChan; /* 设置哪路485透明通道是全双工的 取值1到MAX_SERIAL_NUM */
        public byte[] res = new byte[2]; /* 保留 */
        public NET_DVR_MATRIX_TRAN_CHAN_INFO[] struTranInfo =
                new NET_DVR_MATRIX_TRAN_CHAN_INFO[MAX_SERIAL_NUM]; /*同时支持建立MAX_SERIAL_NUM个透明通道*/
    }

    // 2007-12-24 Merry Christmas Eve...
    public static class NET_DVR_MATRIX_DEC_REMOTE_PLAY extends Structure {

        public int dwSize;
        public byte[] sDVRIP = new byte[16]; /* DVR IP地址 */
        public short wDVRPort; /* 端口号 */
        public byte byChannel; /* 通道号 */
        public byte byReserve;
        public byte[] sUserName = new byte[NAME_LEN]; /* 用户名 */
        public byte[] sPassword = new byte[PASSWD_LEN]; /* 密码 */
        public int dwPlayMode; /* 0－按文件 1－按时间*/
        public NET_DVR_TIME StartTime;
        public NET_DVR_TIME StopTime;
        public byte[] sFileName = new byte[128];
    }

    public static class NET_DVR_MATRIX_DEC_REMOTE_PLAY_CONTROL extends Structure {

        public int dwSize;
        public int dwPlayCmd; /* 播放命令 见文件播放命令*/
        public int dwCmdParam; /* 播放命令参数 */
    }

    public static class NET_DVR_MATRIX_DEC_REMOTE_PLAY_STATUS extends Structure {

        public int dwSize;
        public int dwCurMediaFileLen; /* 当前播放的媒体文件长度 */
        public int dwCurMediaFilePosition; /* 当前播放文件的播放位置 */
        public int dwCurMediaFileDuration; /* 当前播放文件的总时间 */
        public int dwCurPlayTime; /* 当前已经播放的时间 */
        public int dwCurMediaFIleFrames; /* 当前播放文件的总帧数 */
        public int dwCurDataType; /* 当前传输的数据类型，19-文件头，20-流数据， 21-播放结束标志 */
        public byte[] res = new byte[72];
    }

    public static class NET_DVR_MATRIX_PASSIVEMODE extends Structure {

        public short wTransProtol; // 传输协议，0-TCP, 1-UDP, 2-MCAST
        public short wPassivePort; // TCP,UDP时为TCP,UDP端口, MCAST时为MCAST端口
        public byte[] sMcastIP = new byte[16]; // TCP,UDP时无效, MCAST时为多播地址
        public byte[] res = new byte[8];
    }
    /************************************多路解码器(end)***************************************/

    /************************************多路解码器(end)***************************************/
    public static class NET_DVR_EMAILCFG extends Structure {
        /* 12 bytes */

        public int dwSize;
        public byte[] sUserName = new byte[32];
        public byte[] sPassWord = new byte[32];
        public byte[] sFromName = new byte[32]; /* Sender */ // 字符串中的第一个字符和最后一个字符不能是"@",并且字符串中要有"@"字符
        public byte[] sFromAddr = new byte[48]; /* Sender address */
        public byte[] sToName1 = new byte[32]; /* Receiver1 */
        public byte[] sToName2 = new byte[32]; /* Receiver2 */
        public byte[] sToAddr1 = new byte[48]; /* Receiver address1 */
        public byte[] sToAddr2 = new byte[48]; /* Receiver address2 */
        public byte[] sEmailServer = new byte[32]; /* Email server address */
        public byte byServerType; /* Email server type: 0-SMTP, 1-POP, 2-IMTP…*/
        public byte byUseAuthen; /* Email server authentication method: 1-enable, 0-disable */
        public byte byAttachment; /* enable attachment */
        public byte byMailinterval; /* mail interval 0-2s, 1-3s, 2-4s. 3-5s*/
    }

    public static class NET_DVR_COMPRESSIONCFG_NEW extends Structure {

        public int dwSize;
        public NET_DVR_COMPRESSION_INFO_EX struLowCompression; // 定时录像
        public NET_DVR_COMPRESSION_INFO_EX struEventCompression; // 事件触发录像
    }

    // 球机位置信息
    public static class NET_DVR_PTZPOS extends Structure {

        public short wAction; // 获取时该字段无效
        public short wPanPos; // 水平参数
        public short wTiltPos; // 垂直参数
        public short wZoomPos; // 变倍参数
    }

    // 球机范围信息
    public static class NET_DVR_PTZSCOPE extends Structure {

        public short wPanPosMin; // 水平参数min
        public short wPanPosMax; // 水平参数max
        public short wTiltPosMin; // 垂直参数min
        public short wTiltPosMax; // 垂直参数max
        public short wZoomPosMin; // 变倍参数min
        public short wZoomPosMax; // 变倍参数max
    }

    // rtsp配置 ipcamera专用
    public static class NET_DVR_RTSPCFG extends Structure {

        public int dwSize; // 长度
        public short wPort; // rtsp服务器侦听端口
        public byte[] byReserve = new byte[54]; // 预留
    }

    /********************************接口参数结构(begin)*********************************/

    // NET_DVR_Login()参数结构
    public static class NET_DVR_DEVICEINFO extends Structure {

        public byte[] sSerialNumber = new byte[SERIALNO_LEN]; // 序列号
        public byte byAlarmInPortNum; // DVR报警输入个数
        public byte byAlarmOutPortNum; // DVR报警输出个数
        public byte byDiskNum; // DVR硬盘个数
        public byte byDVRType; // DVR类型, 1:DVR 2:ATM DVR 3:DVS ......
        public byte byChanNum; // DVR 通道个数
        public byte byStartChan; // 起始通道号,例如DVS-1,DVR - 1
    }

    // NET_DVR_Login_V30()参数结构
    public static class NET_DVR_DEVICEINFO_V30 extends Structure {

        public byte[] sSerialNumber = new byte[SERIALNO_LEN]; // 序列号
        public byte byAlarmInPortNum; // 报警输入个数
        public byte byAlarmOutPortNum; // 报警输出个数
        public byte byDiskNum; // 硬盘个数
        public byte byDVRType; // 设备类型, 1:DVR 2:ATM DVR 3:DVS ......
        public byte byChanNum; // 模拟通道个数
        public byte byStartChan; // 起始通道号,例如DVS-1,DVR - 1
        public byte byAudioChanNum; // 语音通道数
        public byte byIPChanNum; // 最大数字通道个数
        public byte[] byRes1 = new byte[24]; // 保留
    }

    // sdk网络环境枚举变量，用于远程升级
    enum _SDK_NET_ENV {
        LOCAL_AREA_NETWORK,
        WIDE_AREA_NETWORK
    }

    // 显示模式
    enum DISPLAY_MODE {
        NORMALMODE,
        OVERLAYMODE
    }

    // 发送模式
    enum SEND_MODE {
        PTOPTCPMODE,
        PTOPUDPMODE,
        MULTIMODE,
        RTPMODE,
        RESERVEDMODE
    };

    // 抓图模式
    enum CAPTURE_MODE {
        BMP_MODE, // BMP模式
        JPEG_MODE // JPEG模式
    };

    // 实时声音模式
    enum REALSOUND_MODE {
        NONE, // SDK中无此模式,只是为了填补0这个位置
        MONOPOLIZE_MODE, // 独占模式 1
        SHARE_MODE // 共享模式 2
    };

    // 软解码预览参数
    public static class NET_DVR_CLIENTINFO extends Structure {

        public NativeLong lChannel;
        public NativeLong lLinkMode;
        public HWND hPlayWnd;
        public String sMultiCastIP;
    }

    // SDK状态信息(9000新增)
    public static class NET_DVR_SDKSTATE extends Structure {

        public int dwTotalLoginNum; // 当前login用户数
        public int dwTotalRealPlayNum; // 当前realplay路数
        public int dwTotalPlayBackNum; // 当前回放或下载路数
        public int dwTotalAlarmChanNum; // 当前建立报警通道路数
        public int dwTotalFormatNum; // 当前硬盘格式化路数
        public int dwTotalFileSearchNum; // 当前日志或文件搜索路数
        public int dwTotalLogSearchNum; // 当前日志或文件搜索路数
        public int dwTotalSerialNum; // 当前透明通道路数
        public int dwTotalUpgradeNum; // 当前升级路数
        public int dwTotalVoiceComNum; // 当前语音转发路数
        public int dwTotalBroadCastNum; // 当前语音广播路数
        public int[] dwRes = new int[10];
    }

    // SDK功能支持信息(9000新增)
    public static class NET_DVR_SDKABL extends Structure {

        public int dwMaxLoginNum; // 最大login用户数 MAX_LOGIN_USERS
        public int dwMaxRealPlayNum; // 最大realplay路数 WATCH_NUM
        public int dwMaxPlayBackNum; // 最大回放或下载路数 WATCH_NUM
        public int dwMaxAlarmChanNum; // 最大建立报警通道路数 ALARM_NUM
        public int dwMaxFormatNum; // 最大硬盘格式化路数 SERVER_NUM
        public int dwMaxFileSearchNum; // 最大文件搜索路数 SERVER_NUM
        public int dwMaxLogSearchNum; // 最大日志搜索路数 SERVER_NUM
        public int dwMaxSerialNum; // 最大透明通道路数 SERVER_NUM
        public int dwMaxUpgradeNum; // 最大升级路数 SERVER_NUM
        public int dwMaxVoiceComNum; // 最大语音转发路数 SERVER_NUM
        public int dwMaxBroadCastNum; // 最大语音广播路数 MAX_CASTNUM
        public int[] dwRes = new int[10];
    }

    // 报警设备信息
    public static class NET_DVR_ALARMER extends Structure {

        public byte byUserIDValid; /* userid是否有效 0-无效，1-有效 */
        public byte bySerialValid; /* 序列号是否有效 0-无效，1-有效 */
        public byte byVersionValid; /* 版本号是否有效 0-无效，1-有效 */
        public byte byDeviceNameValid; /* 设备名字是否有效 0-无效，1-有效 */
        public byte byMacAddrValid; /* MAC地址是否有效 0-无效，1-有效 */
        public byte byLinkPortValid; /* login端口是否有效 0-无效，1-有效 */
        public byte byDeviceIPValid; /* 设备IP是否有效 0-无效，1-有效 */
        public byte bySocketIPValid; /* socket ip是否有效 0-无效，1-有效 */
        public NativeLong lUserID; /* NET_DVR_Login()返回值, 布防时有效 */
        public byte[] sSerialNumber = new byte[SERIALNO_LEN]; /* 序列号 */
        public int dwDeviceVersion; /* 版本信息 高16位表示主版本，低16位表示次版本*/
        public byte[] sDeviceName = new byte[NAME_LEN]; /* 设备名字 */
        public byte[] byMacAddr = new byte[MACADDR_LEN]; /* MAC地址 */
        public short wLinkPort; /* link port */
        public byte[] sDeviceIP = new byte[128]; /* IP地址 */
        public byte[] sSocketIP = new byte[128]; /* 报警主动上传时的socket IP地址 */
        public byte byIpProtocol; /* Ip协议 0-IPV4, 1-IPV6 */
        public byte[] byRes2 = new byte[11];
    }

    // 硬解码显示区域参数(子结构)
    public static class NET_DVR_DISPLAY_PARA extends Structure {

        public NativeLong bToScreen;
        public NativeLong bToVideoOut;
        public NativeLong nLeft;
        public NativeLong nTop;
        public NativeLong nWidth;
        public NativeLong nHeight;
        public NativeLong nReserved;
    }

    // 硬解码预览参数
    public static class NET_DVR_CARDINFO extends Structure {

        public NativeLong lChannel; // 通道号
        public NativeLong lLinkMode; // 最高位(31)为0表示主码流，为1表示子，0－30位表示码流连接方式:0：TCP方式,1：UDP方式,2：多播方式,3 -
        // RTP方式，4-电话线，5－128k宽带，6－256k宽带，7－384k宽带，8－512k宽带；
        public String sMultiCastIP;
        public NET_DVR_DISPLAY_PARA struDisplayPara;
    }

    // 录象文件参数
    public static class NET_DVR_FIND_DATA extends Structure {

        public byte[] sFileName = new byte[100]; // 文件名
        public NET_DVR_TIME struStartTime; // 文件的开始时间
        public NET_DVR_TIME struStopTime; // 文件的结束时间
        public int dwFileSize; // 文件的大小
    }

    // 录象文件参数(9000)
    public static class NET_DVR_FINDDATA_V30 extends Structure {

        public byte[] sFileName = new byte[100]; // 文件名
        public NET_DVR_TIME struStartTime; // 文件的开始时间
        public NET_DVR_TIME struStopTime; // 文件的结束时间
        public int dwFileSize; // 文件的大小
        public byte[] sCardNum = new byte[32];
        public byte byLocked; // 9000设备支持,1表示此文件已经被锁定,0表示正常的文件
        public byte[] byRes = new byte[3];
    }

    // 录象文件参数(带卡号)
    public static class NET_DVR_FINDDATA_CARD extends Structure {

        public byte[] sFileName = new byte[100]; // 文件名
        public NET_DVR_TIME struStartTime; // 文件的开始时间
        public NET_DVR_TIME struStopTime; // 文件的结束时间
        public int dwFileSize; // 文件的大小
        public byte[] sCardNum = new byte[32];
    }

    public static class NET_DVR_FILECOND extends Structure // 录象文件查找条件结构
    {

        public NativeLong lChannel; // 通道号
        public int dwFileType; // 录象文件类型0xff－全部，0－定时录像,1-移动侦测 ，2－报警触发，3-报警|移动侦测 4-报警&移动侦测 5-命令触发 6-手动录像
        public int dwIsLocked; // 是否锁定 0-正常文件,1-锁定文件, 0xff表示所有文件
        public int dwUseCardNo; // 是否使用卡号
        public byte[] sCardNumber = new byte[32]; // 卡号
        public NET_DVR_TIME struStartTime; // 开始时间
        public NET_DVR_TIME struStopTime; // 结束时间
    }

    // 云台区域选择放大缩小(HIK 快球专用)
    public static class NET_DVR_POINT_FRAME extends Structure {

        public int xTop; // 方框起始点的x坐标
        public int yTop; // 方框结束点的y坐标
        public int xBottom; // 方框结束点的x坐标
        public int yBottom; // 方框结束点的y坐标
        public int bCounter; // 保留
    }

    // 语音对讲参数
    public static class NET_DVR_COMPRESSION_AUDIO extends Structure {

        public byte byAudioEncType; // 音频编码类型 0-G722; 1-G711
        public byte[] byres = new byte[7]; // 这里保留音频的压缩参数
    }

    // 用于接收报警信息的缓存区
    public static class RECV_ALARM extends Structure {

        public byte[] RecvBuffer = new byte[400]; // 此处的400应不小于最大报警报文长度
    }

    /***API函数声明,详细说明见API手册***/
    public static interface FRealDataCallBack_V30 extends StdCallCallback {

        public void invoke(
                NativeLong lRealHandle, int dwDataType, ByteByReference pBuffer, int dwBufSize, Pointer pUser);
    }

    public static interface FMSGCallBack extends StdCallCallback {

        public void invoke(
                NativeLong lCommand, NET_DVR_ALARMER pAlarmer, RECV_ALARM pAlarmInfo, int dwBufLen, Pointer pUser);
    }

    public static interface FMessCallBack extends StdCallCallback {

        public boolean invoke(NativeLong lCommand, String sDVRIP, String pBuf, int dwBufLen);
    }

    public static interface FMessCallBack_EX extends StdCallCallback {

        public boolean invoke(NativeLong lCommand, NativeLong lUserID, String pBuf, int dwBufLen);
    }

    public static interface FMessCallBack_NEW extends StdCallCallback {

        public boolean invoke(NativeLong lCommand, String sDVRIP, String pBuf, int dwBufLen, short dwLinkDVRPort);
    }

    public static interface FMessageCallBack extends StdCallCallback {

        public boolean invoke(NativeLong lCommand, String sDVRIP, String pBuf, int dwBufLen, int dwUser);
    }

    public static interface FExceptionCallBack extends StdCallCallback {

        public void invoke(int dwType, NativeLong lUserID, NativeLong lHandle, Pointer pUser);
    }

    public static interface FDrawFun extends StdCallCallback {

        public void invoke(NativeLong lRealHandle, W32API.HDC hDc, int dwUser);
    }

    public static interface FStdDataCallBack extends StdCallCallback {

        public void invoke(NativeLong lRealHandle, int dwDataType, ByteByReference pBuffer, int dwBufSize, int dwUser);
    }

    public static interface FPlayDataCallBack extends StdCallCallback {

        public void invoke(NativeLong lPlayHandle, int dwDataType, ByteByReference pBuffer, int dwBufSize, int dwUser);
    }

    public static interface FVoiceDataCallBack extends StdCallCallback {

        public void invoke(
                NativeLong lVoiceComHandle, String pRecvDataBuffer, int dwBufSize, byte byAudioFlag, int dwUser);
    }

    public static interface FVoiceDataCallBack_V30 extends StdCallCallback {

        public void invoke(
                NativeLong lVoiceComHandle, String pRecvDataBuffer, int dwBufSize, byte byAudioFlag, Pointer pUser);
    }

    public static interface FVoiceDataCallBack_MR extends StdCallCallback {

        public void invoke(
                NativeLong lVoiceComHandle, String pRecvDataBuffer, int dwBufSize, byte byAudioFlag, int dwUser);
    }

    public static interface FVoiceDataCallBack_MR_V30 extends StdCallCallback {

        public void invoke(
                NativeLong lVoiceComHandle, String pRecvDataBuffer, int dwBufSize, byte byAudioFlag, String pUser);
    }

    public static interface FVoiceDataCallBack2 extends StdCallCallback {

        public void invoke(String pRecvDataBuffer, int dwBufSize, Pointer pUser);
    }

    public static interface FSerialDataCallBack extends StdCallCallback {

        public void invoke(NativeLong lSerialHandle, String pRecvDataBuffer, int dwBufSize, int dwUser);
    }

    public static interface FRowDataCallBack extends StdCallCallback {

        public void invoke(
                NativeLong lUserID,
                String sIPAddr,
                NativeLong lRowAmout,
                String pRecvDataBuffer,
                int dwBufSize,
                int dwUser);
    }

    public static interface FColLocalDataCallBack extends StdCallCallback {

        public void invoke(
                NativeLong lUserID,
                String sIPAddr,
                NativeLong lColumnAmout,
                String pRecvDataBuffer,
                int dwBufSize,
                int dwUser);
    }

    public static interface FColGlobalDataCallBack extends StdCallCallback {

        public void invoke(
                NativeLong lUserID,
                String sIPAddr,
                NativeLong lColumnAmout,
                String pRecvDataBuffer,
                int dwBufSize,
                int dwUser);
    }

    public static interface FJpegdataCallBack extends StdCallCallback {

        public int invoke(
                NativeLong lCommand,
                NativeLong lUserID,
                String sDVRIP,
                String sJpegName,
                String pJpegBuf,
                int dwBufLen,
                int dwUser);
    }

    public static interface FPostMessageCallBack extends StdCallCallback {

        public int invoke(int dwType, NativeLong lIndex);
    }

    boolean NET_DVR_Init();

    boolean NET_DVR_Cleanup();

    boolean NET_DVR_SetDVRMessage(int nMessage, int hWnd);

    // NET_DVR_SetDVRMessage的扩展
    boolean NET_DVR_SetExceptionCallBack_V30(
            int nMessage, int hWnd, FExceptionCallBack fExceptionCallBack, Pointer pUser);

    boolean NET_DVR_SetDVRMessCallBack(FMessCallBack fMessCallBack);

    boolean NET_DVR_SetDVRMessCallBack_EX(FMessCallBack_EX fMessCallBack_EX);

    boolean NET_DVR_SetDVRMessCallBack_NEW(FMessCallBack_NEW fMessCallBack_NEW);

    boolean NET_DVR_SetDVRMessageCallBack(FMessageCallBack fMessageCallBack, int dwUser);

    boolean NET_DVR_SetDVRMessageCallBack_V30(FMSGCallBack fMessageCallBack, Pointer pUser);

    boolean NET_DVR_SetConnectTime(int dwWaitTime, int dwTryTimes);

    boolean NET_DVR_SetReconnect(int dwInterval, boolean bEnableRecon);

    int NET_DVR_GetSDKVersion();

    int NET_DVR_GetSDKBuildVersion();

    int NET_DVR_IsSupport();

    boolean NET_DVR_StartListen(String sLocalIP, short wLocalPort);

    boolean NET_DVR_StopListen();

    NativeLong NET_DVR_StartListen_V30(String sLocalIP, short wLocalPort, FMSGCallBack DataCallback, Pointer pUserData);

    boolean NET_DVR_StopListen_V30(NativeLong lListenHandle);

    NativeLong NET_DVR_Login(
            String sDVRIP, short wDVRPort, String sUserName, String sPassword, NET_DVR_DEVICEINFO lpDeviceInfo);

    NativeLong NET_DVR_Login_V30(
            String sDVRIP, short wDVRPort, String sUserName, String sPassword, NET_DVR_DEVICEINFO_V30 lpDeviceInfo);

    boolean NET_DVR_Logout(NativeLong lUserID);

    boolean NET_DVR_Logout_V30(NativeLong lUserID);

    int NET_DVR_GetLastError();

    String NET_DVR_GetErrorMsg(NativeLongByReference pErrorNo);

    boolean NET_DVR_SetShowMode(int dwShowType, int colorKey);

    boolean NET_DVR_GetDVRIPByResolveSvr(
            String sServerIP,
            short wServerPort,
            String sDVRName,
            short wDVRNameLen,
            String sDVRSerialNumber,
            short wDVRSerialLen,
            String sGetIP);

    boolean NET_DVR_GetDVRIPByResolveSvr_EX(
            String sServerIP,
            short wServerPort,
            String sDVRName,
            short wDVRNameLen,
            String sDVRSerialNumber,
            short wDVRSerialLen,
            String sGetIP,
            IntByReference dwPort);

    // 预览相关接口
    NativeLong NET_DVR_RealPlay(NativeLong lUserID, NET_DVR_CLIENTINFO lpClientInfo);

    NativeLong NET_DVR_RealPlay_V30(
            NativeLong lUserID,
            NET_DVR_CLIENTINFO lpClientInfo,
            FRealDataCallBack_V30 fRealDataCallBack_V30,
            Pointer pUser,
            boolean bBlocked);

    boolean NET_DVR_StopRealPlay(NativeLong lRealHandle);

    boolean NET_DVR_RigisterDrawFun(NativeLong lRealHandle, FDrawFun fDrawFun, int dwUser);

    boolean NET_DVR_SetPlayerBufNumber(NativeLong lRealHandle, int dwBufNum);

    boolean NET_DVR_ThrowBFrame(NativeLong lRealHandle, int dwNum);

    boolean NET_DVR_SetAudioMode(int dwMode);

    boolean NET_DVR_OpenSound(NativeLong lRealHandle);

    boolean NET_DVR_CloseSound();

    boolean NET_DVR_OpenSoundShare(NativeLong lRealHandle);

    boolean NET_DVR_CloseSoundShare(NativeLong lRealHandle);

    boolean NET_DVR_Volume(NativeLong lRealHandle, short wVolume);

    boolean NET_DVR_SaveRealData(NativeLong lRealHandle, String sFileName);

    boolean NET_DVR_StopSaveRealData(NativeLong lRealHandle);

    boolean NET_DVR_SetRealDataCallBack(NativeLong lRealHandle, FRowDataCallBack fRealDataCallBack, int dwUser);

    boolean NET_DVR_SetStandardDataCallBack(NativeLong lRealHandle, FStdDataCallBack fStdDataCallBack, int dwUser);

    boolean NET_DVR_CapturePicture(NativeLong lRealHandle, String sPicFileName); // bmp

    // 动态生成I帧
    boolean NET_DVR_MakeKeyFrame(NativeLong lUserID, NativeLong lChannel); // 主码流

    boolean NET_DVR_MakeKeyFrameSub(NativeLong lUserID, NativeLong lChannel); // 子码流

    // 云台控制相关接口
    boolean NET_DVR_PTZControl(NativeLong lRealHandle, int dwPTZCommand, int dwStop);

    boolean NET_DVR_PTZControl_Other(NativeLong lUserID, NativeLong lChannel, int dwPTZCommand, int dwStop);

    boolean NET_DVR_TransPTZ(NativeLong lRealHandle, String pPTZCodeBuf, int dwBufSize);

    boolean NET_DVR_TransPTZ_Other(NativeLong lUserID, NativeLong lChannel, String pPTZCodeBuf, int dwBufSize);

    boolean NET_DVR_PTZPreset(NativeLong lRealHandle, int dwPTZPresetCmd, int dwPresetIndex);

    boolean NET_DVR_PTZPreset_Other(NativeLong lUserID, NativeLong lChannel, int dwPTZPresetCmd, int dwPresetIndex);

    boolean NET_DVR_TransPTZ_EX(NativeLong lRealHandle, String pPTZCodeBuf, int dwBufSize);

    boolean NET_DVR_PTZControl_EX(NativeLong lRealHandle, int dwPTZCommand, int dwStop);

    boolean NET_DVR_PTZPreset_EX(NativeLong lRealHandle, int dwPTZPresetCmd, int dwPresetIndex);

    boolean NET_DVR_PTZCruise(
            NativeLong lRealHandle, int dwPTZCruiseCmd, byte byCruiseRoute, byte byCruisePoint, short wInput);

    boolean NET_DVR_PTZCruise_Other(
            NativeLong lUserID,
            NativeLong lChannel,
            int dwPTZCruiseCmd,
            byte byCruiseRoute,
            byte byCruisePoint,
            short wInput);

    boolean NET_DVR_PTZCruise_EX(
            NativeLong lRealHandle, int dwPTZCruiseCmd, byte byCruiseRoute, byte byCruisePoint, short wInput);

    boolean NET_DVR_PTZTrack(NativeLong lRealHandle, int dwPTZTrackCmd);

    boolean NET_DVR_PTZTrack_Other(NativeLong lUserID, NativeLong lChannel, int dwPTZTrackCmd);

    boolean NET_DVR_PTZTrack_EX(NativeLong lRealHandle, int dwPTZTrackCmd);

    boolean NET_DVR_PTZControlWithSpeed(NativeLong lRealHandle, int dwPTZCommand, int dwStop, int dwSpeed);

    boolean NET_DVR_PTZControlWithSpeed_Other(
            NativeLong lUserID, NativeLong lChannel, int dwPTZCommand, int dwStop, int dwSpeed);

    boolean NET_DVR_PTZControlWithSpeed_EX(NativeLong lRealHandle, int dwPTZCommand, int dwStop, int dwSpeed);

    boolean NET_DVR_GetPTZCruise(
            NativeLong lUserID, NativeLong lChannel, NativeLong lCruiseRoute, NET_DVR_CRUISE_RET lpCruiseRet);

    boolean NET_DVR_PTZMltTrack(NativeLong lRealHandle, int dwPTZTrackCmd, int dwTrackIndex);

    boolean NET_DVR_PTZMltTrack_Other(NativeLong lUserID, NativeLong lChannel, int dwPTZTrackCmd, int dwTrackIndex);

    boolean NET_DVR_PTZMltTrack_EX(NativeLong lRealHandle, int dwPTZTrackCmd, int dwTrackIndex);

    // 文件查找与回放
    NativeLong NET_DVR_FindFile(
            NativeLong lUserID, NativeLong lChannel, int dwFileType, NET_DVR_TIME lpStartTime, NET_DVR_TIME lpStopTime);

    NativeLong NET_DVR_FindNextFile(NativeLong lFindHandle, NET_DVR_FIND_DATA lpFindData);

    boolean NET_DVR_FindClose(NativeLong lFindHandle);

    NativeLong NET_DVR_FindNextFile_V30(NativeLong lFindHandle, NET_DVR_FINDDATA_V30 lpFindData);

    NativeLong NET_DVR_FindFile_V30(NativeLong lUserID, NET_DVR_FILECOND pFindCond);

    boolean NET_DVR_FindClose_V30(NativeLong lFindHandle);

    // 2007-04-16增加查询结果带卡号的文件查找
    NativeLong NET_DVR_FindNextFile_Card(NativeLong lFindHandle, NET_DVR_FINDDATA_CARD lpFindData);

    NativeLong NET_DVR_FindFile_Card(
            NativeLong lUserID, NativeLong lChannel, int dwFileType, NET_DVR_TIME lpStartTime, NET_DVR_TIME lpStopTime);

    boolean NET_DVR_LockFileByName(NativeLong lUserID, String sLockFileName);

    boolean NET_DVR_UnlockFileByName(NativeLong lUserID, String sUnlockFileName);

    NativeLong NET_DVR_PlayBackByName(NativeLong lUserID, String sPlayBackFileName, HWND hWnd);

    NativeLong NET_DVR_PlayBackByTime(
            NativeLong lUserID, NativeLong lChannel, NET_DVR_TIME lpStartTime, NET_DVR_TIME lpStopTime, HWND hWnd);

    boolean NET_DVR_PlayBackControl(
            NativeLong lPlayHandle, int dwControlCode, int dwInValue, IntByReference LPOutValue);

    boolean NET_DVR_StopPlayBack(NativeLong lPlayHandle);

    boolean NET_DVR_SetPlayDataCallBack(NativeLong lPlayHandle, FPlayDataCallBack fPlayDataCallBack, int dwUser);

    boolean NET_DVR_PlayBackSaveData(NativeLong lPlayHandle, String sFileName);

    boolean NET_DVR_StopPlayBackSave(NativeLong lPlayHandle);

    boolean NET_DVR_GetPlayBackOsdTime(NativeLong lPlayHandle, NET_DVR_TIME lpOsdTime);

    boolean NET_DVR_PlayBackCaptureFile(NativeLong lPlayHandle, String sFileName);

    NativeLong NET_DVR_GetFileByName(NativeLong lUserID, String sDVRFileName, String sSavedFileName);

    NativeLong NET_DVR_GetFileByTime(
            NativeLong lUserID,
            NativeLong lChannel,
            NET_DVR_TIME lpStartTime,
            NET_DVR_TIME lpStopTime,
            String sSavedFileName);

    boolean NET_DVR_StopGetFile(NativeLong lFileHandle);

    int NET_DVR_GetDownloadPos(NativeLong lFileHandle);

    int NET_DVR_GetPlayBackPos(NativeLong lPlayHandle);

    // 升级
    NativeLong NET_DVR_Upgrade(NativeLong lUserID, String sFileName);

    int NET_DVR_GetUpgradeState(NativeLong lUpgradeHandle);

    int NET_DVR_GetUpgradeProgress(NativeLong lUpgradeHandle);

    boolean NET_DVR_CloseUpgradeHandle(NativeLong lUpgradeHandle);

    boolean NET_DVR_SetNetworkEnvironment(int dwEnvironmentLevel);

    // 远程格式化硬盘
    NativeLong NET_DVR_FormatDisk(NativeLong lUserID, NativeLong lDiskNumber);

    boolean NET_DVR_GetFormatProgress(
            NativeLong lFormatHandle,
            NativeLongByReference pCurrentFormatDisk,
            NativeLongByReference pCurrentDiskPos,
            NativeLongByReference pFormatStatic);

    boolean NET_DVR_CloseFormatHandle(NativeLong lFormatHandle);

    // 报警
    NativeLong NET_DVR_SetupAlarmChan(NativeLong lUserID);

    boolean NET_DVR_CloseAlarmChan(NativeLong lAlarmHandle);

    NativeLong NET_DVR_SetupAlarmChan_V30(NativeLong lUserID);

    boolean NET_DVR_CloseAlarmChan_V30(NativeLong lAlarmHandle);

    // 语音对讲
    NativeLong NET_DVR_StartVoiceCom(NativeLong lUserID, FVoiceDataCallBack fVoiceDataCallBack, int dwUser);

    NativeLong NET_DVR_StartVoiceCom_V30(
            NativeLong lUserID,
            int dwVoiceChan,
            boolean bNeedCBNoEncData,
            FVoiceDataCallBack_V30 fVoiceDataCallBack,
            Pointer pUser);

    boolean NET_DVR_SetVoiceComClientVolume(NativeLong lVoiceComHandle, short wVolume);

    boolean NET_DVR_StopVoiceCom(NativeLong lVoiceComHandle);

    // 语音转发
    NativeLong NET_DVR_StartVoiceCom_MR(NativeLong lUserID, FVoiceDataCallBack_MR fVoiceDataCallBack, int dwUser);

    NativeLong NET_DVR_StartVoiceCom_MR_V30(
            NativeLong lUserID, int dwVoiceChan, FVoiceDataCallBack_MR_V30 fVoiceDataCallBack, Pointer pUser);

    boolean NET_DVR_VoiceComSendData(NativeLong lVoiceComHandle, String pSendBuf, int dwBufSize);

    // 语音广播
    boolean NET_DVR_ClientAudioStart();

    boolean NET_DVR_ClientAudioStart_V30(FVoiceDataCallBack2 fVoiceDataCallBack2, Pointer pUser);

    boolean NET_DVR_ClientAudioStop();

    boolean NET_DVR_AddDVR(NativeLong lUserID);

    NativeLong NET_DVR_AddDVR_V30(NativeLong lUserID, int dwVoiceChan);

    boolean NET_DVR_DelDVR(NativeLong lUserID);

    boolean NET_DVR_DelDVR_V30(NativeLong lVoiceHandle);

    ////////////////////////////////////////////////////////////
    // 透明通道设置
    NativeLong NET_DVR_SerialStart(
            NativeLong lUserID, NativeLong lSerialPort, FSerialDataCallBack fSerialDataCallBack, int dwUser);

    // 485作为透明通道时，需要指明通道号，因为不同通道号485的设置可以不同(比如波特率)
    boolean NET_DVR_SerialSend(NativeLong lSerialHandle, NativeLong lChannel, String pSendBuf, int dwBufSize);

    boolean NET_DVR_SerialStop(NativeLong lSerialHandle);

    boolean NET_DVR_SendTo232Port(NativeLong lUserID, String pSendBuf, int dwBufSize);

    boolean NET_DVR_SendToSerialPort(
            NativeLong lUserID, int dwSerialPort, int dwSerialIndex, String pSendBuf, int dwBufSize);

    // 解码 nBitrate = 16000
    Pointer NET_DVR_InitG722Decoder(int nBitrate);

    void NET_DVR_ReleaseG722Decoder(Pointer pDecHandle);

    boolean NET_DVR_DecodeG722Frame(Pointer pDecHandle, String pInBuffer, String pOutBuffer);

    // 编码
    Pointer NET_DVR_InitG722Encoder();

    boolean NET_DVR_EncodeG722Frame(Pointer pEncodeHandle, String pInBuff, String pOutBuffer);

    void NET_DVR_ReleaseG722Encoder(Pointer pEncodeHandle);

    // 远程控制本地显示
    boolean NET_DVR_ClickKey(NativeLong lUserID, NativeLong lKeyIndex);

    // 远程控制设备端手动录像
    boolean NET_DVR_StartDVRRecord(NativeLong lUserID, NativeLong lChannel, NativeLong lRecordType);

    boolean NET_DVR_StopDVRRecord(NativeLong lUserID, NativeLong lChannel);

    // 解码卡
    boolean NET_DVR_InitDevice_Card(NativeLongByReference pDeviceTotalChan);

    boolean NET_DVR_ReleaseDevice_Card();

    boolean NET_DVR_InitDDraw_Card(int hParent, int colorKey);

    boolean NET_DVR_ReleaseDDraw_Card();

    NativeLong NET_DVR_RealPlay_Card(NativeLong lUserID, NET_DVR_CARDINFO lpCardInfo, NativeLong lChannelNum);

    boolean NET_DVR_ResetPara_Card(NativeLong lRealHandle, NET_DVR_DISPLAY_PARA lpDisplayPara);

    boolean NET_DVR_RefreshSurface_Card();

    boolean NET_DVR_ClearSurface_Card();

    boolean NET_DVR_RestoreSurface_Card();

    boolean NET_DVR_OpenSound_Card(NativeLong lRealHandle);

    boolean NET_DVR_CloseSound_Card(NativeLong lRealHandle);

    boolean NET_DVR_SetVolume_Card(NativeLong lRealHandle, short wVolume);

    boolean NET_DVR_AudioPreview_Card(NativeLong lRealHandle, boolean bEnable);

    NativeLong NET_DVR_GetCardLastError_Card();

    Pointer NET_DVR_GetChanHandle_Card(NativeLong lRealHandle);

    boolean NET_DVR_CapturePicture_Card(NativeLong lRealHandle, String sPicFileName);

    // 获取解码卡序列号此接口无效，改用GetBoardDetail接口获得(2005-12-08支持)
    boolean NET_DVR_GetSerialNum_Card(NativeLong lChannelNum, IntByReference pDeviceSerialNo);

    // 日志
    NativeLong NET_DVR_FindDVRLog(
            NativeLong lUserID,
            NativeLong lSelectMode,
            int dwMajorType,
            int dwMinorType,
            NET_DVR_TIME lpStartTime,
            NET_DVR_TIME lpStopTime);

    NativeLong NET_DVR_FindNextLog(NativeLong lLogHandle, NET_DVR_LOG lpLogData);

    boolean NET_DVR_FindLogClose(NativeLong lLogHandle);

    NativeLong NET_DVR_FindDVRLog_V30(
            NativeLong lUserID,
            NativeLong lSelectMode,
            int dwMajorType,
            int dwMinorType,
            NET_DVR_TIME lpStartTime,
            NET_DVR_TIME lpStopTime,
            boolean bOnlySmart);

    NativeLong NET_DVR_FindNextLog_V30(NativeLong lLogHandle, NET_DVR_LOG_V30 lpLogData);

    boolean NET_DVR_FindLogClose_V30(NativeLong lLogHandle);

    // 截止2004年8月5日,共113个接口
    // ATM DVR
    NativeLong NET_DVR_FindFileByCard(
            NativeLong lUserID,
            NativeLong lChannel,
            int dwFileType,
            int nFindType,
            String sCardNumber,
            NET_DVR_TIME lpStartTime,
            NET_DVR_TIME lpStopTime);
    // 截止2004年10月5日,共116个接口

    // 2005-09-15
    boolean NET_DVR_CaptureJPEGPicture(
            NativeLong lUserID, NativeLong lChannel, NET_DVR_JPEGPARA lpJpegPara, String sPicFileName);

    // JPEG抓图到内存
    boolean NET_DVR_CaptureJPEGPicture_NEW(
            NativeLong lUserID,
            NativeLong lChannel,
            NET_DVR_JPEGPARA lpJpegPara,
            String sJpegPicBuffer,
            int dwPicSize,
            IntByReference lpSizeReturned);

    // 2006-02-16
    int NET_DVR_GetRealPlayerIndex(NativeLong lRealHandle);

    int NET_DVR_GetPlayBackPlayerIndex(NativeLong lPlayHandle);

    // 2006-08-28 704-640 缩放配置
    boolean NET_DVR_SetScaleCFG(NativeLong lUserID, int dwScale);

    boolean NET_DVR_GetScaleCFG(NativeLong lUserID, IntByReference lpOutScale);

    boolean NET_DVR_SetScaleCFG_V30(NativeLong lUserID, NET_DVR_SCALECFG pScalecfg);

    boolean NET_DVR_GetScaleCFG_V30(NativeLong lUserID, NET_DVR_SCALECFG pScalecfg);

    // 2006-08-28 ATM机端口设置
    boolean NET_DVR_SetATMPortCFG(NativeLong lUserID, short wATMPort);

    boolean NET_DVR_GetATMPortCFG(NativeLong lUserID, ShortByReference LPOutATMPort);

    // 2006-11-10 支持显卡辅助输出
    boolean NET_DVR_InitDDrawDevice();

    boolean NET_DVR_ReleaseDDrawDevice();

    NativeLong NET_DVR_GetDDrawDeviceTotalNums();

    boolean NET_DVR_SetDDrawDevice(NativeLong lPlayPort, int nDeviceNum);

    boolean NET_DVR_PTZSelZoomIn(NativeLong lRealHandle, NET_DVR_POINT_FRAME pStruPointFrame);

    boolean NET_DVR_PTZSelZoomIn_EX(NativeLong lUserID, NativeLong lChannel, NET_DVR_POINT_FRAME pStruPointFrame);

    // 解码设备DS-6001D/DS-6001F
    boolean NET_DVR_StartDecode(NativeLong lUserID, NativeLong lChannel, NET_DVR_DECODERINFO lpDecoderinfo);

    boolean NET_DVR_StopDecode(NativeLong lUserID, NativeLong lChannel);

    boolean NET_DVR_GetDecoderState(NativeLong lUserID, NativeLong lChannel, NET_DVR_DECODERSTATE lpDecoderState);

    // 2005-08-01
    boolean NET_DVR_SetDecInfo(NativeLong lUserID, NativeLong lChannel, NET_DVR_DECCFG lpDecoderinfo);

    boolean NET_DVR_GetDecInfo(NativeLong lUserID, NativeLong lChannel, NET_DVR_DECCFG lpDecoderinfo);

    boolean NET_DVR_SetDecTransPort(NativeLong lUserID, NET_DVR_PORTCFG lpTransPort);

    boolean NET_DVR_GetDecTransPort(NativeLong lUserID, NET_DVR_PORTCFG lpTransPort);

    boolean NET_DVR_DecPlayBackCtrl(
            NativeLong lUserID,
            NativeLong lChannel,
            int dwControlCode,
            int dwInValue,
            IntByReference LPOutValue,
            NET_DVR_PLAYREMOTEFILE lpRemoteFileInfo);

    boolean NET_DVR_StartDecSpecialCon(NativeLong lUserID, NativeLong lChannel, NET_DVR_DECCHANINFO lpDecChanInfo);

    boolean NET_DVR_StopDecSpecialCon(NativeLong lUserID, NativeLong lChannel, NET_DVR_DECCHANINFO lpDecChanInfo);

    boolean NET_DVR_DecCtrlDec(NativeLong lUserID, NativeLong lChannel, int dwControlCode);

    boolean NET_DVR_DecCtrlScreen(NativeLong lUserID, NativeLong lChannel, int dwControl);

    boolean NET_DVR_GetDecCurLinkStatus(NativeLong lUserID, NativeLong lChannel, NET_DVR_DECSTATUS lpDecStatus);

    // 多路解码器
    // 2007-11-30 V211支持以下接口 //11
    boolean NET_DVR_MatrixStartDynamic(NativeLong lUserID, int dwDecChanNum, NET_DVR_MATRIX_DYNAMIC_DEC lpDynamicInfo);

    boolean NET_DVR_MatrixStopDynamic(NativeLong lUserID, int dwDecChanNum);

    boolean NET_DVR_MatrixGetDecChanInfo(NativeLong lUserID, int dwDecChanNum, NET_DVR_MATRIX_DEC_CHAN_INFO lpInter);

    boolean NET_DVR_MatrixSetLoopDecChanInfo(NativeLong lUserID, int dwDecChanNum, NET_DVR_MATRIX_LOOP_DECINFO lpInter);

    boolean NET_DVR_MatrixGetLoopDecChanInfo(NativeLong lUserID, int dwDecChanNum, NET_DVR_MATRIX_LOOP_DECINFO lpInter);

    boolean NET_DVR_MatrixSetLoopDecChanEnable(NativeLong lUserID, int dwDecChanNum, int dwEnable);

    boolean NET_DVR_MatrixGetLoopDecChanEnable(NativeLong lUserID, int dwDecChanNum, IntByReference lpdwEnable);

    boolean NET_DVR_MatrixGetLoopDecEnable(NativeLong lUserID, IntByReference lpdwEnable);

    boolean NET_DVR_MatrixSetDecChanEnable(NativeLong lUserID, int dwDecChanNum, int dwEnable);

    boolean NET_DVR_MatrixGetDecChanEnable(NativeLong lUserID, int dwDecChanNum, IntByReference lpdwEnable);

    boolean NET_DVR_MatrixGetDecChanStatus(
            NativeLong lUserID, int dwDecChanNum, NET_DVR_MATRIX_DEC_CHAN_STATUS lpInter);

    // 2007-12-22 增加支持接口 //18
    boolean NET_DVR_MatrixSetTranInfo(NativeLong lUserID, NET_DVR_MATRIX_TRAN_CHAN_CONFIG lpTranInfo);

    boolean NET_DVR_MatrixGetTranInfo(NativeLong lUserID, NET_DVR_MATRIX_TRAN_CHAN_CONFIG lpTranInfo);

    boolean NET_DVR_MatrixSetRemotePlay(NativeLong lUserID, int dwDecChanNum, NET_DVR_MATRIX_DEC_REMOTE_PLAY lpInter);

    boolean NET_DVR_MatrixSetRemotePlayControl(
            NativeLong lUserID, int dwDecChanNum, int dwControlCode, int dwInValue, IntByReference LPOutValue);

    boolean NET_DVR_MatrixGetRemotePlayStatus(
            NativeLong lUserID, int dwDecChanNum, NET_DVR_MATRIX_DEC_REMOTE_PLAY_STATUS lpOuter);

    // end
    boolean NET_DVR_RefreshPlay(NativeLong lPlayHandle);

    // 恢复默认值
    boolean NET_DVR_RestoreConfig(NativeLong lUserID);

    // 保存参数
    boolean NET_DVR_SaveConfig(NativeLong lUserID);

    // 重启
    boolean NET_DVR_RebootDVR(NativeLong lUserID);

    // 关闭DVR
    boolean NET_DVR_ShutDownDVR(NativeLong lUserID);

    // 参数配置 begin
    boolean NET_DVR_GetDVRConfig(
            NativeLong lUserID,
            int dwCommand,
            NativeLong lChannel,
            Pointer lpOutBuffer,
            int dwOutBufferSize,
            IntByReference lpBytesReturned);

    boolean NET_DVR_SetDVRConfig(
            NativeLong lUserID, int dwCommand, NativeLong lChannel, Pointer lpInBuffer, int dwInBufferSize);

    boolean NET_DVR_GetDVRWorkState_V30(NativeLong lUserID, NET_DVR_WORKSTATE_V30 lpWorkState);

    boolean NET_DVR_GetDVRWorkState(NativeLong lUserID, NET_DVR_WORKSTATE lpWorkState);

    boolean NET_DVR_SetVideoEffect(
            NativeLong lUserID,
            NativeLong lChannel,
            int dwBrightValue,
            int dwContrastValue,
            int dwSaturationValue,
            int dwHueValue);

    boolean NET_DVR_GetVideoEffect(
            NativeLong lUserID,
            NativeLong lChannel,
            IntByReference pBrightValue,
            IntByReference pContrastValue,
            IntByReference pSaturationValue,
            IntByReference pHueValue);

    boolean NET_DVR_ClientGetframeformat(NativeLong lUserID, NET_DVR_FRAMEFORMAT lpFrameFormat);

    boolean NET_DVR_ClientSetframeformat(NativeLong lUserID, NET_DVR_FRAMEFORMAT lpFrameFormat);

    boolean NET_DVR_ClientGetframeformat_V30(NativeLong lUserID, NET_DVR_FRAMEFORMAT_V30 lpFrameFormat);

    boolean NET_DVR_ClientSetframeformat_V30(NativeLong lUserID, NET_DVR_FRAMEFORMAT_V30 lpFrameFormat);

    boolean NET_DVR_GetAlarmOut_V30(NativeLong lUserID, NET_DVR_ALARMOUTSTATUS_V30 lpAlarmOutState);

    boolean NET_DVR_GetAlarmOut(NativeLong lUserID, NET_DVR_ALARMOUTSTATUS lpAlarmOutState);

    boolean NET_DVR_SetAlarmOut(NativeLong lUserID, NativeLong lAlarmOutPort, NativeLong lAlarmOutStatic);

    // 视频参数调节
    boolean NET_DVR_ClientSetVideoEffect(
            NativeLong lRealHandle, int dwBrightValue, int dwContrastValue, int dwSaturationValue, int dwHueValue);

    boolean NET_DVR_ClientGetVideoEffect(
            NativeLong lRealHandle,
            IntByReference pBrightValue,
            IntByReference pContrastValue,
            IntByReference pSaturationValue,
            IntByReference pHueValue);

    // 配置文件
    boolean NET_DVR_GetConfigFile(NativeLong lUserID, String sFileName);

    boolean NET_DVR_SetConfigFile(NativeLong lUserID, String sFileName);

    boolean NET_DVR_GetConfigFile_V30(NativeLong lUserID, String sOutBuffer, int dwOutSize, IntByReference pReturnSize);

    boolean NET_DVR_GetConfigFile_EX(NativeLong lUserID, String sOutBuffer, int dwOutSize);

    boolean NET_DVR_SetConfigFile_EX(NativeLong lUserID, String sInBuffer, int dwInSize);

    // 启用日志文件写入接口
    boolean NET_DVR_SetLogToFile(boolean bLogEnable, String strLogDir, boolean bAutoDel);

    boolean NET_DVR_GetSDKState(NET_DVR_SDKSTATE pSDKState);

    boolean NET_DVR_GetSDKAbility(NET_DVR_SDKABL pSDKAbl);

    boolean NET_DVR_GetPTZProtocol(NativeLong lUserID, NET_DVR_PTZCFG pPtzcfg);

    // 前面板锁定
    boolean NET_DVR_LockPanel(NativeLong lUserID);

    boolean NET_DVR_UnLockPanel(NativeLong lUserID);

    boolean NET_DVR_SetRtspConfig(NativeLong lUserID, int dwCommand, NET_DVR_RTSPCFG lpInBuffer, int dwInBufferSize);

    boolean NET_DVR_GetRtspConfig(NativeLong lUserID, int dwCommand, NET_DVR_RTSPCFG lpOutBuffer, int dwOutBufferSize);
}

// 播放库函数声明,PlayCtrl.dll
interface PlayCtrl extends StdCallLibrary {

    PlayCtrl INSTANCE = (PlayCtrl) Native.loadLibrary("PlayCtrl", PlayCtrl.class);

    public static final int STREAME_REALTIME = 0;
    public static final int STREAME_FILE = 1;

    boolean PlayM4_GetPort(NativeLongByReference nPort);

    boolean PlayM4_OpenStream(NativeLong nPort, ByteByReference pFileHeadBuf, int nSize, int nBufPoolSize);

    boolean PlayM4_InputData(NativeLong nPort, ByteByReference pBuf, int nSize);

    boolean PlayM4_CloseStream(NativeLong nPort);

    boolean PlayM4_SetStreamOpenMode(NativeLong nPort, int nMode);

    boolean PlayM4_Play(NativeLong nPort, HWND hWnd);

    boolean PlayM4_Stop(NativeLong nPort);

    boolean PlayM4_SetSecretKey(NativeLong nPort, NativeLong lKeyType, String pSecretKey, NativeLong lKeyLen);
}

// windows gdi接口,gdi32.dll in system32 folder, 在设置遮挡区域,移动侦测区域等情况下使用
interface GDI32 extends W32API {

    GDI32 INSTANCE = (GDI32) Native.loadLibrary("gdi32", GDI32.class, DEFAULT_OPTIONS);

    public static final int TRANSPARENT = 1;

    int SetBkMode(HDC hdc, int i);

    HANDLE CreateSolidBrush(int icolor);
}

// windows user32接口,user32.dll in system32 folder, 在设置遮挡区域,移动侦测区域等情况下使用
interface USER32 extends W32API {

    USER32 INSTANCE = (USER32) Native.loadLibrary("user32", USER32.class, DEFAULT_OPTIONS);

    public static final int BF_LEFT = 0x0001;
    public static final int BF_TOP = 0x0002;
    public static final int BF_RIGHT = 0x0004;
    public static final int BF_BOTTOM = 0x0008;
    public static final int BDR_SUNKENOUTER = 0x0002;
    public static final int BF_RECT = (BF_LEFT | BF_TOP | BF_RIGHT | BF_BOTTOM);

    boolean DrawEdge(HDC hdc, RECT qrc, int edge, int grfFlags);

    int FillRect(HDC hDC, RECT lprc, HANDLE hbr);
}
