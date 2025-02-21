import { Delete, Get, Post, Put } from "@/modules/AxiosHelper";
import { authorization } from "@/modules/Common";
import { ElMessage, ElNotification} from "element-plus";

const userType = [];
userType[2] = "普通用户";
userType[3] = "VIP用户";
userType[4] = "管理员";
const _userType = {};
_userType['2'] = userType[2];
_userType['3'] = userType[3];
_userType['4'] = userType[4];


export function getUserType(type)
{
   return userType[type];
}

export function UserType()
{ 
    return _userType;
}

export async function Login(email,password)
{
    const res = await Post("/User/EmailLogin",{
        email:email,
        password:password
    });
    if(!res.successful)
    {
        ElMessage({
            message:res.message,
            type:"error"
        });
        return null;
    }
    return res.data;
}

export async function RemoveToken(id)
{
    const res =await Delete(`/User/RemoveToken/${id}`,authorization);
    if (!res.successful) {
      ElMessage({
        message: res.message,
        type: "error",
      });
    }
    return res.successful;
}

export async function AskForCheckCode(email,length) {
    const res = await Get(`/User/AskForCheckCode/${length}?email=${email}`);
    const successful = res.successful;
    if(!successful)
      ElMessage({
        message: res.message,
        type:"error",
      });
    else
       ElNotification({
        message:res.message,
        type:"success"
      });
    return successful;  
}

export async function IdentifyLogin(email,checkCode) {
    const res = await Post(`/User/IdentifyLogin/${checkCode}?email=${email}`);
    if (!res.successful) {
      ElMessage({
        message: res.message,
        type: "error",
      });
      return null;
    }
    return res.data;
}

export async function Register(checkCode,email)
{
    const res = await Put(`/User/Register/${checkCode}?email=${email}`);
    if (!res.successful) {
      ElMessage({
        message: res.message,
        type: "error",
      });
      return false;
    }
    ElMessage({
        message:res.message,
        type:"success"
    });
    return true;
}

export async function VerifyToken(id,token) {
  const res = await Post("/User/VerifyToken",{
    id:id,
    token:token
  });
  if(!res.successful){
    ElMessage({
      message:res.message,
      type:"error"
    });
    return null;
  }
  return res.data;
}

export async function GetUser(id) {
  const res = await Get(`/User/GetUser/${id}`,authorization);
  if (!res.successful) {
    ElMessage({
      message: res.message,
      type: "error",
    });
    return null;
  }
  return res.data;
}