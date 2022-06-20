import client from "../../utils/client";
import gql from "graphql-tag";
export const addAccount = (name, phone, account, card) =>
    client.mutate({
        mutation: gql`
      mutation CreateOrder(
        $name: String
        $phone: String
        $account: String
        $card: String
        $openId: String
      ) {
        addAccount(
          input: {
            name: $name
            phone: $phone
            account: $account
            card: $card
            openId: $openId
          }
        ) {
            name
            phone
            account
            card
            openId
        }
      }
    `,
        variables: { name, phone, account, card }
    });