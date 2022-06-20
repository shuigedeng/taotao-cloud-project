import Taro, {useRouter} from "@tarojs/taro";
import {Button, Image, ScrollView, Text, View} from "@tarojs/components";
import {details} from "./service";
import "./index.less";
import detailsHome from "../../img/homePage.png";
import shopping from "../../img/shopping.png";
import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";

interface IState {
  number: number;
  isOpened: boolean;
  activeColor: number;
  activityId: number;
  query: {
    data: {
      item?: any;
    };
    loading: boolean
  };
  itemInformation?: any;
}

const Index: Taro.FC = () => {
  let [state, setState] = useState<IState>({
    isOpened: false,
    number: 1,
    activityId: 1,
    activeColor: 1,
    query: {
      data: {
        item: {
          code: null,
          imageUrl: '',
          name: null,
          stock: null,
          content: null,
          followed: null,
          unit: null,
          price: 1,
          originalPrice: 1,
          memberPrice: 1,
        }
      },
      loading: true
    },
    itemInformation: null
  })

  // @ts-ignore
  const cartItems = useSelector(({cart}) => cart.cartItems);
  const router = useRouter();
  const dispatch = useDispatch();

  useEffect(() => {
    const init = async () => {
      const {code} = router.params;

      const result = await details(code);
      console.log('商品详情result', result);

      setState(prevState => {
        return {...prevState, query: result}
      })
    }
    init()
  }, [])

  const handleClose = () => {
    setState(prevState => {
      return {...prevState, isOpened: false}
    })
  }

  const handleChangeNumber = (value) => {
    setState(prevState => {
      return {...prevState, number: value}
    })
  }

  const toHome = () => {
    Taro.switchTab({
      url: "../home/index"
    });
  }

  const shoppingCart = () => {
    Taro.switchTab({
      url: "../shoppingCart/index"
    });
  }

  const address = () => {
    Taro.chooseAddress({
      success(e) {

      }
    });
  }

  /**
   * 加入购物
   * @param a 商品的 ID
   * @param b 名称
   * @param c 数量
   * @param d 价格
   * @param e 图片
   */
  const onOpenDoor = (code, name, number, price, unit, imageUrl, originalPrice) => {
    const data: any = {};
    data.itemId = code;
    data.name = name;
    data.number = number;
    data.price = price;
    data.unit = unit;
    data.imageUrl = imageUrl;
    data.originalPrice = originalPrice;
    dispatch({
      type: 'common/addToCartByCode',
      payload: data
    });
  }

  // 收藏
  const collection = async () => {
    const token = Taro.getStorageSync('accessToken');
    if (token) {
      const {query} = state;
      const itemCode = query.data.item.code;
      await collection(itemCode, "");
      const {code} = router.params;
      const result = await details(code);
      setState(prevState => {
        return {...prevState, query: result}
      })

      Taro.showToast({
        title: '已收藏',
        icon: 'success',
        duration: 1500,
        mask: true,
      });
    } else {
      Taro.switchTab({
        url: "../mine/index"
      });
    }
  }

  const trueCollection = async () => {
    const {query} = state;
    const itemCode = query.data.item.code;
    await collection(itemCode, "false");
    const {code} = router.params;
    const result = await details(code);
    setState(prevState => {
      return {...prevState, query: result}
    })
    Taro.showToast({
      title: '已取消收藏',
      icon: 'success',
      duration: 1500,
      mask: true,
    });
  }

  const {query} = state;
  const length = cartItems.length;
  console.log('query.data.item.originalPrice', query.data.item.originalPrice);

  return (
    <ScrollView className="index">
      {/* 顶部图 */}
      <View className="topBanner-box">
        <Image src={query.data.item.imageUrl} className="banner"/>
      </View>
      {/* 名称 */}
      <View className="title_share">
        <Text className="item_title">{query.data.item.name}</Text>
        <Button open-type="share" className="shareButton">
          <Image src='https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/share.png'
                 className="share"/>
          分享
        </Button>
      </View>
      {/* 价格 */}
      <View className="price">
        <Text className="memberPrice">
          会员价￥{(query.data.item.memberPrice / 100).toFixed(2)}
        </Text>
        <Text className="presentPrice">
          ￥{(query.data.item.price / 100).toFixed(2)}
        </Text>
        <Text className="originalPrice">
          ￥{(query.data.item.originalPrice / 100).toFixed(2)}
        </Text>
        <Text className="stockName">库存:{query.data.item.stock} </Text>
      </View>
      {/* 保证 */}
      <View className="ensures">
        <Text className="ensure">正品担保 7天放心退 假一赔十</Text>
      </View>
      {/* 分割线 */}
      <View className="coarseLine"/>
      {/* 商品介绍 */}
      <View className="item_introduces">
        <Text className="specifications_text_bold">商品规格 \n</Text>
        <Text className="introduce_name">{query.data.item.content}</Text>
      </View>
      {/* 分割线 */}
      <View className="coarseLine"/>
      {/* 图文详情 */}
      <View className="img_text_box">
        <Text className="graphic_introduces">价格说明</Text>
        <Text className="introduce_price">
          有地道价：有地道价为商品的销售价，是您最终决定是否购买商品的依据。
        </Text>
        <Text className="introduce_price">
          划线价：商品展示的划横线价格为参考价，并非原价，该价格可能是品牌专柜标价、商品吊牌价或由品牌供应商提供的正品零售价（如厂商指导价、建议零售价等）或该商品在有地道平台或者门店上曾经展示过的销售价；由于地区、时间的差异性和市场行情波动，品牌专柜标价、商品吊牌价等可能会与您购物时展示的不一致，该价格仅供您参考。
        </Text>
        <Text className="introduce_price">
          折扣：如无特殊说明，折扣指销售商在原价、或划线价（如品牌专柜标价、商品吊牌价、厂商指导价、厂商建议零售价）等某一价格基础上计算出的优惠比例或优惠金额；如有疑问，您可在购买前联系客服进行咨询。
        </Text>
        <Text className="introduce_price">
          异常问题：商品促销信息以商品详情页“促销”栏中的信息为准；商品的具体售价以订单结算页价格为准；如您发现活动商品售价或促销信息有异常，建议购买前先联系客服咨询。
        </Text>
      </View>
      {/* 底部按钮栏 */}
      {length > 0 ? (
        <View className="length">
          <View className="length1">
            <Text className="icon_text2">{length}</Text>
          </View>
        </View>
      ) : (
        null
      )}
      <View className="bottom_box">
        <View className="icon_text" onClick={toHome}>
          <Image src={detailsHome} className="detailsHome"/>
          <Text className="bottom_text">首页</Text>
        </View>
        {query.data.item.followed ? (
          <View className="icon_text1" onClick={trueCollection}>
            <Image
              src="https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/truedetailsCart.png"
              className="detailsCart"/>
            <Text className="bottom_text1">已收藏</Text>
          </View>
        ) : (
          <View className="icon_text1" onClick={collection}>
            <Image src="https://mengmao-qingying-files.oss-cn-hangzhou.aliyuncs.com/shoucang.png"
                   className="detailsCart"/>
            <Text className="bottom_text">收藏</Text>
          </View>
        )}
        <View className="icon_text1" onClick={shoppingCart}>
          <Image src={shopping} className="detailsHome"/>
          <Text className="bottom_text">购物车</Text>
        </View>
        <View className="buy_box"
              onClick={() => onOpenDoor(
                query.data.item.code,
                query.data.item.name,
                1,
                query.data.item.price,
                query.data.item.unit,
                query.data.item.imageUrl,
                query.data.item.originalPrice,
                query.data.item.content,
                query.data.item.memberPrice
              )}
        >
          <Text className="buy_text">加入购物车</Text>
        </View>
      </View>
    </ScrollView>
  );
}


export default Index
