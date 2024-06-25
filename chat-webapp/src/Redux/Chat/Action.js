import { BASE_URL } from "../../Config/Api";
import { CREATE_GROUP_CHAT, CREATE_SINGLE_CHAT, GET_ALL_CHAT, PUT_REMOVE_MEMBER , GET_ALL_PUBLI_CHAT } from "./ActionType";

export const createSingleChat = (data) => async(dispatch) => {
  try {
     const res = await fetch(`${BASE_URL}/chats/single`, {
  
      method:"POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization":`Bearer ${data.token}`,
      },
      body:JSON.stringify({userId:data.userId})
    }
    )
 const chat = await res.json();

 console.log("created chat ----- ",chat)
 dispatch({type:CREATE_SINGLE_CHAT, payload:chat})
  } catch (error) {
    console.log("error catch ",error)
  }

}
export const createGroupChat = (data) => async(dispatch) => {
  try {
     const res = await fetch(`${BASE_URL}/chats/group`, {
  
      method:"POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization":`Bearer ${data.token}`,
      },
      body:JSON.stringify(data.group)
    }
    )
 const chat = await res.json();

 console.log("created group chat ----- ",chat)
 dispatch({type:CREATE_GROUP_CHAT, payload:chat})
  } catch (error) {
    console.log("error catch ",error)
  }

}

export const getAllChat = (token) => async(dispatch) => {
 const res = await fetch(`${BASE_URL}/chats/user`, {
      method:"GET",
      headers: {
        "Content-Type": "application/json",
        "Authorization":`Bearer ${token}`
      }
    });
    const chats = await res.json();
    console.log("get chats ----- ",chats)
 dispatch({type:GET_ALL_CHAT, payload:chats})
}

export const getAllPubliChat = (token) => async(dispatch) => {
  const res = await fetch(`${BASE_URL}/chats/all`, {
       method:"GET",
       headers: {
         "Content-Type": "application/json",
         "Authorization":`Bearer ${token}`
       }
     });
     const chatPubli = await res.json();
     console.log("get chats PUBLI ----- ",chatPubli)
  dispatch({type:GET_ALL_PUBLI_CHAT, payload:chatPubli})
 }

export const PutRemoveMember = (token , chatId , userId) => async(dispatch) => {
  const res = await fetch(`${BASE_URL}/chats/${chatId}/remove/${userId}`, {
       method:"PUT",
       headers: {
         "Content-Type": "application/json",
         "Authorization":`Bearer ${token}`
       }
     });
     console.log("token ----- ",token);
     console.log("chatid ----- ",chatId);
     console.log("userid ----- ",userId);
     const response = await res.json();
     console.log("Put remove member ----- ",response)
  dispatch({type:PUT_REMOVE_MEMBER, payload:response})
 }
