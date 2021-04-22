import client from "../../utils/client";
import gql from "graphql-tag";


// 金额变动
export const balance = () =>
    client.query({
        query: gql`
      query() {
        balance() {
          list {
            id
            balance
            price
            add 
            remark
            createdAt
            updatedAt
            balanceStoreInfo{
                id
                imageUrl
                name
            }
            balanceUser{
                id
            }
            balanceOrder{
                id
                price
                discount
                amount
                code
                time
                orderItem{
                title
                imageUrl
                number
                }
                
            }
          }
          pagination {
            pageSize
            total
            current
          }
        }
      }
    `,
        variables: {},
        fetchPolicy: "no-cache"
    });