import gql from "graphql-tag";
import client from "@/http/graphql/client";

// 商品分类
export const classify = () =>
  client.query({
    query: gql`
      query {
        classify {
          id
          title
          imageUrl
        }
      }
    `,
    fetchPolicy: "no-cache"
  });

// 商品
export const items = (itemClassId, currentPage, pageSize) =>
  client.query({
    query: gql`
      query($itemClassId: ID,$currentPage: Int, $pageSize: Int) {
        items(itemClassId : $itemClassId,currentPage: $currentPage, pageSize: $pageSize) {
          list {
            code
            name
            imageUrl
            content
            originalPrice
            commission
            price
            memberPrice
            unit
            stock
            type
            kind
            status
            followed
          }
          pagination {
            pageSize
            total
            current
          }
        }
      }
    `,
    variables: { itemClassId, currentPage, pageSize},
    fetchPolicy: "no-cache"
  });
