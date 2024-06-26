/* eslint-disable react-hooks/exhaustive-deps */
import React, { useEffect, useState } from "react";
import { BiCommentDetail } from "react-icons/bi";
import {
  BsThreeDotsVertical,
  BsFilter,
  BsMicFill,
  BsEmojiSmile,
} from "react-icons/bs";
import { TbCircleDashed, TbMessagePlus } from "react-icons/tb";
import { AiOutlineSearch } from "react-icons/ai";
import { ImAttachment } from "react-icons/im";
import UserChat from "./UserChat";
import Message from "./Message";
import { useDispatch, useSelector } from "react-redux";
import { currentUser, searchUser } from "../../Redux/Auth/Action";
import { PutSendInvite, createSingleChat, getAllChat, getAllPubliChat } from "../../Redux/Chat/Action";
import { createNewMessage, getAllMessage } from "../../Redux/Message/Action";
import { useNavigate } from "react-router-dom";
import io from "socket.io-client";
import "./Home.css";
import { useRef } from "react";
import Picker from "emoji-picker-react";
import Profile from "./Profile";
import EditGroup from "./EditGroup";
import { BsArrowLeft } from "react-icons/bs";
import SimpleSnackbar from "./SimpleSnackbar";
import DropDown from "../DropDown/DropDown";
import SockJS from "sockjs-client/dist/sockjs";
import { over } from "stompjs";
import CreateGroup from "../Group/CreateGroup";

let soket, selectedChatCompare;

