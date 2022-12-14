package com.taotao.cloud.workflow.biz.engine.controller;

import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.workflow.biz.engine.entity.FlowDelegateEntity;
import com.taotao.cloud.workflow.biz.engine.model.flowdelegate.FlowDelegatListVO;
import com.taotao.cloud.workflow.biz.engine.model.flowdelegate.FlowDelegateCrForm;
import com.taotao.cloud.workflow.biz.engine.model.flowdelegate.FlowDelegateInfoVO;
import com.taotao.cloud.workflow.biz.engine.model.flowdelegate.FlowDelegateUpForm;
import com.taotao.cloud.workflow.biz.engine.service.FlowDelegateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程委托
 */
@Tag(tags = "流程委托", value = "FlowDelegate")
@RestController
@RequestMapping("/api/workflow/Engine/FlowDelegate")
public class FlowDelegateController {

	@Autowired
	private FlowDelegateService flowDelegateService;
	@Autowired
	private UserProvider userProvider;

	/**
	 * 获取流程委托列表
	 *
	 * @param pagination
	 * @return
	 */
	@Operation("获取流程委托列表")
	@GetMapping
	public Result<PageListVO<FlowDelegatListVO>> list(Pagination pagination) {
		List<FlowDelegateEntity> list = flowDelegateService.getList(pagination);
		PaginationVO paginationVO = JsonUtils.getJsonToBean(pagination, PaginationVO.class);
		List<FlowDelegatListVO> listVO = JsonUtils.getJsonToList(list, FlowDelegatListVO.class);
		return Result.page(listVO, paginationVO);
	}

	/**
	 * 获取流程委托信息
	 *
	 * @param id 主键值
	 * @return
	 */
	@Operation("获取流程委托信息")
	@GetMapping("/{id}")
	public Result<FlowDelegateInfoVO> info(@PathVariable("id") String id) throws DataException {
		FlowDelegateEntity entity = flowDelegateService.getInfo(id);
		FlowDelegateInfoVO vo = JsonUtilEx.getJsonToBeanEx(entity, FlowDelegateInfoVO.class);
		return Result.success(vo);
	}

	/**
	 * 新建流程委托
	 *
	 * @param flowDelegateCrForm 实体对象
	 * @return
	 */
	@Operation("新建流程委托")
	@PostMapping
	public Result create(@RequestBody @Valid FlowDelegateCrForm flowDelegateCrForm) {
		FlowDelegateEntity entity = JsonUtils.getJsonToBean(flowDelegateCrForm,
				FlowDelegateEntity.class);
		UserInfo userInfo = userProvider.get();
		if (userInfo.getUserId().equals(entity.getFTouserid())) {
			return Result.fail("委托人为自己，委托失败");
		}
		flowDelegateService.create(entity);
		return Result.success(MsgCode.SU001.get());
	}

	/**
	 * 更新流程委托
	 *
	 * @param id 主键值
	 * @return
	 */
	@Operation("更新流程委托")
	@PutMapping("/{id}")
	public Result update(@PathVariable("id") String id,
			@RequestBody @Valid FlowDelegateUpForm flowDelegateUpForm) {
		FlowDelegateEntity entity = JsonUtils.getJsonToBean(flowDelegateUpForm,
				FlowDelegateEntity.class);
		UserInfo userInfo = userProvider.get();
		if (userInfo.getUserId().equals(entity.getFTouserid())) {
			return Result.fail("委托人为自己，委托失败");
		}
		boolean flag = flowDelegateService.update(id, entity);
		if (flag == false) {
			return Result.success(MsgCode.FA002.get());
		}
		return Result.success(MsgCode.SU004.get());
	}

	/**
	 * 删除流程委托
	 *
	 * @param id 主键值
	 * @return
	 */
	@Operation("删除流程委托")
	@DeleteMapping("/{id}")
	public Result delete(@PathVariable("id") String id) {
		FlowDelegateEntity entity = flowDelegateService.getInfo(id);
		if (entity != null) {
			flowDelegateService.delete(entity);
			return Result.success(MsgCode.SU003.get());
		}
		return Result.fail(MsgCode.FA003.get());
	}
}
