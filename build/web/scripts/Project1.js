/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var Project1 = ( function () {
    
    return {
        
        getSelection: function (sessionid) {
            //Send ajax request and call convert to table method on success
            //alert("getSelection() called");
            var that = this;
            var id = $("select").val();
            $.ajax({
                url: 'registration?code=' + id,
                method: 'GET',
                dataType: 'html',
                success: function(response) {
                    that.output(response);                    
                }
            });
            
        },
        addUser: function (fname, lname, dname, sessionid) {
            var that = this;
            var fname = $("#fname").val();
            var lname = $("#lname").val();
            var dname = $("#dname").val();
            var id = $("select").val();
            $.ajax({
                url: 'registration?code=' + fname + ";" + lname + ";" + dname + ";" + id,
                method: 'POST',
                dataType: 'json',
                success: function(response) {
                    //console.log(response);
                    that.outputJSON(response);
                }
            });
        },
        output: function(response) {
            $("#output").html(response);
            
        },
        outputJSON: function(response) {
            var congrats = "Congratulations! You have successfully registered as: ";
            var yourcode = "Your registration code is: ";
            $("#output").html("<p>" + congrats + response["displayname"] + "<br>" + yourcode + response["code"]);
        }
    };
}());


