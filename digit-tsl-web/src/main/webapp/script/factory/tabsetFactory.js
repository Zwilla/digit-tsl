/**
 * TabsetFactory : manage active tab in tabset
 * Used in TL information edition to focus on the right tab at openning 
 */
digitTslWeb.factory('tabsetFactory', [ function() {
	return {
		activeTab : function(index,tabset){
			if(tabset!=undefined && tabset!=null && tabset.length>0){
				for(var i =1;i<tabset.length;i++){
					tabset[i].isActive = false;
				};
				tabset[index].isActive = true;
			};
			return tabset;
		}
	};

} ]);