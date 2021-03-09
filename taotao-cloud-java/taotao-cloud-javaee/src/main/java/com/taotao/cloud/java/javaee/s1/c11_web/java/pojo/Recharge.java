package com.taotao.cloud.java.javaee.s1.c11_web.java.pojo;
import java.io.Serializable;
import java.util.*;

/**
*
*  @author author
*/
public class Recharge implements Serializable {

    private static final long serialVersionUID = 1586363206787L;


    /**
    * 主键
    * 
    * isNullAble:0
    */
    private Integer id;

    /**
    * 客户 id
    * isNullAble:0
    */
    private Integer cusId;

    /**
    * 订单号
    * isNullAble:1
    */
    private Long orderId;

    /**
    * 创建时间
    * isNullAble:0,defaultVal:CURRENT_TIMESTAMP
    */
    private Date createtime;

    /**
    * 更新时间
    * isNullAble:1,defaultVal:CURRENT_TIMESTAMP
    */
    private Date updatetime;

    /**
    * 充值金额(分)
    * isNullAble:1
    */
    private Integer money;

    /**
    * 状态,0创建,1 支付,2 取消
    * isNullAble:0,defaultVal:0
    */
    private Integer state;

    /**
    * 支付方式 支付宝 微信等
    * isNullAble:1
    */
    private Integer paymenttype;


    public void setId(Integer id){this.id = id;}

    public Integer getId(){return this.id;}

    public void setCusId(Integer cusId){this.cusId = cusId;}

    public Integer getCusId(){return this.cusId;}

    public void setOrderId(Long orderId){this.orderId = orderId;}

    public Long getOrderId(){return this.orderId;}

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public void setMoney(Integer money){this.money = money;}

    public Integer getMoney(){return this.money;}

    public void setState(Integer state){this.state = state;}

    public Integer getState(){return this.state;}

    public void setPaymenttype(Integer paymenttype){this.paymenttype = paymenttype;}

    public Integer getPaymenttype(){return this.paymenttype;}
    @Override
    public String toString() {
        return "Recharge{" +
                "id='" + id + '\'' +
                "cusId='" + cusId + '\'' +
                "orderId='" + orderId + '\'' +
                "createtime='" + createtime + '\'' +
                "updatetime='" + updatetime + '\'' +
                "money='" + money + '\'' +
                "state='" + state + '\'' +
                "paymenttype='" + paymenttype + '\'' +
            '}';
    }

    public static Builder Build(){return new Builder();}

    public static ConditionBuilder ConditionBuild(){return new ConditionBuilder();}

    public static UpdateBuilder UpdateBuild(){return new UpdateBuilder();}

    public static QueryBuilder QueryBuild(){return new QueryBuilder();}

    public static class UpdateBuilder {

        private Recharge set;

        private ConditionBuilder where;

        public UpdateBuilder set(Recharge set){
            this.set = set;
            return this;
        }

        public Recharge getSet(){
            return this.set;
        }

        public UpdateBuilder where(ConditionBuilder where){
            this.where = where;
            return this;
        }

        public ConditionBuilder getWhere(){
            return this.where;
        }

        public UpdateBuilder build(){
            return this;
        }
    }

    public static class QueryBuilder extends Recharge{
        /**
        * 需要返回的列
        */
        private Map<String,Object> fetchFields;

        public Map<String,Object> getFetchFields(){return this.fetchFields;}

        private List<Integer> idList;

        public List<Integer> getIdList(){return this.idList;}

        private Integer idSt;

        private Integer idEd;

        public Integer getIdSt(){return this.idSt;}

        public Integer getIdEd(){return this.idEd;}

        private List<Integer> cusIdList;

        public List<Integer> getCusIdList(){return this.cusIdList;}

        private Integer cusIdSt;

        private Integer cusIdEd;

        public Integer getCusIdSt(){return this.cusIdSt;}

        public Integer getCusIdEd(){return this.cusIdEd;}

        private List<Long> orderIdList;

        public List<Long> getOrderIdList(){return this.orderIdList;}

        private Long orderIdSt;

        private Long orderIdEd;

        public Long getOrderIdSt(){return this.orderIdSt;}

        public Long getOrderIdEd(){return this.orderIdEd;}

        private List<java.time.LocalDateTime> createtimeList;

