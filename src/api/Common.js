import { Post } from "@/modules/AxiosHelper";
import { formDataAuth } from "@/modules/Common";
import { ElMessage, ElNotification } from "element-plus";

const chunkSize = 1024*1024*10;

export const GetTotalChunkedSize=(size)=>Math.ceil(size/chunkSize);
export const IsBigFile = file=>file.size>3*chunkSize;
export function ChunkeFile(index,end,file)
{
    if(index<end)
    {
      return file.slice((index-1)*chunkSize,index*chunkSize);
    }
    else{
        return file.slice((end-1)*chunkSize);
    }

}

export const FileType={
    UserAvatar:1,
    VideoCover:2,
    ShortVideo:3,
    LongVideo:4
};

export async function UploadFile(type,file,originalFileName)
{
    const data = new FormData();
    data.append("type",type);
    data.append("file",file);
    data.append("originalFileName",originalFileName);

    const res = await Post("/Common/Upload",data,formDataAuth);
    if(!res.successful)
    {
        ElMessage({
            message:res.message,
            type:"error"
        });
        return null;
    }
    ElNotification({
        message:res.message,
        type:"success"
    });
    return res.data;
}

