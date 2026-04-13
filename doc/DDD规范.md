## 团队DDD规范

### 模块构成
- api
- application
- assembly
- common
- domain
- facade
- infrastructure
- interfaces

### assembly模块
- 启动器

### common模块
- 定义公共类、枚举、utils

### api模块
- 定义dto (command、query、response)对象
- 定义inner、rpc、gprc接口 (command、query)接口

### interfaces模块
- 定义各个端的controller接口
- 实现api模块定义的inner、rpc、gprc接口 (command、query)接口

### facade模块
- 定义Application层定义的ACL接口 访问外部接口
- 转换接口的request和response

###
### Application模块
- 定义ACL接口
- 定义assembler接口
- 定义Application Service和实现Application Service Impl
- 定义各种流程编排引擎 实现handler、flow、等组件
- 定义Application 的queryRepository

Application Service职责:
- 管理事务边界（@Transactional）
- 加载聚合（通过Repository）
- 调用Domain Service执行业务规则
- 保存聚合
- 转换DTO
- 调用ACL外部接口、进行业务逻辑处理
- **禁止包含任何业务规则判断**

### Domain模块
- 定义aggregate
- 定义entity
- 定义domain事件
- 定义domain Repository
- 定义valobj
- 定义Domain Service和实现Domain Service Impl

Domain Service职责:
- 封装跨聚合的业务规则
- 封装复杂的计算逻辑
- 不依赖Repository
- 依赖其他Domain Service（如果需要）
- **禁止包含任何技术细节（事务、数据库操作等）**
  关于Domain Service依赖其他Domain Service
  虽然可以这样做，但需要注意：
  避免循环依赖
  尽量减少跨服务调用
  优先考虑通过参数传递数据

### infrastructure模块
- 定义assembler转换器
- 定义configuration(aop、aware、cache、grpc、mq、stream、properties)
- 实现事件的订阅、包括进程内和进程外、并且调用Application Service进行业务逻辑处理
- 实现定时任务的定义、并且调用Application Service进行业务逻辑处理
- 定义Repository 实现DomainRepository和ApplicationQueryRepository
- 定义数据库PO对象、定义mybaits mapper接口、定义jpa Repository接口

## 命令/查询对象命名规范

### 命名格式
{动词}{名词}{类型}

### 示例

| 类型 | 格式 | 示例 |
| :--- | :--- | :--- |
| 创建命令 | Create{名词}Command | CreateOrderCommand |
| 更新命令 | Update{名词}Command | UpdateOrderCommand |
| 删除命令 | Delete{名词}Command | DeleteOrderCommand |
| 查询 | Get{名词}Query | GetOrderQuery |
| 列表查询 | List{名词}Query | ListOrderQuery |

### 业务操作
{业务动词}{名词}Command

| 示例 | 说明 |
| :--- | :--- |
| PlaceOrderCommand | 下单 |
| CancelOrderCommand | 取消订单 |
| PayOrderCommand | 支付订单 |

### 禁止使用
- {名词}{动词}Command（如 OrderCreateCommand）
