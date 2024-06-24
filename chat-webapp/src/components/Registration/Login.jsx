/* eslint-disable react-hooks/exhaustive-deps */
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { currentUser, login } from "../../Redux/Auth/Action";
import SimpleSnackbar from "../HomePage/SimpleSnackbar";

const Login = () => {
  const [inputData, setInputData] = useState({
    email: "",
    password: "",
  });
  const [open, setOpen] = useState(false);

 const navigate = useNavigate();
 const dispatch = useDispatch();
 const {auth}=useSelector((store)=>store)
  const token = localStorage.getItem("token");

  console.log("auth",auth)
  const handleChange = (e) => {
    const { name, value } = e.target;
    setInputData((values) => ({ ...values, [name]: value }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    dispatch(login(inputData))
  };
  
 const handleClose = () => setOpen(false)

 //dispatch current user if user already signup
 useEffect(() => {
  
  if (token) dispatch(currentUser(token))
  
 }, [token])
 

 //redirect to main page if register success
 useEffect(() => {
   if (auth.reqUser) {
    
    navigate("/")
  }
 }, [auth.reqUser])
  
  useEffect(() => {
    if (auth.login?.error) {
      setOpen(true)
    }
  },[auth.login])

  return (
    <div>
      <div className="flex justify-center min-h-screen items-center flex-col">
      <div className="flex justify-center items-center">
        <img
          src="https://i.ibb.co/XWwBS4L/removal-ai-93923e6b-9431-43a3-8c13-17095cfa42ef-whatsapp-image-2024-06-23-at-18-54-23.png"
          alt=""
          className="w-[20%]"
        />
        <h1 className="text-4xl text-amber-400 drop-shadow-[0_1px_1px_rgba(5,5,5,5)]">Whats<strong className="text-4xl text-black">UT</strong></h1>
      </div>
      
      <div className="w-[30%] p-10  shadow-lg bg-white rounded-lg">
        <form onSubmit={handleSubmit} className="space-y-5">
          <div>
            <p className="mb-2 text-zinc-800">Email</p>
            <input
              className="py-2 px-3 outline outline-2 outline-amber-400 w-full rounded-md border-1"
              type="text"
              placeholder="Digite seu Email"
              name="email"
              onChange={(e) => handleChange(e)}
              value={inputData.email}
            />
          </div>
          <div>
            <p className="mb-2 text-zinc-800">Senha</p>
            <input
              className="py-2 px-2 outline outline-2 outline-amber-400 w-full rounded-md border-1"
              type="password"
              placeholder="Digite sua Password"
              name="password"
              onChange={(e) => handleChange(e)}
              value={inputData.password}
            />
          </div>
          <div>
            <input
              className="py-[0.7rem] px-3 w-full rounded-md bg-amber-400 text-zinc-800 font-bold text-white mt-3"
              type="Submit"
              placeholder="Digite sua Password"
              value={"Entrar"}
              readOnly
            />
          </div>
        </form>
        <div className="flex space-x-1 item-center mt-5">
          <p className="">É novo na plataforma?</p>
          <p
            onClick={() => navigate("/Signup")}
            className="text-blue-500 hover:text-blue-800 cursor-pointer"
          >
            clique aqui
          </p>
        </div>
      </div>
     
      </div>
      <SimpleSnackbar
        message={auth.login?.error}
        open={open}
        handleClose={handleClose}
        type={"error"}
      />
    </div>
    
  );
};

export default Login;
