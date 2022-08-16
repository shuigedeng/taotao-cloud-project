import { gql } from 'apollo-boost'

export const LIST_USERS = gql`
  query ListUsers {
    users {
      id
      nickname
    }
  }
`

export const CATEGORIES_PRODUCT = gql`
  query {
    getCategories {
      id
      name
      products {
        id
        name
      }
    }
    getProducts {
      id
      name
      category {
        id
        name
        products {
          id
          name
        }
      }
    }
  }
`

export const CATEGORIES = gql`
  query {
    getCategories {
      id
      name
    }
  }
`

export const PRODUCTS = gql`
  query {
    getProducts {
      id
      name
      category {
        id
        name
      }
    }
  }
`

export const ADD_PRODUCT = gql`
  mutation($name: String!, $category: String!) {
    addProduct(name: $name, category: $category) {
      id
      name
      category {
        id
        name
      }
    }
  }
`

export const ADD_CATEGORY = gql`
  mutation($name: String!) {
    addCategory(name: $name) {
      id
      name
    }
  }
`

export const DEL_PRODUCT = gql`
  mutation($name: String!, $categoryId: String!) {
    delProductByName(name: $name, category: $categoryId) {
      id
      name
      category {
        id
        name
        products {
          id
          name
        }
      }
    }
  }
`
