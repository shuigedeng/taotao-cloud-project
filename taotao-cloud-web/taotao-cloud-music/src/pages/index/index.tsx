import Taro from "@tarojs/taro";
import {Image, Swiper, SwiperItem, Text, View} from "@tarojs/components";
import {AtIcon, AtSearchBar, AtTabBar} from "taro-ui";
import {useDispatch, useSelector} from "@tarojs/redux";
import classnames from "classnames";

import CLoading from "../../components/CLoading";
import CMusic from "../../components/CMusic";
import api from "../../services/api";

import {
  getRecommend,
  getRecommendDj,
  getRecommendNewSong,
  getRecommendPlayList,
  updatePlayStatus
} from "../../actions/song";

import "./index.less";
import useState = Taro.useState;
import useEffect = Taro.useEffect;
import useCallback = Taro.useCallback;

type PageStateProps = {}

type PageOwnProps = {};

type PageState = {
  current: number;
  showLoading: boolean;
  bannerList: Array<{
    typeTitle: string;
    pic: string;
    targetId: number;
  }>;
  searchValue: string;
};

type IProps = PageStateProps & PageOwnProps;

// @injectPlaySong()
const Index: Taro.FC<IProps> = () => {
  const {
    song,
    recommendPlayList,
    recommendDj,
  } = useSelector(({song}) => ({
    song: song,
    recommendPlayList: song.recommendPlayList,
    recommendDj: song.recommendDj,
    recommendNewSong: song.recommendNewSong,
    recommend: song.recommend
  }));

  const dispatch = useDispatch();
  const getRecommendPlayListDispatch = useCallback(
    () => dispatch(getRecommendPlayList()), [dispatch]
  )
  const getRecommendDjDispatch = useCallback(
    () => dispatch(getRecommendDj()), [dispatch]
  )
  const getRecommendNewSongDispatch = useCallback(
    () => dispatch(getRecommendNewSong()), [dispatch]
  )
  const getRecommendDispatch = useCallback(
    () => dispatch(getRecommend()), [dispatch]
  )
  const updatePlayStatusDispatch = useCallback(
    (object) => dispatch(updatePlayStatus(object)), [dispatch]
  )

  const [state, setState] = useState<PageState>({
    current: 0,
    showLoading: true,
    bannerList: [],
    searchValue: ""
  })

  const getBanner = () => {
    api.get("/banner", {type: 2})
      .then(({data}) => {
        if (data.banners) {
          setState(preState => {
            return {...preState, bannerList: data.banners}
          })
        }
      });
  }

  useEffect(() => {
    getRecommendPlayListDispatch()
    getRecommendNewSongDispatch()
    getRecommendDjDispatch()
    getRecommendDispatch()
    getBanner()
  }, [dispatch])

  useEffect(() => {
    removeLoading();
  }, [recommendPlayList, recommendDj])

  const switchTab = (value) => {
    if (value == 1) {
      Taro.reLaunch({
        url: "/pages/user/pages/user/index"
      });
      return;
    }
    if (value == 0) {
      Taro.reLaunch({
        url: "/pages/index/index"
      });
      return;
    }

    Taro.showToast({
      title: "亲 正在开发中，敬请期待",
      icon: "none"
    });
  }

  const goSearch = () => {
    Taro.navigateTo({
      url: `/pages/search/pages/search/index`
    });
  }

  const goDetail = (item) => {
    Taro.navigateTo({
      url: `/pages/details/pages/playListDetail/index?id=${item.id}&name=${item.name}`
    });
  }

  const goPage = (pageName: string) => {
    console.log(pageName)
    Taro.showToast({
      title: "亲 正在开发中，敬请期待",
      icon: "none"
    });
  }

  // const goDjDetail = (item) => {
  //   Taro.navigateTo({
  //     url: `/pages/djprogramListDetail/index?id=${item.id}&name=${item.name}`
  //   });
  // }

  const removeLoading = () => {
    if (recommendPlayList.length || recommendDj.length) {
      setState(preState => {
        return {...preState, showLoading: false}
      });
    }
  }

  const {currentSongInfo, isPlaying, canPlayList} = song;

  const blocks: Array<{ id: number, icon: string, name: string, page: string }> = [
    {id: 1, icon: "calendar-minus-o", name: "个性推荐", page: "dailyRecommend"},
    {id: 2, icon: "bar-chart", name: "排行榜", page: "rank"},
    {id: 3, icon: "music", name: "歌单", page: "rank"},
    {id: 4, icon: "ellipsis-v", name: "主播电台", page: "rank"},
    {id: 5, icon: "thumbs-o-up", name: "最新音乐", page: "rank"},
  ]

  return (
    <View
      className={classnames({
        index_container: true,
        hasMusicBox: !!currentSongInfo.name
      })}
    >
      <CLoading fullPage={true} hide={!state.showLoading}/>
      <CMusic
        songInfo={{
          currentSongInfo,
          isPlaying,
          canPlayList
        }}
        isHome={true}
        onUpdatePlayStatus={updatePlayStatusDispatch}
      />
      <View onClick={goSearch}>
        <AtSearchBar
          actionName="搜一下"
          disabled={true}
          value={state.searchValue}
          onChange={goSearch}
        />
      </View>
      <Swiper
        className="banner_list"
        indicatorColor="#999"
        indicatorActiveColor="#d43c33"
        circular
        indicatorDots
        autoplay
      >
        {state.bannerList.map(item => (
          <SwiperItem key={item.targetId} className="banner_list__item">
            <Image src={item.pic} className="banner_list__item__img"/>
          </SwiperItem>
        ))}
      </Swiper>
      <View className="handle_list">
        {blocks.map(item => (
          <View
            key={item.id}
            className="handle_list__item"
            onClick={() => {
              goPage(item.page)
            }}
          >
            <View className="handle_list__item__icon-wrap">
              <AtIcon
                prefixClass="fa"
                value={item.icon}
                size="25"
                color="#ffffff"
                className="handle_list_item__icon"
              />
            </View>
            <Text className="handle_list__item__text">{item.name}</Text>
          </View>
        ))}
      </View>
      <View className="recommend_playlist">
        <View className="recommend_playlist__title">推荐歌单</View>
        <View className="recommend_playlist__content">
          {recommendPlayList.map(item => (
            <View
              key={item.id}
              className="recommend_playlist__item"
              onClick={() => {
                goDetail(item)
              }}
            >
              <Image
                src={`${item.picUrl}?imageView&thumbnail=250x0`}
                className="recommend_playlist__item__cover"
              />
              <View className="recommend_playlist__item__cover__num">
                <Text className="at-icon at-icon-sound"/>
                {item.playCount < 10000
                  ? item.playCount
                  : `${Number(item.playCount / 10000).toFixed(0)}万`}
              </View>
              <View className="recommend_playlist__item__title">
                {item.name}
              </View>
            </View>
          ))}
        </View>
      </View>
      <View className='recommend_playlist'>
        <View className='recommend_playlist__title'>
          推荐电台
        </View>
        <View className='recommend_playlist__content'>
          {
            recommendDj.map((item, index) =>
              <View key={index}
                    className='recommend_playlist__item'
                    onClick={() => {
                      goDetail(item)
                    }}>
                <Image
                  src={`${item.picUrl}?imageView&thumbnail=250x0`}
                  className='recommend_playlist__item__cover'
                />
                <View className='recommend_playlist__item__title'>{item.name}</View>
              </View>)
          }
        </View>
      </View>
      <AtTabBar
        fixed
        selectedColor="#d43c33"
        tabList={[
          {title: "发现", iconPrefixClass: "fa", iconType: "feed"},
          {title: "我的", iconPrefixClass: "fa", iconType: "music"},
          {title: "朋友", iconPrefixClass: "fa", iconType: "music"},
          {title: "视频", iconPrefixClass: "fa", iconType: "music"}
        ]}
        onClick={switchTab}
        current={state.current}
      />
    </View>
  );
}

Index.config = {
  navigationStyle: "default",
  navigationBarBackgroundColor: "#F0F1F2",
  navigationBarTextStyle: "black",
  navigationBarTitleText: "滔滔云音乐",
  backgroundColor: "#F0F1F2",
  backgroundColorBottom: "#F0F1F2"
}

export default Index;
