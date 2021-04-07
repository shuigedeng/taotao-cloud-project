import Taro, {useCallback, useEffect, useRouter, useState} from "@tarojs/taro";
import {Image, ScrollView, Text, View} from "@tarojs/components";
import {AtIcon, AtSearchBar, AtTabs, AtTabsPane} from "taro-ui";
import classnames from "classnames";
import CLoading from "../../../../components/CLoading";
import {useDispatch, useSelector} from "@tarojs/redux";
import CMusic from "../../../../components/CMusic";
import CWhiteSpace from "../../../../components/CWhiteSpace";
import {updatePlayStatus} from "../../../../actions/song";
import {formatCount, formatNumber, formatTimeStampToTime, setKeywordInHistory} from "../../../../utils/common";
import api from "../../../../services/api";
import "./index.less";

type PageState = {
  keywords: string;
  activeTab: number;
  totalInfo: {
    loading: boolean;
    noData: boolean;
    songInfo: {
      // 单曲
      songs: Array<{
        id: number;
        name: string;
        al: {
          id: number;
          name: string;
        };
        ar: Array<{
          name: string;
        }>;
      }>;
      more: boolean;
      moreText?: string;
    };
    videoInfo: {
      // 视频
      videos: Array<{
        title: string;
        vid: string;
        coverUrl: string;
        creator: Array<{
          userName: string;
        }>;
        durationms: number;
        playTime: number;
      }>;
      more: boolean;
      moreText: string;
    };
    userListInfo: {
      // 用户
      users: Array<{
        nickname: string;
        userId: number;
        avatarUrl: string;
        gender: number;
        signature: string;
      }>;
      more: boolean;
      moreText: string;
    };
    djRadioInfo: {
      // 电台
      djRadios: Array<{
        name: string;
        id: number;
        picUrl: string;
        desc: string;
      }>;
      more: boolean;
      moreText: string;
    };
    playListInfo: {
      // 歌单
      playLists: Array<{
        name: string;
        id: number;
        coverImgUrl: string;
        trackCount: number;
        playCount: number;
        creator: {
          nickname: string;
        };
      }>;
      more: boolean;
      moreText?: string;
    };
    albumInfo: {
      // 专辑
      albums: Array<{
        name: string;
        id: number;
        publishTime: number;
        picUrl: string;
        artist: {
          name: string;
        };
        containedSong: string;
      }>;
      more: boolean;
      moreText: string;
    };
    artistInfo: {
      // 歌手
      artists: Array<{
        name: string;
        id: number;
        picUrl: string;
        alias: Array<string>;
      }>;
      more: boolean;
      moreText: string;
    };
    sim_query: {
      sim_querys: Array<{
        keyword: string;
      }>;
      more: boolean;
    };
  };
  tabList: Array<{
    title: string;
  }>;
  albumInfo: {
    // 专辑
    albums: Array<{
      name: string;
      id: number;
      publishTime: number;
      picUrl: string;
      artist: {
        name: string;
      };
      containedSong: string;
    }>;
    more: boolean;
  };
  artistInfo: {
    // 歌手
    artists: Array<{
      name: string;
      id: number;
      picUrl: string;
      alias: Array<string>;
    }>;
    more: boolean;
  };
  djRadioInfo: {
    // 电台
    djRadios: Array<{
      name: string;
      id: number;
      picUrl: string;
      desc: string;
    }>;
    more: boolean;
  };
  playListInfo: {
    // 歌单
    playLists: Array<{
      name: string;
      id: number;
      coverImgUrl: string;
      trackCount: number;
      playCount: number;
      creator: {
        nickname: string;
      };
    }>;
    more: boolean;
    moreText?: string;
  };
  videoInfo: {
    // 视频
    videos: Array<{
      title: string;
      vid: string;
      coverUrl: string;
      creator: Array<{
        userName: string;
      }>;
      durationms: number;
      playTime: number;
    }>;
    more: boolean;
  };
  mvInfo: {
    // 视频
    mvs: Array<{
      name: string;
      id: string;
      cover: string;
      artists: Array<{
        name: string;
      }>;
      duration: number;
      playCount: number;
    }>;
    more: boolean;
  };
  userListInfo: {
    // 用户
    users: Array<{
      nickname: string;
      userId: number;
      avatarUrl: string;
      gender: number;
      signature: string;
    }>;
    more: boolean;
  };
  songInfo: {
    // 单曲
    songs: Array<{
      id: number;
      name: string;
      al: {
        id: number;
        name: string;
      };
      ar: Array<{
        name: string;
      }>;
    }>;
    more: boolean;
  };
  sim_query: Array<{
    keyword: string;
  }>;
};

