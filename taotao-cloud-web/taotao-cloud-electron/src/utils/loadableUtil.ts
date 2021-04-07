import Loadable from 'react-loadable'
import Loading from '../components/loading'

const loadableUtil = (loader: any, loading = Loading) => {
  return Loadable({
    loader: loader,
    loading: loading
  })
}

export default loadableUtil
