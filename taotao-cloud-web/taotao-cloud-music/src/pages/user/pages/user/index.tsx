import Taro, {useCallback, useDidShow, useState} from "@tarojs/taro";
import {AtButton, AtIcon, AtSearchBar, AtTabBar} from "taro-ui";
import classnames from "classnames";
import {Image, Text, View} from "@tarojs/components";
import {useDispatch, useSelector} from "@tarojs/redux";
import CLoading from "../../../../components/CLoading";
import api from "../../../../services/api";
import CMusic from "../../../../components/CMusic";
import {updatePlayStatus} from "../../../../actions/song";
import {formatCount} from "../../../../utils/common";
import "./index.less";

type ListItemInfo = {
  id: number;
  coverImgUrl: string;
  name: string;
  trackCount: number;
  playCount: number;
};


type PageState = {
  userInfo: {
    account: {
      id: number;
    };
    level: number;
    profile: {
      avatarUrl: string;
      backgroundUrl: string;
      nickname: string;
      eventCount: number;
      newFollows: number;
      followeds: number;
      userId: number;
    };
  };
  current: number;
  userCreateList: Array<ListItemInfo>;
  userCollectList: Array<ListItemInfo>;
  searchValue: string;
};


// @injectPlaySong()
const Page: Taro.FC = () => {
  const [state, setState] = useState<PageState>({
    userInfo: Taro.getStorageSync("userInfo"),
    current: 1,
    userCreateList: [],
    userCollectList: [],
    searchValue: ""
  })

  const {song} = useSelector(({song}) => ({song: song}));
  const dispatch = useDispatch();
  const updatePlayStatusDispatch = useCallback(
    (object) => dispatch(updatePlayStatus(object)), [dispatch]
  )

  useDidShow(() => {
    if (state.userInfo) {
      getSubcount()
      getUserDetail()
      getPlayList()
    }
  })

  const getSubcount = () => {
    api.get("/user/subcount").then(res => {
      console.log("res", res);
    });
  }

  const getUserDetail = () => {
    const {userId} = state.userInfo.profile;
    api.get("/user/detail", {
      uid: userId
    })
      .then(res => {
        res.data;
        setState(prevState => {
          return {...prevState, userInfo: res.data}
        });
      });
  }

  const getPlayList = () => {
    const {userId} = state.userInfo.profile;
    api.get("/user/playlist", {
      uid: userId,
      limit: 300
    }).then(res => {
      if (res.data.playlist && res.data.playlist.length > 0) {
        setState(prevState => {
          return {
            ...prevState,
            userCreateList: res.data.playlist.filter(
              item => item.userId === userId
            ),
            userCollectList: res.data.playlist.filter(
              item => item.userId !== userId
            )
          }
        });
      }
    });
  }

  const switchTab = (value) => {
    if (value !== 0) {
      return;
    }
    Taro.reLaunch({
      url: "/pages/index/index"
    });
  }

  const showToast = () => {
    Taro.showToast({
      title: "暂未实现，敬请期待",
      icon: "none"
    });
  }

  const goUserDetail = () => {
    return;
    // const { userId } = this.state.userInfo.profile
    // Taro.navigateTo({
    //   url: `/pages/user/index?id=${userId}`
    // })
  }

  const goSearch = () => {
    Taro.navigateTo({
      url: `/pages/search/pages/search/index`
    });
  }

  const jumpEventPage = () => {
    const {userId} = state.userInfo.profile;
    Taro.navigateTo({
      url: `/pages/user/pages/userEvents/index?uid=${userId}`
    });
  }

  const signOut = () => {
    Taro.clearStorage();
    api.get("/logout").then(res => {
      console.log("退出登陆", res);
    });
    Taro.redirectTo({
      url: "/pages/login/index"
    });
  }

  const goDetail = (item) => {
    Taro.navigateTo({
      url: `/pages/details/pages/playListDetail/index?id=${item.id}&name=${item.name}`
    });
  }

  const {
    userInfo,
    userCreateList,
    userCollectList,
    searchValue
  } = state;
  const {currentSongInfo, isPlaying, canPlayList} = song;

  return (
    <View
      className={classnames({
        my_container: true,
        hasMusicBox: !!currentSongInfo.name
      })}
    >
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
          value={searchValue}
          onChange={goSearch}
        />
      </View>
      <View className="header">
        <View className="header__left" onClick={goUserDetail}>
          <Image
            src={`${userInfo.profile.avatarUrl}?imageView&thumbnail=250x0`}
            className="header__img"
          />
          <View className="header__info">
            <View className="header__info__name">
              {userInfo.profile.nickname}
            </View>
            <View>
              <Text className="header__info__level">LV.{userInfo.level}</Text>
            </View>
          </View>
        </View>
        {state.userInfo ? <AtIcon
          prefixClass="fa"
          value="sign-out"
          size="30"
          color="#d43c33"
          className="exit_icon"
          onClick={signOut}
        /> : <AtButton className="exit_icon" size="small" circle onClick={() => {
          Taro.navigateTo({
            url: "/pages/login/index"
          })
        }}>
          亲 请登录
        </AtButton>}

      </View>
      <View className="user_count">
        <View
          className="user_count__sub"
          onClick={jumpEventPage}
        >
          <View className="user_count__sub--num">
            {userInfo.profile.eventCount || 0}
          </View>
          <View>动态</View>
        </View>
        <View
          className="user_count__sub"
          onClick={() => {
            Taro.navigateTo({
              url: `/pages/user/pages/userFocus/index`
            });
          }}
        >
          <View className="user_count__sub--num">
            {userInfo.profile.newFollows || 0}
          </View>
          <View>关注</View>
        </View>
        <View
          className="user_count__sub"
          onClick={() => {
            Taro.navigateTo({
              url: `/pages/user/pages/userFans/index`
            });
          }}
        >
          <View className="user_count__sub--num">
            {userInfo.profile.followeds || 0}
          </View>
          <View>粉丝</View>
        </View>
      </View>
      <View className="user_brief">
        <View className="user_brief__item">
          <Image
            className="user_brief__item__img"
            src={require("../../../../assets/images/my/recent_play.png")}
          />
          <View
            className="user_brief__item__text"
            onClick={() => {
              Taro.navigateTo({
                url: `/pages/recentPlay/pages/recentPlay/index`
              });
            }}
          >
            <Text>最近播放</Text>
            <Text className="at-icon at-icon-chevron-right"/>
          </View>
        </View>
        <View className="user_brief__item">
          <Image
            className="user_brief__item__img"
            src={require("../../../../assets/images/my/my_radio.png")}
          />
          <View
            className="user_brief__item__text"
            onClick={showToast}
          >
            <Text>我的电台</Text>
            <Text className="at-icon at-icon-chevron-right"/>
          </View>
        </View>
        <View className="user_brief__item">
          <Image
            className="user_brief__item__img"
            src={require("../../../../assets/images/my/my_collection_icon.png")}
          />
          <View
            className="user_brief__item__text"
            onClick={showToast}
          >
            <Text>我的收藏</Text>
            <Text className="at-icon at-icon-chevron-right"/>
          </View>
        </View>
      </View>
      <View className="user_playlist">
        <View className="user_playlist__title">
          我创建的歌单
          <Text className="user_playlist__title__desc">
            ({userCreateList.length})
          </Text>
        </View>
        {userCreateList.length === 0 ? <CLoading/> : ""}
        <View>
          {userCreateList.map(item => (
            <View
              key={item.id}
              className="user_playlist__item"
              onClick={() => {
                goDetail(item)
              }}
            >
              <Image
                className="user_playlist__item__cover"
                src={`${item.coverImgUrl}?imageView&thumbnail=250x0`}
              />
              <View className="user_playlist__item__info">
                <View className="user_playlist__item__info__name">
                  {item.name}
                </View>
                <View className="user_playlist__item__info__count">
                  {item.trackCount}首, 播放{formatCount(item.playCount)}次
                </View>
              </View>
            </View>
          ))}
        </View>
      </View>
      <View className="user_playlist">
        <View className="user_playlist__title">
          我收藏的歌单
          <Text className="user_playlist__title__desc">
            ({userCollectList.length})
          </Text>
        </View>
        {userCollectList.length === 0 ? <CLoading/> : ""}
        <View>
          {userCollectList.map(item => (
            <View
              key={item.id}
              className="user_playlist__item"
              onClick={() => {
                goDetail(item)
              }}
            >
              <Image
                className="user_playlist__item__cover"
                src={`${item.coverImgUrl}?imageView&thumbnail=250x0`}
              />
              <View className="user_playlist__item__info">
                <View className="user_playlist__item__info__name">
                  {item.name}
                </View>
                <View className="user_playlist__item__info__count">
                  {item.trackCount}首, 播放{formatCount(item.playCount)}次
                </View>
              </View>
            </View>
          ))}
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

Page.config = {
  navigationBarTitleText: "我的"
};

export default Page
