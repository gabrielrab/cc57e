import React, { useState } from "react";
import { BsArrowLeft, BsCheck2 } from "react-icons/bs";
import { useDispatch } from "react-redux";
import { createGroupChat } from "../../Redux/Chat/Action";
import { Button, CircularProgress } from "@mui/material";

const NewGroup = ({ handleBack, groupMember,setIsCreateGroup }) => {
  const dispatch = useDispatch();
  const token = localStorage.getItem("token");
  const [groupName, setGroupName] = useState("");
  const [groupImage, setgroupImage] = useState(null);
  const [isImageUploading, setIsImageUploading] = useState(false);
  const [leaveAction, setLeaveAction] = useState(0);

  const handleCreateGroup = () => {
    let userIds = [];
    
    for (let user of groupMember) {
      userIds.push(user.id);
    }
    const group = {
      userIds,
      chat_name: groupName,
      chat_image: groupImage,
      leave_strategy: leaveAction,
    };
    
    const data = {
      group,
      token,
    };

    dispatch(createGroupChat(data));
    setIsCreateGroup(false)
  };

  return (
    <div className=" w-full h-full">
      <div className=" flex items-center space-x-3 bg-amber-400 text-white pt-6 px-10 pb-5 border-b-4 border-black">
        <BsArrowLeft
          onClick={handleBack}
          className="cursor-pointer text-2xl font-bold"
        />
        <p className="text-xl font-semibold">Novo Grupo</p>
      </div>

      <div className="flex flex-col justify-center items-center my-12">
        <label className="relative " htmlFor="imgInput">
          <img
            className="rounded-full w-[15vw] h-[15vw] cursor-pointer"
            src={
              groupImage ||
              "https://cdn.pixabay.com/photo/2016/04/15/18/05/computer-1331579__340.png"
            }
            alt=""
          />
          {isImageUploading && <CircularProgress className="absolute top-[5rem] left-[6rem]"/>}
        </label>

        <input
          type="file"
          id="imgInput"
          className="hidden"
          onChange={(e) => {
            const uploadPic = (pics) => {
              setIsImageUploading(true)
              const data = new FormData();
              data.append("file", pics);
              data.append("upload_preset", "ashok21");
              data.append("cloud_name", "zarmariya");
              fetch("https://api.cloudinary.com/v1_1/zarmariya/image/upload", {
                method: "post",
                body: data,
              })
                .then((res) => res.json())
                .then((data) => {
                  setgroupImage(data.url.toString());
                  
                });
                setIsImageUploading(false)
            };
            if (!e.target.files) return;

            uploadPic(e.target.files[0]);
          }}
        />
      </div>

      <div className="w-full flex flex-col justify-between items-center py-2 px-5">
        <input
          onChange={(e) => setGroupName(e.target.value)}
          className="w-full outline-none border-b-2 border-green-700 px-2  py-2 bg-transparent"
          type="text"
          placeholder="Nome do Grupo"
          value={groupName}
        />
        <div className="py-4 flex flex-col">
          <label className="inline-flex items-center">
            <input
              type="radio"
              className="form-radio"
              name="leaveAction"
              value="chooseAdmin"
              checked={leaveAction === 0}
              onChange={() => setLeaveAction(0)}
            />
            <span className="ml-2">Ao sair, escolher um administrador aleatório</span>
          </label>
          <label className="inline-flex items-center">
            <input
              type="radio"
              className="form-radio"
              name="leaveAction"
              value="deleteGroup"
              checked={leaveAction === 1}
              onChange={() => setLeaveAction(1)}
            />
            <span className="ml-2">Ao sair, deletar grupo</span>
          </label>
        </div>
      </div>
      {groupName && <div className=" py-10 bg-white flex items-center justify-center">
        <Button onClick={handleCreateGroup} variant="text">
          <div className="bg-amber-400 rounded-full p-4 ">
            <BsCheck2 className="text-white font-bold text-3xl" />
          </div>
        </Button>
      </div>}

      
    </div>
  );
};

export default NewGroup;
