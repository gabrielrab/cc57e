import React from "react";

const Message = ({ username,isReqUserMessage, content, messageRef}) => {
  return (

    <div
    ref={messageRef}
        className={` max-w-[50%] mt-2 ${
          isReqUserMessage ? "self-start  " : "self-end"
        }`}
    >
      <p ref={messageRef}
        className={`font-bold mb-1 ${
          isReqUserMessage ? "text-left  " : "text-right"
        }`}>{username}</p>
      <div
      ref={messageRef}
      className={` py-2 px-2 rounded-md w-fit ${
        isReqUserMessage ? " bg-white text-left" : " bg-amber-100 text-right"
      }`}
      >

      <p>{content} </p>
      </div>
    </div>
  );
};

export default Message;
