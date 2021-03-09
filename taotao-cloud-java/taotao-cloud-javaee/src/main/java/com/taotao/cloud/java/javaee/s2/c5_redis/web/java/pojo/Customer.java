package com.taotao.cloud.java.javaee.s2.c5_redis.web.java.pojo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
/**
*
*  @author author
*/
public class Customer implements Serializable {

    private static final long serialVersionUID = 1586034423739L;


    /**
    * 主键
    * 
    * isNullAble:0
    */
    private Integer id;

    /**
    * 公司名
    * isNullAble:1
    */
    private String username;

    /**
    * 
    * isNullAble:1
    */
    private String password;

    /**
    * 
    * isNullAble:1
    */
    private String nickname;

    /**
    * 金钱
    * isNullAble:1
    */
    private Long money;

    /**
    * 地址
    * isNullAble:1
    */
    private String address;

    /**
    * 状态
    * isNullAble:1
    */
    private Integer state;


    public void setId(Integer id){this.id = id;}

    public Integer getId(){return this.id;}

    public void setUsername(String username){this.username = username;}

    public String getUsername(){return this.username;}

    public void setPassword(String password){this.password = password;}

    public String getPassword(){return this.password;}

    public void setNickname(String nickname){this.nickname = nickname;}

    public String getNickname(){return this.nickname;}

    public void setMoney(Long money){this.money = money;}

    public Long getMoney(){return this.money;}

    public void setAddress(String address){this.address = address;}

    public String getAddress(){return this.address;}

    public void setState(Integer state){this.state = state;}

