import client from "../../utils/client";
import gql from "graphql-tag";

// query
export const couponsList = (type) =>
  client.query({
    query: gql`
      query($type:CouponType){
        issueCoupon(type: $type){
          id
          amount
          require
          expiredDate
          number
        }
      }
    `,
    variables: { type },
    fetchPolicy: "no-cache"
  });

// 领取优惠券
export const receive = id =>
  client.mutate({
    mutation: gql`
      mutation($id: ID!) {
        drawCoupon(id: $id) {
          id
        }
      }
    `,
    variables: { id }
  });
