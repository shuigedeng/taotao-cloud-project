/*
 * Copyright (c) ©2015-2021 Jaemon. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.dingtalk.entity;


import com.taotao.cloud.dingtalk.enums.DingTalkMsgType;
import java.io.Serializable;
import java.util.List;

/**
 * 独立跳转ActionCard类型
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 15:19:11
 */
public class DingActionCard extends DingTalkMessage {

	/**
	 * 行动卡
	 * {@link ActionCard}
	 */
	private ActionCard actionCard;

	/**
	 * 丁行动卡
	 *
	 * @return
	 * @since 2022-07-06 15:19:11
	 */
	public DingActionCard() {
		setMsgtype(DingTalkMsgType.ACTION_CARD.type());
	}

	/**
	 * 让行动卡
	 *
	 * @return {@link ActionCard }
	 * @since 2022-07-06 15:19:11
	 */
	public ActionCard getActionCard() {
		return actionCard;
	}

	/**
	 * 设置操作卡
	 *
	 * @param actionCard 行动卡
	 * @since 2022-07-06 15:19:11
	 */
	public void setActionCard(ActionCard actionCard) {
		this.actionCard = actionCard;
	}

	/**
	 * 行动卡
	 *
	 * @author shuigedeng
	 * @version 2022.07
	 * @since 2022-07-06 15:19:11
	 */
	public static class ActionCard implements Serializable {

		/**
		 * 标题
		 * 首屏会话透出的展示内容
		 */
		private String title;
		/**
		 * 文本
		 * markdown格式的消息
		 */
		private String text;
		/**
		 * btn取向
		 * 0-按钮竖直排列，1-按钮横向排列
		 */
		private String btnOrientation;
		/**
		 * btn
		 * 按钮
		 */
		private List<Button> btns;

		/**
		 * 获得冠军
		 *
		 * @return {@link String }
		 * @since 2022-07-06 15:19:11
		 */
		public String getTitle() {
			return title;
		}

		/**
		 * 设置标题
		 *
		 * @param title 标题
		 * @since 2022-07-06 15:19:11
		 */
		public void setTitle(String title) {
			this.title = title;
		}

		/**
		 * 得到文本
		 *
		 * @return {@link String }
		 * @since 2022-07-06 15:19:11
		 */
		public String getText() {
			return text;
		}

		/**
		 * 设置文本
		 *
		 * @param text 文本
		 * @since 2022-07-06 15:19:11
		 */
		public void setText(String text) {
			this.text = text;
		}

		/**
		 * 得到btn取向
		 *
		 * @return {@link String }
		 * @since 2022-07-06 15:19:12
		 */
		public String getBtnOrientation() {
			return btnOrientation;
		}

		/**
		 * 设置btn取向
		 *
		 * @param btnOrientation btn取向
		 * @since 2022-07-06 15:19:12
		 */
		public void setBtnOrientation(String btnOrientation) {
			this.btnOrientation = btnOrientation;
		}

		/**
		 * 得到btn
		 *
		 * @return {@link List }<{@link Button }>
		 * @since 2022-07-06 15:19:12
		 */
		public List<Button> getBtns() {
			return btns;
		}

		/**
		 * 设置btn
		 *
		 * @param btns btn
		 * @since 2022-07-06 15:19:12
		 */
		public void setBtns(List<Button> btns) {
			this.btns = btns;
		}

		/**
		 * 按钮
		 *
		 * @author shuigedeng
		 * @version 2022.07
		 * @since 2022-07-06 15:19:12
		 */
		public static class Button implements Serializable {

			/**
			 * 标题
			 * 按钮标题
			 */
			private String title;
			/**
			 * 动作url
			 * 点击按钮触发的URL
			 */
			private String actionURL;

			/**
			 * 获得冠军
			 *
			 * @return {@link String }
			 * @since 2022-07-06 15:19:12
			 */
			public String getTitle() {
				return title;
			}

			/**
			 * 设置标题
			 *
			 * @param title 标题
			 * @since 2022-07-06 15:19:12
			 */
			public void setTitle(String title) {
				this.title = title;
			}

			/**
			 * 让行动url
			 *
			 * @return {@link String }
			 * @since 2022-07-06 15:19:12
			 */
			public String getActionURL() {
				return actionURL;
			}

			/**
			 * 设置动作url
			 *
			 * @param actionURL 动作url
			 * @since 2022-07-06 15:19:13
			 */
			public void setActionURL(String actionURL) {
				this.actionURL = actionURL;
			}
		}
	}

}
