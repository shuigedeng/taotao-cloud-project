import client from "../../utils/client";
import gql from "graphql-tag";

// query
export const orders = (status, currentPage,type) =>
  client.query({
    query: gql`
      query($status: OrderStatus,$currentPage: Int,$type:OrderType) {
        orders(input: { status: $status, currentPage: $currentPage,type:$type }) {
          list{
            id
            price
            discount
            amount
            trade{
              id
              price 
              status
            }
            orderItem{
              id
              imageUrl
              title
              price
              amount
              number
            }
            code
            time
            qrCode
            user{
              id 
              imageUrl 
              nickname 
              balance 
              point 
              role
              phone
            }
            store {
              name
            }
          }
          pagination {
            total
            current
            pageSize
          }
        }
      }
    `,
    variables: { status, currentPage,type },
    fetchPolicy: "no-cache"
  });
