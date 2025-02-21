import axios from "axios";

const baseUrl = "http://localhost:5325/api";

axios.defaults.baseURL = baseUrl;

export function imgSrc(sourceName)
{
  return `${baseUrl}/image/${sourceName}`;
}

export function videoSrc(sourceName)
{
  return `${baseUrl}/video/${sourceName}`;
}

export async function Get(url, config) {
  return (await axios.get(url, config)).data;
}
export async function Post(url,data,config){
    return (await axios.post(url,data,config)).data;
}
export async function Put(url,data,config){
  return  (await axios.put(url,data,config)).data;
}
export async function Delete(url,config)
{
  return (await axios.delete(url, config)).data;
}
export async function Patch(url,data,config){
  return  (await axios.patch(url,data,config)).data;
}
export async function Head(url,config){
  return (await axios.head(url,config)).data;
}


/*class AxiosHelper{
  constructor(){
  }
   Get(url,config){
    return  Get(url,config);
  }
  Post(url,data,config){
    return  Post(url,data,config);
  }
   Put(url,data,config)
  {
    return  Put(url,data,config);
  }
   Delete(url,config){
    return  Delete(url,config);
  }
  Patch(url,data,config){
    return  Patch(url,data,config);
  }
  Head(url,config){
    return Head(url,config);
  }
}
const axiosHelper=new AxiosHelper();
export default axiosHelper;*/