        public List<java.time.LocalDateTime> getCreatetimeList(){return this.createtimeList;}

        private java.time.LocalDateTime createtimeSt;

        private java.time.LocalDateTime createtimeEd;

        public java.time.LocalDateTime getCreatetimeSt(){return this.createtimeSt;}

        public java.time.LocalDateTime getCreatetimeEd(){return this.createtimeEd;}

        private List<java.time.LocalDateTime> updatetimeList;

        public List<java.time.LocalDateTime> getUpdatetimeList(){return this.updatetimeList;}

        private java.time.LocalDateTime updatetimeSt;

        private java.time.LocalDateTime updatetimeEd;

        public java.time.LocalDateTime getUpdatetimeSt(){return this.updatetimeSt;}

        public java.time.LocalDateTime getUpdatetimeEd(){return this.updatetimeEd;}

        private List<Integer> moneyList;

        public List<Integer> getMoneyList(){return this.moneyList;}

        private Integer moneySt;

        private Integer moneyEd;

        public Integer getMoneySt(){return this.moneySt;}

        public Integer getMoneyEd(){return this.moneyEd;}

        private List<Integer> stateList;

        public List<Integer> getStateList(){return this.stateList;}

        private Integer stateSt;

        private Integer stateEd;

        public Integer getStateSt(){return this.stateSt;}

        public Integer getStateEd(){return this.stateEd;}

        private List<Integer> paymenttypeList;

        public List<Integer> getPaymenttypeList(){return this.paymenttypeList;}

        private Integer paymenttypeSt;

        private Integer paymenttypeEd;

        public Integer getPaymenttypeSt(){return this.paymenttypeSt;}

        public Integer getPaymenttypeEd(){return this.paymenttypeEd;}

        private QueryBuilder (){
            this.fetchFields = new HashMap<>();
        }

        public QueryBuilder idBetWeen(Integer idSt,Integer idEd){
            this.idSt = idSt;
            this.idEd = idEd;
            return this;
        }

        public QueryBuilder idGreaterEqThan(Integer idSt){
            this.idSt = idSt;
            return this;
        }
        public QueryBuilder idLessEqThan(Integer idEd){
            this.idEd = idEd;
            return this;
        }


        public QueryBuilder id(Integer id){
            setId(id);
            return this;
        }

        public QueryBuilder idList(Integer ... id){
            this.idList = solveNullList(id);
            return this;
        }

        public QueryBuilder idList(List<Integer> id){
            this.idList = id;
            return this;
        }

        public QueryBuilder fetchId(){
            setFetchFields("fetchFields","id");
            return this;
        }

        public QueryBuilder excludeId(){
            setFetchFields("excludeFields","id");
            return this;
        }

        public QueryBuilder cusIdBetWeen(Integer cusIdSt,Integer cusIdEd){
            this.cusIdSt = cusIdSt;
            this.cusIdEd = cusIdEd;
            return this;
        }

        public QueryBuilder cusIdGreaterEqThan(Integer cusIdSt){
            this.cusIdSt = cusIdSt;
            return this;
        }
        public QueryBuilder cusIdLessEqThan(Integer cusIdEd){
            this.cusIdEd = cusIdEd;
            return this;
        }


        public QueryBuilder cusId(Integer cusId){
            setCusId(cusId);
            return this;
        }

        public QueryBuilder cusIdList(Integer ... cusId){
            this.cusIdList = solveNullList(cusId);
            return this;
        }

        public QueryBuilder cusIdList(List<Integer> cusId){
            this.cusIdList = cusId;
            return this;
        }

        public QueryBuilder fetchCusId(){
            setFetchFields("fetchFields","cusId");
            return this;
        }

        public QueryBuilder excludeCusId(){
            setFetchFields("excludeFields","cusId");
            return this;
        }

        public QueryBuilder orderIdBetWeen(Long orderIdSt,Long orderIdEd){
            this.orderIdSt = orderIdSt;
            this.orderIdEd = orderIdEd;
            return this;
        }

        public QueryBuilder orderIdGreaterEqThan(Long orderIdSt){
            this.orderIdSt = orderIdSt;
            return this;
        }
        public QueryBuilder orderIdLessEqThan(Long orderIdEd){
            this.orderIdEd = orderIdEd;
            return this;
        }


        public QueryBuilder orderId(Long orderId){
            setOrderId(orderId);
            return this;
        }

