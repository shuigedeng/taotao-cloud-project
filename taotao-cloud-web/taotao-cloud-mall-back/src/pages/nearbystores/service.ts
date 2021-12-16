import gql from "graphql-tag";
import client from "@/http/graphql/client";

 // 首页附近商家
export const nearbyStore = (longitude, latitude, currentPage, pageSize) =>
client.query({
  query: gql`
    query(
      $longitude: String!
      $latitude: String!
      $currentPage: Int
      $pageSize: Int
    ) {
      nearbyStore(
        input: {
          longitude: $longitude
          latitude: $latitude
          currentPage: $currentPage
          pageSize: $index
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
  variables: { longitude, latitude, currentPage, pageSize},
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
