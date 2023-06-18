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

package com.taotao.cloud.auth.biz.authentication.login.social.weibo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.taotao.cloud.auth.biz.authentication.utils.AuthorityUtils;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.*;

@Data
public class WeiboOAuth2User implements OAuth2User {
	// 统一赋予USER角色
	private Set<GrantedAuthority> authorities =  AuthorityUtils.createAuthorityList("ROLE_USER");
	private Map<String, Object> attributes = new HashMap<>();
	private String nameAttributeKey;

	/**
	 * 用户UID
	 */
	@JsonProperty("id")
	private long id;

	/**
	 * 字符串型的用户UID
	 */
	@JsonProperty("idstr")
	private String idstr;

	/**
	 * 用户昵称
	 */
	@JsonProperty("screen_name")
	private String screenName;

	/**
	 * 友好显示名称
	 */
	@JsonProperty("name")
	private String name;

	/**
	 * 用户所在省级ID
	 */
	@JsonProperty("province")
	private String province;

	/**
	 * 用户所在城市ID
	 */
	@JsonProperty("city")
	private String city;

	/**
	 * 用户所在地
	 */
	@JsonProperty("location")
	private String location;

	/**
	 * 用户个人描述
	 */
	@JsonProperty("description")
	private String description;

	/**
	 * 用户博客地址
	 */
	@JsonProperty("url")
	private String url;

	/**
	 * 用户头像地址（中图），50×50像素
	 */
	@JsonProperty("profile_image_url")
	private String profileImageUrl;

	/**
	 * 用户的微博统一URL地址
	 */
	@JsonProperty("profile_url")
	private String profileUrl;

	/**
	 * 用户的个性化域名
	 */
	@JsonProperty("domain")
	private String domain;

	/**
	 * 用户的微号
	 */
	@JsonProperty("weihao")
	private String weihao;

	/**
	 * 性别，m：男、f：女、n：未知
	 */
	@JsonProperty("gender")
	private String gender;

	/**
	 * 粉丝数
	 */
	@JsonProperty("followers_count")
	private int followersCount;

	/**
	 * 关注数
	 */
	@JsonProperty("friends_count")
	private int friendsCount;

	/**
	 * 微博数
	 */
	@JsonProperty("statuses_count")
	private int statusesCount;

	/**
	 * 收藏数
	 */
	@JsonProperty("favourites_count")
	private int favouritesCount;

	/**
	 * 用户创建（注册）时间
	 */
	@JsonProperty("created_at")
	@JsonFormat(pattern = "E MMM dd HH:mm:ss '+0800' yyyy", locale = "en")
	private LocalDateTime createdAt;

	/**
	 * 暂未支持
	 */
	@JsonProperty("following")
	private boolean following;

	/**
	 * 是否允许所有人给我发私信，true：是，false：否
	 */
	@JsonProperty("allow_all_act_msg")
	private boolean allowAllActMsg;

	/**
	 * 是否允许标识用户的地理位置，true：是，false：否
	 */
	@JsonProperty("geo_enabled")
	private boolean geoEnabled;

	/**
	 * 是否是微博认证用户，即加V用户，true：是，false：否
	 */
	@JsonProperty("verified")
	private boolean verified;

	/**
	 * 暂未支持
	 */
	@JsonProperty("verified_type")
	private int verifiedType;

	/**
	 * 用户备注信息，只有在查询用户关系时才返回此字段
	 */
	@JsonProperty("remark")
	private String remark;

	/**
	 * 用户的最近一条微博信息字段
	 */
	@JsonProperty("status")
	private Status status;

	/**
	 * 是否允许所有人对我的微博进行评论，true：是，false：否
	 */
	@JsonProperty("allow_all_comment")
	private boolean allowAllComment;

	/**
	 * 用户头像地址（大图），180×180像素
	 */
	@JsonProperty("avatar_large")
	private String avatarLarge;

	/**
	 * 用户头像地址（高清），高清头像原图
	 */
	@JsonProperty("avatar_hd")
	private String avatarHd;

	/**
	 * 认证原因
	 */
	@JsonProperty("verified_reason")
	private String verifiedReason;