        public QueryBuilder orderIdList(Long ... orderId){
            this.orderIdList = solveNullList(orderId);
            return this;
        }

        public QueryBuilder orderIdList(List<Long> orderId){
            this.orderIdList = orderId;
            return this;
        }

        public QueryBuilder fetchOrderId(){
            setFetchFields("fetchFields","orderId");
            return this;
        }

        public QueryBuilder excludeOrderId(){
            setFetchFields("excludeFields","orderId");
            return this;
        }

        public QueryBuilder createtimeBetWeen(java.time.LocalDateTime createtimeSt,java.time.LocalDateTime createtimeEd){
            this.createtimeSt = createtimeSt;
            this.createtimeEd = createtimeEd;
            return this;
        }

        public QueryBuilder createtimeGreaterEqThan(java.time.LocalDateTime createtimeSt){
            this.createtimeSt = createtimeSt;
            return this;
        }
        public QueryBuilder createtimeLessEqThan(java.time.LocalDateTime createtimeEd){
            this.createtimeEd = createtimeEd;
            return this;
        }


        public QueryBuilder createtime(Date createtime){
            setCreatetime(createtime);
            return this;
        }

        public QueryBuilder createtimeList(java.time.LocalDateTime ... createtime){
            this.createtimeList = solveNullList(createtime);
            return this;
        }

        public QueryBuilder createtimeList(List<java.time.LocalDateTime> createtime){
            this.createtimeList = createtime;
            return this;
        }

        public QueryBuilder fetchCreatetime(){
            setFetchFields("fetchFields","createtime");
            return this;
        }

        public QueryBuilder excludeCreatetime(){
            setFetchFields("excludeFields","createtime");
            return this;
        }

        public QueryBuilder updatetimeBetWeen(java.time.LocalDateTime updatetimeSt,java.time.LocalDateTime updatetimeEd){
            this.updatetimeSt = updatetimeSt;
            this.updatetimeEd = updatetimeEd;
            return this;
        }

        public QueryBuilder updatetimeGreaterEqThan(java.time.LocalDateTime updatetimeSt){
            this.updatetimeSt = updatetimeSt;
            return this;
        }
        public QueryBuilder updatetimeLessEqThan(java.time.LocalDateTime updatetimeEd){
            this.updatetimeEd = updatetimeEd;
            return this;
        }


        public QueryBuilder updatetime(Date updatetime){
            setUpdatetime(updatetime);
            return this;
        }

        public QueryBuilder updatetimeList(java.time.LocalDateTime ... updatetime){
            this.updatetimeList = solveNullList(updatetime);
            return this;
        }

        public QueryBuilder updatetimeList(List<java.time.LocalDateTime> updatetime){
            this.updatetimeList = updatetime;
            return this;
        }

        public QueryBuilder fetchUpdatetime(){
            setFetchFields("fetchFields","updatetime");
            return this;
        }

        public QueryBuilder excludeUpdatetime(){
            setFetchFields("excludeFields","updatetime");
            return this;
        }

        public QueryBuilder moneyBetWeen(Integer moneySt,Integer moneyEd){
            this.moneySt = moneySt;
            this.moneyEd = moneyEd;
            return this;
        }

        public QueryBuilder moneyGreaterEqThan(Integer moneySt){
            this.moneySt = moneySt;
            return this;
        }
        public QueryBuilder moneyLessEqThan(Integer moneyEd){
            this.moneyEd = moneyEd;
            return this;
        }


        public QueryBuilder money(Integer money){
            setMoney(money);
            return this;
        }

        public QueryBuilder moneyList(Integer ... money){
            this.moneyList = solveNullList(money);
            return this;
        }

        public QueryBuilder moneyList(List<Integer> money){
            this.moneyList = money;
            return this;
        }

        public QueryBuilder fetchMoney(){
            setFetchFields("fetchFields","money");
            return this;
        }

        public QueryBuilder excludeMoney(){
            setFetchFields("excludeFields","money");
            return this;
        }

        public QueryBuilder stateBetWeen(Integer stateSt,Integer stateEd){
            this.stateSt = stateSt;
            this.stateEd = stateEd;
            return this;
        }

        public QueryBuilder stateGreaterEqThan(Integer stateSt){
            this.stateSt = stateSt;
            return this;
        }
        public QueryBuilder stateLessEqThan(Integer stateEd){
            this.stateEd = stateEd;
            return this;
        }