// @injectPlaySong()
const Page: Taro.FC = () => {
  const router = useRouter()
  const {song} = useSelector(({song}) => ({song: song}));
  const dispatch = useDispatch();
  // const updateCanplayListDispatch = useCallback(
  //   (object) => dispatch(updateCanplayList(object)), [dispatch]
  // )
  // const getSongInfoDispatch = useCallback(
  //   (object) => dispatch(getSongInfo(object)), [dispatch]
  // )
  const updatePlayStatusDispatch = useCallback(
    (object) => dispatch(updatePlayStatus(object)), [dispatch]
  )

  const [state, setState] = useState<PageState>({
    // keywords: '海阔天空',
    keywords: router.params.keywords,
    activeTab: 0,
    totalInfo: {
      loading: true,
      noData: false,
      userListInfo: {
        users: [],
        more: false,
        moreText: ""
      },
      videoInfo: {
        videos: [],
        more: false,
        moreText: ""
      },
      playListInfo: {
        playLists: [],
        more: false,
        moreText: ""
      },
      songInfo: {
        songs: [],
        more: false,
        moreText: ""
      },
      albumInfo: {
        albums: [],
        more: false,
        moreText: ""
      },
      djRadioInfo: {
        djRadios: [],
        more: false,
        moreText: ""
      },
      artistInfo: {
        artists: [],
        more: false,
        moreText: ""
      },
      sim_query: {
        sim_querys: [],
        more: false
      }
    },
    tabList: [
      {
        title: "综合"
      },
      {
        title: "单曲"
      },
      {
        title: "歌单"
      },
      {
        title: "视频"
      },
      {
        title: "歌手"
      },
      {
        title: "专辑"
      },
      {
        title: "主播电台"
      },
      {
        title: "用户"
      },
      {
        title: "MV"
      }
    ],
    userListInfo: {
      users: [],
      more: true
    },
    videoInfo: {
      videos: [],
      more: true
    },
    mvInfo: {
      mvs: [],
      more: true
    },
    playListInfo: {
      playLists: [],
      more: true,
      moreText: ""
    },
    songInfo: {
      songs: [],
      more: true
    },
    albumInfo: {
      albums: [],
      more: true
    },
    djRadioInfo: {
      djRadios: [],
      more: true
    },
    artistInfo: {
      artists: [],
      more: true
    },
    sim_query: []
  })

  const {keywords} = state;
  useEffect(() => {
    Taro.setNavigationBarTitle({
      title: `${keywords}的搜索结果`
    });
    getResult();
  }, [keywords]);

  const {totalInfo} = state
  console.log(totalInfo)
  // useEffect(() => {
  //   switchTab(0);
  //   resetInfo();
  // }, [totalInfo]);

  const getResult = () => {
    const {keywords, totalInfo} = state;
    Taro.setNavigationBarTitle({
      title: `${keywords}的搜索结果`
    });
    setState(prevState => {
      return {...prevState, totalInfo: Object.assign(totalInfo, {loading: true})}
    })
    api.get("/search", {
      keywords,
      type: 1018
    }).then(res => {
      if (!res.data || !res.data.result) {
        setState(prevState => {
          return {
            ...prevState, totalInfo: Object.assign(prevState.totalInfo, {
              loading: false,
              noData: true
            })
          }
        })
        return;
      }
      const result = res.data.result;
      if (result) {
        setState(prevState => {
          return {
            ...prevState, totalInfo: {
              loading: false,
              noData:
                !result.album &&
                !result.artist &&
                !result.djRadio &&
                !result.playList &&
                !result.song &&
                !result.user &&
                !result.video &&
                !result.sim_query,
              albumInfo: result.album || {
                albums: []
              },
              artistInfo: result.artist || {
                artists: []
              },
              djRadioInfo: result.djRadio || {
                djRadios: []
              },
              playListInfo: result.playList || {
                playLists: []
              },
              songInfo: result.song || {
                songs: []
              },
              userListInfo: result.user || {
                users: []
              },
              videoInfo: result.video || {
                videos: []
              },
              sim_query: result.sim_query || {
                sim_querys: []
              }
            }
          }
        })
        // this.props.updateCanplayList({
        //   canPlayList: res.data.result.songs
        // })
      }
    });
  }

  const playSong = (songId) => {
    api.get("/check/music", {id: songId})
      .then(res => {
        if (res.data.success) {
          Taro.navigateTo({
            url: `/pages/details/pages/songDetail/index?id=${songId}`
          });
        } else {
          Taro.showToast({
            title: res.data.message,
            icon: "none"
          });
        }
      });
  }

  const goVideoDetail = (id, type) => {
    let apiUrl = "/video/url";
    if (type === "mv") {
      apiUrl = "/mv/url";
    }
    api.get(apiUrl, {id})
      .then(({data}) => {
        if (
          (type === "video" && data.urls && data.urls.length) ||
          (type === "mv" && data.data.url)
        ) {
          Taro.navigateTo({
            url: `/pages/details/pages/videoDetail/index?id=${id}&type=${type}`
          });
        } else {
          Taro.showToast({
            title: `该${type === "mv" ? "mv" : "视频"}暂无版权播放`,
            icon: "none"
          });
        }
      });
  }

  // 获取单曲列表
  const getSongList = () => {
    const {keywords, songInfo} = state;
    if (!songInfo.more) {
      return;
    }
    api.get("/search", {
      keywords,
      type: 1,
      limit: 30,
      offset: songInfo.songs.length
    }).then(({data}) => {
      if (data.result && data.result.songs) {
        let tempSongList = data.result.songs.map(item => {
          item.al = item.album;
          item.ar = item.artists;
          return item;
        });
        setState(prevState => {
          return {
            ...prevState, songInfo: {
              songs: songInfo.songs.concat(tempSongList),
              more:
                songInfo.songs.concat(data.result.songs).length <
                data.result.songCount
            }
          }
        })
      }
    });
  }

  // 获取歌单列表
  const getPlayList = () => {
    const {keywords, playListInfo} = state;
    if (!playListInfo.more) {
      return;
    }
    api.get("/search", {
      keywords,
      type: 1000,
      limit: 30,
      offset: playListInfo.playLists.length
    })
      .then(({data}) => {
        if (data.result && data.result.playlists) {
          setState(prevState => {
            return {
              ...prevState, playListInfo: {
                playLists: playListInfo.playLists.concat(data.result.playlists),
                more:
                  playListInfo.playLists.concat(data.result.playlists).length <
                  data.result.playlistCount
              }
            }
          })
        }
      });
  }

  // 获取视频列表
  const getVideoList = () => {
    const {keywords, videoInfo} = state;
    if (!videoInfo.more) {
      return;
    }
    api.get("/search", {
      keywords,
      type: 1014,
      limit: 30,
      offset: videoInfo.videos.length
    })
      .then(({data}) => {
        console.log("getVideoList=>data", data);
        if (data.result && data.result.videos) {
          setState(prevState => {
            return {
              ...prevState, videoInfo: {
                videos: videoInfo.videos.concat(data.result.videos),
                more:
                  videoInfo.videos.concat(data.result.videos).length <
                  data.result.videoCount
              }
            }
          })
        }
      });
  }

  // 获取mv列表
  const getMvList = () => {
    const {keywords, mvInfo} = state;
    if (!mvInfo.more) {
      return;
    }
    api
      .get("/search", {
        keywords,
        type: 1004,
        limit: 30,
        offset: mvInfo.mvs.length
      })
      .then(({data}) => {
        console.log("getMvList=>data", data);
        if (data.result && data.result.mvs) {
          setState(prevState => {
            return {
              ...prevState, mvInfo: {
                mvs: mvInfo.mvs.concat(data.result.mvs),
                more:
                  mvInfo.mvs.concat(data.result.mvs).length < data.result.mvCount
              }
            }
          })
        }
      });
  }

  // 获取歌手列表
  const getArtistList = () => {
    const {keywords, artistInfo} = state;
    if (!artistInfo.more) {
      return;
    }
    api.get("/search", {
      keywords,
      type: 100,
      limit: 30,
      offset: artistInfo.artists.length
    })
      .then(({data}) => {
        console.log("getArtistList=>data", data);
        if (data.result && data.result.artists) {
          setState(prevState => {
            return {
              ...prevState, artistInfo: {
                artists: artistInfo.artists.concat(data.result.artists),
                more:
                  artistInfo.artists.concat(data.result.artists).length <
                  data.result.artistCount
              }
            }
          })
        }
      });
  }

  // 获取用户列表
  const getUserList = () => {
    const {keywords, userListInfo} = state;
    if (!userListInfo.more) {
      return;
    }
    api
      .get("/search", {
        keywords,
        type: 1002,
        limit: 30,
        offset: userListInfo.users.length
      })
      .then(({data}) => {
        console.log("getUserList=>data", data);
        if (data.result && data.result.userprofiles) {
          setState(prevState => {
            return {
              ...prevState, userListInfo: {
                users: userListInfo.users.concat(data.result.userprofiles),
                more:
                  userListInfo.users.concat(data.result.userprofiles).length <
                  data.result.userprofileCount
              }
            }
          })
        }
      });
  }

  // 获取专辑列表
  const getAlbumList = () => {
    const {keywords, albumInfo} = state;
    if (!albumInfo.more) {
      return;
    }
    api
      .get("/search", {
        keywords,
        type: 10,
        limit: 30,
        offset: albumInfo.albums.length
      })
      .then(({data}) => {
        console.log("getUserList=>data", data);
        if (data.result && data.result.albums) {
          setState(prevState => {
            return {
              ...prevState, albumInfo: {
                albums: albumInfo.albums.concat(data.result.albums),
                more:
                  albumInfo.albums.concat(data.result.albums).length <
                  data.result.albumCount
              }
            }
          })
        }
      });
  }

  // 获取电台列表
  const getDjRadioList = () => {
    const {keywords, djRadioInfo} = state;
    if (!djRadioInfo.more) {
      return;
    }
    api.get("/search", {
        keywords,
        type: 1009,
        limit: 30,
        offset: djRadioInfo.djRadios.length
      })
      .then(({data}) => {
        console.log("getUserList=>data", data);
        if (data.result && data.result.djRadios) {
          setState(prevState => {
            return {
              ...prevState, djRadioInfo: {
                djRadios: djRadioInfo.djRadios.concat(data.result.djRadios),
                more:
                  djRadioInfo.djRadios.concat(data.result.djRadios).length <
                  data.result.djRadiosCount
              }
            }
          })
        }
      });
  }

  const goPlayListDetail = (item) => {
    Taro.navigateTo({
      url: `/pages/details/pages/playListDetail/index?id=${item.id}&name=${item.name}`
    });
  }

  const showMore = () => {
    Taro.showToast({
      title: "暂未实现，敬请期待",
      icon: "none"
    });
  }

  const searchTextChange = (val) => {
    setState(prevState => {
      return {...prevState, keywords: val}
    })
  }

  const resetInfo = () => {
    setState(prevState => {
      return {
        ...prevState, userListInfo: {
          users: [],
          more: true
        },
        videoInfo: {
          videos: [],
          more: true
        },
        playListInfo: {
          playLists: [],
          more: true,
          moreText: ""
        },
        songInfo: {
          songs: [],
          more: true
        },
        albumInfo: {
          albums: [],
          more: true
        },
        djRadioInfo: {
          djRadios: [],
          more: true
        },
        artistInfo: {
          artists: [],
          more: true
        }
      }
    })
  }

  const searchResult = () => {
    setKeywordInHistory(state.keywords);
    setState(prevState => {
      return {
        ...prevState, totalInfo: Object.assign(prevState.totalInfo, {
          loading: true
        })
      }
    })
  }

  const queryResultBySim = (keyword) => {
    setKeywordInHistory(keyword);
    setState(prevState => {
      return {...prevState, keywords: keyword}
    })
  }

  const showTip = () => {
    Taro.showToast({
      title: "正在开发，敬请期待",
      icon: "none"
    });
  }

  const switchTab = (activeTab) => {
    console.log("activeTab", activeTab);
    switch (activeTab) {
      case 0:
        getResult();
        break;
      case 1:
        getSongList();
        break;
      case 2:
        getPlayList();
        break;
      case 3:
        getVideoList();
        break;
      case 4:
        getArtistList();
        break;
      case 5:
        getAlbumList();
        break;
      case 6:
        getDjRadioList();
        break;
      case 7:
        getUserList();
        break;
      case 8:
        getMvList();
        break;
    }
    setState(prevState => {
      return {...prevState, activeTab: activeTab}
    })
  }

  const formatDuration = (ms: number) => {
    // @ts-ignore
    let minutes: string = formatNumber(parseInt(ms / 60000));
    // @ts-ignore
    let seconds: string = formatNumber(parseInt((ms / 1000) % 60));
    return `${minutes}:${seconds}`;
  }

  const {
    activeTab,
    tabList,
    songInfo,
    playListInfo,
    videoInfo,
    artistInfo,
    userListInfo,
    albumInfo,
    djRadioInfo,
    mvInfo
  } = state;
  const {currentSongInfo, isPlaying, canPlayList} = song;


  return (
    <View
      className={classnames({
        searchResult_container: true,
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
      <AtSearchBar
        actionName="搜一下"
        value={keywords}
        onChange={searchTextChange}
        onActionClick={searchResult}
        onConfirm={searchResult}
        className="search__input"
        fixed={true}
      />
      <View className="search_content">
        <AtTabs
          current={activeTab}
          scroll
          tabList={tabList}
          onClick={switchTab}
        >
          <AtTabsPane current={activeTab} index={0}>
            {totalInfo.loading ? (
              <CLoading/>
            ) : (
              <ScrollView scrollY className="search_content__scroll">
                {totalInfo.noData ? (
                  <View className="search_content__nodata">暂无数据</View>
                ) : (
                  ""
                )}
                {totalInfo.songInfo.songs.length ? (
                  <View>
                    <View className="search_content__title">单曲</View>
                    {totalInfo.songInfo.songs.map(item => (
                      <View key={item.id} className="searchResult__music">
                        <View
                          className="searchResult__music__info"
                          onClick={() => {
                            playSong(item.id)
                          }}
                        >
                          <View className="searchResult__music__info__name">
                            {item.name}
                          </View>
                          <View className="searchResult__music__info__desc">
                            {`${item.ar[0] ? item.ar[0].name : ""} - ${
                              item.al.name
                            }`}
                          </View>
                        </View>
                        <View
                          className="fa fa-ellipsis-v searchResult__music__icon"
                          onClick={showMore}
                        />
                      </View>
                    ))}
                    {totalInfo.songInfo.moreText ? (
                      <View
                        className="search_content__more"
                        onClick={() => {
                          switchTab(1)
                        }}
                      >
                        {totalInfo.songInfo.moreText}
                        <AtIcon
                          value="chevron-right"
                          size="16"
                          color="#ccc"
                        />
                      </View>
                    ) : (
                      ""
                    )}
                  </View>
                ) : (
                  ""
                )}
                {totalInfo.playListInfo.playLists.length ? (
                  <View>
                    <View className="search_content__title">歌单</View>
                    <View>
                      {totalInfo.playListInfo.playLists.map((item) => (
                        <View
                          className="search_content__playList__item"
                          key={item.id}
                          onClick={() => {
                            goPlayListDetail(item)
                          }}
                        >
                          <View>
                            <Image
                              src={item.coverImgUrl}
                              className="search_content__playList__item__cover"
                            />
                          </View>
                          <View className="search_content__playList__item__info">
                            <View className="search_content__playList__item__info__title">
                              {item.name}
                            </View>
                            <View className="search_content__playList__item__info__desc">
                              <Text>{item.trackCount}首音乐</Text>
                              <Text className="search_content__playList__item__info__desc__nickname">
                                by {item.creator.nickname}
                              </Text>
                              <Text>{formatCount(item.playCount)}次</Text>
                            </View>
                          </View>
                        </View>
                      ))}
                      {totalInfo.playListInfo.moreText ? (
                        <View
                          className="search_content__more"
                          onClick={() => {
                            switchTab(2)
                          }}
                        >
                          {totalInfo.playListInfo.moreText}
                          <AtIcon
                            value="chevron-right"
                            size="16"
                            color="#ccc"
                          />
                        </View>
                      ) : (
                        ""
                      )}
                    </View>
                  </View>
                ) : (
                  ""
                )}
                {totalInfo.videoInfo.videos.length ? (
                  <View>
                    <View className="search_content__title">视频</View>
                    <View>
                      {totalInfo.videoInfo.videos.map(item => (
                        <View
                          className="search_content__video__item"
                          key={item.vid}
                          onClick={() => {
                            goVideoDetail(item.vid, "video")
                          }}
                        >
                          <View className="search_content__video__item__cover--wrap">
                            <View className="search_content__video__item__cover--playtime">
                              <Text className="at-icon at-icon-play"/>
                              <Text>{formatCount(item.playTime)}</Text>
                            </View>
                            <Image
                              src={item.coverUrl}
                              className="search_content__video__item__cover"
                            />
                          </View>
                          <View className="search_content__video__item__info">
                            <View className="search_content__video__item__info__title">
                              {item.title}
                            </View>
                            <View className="search_content__video__item__info__desc">
                              <Text>
                                {formatDuration(item.durationms)},
                              </Text>
                              <Text className="search_content__video__item__info__desc__nickname">
                                by {item.creator[0].userName}
                              </Text>
                            </View>
                          </View>
                        </View>
                      ))}
                      {totalInfo.videoInfo.moreText ? (
                        <View
                          className="search_content__more"
                          onClick={() => {
                            switchTab(3)
                          }}
                        >
                          {totalInfo.videoInfo.moreText}
                          <AtIcon
                            value="chevron-right"
                            size="16"
                            color="#ccc"
                          />
                        </View>
                      ) : (
                        ""
                      )}
                    </View>
                  </View>
                ) : (
                  ""
                )}

                {totalInfo.sim_query.sim_querys.length ? (
                  <View>
                    <View className="search_content__title">相关搜索</View>
                    <View className="search_content__simquery">
                      {totalInfo.sim_query.sim_querys.map(item => (
                        <Text
                          key={item.keyword}
                          onClick={() => {
                            queryResultBySim(item.keyword)
                          }}
                          className="search_content__simquery__item"
                        >
                          {item.keyword}
                        </Text>
                      ))}
                    </View>
                  </View>
                ) : (
                  ""
                )}
                {totalInfo.artistInfo.artists.length ? (
                  <View>
                    <View className="search_content__title">歌手</View>
                    <View>
                      {totalInfo.artistInfo.artists.map(item => (
                        <View
                          className="search_content__artist__item"
                          key={item.id}
                          onClick={showTip}
                        >
                          <Image
                            src={item.picUrl}
                            className="search_content__artist__item__cover"
                          />
                          <Text>
                            {item.name}
                            {item.alias[0] ? `（${item.alias[0]}）` : ""}
                          </Text>
                        </View>
                      ))}
                      {totalInfo.artistInfo.moreText ? (
                        <View
                          className="search_content__more"
                          onClick={() => {
                            switchTab(4)
                          }}
                        >
                          {totalInfo.artistInfo.moreText}
                          <AtIcon
                            value="chevron-right"
                            size="16"
                            color="#ccc"
                          />
                        </View>
                      ) : (
                        ""
                      )}
                    </View>
                  </View>
                ) : (
                  ""
                )}
                {totalInfo.albumInfo.albums.length ? (
                  <View>
                    <View className="search_content__title">专辑</View>
                    <View>
                      {totalInfo.albumInfo.albums.map(item => (
                        <View
                          className="search_content__playList__item"
                          key={item.id}
                          onClick={showTip}
                        >
                          <View>
                            <Image
                              src={item.picUrl}
                              className="search_content__playList__item__cover"
                            />
                          </View>
                          <View className="search_content__playList__item__info">
                            <View className="search_content__playList__item__info__title">
                              {item.name}
                            </View>
                            <View className="search_content__playList__item__info__desc">
                              <Text>{item.artist.name}</Text>
                              <Text className="search_content__playList__item__info__desc__nickname">
                                {item.containedSong
                                  ? `包含单曲：${item.containedSong}`
                                  : formatTimeStampToTime(item.publishTime)}
                              </Text>
                            </View>
                          </View>
                        </View>
                      ))}
                      {totalInfo.albumInfo.moreText ? (
                        <View
                          className="search_content__more"
                          onClick={() => {
                            switchTab(5)
                          }}
                        >
                          {totalInfo.albumInfo.moreText}
                          <AtIcon
                            value="chevron-right"
                            size="16"
                            color="#ccc"
                          />
                        </View>
                      ) : (
                        ""
                      )}
                    </View>
                  </View>
                ) : (
                  ""
                )}
                {totalInfo.djRadioInfo.djRadios.length ? (
                  <View>
                    <View className="search_content__title">电台</View>
                    <View>
                      {totalInfo.djRadioInfo.djRadios.map(item => (
                        <View
                          className="search_content__playList__item"
                          key={item.id}
                          onClick={showTip}
                        >
                          <View>
                            <Image
                              src={item.picUrl}
                              className="search_content__playList__item__cover"
                            />
                          </View>
                          <View className="search_content__playList__item__info">
                            <View className="search_content__playList__item__info__title">
                              {item.name}
                            </View>
                            <View className="search_content__playList__item__info__desc">
                              <Text>{item.desc}</Text>
                            </View>
                          </View>
                        </View>
                      ))}
                      {totalInfo.djRadioInfo.moreText ? (
                        <View
                          className="search_content__more"
                          onClick={() => {
                            switchTab(6)
                          }}
                        >
                          {totalInfo.djRadioInfo.moreText}
                          <AtIcon
                            value="chevron-right"
                            size="16"
                            color="#ccc"
                          />
                        </View>
                      ) : (
                        ""
                      )}
                    </View>
                  </View>
                ) : (
                  ""
                )}
                {totalInfo.userListInfo.users.length ? (
                  <View>
                    <View className="search_content__title">用户</View>
                    <View>
                      {totalInfo.userListInfo.users.map(item => (
                        <View
                          className="search_content__artist__item"
                          key={item.userId}
                          onClick={showTip}
                        >
                          <Image
                            src={item.avatarUrl}
                            className="search_content__artist__item__cover"
                          />
                          <View className="search_content__artist__item__info">
                            <View>
                              {item.nickname}
                              {item.gender === 1 ? (
                                <AtIcon
                                  prefixClass="fa"
                                  value="mars"
                                  size="12"
                                  color="#5cb8e7"
                                />
                              ) : (
                                ""
                              )}
                              {item.gender === 2 ? (
                                <AtIcon
                                  prefixClass="fa"
                                  value="venus"
                                  size="12"
                                  color="#f88fb8"
                                />
                              ) : (
                                ""
                              )}
                            </View>
                            {item.signature ? (
                              <View className="search_content__artist__item__desc">
                                {item.signature}
                              </View>
                            ) : (
                              ""
                            )}
                          </View>
                        </View>
                      ))}
                      {totalInfo.userListInfo.moreText ? (
                        <View
                          className="search_content__more"
                          onClick={() => {
                            switchTab(7)
                          }}
                        >
                          {totalInfo.userListInfo.moreText}
                          <AtIcon
                            value="chevron-right"
                            size="16"
                            color="#ccc"
                          />
                        </View>
                      ) : (
                        ""
                      )}
                    </View>
                  </View>
                ) : (
                  ""
                )}
              </ScrollView>
            )}
          </AtTabsPane>
          <AtTabsPane current={activeTab} index={1}>
            <ScrollView
              scrollY
              onScrollToLower={getSongList}
              className="search_content__scroll"
            >
              {songInfo.songs.map(item => (
                <View key={item.id} className="searchResult__music">
                  <View
                    className="searchResult__music__info"
                    onClick={() => {
                      playSong(item.id)
                    }}
                  >
                    <View className="searchResult__music__info__name">
                      {item.name}
                    </View>
                    <View className="searchResult__music__info__desc">
                      {`${item.ar[0] ? item.ar[0].name : ""} - ${
                        item.al.name
                      }`}
                    </View>
                  </View>
                  <View
                    className="fa fa-ellipsis-v searchResult__music__icon"
                    onClick={showMore}
                  ></View>
                </View>
              ))}
              {songInfo.more ? <CLoading/> : ""}
            </ScrollView>
          </AtTabsPane>
          <AtTabsPane current={activeTab} index={2}>
            <ScrollView
              scrollY
              onScrollToLower={getPlayList}
              className="search_content__scroll"
            >
              <CWhiteSpace size="sm" color="#fff"/>
              {playListInfo.playLists.map(item => (
                <View
                  className="search_content__playList__item"
                  key={item.id}
                  onClick={() => {
                    goPlayListDetail(item)
                  }}
                >
                  <View>
                    <Image
                      src={item.coverImgUrl}
                      className="search_content__playList__item__cover"
                    />
                  </View>
                  <View className="search_content__playList__item__info">
                    <View className="search_content__playList__item__info__title">
                      {item.name}
                    </View>
                    <View className="search_content__playList__item__info__desc">
                      <Text>{item.trackCount}首音乐</Text>
                      <Text className="search_content__playList__item__info__desc__nickname">
                        by {item.creator.nickname}
                      </Text>
                      <Text>{formatCount(item.playCount)}次</Text>
                    </View>
                  </View>
                </View>
              ))}
              {playListInfo.more ? <CLoading/> : ""}
            </ScrollView>
          </AtTabsPane>
          <AtTabsPane current={activeTab} index={3}>
            <ScrollView
              scrollY
              onScrollToLower={getVideoList}
              className="search_content__scroll"
            >
              <CWhiteSpace size="sm" color="#fff"/>
              {videoInfo.videos.map((item) => (
                <View
                  className="search_content__video__item"
                  key={item.vid}
                  onClick={() => {
                    goVideoDetail(item.vid, "video")
                  }}
                >
                  <View className="search_content__video__item__cover--wrap">
                    <View className="search_content__video__item__cover--playtime">
                      <Text className="at-icon at-icon-play"/>
                      <Text>{formatCount(item.playTime)}</Text>
                    </View>
                    <Image
                      src={item.coverUrl}
                      className="search_content__video__item__cover"
                    />
                  </View>
                  <View className="search_content__video__item__info">
                    <View className="search_content__video__item__info__title">
                      {item.title}
                    </View>
                    <View className="search_content__video__item__info__desc">
                      <Text>{formatDuration(item.durationms)},</Text>
                      <Text className="search_content__video__item__info__desc__nickname">
                        by {item.creator[0].userName}
                      </Text>
                    </View>
                  </View>
                </View>
              ))}
              {videoInfo.more ? <CLoading/> : ""}
            </ScrollView>
          </AtTabsPane>
          <AtTabsPane current={activeTab} index={4}>
            <ScrollView
              scrollY
              onScrollToLower={getArtistList}
              className="search_content__scroll"
            >
              <CWhiteSpace size="sm" color="#fff"/>
              {artistInfo.artists.map(item => (
                <View
                  className="search_content__artist__item"
                  key={item.id}
                  onClick={showTip}
                >
                  <Image
                    src={item.picUrl}
                    className="search_content__artist__item__cover"
                  />
                  <Text>
                    {item.name}
                    {item.alias[0] ? `（${item.alias[0]}）` : ""}
                  </Text>
                </View>
              ))}
              {artistInfo.more ? <CLoading/> : ""}
            </ScrollView>
          </AtTabsPane>
          <AtTabsPane current={activeTab} index={5}>
            <ScrollView
              scrollY
              onScrollToLower={getAlbumList}
              className="search_content__scroll"
            >
              <CWhiteSpace size="sm" color="#fff"/>
              {albumInfo.albums.map(item => (
                <View
                  className="search_content__playList__item"
                  key={item.id}
                  onClick={showTip}
                >
                  <View>
                    <Image
                      src={item.picUrl}
                      className="search_content__playList__item__cover"
                    />
                  </View>
                  <View className="search_content__playList__item__info">
                    <View className="search_content__playList__item__info__title">
                      {item.name}
                    </View>
                    <View className="search_content__playList__item__info__desc">
                      <Text>{item.artist.name}</Text>
                      <Text className="search_content__playList__item__info__desc__nickname">
                        {item.containedSong
                          ? `包含单曲：${item.containedSong}`
                          : formatTimeStampToTime(item.publishTime)}
                      </Text>
                    </View>
                  </View>
                </View>
              ))}
              {albumInfo.more ? <CLoading/> : ""}
            </ScrollView>
          </AtTabsPane>
          <AtTabsPane current={activeTab} index={6}>
            <ScrollView
              scrollY
              onScrollToLower={getDjRadioList}
              className="search_content__scroll"
            >
              <CWhiteSpace size="sm" color="#fff"/>
              {djRadioInfo.djRadios.map(item => (
                <View
                  className="search_content__playList__item"
                  key={item.id}
                  onClick={showTip}
                >
                  <View>
                    <Image
                      src={item.picUrl}
                      className="search_content__playList__item__cover"
                    />
                  </View>
                  <View className="search_content__playList__item__info">
                    <View className="search_content__playList__item__info__title">
                      {item.name}
                    </View>
                    <View className="search_content__playList__item__info__desc">
                      <Text>{item.desc}</Text>
                    </View>
                  </View>
                </View>
              ))}
              {djRadioInfo.more ? <CLoading/> : ""}
            </ScrollView>
          </AtTabsPane>
          <AtTabsPane current={activeTab} index={7}>
            <ScrollView
              scrollY
              onScrollToLower={getUserList}
              className="search_content__scroll"
            >
              <CWhiteSpace size="sm" color="#fff"/>
              {userListInfo.users.map(item => (
                <View
                  className="search_content__artist__item"
                  key={item.userId}
                  onClick={showTip}
                >
                  <Image
                    src={item.avatarUrl}
                    className="search_content__artist__item__cover"
                  />
                  <View className="search_content__artist__item__info">
                    <View>
                      {item.nickname}
                      {item.gender === 1 ? (
                        <AtIcon
                          prefixClass="fa"
                          value="mars"
                          size="12"
                          color="#5cb8e7"
                        />
                      ) : (
                        ""
                      )}
                      {item.gender === 2 ? (
                        <AtIcon
                          prefixClass="fa"
                          value="venus"
                          size="12"
                          color="#f88fb8"
                        />
                      ) : (
                        ""
                      )}
                    </View>
                    <View className="search_content__artist__item__desc">
                      {item.signature}
                    </View>
                  </View>
                </View>
              ))}
              {userListInfo.more ? <CLoading/> : ""}
            </ScrollView>
          </AtTabsPane>
          <AtTabsPane current={activeTab} index={8}>
            <ScrollView
              scrollY
              onScrollToLower={getVideoList}
              className="search_content__scroll"
            >
              <CWhiteSpace size="sm" color="#fff"/>
              {mvInfo.mvs.map(item => (
                <View
                  className="search_content__video__item"
                  key={item.id}
                  onClick={() => {
                    goVideoDetail(item.id, "mv")
                  }}
                >
                  <View className="search_content__video__item__cover--wrap">
                    <View className="search_content__video__item__cover--playtime">
                      <Text className="at-icon at-icon-play"/>
                      <Text>{formatCount(item.playCount)}</Text>
                    </View>
                    <Image
                      src={item.cover}
                      className="search_content__video__item__cover"
                    />
                  </View>
                  <View className="search_content__video__item__info">
                    <View className="search_content__video__item__info__title">
                      {item.name}
                    </View>
                    <View className="search_content__video__item__info__desc">
                      <Text>{formatDuration(item.duration)},</Text>
                      <Text className="search_content__video__item__info__desc__nickname">
                        by {item.artists[0].name}
                      </Text>
                    </View>
                  </View>
                </View>
              ))}
              {mvInfo.more ? <CLoading/> : ""}
            </ScrollView>
          </AtTabsPane>
        </AtTabs>
      </View>
    </View>
  );
}

Page.config = {
  navigationBarTitleText: "搜索"
};

export default Page
