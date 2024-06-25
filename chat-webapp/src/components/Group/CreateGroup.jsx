import React, { useEffect, useState } from "react";
import { BASE_URL } from "../../Config/Api";
import {
  BsArrowLeft,
  BsArrowRight,
} from "react-icons/bs";
import UserChat from "../HomePage/UserChat";
import SelectedMember from "./SelectedMember";
import NewGroup from "./NewGroup";
import { useSelector } from "react-redux";

const CreateGroup = ({handleBack, setIsGoup}) => {
  const { auth } = useSelector((store) => store);

  const [groupMember, setGroupMember] = useState(new Set());
  const [users, setUsers] = useState([]);

  const token = localStorage.getItem("token");

  const [newGroup, setNewGroup] = useState(false);


  const handleRemoveMember = (item) => {
    const newGroupMember = new Set(groupMember);
    newGroupMember.delete(item);
    setGroupMember(newGroupMember);

    const newUsers = [...users, item];
    setUsers(newUsers);
  };

  const handleAddMember = (item) => {
    const newGroupMember = new Set(groupMember);
    newGroupMember.add(item);
    setGroupMember(newGroupMember);
  
    const newUsers = users.filter(user => user.id !== item.id);
    setUsers(newUsers);
  };


  useEffect(() => {
    const fetchUsers = async () => {
      const response = await fetch(`${BASE_URL}/users/all`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
      const data = await response.json();

      const filteredUsers = data.filter(user => user.id !== auth.reqUser.id);
      setUsers(filteredUsers);
    };

    fetchUsers();
  }, [token, auth.reqUser.id]);


  return (
    <div className="w-full h-full">
      {!newGroup && (
        <div>
          <div className="flex items-center space-x-3 bg-amber-400 text-white pt-6 px-10 pb-5 border-b-4 border-black">
            <BsArrowLeft
              onClick={handleBack}
              className="cursor-pointer text-2xl font-bold"
            />
            <p className="text-xl font-semibold">Adicionar participantes do grupo</p>
          </div>

          <div className="relative  bg-white py-4 px-3">
            <div className="flex space-x-2 flex-wrap space-y-1">
              {groupMember.size > 0 &&
                Array.from(groupMember).map((item) => (
                  <SelectedMember
                    handleRemoveMember={() => handleRemoveMember(item)}
                    member={item}
                  />
                ))}
            </div>
          </div>

          <div className="bg-white overflow-y-scroll h-[50.2vh]">
            {users &&
              users?.map((item) => (
                <div
                  onClick={() => handleAddMember(item)}
                  key={item?.id}
                >
                  <hr />
                  <UserChat
                    isChat={false}
                    name={item.full_name}
                    userImg={
                      item.profile_picture ||
                      "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460__340.png"
                    }
                  />
                </div>
              ))}
          </div>

          <div className="bottom-10 py-10  flex items-center justify-center">
            <div
              onClick={() => {
                setNewGroup(true);
              }}
              className="bg-amber-400 rounded-full p-4 cursor-pointer"
            >
              <BsArrowRight className="text-white font-bold text-3xl" />
            </div>
          </div>
        </div>
      )}

      {newGroup && (
        <div>
          <NewGroup groupMember={groupMember} setIsCreateGroup={setIsGoup} handleBack={
            () => setNewGroup(false)}/>
        </div>
      )}
    </div>
  );
};

export default CreateGroup;