        public QueryBuilder state(Integer state){
            setState(state);
            return this;
        }

        public QueryBuilder stateList(Integer ... state){
            this.stateList = solveNullList(state);
            return this;
        }

        public QueryBuilder stateList(List<Integer> state){
            this.stateList = state;
            return this;
        }

        public QueryBuilder fetchState(){
            setFetchFields("fetchFields","state");
            return this;
        }

        public QueryBuilder excludeState(){
            setFetchFields("excludeFields","state");
            return this;
        }

        public QueryBuilder paymenttypeBetWeen(Integer paymenttypeSt,Integer paymenttypeEd){
            this.paymenttypeSt = paymenttypeSt;
            this.paymenttypeEd = paymenttypeEd;
            return this;
        }

        public QueryBuilder paymenttypeGreaterEqThan(Integer paymenttypeSt){
            this.paymenttypeSt = paymenttypeSt;
            return this;
        }
        public QueryBuilder paymenttypeLessEqThan(Integer paymenttypeEd){
            this.paymenttypeEd = paymenttypeEd;
            return this;
        }


        public QueryBuilder paymenttype(Integer paymenttype){
            setPaymenttype(paymenttype);
            return this;
        }

        public QueryBuilder paymenttypeList(Integer ... paymenttype){
            this.paymenttypeList = solveNullList(paymenttype);
            return this;
        }

        public QueryBuilder paymenttypeList(List<Integer> paymenttype){
            this.paymenttypeList = paymenttype;
            return this;
        }

        public QueryBuilder fetchPaymenttype(){
            setFetchFields("fetchFields","paymenttype");
            return this;
        }

        public QueryBuilder excludePaymenttype(){
            setFetchFields("excludeFields","paymenttype");
            return this;
        }
        private <T>List<T> solveNullList(T ... objs){
            if (objs != null){
            List<T> list = new ArrayList<>();
                for (T item : objs){
                    if (item != null){
                        list.add(item);
                    }
                }
                return list;
            }
            return null;
        }

        public QueryBuilder fetchAll(){
            this.fetchFields.put("AllFields",true);
            return this;
        }

        public QueryBuilder addField(String ... fields){
            List<String> list = new ArrayList<>();
            if (fields != null){
                for (String field : fields){
                    list.add(field);
                }
            }
            this.fetchFields.put("otherFields",list);
            return this;
        }
        @SuppressWarnings("unchecked")
        private void setFetchFields(String key,String val){
            Map<String,Boolean> fields= (Map<String, Boolean>) this.fetchFields.get(key);
            if (fields == null){
                fields = new HashMap<>();
            }
            fields.put(val,true);
            this.fetchFields.put(key,fields);
        }

        public Recharge build(){return this;}
    }


    public static class ConditionBuilder{
        private List<Integer> idList;

        public List<Integer> getIdList(){return this.idList;}

        private Integer idSt;

        private Integer idEd;

        public Integer getIdSt(){return this.idSt;}

        public Integer getIdEd(){return this.idEd;}

        private List<Integer> cusIdList;

        public List<Integer> getCusIdList(){return this.cusIdList;}

        private Integer cusIdSt;

        private Integer cusIdEd;

        public Integer getCusIdSt(){return this.cusIdSt;}

        public Integer getCusIdEd(){return this.cusIdEd;}

        private List<Long> orderIdList;

        public List<Long> getOrderIdList(){return this.orderIdList;}

        private Long orderIdSt;

        private Long orderIdEd;

        public Long getOrderIdSt(){return this.orderIdSt;}

        public Long getOrderIdEd(){return this.orderIdEd;}

        private List<java.time.LocalDateTime> createtimeList;

        public List<java.time.LocalDateTime> getCreatetimeList(){return this.createtimeList;}

        private java.time.LocalDateTime createtimeSt;

        private java.time.LocalDateTime createtimeEd;

        public java.time.LocalDateTime getCreatetimeSt(){return this.createtimeSt;}

        public java.time.LocalDateTime getCreatetimeEd(){return this.createtimeEd;}

        private List<java.time.LocalDateTime> updatetimeList;

        public List<java.time.LocalDateTime> getUpdatetimeList(){return this.updatetimeList;}

        private java.time.LocalDateTime updatetimeSt;

