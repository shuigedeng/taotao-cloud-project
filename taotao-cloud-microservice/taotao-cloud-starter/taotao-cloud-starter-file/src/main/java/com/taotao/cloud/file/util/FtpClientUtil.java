package com.taotao.cloud.file.util;

import com.taotao.cloud.common.utils.LogUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

/**
 * ftp客户端工具类
 *
 * @author dengtao
 * @date 2020/11/12 16:36
 * @since v1.0
 */
public class FtpClientUtil {

	private static final Logger logger = LoggerFactory.getLogger("FtpClient");
	private String host;
	private String port;
	private String username;
	private String passwd;
	private FTPClient client = null;
	private boolean isLogin = false;
	private InputStream input = null;
	private String remoteDir = null;
	private String ftpHome = null;
	private String controlEncoding = null;

	public FtpClientUtil(String host, String port, String username, String passwd, String remoteDir) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.passwd = passwd;
		this.remoteDir = remoteDir;
		init();
	}

	public FtpClientUtil(String host, String port, String username, String passwd) {
		this(host, port, username, passwd, null);
	}

	/**
	 * 初始化APACHE的FTPClient
	 */
	private void init() {
		try {
			this.client = new FTPClient();
//			获取服务器编码
			this.controlEncoding = client.getControlEncoding();

			this.client.connect(this.host, Integer.parseInt(this.port));
			this.isLogin = this.client.login(this.username, this.passwd);
			int reply = this.client.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				this.isLogin = false;
				destroy();
				LogUtil.info("FTPClient连接失败！");
			} else {
				this.client.setFileType(FTPClient.BINARY_FILE_TYPE);
				this.ftpHome = this.client.printWorkingDirectory();
				this.client.makeDirectory(this.remoteDir);
				this.client.changeWorkingDirectory(this.remoteDir);

			}
		} catch (Exception e) {
			LogUtil.info("FTP初始化异常：" + e.getMessage(), e);
		}
	}

	/**
	 * 销毁FTP
	 */
	public void destroy() {
		if (this.client == null) {
			return;
		}
		try {
			this.client.logout();
		} catch (IOException e) {
			LogUtil.info("FTP注销异常：" + e.getMessage(), e);
		} finally {
			try {
				if (this.client.isConnected()) {
					this.client.disconnect();
				}
			} catch (IOException e) {
				LogUtil.info(e + "");
			} finally {
				this.client = null;
			}
		}
	}

	/**
	 * 小文件上传
	 *
	 * @param remoteDir
	 * @param rName
	 * @param lFile
	 * @return
	 */
	public boolean upload(String remoteDir, String rName, File lFile) {
		String sremoteDir = this.ConvertEncoding(remoteDir);
		String srName = this.ConvertEncoding(rName);

		boolean result = false;
		if (!this.isLogin) {
			reInit();
			LogUtil.info("FTP未登录，重新初始化。。。");
		}
		if (this.isLogin) {
			try {
				this.input = new FileInputStream(lFile);
				if (sremoteDir != null && sremoteDir.length() > 0) {
					boolean bb = this.client.changeWorkingDirectory(sremoteDir);
					LogUtil.info("远程地址:" + sremoteDir + ",切换结果:" + bb);
				}
				// 设置被动模式
				this.client.enterLocalPassiveMode();
				result = client.storeFile(srName, input);
				LogUtil.info("文件[" + lFile.getPath() + "]上传结果：" + result);
			} catch (Exception e) {
				result = false;
				LogUtil.info(e + "");
			} finally {
				try {
					this.input.close();
					this.input = null;
				} catch (IOException e) {
					LogUtil.info(e + "");
				}
			}
		}
		return result;
	}

	/**
	 * 大文件上传
	 *
	 * @param remoteDir
	 * @param rName     远程文件名
	 * @param lFile     本地文件
	 * @return
	 */
	public boolean uploadBigFiles(String remoteDir, String rName, File lFile) {
		String sremoteDir = this.ConvertEncoding(remoteDir);
		String srName = this.ConvertEncoding(rName);
		boolean result = false;
		if (!this.isLogin) {
			reInit();
			LogUtil.info("FTP未登录，重新初始化。。。");
		}
		if (this.isLogin) {
			RandomAccessFile inputRAF = null;
			OutputStream out = null;
			try {
				if (sremoteDir != null && sremoteDir.length() > 0) {
					this.client.changeWorkingDirectory(sremoteDir);
				}
				// 设置被动模式
				this.client.enterLocalPassiveMode();
				inputRAF = new RandomAccessFile(lFile, "r");
				out = this.client.storeFileStream(srName);
				byte[] bytes = new byte[1024];
				int c;
				while ((c = inputRAF.read(bytes)) != -1) {
					out.write(bytes, 0, c);
					out.flush();
				}
				out.flush();
			} catch (Exception e) {
				result = false;
				LogUtil.info(e + "");
			} finally {
				try {
					inputRAF.close();
				} catch (IOException ex) {
					LogUtil.info("" + ex);
				}
				try {
					out.close();
					result = this.client.completePendingCommand();
					LogUtil.info("文件[" + lFile.getPath() + "]上传结果：" + result);
				} catch (IOException ex) {
					LogUtil.info("" + ex);
				}
			}
		}
		return result;
	}

	/**
	 * 上传
	 *
	 * @param remoteFile 相对当前ftp路径的文件路径及文件全名
	 * @param localFile  本地文件全路径
	 * @return 上传结果，true：成功，false：失败
	 */
	public boolean upload(String remoteFile, String localFile) {
		String sremoteFile = this.ConvertEncoding(remoteFile);
		String slocalFile = this.ConvertEncoding(localFile);
		try {
			InputStream in = new FileInputStream(slocalFile);
			boolean result = upload(sremoteFile, in);
			in.close();
			return result;
		} catch (FileNotFoundException e) {
			LogUtil.error("本地文件未找到，上传失败", e);
		} catch (IOException e) {
			LogUtil.error("关闭本地文件流出错", e);
		}
		return false;
	}

	/**
	 * 上传
	 *
	 * @param remoteFile 相对当前ftp路径的文件路径及文件全名
	 * @param in         文件来源的本地流
	 * @return 上传结果，true：成功，false：失败
	 */
	public boolean upload(String remoteFile, InputStream in) {
		String sremoteFile = this.ConvertEncoding(remoteFile);
		LogUtil.info("开始上传文件：" + sremoteFile);
		sremoteFile = sremoteFile.replaceAll("\\\\", "/");
		if (sremoteFile.startsWith("/") && ftpHome != null) {
			sremoteFile = ftpHome + sremoteFile;
		}
		OutputStream out = null;
		try {
			this.client.enterLocalPassiveMode();
			String[] pathInfo = sremoteFile.split("/");
			if (pathInfo.length > 0) {
				for (int i = 0; i < pathInfo.length - 1; i++) {
					String path = pathInfo[i];
					if (path.trim().equals("")) {
						continue;
					}
					this.client.makeDirectory(path);
					boolean changeRes = this.client.changeWorkingDirectory(path);
					if (!changeRes) {
						LogUtil.info("不能打开目录：" + this.client.printWorkingDirectory() + "/" + path);
						LogUtil.error("上传文件失败：" + sremoteFile);
						return false;
					}
				}
				// 设置被动模式
				this.client.enterLocalPassiveMode();
				out = client.storeFileStream(pathInfo[pathInfo.length - 1]);
				byte[] cache = new byte[4096];
				int read;
				while ((read = in.read(cache)) != -1) {
					out.write(cache, 0, read);
				}
				out.flush();
				out.close();
				boolean res = this.client.completePendingCommand();
				if (res) {
					LogUtil.info("上传文件成功：" + sremoteFile);
				} else {
					LogUtil.info("上传文件失败：" + sremoteFile);
				}
				return res;
			}
		} catch (IOException e) {
			LogUtil.error("上传文件失败：" + sremoteFile);
			LogUtil.error("上传文件错误", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public InputStream getInputStream(String remoteFile) {
		String sremoteFile = this.ConvertEncoding(remoteFile);
		this.client.enterLocalPassiveMode();
		InputStream in = null;
		sremoteFile = sremoteFile.replaceAll("\\\\", "/");
		if (sremoteFile.startsWith("/") && ftpHome != null) {
			sremoteFile = ftpHome + sremoteFile;
		}
		try {
			in = this.client.retrieveFileStream(sremoteFile);
		} catch (IOException e) {
			LogUtil.error("ftp下载失败！", e);
			return null;
		}
		return in;
	}

	public InputStream getBinaryInputStream(String remoteFile) {
		String sremoteFile = this.ConvertEncoding(remoteFile);

		this.client.enterLocalPassiveMode();
		InputStream in = null;
		sremoteFile = sremoteFile.replaceAll("\\\\", "/");
		if (sremoteFile.startsWith("/") && ftpHome != null) {
			sremoteFile = ftpHome + sremoteFile;
		}
		try {
			this.client.setFileType(FTPClient.BINARY_FILE_TYPE);
			in = this.client.retrieveFileStream(sremoteFile);
		} catch (IOException e) {
			LogUtil.error("getBinaryInputStream:ftp下载失败！", e);
			return null;
		}
		return in;
	}

	public boolean download(String remoteFile, OutputStream out) {
		String sremoteFile = this.ConvertEncoding(remoteFile);
		this.client.enterLocalPassiveMode();
		sremoteFile = sremoteFile.replaceAll("\\\\", "/");
		if (sremoteFile.startsWith("/") && ftpHome != null) {
			sremoteFile = ftpHome + sremoteFile;
		}
		LogUtil.info("ftp下载文件：" + sremoteFile);
		try {
			return this.client.retrieveFile(sremoteFile, out);
		} catch (IOException e) {
			LogUtil.error("ftp下载失败！", e);
			return false;
		}
	}

	public boolean downloadForKDH(String remoteFile, OutputStream out) {
		String sremoteFile = this.ConvertEncoding(remoteFile);
		this.client.enterLocalPassiveMode();
		sremoteFile = sremoteFile.replaceAll("\\\\", "/");
		LogUtil.info("ftp下载文件：" + sremoteFile);
		try {
			return this.client.retrieveFile(sremoteFile, out);
		} catch (IOException e) {
			LogUtil.error("ftp下载失败！", e);
			return false;
		}
	}

	public boolean binaryDownload(String remoteFile, OutputStream out) {
		String sremoteFile = this.ConvertEncoding(remoteFile);
		this.client.enterLocalPassiveMode();
		sremoteFile = sremoteFile.replaceAll("\\\\", "/");
		if (sremoteFile.startsWith("/") && ftpHome != null) {
			sremoteFile = ftpHome + sremoteFile;
		}
		LogUtil.info("ftp下载文件：" + sremoteFile);
		try {
			this.client.setFileType(FTPClient.BINARY_FILE_TYPE);
			return this.client.retrieveFile(sremoteFile, out);
		} catch (IOException e) {
			LogUtil.error("ftp下载失败！", e);
			return false;
		}
	}

	/**
	 * 重命名Ftp文件名称
	 *
	 * @param srcFileName  待修改的文件名称
	 * @param targFileName 要修改成的文件名
	 * @return
	 */
	public boolean renameFile(String srcFileName, String targFileName) {
		String ssrcFileName = this.ConvertEncoding(srcFileName);
		String stargFileName = this.ConvertEncoding(targFileName);
		LogUtil.info("将修改文件名" + ssrcFileName + "为" + stargFileName);
		boolean bRet = false;
		try {

			bRet = this.client.rename(ssrcFileName, stargFileName);
		} catch (Exception e) {
			LogUtil.error("重命名文件名异常", e);
		}

		return bRet;
	}

	/**
	 * 获取文件集合
	 */
	public FTPFile[] listFiles(String path) {
		String spath = this.ConvertEncoding(path);
		FTPFile[] files = null;
		try {
			files = client.listFiles(spath);
		} catch (IOException e) {
			LogUtil.error("获取文件列表异常！", e);
		}
		return files;
	}


	public boolean remove(String filePath) {
		try {
			return client.remoteStore(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void reInit() {
		destroy();
		init();
	}

	private String ConvertEncoding(String str) {
		if (str == null || "".equals(str.trim()) || controlEncoding == null || "".equals(controlEncoding.trim())) {
			return str;
		}
		try {
			return new String(str.getBytes("UTF-8"), controlEncoding);
		} catch (UnsupportedEncodingException e) {
			LogUtil.error("编码转换错误", e);
			return str;
		}
	}

}
