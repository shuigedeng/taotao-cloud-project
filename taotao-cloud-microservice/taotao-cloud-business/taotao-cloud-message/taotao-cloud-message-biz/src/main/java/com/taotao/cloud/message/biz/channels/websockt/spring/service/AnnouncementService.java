package com.taotao.cloud.message.biz.channels.websockt.spring.service;

import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.data.mybatis.pagehelper.PageParam;
import com.taotao.cloud.message.biz.ballcat.notify.model.dto.AnnouncementDTO;
import com.taotao.cloud.message.biz.ballcat.notify.model.entity.Announcement;
import com.taotao.cloud.message.biz.ballcat.notify.model.qo.AnnouncementQO;
import com.taotao.cloud.message.biz.ballcat.notify.model.vo.AnnouncementPageVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 公告信息
 *
 * @author hccake 2020-12-15 17:01:15
 */
public interface AnnouncementService extends ExtendService<Announcement> {

	/**
	 * 根据QueryObject查询分页数据
	 * @param page 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<AnnouncementVO> 分页数据
	 */
	PageResult<AnnouncementPageVO> queryPage(PageParam page, AnnouncementQO qo);

	/**
	 * 创建公告
	 * @param announcementDTO 公告信息
	 * @return boolean
	 */
	boolean addAnnouncement(AnnouncementDTO announcementDTO);

	/**
	 * 更新公告信息
	 * @param announcementDTO announcementDTO
	 * @return boolean
	 */
	boolean updateAnnouncement(AnnouncementDTO announcementDTO);

	/**
	 * 发布公告信息
	 * @param announcementId 公告ID
	 * @return boolean
	 */
	boolean publish(Long announcementId);

	/**
	 * 关闭公告信息
	 * @param announcementId 公告ID
	 * @return boolean
	 */
	boolean close(Long announcementId);

	/**
	 * 批量上传公告图片
	 * @param files 图片文件
	 * @return 上传后的图片相对路径集合
	 */
	List<String> uploadImages(List<MultipartFile> files);

	/**
	 * 当前用户未拉取过的发布中，且满足失效时间的公告信息
	 * @param userId 用户id
	 * @return List<Announcement>
	 */
	List<Announcement> listUnPulled(Integer userId);

	/**
	 * 获取用户拉取过的发布中，且满足失效时间的公告信息
	 * @param userId 用户id
	 * @return List<Announcement>
	 */
	List<Announcement> listActiveAnnouncements(Integer userId);

}
