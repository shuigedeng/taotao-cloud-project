import client from "../../utils/client";
import gql from "graphql-tag";

export const handleQuery = ( currentPage, pageSize, projectId) =>
  client.query({
    query: gql`
      query($currentPage: Int, $pageSize: Int, $projectId:ID) {
        items(currentPage: $currentPage, pageSize: $pageSize, projectId:$projectId) {
          list {
            code
            name
            imageUrl
            originalPrice 
            price
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
    variables: { currentPage, pageSize, projectId},
    fetchPolicy: "no-cache"
  });
