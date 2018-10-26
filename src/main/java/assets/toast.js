/**
 * toast.js
 *
 * JS representation of a 'toast' temporary notification element
 */

 function Toast(msg, color) {
    this.msg = msg;
    this.color = color;
 }

 var ToastCount = 0;

 Toast.prototype = {
    constructor: Toast,

    show: function() {
        var html = "<div id='toast"+ToastCount+"' class='toast' style='color:"+this.color+"'>";
        html+=this.msg;
        html+="</div>";

        document.getElementsByTagName("body")[0].insertAdjacentHTML("afterbegin", html);

        var e = document.getElementById("toast"+ToastCount);

        ToastCount++;

        // set timeout to start fading out
        setTimeout(function() {
            e.className = "toast fadeout";

            // set timeout to remove toast from DOM
            setTimeout(function() {
                e.parentNode.removeChild(e);
            }, 333);
        }, 2000);
    }
 };