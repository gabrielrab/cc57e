import { CREATE_GROUP_CHAT, CREATE_SINGLE_CHAT, GET_ALL_CHAT, PUT_REMOVE_MEMBER, GET_ALL_PUBLI_CHAT, PUT_SEND_INVITE_MEMBER, PUT_ACCEPT_INVITE_MEMBER } from "./ActionType"

const initialState = {
 chats:null,
 chatPubli:null,
 createdGroup:null
}

export const chatReducer = (store = initialState, { type, payload }) => {
 if (type === CREATE_SINGLE_CHAT) {
  return {...store, singleChat:payload}
 }
 else if (type === GET_ALL_CHAT) {
  return {...store, chats:payload}
 }
 else if (type === GET_ALL_PUBLI_CHAT) {
   return {...store, chatPubli:payload}
  }
 else if(type === CREATE_GROUP_CHAT){
    return {...store, createdGroup:payload}
 }

 else if(type === PUT_REMOVE_MEMBER){
   return {...store, response:payload}
 }
   
 else if(type === PUT_SEND_INVITE_MEMBER){
   return {...store, response:payload}
 }

 else if(type === PUT_ACCEPT_INVITE_MEMBER){
   return {...store, response:payload}
 }

 return store
}