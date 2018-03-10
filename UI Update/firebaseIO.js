function writeUserData(id, first, last, masteradmin, programadmin, active, provider, facility, age) {
		// Active: active,
		// First: first,
		// Age: age,
		// Last: last,
		// MasterAdmin: masteradmin,
		// ProgramAdmin: programadmin,
		// Provider: provider,
		// ReportingFacility: facility
	firebase.database().ref('Users/' + id).update({
		Active: active,
		First: first,
		Age: age,
		Last: last,
		MasterAdmin: masteradmin,
		ProgramAdmin: programadmin,
		Provider: provider,
		ReportingFacility: facility
	}, complete);
}

function complete(error) {
    if (error) {
      console.log('Update failed');
    } else {
      console.log('Update succeeded');
      location.href = "./userlist.html";
    }
  };

function loadConfig(callback) {

    var xobj = new XMLHttpRequest();
    xobj.overrideMimeType("application/json");
    xobj.open('GET', 'config.json', true);
    xobj.onreadystatechange = function() {
        if (xobj.readyState == 4 && xobj.status == "200") {
            // .open will NOT return a value but simply returns undefined in async mode so use a callback
            callback(xobj.responseText);

        }
    }
    xobj.send(null);
}

function readConfigAndConnect(configcontent) {
	config = JSON.parse(configcontent);
		console.log(config);
	firebase.initializeApp(config);
	if (populateUsers) {
		getUsers();
	}
}

function connect(get) {
	console.log("connecting " + get);
	populateUsers = get;
	// Initialize Firebase
	loadConfig(readConfigAndConnect);
	// $.getJSON("config.json", function(config) {
	// 	console.log(config);
	//   firebase.initializeApp(config);
	// });
}

function getUsers() {
	var ref = firebase.database().ref("Users");
	ref.once("value")
	  .then(function(snapshot) {
	    data = snapshot;
	    if(extract(data)) {
	    	setHTML(users);
	    } else {
	    	//TODO - Handle fail
	    };
	  });
}

function extract(snapshot) {
	if (!snapshot.exists()) {
		console.log("no snapshot");
		return false;
	} else {
		users = [];
		//populate users
		snapshot.forEach(function(child) {
			console.log(child.key);
			var user = {};
			user.id = child.key;
			user.data = child.val();
			users.push(user);
		});
		return true;
	}
}

function setHTML(array) {
    // cache <tbody> element:
    var tbody = $('#user_table');
    for (var i = 0; i < array.length; i++) {
        // create an <tr> element, append it to the <tbody> and cache it as a variable:
        var tr = $('<tr/>').appendTo(tbody);
        ($(tr[0])).addClass('tr-hover');
        // append <td> elements to previously created <tr> element:
        tr.append("<td>" + array[i].data.First + '</td>');
        tr.append("<td>" + array[i].data.Last + '</td>');
        tr.append("<td>" + array[i].data.Age + '</td>');
    }
    setClasses()
}

function setClasses() {
  $('.tr-hover').hover(function() {
    $(this).css("background-color", "#075290");
    $(this).css("color", "white");
  });
  $('.tr-hover').mouseout(function() {
    $(this).css("color", "black");
    if ($(this).index() - 1 % 2 == 0) {
      $(this).css("background-color", "#f2f2f2");
    } else {
      $(this).css("background-color", "#ffffff");
    }
  });
  $('.tr-hover').click(function() {
  	console.log(users[$(this).index() - 1]);
  	localStorage.setItem("currentUserBeingModified", JSON.stringify(users[$(this).index() - 1]));
    // localStorage.setItem("firstNameToEdit",$(this).children()[0].innerHTML);
    // localStorage.setItem("lastNameToEdit",$(this).children()[1].innerHTML);
    // localStorage.setItem("index",$(this).index() - 1);
    location.href = "./edituser.html";
  });
}

