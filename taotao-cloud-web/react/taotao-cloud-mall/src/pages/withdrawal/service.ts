import gql from "graphql-tag";
import client from "@/http/graphql/client";

export const myAccount = () =>
  client.query({
    query: gql`
      query() {
        account {
          name
          phone
          account
          card
          openId
        }
      }
    `,
    variables: { },
    fetchPolicy: "no-cache"
  });

// 申请提现
export const toApplyForCashWithdrawals = () =>
  client.mutate({
    mutation: gql`
      mutation toApplyForCashWithdrawals(
        $remark:String
        ) {
        toApplyForCashWithdrawals (
          input: {
            remark: $remark
          }) {
                id
                price
              }
      }
    `,
    variables: {  }
  });
