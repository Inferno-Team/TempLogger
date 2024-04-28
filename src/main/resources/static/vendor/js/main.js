
(function ($) {
    "use strict";


    /*==================================================================
   [ Focus input ]*/
    $('.input100').each(function () {
        $(this).on('blur', function () {
            if ($(this).val().trim() != "") {
                $(this).addClass('has-val');
            }
            else {
                $(this).removeClass('has-val');
            }
        })
    })


    /*==================================================================
    [ Validate ]*/
    var input = $('.validate-input .input100');

    $('.validate-form').on('submit', function (e) {
        var check = true;

        for (var i = 0; i < input.length; i++) {
            if (validate(input[i]) == false) {
                showValidate(input[i]);
                check = false;
            }
        }
        if (!check) return false;
        e.preventDefault();
        axios.post("/auth/authenticate", {
            email: $("#email-input").val(),
            password: $("#password-input").val()
        }).then((result) => {
            let data = result.data;
            let token = data.token;
            setCookie('token', token, 7);
        }).catch((err) => {
            console.error(err);
        });

        return check;
    });

    $('.create-new-module-form').on('submit', function (e) {
        e.preventDefault();
        axios.post('/module/store', {
            name: $("#module-name-input").val(),
            mac: $("#mac-input").val(),
        }, {
            headers: {
                "Authorization": "Bearer " + getCookieValue('token'),
            }
        }).then((result) => {

        }).catch((err) => {

        });
    })


    function setCookie(name, value, days) {
        var expires = "";
        if (days) {
            var date = new Date();
            date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
            expires = "; expires=" + date.toUTCString();
        }
        document.cookie = name + "=" + (value || "") + expires + "; path=/";
    }
    function getCookieValue(name) {
        const regex = new RegExp(`(^| )${name}=([^;]+)`)
        const match = document.cookie.match(regex)
        if (match) {
            return match[2]
        }
    }
    $('.validate-form .input100').each(function () {
        $(this).focus(function () {
            hideValidate(this);
        });
    });

    function validate(input) {
        if ($(input).attr('type') == 'email' || $(input).attr('name') == 'email') {
            if ($(input).val().trim().match(/^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{1,5}|[0-9]{1,3})(\]?)$/) == null) {
                return false;
            }
        }
        else {
            if ($(input).val().trim() == '') {
                return false;
            }
        }
    }

    function showValidate(input) {
        var thisAlert = $(input).parent();

        $(thisAlert).addClass('alert-validate');
    }

    function hideValidate(input) {
        var thisAlert = $(input).parent();

        $(thisAlert).removeClass('alert-validate');
    }



})(jQuery);