        private java.time.LocalDateTime updatetimeEd;

        public java.time.LocalDateTime getUpdatetimeSt(){return this.updatetimeSt;}

        public java.time.LocalDateTime getUpdatetimeEd(){return this.updatetimeEd;}

        private List<Integer> moneyList;

        public List<Integer> getMoneyList(){return this.moneyList;}

        private Integer moneySt;

        private Integer moneyEd;

        public Integer getMoneySt(){return this.moneySt;}

        public Integer getMoneyEd(){return this.moneyEd;}

        private List<Integer> stateList;

        public List<Integer> getStateList(){return this.stateList;}

        private Integer stateSt;

        private Integer stateEd;

        public Integer getStateSt(){return this.stateSt;}

        public Integer getStateEd(){return this.stateEd;}

        private List<Integer> paymenttypeList;

        public List<Integer> getPaymenttypeList(){return this.paymenttypeList;}

        private Integer paymenttypeSt;

        private Integer paymenttypeEd;

        public Integer getPaymenttypeSt(){return this.paymenttypeSt;}

        public Integer getPaymenttypeEd(){return this.paymenttypeEd;}


        public ConditionBuilder idBetWeen(Integer idSt,Integer idEd){
            this.idSt = idSt;
            this.idEd = idEd;
            return this;
        }

        public ConditionBuilder idGreaterEqThan(Integer idSt){
            this.idSt = idSt;
            return this;
        }
        public ConditionBuilder idLessEqThan(Integer idEd){
            this.idEd = idEd;
            return this;
        }


        public ConditionBuilder idList(Integer ... id){
            this.idList = solveNullList(id);
            return this;
        }

        public ConditionBuilder idList(List<Integer> id){
            this.idList = id;
            return this;
        }

        public ConditionBuilder cusIdBetWeen(Integer cusIdSt,Integer cusIdEd){
            this.cusIdSt = cusIdSt;
            this.cusIdEd = cusIdEd;
            return this;
        }

        public ConditionBuilder cusIdGreaterEqThan(Integer cusIdSt){
            this.cusIdSt = cusIdSt;
            return this;
        }
        public ConditionBuilder cusIdLessEqThan(Integer cusIdEd){
            this.cusIdEd = cusIdEd;
            return this;
        }


        public ConditionBuilder cusIdList(Integer ... cusId){
            this.cusIdList = solveNullList(cusId);
            return this;
        }

        public ConditionBuilder cusIdList(List<Integer> cusId){
            this.cusIdList = cusId;
            return this;
        }

        public ConditionBuilder orderIdBetWeen(Long orderIdSt,Long orderIdEd){
            this.orderIdSt = orderIdSt;
            this.orderIdEd = orderIdEd;
            return this;
        }

        public ConditionBuilder orderIdGreaterEqThan(Long orderIdSt){
            this.orderIdSt = orderIdSt;
            return this;
        }
        public ConditionBuilder orderIdLessEqThan(Long orderIdEd){
            this.orderIdEd = orderIdEd;
            return this;
        }


        public ConditionBuilder orderIdList(Long ... orderId){
            this.orderIdList = solveNullList(orderId);
            return this;
        }

        public ConditionBuilder orderIdList(List<Long> orderId){
            this.orderIdList = orderId;
            return this;
        }

        public ConditionBuilder createtimeBetWeen(java.time.LocalDateTime createtimeSt,java.time.LocalDateTime createtimeEd){
            this.createtimeSt = createtimeSt;
            this.createtimeEd = createtimeEd;
            return this;
        }

        public ConditionBuilder createtimeGreaterEqThan(java.time.LocalDateTime createtimeSt){
            this.createtimeSt = createtimeSt;
            return this;
        }
        public ConditionBuilder createtimeLessEqThan(java.time.LocalDateTime createtimeEd){
            this.createtimeEd = createtimeEd;
            return this;
        }


        public ConditionBuilder createtimeList(java.time.LocalDateTime ... createtime){
            this.createtimeList = solveNullList(createtime);
            return this;
        }

        public ConditionBuilder createtimeList(List<java.time.LocalDateTime> createtime){
            this.createtimeList = createtime;
            return this;
        }

