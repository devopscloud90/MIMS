function ajaxFunction(URL,divobj)
{ 
	if (URL.indexOf("?") != -1) { URL = URL + "&date=" + new Date(); } else { URL = URL + "?date=" + new Date(); }
	//alert(URL);
	var xmlHttp;
	try{
		xmlHttp=new XMLHttpRequest();    
	}
	catch (e){    
		try{
			xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");      
		}
		catch (e){
		  try{
		      xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");        
		  }
		  catch (e){
		      alert("Your browser does not support AJAX!");        
		      return false;        
		  }      
		}    
	}
	if (xmlHttp==null)
	{
	  alert ("Your browser does not support AJAX!");
	  return;
	} 
	xmlHttp.onreadystatechange=function()
	{
	  if(xmlHttp.readyState==4)
	  {
		divobj.innerHTML=xmlHttp.responseText;
	  }
	  else{
		divobj.innerHTML="<FONT SIZE='1' COLOR='black' face=verdana></FONT>";
	  }
	}
	//alert(URL);
	xmlHttp.open("GET",URL,true);
	xmlHttp.send(null);  
}
function fnPopUp(strUrl,h,w)
{
	window.open(strUrl,"PopUp","menubar=no,toolbar=no,scrollbars=Yes,resizable=Yes,status=Yes,height="+h+",width="+w+",top=10").focus();
}
function chkStatus(CBox)
{
	return CBox.checked;
}
function ShowToolTip(DivID,width)
    {
        /* get the mouse left position */
        x = event.clientX + document.body.scrollLeft;
        /* get the mouse top position  */
        y = event.clientY + document.body.scrollTop + 10;
        /* display the pop-up */
        DivID.style.display="block";
        /* set the pop-up's left */
        DivID.style.left = x;
        /* set the pop-up's top */
        DivID.style.top = y;
		DivID.style.width =width;

		
    }

    /* this function hides the pop-up when
     user moves the mouse out of the link */
    function HideToolTip(DivID)
    {
        /* hide the pop-up */
        DivID.style.display="none";
    }