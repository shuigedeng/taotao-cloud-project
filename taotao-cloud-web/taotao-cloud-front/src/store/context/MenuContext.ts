import {Context, createContext} from "react"
import {IMenuState, menuInitState} from "../state/MenuState"

export const MenuContext: Context<IMenuState> = createContext(menuInitState)


