import {Context, createContext} from "react"
import {IUserState, userInitState} from "../state/UserState"

export const UserContext: Context<IUserState> = createContext(userInitState)


