import client from '../../utils/client';
import gql from 'graphql-tag';

export const bind = (code, encryptedData, iv) => {
  return client.mutate({
    mutation: gql`
      mutation BindPhoneByUser(
        $code: String!
        $encryptedData: String!
        $iv: String!
      ) {
        bindPhoneByUser(
          code: $code
          encryptedData: $encryptedData
          iv: $iv
        ) {
          number
        }
      }
    `,
    variables: { code, encryptedData, iv },
  });
};
