import { Delete, Post, Get, Patch } from "@/modules/AxiosHelper";
import { authorization } from "@/modules/Common";
import { ElMessage, ElNotification } from "element-plus";

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


export async function Login(account,password)
{
    const res = await Post("/User/Login",{
        account:account,
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
    const res = Delete(`/User/RemoveToken/${id}`,authorization);
    if (!res.successful) {
      ElMessage({
        message: res.message,
        type: "error",
      });
    }
    return res.successful;
}

export async function GetUsers(pageOption,queryKey,status,type)
{
    const res = await Get(
        `/User/GetUsers?page=${pageOption.current}&pageSize=${pageOption.size}&queryKey=${queryKey}&status=${status}&type=${type}`,authorization);
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

export async function ChangeStatus(status,id) {
    const res = await Patch(`/User/ChangeStatus/${id}?status=${status}`,{},authorization);
    if(!res.successful)
    {
        ElMessage({
            message:res.message,
            type:"error"
        });
        return false;
    }
    ElNotification({
      message: res.message,
      type: "success",
    });
    return true;
}