	/**
	 * 该用户是否关注当前登录用户，true：是，false：否
	 */
	@JsonProperty("follow_me")
	private boolean followMe;

	/**
	 * 用户的在线状态，0：不在线、1：在线
	 */
	@JsonProperty("online_status")
	private int onlineStatus;

	/**
	 * 用户的互粉数
	 */
	@JsonProperty("bi_followers_count")
	private int biFollowersCount;

	/**
	 * 用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语
	 */
	@JsonProperty("lang")
	private String lang;

	@JsonProperty("is_teenager")
	private int isTeenager;

	@JsonProperty("vplus_ability")
	private int vplusAbility;

	@JsonProperty("like_me")
	private boolean likeMe;

	@JsonProperty("verified_contact_mobile")
	private String verifiedContactMobile;

	@JsonProperty("light_ring")
	private boolean lightRing;

	@JsonProperty("wenda_ability")
	private int wendaAbility;

	@JsonProperty("video_total_counter")
	private VideoTotalCounter videoTotalCounter;

	@JsonProperty("nft_ability")
	private int nftAbility;

	@JsonProperty("ecommerce_ability")
	private int ecommerceAbility;

	@JsonProperty("verified_contact_email")
	private String verifiedContactEmail;

	@JsonProperty("pay_date")
	private String payDate;

	@JsonProperty("credit_score")
	private int creditScore;

	@JsonProperty("user_ability_extend")
	private int userAbilityExtend;

	@JsonProperty("pay_remind")
	private int payRemind;

	@JsonProperty("brand_ability")
	private int brandAbility;

	@JsonProperty("super_topic_not_syn_count")
	private int superTopicNotSynCount;

	@JsonProperty("live_ability")
	private int liveAbility;

	@JsonProperty("cardid")
	private String cardid;

	@JsonProperty("is_teenager_list")
	private int isTeenagerList;

	@JsonProperty("video_status_count")
	private int videoStatusCount;

	@JsonProperty("newbrand_ability")
	private int newbrandAbility;

	@JsonProperty("verified_level")
	private int verifiedLevel;

	@JsonProperty("urisk")
	private int urisk;

	@JsonProperty("star")
	private int star;

	@JsonProperty("status_total_counter")
	private StatusTotalCounter statusTotalCounter;

	@JsonProperty("has_service_tel")
	private boolean hasServiceTel;

	@JsonProperty("block_app")
	private int blockApp;

	@JsonProperty("planet_video")
	private int planetVideo;

	@JsonProperty("gongyi_ability")
	private int gongyiAbility;

	@JsonProperty("hardfan_ability")
	private int hardfanAbility;

	@JsonProperty("insecurity")
	private Insecurity insecurity;

	@JsonProperty("verified_source")
	private String verifiedSource;

	@JsonProperty("urank")
	private int urank;

	@JsonProperty("verified_trade")
	private String verifiedTrade;

	@JsonProperty("green_mode")
	private int greenMode;

	@JsonProperty("mb_expire_time")
	private int mbExpireTime;

	@JsonProperty("verified_source_url")
	private String verifiedSourceUrl;

	@JsonProperty("video_mark")
	private int videoMark;

	@JsonProperty("live_status")
	private int liveStatus;

	@JsonProperty("special_follow")
	private boolean specialFollow;

	@JsonProperty("followers_count_str")
	private String followersCountStr;

	@JsonProperty("chaohua_ability")
	private int chaohuaAbility;

	@JsonProperty("like")
	private boolean like;

	@JsonProperty("verified_type_ext")
	private int verifiedTypeExt;

	@JsonProperty("pagefriends_count")
	private int pagefriendsCount;

	@JsonProperty("cover_image_phone")
	private String coverImagePhone;

	@JsonProperty("ptype")
	private int ptype;

	@JsonProperty("verified_reason_url")
	private String verifiedReasonUrl;

	@JsonProperty("block_word")
	private int blockWord;

	@JsonProperty("verified_state")
	private int verifiedState;

	@JsonProperty("avatar_type")
	private int avatarType;

	@JsonProperty("hongbaofei")
	private int hongbaofei;

	@JsonProperty("video_play_count")
	private int videoPlayCount;

