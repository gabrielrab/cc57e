import React from "react";
import { useState } from "react";
import { BsArrowLeft, BsCheck2, BsPencil, BsTrash, BsX } from "react-icons/bs";
import { useDispatch, useSelector } from "react-redux";
import { currentUser, updateUser } from "../../Redux/Auth/Action";
import SimpleSnackbar from "./SimpleSnackbar";
import { PutAcceptInvite, PutRemoveMember } from "../../Redux/Chat/Action";

const EditGroup = ({ handleBack, chat, user , exitGroupHandle, handleBackRemove, handleBackAccept }) => {
  const { auth } = useSelector((store) => store);
  const dispatch = useDispatch();
  const token = localStorage.getItem("token");
  const [groupName] = useState(chat.chat_name);
  const [groupImage] = useState(chat.chat_image);

  const handleRemoveMember = (userId) => {
    dispatch(PutRemoveMember(token, chat.id, userId));
    chat.users = chat.users.filter(users => users.id !== userId);
    handleBackRemove();
  };

  const handleAcceptInvite = (userId) => {
    dispatch(PutAcceptInvite(token, chat.id, userId));
    chat.pendingUsers = chat.pendingUsers.filter(users => users.id !== userId);
    handleBackAccept();
  };

  const exitGroup = () => {
    dispatch(PutRemoveMember(token, chat.id, auth.reqUser?.id));
    chat.users = chat.users.filter(users => users.id !== auth.reqUser?.id);
    exitGroupHandle();
  };

  return (
    <div className=" w-full h-full overflow-auto">
      <div className=" flex items-center space-x-3 bg-amber-400 text-white pt-6 px-10 pb-5 border-b-4 border-black">
        <BsArrowLeft
          onClick={handleBack}
          className="cursor-pointer text-2xl font-bold mt-1"
        />
        <p className="text-xl font-semibold">Editar Grupo</p>
      </div>

      <div className="flex flex-col justify-center items-center my-12">
        <label className="relative " htmlFor="imgInput">
          <img
            className="rounded-full w-[15vw] h-[15vw]"
            src={
              groupImage ||
              "https://cdn.pixabay.com/photo/2016/04/15/18/05/computer-1331579__340.png"
            }
            alt=""
          />
        </label>
      </div>

      <div className="w-full flex flex-col justify-between items-center py-2 px-5">
        <p className="w-full p-3 border-b-2 border-zinc-800 text-left font-bold">
          Nome do grupo
        </p>
        <p className="w-full outline-none border-b border-zinc-800 px-2  py-2 bg-transparent">
          {groupName}
        </p>
      </div>

      <div className="w-full flex flex-col justify-between items-center py-3 px-5">
        <p className="w-full p-3 border-b-2 border-zinc-800 text-left font-bold">
          Membros
        </p>
        <div className="w-full h-30 overflow-auto">
          {chat.users?.map((item, index) => (
            <div className="w-full p-2 flex justify-between items-center border-b border-zinc-800">
              {item.full_name}
              {item.id != user.id &&
                user.id == chat.admins[0].id && (
                  <div className="cursor-pointer" onClick={()=> handleRemoveMember(item.id, index)}>
                    <BsTrash />
                  </div>
                )}
            </div>
          ))}
        </div>
      </div>
      {user.full_name == chat.created_by.full_name && (
        <div className="w-full flex flex-col justify-between items-center py-3 px-5">
          <p className="w-full p-3 border-b-2 border-zinc-800 text-left font-bold">
            Pedidos para entrar
          </p>
          <div className="w-full h-30 overflow-auto">
            {chat.pendingUsers?.map((item, index) => (
              <div className="w-full p-2 flex justify-between items-center border-b border-zinc-800">
                {item.full_name}

                <div className="w-fit flex justify-between items-center">
                  <div className="p-2 cursor-pointer" onClick={()=> handleAcceptInvite(item.id)}>
                    <BsCheck2 />
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      <div className="flex justify-center">
        <p className=" cursor-pointer p-2 underline text-red-700 font-bold" onClick={()=>exitGroup()}>
          Sair do grupo X
        </p>

      </div>

    </div>
  );
};

export default EditGroup;
