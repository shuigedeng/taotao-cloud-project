import client from "../../../utils/client";
import gql from "graphql-tag";

// 商品
export const balance = (currentPage) =>
client.query({
  query: gql`
    query($currentPage: Int){
      balance(currentPage: $currentPage){
        list{
          id
          balance
          price
          add
          remark
          createdAt
        }
        pagination{
          pageSize
          total
          current
        }
      }
    }
  `,
  variables: {currentPage},
  fetchPolicy: "no-cache"
});
