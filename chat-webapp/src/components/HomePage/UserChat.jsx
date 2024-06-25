import React from "react";
import { AiOutlineDown } from "react-icons/ai";
import { BsPeople } from "react-icons/bs";

const UserChat = ({
  name,
  userImg,
  sentTime,
  isChat,
  message,
  isGroup,
  notification,
  isNotification,
}) => {
  return (
    <div className="flex items-center justify-center py-4 group cursor-pointer bg-white hover:bg-amber-200 hover:border-b-2 hover:border-zinc-800">
      <div className="w=[20%]">
        <img className="h-10 w-10 rounded-full" src={userImg} alt="" />
      </div>
      <div className="pl-5 w-[80%]">
        <div className="flex justify-between items-center">
          <p className="text-lg">{name}</p>
          {isChat && <p className="text-sm">{sentTime}</p>}
        </div>
        <div className="flex justify-between items-center">
           <p className={`${isChat && message?.length>0?"visible":"invisible"}`}>{message?.length>15? message.slice(0,15)+"...":message}</p>
          <div className="flex space-x-2 items-center">
            {isChat && isNotification && notification > 0 && (
              <p className="text-xs p-1 px-2 text-white bg-green-500 rounded-full">
                {notification}
              </p>
            )}         
          </div>
        </div>
      </div>
      <div className="w-3">
        {isGroup &&(
          <BsPeople />
        )}
      </div>
    </div>
  );
};

export default UserChat;