	@JsonProperty("mbtype")
	private int mbtype;

	@JsonProperty("user_ability")
	private int userAbility;

	@JsonProperty("story_read_state")
	private int storyReadState;

	@JsonProperty("mbrank")
	private int mbrank;

	@JsonProperty("class")
	private int jsonMemberClass;

	@JsonProperty("pc_new")
	private int pcNew;

	@JsonProperty("paycolumn_ability")
	private int paycolumnAbility;

	@JsonProperty("brand_account")
	private int brandAccount;

	@JsonProperty("verified_contact_name")
	private String verifiedContactName;

	@JsonProperty("vclub_member")
	private int vclubMember;

	@JsonProperty("is_guardian")
	private int isGuardian;

	@JsonProperty("svip")
	private int svip;

	@JsonProperty("verified_reason_modified")
	private String verifiedReasonModified;

	private Integer block;

	@JsonProperty("block_me")
	private Integer blockMe;

    @Override
    public String getName() {
        return idstr;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }


	/**
	 * 微博（status）
	 *
	 * @since 0.0.1
	 * @see <a href=
	 * "https://open.weibo.com/wiki/%E5%B8%B8%E8%A7%81%E8%BF%94%E5%9B%9E%E5%AF%B9%E8%B1%A1%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84">常见返回对象数据结构</a>
	 */
	@Data
	public static class Status {

		/**
		 *
		 */
		private Integer version;

		/**
		 *
		 */
		private String picStatus;

		/**
		 *
		 */
		@JsonProperty("show_mlevel")
		private Integer showMlevel;

		/**
		 * 微博创建时间
		 */
		@JsonFormat(pattern = "E MMM dd HH:mm:ss '+0800' yyyy", locale = "en")
		@JsonProperty("created_at")
		private LocalDateTime createdAt;

		/**
		 * 微博ID
		 */
		@JsonProperty("id")
		private long id;

		/**
		 * 微博MID
		 */
		@JsonProperty("mid")
		private String mid;

		/**
		 * 字符串型的微博ID
		 */
		@JsonProperty("idstr")
		private String idstr;

		/**
		 * 微博信息内容
		 */
		@JsonProperty("text")
		private String text;

		/**
		 * 微博来源
		 */
		@JsonProperty("source")
		private String source;

		/**
		 * 是否已收藏，true：是，false：否
		 */
		@JsonProperty("favorited")
		private boolean favorited;

		/**
		 * 是否被截断，true：是，false：否
		 */
		@JsonProperty("truncated")
		private boolean truncated;

		/**
		 * （暂未支持）回复ID
		 */
		@JsonProperty("in_reply_to_status_id")
		private String inReplyToStatusId;

		/**
		 * （暂未支持）回复人UID
		 */
		@JsonProperty("in_reply_to_user_id")
		private String inReplyToUserId;

		/**
		 * （暂未支持）回复人昵称
		 */
		@JsonProperty("in_reply_to_screen_name")
		private String inReplyToScreenName;

		/**
		 * 缩略图片地址，没有时不返回此字段
		 */
		@JsonProperty("thumbnail_pic")
		private String thumbnailPic;

		/**
		 * 中等尺寸图片地址，没有时不返回此字段
		 */
		@JsonProperty("bmiddle_pic")
		private String bmiddlePic;

		/**
		 * 原始图片地址，没有时不返回此字段
		 */
		@JsonProperty("original_pic")
		private String originalPic;

		/**
		 * 地理信息字段
		 * @see <a href=
		 * "https://open.weibo.com/wiki/%E5%B8%B8%E8%A7%81%E8%BF%94%E5%9B%9E%E5%AF%B9%E8%B1%A1%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84#.E5.9C.B0.E7.90.86.E4.BF.A1.E6.81.AF.EF.BC.88geo.EF.BC.89">地理信息（geo）</a>
		 */
		@JsonProperty("geo")
		private Object geo;

		/**
		 * 转发数
		 */
		@JsonProperty("reposts_count")
		private int repostsCount;

		/**
		 * 评论数
		 */
		@JsonProperty("comments_count")
		private int commentsCount;

