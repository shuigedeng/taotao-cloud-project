import client from "../../utils/client";
import gql from "graphql-tag";

export const free = () =>
  client.query({
    query: gql`
      query {
        config(primaryKey: "agreement") {
          value
        }
      }
    `,
    fetchPolicy: "no-cache"
  });