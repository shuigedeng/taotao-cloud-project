import { Context, createContext } from 'react'
import { IUserState, userInitState } from './UserState'

export const UserContext: Context<IUserState> = createContext(userInitState)