const HomePage = () => {
  const dispatch = useDispatch();
  const { auth, chat, chatPubli, message } = useSelector((store) => store);
  const token = localStorage.getItem("token");
  const [querys, setQuerys] = useState("");
  const [content, setContent] = useState("");
  const [currentChat, setCurrentChat] = useState(null);
  const navigate = useNavigate();
  const [messages, setMessages] = useState([]);
  const [connected, setConnected] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const messageRef = useRef();
  const [isOpen, setIsOpen] = useState(false);
  const [Tab1, setTab] = useState(true);
  const [isProfile, setIsProfile] = useState(false);
  const [isEditGroup, setIsEditGroup] = useState(false);
  const [open, setOpen] = useState(false);
  const [openRemove, setOpenRemove] = useState(false);
  const [openExit, setOpenExit] = useState(false);
  const [openInvite, setOpenInvite] = useState(false);
  const [openAccept, setOpenAccept] = useState(false);
  const handleClose = () => setOpen(false);
  const [stompClient, setStompClient] = useState(null);
  const [isCreateGroup, setIsCreateGroup] = useState(false);

  //dispatch current user if user signup or login
  useEffect(() => {
    if (token) dispatch(currentUser(token));
  }, [token, auth.updatedUser]);

  //redirect to signup page if user not authenticate
  useEffect(() => {
    if (!auth.reqUser) navigate("/signup");
  }, [auth.reqUser]);

  const connect = () => {
    const sock = new SockJS("http://localhost:5454/ws");
    const temp = over(sock);
    setStompClient(temp);
    const headers = {
      Authorization: `Bearer ${token}`,
      "X-XSRF-TOKEN": getCookie("XSRF-TOKEN"),
    };
    temp.connect(headers, onConnect, onErr);
  };

  function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) {
      return parts.pop().split(";").shift();
    }
  }

  const onErr = (error) => {
    console.log("on Error", error);
  };

  const onConnect = () => {
    setConnected(true);

    // stompClient.subscribe("/topic/notification",onMessageRecive)
    console.log("------ ", stompClient);

    // stompClient.send("/app/notification",{},JSON.stringify(messages))
  };

  // setCurrentChat
  // useEffect(() => {
  //   if (chat.singleChat) {
  //     setCurrentChat(chat.singleChat);

  //   }
  // }, [chat.singleChat]);

  const handleCurrentChat = (item) => {
    setCurrentChat(item);

    // if (item.id === notifications[0]?.chat.id) {
    //   setNotifications([]);
    // }
    messageRef.current?.scrollIntoView({
      behavior: "smooth",
    });
  };

  //create new Single chat
  const createNewChat = (userId) => {
    const data = { token, userId };
    if (token) dispatch(createSingleChat(data));
  };

  //get all chats
  useEffect(() => {
    if (token) dispatch(getAllChat(token));
  }, [token, chat.singleChat,chat.createdGroup]);


  //get all groups
  useEffect(() => {
    if (token) dispatch(getAllPubliChat(token));
  }, [token]);

  //create new message
  const handleCreateNewMessage = () => {
    const data = { token, chatId: currentChat?.id, content };
    dispatch(createNewMessage(data));
    sendMessageToServer();
    messageRef.current?.scrollIntoView({
      behavior: "smooth",
    });
  };

  //get all message
  useEffect(() => {
    if (!currentChat?.id) return;
    dispatch(getAllMessage({ chatId: currentChat?.id, token }));
    selectedChatCompare = currentChat;
  }, [currentChat, message.newMessage]);

  //setMessage and send to server
  useEffect(() => {
    if (message.newMessage && stompClient) {
      setMessages([...messages, message.newMessage]);
      stompClient?.send("/app/message", {}, JSON.stringify(message.newMessage));
      messageRef.current?.scrollIntoView({
        behavior: "smooth",
      });
    }
  }, [message.newMessage]);

  useEffect(() => {
    if (message.messages) setMessages(message.messages);
  }, [message.messages]);

  //Search User
  const handleSearch = (keyword) => {
    dispatch(searchUser({ userId: auth.reqUser?.id, keyword, token }));
  };

  useEffect(() => {
    if (connected && stompClient && auth.reqUser && currentChat) {
      const subscription = stompClient.subscribe(
        `/user/${currentChat?.id}/private`,
        onMessageRecive
      );
      stompClient.subscribe(
        "/group/" + currentChat.id.toString(),
        onMessageRecive
      );
      return () => {
        subscription.unsubscribe();
      };
    }
  });

  const onMessageRecive = (payload) => {
    console.log("onMessageRecive ............. -----------", payload);

    console.log("recive message -  - - - - - - -  -", JSON.parse(payload.body));

    const recievedMessage = JSON.parse(payload.body);

    setMessages([...messages, recievedMessage]);
  };

  const sendMessageToServer = () => {
    if (stompClient) {
      const value = {
        content,
        chatId: currentChat?.id,
      };
      console.log("---- send message --- ", value);
      stompClient?.send(
        `/app/chat/${currentChat?.id.toString()}`,
        {},
        JSON.stringify(value)
      );
    }
  };

  const onEmojiClick = (event, emojiObject) => {
    setContent(content + " " + emojiObject.emoji);
  };

  const handleEmojiBoxClose = () => {
    setIsOpen(false);
  };

  const handleBack = () => setIsProfile(false);

  const handleBackEdit = () => setIsEditGroup(false);

  const handleBackRemove = () => { 
    setOpenRemove(true);
    setIsEditGroup(false)
  };

  const handleBackAccept = () => { 
    setOpenAccept(true);
    setIsEditGroup(false)
  };

  const handleInvite = (chatId , chat) => { 
    console.log("token--",{token});
    dispatch(PutSendInvite(token, chatId, auth.reqUser?.id));
    chat.pendingUsers.push(auth.reqUser);
    setOpenInvite(true);
  };

  useEffect(() => {
    setOpen(true);
    connect();
  }, []);

  
  const handleCreateGroup = () => {
    setIsCreateGroup(true);
  };

  const exitGroupHandle = () => {
    setIsEditGroup(false);
    chat.chats = chat.chats.filter(chat => chat.id !== currentChat.id);
    setCurrentChat(null);
    setOpenExit(true);
  };

  return (
    <div className="relative">
      <div className="py-12 bg-amber-400 w-full"></div>
      <div className="py-3 bg-black w-full"></div>

      <div className="absolute w-[97vw] h-[94vh] bg-white top-6 left-6 flex rounded-lg shadow-md border border-[#e5e7eb]">
        <div className="w-[30%] bg-white h-full rounded-l-lg border-r border-[#e5e7eb]">
          {isCreateGroup && (
            <div className="h-full">
              <CreateGroup
                setIsGoup={setIsCreateGroup}
                handleBack={() => setIsCreateGroup(false)}
              />{" "}
            </div>
          )}

          {isProfile && (
            <div className="h-full">
              <Profile handleBack={handleBack} />
            </div>
          )}

          {!isProfile && !isCreateGroup && (
            <div>
              <div className=" w-full ">
                {/* profile img and icons */}
                <div className="flex justify-between items-center  px-3 py-3">
                  <div className="flex items-center space-x-3">
                    <img
                      onClick={() => setIsProfile(true)}
                      className="rounded-full w-10 h-10 cursor-pointer"
                      src={
                        auth.reqUser?.profile_picture ||
                        "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460__340.png"
                      }
                      alt=""
                    />
                    <p>{auth.reqUser?.full_name}</p>
                  </div>
                  <div className="space-x-3 text-2xl flex">
                    <DropDown handleCreateGroup={handleCreateGroup} />
                  </div>
                </div>
                <div className="relative flex justify-center items-center bg-white py-4 px-3">
                  <input
                    onChange={(e) => {
                      setQuerys(e.target.value);
                      handleSearch(e.target.value);
                    }}
                    className="border-none outline-none py-2 bg-gray-100 rounded-md w-full pl-9"
                    type="text"
                    placeholder="Procure pessoas para conversar individualmente!"
                    value={querys}
                  />
                  <AiOutlineSearch className="absolute top-7 left-6" />
                </div>
              </div>

              {/* Tabs */}
              
              {Tab1 && (
              <div className="flex justify-evenly p-4">
                <div className="font-bold p-2 border-zinc-800 border-b-2 rounded-sm" onClick={() => setTab(true)}>
                    Meus Chats
                </div>
                <div className="p-2" onClick={() => setTab(false)}>
                    Chats Publicos
                </div>
              </div>
              )}
              {!Tab1 && (
              <div className="flex justify-evenly p-4" onClick={() => setTab(true)}>
                <div className="p-2">
                    Meus Chats
                </div>
                <div className="font-bold p-2 border-zinc-800 border-b-2 rounded-sm" onClick={() => setTab(false)}>
                    Chats Publicos
                </div>
              </div>
              )}

           
              {/* all chats user */}
              {Tab1 &&(
              <div className="bg-white overflow-y-scroll h-[440px]">
                {querys &&
                  auth.searchUser?.map((item, index) => (
                    <div
                      onClick={() => {
                        createNewChat(item?.id);
                        setQuerys("");
                      }}
                      key={index}
                    >
                      <hr />
                      <UserChat
                        isChat={false}
                        isGroup={false}
                        name={item.full_name}
                        userImg={
                          item.profile_picture ||
                          "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460__340.png"
                        }
                      />
                    </div>
                  ))}

                {!chat.chats?.error &&
                  chat?.chats?.map((item, index) => (
                    <div onClick={() => handleCurrentChat(item)} key={item.id}>
                      <hr />
                      {item.is_group ? (
                        <UserChat
                          name={item.chat_name}
                          isGroup={true}
                          userImg={
                            item.chat_image ||
                            "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460__340.png"
                          }
                        />
                      ) : (
                        <UserChat
                          isChat={true}
                          isGroup={false}
                          name={
                            auth.reqUser?.id !== item.users[0]?.id
                              ? item.users[0].full_name
                              : item.users[1].full_name
                          }
                          userImg={
                            auth.reqUser.id !== item.users[0].id
                              ? item.users[0].profile_picture ||
                                "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460__340.png"
                              : item.users[1].profile_picture ||
                                "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460__340.png"
                          }
                          notification={notifications.length}
                          isNotification={
                            notifications[0]?.chat?.id === item.id
                          }
                          message={
                            (item.id ===
                              messages[messages.length - 1]?.chat?.id &&
                              messages[messages.length - 1]?.content) ||
                            (item.id === notifications[0]?.chat?.id &&
                              notifications[0]?.content)
                          }
                        />
                      )}
                    </div>
                  ))}
              </div>
            )}

            {/* all groups */}

            {!Tab1 &&(
              <div className="bg-white overflow-y-scroll h-[440px]">
                {!chatPubli.chatPubli?.error &&
                  chatPubli.chatPubli?.map((item, index) => (
                    <div  key={index} onClick={()=>handleInvite(item.id , item)}>
                      {item.is_group  && item.users[0] != null && item.admins[0].id != auth.reqUser.id && item.admins[0] != null && item.users.find(user => user.id == auth.reqUser.id) == null &&(
                        <UserChat
                          name={item.chat_name}
                          isGroup={true}
                          SendInvite={item.pendingUsers.find(user => user.id == auth.reqUser.id)}
                          userImg={
                            item.chat_image ||
                            "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460__340.png"
                          }
                        />
                      )}
                    </div>
                  ))}
              </div>
            )}



            </div>
          )}
        </div>

        {!currentChat && (
          <div className="w-[70%] flex flex-col items-center justify-center">
            <div className="max-w-[70%] text-center flex flex-col items-center justify-center">
              <img
                src="https://i.ibb.co/XWwBS4L/removal-ai-93923e6b-9431-43a3-8c13-17095cfa42ef-whatsapp-image-2024-06-23-at-18-54-23.png"
                alt=""
              />
              <h1 className="text-4xl text-amber-400 drop-shadow-[0_1px_1px_rgba(5,5,5,5)]">Whats<strong className="text-4xl text-black">UT</strong></h1>
              <p className=" my-9">
              WhatsUT é uma plataforma de chat online que facilita a comunicação instantânea 
              entre usuários de forma prática e intuitiva.
              </p>
            </div>
          </div>
        )}

        {currentChat &&(
          <div className="w-[70%] bg-blue-100 relative">
            {/* header part */}
            <div className="header absolute top-0 w-full bg-[#f0f2f5]">
              <div className=" flex justify-between ">
                <div className="py-3 space-x-4 flex items-center px-3 bg" onClick={() => setIsEditGroup(true)}>
                  <img
                    className="w-10 h-10 rounded-full"
                    src={currentChat?.is_group? (currentChat?.chat_image || "https://i.ibb.co/XWwBS4L/removal-ai-93923e6b-9431-43a3-8c13-17095cfa42ef-whatsapp-image-2024-06-23-at-18-54-23.png"):
                      (auth.reqUser?.id !== currentChat?.users[0].id
                        ? currentChat?.users[0].profile_picture ||
                          "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460__340.png"
                        : currentChat?.users[1].profile_picture ||
                          "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460__340.png")
                    }
                    alt=""
                  />
                  <p>
                    {currentChat?.is_group? (currentChat?.chat_name):(auth.reqUser?.id !== currentChat?.users[0].id
                      ? currentChat?.users[0].full_name
                      : currentChat?.users[1].full_name)}
                  </p>
                </div>
              </div>
            </div>

            {/* message secition */}

            <div
              onClick={handleEmojiBoxClose}
              className="px-10   h-[85vh] overflow-y-scroll"
            >
              <div className=" space-y-1 flex flex-col justify-center mt-20 py-2">
                {messages.length > 0 &&
                  messages?.map((item, index) => (
                    <Message
                      username={item.user.full_name}
                      messageRef={messageRef}
                      key={item.id}
                      isReqUserMessage={item.user?.id !== auth.reqUser.id}
                      content={`${item.content}`}
                    />
                  ))}
              </div>
            </div>

            {/* footer send message part */}
            <div className="footer bg-[#f0f2f5] absolute bottom-0 w-full py-3 text-2xl">
              <div className="flex justify-between items-center px-5 relative ">
                <BsEmojiSmile
                  onClick={() => setIsOpen(!isOpen)}
                  className="cursor-pointer"
                />
                <ImAttachment />
                <div
                  className={`${
                    isOpen ? "block" : "hidden"
                  } absolute bottom-16`}
                >
                  <Picker onEmojiClick={onEmojiClick} />
                </div>

                <input
                  onChange={(e) => setContent(e.target.value)}
                  className="py-2 outline-none border-none bg-white pl-4 rounded-md w-[85%]"
                  placeholder="Mensagem..."
                  value={content}
                  onKeyPress={(e) => {
                    if (e.key === "Enter") {
                      handleCreateNewMessage();
                      setContent("");
                    }
                  }}
                />
              </div>
            </div>
          </div>
        )}

        {isEditGroup &&(
          <div className="absolute right-0 top-0 bottom-0 bg-white w-[30%]">
            <EditGroup handleBack={handleBackEdit}  chat = {currentChat} user={auth.reqUser} exitGroupHandle={exitGroupHandle} handleBackRemove={handleBackRemove} handleBackAccept={handleBackAccept}/>
          </div>
        )}
      </div>
      <SimpleSnackbar
        message={`Bem vindo ${auth.reqUser?.full_name}!`}
        open={open}
        handleClose={handleClose}
        type={"success"}
      />

      <SimpleSnackbar
        message={`Usuário removido com sucesso!`}
        open={openRemove}
        handleClose={()=>setOpenRemove(!openRemove)}
        type={"success"}
      />

      <SimpleSnackbar
        message={`Você saiu do grupo!`}
        open={openExit}
        handleClose={()=>setOpenExit(!openExit)}
        type={"success"}
      />

      <SimpleSnackbar
        message={`Pedido de entrada enviado para o administrador do grupo!`}
        open={openInvite}
        handleClose={()=>setOpenInvite(!openInvite)}
        type={"success"}
      />

      <SimpleSnackbar
        message={`Invite aceito!`}
        open={openAccept}
        handleClose={()=>setOpenAccept(!openAccept)}
        type={"success"}
      />
    </div>
  );
};

export default HomePage;
