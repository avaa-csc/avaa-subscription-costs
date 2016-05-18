var loc = window.location.toString()
if(loc.indexOf('#') != -1) {
   var params = loc.split('#')[1];
   var iframe = document.getElementById('embeddedIframe');
   iframe.src = iframe.src + '#' + params;​​​​​​​​​​​​​​​​
}