import {ISongAction, SongActionType} from "../../action/songAction";
import {songInitState, SongStateType} from "../../state/songState";

export default function index(state: SongStateType = songInitState, action: ISongAction) {
  switch (action.type) {
    // 获取歌曲详情
    case SongActionType.GETSONGDETAIL:
      return {
        ...state
      }
    // 获取歌单详情
    case SongActionType.GETPLAYLISTDETAIL:
      const {playListDetailInfo, playListDetailPrivileges} = action.payload
      let canPlayList = playListDetailInfo.tracks.filter((_, index) => {
        return playListDetailPrivileges[index].st !== -200
      })
      return {
        ...state,
        playListDetailInfo,
        playListDetailPrivileges,
        canPlayList
      }
    case SongActionType.RESETPLAYLIST:
      return {
        ...state,
        playListDetailInfo: songInitState.playListDetailInfo,
        playListDetailPrivileges: [],
        canPlayList: []
      }
    // 获取推荐歌单
    case SongActionType.GETRECOMMENDPLAYLIST:
      const {recommendPlayList} = action.payload
      return {
        ...state,
        recommendPlayList
      }
    // 获取推荐电台
    case SongActionType.GETRECOMMENDDJ:
      const {recommendDj} = action.payload
      return {
        ...state,
        recommendDj
      }
    // 获取推荐新音乐
    case SongActionType.GETRECOMMENDNEWSONG:
      const {recommendNewSong} = action.payload
      return {
        ...state,
        recommendNewSong
      }
    // 获取推荐精彩节目
    case SongActionType.GETRECOMMEND:
      const {recommend} = action.payload
      return {
        ...state,
        recommend
      }
    // 获取歌曲详情
    case SongActionType.GETSONGINFO:
      const {currentSongInfo} = action.payload
      let currentSongIndex = state.canPlayList.findIndex(item => item.id === currentSongInfo.id)
      state.canPlayList.map((item, index) => {
        item.current = false
        if (currentSongIndex === index) {
          item.current = true
        }
        return item
      })
      return {
        ...state,
        currentSongInfo,
        currentSongIndex,
        canPlayList: state.canPlayList
      }
    // 切换播放模式
    case SongActionType.CHANGEPLAYMODE:
      const {playMode} = action.payload
      return {
        ...state,
        playMode
      }
    // 获取喜欢列表
    case SongActionType.GETLIKEMUSICLIST:
      const {likeMusicList} = action.payload
      return {
        ...state,
        likeMusicList
      }
    // 更新喜欢列表
    case SongActionType.UPDATELIKEMUSICLIST:
      const {like, id} = action.payload
      let list: Array<number> = []
      if (like) {
        list = state.likeMusicList.concat([id])
      } else {
        state.likeMusicList.forEach((item) => {
          if (item !== id) {
            list.push(item)
          }
        })
      }
      return {
        ...state,
        likeMusicList: list
      }
    case SongActionType.UPDATEPLAYSTATUS:
      const {isPlaying} = action.payload
      return {
        ...state,
        isPlaying
      }
    case SongActionType.UPDATECANPLAYLIST:
      currentSongIndex = action.payload.canPlayList.findIndex(item => item.id === action.payload.currentSongId)
      return {
        ...state,
        canPlayList: action.payload.canPlayList,
        currentSongIndex
      }
    case SongActionType.UPDATERECENTTAB:
      const {recentTab} = action.payload
      return {
        ...state,
        recentTab
      }
    default:
      return state
  }
}