    public Integer getState(){return this.state;}
    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                "username='" + username + '\'' +
                "password='" + password + '\'' +
                "nickname='" + nickname + '\'' +
                "money='" + money + '\'' +
                "address='" + address + '\'' +
                "state='" + state + '\'' +
            '}';
    }

    public static Builder Build(){return new Builder();}

    public static ConditionBuilder ConditionBuild(){return new ConditionBuilder();}

    public static UpdateBuilder UpdateBuild(){return new UpdateBuilder();}

    public static QueryBuilder QueryBuild(){return new QueryBuilder();}

    public static class UpdateBuilder {

        private Customer set;

        private ConditionBuilder where;

        public UpdateBuilder set(Customer set){
            this.set = set;
            return this;
        }

        public Customer getSet(){
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

    public static class QueryBuilder extends Customer{
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

        private List<String> usernameList;

        public List<String> getUsernameList(){return this.usernameList;}


        private List<String> fuzzyUsername;

        public List<String> getFuzzyUsername(){return this.fuzzyUsername;}

        private List<String> rightFuzzyUsername;

        public List<String> getRightFuzzyUsername(){return this.rightFuzzyUsername;}
        private List<String> passwordList;

        public List<String> getPasswordList(){return this.passwordList;}


        private List<String> fuzzyPassword;

        public List<String> getFuzzyPassword(){return this.fuzzyPassword;}

        private List<String> rightFuzzyPassword;

        public List<String> getRightFuzzyPassword(){return this.rightFuzzyPassword;}
        private List<String> nicknameList;

        public List<String> getNicknameList(){return this.nicknameList;}


        private List<String> fuzzyNickname;

        public List<String> getFuzzyNickname(){return this.fuzzyNickname;}

        private List<String> rightFuzzyNickname;

        public List<String> getRightFuzzyNickname(){return this.rightFuzzyNickname;}
        private List<Long> moneyList;

        public List<Long> getMoneyList(){return this.moneyList;}

        private Long moneySt;

        private Long moneyEd;

        public Long getMoneySt(){return this.moneySt;}

        public Long getMoneyEd(){return this.moneyEd;}

        private List<String> addressList;

        public List<String> getAddressList(){return this.addressList;}


        private List<String> fuzzyAddress;

        public List<String> getFuzzyAddress(){return this.fuzzyAddress;}

        private List<String> rightFuzzyAddress;

        public List<String> getRightFuzzyAddress(){return this.rightFuzzyAddress;}
        private List<Integer> stateList;

        public List<Integer> getStateList(){return this.stateList;}

        private Integer stateSt;

        private Integer stateEd;

        public Integer getStateSt(){return this.stateSt;}

        public Integer getStateEd(){return this.stateEd;}

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

        public QueryBuilder fuzzyUsername (List<String> fuzzyUsername){
            this.fuzzyUsername = fuzzyUsername;
            return this;
        }

        public QueryBuilder fuzzyUsername (String ... fuzzyUsername){
            this.fuzzyUsername = solveNullList(fuzzyUsername);
            return this;
        }

        public QueryBuilder rightFuzzyUsername (List<String> rightFuzzyUsername){
            this.rightFuzzyUsername = rightFuzzyUsername;
            return this;
        }

        public QueryBuilder rightFuzzyUsername (String ... rightFuzzyUsername){
            this.rightFuzzyUsername = solveNullList(rightFuzzyUsername);
            return this;
        }

        public QueryBuilder username(String username){
            setUsername(username);
            return this;
        }

        public QueryBuilder usernameList(String ... username){
            this.usernameList = solveNullList(username);
            return this;
        }

        public QueryBuilder usernameList(List<String> username){
            this.usernameList = username;
            return this;
        }

        public QueryBuilder fetchUsername(){
            setFetchFields("fetchFields","username");
            return this;
        }

        public QueryBuilder excludeUsername(){
            setFetchFields("excludeFields","username");
            return this;
        }

        public QueryBuilder fuzzyPassword (List<String> fuzzyPassword){
            this.fuzzyPassword = fuzzyPassword;
            return this;
        }

        public QueryBuilder fuzzyPassword (String ... fuzzyPassword){
            this.fuzzyPassword = solveNullList(fuzzyPassword);
            return this;
        }

        public QueryBuilder rightFuzzyPassword (List<String> rightFuzzyPassword){
            this.rightFuzzyPassword = rightFuzzyPassword;
            return this;
        }

        public QueryBuilder rightFuzzyPassword (String ... rightFuzzyPassword){
            this.rightFuzzyPassword = solveNullList(rightFuzzyPassword);
            return this;
        }

        public QueryBuilder password(String password){
            setPassword(password);
            return this;
        }

        public QueryBuilder passwordList(String ... password){
            this.passwordList = solveNullList(password);
            return this;
        }

        public QueryBuilder passwordList(List<String> password){
            this.passwordList = password;
            return this;
        }

        public QueryBuilder fetchPassword(){
            setFetchFields("fetchFields","password");
            return this;
        }

        public QueryBuilder excludePassword(){
            setFetchFields("excludeFields","password");
            return this;
        }

        public QueryBuilder fuzzyNickname (List<String> fuzzyNickname){
            this.fuzzyNickname = fuzzyNickname;
            return this;
        }

        public QueryBuilder fuzzyNickname (String ... fuzzyNickname){
            this.fuzzyNickname = solveNullList(fuzzyNickname);
            return this;
        }

        public QueryBuilder rightFuzzyNickname (List<String> rightFuzzyNickname){
            this.rightFuzzyNickname = rightFuzzyNickname;
            return this;
        }

        public QueryBuilder rightFuzzyNickname (String ... rightFuzzyNickname){
            this.rightFuzzyNickname = solveNullList(rightFuzzyNickname);
            return this;
        }

        public QueryBuilder nickname(String nickname){
            setNickname(nickname);
            return this;
        }

        public QueryBuilder nicknameList(String ... nickname){
            this.nicknameList = solveNullList(nickname);
            return this;
        }

        public QueryBuilder nicknameList(List<String> nickname){
            this.nicknameList = nickname;
            return this;
        }

        public QueryBuilder fetchNickname(){
            setFetchFields("fetchFields","nickname");
            return this;
        }

        public QueryBuilder excludeNickname(){
            setFetchFields("excludeFields","nickname");
            return this;
        }

        public QueryBuilder moneyBetWeen(Long moneySt,Long moneyEd){
            this.moneySt = moneySt;
            this.moneyEd = moneyEd;
            return this;
        }

        public QueryBuilder moneyGreaterEqThan(Long moneySt){
            this.moneySt = moneySt;
            return this;
        }
        public QueryBuilder moneyLessEqThan(Long moneyEd){
            this.moneyEd = moneyEd;
            return this;
        }


        public QueryBuilder money(Long money){
            setMoney(money);
            return this;
        }

        public QueryBuilder moneyList(Long ... money){
            this.moneyList = solveNullList(money);
            return this;
        }

        public QueryBuilder moneyList(List<Long> money){
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

        public QueryBuilder fuzzyAddress (List<String> fuzzyAddress){
            this.fuzzyAddress = fuzzyAddress;
            return this;
        }

        public QueryBuilder fuzzyAddress (String ... fuzzyAddress){
            this.fuzzyAddress = solveNullList(fuzzyAddress);
            return this;
        }

        public QueryBuilder rightFuzzyAddress (List<String> rightFuzzyAddress){
            this.rightFuzzyAddress = rightFuzzyAddress;
            return this;
        }

        public QueryBuilder rightFuzzyAddress (String ... rightFuzzyAddress){
            this.rightFuzzyAddress = solveNullList(rightFuzzyAddress);
            return this;
        }

        public QueryBuilder address(String address){
            setAddress(address);
            return this;
        }

        public QueryBuilder addressList(String ... address){
            this.addressList = solveNullList(address);
            return this;
        }

        public QueryBuilder addressList(List<String> address){
            this.addressList = address;
            return this;
        }

        public QueryBuilder fetchAddress(){
            setFetchFields("fetchFields","address");
            return this;
        }

        public QueryBuilder excludeAddress(){
            setFetchFields("excludeFields","address");
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

        public Customer build(){return this;}
    }


    public static class ConditionBuilder{
        private List<Integer> idList;

        public List<Integer> getIdList(){return this.idList;}

        private Integer idSt;

        private Integer idEd;

        public Integer getIdSt(){return this.idSt;}

        public Integer getIdEd(){return this.idEd;}

        private List<String> usernameList;

        public List<String> getUsernameList(){return this.usernameList;}


        private List<String> fuzzyUsername;

        public List<String> getFuzzyUsername(){return this.fuzzyUsername;}

        private List<String> rightFuzzyUsername;

        public List<String> getRightFuzzyUsername(){return this.rightFuzzyUsername;}
        private List<String> passwordList;

        public List<String> getPasswordList(){return this.passwordList;}


        private List<String> fuzzyPassword;

        public List<String> getFuzzyPassword(){return this.fuzzyPassword;}

        private List<String> rightFuzzyPassword;

        public List<String> getRightFuzzyPassword(){return this.rightFuzzyPassword;}
        private List<String> nicknameList;

        public List<String> getNicknameList(){return this.nicknameList;}


        private List<String> fuzzyNickname;

        public List<String> getFuzzyNickname(){return this.fuzzyNickname;}

        private List<String> rightFuzzyNickname;

        public List<String> getRightFuzzyNickname(){return this.rightFuzzyNickname;}
        private List<Long> moneyList;

        public List<Long> getMoneyList(){return this.moneyList;}

        private Long moneySt;

        private Long moneyEd;

        public Long getMoneySt(){return this.moneySt;}

        public Long getMoneyEd(){return this.moneyEd;}

        private List<String> addressList;

        public List<String> getAddressList(){return this.addressList;}


        private List<String> fuzzyAddress;

        public List<String> getFuzzyAddress(){return this.fuzzyAddress;}

        private List<String> rightFuzzyAddress;

        public List<String> getRightFuzzyAddress(){return this.rightFuzzyAddress;}
        private List<Integer> stateList;

        public List<Integer> getStateList(){return this.stateList;}

        private Integer stateSt;

        private Integer stateEd;

        public Integer getStateSt(){return this.stateSt;}

        public Integer getStateEd(){return this.stateEd;}


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

        public ConditionBuilder fuzzyUsername (List<String> fuzzyUsername){
            this.fuzzyUsername = fuzzyUsername;
            return this;
        }

        public ConditionBuilder fuzzyUsername (String ... fuzzyUsername){
            this.fuzzyUsername = solveNullList(fuzzyUsername);
            return this;
        }

        public ConditionBuilder rightFuzzyUsername (List<String> rightFuzzyUsername){
            this.rightFuzzyUsername = rightFuzzyUsername;
            return this;
        }

        public ConditionBuilder rightFuzzyUsername (String ... rightFuzzyUsername){
            this.rightFuzzyUsername = solveNullList(rightFuzzyUsername);
            return this;
        }

        public ConditionBuilder usernameList(String ... username){
            this.usernameList = solveNullList(username);
            return this;
        }

        public ConditionBuilder usernameList(List<String> username){
            this.usernameList = username;
            return this;
        }

        public ConditionBuilder fuzzyPassword (List<String> fuzzyPassword){
            this.fuzzyPassword = fuzzyPassword;
            return this;
        }

        public ConditionBuilder fuzzyPassword (String ... fuzzyPassword){
            this.fuzzyPassword = solveNullList(fuzzyPassword);
            return this;
        }

        public ConditionBuilder rightFuzzyPassword (List<String> rightFuzzyPassword){
            this.rightFuzzyPassword = rightFuzzyPassword;
            return this;
        }

        public ConditionBuilder rightFuzzyPassword (String ... rightFuzzyPassword){
            this.rightFuzzyPassword = solveNullList(rightFuzzyPassword);
            return this;
        }

        public ConditionBuilder passwordList(String ... password){
            this.passwordList = solveNullList(password);
            return this;
        }

        public ConditionBuilder passwordList(List<String> password){
            this.passwordList = password;
            return this;
        }

        public ConditionBuilder fuzzyNickname (List<String> fuzzyNickname){
            this.fuzzyNickname = fuzzyNickname;
            return this;
        }

        public ConditionBuilder fuzzyNickname (String ... fuzzyNickname){
            this.fuzzyNickname = solveNullList(fuzzyNickname);
            return this;
        }

        public ConditionBuilder rightFuzzyNickname (List<String> rightFuzzyNickname){
            this.rightFuzzyNickname = rightFuzzyNickname;
            return this;
        }

        public ConditionBuilder rightFuzzyNickname (String ... rightFuzzyNickname){
            this.rightFuzzyNickname = solveNullList(rightFuzzyNickname);
            return this;
        }

        public ConditionBuilder nicknameList(String ... nickname){
            this.nicknameList = solveNullList(nickname);
            return this;
        }

        public ConditionBuilder nicknameList(List<String> nickname){
            this.nicknameList = nickname;
            return this;
        }

        public ConditionBuilder moneyBetWeen(Long moneySt,Long moneyEd){
            this.moneySt = moneySt;
            this.moneyEd = moneyEd;
            return this;
        }

        public ConditionBuilder moneyGreaterEqThan(Long moneySt){
            this.moneySt = moneySt;
            return this;
        }
        public ConditionBuilder moneyLessEqThan(Long moneyEd){
            this.moneyEd = moneyEd;
            return this;
        }


        public ConditionBuilder moneyList(Long ... money){
            this.moneyList = solveNullList(money);
            return this;
        }

        public ConditionBuilder moneyList(List<Long> money){
            this.moneyList = money;
            return this;
        }

        public ConditionBuilder fuzzyAddress (List<String> fuzzyAddress){
            this.fuzzyAddress = fuzzyAddress;
            return this;
        }

        public ConditionBuilder fuzzyAddress (String ... fuzzyAddress){
            this.fuzzyAddress = solveNullList(fuzzyAddress);
            return this;
        }

        public ConditionBuilder rightFuzzyAddress (List<String> rightFuzzyAddress){
            this.rightFuzzyAddress = rightFuzzyAddress;
            return this;
        }

        public ConditionBuilder rightFuzzyAddress (String ... rightFuzzyAddress){
            this.rightFuzzyAddress = solveNullList(rightFuzzyAddress);
            return this;
        }

        public ConditionBuilder addressList(String ... address){
            this.addressList = solveNullList(address);
            return this;
        }

        public ConditionBuilder addressList(List<String> address){
            this.addressList = address;
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

        private Customer obj;

        public Builder(){
            this.obj = new Customer();
        }

        public Builder id(Integer id){
            this.obj.setId(id);
            return this;
        }
        public Builder username(String username){
            this.obj.setUsername(username);
            return this;
        }
        public Builder password(String password){
            this.obj.setPassword(password);
            return this;
        }
        public Builder nickname(String nickname){
            this.obj.setNickname(nickname);
            return this;
        }
        public Builder money(Long money){
            this.obj.setMoney(money);
            return this;
        }
        public Builder address(String address){
            this.obj.setAddress(address);
            return this;
        }
        public Builder state(Integer state){
            this.obj.setState(state);
            return this;
        }
        public Customer build(){return obj;}
    }

}
