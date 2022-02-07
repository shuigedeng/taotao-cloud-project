import client from "../../utils/client";
import gql from "graphql-tag";

// 商品
export const details = code =>
  client.query({
    query: gql`
      query($code: ID!) {
        item(code: $code) {
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
      }
    `,
    variables: { code },
    fetchPolicy: "no-cache"
  });
  // 收藏
export const collection = (itemCode, status) =>
  client.mutate({
    mutation: gql`
      mutation($itemCode: ID!, $status: String) {
        addCollection(itemCode: $itemCode, status: $status)
      }
    `,
    variables: { itemCode, status }
  });