        public ConditionBuilder updatetimeBetWeen(java.time.LocalDateTime updatetimeSt,java.time.LocalDateTime updatetimeEd){
            this.updatetimeSt = updatetimeSt;
            this.updatetimeEd = updatetimeEd;
            return this;
        }

        public ConditionBuilder updatetimeGreaterEqThan(java.time.LocalDateTime updatetimeSt){
            this.updatetimeSt = updatetimeSt;
            return this;
        }
        public ConditionBuilder updatetimeLessEqThan(java.time.LocalDateTime updatetimeEd){
            this.updatetimeEd = updatetimeEd;
            return this;
        }


        public ConditionBuilder updatetimeList(java.time.LocalDateTime ... updatetime){
            this.updatetimeList = solveNullList(updatetime);
            return this;
        }

        public ConditionBuilder updatetimeList(List<java.time.LocalDateTime> updatetime){
            this.updatetimeList = updatetime;
            return this;
        }

        public ConditionBuilder moneyBetWeen(Integer moneySt,Integer moneyEd){
            this.moneySt = moneySt;
            this.moneyEd = moneyEd;
            return this;
        }

        public ConditionBuilder moneyGreaterEqThan(Integer moneySt){
            this.moneySt = moneySt;
            return this;
        }
        public ConditionBuilder moneyLessEqThan(Integer moneyEd){
            this.moneyEd = moneyEd;
            return this;
        }


        public ConditionBuilder moneyList(Integer ... money){
            this.moneyList = solveNullList(money);
            return this;
        }

        public ConditionBuilder moneyList(List<Integer> money){
            this.moneyList = money;
            return this;
        }

        public ConditionBuilder stateBetWeen(Integer stateSt,Integer stateEd){
            this.stateSt = stateSt;
            this.stateEd = stateEd;
            return this;
        }

        public ConditionBuilder stateGreaterEqThan(Integer stateSt){
            this.stateSt = stateSt;
            return this;
        }
        public ConditionBuilder stateLessEqThan(Integer stateEd){
            this.stateEd = stateEd;
            return this;
        }


        public ConditionBuilder stateList(Integer ... state){
            this.stateList = solveNullList(state);
            return this;
        }

        public ConditionBuilder stateList(List<Integer> state){
            this.stateList = state;
            return this;
        }

        public ConditionBuilder paymenttypeBetWeen(Integer paymenttypeSt,Integer paymenttypeEd){
            this.paymenttypeSt = paymenttypeSt;
            this.paymenttypeEd = paymenttypeEd;
            return this;
        }

        public ConditionBuilder paymenttypeGreaterEqThan(Integer paymenttypeSt){
            this.paymenttypeSt = paymenttypeSt;
            return this;
        }
        public ConditionBuilder paymenttypeLessEqThan(Integer paymenttypeEd){
            this.paymenttypeEd = paymenttypeEd;
            return this;
        }


        public ConditionBuilder paymenttypeList(Integer ... paymenttype){
            this.paymenttypeList = solveNullList(paymenttype);
            return this;
        }

        public ConditionBuilder paymenttypeList(List<Integer> paymenttype){
            this.paymenttypeList = paymenttype;
            return this;
        }

        private <T>List<T> solveNullList(T ... objs){
            if (objs != null){
            List<T> list = new ArrayList<>();
                for (T item : objs){
                    if (item != null){
                        list.add(item);
                    }
                }
                return list;
            }
            return null;
        }

        public ConditionBuilder build(){return this;}
    }

    public static class Builder {

        private Recharge obj;

        public Builder(){
            this.obj = new Recharge();
        }

        public Builder id(Integer id){
            this.obj.setId(id);
            return this;
        }
        public Builder cusId(Integer cusId){
            this.obj.setCusId(cusId);
            return this;
        }
        public Builder orderId(Long orderId){
            this.obj.setOrderId(orderId);
            return this;
        }
        public Builder createtime(Date createtime){
            this.obj.setCreatetime(createtime);
            return this;
        }
        public Builder updatetime(Date updatetime){
            this.obj.setUpdatetime(updatetime);
            return this;
        }
        public Builder money(Integer money){
            this.obj.setMoney(money);
            return this;
        }
        public Builder state(Integer state){
            this.obj.setState(state);
            return this;
        }
        public Builder paymenttype(Integer paymenttype){
            this.obj.setPaymenttype(paymenttype);
            return this;
        }
        public Recharge build(){return obj;}
    }

}