		/**
		 * 表态数
		 */
		@JsonProperty("attitudes_count")
		private int attitudesCount;

		/**
		 * 暂未支持
		 */
		@JsonProperty("mlevel")
		private int mlevel;

		/**
		 * 微博的可见性及指定可见分组信息。该object中type取值，0：普通微博，1：私密微博，3：指定分组微博，4：密友微博；list_id为分组的组号
		 */
		@JsonProperty("visible")
		private Visible visible;

		@JsonProperty("isLongText")
		private boolean isLongText;

		@JsonProperty("hot_weibo_tags")
		private List<Object> hotWeiboTags;

		@JsonProperty("annotations")
		private List<AnnotationsItem> annotations;

		@JsonProperty("mblogtype")
		private int mblogtype;

		@JsonProperty("rid")
		private String rid;

		@JsonProperty("positive_recom_flag")
		private int positiveRecomFlag;

		@JsonProperty("can_reprint")
		private boolean canReprint;

		@JsonProperty("is_show_bulletin")
		private int isShowBulletin;

		@JsonProperty("hide_flag")
		private int hideFlag;

		@JsonProperty("hasActionTypeCard")
		private int hasActionTypeCard;

		@JsonProperty("new_comment_style")
		private int newCommentStyle;

		@JsonProperty("mblog_vip_type")
		private int mblogVipType;

		@JsonProperty("content_auth")
		private int contentAuth;

		@JsonProperty("gif_ids")
		private String gifIds;

		@JsonProperty("source_type")
		private int sourceType;

		@JsonProperty("pic_urls")
		private List<PicUrlsItem> picUrls;

		@JsonProperty("biz_feature")
		private long bizFeature;

		@JsonProperty("userType")
		private int userType;

		@JsonProperty("text_tag_tips")
		private List<Object> textTagTips;

		@JsonProperty("darwin_tags")
		private List<Object> darwinTags;

		@JsonProperty("pending_approval_count")
		private int pendingApprovalCount;

		@JsonProperty("pic_num")
		private int picNum;

		@JsonProperty("is_paid")
		private boolean isPaid;

		@JsonProperty("reward_exhibition_type")
		private int rewardExhibitionType;

		@JsonProperty("reprint_cmt_count")
		private int reprintCmtCount;

		@JsonProperty("can_edit")
		private boolean canEdit;

		@JsonProperty("textLength")
		private int textLength;

		@JsonProperty("source_allowclick")
		private int sourceAllowclick;

		@JsonProperty("show_additional_indication")
		private int showAdditionalIndication;

		@JsonProperty("comment_manage_info")
		private CommentManageInfo commentManageInfo;

		@JsonProperty("more_info_type")
		private int moreInfoType;

	}

	@Data
	public static class StatusTotalCounter {

		@JsonProperty("total_cnt")
		private int totalCnt;

		@JsonProperty("repost_cnt")
		private int repostCnt;

		@JsonProperty("comment_like_cnt")
		private int commentLikeCnt;

		@JsonProperty("like_cnt")
		private int likeCnt;

		@JsonProperty("comment_cnt")
		private int commentCnt;

	}

	@Data
	public static class VideoTotalCounter {

		@JsonProperty("play_cnt")
		private int playCnt;

	}

	@Data
	public static class Visible {

		@JsonProperty("list_id")
		private int listId;

		@JsonProperty("type")
		private int type;

	}

	@Data
	public static class AnnotationsItem {

		@JsonProperty("mapi_request")
		private boolean mapiRequest;

		@JsonProperty("client_mblogid")
		private String clientMblogid;

		@JsonProperty("photo_sub_type")
		private String photoSubType;

	}

	@Data
	public static class CommentManageInfo {

		@JsonProperty("comment_permission_type")
		private int commentPermissionType;

		@JsonProperty("comment_sort_type")
		private int commentSortType;

		@JsonProperty("approval_comment_type")
		private int approvalCommentType;

	}

	@Data
	public static class PicUrlsItem {
		@JsonProperty("thumbnail_pic")
		private String thumbnailPic;
	}

	@Data
	public static class Insecurity {

		@JsonProperty("sexual_content")
		private boolean sexualContent;

	}
}
