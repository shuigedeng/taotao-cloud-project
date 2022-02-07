import client from "../../utils/client";
import gql from "graphql-tag";

export const withdrawals = (currentPage) =>
client.query({
    query: gql`
      query($currentPage: Int) {
        withdrawals(currentPage: $currentPage) {
        list {
            id
            price
            name
            phone
            account
            remark
            status
            createdAt
            card
            }
            pagination {
                total
                current
                pageSize
            }
        }
    }
`,
    variables: { currentPage },
    fetchPolicy: "no-cache"
  });