import gql from "graphql-tag";
import client from "@/http/graphql/client";

// query
export const myCoupons = (status,pageSize,currentPage) =>
  client.query({
    query: gql`
      query($status: Priority, $pageSize: Int, $currentPage: Int){
        coupons(input: {
          status: $status
          pageSize: $pageSize
          currentPage: $currentPage
        }){
          list{
            id
            amount
            require
            usedAt
            expiredDate
            type
          }
          pagination{
            pageSize
            total
            current
          }
        }
      }
    `,
    variables: { status, pageSize, currentPage },
    fetchPolicy: "no-cache"
  });
