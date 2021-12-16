import client from "../../../utils/client";
import gql from "graphql-tag";

// 商品
export const points = (userId,currentPage,pageSize) =>
client.query({
  query: gql`
    query($userId: ID,$currentPage: Int,$pageSize: Int) {
      points(userId: $userId, currentPage: $currentPage, pageSize: $pageSize) {
        list{
          point
          price
          add
          remark
          createdAt
        }
        pagination {
          pageSize
          total
          current
        }
      }
    }
  `,
  variables: { userId,currentPage,pageSize},
  fetchPolicy: "no-cache"
});
