import Taro from "@tarojs/taro";
import {Button, Image, Text, View} from "@tarojs/components";
import "./index.less";
import {orderInformation} from "./service";
import React, {useEffect, useState} from "react";
import useRouter = Taro.useRouter;

var moment = require("moment");
moment.locale("zh-cn");

interface IState {
  query: {
    data?: any;
    loading: boolean
  };
  id: number;
  imageUrl: string;
  title: string;
  number: number;
  amount: number;
}

const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
    query: {
      data: {
        order: {
          price: 1,
          discount: 1,
          amount: 1,
          time: '',
          id: '',
          code: '',
          trade: {
            id: '',
            status: 'paid',
          },
          orderItem: [],

        }
      },
      loading: true
    },
    id: 1,
    imageUrl: '',
    title: '',
    number: 1,
    amount: 1,
  })

  const router = useRouter();

  useEffect(() => {
    const init = async () => {
      const {id} = router.params;
      const result = await orderInformation(id);
      console.log('orderinFormation willmount result', result);

      setState(prevState => {
        return {...prevState, query: result}
      })
    }
    init()
  }, [])

  const copy = tradeid => {
    Taro.setClipboardData({
      data: tradeid,
      success() {
        Taro.showToast({
          title: "复制成功",
          icon: "none"
        });
      }
    });
  }

  const huoqu = (id, code) => {
    Taro.setStorage({
      key: "paymentId",
      data: id
    });
    Taro.setStorage({
      key: "code",
      data: code
    });
    Taro.switchTab({
      url: "../certificates/index"
    });
  }

  const {query} = state;
  const information = query.data.order;
  const amountInformation = [
    {
      id: 1,
      title: "商品金额",
      text: (information.price / 100).toFixed(2)
    },
    {
      id: 3,
      title: "优惠金额",
      text: (information.discount / 100).toFixed(2)
    },
    {
      id: 3,
      title: "实付金额",
      text: (information.amount / 100).toFixed(2)
    }, {
      id: 4,
      title: '支付方式',
      text: information.payment === "balance" ? "余额支付" : '微信支付'
    }
  ];
  //配送订单  门店自提

  return (
    <View className="index">
      {/* 分割线 */}

      <View className="top_line">
        <View className="delivery">
          <Text className="delivery_text_number">订单编号：{information.id.slice(1, 17)}</Text>
        </View>
        <View className="delivery">
          <Text className="delivery_text">
            订单时间：
            {moment(information.createdAt).format("YYYY-MM-DD")}
          </Text>
          <Text className='buyingPatterns'>
            {information.type === 'unmanned' ? '门店自提' : information.type === 'distribution' ? "配送订单" : '门店现购'}
          </Text>
        </View>
      </View>
      {/* <View className="thickLine" /> */}
      <View className='store_information'>
        <Text className='store_text'>门店信息</Text>
        <View className="store_line">
          <Text className="delivery_text_store">{information.store.name}</Text>
          <Text className="delivery_text_store">合肥市包公大道瑶海都市科技园2栋D座101</Text>
        </View>
      </View>

      {/* 取货码 */}
      {information.type === 'unmanned' ? (
        <View className='pickup_box'>
          <Text className='pickup_text'>取货码</Text>
          <Text className='information_code'>{information.code}</Text>
        </View>
      ) : null}

      {/* 购买的商品 */}

      <View className='items_view'>
        <Text>商品清单</Text>
        <View className='item_big_box'>
          {information.orderItem.map((item: IState) => (
            <View className="item_box" key={item.id}>
              <Image src={item.imageUrl} className="itemImage"/>
              <View className="item_right_box">
                <View className="item_right_bottom_box">
                  <Text className="item_title">
                    {item.title}
                  </Text>
                  <Text className="item_price">
                    ￥{(item.amount / 100).toFixed(2)}
                  </Text>
                </View>
                <Text className="item_number">数量：{item.number}</Text>

              </View>
            </View>
          ))}
        </View>
      </View>
      {/* 取货信息 */}
      {information.type === 'unmanned' ? (
        <View className='pick_view'>
          <View className='pick_box'>
            <Text className='pick'>取货日期</Text>
            <Text className='pick'>{information.time}</Text>
          </View>
          <View className='pick_box'>
            <Text className='pick'>取货时间</Text>
            <Text className='pcik_time'>09：00-18：00</Text>
          </View>
        </View>
      ) : information.type === 'distribution' ? (
        <View>
          <View className='address_view'>
            <Text>配送信息</Text>
            <View>
              <Text className="delivery_text_store">
                {information.address.receiverAddress}
              </Text>
              <View>
                <Text className="consignee">
                  {information.address.receiverName}
                </Text>
                <Text className="delivery_text_store">
                  {information.address.receiverPhone}
                </Text>
              </View>
            </View>
          </View>
          <View className='address_view'>
            <Text>配送人员信息</Text>
            <View>
              <Text className="delivery_text_store">
                姓名：{information.distributionUser.name}
              </Text>
              <View>
                <Text className="delivery_text_store">
                  手机号：{information.distributionUser.phone}
                </Text>
              </View>
            </View>
          </View>
        </View>
      ) : null}

      {/* 金额信息 */}
      <View className="amount_information">
        {amountInformation.map(amount => (
          <View className="one_amount_information" key={amount.title}>
            <Text className="amount_title">{amount.title}</Text>
            <Text className="amount_text">{amount.text}</Text>
          </View>
        ))}
      </View>
      {/* 底部按钮 */}
      <View className='bottom_view'>
        <Button open-type="contact" className="bottom_view_text">
          售后服务
        </Button>
        <Button open-type="contact" className="bottom_view_text">
          申请开票
        </Button>
        <Button open-type="contact" className="bottom_view_text">
          联系门店
        </Button>

      </View>
    </View>
  );
}


export default Index
