export const getElement=selector=>document.querySelector(selector);
export const show=Element=>Element.style.display="block";
export const hide=Element=>Element.style.display="none";
export const flexShow=Element=>Element.style.display="flex";
export const getElements=selector=>document.querySelectorAll(selector);
export const setBackground=(Element,Background)=>Element.style.background=Background;
export const switchPage=url=>window.location.replace(url);
export const divePage=url=>window.location.assign(url);
export const freshPage=()=>window.location.reload();
export const closePage=()=>window.close();
export const openPage=url=>window.open(url);
export const goBack=()=>history.back();
export const setOpacity=(Element,opacity)=>Element.style.opacity=opacity;
export const setCursor=(Element,Cursor)=>Element.style.cursor=Cursor;
export const setForeColor=(Element,Color)=>Element.style.color=Color;
export const virtualHide=Element=>Element.style.visibility="hidden";
export const virtualShow=Element=>Element.style.visibility="visible";
export const setMargin=(Element,left,right,top,bottom,units)=>{
    if(left!=null)
    Element.style.marginLeft=left+units[0];
    if(right!=null)
    Element.style.marginRight=right+units[1];
    if(top!=null)
    Element.style.marginTop=top+units[2];
    if(bottom!=null)
    Element.style.marginBottom=bottom+units[3];
}
export const setMarginLeft=(Element,Left,unit)=>Element.style.marginLeft=Left+unit;
export const setMarginRight=(Element,Right,unit)=>Element.style.marginRight=Right+unit;
export const setMarginTop = (Element, Top, unit) =>
  (Element.style.marginTop =Top + unit);
  export const setMarginBottom = (Element, Bottom, unit) =>
    (Element.style.marginBottom = Bottom + unit);