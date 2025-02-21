import { Get, Post, Put, Patch } from "@/modules/AxiosHelper";
import { authorization, formDataAuth } from "@/modules/Common";
import { ElMessage, ElNotification } from "element-plus";


export async function SaveVideoInfo(video)
{
    const res = await Put("/Video/SaveVideoInfo",video,authorization);
    if(!res.successful)
    {
        ElMessage({
            message:res.message,
            type:"error"
        });
        return null;
    }
    return {
        id:res.data
    };
}

export async function UploadLongVideo(current,filePart,end,fileName,fileSuffix,videoTitle)
{
    const data = new FormData();
    data.append("current",current);
    data.append("filePart",filePart);
    data.append("end",end);
    data.append("fileName",fileName);
    data.append("fileSuffix",fileSuffix);
    data.append("videoTitle",videoTitle);

    const res = await Post("/Video/Upload",data,formDataAuth);
    if(!res.successful)
    {
        ElMessage({
            message:res.message,
            type:"error"
        });
        return {
            ok:false
        }
    }
    return {
        name:res.data,
        ok:true
    };
}

export async function GetVideos(pagination,title,type,status,year)
{
    const res = await Get(`/Video/GetVideos?page=${pagination.current}&pageSize=${pagination.size}&title=${title}&type=${type}&status=${status}&year=${year}`,authorization);
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

export async function ChangeStatus(status, id) {
  const res = await Patch(
    `/Video/ChangeStatus/${id}?status=${status}`,
    {},
    authorization
  );
  if (!res.successful) {
    ElMessage({
      message: res.message,
      type: "error",
    });
    return false;
  }
   ElNotification({
     message: res.message,
     type: "success",
   });
  return true;
}