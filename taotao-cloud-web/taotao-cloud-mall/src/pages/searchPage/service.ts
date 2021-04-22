import client from "../../utils/client";
import gql from "graphql-tag";

// 商品
export const items = (nameLike,currentPage,pageSize) =>
client.query({
  query: gql`
    query($nameLike: String,$currentPage: Int,$pageSize: Int) {
      items(nameLike: $nameLike,currentPage: $currentPage,pageSize: $pageSize) {
        list {
          code
          name
          content
          originalPrice 
          price
          memberPrice
          unit
          imageUrl
          stock
          type
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
  variables: { nameLike,currentPage,pageSize },
  fetchPolicy: "no-cache"
});
