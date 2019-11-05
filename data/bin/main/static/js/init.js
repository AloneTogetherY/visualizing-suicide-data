'use strict';
(function ($) {
    $(function () {

        $('.countries-reference-btn').click(function (e) {
            e.preventDefault();                   // prevent default anchor behavior
            let goTo = this.getAttribute("href"); // store anchor href

            setTimeout(function(){
                window.location = goTo;
            }, 800);                             // time in ms
        });
        $('a[href="#top"]').click(function () {
            $('html, body').animate({scrollTop: 0}, 'slow');
            return false;
        });

    }); // end of document ready
})(jQuery);