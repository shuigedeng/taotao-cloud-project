import { ApolloClient, InMemoryCache } from '@apollo/client'

export const client = new ApolloClient({
  uri: 'http://localhost:9900/api/v1.0/manager/graphql',
  cache: new InMemoryCache()
})
