/**
 * genericQueue.js: JavaScript file with generic methods used from generic queues.
 */

	/*The following methods were removed from the specific JSP file and added here*/

	//Added for resolving an issue with IE11
	function enableFiltering(){
		document.getElementById("dropdownsFiltering").style.display="block";
	}
	function removeMargin(){
		document.getElementById("whitebar").style.marginTop="0px";
	}
	
	/*onKeyUpValidate: this method is used to enable/disable the OK button form the text filtering*/
 	function onKeyUpValidate()
	{      	  

		var stringQueue = document.getElementById("stringQueueCollection").value;
		var arrayQueue = stringQueue.split("#");
		
		for(var i=0; i<arrayQueue.length; i++){
			var elements = arrayQueue[i].split(",");
			var dropdownProperty="";
			var type="";
			for(var j=0; j<elements.length; j++){
				var key =elements[j].split(":")[0];
				var value =elements[j].split(":")[1];
				if(key.indexOf("dropdownProperty")!=-1)
					dropdownProperty=value;
				if(key.indexOf("filterType")!=-1)
					type=value;
			}
			
			if(type==1 && dropdownProperty!=null && dropdownProperty!="null" && dropdownProperty!=""){//Text
				if(getElementByIdOrByName(dropdownProperty).value != ""){
					getElementByIdOrByName("b1"+dropdownProperty).disabled=false;
					getElementByIdOrByName("b2"+dropdownProperty).disabled=false;
				   }else if(getElementByIdOrByName(dropdownProperty).value == ""){
					getElementByIdOrByName("b1"+dropdownProperty).disabled=true;
					getElementByIdOrByName("b2"+dropdownProperty).disabled=true;
				   }
			}
		}
	}
								
	function showCount() {
		$j(".pagebanner b").each(function(i){ 
			$j(this).append(" of ").append($j("#queueCnt").attr("value"));
		});
		$j(".singlepagebanner b").each(function(i){ 
			var cnt = $j("#queueCnt").attr("value");
			if(cnt > 0)
				$j(this).append(" Results 1 to ").append(cnt).append(" of ").append(cnt);
		});				
	}
	function createLink(element, url)
	{
		// call the JS function to block the UI while saving is on progress.
		blockUIDuringFormSubmissionNoGraphic();
        document.forms[0].action= url;
        document.forms[0].submit();  
	}


			
	/*The following methods were removed from the specific JavaScript file and added here*/
			 

	function printQueue() {
		window.location.href = $j(".exportlinks a:last").attr("href") == null ? "#" :  $j(".exportlinks a:last").attr("href");
	}
	function exportQueue() {
		window.location.href = $j(".exportlinks a:first").attr("href") == null ? "#" : $j(".exportlinks a:first").attr("href");
	}        	  
	

	function makeMSelects() {
		
		if(document.getElementById("stringQueueCollection")!=null && document.getElementById("stringQueueCollection")!=undefined){
		var stringQueue = document.getElementById("stringQueueCollection").value;
		var arrayQueue = stringQueue.split("#");
		
		for(var i=0; i<arrayQueue.length; i++){
			var elements = arrayQueue[i].split(",");
			var dropdownStyleId="";
			var type="";
			for(var j=0; j<elements.length; j++){
				var key =elements[j].split(":")[0];
				var value =elements[j].split(":")[1];
				if(key.indexOf("dropdownStyleId")!=-1)
					dropdownStyleId=value;
				if(key.indexOf("filterType")!=-1)
					type=value;
			}
			
			if(type==0 || type ==2)//Date or multiselect
				$j("#"+dropdownStyleId+"").multiSelect({actionMode: document.getElementById("actionMode").value});
			else//Text
				$j("#"+dropdownStyleId+"").text({actionMode: document.getElementById("actionMode").value});
			
		}
		}
	}	
	
	function attachIcons() {	
	
			if(document.getElementById("stringQueueCollection")!=null &&  document.getElementById("stringQueueCollection")!=undefined){
				var stringQueue = document.getElementById("stringQueueCollection").value;
				var arrayQueue = stringQueue.split("#");
				var values = new Array();
				
				for(var i=0; i<arrayQueue.length; i++){
					var elements = arrayQueue[i].split(",");
					var dropdownStyleId="";
					var type="";
					for(var j=0; j<elements.length; j++){
						var key =elements[j].split(":")[0];
						var value =elements[j].split(":")[1];
						if(key.indexOf("dropdownStyleId")!=-1){
							dropdownStyleId=value;
							if(values.indexOf(dropdownStyleId)==-1)
								values.push(dropdownStyleId);
						}
						
					}
				}
				
					var i=0;
					$j("#parent thead tr th a").each(function(i) {	
					$j(this).parent().append($j("#"+values[i]+""));
					i++;
					});

	    $j("#parent").before($j("#whitebar"));
	    $j("#parent").before($j("#removeFilters"));
		}
	}
	
	function displayTooltips() {
		if(document.getElementById("stringQueueCollection")!=null &&  document.getElementById("stringQueueCollection")!=undefined){
				var stringQueue = document.getElementById("stringQueueCollection").value;
				var arrayQueue = stringQueue.split("#");
				var values = new Array();
				
				for(var i=0; i<arrayQueue.length; i++){
					var elements = arrayQueue[i].split(",");
					var dropdownStyleId="";
					var type="";
					for(var j=0; j<elements.length; j++){
						var key =elements[j].split(":")[0];
						var value =elements[j].split(":")[1];
						if(key.indexOf("backendId")!=-1){
							dropdownStyleId=value;
							if(values.indexOf(dropdownStyleId)==-1)
								values.push(dropdownStyleId);
						}
						
					}
				}

		var i=0;
		$j(".sortable a").each(function(i) {
			var headerNm = $j(this).html();
			
			_setAttributes(headerNm, $j(this), $j("#"+values[i]+""));
			i++;
			
		      
		      
		      
		});	
		}
	}
	
	function _handlePatient(headerNm, link, colId) {
		var htmlAsc = '<img class="multiSelect" src="GraySortAsc.gif" id="queueIcon" align="top" border="0"/>';
		var htmlDesc = '<img class="multiSelect" src="GraySortDesc.gif" id="queueIcon" align="top" border="0"/>';
		
		var sortSt = $j("#sortSt") == null ? "" : $j("#sortSt").html();
		if(sortSt != null && sortSt.indexOf(headerNm) != -1 ) {
			if(sortSt != null && sortSt.indexOf("descending") != -1) {
				link.after(htmlDesc);
			} else {
				link.after(htmlAsc);
			}
		}
	
	}
	
	
	function _setAttributes(headerNm, link, colId) {
		
		var imgObj = link.parent().find("img");
		var toolTip = "";	
		var sortSt = $j("#sortSt") == null ? "" : $j("#sortSt").html();
		var orderCls = "SortAsc.gif";
		var sortOrderCls = "FilterAndSortAsc.gif";
		if(sortSt != null && sortSt.indexOf("descending") != -1) {
			orderCls = "SortDesc.gif";
			sortOrderCls = "FilterAndSortDesc.gif";
		}  
		var filterCls = "Filter.gif";
	  	toolTip = colId.html() == null ? "" : colId.html();
	  	if(toolTip.length > 0) {
			link.attr("title", toolTip);
			imgObj.attr("src", filterCls);
			if(sortSt != null && sortSt.indexOf(headerNm) != -1 )
				imgObj.attr("src", sortOrderCls);			
	  	} else {
			if(sortSt != null && sortSt.indexOf(headerNm) != -1 ) {
				imgObj.attr("src", orderCls);						
			}				
	  	}
	}
	
			
	/**
	* disableRemoveAllFilters: disable the link called 'Remove All Filters/Sorts'
	*/
	
	 function disableRemoveAllFilters(){
    	$j(".removefilerLink").removeAttr("href");
    }

	/**
	* enableRemoveAllFilters: enable the link called 'Remove All Filters/Sorts'
	*/
	
    function enableRemoveAllFilters(){
    	$j(".removefilerLink").attr("href", "javascript:clearFilter()");
    }