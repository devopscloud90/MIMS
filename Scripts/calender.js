function fnCalendar(e) 
{
    var temp;
    var presentvalue="";
     temp = window.showModalDialog("./Calendar.html",presentvalue,"dialogHeight:13;dialogWidth:8;center:yes;toolbar:false;status:0;scroll:0;unadorned:0;help:no");
     if(temp!=null)
     {
       e.parentElement.children(0).value =temp;
     }
}
function fnEnableCalLookup(e)
{
 e.src = "./Images/CalEn.gif";
}
function fnDisableCalLookup(e)
{
 e.src = "./Images/CalDis.gif";
}