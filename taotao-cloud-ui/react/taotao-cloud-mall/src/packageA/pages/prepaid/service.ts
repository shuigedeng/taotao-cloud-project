import gql from "graphql-tag";
import client from "@/http/graphql/client";

  // query
export const topUpList = () =>
  client.query({
    query: gql`
      query{
        topUpList{
          id
          price
          givePoint
        }
      }
    `,
    fetchPolicy: "no-cache"
  });


export const userTopUp = (id) =>
  client.mutate({
    mutation: gql`
      mutation($id: ID!){
        userTopUp(id: $id){
          appId
          timeStamp
          nonceStr
          signType
          package
          paySign
          timestamp
        }
      }
  `,
      variables: { id }
  });
