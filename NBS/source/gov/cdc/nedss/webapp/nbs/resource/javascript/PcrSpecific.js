		var alertFunctions = false;

		var ASC = "ascending=";

		var EDOBCOL	= 0;
		var MOTHERCOL = 1;
		var MOTHERIDCOL = 2;
		var PEDIATRICIANCOL = 3;
		var PEDIATRICIANIDCOL = 4;

		var rowsPerPage = 20;

	    function chkPedEntry(obj) {
	    	if(alertFunctions) alert("chkPedEntry");
	    	if(obj.value.trim() == "") {
	    		clearFilterPediatrician();
	    	}
	    }

	    function clearAllFiltersAndSorts() {
	    	if(alertFunctions) alert("clearAllFiltersAndSorts");
			for (i=0; i < SORTASCFLAG.length; i++) {
				SORTASCFLAG[i] = null;
			}
            resetFilterEDOB();
			resetFilterPediatrician();
			resetFilterMother()

	    	var parm=CLEARALLFILTERSANDSORTS;
			parm+="&"+REQCOL+PEDIATRICIANCOL;
			getData(parm);
	    }

		function resetFilterPediatrician() {
			if(alertFunctions) alert("resetFilterPediatrician");
	    	var lnk = getElementByIdOrByName("linkPediatrician");
	    	lnk.innerHTML = "Pediatrician";
	    	var f = getElementByIdOrByName("pediatricianFilter");
			f.value = "";
		}

		function clearFilterPediatrician() {
			if(alertFunctions) alert("clearFilterPediatrician");
			getPage = 1;
			resetFilterPediatrician();
			var parm=CLEARFILTER;
			parm+="&"+REQCOL+PEDIATRICIANCOL;
			getData(parm);
		}

		function filterPediatrician() {
			if(alertFunctions) alert("filterPediatrician");
			getPage = 1;
			var vtxt = getElementByIdOrByName("pediatricianFilter").value;

	    	var vlnk = getElementByIdOrByName("linkPediatrician");
	    	if (vtxt.length == 0) {
		    	vlnk.innerHTML = "Pediatrician";
	    	} else {
		    	vlnk.innerHTML = "Pediatrician (..."+vtxt.substring(0,5)+"...)";
	    	}

			var parm=FILTERBYTEXT;
			parm+="&"+ASC+SORTASCFLAG[PEDIATRICIANCOL];
			parm+="&"+REQCOL+PEDIATRICIANCOL;
			parm+="&"+FILTERTEXT+vtxt;
			getData(parm);
		}


	    function chkMothEntry(obj) {
	    	if(alertFunctions) alert("chkMothEntry");
	    	if(obj.value.trim() == "") {
	    		clearFilterMother();
	    	}
	    }

		function resetFilterMother() {
			if(alertFunctions) alert("resetFilterMother");
	    	var lnk = getElementByIdOrByName("linkMother");
	    	lnk.innerHTML = "Mother";
			getElementByIdOrByName("motherFilter").value = "";
		}

		function clearFilterMother() {
			if(alertFunctions) alert("clearFilterMother");
			getPage = 1;
			resetFilterMother();
			var parm=CLEARFILTER;
			parm+="&"+REQCOL+MOTHERCOL;
			getData(parm);
		}

		function filterMother() {
			if(alertFunctions) alert("filterMother");
			getPage = 1;
			var vtxt = getElementByIdOrByName("motherFilter").value;

	    	var vlnk = getElementByIdOrByName("linkMother");
	    	if (vtxt.length == 0) {
		    	vlnk.innerHTML = "Mother";
	    	} else {
		    	vlnk.innerHTML = "Mother (..."+vtxt.substring(0,5)+"...)";
	    	}

			var parm=FILTERBYTEXT;
			parm+="&"+ASC+SORTASCFLAG[MOTHERCOL];
			parm+="&"+REQCOL+MOTHERCOL;
			parm+="&"+FILTERTEXT+vtxt;
			getData(parm);
		}

		function displayRow(rowId){
			if(alertFunctions) alert("displayRow");
			var imgName ="";
			var fld;
			if (rowId == 'filterRowEDOB') {
				imgName = "filterIcoEDOB";
				fld = null;
			} else if(rowId == "filterRowMother") {
				imgName = "filterIcoMother";
				fld = getElementByIdOrByName("motherFilter");
			} else if (rowId == "filterRowPediatrician") {
				imgName = "filterIcoPediatrician";
				fld = getElementByIdOrByName("pediatricianFilter");
			}
			var img = getElementByIdOrByName(imgName);
			var row = getElementByIdOrByName(rowId);
			if (row.style.display == '') {
				row.style.display = 'none';
				img.src = "Filter.gif";
			} else {
				row.style.display = '';
				img.src = "notFilter.gif";
				if (fld != null) {
		            //fld.blur();
		            //fld.value = "";
		            fld.focus();
				}
			}
		}

		function filterEDOB(p) {
			if(alertFunctions) alert("filterEDOB");
			getPage = 1;
	    	var vlnk = getElementByIdOrByName("linkEDOB");
	    	vlnk.innerHTML = "Expected DOB ("+p+" days)";
			var parm=FILTERBYFUTUREDAYS;
			parm+="&"+ASC+SORTASCFLAG[EDOBCOL];
			parm+="&"+REQCOL+EDOBCOL;
			parm+="&"+ENDDTOFFSET+p;
			getData(parm);
		}

		function resetFilterEDOB() {
			if(alertFunctions) alert("resetFilterEDOB");
            getElementByIdOrByName("radioEDOBAll").checked = true;
	    	var lnk = getElementByIdOrByName("linkEDOB");
	    	lnk.innerHTML = "Expected DOB";
		}

		function clearFilterEDOB() {
			if(alertFunctions) alert("clearFilterEDOB");
			getPage = 1;
			resetFilterEDOB();
			var parm=CLEARFILTER;
			parm+="&"+ASC+SORTASCFLAG[EDOBCOL];
			parm+="&"+REQCOL+EDOBCOL;
			getData(parm);
		}

	    function createXMLHttpRequest() {
	    	if(alertFunctions) alert("createXMLHttpRequest");
	    	if (typeof XMLHttpRequest != "undefined") {
	    		return new XMLHttpRequest();
	         } else {
	        	return new ActiveXObject("Microsoft.XMLHTTP");
	         }
	    }

    	var loadDataReq;
	    function loadData() {
	    	if(alertFunctions) alert("loadData");
	    	loadDataReq = createXMLHttpRequest();
	    	var loadDataUrl = "/nbs/PCR/PCRLoadServlet";
	    	loadDataReq.open("POST", loadDataUrl, true);
	    	loadDataReq.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	    	loadDataReq.onreadystatechange    = loadDataCallbackHandler;
	    	loadDataReq.send(null);
	    }

		function loadDataCallbackHandler() {
			if (loadDataReq.readyState == 4)  {
				if(alertFunctions) alert("loadDataCallbackHandler readyState == 4");
				var rsp = loadDataReq.responseText;
				getData(INIT);
			}
		}

    	var getDataReq;
	    function getData(parm) {
	    	if(alertFunctions) alert("getData");
	    	if (parm == null) {
	    		parm = "";
	    	} else {
	    		parm = "?"+parm;
	    	}

	    	parm += "&"+ROWSPERPAGE+rowsPerPage;
	    	parm += "&"+GETPAGE+getPage;

	    	getDataReq 	= createXMLHttpRequest();
	        var getDataReqUrl	= "/nbs/PCR/SortFilterServlet"+parm;
	        getDataReq.open("POST", getDataReqUrl, true);
	    	getDataReq.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	        getDataReq.onreadystatechange    = getDataCallbackHandler;
	        getDataReq.send(null);
	    }
		function isOdd(num) {
			return (num % 2) == 1;
		}

		function getDataCallbackHandler() {
			if (getDataReq.readyState == 4) {
				if(alertFunctions) alert("getDataCallbackHandler readyState == 4");
				var sei = getElementByIdOrByName("sortIcoEDOB");
				var smi = getElementByIdOrByName("sortIcoMoth");
				var spi = getElementByIdOrByName("sortIcoPed");

				if (SORTASCFLAG[EDOBCOL] == null) {
					sei.src = "blank.gif";
				} else if (SORTASCFLAG[EDOBCOL] == true) {
					//sei.src = "SortDesc.gif";
					sei.src = "Down.gif";
				} else {
					//sei.src = "SortAsc.gif";
					sei.src = "Up.gif";
				}

				if (SORTASCFLAG[MOTHERCOL] == null) {
					smi.src = "blank.gif";
				} else if (SORTASCFLAG[MOTHERCOL] == true) {
					//smi.src = "SortDesc.gif";
					smi.src = "Down.gif";
				} else {
					//smi.src = "SortAsc.gif";
					smi.src = "Up.gif";
				}

				if (SORTASCFLAG[PEDIATRICIANCOL] == null) {
					spi.src = "blank.gif";
				} else if (SORTASCFLAG[PEDIATRICIANCOL] == true) {
					//spi.src = "SortDesc.gif";
					spi.src = "Down.gif";
				} else {
					//spi.src = "SortAsc.gif";
					spi.src = "Up.gif";
				}

				var tabData = null;
				tabData = new Object();
				var rsp = JSON.parse(getDataReq.responseText);
				totalRows = rsp.totalRows;
				filteredRows = rsp.filteredRows;
//				alert(filteredRows+" of "+totalRows);
				displayMsg();
				setPagingLinks();
				tabData = rsp.data;

				var tab = getElementByIdOrByName("pcrTableBody");
				clearChildren(tab);
				for (i=0; i < tabData.length; i++) {
					var newtr = document.createElement('tr');
					if (isOdd(i)) {
						newtr.className = "altRow";
					}

					var tabRow;
					tabRow = new Object();
					tabRow = tabData[i];

					/*
						var EDOBCOL	= 0;
						var MOTHERCOL = 1;
						var MOTHERIDCOL = 2;
						var PEDIATRICIANCOL = 3;
						var PEDIATRICIANIDCOL = 4;
					*/
					{
						var txt = tabRow[EDOBCOL];
						if (txt != null) {
							txt = formatDate(txt);
						} else {
							txt = "";
						}
						var td = document.createElement('td');
						td.innerHTML = txt;
						newtr.appendChild(td);
					}
					{
						var txt = tabRow[MOTHERCOL];
						var id  = tabRow[MOTHERIDCOL];
						if (txt== null) {
							txt = "";
						}
						if (id == null) {
							id = "";
						}
						var td = document.createElement('td');
						if (id != null && id != "") {
							td.innerHTML = "<a href='#' onClick='goToPatient("+id+")'>"+txt+"</a>";
						} else {
							td.innerHTML = txt;
						}
						newtr.appendChild(td);
					}
					{
						var txt = tabRow[PEDIATRICIANCOL];
						var id  = tabRow[PEDIATRICIANIDCOL];
						if (txt == null) {
							txt = "";
						}
						if (id == null) {
							id = "";
						}
						var td = document.createElement('td');
						td.setAttribute("colspan", "2");
						if (id != null && id != "") {
							td.innerHTML = "<a href='#' onClick='goToProvider("+id+")'>"+txt+"</a>";
						} else {
							td.innerHTML = txt;
						}
						newtr.appendChild(td);
					}
					tab.appendChild(newtr);
				}
			}
		}

		function goToPatient(n) {
			alert ("Go to Patient: "+n);
		}

		function goToProvider(n) {
			alert ("Go to Provider: "+n);
		}

		function showHideAllFilters() {
			if(alertFunctions) alert("showHideAllFilters");
			displayRow("filterRowEDOB");
			displayRow("filterRowMother");
			displayRow("filterRowPediatrician");
		}