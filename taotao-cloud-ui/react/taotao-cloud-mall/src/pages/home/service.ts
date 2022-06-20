import gql from "graphql-tag";
import client from "@/http/graphql/client";

// 授权登录
export const login = (code, imageUrl, nickname) =>
client.mutate({
  mutation: gql`
    mutation generateAccessTokenByLoginWithWeChatCode(
      $code: String!
      $imageUrl: String
      $nickname: String
    ) {
      accessToken: generateAccessTokenByLoginWithWeChatCode(
        code: $code
        imageUrl: $imageUrl
        nickname: $nickname
      )
    }
  `,
  variables: {code, imageUrl, nickname}
});

// 扫码邀请
export const invitedUsers = (invitedId) =>
client.mutate({
  mutation: gql`
    mutation invitedUsers(
      $invitedId: ID!
    ) {
      invitedUsers (
        invitedId: $invitedId
      )
    }
  `,
  variables: {invitedId}
});

// 首页banner
export const banners = position =>
client.query({
  query: gql`
    query($position: String!) {
      banners(position: $position) {
        id
        title
        position
        imageUrl
      }
    }
  `,
  variables: {position},
  fetchPolicy: "no-cache"
});

// 专题
export const projects = () =>
client.query({
  query: gql`
    query{
      projects{
        id
        title
        imageUrl
      }
    }
  `,
  fetchPolicy: "no-cache"
});

export const homeCenterQuery = position =>
client.query({
  query: gql`
    query($position: String!) {
      banners(position: $position) {
        id
        title
        position
        imageUrl
      }
    }
  `,
  variables: {position},
  fetchPolicy: "no-cache"
});

export const homeCenter = position =>
client.query({
  query: gql`
    query($position: String!) {
      banners(position: $position) {
        id
        title
        position
        imageUrl
      }
    }
  `,
  variables: {position},
  fetchPolicy: "no-cache"
});


// 商品分类
export const classify = () =>
client.query({
  query: gql`
    query {
      classify {
        id
        title
        imageUrl
      }
    }
  `,
  fetchPolicy: "no-cache"
});

// 商品
export const items = (pageSize, currentPage) =>
client.query({
  query: gql`
    query($pageSize: Int, $currentPage: Int) {
      items(pageSize: $pageSize, currentPage: $currentPage) {
        list {
          code
          name
          imageUrl
          content
          originalPrice
          commission
          price
          memberPrice
          unit
          stock
          type
          kind
          status
          followed
          pointDiscountPrice
        }
        pagination {
          pageSize
          total
          current
        }
      }
    }
  `,
  variables: {pageSize, currentPage},
  fetchPolicy: "no-cache"
});

// 专题商品
export const projectItems = (projectId, pageSize) =>
client.query({
  query: gql`
    query($projectId: ID, $pageSize: Int) {
      items(projectId: $projectId, pageSize: $pageSize) {
        list {
          code
          name
          imageUrl
          content
          originalPrice
          commission
          price
          unit
          stock
          type
          kind
          status
          followed
        }
        pagination {
          pageSize
          total
          current
        }
      }
    }
  `,
  variables: {projectId, pageSize},
  fetchPolicy: "no-cache"
});

// 首页附近商家
export const nearbyStore = (longitude, latitude, currentPage,) =>
client.query({
  query: gql`
    query(
      $longitude: String!
      $latitude: String!
      $currentPage: Int
    ) {
      nearbyStore(
        input: {
          longitude: $longitude
          latitude: $latitude
          currentPage: $currentPage
        }
      ) {
        list {
          id
          name
          imageUrl
          address
          longitude
          latitude
          distance
          sales
          balance
          status
        }
        pagination {
          pageSize
          total
          current
        }
      }
    }
  `,
  variables: {longitude, latitude, currentPage,},
  fetchPolicy: "no-cache"
});

// 根据code查询单件商品
export const singleItem = code =>
client.query({
  query: gql`
    query Item($code:ID!){
      item(code:$code ){
        code
        name
        imageUrl
        content
        originalPrice
        commission
        memberPrice
        price
        pointDiscountPrice
        unit
        stock
        type
        kind
        status
        followed
      }
    }
  `,
  variables: {code},
  fetchPolicy: "no-cache"
});
