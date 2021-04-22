import client from "../../utils/client";
import gql from "graphql-tag";

// export const confirm = (itemIds, storeId, couponId, time, address) =>
//   client.mutate({
//     mutation: gql`
//       mutation CreateOrder(
//         $itemIds: [itemIds]!
//         $storeId: ID!
//         $couponId: ID
//         $address: AddressInput
//         $time: String
//       ) {
//         createOrder(
//           input: {
//             itemIds: $itemIds
//             storeId: $storeId
//             couponId: $couponId
//             address: $address
//             time: $time
//           }
//         ) {
//           appId
//           timeStamp
//           paySign
//           package
//           signType
//           nonceStr
//           timestamp
//         }
//       }
//     `,
//     variables: { itemIds, storeId, couponId, time, address }
//   });

export const createOrders = (itemIds, storeId, couponId, time, address, type, payment, pointDiscount) =>
  client.mutate({
    mutation: gql`
      mutation createOrder(
        $itemIds: [itemIds]!
        $storeId: ID!
        $couponId: ID
        $address: AddressInput
        $time: String
        $type:OrderType!
        $payment:OrderPayment
        $pointDiscount:Boolean!
      ) {
        createOrder(
          input: {
            itemIds: $itemIds
            storeId: $storeId
            couponId: $couponId
            address: $address
            time: $time
            type:$type
            payment:$payment
            pointDiscount:$pointDiscount
          }
        ) {
          appId
          timeStamp
          paySign
          package
          signType
          nonceStr
          timestamp
        }
      }
    `,
    variables: { itemIds, storeId, couponId, time, address, type, payment, pointDiscount }
  });

export const createBalanceOrder = (itemIds, storeId, couponId, time, address, type, payment, pointDiscount) =>
  client.mutate({
    mutation: gql`
    mutation createBalanceOrder(
      $itemIds: [itemIds]!
      $storeId: ID!
      $couponId: ID
      $address: AddressInput
      $time: String
      $type:OrderType!
      $payment:OrderPayment
      $pointDiscount:Boolean!
    ) {
      createBalanceOrder(
        input: {
          itemIds: $itemIds
          storeId: $storeId
          couponId: $couponId
          address: $address
          time: $time
          type:$type
          payment:$payment
          pointDiscount:$pointDiscount
        }
      )
    }
  `,
    variables: { itemIds, storeId, couponId, time, address, type, payment, pointDiscount }
  });

export const userInfo = () =>
  client.query({
    query: gql`
      {
        userInfo {
          id
          imageUrl
          nickname
          recordBalance
          balance
          point
          role
          memberExpiredDate
          phone
          follow
          fans
          fanList{
            id
            imageUrl
            nickname
            point
            role
          }
        }
      }
    `,
    fetchPolicy: "no-cache"
  });


// 距离计算
export const distanceCalculation = (from, to, ) =>
  client.query({
    query: gql`
    query(
      $from:From
      $to:From
    ) {
      distanceCalculation(
        input: {
          from:$from
          to:$to
        }
      ) {
        distance
        duration
      }
    }
  `,
    variables: { from, to, },
    fetchPolicy: "no-cache"
  });

// 地址解析计算
export const addressToLngAndLat = (address) =>
  client.query({
    query: gql`
       query(
        $address:String!
       ) {
        addressToLngAndLat(
          address:$address
         ) {
          lng
          lat 
         }
       }
     `,
    variables: { address },
    fetchPolicy: "no-cache"
  });

export const freightPrice = () =>
  client.query({
    query: gql`
      query {
        config(primaryKey: "cost") {
          value
        }
      }
    `,
    fetchPolicy: "no-cache"
  });

export const free = () =>
  client.query({
    query: gql`
      query {
        config(primaryKey: "free") {
          value
        }
      }
    `,
    fetchPolicy: "no-cache"
  });

export const couponsList = (status, currentPage) =>
  client.query({
    query: gql`
      query($status: Priority, $currentPage: Int) {
        coupons(input: { status: $status, currentPage: $currentPage }) {
          list {
            id
            amount
            require
            usedAt
            expiredDate
          }
          pagination {
            pageSize
            total
            current
          }
        }
      }
    `,
    variables: { status, currentPage },
    fetchPolicy: "no-cache"
  });
