import user from "./user"
import auth from "./auth"
import banner from "./banner"
import product from "./product"

export default {
  uc: {
    ...user,
  },
  auth: {
    ...auth
  },
  banner: {
    ...banner
  },
  product: {
    ...product
  }
}
