package com.taotao.cloud.health.base.dingding;

import com.taotao.cloud.health.base.DefaultHttpClient;
import com.taotao.cloud.health.base.HttpClient;
import java.net.SocketTimeoutException;
import org.apache.http.entity.ContentType;
import org.springframework.util.StringUtils;


/**
 * @author: chejiangyi
 * @version: 2019-07-23 13:49
 **/
public class DingdingProvider {

	private String getUrl() {
		return StringUtils.trimTrailingCharacter(DingdingProperties.Domain, '/')
			+ "/robot/send?access_token={access_token}";
	}


	public void send(String[] tokens, DingdingBody content) {
		HttpClient.Params params = HttpClient.Params.custom()
			.setContentType(ContentType.APPLICATION_JSON).add(content).build();
		sendToken(tokens, params);
	}

	public void sendMarkDown(String[] tokens, String subject, String text) {
		DingdingBody.MarkDown markDown = new DingdingBody.MarkDown();
		markDown.setText(text);
		markDown.setTitle(subject);
		DingdingBody dingdingBody = new DingdingBody();
		dingdingBody.setMarkdown(markDown);
		dingdingBody.setMsgtype("markdown");
		send(tokens, dingdingBody);
	}

	public void sendText(String[] tokens, String subject, String text) {
		DingdingBody.Text text1 = new DingdingBody.Text();
		text1.setContent(text);
		DingdingBody dingdingBody = new DingdingBody();
		dingdingBody.setText(text1);
		dingdingBody.setMsgtype("text");
		send(tokens, dingdingBody);
	}

	private void sendToken(String[] tokens, HttpClient.Params params) {
		if (tokens != null) {
			for (String token : tokens) {
				try {
					DefaultHttpClient.Default.post(getUrl().replace("{access_token}", token),
						params);
				} catch (Exception e) {
					if (e.getCause() instanceof SocketTimeoutException) {
						//钉钉网关不一定稳定
						return;
					}
					throw e;
				}
			}
		}
	}

	public DingdingProvider() {
	}

	public static class DingdingBody {

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

			private String content;

			public Text() {
			}

			public Text(String content) {
				this.content = content;
			}

			public String getContent() {
				return content;
			}

			public void setContent(String content) {
				this.content = content;
			}
		}

		/**
		 * "markdown","text"
		 */
		private String msgtype = "markdown";
		private MarkDown markdown;
		private Text text;

		public DingdingBody() {
		}

		public DingdingBody(String msgtype,
			MarkDown markdown,
			Text text) {
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

		public void setMarkdown(
			MarkDown markdown) {
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
