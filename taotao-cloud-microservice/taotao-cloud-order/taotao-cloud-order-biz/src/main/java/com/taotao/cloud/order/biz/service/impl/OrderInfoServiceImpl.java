package com.taotao.cloud.order.biz.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.taotao.cloud.elasticsearch.service.IIndexService;
import com.taotao.cloud.mongodb.service.BaseMongoDAO;
import com.taotao.cloud.order.api.bo.order_info.OrderBO;
import com.taotao.cloud.order.api.dubbo.IDubboOrderService;
import com.taotao.cloud.order.biz.entity.order.OrderInfo;
import com.taotao.cloud.order.biz.entity.order.OrderItemBack;
import com.taotao.cloud.order.biz.entity.QOrderInfo;
import com.taotao.cloud.order.biz.stream.StreamFunctionService;
import com.taotao.cloud.order.biz.stream.kafka.OrderProvider;
import com.taotao.cloud.order.biz.mapper.IOrderInfoMapper;
import com.taotao.cloud.order.biz.mapstruct.IOrderMapStruct;
import com.taotao.cloud.order.biz.stream.pulsar.ProductProducer;
import com.taotao.cloud.order.biz.stream.rabbitmq.SmsProvider;
import com.taotao.cloud.order.biz.repository.cls.OrderInfoRepository;
import com.taotao.cloud.order.biz.repository.inf.IOrderInfoRepository;
import com.taotao.cloud.order.biz.stream.rocketmq.EmailProvider;
import com.taotao.cloud.order.biz.service.IOrderInfoService;
import com.taotao.cloud.order.biz.service.IOrderItemService;
import com.taotao.cloud.uc.api.dubbo.IDubboResourceService;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import com.taotao.cloud.zookeeper.template.ZookeeperTemplate;
import java.util.ArrayList;
import java.util.List;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <br>
 *
 * @author shuigedeng
 * @version v1.0.0
 * @create 2020/6/10 16:55
 */
@Service
@DubboService(interfaceClass = IDubboOrderService.class)
public class OrderInfoServiceImpl
	extends BaseSuperServiceImpl<IOrderInfoMapper, OrderInfo, OrderInfoRepository, IOrderInfoRepository, Long>
	implements IDubboOrderService, IOrderInfoService {

	private final static QOrderInfo ORDER_INFO = QOrderInfo.orderInfo;

	@Autowired
	private IOrderItemService<OrderItemBack, Long> orderItemService;

	@Autowired
	private IIndexService indexService;

	@Autowired
	private BaseMongoDAO baseMongoDAO;

	@Autowired
	private ZookeeperTemplate zookeeperTemplate;

	@DubboReference
	private IDubboResourceService dubboResourceService;

	@Autowired
	private OrderProvider orderProvider;
	@Autowired
	private SmsProvider smsProvider;
	@Autowired
	private EmailProvider emailProvider;

	@Autowired
	private ProductProducer productProducer;
	@Autowired
	private StreamFunctionService streamFunctionService;

	@Override
	public Boolean existByCode(String code) {
		BooleanExpression predicate = ORDER_INFO.code.eq(code);
		OrderInfo orderInfoById1 = cr().findOrderInfoById(2L);
		return cr().exists(predicate);
	}

	@Override
	public List<OrderBO> queryRegionByParentId(Long parentId) {
		try {
			//OrderInfo orderInfoById1 = cr().findOrderInfoById(2L);

			streamFunctionService.sendRabbit("streamFunctionService send");

			//streamFunctionService.sendKafka("kafkaFunctionService sned");

			productProducer.send();

			smsProvider.send("我是sms");

			//emailProvider.send("我是email");
			//orderProvider.send("我是order");

			//orderItemService.getById(2L);
			//
			//PageModel<HashMap<String, String>> list = indexService.list("", "");
			//
			//Set<String> collectionNames = baseMongoDAO.getCollectionNames();
			//
			//List<String> children = zookeeperTemplate.getChildren("/");
			//
			//List<ResourceQueryBO> allById = dubboResourceService.queryAllId(1L);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
	}

	@Override
	public OrderBO query(Long id) {
		OrderInfo orderInfo = getById(id);
		return IOrderMapStruct.INSTANCE.entityToBo(orderInfo);
	}
}
