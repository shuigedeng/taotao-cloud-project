import gql from "graphql-tag";
import client from "@/http/graphql/client";

// query
export const orderInformation = id =>
  client.query({
    query: gql`
      query($id: ID!) {
        order(id: $id) {
          id
          price
          discount
          amount
          code
          time
          type
          payment
          address{
            receiverAddress
            receiverName
            receiverPhone
          }
          createdAt
          trade{
              id
              price
              status
            }
          store{
              name
          }
          user{
              id
          }
          orderItem {
            id
            imageUrl
            title
            number
            amount
          }
          distributionUser{
            name
            phone
          }
        }
      }
    `,
    variables: { id },
    fetchPolicy: "no-cache"
  });
