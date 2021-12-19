import { useEffect, useState } from 'react'
import { getLoginUserToken } from '/@/utils/lsUtil'
import { useHistory } from 'react-router-dom'

const useLoginUser = () => {
  const [state, setState] = useState<string>()

  const history = useHistory()

  useEffect(() => {
    const token = getLoginUserToken()
    if (token) {
      setState(token)
    } else {
      history.push('/login')
    }
  }, [])

  return state
}

export default useLoginUser
