import stateStroge from "./StateStorage";
import { switchPage } from "./domHelper";
import { ElLoading } from "element-plus";

function MakeAuthorization(useCookie,contentType,token="")
{
     const authorization = {
        headers:{
            token : ""
        }
     };
     if(useCookie)
       {
        authorization['withCredentials'] = true;
       }
      authorization.headers.token = token; 
      authorization.headers['ContentType'] = contentType;
    return authorization;
}

export const authorization = MakeAuthorization(false,"application/json");
export const formDataAuth = MakeAuthorization(false,"multipart/form-data");
export const makeAuthorization = (token,contentType) => {
       return MakeAuthorization(false,contentType,token);
};

authorization.headers.token = stateStroge.get("token");
formDataAuth.headers.token = stateStroge.get("token");

export function copy(src,to)
{
  for(let pro in src)
    to[pro] = src[pro];
}

export function DelayRunning(func,expire)
{
  const timer = setTimeout(()=>{
    func();
    clearInterval(timer);
  },expire);
}

export const defalutLodingColor = "rgba(235,235,236,.75)";

export function LoadingOperate(
  fullscreen,
  title,
  backgroundColor,
  func,
  expire,
  selector="body"
) {
  const instance = ElLoading.service({
    fullscreen: fullscreen,
    target: selector,
    background: backgroundColor,
    text: title,
  });
  const timer = setTimeout(() => {
    instance.close();
    func();
    clearTimeout(timer);
  }, expire);
}

export function BeforeRouteLeave(to,from,next,notPrevented)
{
 if (notPrevented) {
   next();
 }
 if (to.redirectedFrom != undefined) {
   switchPage("#" + from.fullPath);
 }
 next(from);
}

export function PageOption(current,size,total,sizes)
{
  this.current = current;
  this.size = size;
  this.total = total;
  this.maxPage =function(){
    return Math.ceil(this.total/this.size);
  };
  this.sizes = sizes;
}

export function previewOpenFile(file,func)
{
  if (file) {
    var reader = new FileReader();

    reader.onload = function (e) {
      func(e.target.result);
    };
    reader.readAsDataURL(file);
  } 
}

export function percentage(current,total)
{
    return current>=total ? 100 : ((current/total).toPrecision(2))*100;
}

export function getFileSuffix(fileName)
{
  return fileName.substring(fileName.lastIndexOf('.')+1);
}

export const DefaultVideoCover = "default-cover.png";
export const DefaultAvatar = "default.png";