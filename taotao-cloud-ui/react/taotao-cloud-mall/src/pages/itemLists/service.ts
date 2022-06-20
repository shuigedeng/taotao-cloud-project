import client from "../../utils/client";
import gql from "graphql-tag";

export const handleQuery = ( itemClassId,currentPage,pageSize) =>
  client.query({
    query: gql`
      query($itemClassId: ID, $currentPage: Int, $pageSize: Int) {
        items (
          itemClassId : $itemClassId,
          currentPage: $currentPage,
          pageSize: $pageSize,
        ) {
          list {
            code
            name
            imageUrl
            price
            originalPrice
            memberPrice
          }
          pagination {
            pageSize
            total
            current
          }
        }
      }
    `,
    variables: { itemClassId,currentPage,pageSize },
    fetchPolicy: "no-cache"
  });