function validateEmail()
{
 var emil;
 var validDomains = ["gmail.com", "yahoo.co.in", "yahoo.in", "yahoo.co.UK", "ymail.com", "rocketmail.com", "live.com", "hotmail.com", "msn.com", "outlook.com", "live.in", "yahoo.com"];
 var isValid=0;
email = $('#email').val();
email = email.replace(/^\s+|\s+$/gm, '');
if (email == "")
	{
	alert("please enter email.");
	applyErrors("email", "please enter email");
	isValid = 1;
	return false;
	}
	else if (validDomains.indexOf(email.split("@")[1]) == -1)
	{
		document.getElementById("email").className = "error error2";
		alert("We only accept popular email services Gmail, Yahoo, Hotmail,Ymail, MSN, Outlook &amp; Live.com.");
		isValid = 1;
		return false;
	}
	else if (!emailCheck(email))
	{
		isValid = 1;
		return false;
	}
}
function applyErrors(id, msg) 
{
	if (id == "email") 
	{
		document.getElementById(id + "Div").className = "error error2";
	}
}
function shwError(ele) 
{
	if  (ele != document.activeElement)
		{
		 id = ele.id;
		 if (id == "email")
			 {
			   var email = $("#email").val();
               if (email == "")
				   {
                     alert("please enter email.");
                     applyErrors("email", "please enter email");
                     isValid = 1;
                     return false;
                   } 
				else if (validDomains.indexOf(email.split("@")[1]) == -1)
					{
                       document.getElementById("emailDiv").className = "error error2";
                       alert("We only accept popular email services Gmail, Yahoo, Hotmail, Ymail, MSN, Outlook &amp; Live.com.");
                        // document.getElementById("emailMsg").innerHTML="We only accept popular email services Gmail, Yahoo, Hotmail, Rediffmail, In.com, Ymail, MSN, Outlook, AOL &amp; Live.com";
                       isValid = 1;
                        return false;
                    } 
			  else {
                   removeErrors(id);
                   }
            }
		}
}
var validDomains = ["gmail.com", "yahoo.co.in", "yahoo.in", "yahoo.co.UK", "ymail.com", "rocketmail.com", "live.com", "hotmail.com", "msn.com", "outlook.com", "live.in", "yahoo.com"];
function emailCheck(emailStr)
{
	var checkTLD = 1;
   var knownDomsPat = /^(com|net|org|edu|int|mil|gov|arpa|biz|aero|name|coop|info|pro|museum)$/;
        var emailPat = /^(.+)@(.+)$/;
        var specialChars = "\\(\\)&gt;&lt;@,;:\\\\\\\"\\.\\[\\]";
       var validChars = "\[^\\s" + specialChars + "\]";
        var quotedUser = "(\"[^\"]*\")";
        var ipDomainPat = /^\[(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})\]$/;
        var atom = validChars + '+';
        var word = "(" + atom + "|" + quotedUser + ")";
        var userPat = new RegExp("^" + word + "(\\." + word + ")*$");
        var domainPat = new RegExp("^" + atom + "(\\." + atom + ")*$");
        var matchArray = emailStr.match(emailPat);
        if (matchArray == null) {
            alert("Email address seems incorrect (check @ and .'s).");
            applyErrors("email", "Email address seems incorrect (check @ and .'s)");
            isValid = 1;
            return false;
         }
        var user = matchArray[1];
        var domain = matchArray[2];
          if (user.length &lt; 4 || user.length &gt; 32) {
            alert("Email address seems incorrect (Check length of Email).");
         applyErrors("email", "Email address seems incorrect (Check length of Email)");
          isValid = 1;
            return false;
        }
      var iChars = "!@#$%^&amp;*()+=-[]\\\';,/{}|\":&lt;&gt;?~";
      var iChars1 = "!@#$%^&amp;*()+=-[]\\\';,/{}|\":&lt;&gt;?~_";
      var iChars2 = "!@#$%^&amp;*()+=-[]\\\';,/{}|\":&lt;&gt;?~";
      var iChars3 = "!@#$%^&amp;*()+=[]\\\';,/{}|\":&lt;&gt;?~";
    for (i = 0; i &lt; user.length; i++) {
           if (user.charCodeAt(i) &gt; 127) {
               alert("Ths Email contains invalid characters.");
              applyErrors("email", "Ths Email contains invalid characters.");
                isValid = 1;
                return false;
            }
             if (domain.indexOf("yahoo") != -1 || domain.indexOf("ymail") != -1 || domain.indexOf("rocketmail") != -1) {
                if (iChars.indexOf(user.charAt(i)) != -1) {
                    alert("Email address seems incorrect (Yahoo Mail Id contains characters,numbers,dot and _).");
                    applyErrors("email", "Email address seems incorrect (Yahoo Mail Id contains characters,numbers,dot and _)");
                   isValid = 1;
                   return false;
                }
            }
              if (domain.indexOf("gmail") != -1) {
             if (iChars1.indexOf(user.charAt(i)) != -1) {
              alert("Email address seems incorrect (Gmail Id contains characters,numbers and dot).");
         applyErrors("email", "Email address seems incorrect (Gmail Id contains characters,numbers and dot)");
         isValid = 1;
          return false;
       }
    }
     if (domain.indexOf("rediffmail") != -1) {
        if (iChars2.indexOf(user.charAt(i)) != -1) {
         alert("Email address seems incorrect (rediffmail Id contains characters,numbers,dot and _).");
       applyErrors("email", "Email address seems incorrect (rediffmail Id contains characters,numbers,dot and _)");
      isValid = 1;
       return false;
    }
  }
if (domain.indexOf("hotmail") != -1 || domain.indexOf("live") != -1 || domain.indexOf("windowslive") != -1 || domain.indexOf("outlook") != -1) {
    if (iChars3.indexOf(user.charAt(i)) != -1) {
    alert("Email address seems incorrect (hotmail Id contains characters,,numbers,dot,- and _).");
       applyErrors("email", "Email address seems incorrect (hotmail Id contains characters,,numbers,dot,- and _)");
       isValid = 1;
         return false;
   }
   }
 }
 if (domain.indexOf("hotmail") != -1 || domain.indexOf("live") != -1 || domain.indexOf("windowslive") != -1 || domain.indexOf("outlook") != -1) {
     }
     else {
       for (i = 0; i &lt; domain.length; i++) {
         if (domain.charCodeAt(i) &gt; 127) {
             alert("Ths domain name contains invalid characters.");
         applyErrors("email", "Ths domain name contains invalid characters.");
             isValid = 1;
               return false;
            }
          }
       }
    if (user.match(userPat) == null) {
           alert("The Email doesn't seem to be valid.");
            applyErrors("email", "The Email doesn't seem to be valid.");
            isValid = 1;
          return false;
        }
        var IPArray = domain.match(ipDomainPat);
     if (IPArray != null) {
           for (var i = 1; i &lt;= 4; i++) {
                if (IPArray[i] &gt; 255) {
                  alert("Destination IP address is invalid!.");
                  applyErrors("email", "Destination IP address is invalid!");
                   isValid = 1;
                 return false;
              }
          }
            return true;
       }
 var atomPat = new RegExp("^" + atom + "$");
       var domArr = domain.split(".");
     var len = domArr.length;
        for (i = 0; i &lt; len; i++) {
       if (domArr[i].search(atomPat) == -1) {
          alert("The domain name does not seem to be valid.");
               applyErrors("email", "The domain name does not seem to be valid.");
             isValid = 1;
         return false;
          }
       }
    if (checkTLD &amp;&amp; domArr[domArr.length - 1].length != 2 &amp;&amp;
         domArr[domArr.length - 1].search(knownDomsPat) == -1) {
            alert("The address must end in a well-known domain or two letter " + "country.");
           applyErrors("email", "The address must end in a well-known domain or two letter " + "country.");
            isValid = 1;
            return false;
        }
     if (len &lt; 2) {
         alert("This address is missing a hostname!.");
           applyErrors("email", "This address is missing a hostname!");
         isValid = 1;
         return false;
      }
     return true;
    }


