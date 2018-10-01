/**
 * ElectronicUriFactory : manage electronic uri
 * Convert XML <URI> into two value for edition : type (Adrtype properties) + url
 * Handle Electronic Address & Distribution List cases
 **/
digitTslWeb.factory('electronicUriFactory', [ function() {
	return {

		/**
		 * Init new tab
		 * with Address type & Address value split
		 * (execute on loading)
		 **/
		initTabBis: function(myTabs) {
			var myTabsBIS = [];
			if(myTabs!=undefined){
				for(var i=0;i<myTabs.length;i++){
					var tab = Object();
					tab.id = myTabs[i].id;
					var adr = this.splitAddressType(myTabs[i].value);
					if(myTabs[i].language!=undefined){ //Electronic Address case
						tab.language = myTabs[i].language;
					}else{ // Distribution List
						tab.index = myTabsBIS.length;
					};
					if(adr.length==0){
					    //Undefined type (fax/tel/..)
					    tab.adrType="";
	                    tab.adrLink=myTabs[i].value;
					}else{
					    tab.adrType=adr[0];
	                    tab.adrLink=adr[1];
					};
					myTabsBIS.push(tab);
				};
			};
			return myTabsBIS;
	    },

	    /**
		 * Data transformation
		 * Split address type (from propertie ADRTYPE) & address value
		 **/
	    splitAddressType : function(label){
	    	var addressType= adrTypeProperties;
			var adr = [];
			if((label!=null) || (label!="")){
				for(var i=0;i<addressType.length;i++){
					var splitValue = label.split(addressType[i].label);
					if(splitValue[0]==""){
						adr[0]=addressType[i].label;
						//Case where they type same prefix 2* (ex: http://http://address.com)
						if((splitValue[1]=="") && (splitValue[2]!=undefined) && (splitValue[2]!="")){
							adr[1]=addressType[i].label+splitValue[2];
						}else{
							adr[1]=splitValue[1];
						};
					};
				};
			}else{
				adr[0] = addressType[0];
				adr[1]= "";
			};
			if(adr[1]=="undefined"){
				adr[1]="";
			};
			return adr;
		},

		/**
		* Add
		**/
		addTab : function(label,myTabsBIS) {
			var addressType= adrTypeProperties;
			var tab = Object();
			if(label==undefined){ //Distribution list
				tab.index = myTabsBIS.length;
			}else{ // Electronic Address
				tab.language=label;
			};
			tab.id = Math.floor((Math.random() * 100) + 1);
			tab.adrType=addressType[0].label;
			tab.adrLink="";
			return tab;
		},

		/**
		* Rebuild table
		* w/ address type & address value merged
		**/
		rebuildTab : function(myTabsBIS){
			var myTabs = [];
			for(var i=0;i<myTabsBIS.length;i++){
				var tab = Object();
				tab.id=myTabsBIS[i].id;
				if(myTabsBIS[i].language!=undefined){ //Electronic Address case
					tab.language = myTabsBIS[i].language;
				};
				if(!myTabsBIS[i].adrLink){
					myTabsBIS[i].adrLink="";
				};
				tab.value=myTabsBIS[i].adrType+myTabsBIS[i].adrLink;
				myTabs.push(tab);
			};
			return myTabs;
		}
	};

} ]);