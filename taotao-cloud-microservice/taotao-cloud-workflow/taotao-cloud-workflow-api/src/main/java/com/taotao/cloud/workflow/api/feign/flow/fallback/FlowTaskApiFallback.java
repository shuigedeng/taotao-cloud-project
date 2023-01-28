package com.taotao.cloud.workflow.api.feign.flow.fallback;

import com.taotao.cloud.workflow.api.feign.flow.FlowTaskApi;
import java.util.ArrayList;
import java.util.List;

/**
 * api接口
 */
//@Component
public class FlowTaskApiFallback implements FlowTaskApi {
    @Override
    public List<FlowTaskEntity> getWaitList() {
        return new ArrayList<>();
    }

    @Override
    public List<FlowTaskListModel> getTrialList() {
        return new ArrayList<>();
    }

    @Override
    public List<FlowTaskListModel> getAllWaitList() {
        return new ArrayList<>();
    }

    @Override
    public List<FlowTaskEntity> getOrderStaList(String idAll) {
        return new ArrayList<>();
    }

    @Override
    public void submit(FlowSumbitModel flowSumbitModel) throws WorkFlowException {
        throw new WorkFlowException("创建失败");
    }

    @Override
    public void revoke(FlowRevokeModel flowRevokeModel) {

    }

    @Override
    public List<FlowTaskNodeEntity> getList(String id) {
        return new ArrayList<>();
    }

    @Override
    public FlowEngineEntity getInfo(String id) throws WorkFlowException {
        return null;
    }

    @Override
    public FlowEngineEntity getEngineInfo(String id) {
        return null;
    }

    @Override
    public void update(String id, FlowEngineEntity engineEntity) throws WorkFlowException {

    }

    @Override
    public void delete(FlowEngineEntity engineEntity) {

    }

    @Override
    public FlowTaskEntity getInfoSubmit(String id) {
        return null;
    }

    @Override
    public void deleteFlowTask(FlowTaskEntity taskEntity) throws WorkFlowException {
        throw new WorkFlowException("删除失败");
    }

    @Override
    public FlowEngineEntity getInfoByEnCode(String encode) throws WorkFlowException {
        return null;
    }

    @Override
    public List<FlowEngineEntity> getFlowFormList() {
        return new ArrayList<>();
    }

    @Override
    public List<FlowDelegateEntity> getDelegateList() {
        return new ArrayList<>();
    }

}
