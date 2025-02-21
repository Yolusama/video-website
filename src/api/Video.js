import { Delete, Get, Post, Put } from "@/modules/AxiosHelper";
import { authorization, copy } from "@/modules/Common";
import { ElMessage,ElNotification } from "element-plus";

export async function GetVideos(pagination,title,type,year)
{
    const res = await Get(`/Video/GetVideoInfos?page=${pagination.current}&pageSize=${pagination.size}&title=${title}&type=${type}&publishYear=${year}`,authorization);
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

export async function GetVideo(kvId) {
    const res = await Get(`/Video/GetVideo/${kvId}`,authorization);
    return res;
}

export async function GetCurrentTime(userId,videoId) {
    const res = await Post("/Video/GetCurrentTime",{
        userId:userId,
        videoId:videoId
    },authorization);
    if (!res.successful&&res.code!=404) {
      ElMessage({
        message: res.message,
        type: "error",
      });
      return null;
    }
    return res.data;
}

export async function SaveVideoRecord(videoRecord) {
    const res = await Put("/Video/SaveVideoRecord",videoRecord,authorization);
    if (!res.successful) {
      ElMessage({
        message: res.message,
        type: "error",
      });
      return false;
    }
    return true;
}

export async function DownloadVideo(videoName) {
  const auth = {};
  copy(authorization,auth);
  auth["responseType"] = "blob";
  const res = await Get(
    `/Common/DownloadVideo?videoName=${videoName}`,
    auth
  );
  if(res==null){
    ElMessage({
      message:res.message,
      type:"error"
    });
    return null;
  }
  ElNotification({
    message: "开始下载文件...",
    type: "success",
  });
  const blob = new Blob([res]);
  const href = window.URL.createObjectURL(blob);
  return href;
}

export async function GetVideoRecord(pagination,userId) {
  const res = await Get(`/Video/GetVideoRecord/${userId}?page=${pagination.current}&pageSize=${pagination.size}`,authorization);
  if(!res.successful)
  {
       ElMessage({
         message: res.message,
         type: "error",
       });
       return null;
  }
  return res.data;
}

export async function RemoveRecord(userId,videoId) {
  const res = await Put("/Video/RemoveRecord",{
    userId:userId,
    videoId:videoId
  },authorization);
  if(!res.successful){
    ElMessage({
      message: res.message,
      type: "error",
    });
    return false;
  }
  return true;
}

export async function ClearRecord(userId) {
  const res = await Delete(
    `/Video/ClearRecord/${userId}`,
    authorization
  );
  if (!res.successful) {
    ElMessage({
      message: res.message,
      type: "error",
    });
    return false;
  }
  return true;
}