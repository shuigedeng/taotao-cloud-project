import Taro from '@tarojs/taro';
import {
  Block,
  Button,
  Image,
  Input,
  Navigator,
  RichText,
  Swiper,
  SwiperItem,
  Text,
  View
} from '@tarojs/components';
import {AtIcon} from 'taro-ui';
import {getGoodsDetail, goodsCollectAddOrDelete} from '../../services/goods';
import {groupOnJoin} from '../../services/group';
import {addCart, cartFastAdd, getCartGoodsCount} from '../../services/cart';

import {showErrorToast} from '../../utils/util';
import {ImgFriend, ImgWeChat} from '../../static/images';

import './index.less';
import React, {useEffect, useState} from 'react';
import getCurrentInstance = Taro.getCurrentInstance;

const Index: Taro.FC<any> = (props) => {
  //navigationBarTitleText: '商品详情'

  const [state, setState] = useState({
    canShare: false,
    id: 0,
    goods: {},
    groupon: [], //该商品支持的团购规格
    grouponLink: {}, //参与的团购
    attribute: [],
    issueList: [],
    comment: {},
    brand: {},
    specificationList: [],
    productList: [],
    relatedGoods: [],
    cartGoodsCount: 0,
    userHasCollect: 0,
    number: 1,
    checkedSpecText: '规格数量选择',
    tmpSpecText: '请选择规格数量',
    checkedSpecPrice: 0,
    openAttr: false,
    openShare: false,
    collect: false,
    shareImage: '',
    isGroupon: false, //标识是否是一个参团购买
    soldout: false,
    canWrite: false, //用户是否获取了保存相册的权限
    grouponId: ''
  })

  useEffect(() => {
    getGoodsInfo();
  }, [state.id])

  useEffect(() => {
    getGrouponInfo(state.grouponId);
  }, [state.isGroupon, state.grouponId])


  useEffect(() => {
    let router = getCurrentInstance().router;
    const {id, grouponId} = router.params;

    if (id) {
      setState(prevState => {
        return {...prevState, id: parseInt(id),}
      })
    }

    // 记得打开
    if (grouponId) {
      setState(prevState => {
        return {
          ...prevState,
          isGroupon: true,
          grouponId: grouponId
        }
      })
    }

    let that = this;
    Taro.getSetting({
      success: function (res) {
        console.log(res)
        //不存在相册授权
        if (!res.authSetting['scope.writePhotosAlbum']) {
          Taro.authorize({
            scope: 'scope.writePhotosAlbum',
            success: function () {
              setState(prevState => {
                return {...prevState, canWrite: true}
              })
            },
            fail: function (err) {
              setState(prevState => {
                return {...prevState, canWrite: false}
              })
            }
          })
        } else {
          setState(prevState => {
            return {...prevState, canWrite: true}
          })
        }
      }
    })
  }, [])

  useEffect(() => {
    // 页面显示
    getCartGoodsCount().then(res => {
      setState(prevState => {
        return {...prevState, cartGoodsCount: res || 0}
      })
    })
  }, [])


  const getGrouponInfo = (grouponId) => {
    groupOnJoin({
      grouponId: grouponId
    }).then(res => {
      setState(prevState => {
        return {
          ...prevState,
          grouponLink: res.groupon,
          id: res.goods.id
        }
      })
      //获取商品详情
      getGoodsInfo();
    })
  }

  const getGoodsInfo = () => {
    const {id} = state;
    getGoodsDetail(id).then(res => {
      console.log('----res--------', res);

      let _specificationList = res.specificationList
      // 如果仅仅存在一种货品，那么商品页面初始化时默认checked
      if (_specificationList.length == 1) {
        if (_specificationList[0].valueList.length == 1) {
          _specificationList[0].valueList[0].checked = true

          // 如果仅仅存在一种货品，那么商品价格应该和货品价格一致
          // 这里检测一下
          let _productPrice = res.productList[0].price;
          let _goodsPrice = res.info.retailPrice;
          if (_productPrice != _goodsPrice) {
            console.error('商品数量价格和货品不一致');
          }

          setState(prevState => {
            return {
              ...prevState,
              checkedSpecText: _specificationList[0].valueList[0].value,
              tmpSpecText: '已选择：' + _specificationList[0].valueList[0].value,
            }
          })
        }
      }

      res.info.detail2 = res.info.detail.replace(/style=\"\"/gi, `style="width: 100%;height: ${Taro.pxTransform(375)}"`)

      setState(prevState => {
        return {
          ...prevState,
          goods: res.info,
          attribute: res.attribute,
          issueList: res.issue,
          comment: res.comment,
          brand: res.brand,
          specificationList: res.specificationList,
          productList: res.productList,
          userHasCollect: res.userHasCollect,
          shareImage: res.shareImage,
          checkedSpecPrice: res.info.retailPrice,
          groupon: res.groupon,
          canShare: res.share,
        }
      })

      //如果是通过分享的团购参加团购，则团购项目应该与分享的一致并且不可更改
      if (state.isGroupon) {
        let groupons = state.groupon;
        for (var i = 0; i < groupons.length; i++) {
          if (groupons[i].id != state.grouponLink.rulesId) {
            groupons.splice(i, 1);
          }
        }
        groupons[0].checked = true;
        //重设团购规格
        setState(prevState => {
          return {...prevState, groupon: groupons}
        })
      }

      setState(prevState => {
        return {...prevState, collect: res.userHasCollect == 1}
      })

      // WxParse.wxParse('goodsDetail', 'html', res.info.detail, that);
      //获取推荐商品
      setTimeout(() => {
        getGoodsRelated();
      }, 5)

    });
  }

  const getGoodsRelated = () => {
    const {id} = state;

    getGoodsRelated(id).then(res => {
      setState({
        relatedGoods: res.list,
      });
    })
  }

  const shareFriendOrCircle = () => {
    if (state.openShare === false) {
      setState(prevState => {
        return {...prevState, openShare: !prevState.openShare}
      })
    } else {
      return false;
    }
  }

  const closeShare = () => {
    setState(prevState => {
      return {...prevState, openShare: false,}
    })
  }

  const handleSetting = (e) => {
    console.log(e)
    // TODO 需测试
    if (!e.detail.authSetting['scope.writePhotosAlbum']) {
      Taro.showModal({
        title: '警告',
        content: '不授权无法保存',
        showCancel: false
      })
      setState(prevState => {
        return {...prevState, canWrite: false}
      })
    } else {
      Taro.showToast({
        title: '保存成功'
      })
      setState(prevState => {
        return {...prevState, canWrite: true}
      })
    }
  }

  const saveShare = () => {
    Taro.downloadFile({
      url: state.shareImage,
      success: function (res) {
        console.log(res)
        Taro.saveImageToPhotosAlbum({
          filePath: res.tempFilePath,
          success: function () {
            Taro.showModal({
              title: '存图成功',
              content: '图片成功保存到相册了，可以分享到朋友圈了',
              showCancel: false,
              confirmText: '好的',
              confirmColor: '#a78845',
              success: function (res1) {
                if (res1.confirm) {
                  console.log('用户点击确定');
                }
              }
            })
          },
          fail: function () {
            console.log('fail')
          }
        })
      },
      fail: function () {
        console.log('fail')
      }
    })
  }

  const switchAttrPop = () => {
    if (state.openAttr == false) {
      setState(prevState => {
        return {...prevState, openAttr: !state.openAttr}
      })
    }
  }

  const closeAttr = () => {
    setState(prevState => {
      return {...prevState, openAttr: false,}
    })
  }

  //获取选中的规格信息
  const getCheckedSpecValue = () => {
    let checkedValues = [];
    let _specificationList = state.specificationList;
    for (let i = 0; i < _specificationList.length; i++) {
      let _checkedObj = {
        name: _specificationList[i].name,
        valueId: 0,
        valueText: ''
      };
      for (let j = 0; j < _specificationList[i].valueList.length; j++) {
        if (_specificationList[i].valueList[j].checked) {
          _checkedObj.valueId = _specificationList[i].valueList[j].id;
          _checkedObj.valueText = _specificationList[i].valueList[j].value;
        }
      }
      checkedValues.push(_checkedObj);
    }

    return checkedValues;
  }

  const isCheckedAllSpec = () => {
    return !getCheckedSpecValue().some(function (v) {
      if (v.valueId == 0) {
        return true;
      }
    });
  }

  const getCheckedProductItem = (key) => {
    return state.productList.filter(function (v) {
      if (v.specifications.toString() == key.toString()) {
        return true;
      } else {
        return false;
      }
    });
  }

  const getCheckedSpecKey = () => {
    return getCheckedSpecValue().map(function (v) {
      return v.valueText;
    });
  }

  const changeSpecInfo = () => {
    let checkedNameValue = getCheckedSpecValue();

    //设置选择的信息
    let checkedValue = checkedNameValue.filter(function (v) {
      if (v.valueId != 0) {
        return true;
      } else {
        return false;
      }
    }).map(function (v) {
      return v.valueText;
    });
    if (checkedValue.length > 0) {
      setState(prevState => {
        return {...prevState, tmpSpecText: checkedValue.join('　')}
      })
    } else {
      setState(prevState => {
        return {...prevState, tmpSpecText: '请选择规格数量'}
      })
    }

    if (isCheckedAllSpec()) {
      setState(prevState => {
        return {...prevState, checkedSpecText: state.tmpSpecText}
      })

      // 规格所对应的货品选择以后
      let checkedProductArray = getCheckedProductItem(getCheckedSpecKey());
      if (!checkedProductArray || checkedProductArray.length <= 0) {
        setState(prevState => {
          return {...prevState, soldout: true}
        })
        console.error('规格所对应货品不存在');
        return;
      }

      let checkedProduct = checkedProductArray[0];
      if (checkedProduct.number > 0) {
        setState(prevState => {
          return {
            ...prevState,
            checkedSpecPrice: checkedProduct.price,
            soldout: false
          }
        })
      } else {
        setState(prevState => {
          return {
            ...prevState,
            checkedSpecPrice: state.goods.retailPrice,
            soldout: true
          }
        })
      }
    } else {
      setState(prevState => {
        return {
          ...prevState,
          checkedSpecText: '规格数量选择',
          checkedSpecPrice: state.goods.retailPrice,
          soldout: false
        }
      })
    }
  }

  const clickSkuValue = (data) => {
    let specName = data.specification;
    let specValueId = data.id;

    //判断是否可以点击

    //TODO 性能优化，可在wx:for中添加index，可以直接获取点击的属性名和属性值，不用循环
    let _specificationList = state.specificationList;
    for (let i = 0; i < _specificationList.length; i++) {
      if (_specificationList[i].name === specName) {
        for (let j = 0; j < _specificationList[i].valueList.length; j++) {
          if (_specificationList[i].valueList[j].id == specValueId) {
            //如果已经选中，则反选
            if (_specificationList[i].valueList[j].checked) {
              _specificationList[i].valueList[j].checked = false;
            } else {
              _specificationList[i].valueList[j].checked = true;
            }
          } else {
            _specificationList[i].valueList[j].checked = false;
          }
        }
      }
    }

    setState(prevState => {
      return {...prevState, specificationList: _specificationList}
    })
    //重新计算哪些值不可以点击
  }

  useEffect(() => {
    changeSpecInfo();
  }, [state.specificationList])

  // 团购选择
  const clickGroupon = (data) => {
    //参与团购，不可更改选择
    if (state.isGroupon) {
      return;
    }

    // let specName = data.specification;
    let specValueId = data.id;

    let _grouponList = state.groupon;
    for (let i = 0; i < _grouponList.length; i++) {
      if (_grouponList[i].id == specValueId) {
        if (_grouponList[i].checked) {
          _grouponList[i].checked = false;
        } else {
          _grouponList[i].checked = true;
        }
      } else {
        _grouponList[i].checked = false;
      }
    }

    setState(prevState => {
      return {...prevState, groupon: _grouponList,}
    })
  }

  const cutNumber = () => {
    setState(prevState => {
      return {...prevState, number: (state.number - 1 > 1) ? state.number - 1 : 1}
    })
  }

  const addNumber = () => {
    setState(prevState => {
      return {...prevState, number: state.number + 1}
    })
  }

  //添加或是取消收藏
  const addCollectOrNot = () => {
    goodsCollectAddOrDelete({
      type: 0,
      valueId: state.id
    }).then((res) => {
      if (state.userHasCollect == 1) {
        setState(prevState => {
          return {
            ...prevState,
            collect: false,
            userHasCollect: 0
          }
        })
      } else {
        setState(prevState => {
          return {
            ...prevState,
            collect: true,
            userHasCollect: 1
          }
        })
      }
    })
  }

  const openCartPage = () => {
    Taro.switchTab({
      url: '/pages/cart/cart'
    });
  }

  const addToCart = () => {
    if (state.openAttr == false) {
      //打开规格选择窗口
      setState(prevState => {
        return {
          ...prevState,
          openAttr: !state.openAttr
        }
      })
    } else {
      //提示选择完整规格
      if (!isCheckedAllSpec()) {
        showErrorToast('请选择完整规格');
        return false;
      }

      //根据选中的规格，判断是否有对应的sku信息
      let checkedProductArray = getCheckedProductItem(getCheckedSpecKey());
      if (!checkedProductArray || checkedProductArray.length <= 0) {
        //找不到对应的product信息，提示没有库存
        showErrorToast('没有库存');
        return false;
      }

      let checkedProduct = checkedProductArray[0];
      //验证库存
      if (checkedProduct.number <= 0) {
        showErrorToast('没有库存');
        return false;
      }

      // 添加购物车
      addCart({
        goodsId: state.goods.id,
        number: state.number,
        productId: checkedProduct.id
      }).then(res => {
        Taro.showToast({
          title: '添加成功'
        });
        setState(prevState => {
          return {
            ...prevState,
            openAttr: !state.openAttr,
            cartGoodsCount: res
          }
        })

        if (state.userHasCollect == 1) {
          setState(prevState => {
            return {
              ...prevState,
              collect: true
            }
          })
        } else {
          setState(prevState => {
            return {
              ...prevState,
              collect: false
            }
          })
        }
      })
    }
  }

  //获取选中的团购信息
  const getCheckedGrouponValue = () => {
    let checkedValues = {};
    let _grouponList = state.groupon;
    for (let i = 0; i < _grouponList.length; i++) {
      if (_grouponList[i].checked) {
        checkedValues = _grouponList[i];
      }
    }

    return checkedValues;
  }

  const addFast = () => {
    if (state.openAttr == false) {
      //打开规格选择窗口
      setState(prevState => {
        return {
          ...prevState,
          openAttr: !state.openAttr
        }
      })
    } else {

      //提示选择完整规格
      if (!isCheckedAllSpec()) {
        showErrorToast('请选择完整规格');
        return false;
      }

      //根据选中的规格，判断是否有对应的sku信息
      let checkedProductArray = getCheckedProductItem(getCheckedSpecKey());
      if (!checkedProductArray || checkedProductArray.length <= 0) {
        //找不到对应的product信息，提示没有库存
        showErrorToast('没有库存');
        return false;
      }

      let checkedProduct = checkedProductArray[0];
      //验证库存
      if (checkedProduct.number <= 0) {
        showErrorToast('没有库存');
        return false;
      }

      //验证团购是否有效
      let checkedGroupon = getCheckedGrouponValue();

      //立即购买
      cartFastAdd({
        goodsId: state.goods.id,
        number: state.number,
        productId: checkedProduct.id
      }).then(res => {
        Taro.setStorageSync('cartId', res);
        Taro.setStorageSync('grouponRulesId', checkedGroupon.id);
        Taro.setStorageSync('grouponLinkId', state.grouponLink.id);
        Taro.navigateTo({
          url: '/pages/checkout/checkout'
        })
      })
    }
  }

  const {
    canShare,
    collect,
    cartGoodsCount,
    soldout,
    groupon,
    number,
    specificationList,
    tmpSpecText,
    openAttr,
    canWrite,
    goods,
    isGroupon,
    brand,
    comment,
    attribute,
    issueList,
    relatedGoods,
    openShare,
    checkedSpecText,
    checkedSpecPrice
  } = state;

  return (
    <Block>
      <View className='container'>
        <Swiper className='goodsimgs' indicator-dots='true'
                autoplay={true} interval={3000}
                duration={1000}>
          {Array.isArray(goods.gallery) && goods.gallery.map(item => {
            return <SwiperItem key={item}>
              <Image className='img' src={item} background-size='cover'></Image>
            </SwiperItem>
          })}

        </Swiper>
        {/* <!-- 分享 --> */}
        <View className='goods_name'>
          <View className='goods_name_left'>{goods.name}</View>
          {
            !canShare && <View className='goods_name_right' onClick={shareFriendOrCircle}>分享</View>
          }
        </View>
        <View className='share-pop-box' style={{display: !openShare ? 'none' : 'block'}}>
          <View className='share-pop'>
            <View className='close' onClick={closeShare}>
              <AtIcon className='icon' size='14' color='#666' value='close'/>
            </View>
            <View className='share-info'>
              {
                !isGroupon && <Button className='sharebtn' openType='share'>
                  <Image className='sharebtn_image' src={ImgWeChat}></Image>
                  <View className='sharebtn_text'>分享给好友</View>
                </Button>
              }

              {
                !isGroupon && !canWrite && <Button className='savesharebtn' openType='openSetting'
                                                   onOpenSetting={handleSetting}>
                  <Image className='sharebtn_image' src={ImgFriend}></Image>
                  <View className='sharebtn_text'>发朋友圈</View>
                </Button>
              }
              {
                !isGroupon && canWrite && <Button className='savesharebtn' onClick={saveShare}>
                  <Image className='sharebtn_image' src={ImgFriend}></Image>
                  <View className='sharebtn_text'>发朋友圈</View>
                </Button>
              }
            </View>
          </View>
        </View>

        <View className='goods-info'>
          <View className='c'>
            <Text className='desc'>{goods.brief}</Text>
            <View className='price'>
              <View className='counterPrice'>原价：￥{goods.counterPrice}</View>
              <View className='retailPrice'>现价：￥{checkedSpecPrice}</View>
            </View>
            {
              brand.name && <View className='brand'>
                {/* TODO url 替换 */}
                <Navigator url='../brandDetail/brandDetail?id={brand.id}'>
                  <Text>{brand.name}</Text>
                </Navigator>
              </View>
            }

          </View>
        </View>
        <View className='section-nav section-attr' onClick={switchAttrPop}>
          <View className='t'>{checkedSpecText}</View>
          <AtIcon className='i' value='chevron-right' size='18' color='#666'/>
        </View>
        {
          comment && comment.count > 0 && <View className='comments'>
            <View className='h'>
              <Navigator url={`/pages/comment/comment?valueId=${goods.id}&type=0`}>
                <Text className='t'>评价({comment.count > 999 ? '999+' : comment.count})</Text>
                <View className='i'>
                  查看全部
                  {/*<van-icon name='arrow'/>*/}
                </View>
              </Navigator>
            </View>
            <View className='b'>
              {
                Array.isArray(comment.data) && comment.data.map(item => {
                  return <View className='item' key={item.id}>
                    <View className='info'>
                      <View className='user'>
                        <Image src={item.avatar}></Image>
                        <Text>{item.nickname}</Text>
                      </View>
                      <View className='time'>{item.addTime}</View>
                    </View>
                    <View className='content'>
                      {item.content}
                    </View>
                    {
                      item.picList.length > 0 && <View className='imgs'>
                        {
                          item.picList.map(pic => {
                            return <Image className='img' key={item.pic} src={pic}></Image>
                          })
                        }
                      </View>
                    }
                    {
                      item.adminContent && <View className='customer-service'>
                        <Text className='u'>商家回复：</Text>
                        <Text className='c'>{item.adminContent}</Text>
                      </View>
                    }
                  </View>
                })
              }
            </View>
          </View>
        }

        <View className='goods-attr'>
          <View className='t'>商品参数</View>
          <View className='l'>
            {
              Array.isArray(attribute) && attribute.map(item => {
                return <View className='item' key={item.name}>
                  <Text className='left'>{item.attribute}</Text>
                  <Text className='right'>{item.value}</Text>
                </View>
              })
            }

          </View>
        </View>

        <View className='detail'>
          {goods.detail && <RichText style={{fontSize: 0}} nodes={goods.detail2}/>}
        </View>

        <View className='common-problem'>
          <View className='h'>
            <View className='line' />
            <Text className='title'>常见问题</Text>
          </View>
          <View className='b'>
            {
              Array.isArray(issueList) && issueList.map(item => {
                return <View className='item' key={item.id}>
                  <View className='question-box'>
                    <Text className='spot'></Text>
                    <Text className='question'>{item.question}</Text>
                  </View>
                  <View className='answer'>
                    {item.answer}
                  </View>
                </View>
              })
            }
          </View>
        </View>

        {/* <!-- 大家都在看 --> */}
        {
          Array.isArray(relatedGoods) && relatedGoods.length > 0 && <View className='related-goods'>
            <View className='h'>
              <View className='line'></View>
              <Text className='title'>大家都在看</Text>
            </View>
            <View className='b'>
              {
                relatedGoods.map(item => {
                  return <View className='item' key={item.id}>
                    <Navigator url='/pages/goods/goods?id={item.id}'>
                      <Image className='img' src={item.picUrl} background-size='cover'></Image>
                      <Text className='name'>{item.name}</Text>
                      <Text className='price'>￥{item.retailPrice}</Text>
                    </Navigator>
                  </View>
                })
              }
            </View>
          </View>
        }
      </View>

      {/* <!-- 规格选择界面 --> */}

      <View className='attr-pop-box' style={{display: !openAttr ? 'none' : 'block'}}>
        <View className='attr-pop'>
          <View className='close' onClick={closeAttr}>
            <AtIcon className='icon' size='14' color='#666' value='close'/>
          </View>
          <View className='img-info'>
            <Image className='img' src={goods.picUrl}></Image>
            <View className='info'>
              <View className='c'>
                <View className='p'>价格：￥{checkedSpecPrice}</View>
                <View className='a'>{tmpSpecText}</View>
              </View>
            </View>
          </View>

          {/* <!-- 规格列表 --> */}
          <View className='spec-con'>
            {
              Array.isArray(specificationList) && specificationList.map(item => {
                return <View className='spec-item' key={item.name}>
                  <View className='name'>{item.name}</View>
                  <View className='values'>
                    {
                      item.valueList.map(vitem => {
                        return <View className={`value ${vitem.checked ? 'selected' : ''}`}
                                     onClick={() => clickSkuValue(vitem)}
                                     key={vitem.id}>{vitem.value}</View>
                      })
                    }
                  </View>
                </View>
              })
            }
            {
              groupon.length > 0 && <View className='spec-item'>
                <View className='name'>团购立减</View>
                <View className='values'>
                  {
                    groupon.map(vitem1 => {
                      return <View className={`value ${vitem1.checked ? 'selected' : ''}`}
                                   onClick={() => clickGroupon(vitem1)}
                                   key={vitem1.id}>￥{vitem1.discount} ({vitem1.discountMember}人)</View>
                    })
                  }

                </View>
              </View>
            }


            {/* <!-- 数量 --> */}
            <View className='number-item'>
              <View className='name'>数量</View>
              <View className='selnum'>
                <View className='cut' onClick={cutNumber}>-</View>
                <Input value={number} className='number' disabled type='number'/>
                <View className='add' onClick={addNumber}>+</View>
              </View>
            </View>
          </View>
        </View>
      </View>

      {/* <!-- 联系客服 TODO 禁用了 --> */}
      <View className='contact'>
        {/*<contact-button style='opacity:0;position:absolute;' type='default-dark'*/}
        {/*                session-from='weapp' size='27'>*/}
        {/*</contact-button>*/}
      </View>

      {/* <!-- 底部按钮 --> */}
      <View className='bottom-btn'>
        {
          !isGroupon && <View className='l l-collect' onClick={addCollectOrNot}>
            {
              collect ? <AtIcon className='icon' value='star-2' color='#ab956d' size={20}/> :
                <AtIcon className='icon' value='star' size={20}/>
            }
          </View>
        }
        {
          !isGroupon && <View className='l l-cart'>
            <View className='box'>
              <Text className='cart-count'>{cartGoodsCount}</Text>
              <AtIcon onClick={openCartPage} className='icon' value='shopping-cart' size={22}/>
            </View>
          </View>
        }
        {!soldout && !isGroupon && <View className='r' onClick={addToCart}>加入购物车</View>}
        {!soldout && <View className='c' onClick={addFast}>{isGroupon ? '参加团购' : '立即购买'}</View>}
        {soldout && <View className='n'>商品已售空</View>}
      </View>
    </Block>
  );
}

export default Index;
