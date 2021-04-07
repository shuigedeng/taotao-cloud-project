import Taro, {useCallback, useEffect, useRouter} from "@tarojs/taro";
import {Image, Text, View} from "@tarojs/components";
import classnames from "classnames";
import {useDispatch, useSelector} from "@tarojs/redux";
import CLoading from "../../../../components/CLoading";
import CMusic from "../../../../components/CMusic";
import {getPlayListDetail, updatePlayStatus} from "../../../../actions/song";
import {formatCount} from "../../../../utils/common";
import "./index.less";

// @injectPlaySong()
const Page: Taro.FC = () => {
  const router = useRouter();
  const {song} = useSelector(({song}) => ({song: song}));
  const dispatch = useDispatch();
  const getPlayListDetailDispatch = useCallback(
    (payload) => dispatch(getPlayListDetail(payload)), [dispatch]
  )
  const updatePlayStatusDispatch = useCallback(
    (object) => dispatch(updatePlayStatus(object)), [dispatch]
  )

  useEffect(() => {
    const init = () => {
      const {id, name} = router.params;
      Taro.setNavigationBarTitle({
        title: name
      });
      getPlayListDetailDispatch({
        id
      });
    }
    init()
  }, [router]);


  const playSong = (songId, playStatus) => {
    if (playStatus === 0) {
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

  const {
    playListDetailInfo,
    playListDetailPrivileges,
    currentSongInfo,
    isPlaying,
    canPlayList
  } = song;

  return (
    <View
      className={classnames({
        playList_container: true,
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
      <View className="playList__header">
        <Image
          className="playList__header__bg"
          src={playListDetailInfo.coverImgUrl}
        />
        <View className="playList__header__cover">
          <Image
            className="playList__header__cover__img"
            src={playListDetailInfo.coverImgUrl}
          />
          <Text className="playList__header__cover__desc">歌单</Text>
          <View className="playList__header__cover__num">
            <Text className="at-icon at-icon-sound"/>
            {formatCount(playListDetailInfo.playCount)}
          </View>
        </View>
        <View className="playList__header__info">
          <View className="playList__header__info__title">
            {playListDetailInfo.name}
          </View>
          <View className="playList__header__info__user">
            <Image
              className="playList__header__info__user_avatar"
              src={playListDetailInfo.creator.avatarUrl}
            />
            {playListDetailInfo.creator.nickname}
          </View>
        </View>
      </View>
      <View className="playList__header--more">
        <View className="playList__header--more__tag">
          标签：
          {playListDetailInfo.tags.map(tag => (
            <Text key={tag} className="playList__header--more__tag__item">
              {tag}
            </Text>
          ))}
          {playListDetailInfo.tags.length === 0 ? "暂无" : ""}
        </View>
        <View className="playList__header--more__desc">
          简介：{playListDetailInfo.description || "暂无"}
        </View>
      </View>
      <View className="playList__content">
        <View className="playList__content__title">歌曲列表</View>
        {playListDetailInfo.tracks.length === 0 ? <CLoading/> : ""}
        <View className="playList__content__list">
          {playListDetailInfo.tracks.map((track, index) => (
            <View
              className={classnames({
                playList__content__list__item: true,
                disabled: playListDetailPrivileges[index].st === -200
              })}
              key={track.id}
              onClick={() => {
                playSong(track.id, playListDetailPrivileges[index].st)
              }}
            >
              <Text className="playList__content__list__item__index">
                {index + 1}
              </Text>
              <View className="playList__content__list__item__info">
                <View>
                  <View className="playList__content__list__item__info__name">
                    {track.name}
                  </View>
                  <View className="playList__content__list__item__info__desc">
                    {track.ar[0] ? track.ar[0].name : ""} - {track.al.name}
                  </View>
                </View>
                <Text className="at-icon at-icon-chevron-right"/>
              </View>
            </View>
          ))}
        </View>
      </View>
    </View>
  );
}

Page.config = {
  navigationBarTitleText: "歌单详情"
};

export default Page
