package com.taotao.cloud.health.base;

import com.taotao.cloud.common.utils.PropertyUtil;
import com.yh.csx.bsf.core.http.DefaultHttpClient;
import java.net.SocketTimeoutException;
import lombok.Data;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;


/**
 * @author: huojuncheng
 * @version: 2020-08-25 15:10
 **/
@Data
public class FlyBookProvider {

	private String getUrl() {
		String domain = PropertyUtil.getPropertyCache(FlyBookProperties.flyBookUrl,
			FlyBookProperties.Domain);
		return domain + "{access_token}";
	}


	public void send(String[] tokens, FlyBookBody content) {
		HttpClient.Params params = HttpClient.Params.custom()
			.setContentType(ContentType.APPLICATION_JSON).add(content.text).build();
		sendToken(tokens, params);
	}

	public void sendText(String[] tokens, String subject, String text) {
		FlyBookBody.Text text1 = new FlyBookBody.Text();
		text1.setText(text);
		FlyBookBody flyBookBody = new FlyBookBody();
		flyBookBody.setText(text1);
		flyBookBody.setMsgtype("text");
		send(tokens, flyBookBody);
	}

	private void sendToken(String[] tokens, HttpClient.Params params) {
		if (tokens != null) {
			for (String token : tokens) {
				try {
					DefaultHttpClient.Default.post(getUrl().replace("{access_token}", token),
						params);
				} catch (Exception e) {
					if (e.getCause() instanceof SocketTimeoutException) {
						//网关不一定稳定
						return;
					}
					throw e;
				}
			}
		}
	}

	public static class FlyBookBody {

		public static class MarkDown {

			private String title;
			private String text;

			public MarkDown() {
			}

			public MarkDown(String title, String text) {
				this.title = title;
				this.text = text;
			}

			public String getTitle() {
				return title;
			}

			public void setTitle(String title) {
				this.title = title;
			}

			public String getText() {
				return text;
			}

			public void setText(String text) {
				this.text = text;
			}
		}

		public static class Text {

			private String text;

			public Text() {
			}

			public Text(String text) {
				this.text = text;
			}

			public String getText() {
				return text;
			}

			public void setText(String text) {
				this.text = text;
			}
		}

		/**
		 * "markdown","text"
		 */
		private String msgtype = "markdown";
		private MarkDown markdown;
		private Text text;


		public FlyBookBody() {
		}

		public FlyBookBody(String msgtype,
			MarkDown markdown, Text text) {
			this.msgtype = msgtype;
			this.markdown = markdown;
			this.text = text;
		}

		public String getMsgtype() {
			return msgtype;
		}

		public void setMsgtype(String msgtype) {
			this.msgtype = msgtype;
		}

		public MarkDown getMarkdown() {
			return markdown;
		}

		public void setMarkdown(MarkDown markdown) {
			this.markdown = markdown;
		}

		public Text getText() {
			return text;
		}

		public void setText(Text text) {
			this.text = text;
		}
	}
}
