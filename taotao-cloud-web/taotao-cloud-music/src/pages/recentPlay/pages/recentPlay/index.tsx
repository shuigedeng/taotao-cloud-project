import Taro, {useCallback, useEffect, useState} from "@tarojs/taro";
import {AtTabs, AtTabsPane} from "taro-ui";
import {View} from "@tarojs/components";
import api from "../../../../services/api";
import {useDispatch, useSelector} from "@tarojs/redux";
import classnames from "classnames";
import CLoading from "../../../../components/CLoading";
import CMusic from "../../../../components/CMusic";
import {updateCanplayList, updatePlayStatus, updateRecentTab} from "../../../../actions/song";
import "./index.less";
import {MusicItemType} from "../../../../store/state/songState";


type PageState = {
  tabList: Array<{
    title: string;
  }>;
  list: Array<{
    playCount: number;
    song: MusicItemType;
  }>;
  currentTab: number;
};

// @injectPlaySong()
const Page: Taro.FC = () => {
  const {song} = useSelector(({song}) => ({song: song}));
  const dispatch = useDispatch();
  const updateCanplayListDispatch = useCallback(
    (object) => dispatch(updateCanplayList(object)), [dispatch]
  )
  const updateRecentTabDispatch = useCallback(
    (object) => dispatch(updateRecentTab(object)), [dispatch]
  )
  const updatePlayStatusDispatch = useCallback(
    (object) => dispatch(updatePlayStatus(object)), [dispatch]
  )

  const [state, setState] = useState<PageState>({
    tabList: [
      {
        title: "最近7天"
      },
      {
        title: "全部"
      }
    ],
    list: [],
    currentTab: song.recentTab || 0
  })

  useEffect(() => {
    getData()
  }, [state]);

  const getData = () => {
    const {currentTab} = state;
    const userId = Taro.getStorageSync("userId");
    api.get("/user/record", {
      uid: userId,
      type: currentTab === 0 ? 1 : 0
    })
      .then(res => {
        const dataType = currentTab === 0 ? "weekData" : "allData";
        if (res.data && res.data[dataType] && res.data[dataType].length > 0) {
          setState(prevState => {
            return {...prevState, list: res.data[dataType]}
          });
        }
      });
  }

  const switchTab = (val) => {
    setState(prevState => {
      return {...prevState, currentTab: val, list: []}
    })
  }

  const playSong = (songId, canPlay) => {
    if (canPlay) {
      saveData(songId);
      Taro.navigateTo({
        url: `/pages/details/pages/songDetail/index?id=${songId}`
      });
    } else {
      Taro.showToast({
        title: "暂无版权",
        icon: "none"
      });
    }
  }

  const saveData = (songId) => {
    const {list, currentTab} = state;
    const tempList = list.map(item => {
      let temp: any = {};
      temp.name = item.song.name;
      temp.id = item.song.id;
      temp.ar = item.song.ar;
      temp.al = item.song.al;
      temp.copyright = item.song.copyright;
      temp.st = item.song.st;
      return temp;
    });
    const canPlayList = tempList.filter(item => {
      return item.st !== -200;
    });
    updateCanplayListDispatch({
      canPlayList,
      currentSongId: songId
    });
    updateRecentTabDispatch({
      recentTab: currentTab
    });
  }

  const showMore = () => {
    Taro.showToast({
      title: "暂未实现，敬请期待",
      icon: "none"
    });
  }

  const {list, currentTab, tabList} = state;
  const {currentSongInfo, isPlaying, canPlayList} = song;

  return (
    <View
      className={classnames({
        recentPlay_container: true,
        hasMusicBox: !!currentSongInfo.name
      })}
    >
      <CMusic
        songInfo={{
          currentSongInfo,
          isPlaying,
          canPlayList
        }}
        onUpdatePlayStatus={updatePlayStatusDispatch}
      />
      <AtTabs
        current={currentTab}
        swipeable={false}
        tabList={tabList}
        onClick={switchTab}
      >
        <AtTabsPane current={currentTab} index={0}>
          {list.length === 0 ? (
            <CLoading/>
          ) : (
            list.map(item => (
              <View key={item.song.id} className="recentPlay__music">
                <View
                  className="recentPlay__music__info"
                  onClick={() => {
                    playSong(item.song.id, item.song.st !== -200)
                  }}
                >
                  <View className="recentPlay__music__info__name">
                    {item.song.name}
                  </View>
                  <View className="recentPlay__music__info__desc">
                    {`${item.song.ar[0] ? item.song.ar[0].name : ""} - ${
                      item.song.al.name
                    }`}
                  </View>
                </View>
                <View
                  className="fa fa-ellipsis-v recentPlay__music__icon"
                  onClick={showMore}
                />
              </View>
            ))
          )}
        </AtTabsPane>
        <AtTabsPane current={currentTab} index={1}>
          {list.length === 0 ? (
            <CLoading/>
          ) : (
            list.map((item) => (
              <View key={item.song.id} className="recentPlay__music">
                <View
                  className="recentPlay__music__info"
                  onClick={() => {
                    playSong(item.song.id, item.song.st !== -200)
                  }}
                >
                  <View className="recentPlay__music__info__name">
                    {item.song.name}
                  </View>
                  <View className="recentPlay__music__info__desc">
                    {`${item.song.ar[0] ? item.song.ar[0].name : ""} - ${
                      item.song.al.name
                    }`}
                  </View>
                </View>
                <View
                  className="fa fa-ellipsis-v recentPlay__music__icon"
                  onClick={showMore}
                />
              </View>
            ))
          )}
        </AtTabsPane>
      </AtTabs>
    </View>
  );
}

Page.config = {
  navigationBarTitleText: "最近播放"
};

export default Page
