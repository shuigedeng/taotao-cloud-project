import client from "../../utils/client";
import gql from "graphql-tag";

 // 首页附近商家
export const nearbyStore = (longitude, latitude, currentPage,) =>
client.query({
  query: gql`
    query(
      $longitude: String!
      $latitude: String!
      $currentPage: Int
    ) {
      nearbyStore(
        input: {
          longitude: $longitude
          latitude: $latitude
          currentPage: $currentPage
        }
      ) {
        list {
          id
          name
          imageUrl
          address
          longitude
          latitude
          distance
          sales
          balance
          status
        }
        pagination {
          pageSize
          total
          current
        }
      }
    }
  `,
  variables: { longitude, latitude, currentPage, },
  fetchPolicy: "no-cache"
});

  // 获取地址
// export const currentAddress = (longitude, latitude) =>
// client.query({
//   query: gql`
//     query($longitude: String, $latitude: String) {
//       currentAddress(longitude: $longitude, latitude: $latitude) {
//         province
//         city
//       }
//     }
//   `,
//   variables: { longitude, latitude },
//   fetchPolicy: "no-cache"
// });