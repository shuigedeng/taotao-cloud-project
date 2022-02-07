import client from "../../utils/client";
import gql from "graphql-tag";

export const collectionItems = currentPage =>
  client.query({
    query: gql`
      query($currentPage: Int) {
        items( type: "follow", currentPage: $currentPage ) {
          list {
            code
            name
            imageUrl
          }
          pagination {
            pageSize
            total
            current
          }
        }
      }
    `,
    variables: { currentPage },
    fetchPolicy: "no-cache"
  });
