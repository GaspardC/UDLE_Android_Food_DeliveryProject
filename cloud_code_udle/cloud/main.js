
var customerId;
var express = require('express');
var Stripe = require('stripe');
Stripe.initialize('sk_test_XXXXXXXXXXXXXXXXX');
//Stripe.initialize('pk_XXXXXXXXXXXXXXXXX');



Parse.Cloud.define("payment", function(request, response) {

var customerId = request.params.customerId;
var totalInCents = request.params.price;
Stripe.Charges.create({
  amount: totalInCents, // amount in cents, again
  currency: "eur",
  customer: customerId // Previously stored, then retrieved
}).done(function(results) {
  response.success("payment accepted")
  }, function(err) {
  response.error('payment refused : ' + err);
});

});


Parse.Cloud.define("registerCustomer", function(request, response) {


Stripe.Customers.create({
  description: request.params.descrip,
  source:  request.params.cardToken, // obtained with Stripe.js
}).done(function(results) {

	/*	var usrId = request.params.userId;


	getUser(usrId).then
    (
        //When the promise is fulfilled function(user) fires, and now we have our USER!
        function(user)
        {
            response.success(user);

        }
        ,
        function(error)
        {
            response.error(error);
        }
    );*/

        var Usr = new Parse.User({id:request.params.userId});
        var newcust = Parse.Object.extend("customer");
        var newUsr = new newcust();

        newUsr.set("sCID", results.id);
        newUsr.set("parent", Usr);
        var pACL = new Parse.ACL();
              pACL.setPublicReadAccess(false);
              pACL.setPublicWriteAccess(false);
              pACL.setReadAccess(Usr, true);
              pACL.setWriteAccess(Usr, true);
        	  newUsr.set("ACL", pACL);
        	  newUsr.save(null,
            {
                success : function(customer)
                {
                	console.log("customer saved :  " + request.params.userId);
                    response.success("customer saved to parse = " +  request.params.userId );
                },
                error : function(error)
                {
                	console.log("eroor in saving customer");
                    response.error("Ops failed to saved customer id ");
                }
            });
        	  //response.success(results);

}, function(err) {
  response.error('Error:' + err);
});

});



function getUser(userId)
{
    Parse.Cloud.useMasterKey();
    var userQuery = new Parse.Query(Parse.User);
    userQuery.equalTo("objectId", userId);

    //Here you aren't directly returning a user, but you are returning a function that will sometime in the future return a user. This is considered a promise.
    return userQuery.first
    ({
        success: function(userRetrieved)
        {
            //When the success method fires and you return userRetrieved you fulfill the above promise, and the userRetrieved continues up the chain.
            return userRetrieved;
        },
        error: function(error)
        {
            return error;
        }
    });
};
