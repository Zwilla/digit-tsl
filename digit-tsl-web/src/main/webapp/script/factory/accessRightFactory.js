/**
 * accessRightFactory: manage display element in function of user role
 */
digitTslWeb.factory('accessRightFactory', [ function() {
	return {
		/**
         * Return boolean Check if user has right to smthg SUP : always allowed
         */
		authorized : function(userInfo,label){
			var bool = false;
			if((userInfo!= undefined) && (userInfo.ecasId!=null) && (userInfo.role!=null)){
				if(label==undefined){ // Case where user only need to be authentified (no specific role)
					return true;
				};
				var roles = userInfo.role;
				for(var i=0;i<roles.length;i++){
					role = roles[i].code;
					if(role=="SUP"){
						return true;
					}else if(role==label){
						return true;
					};
				};
			};
			return false;
		},

		  /**
             * Return boolean Check if user has a specific role
             */
        isUser : function(userInfo,label){
            var bool = false;
            if((userInfo!= undefined) && (userInfo.ecasId!=null) && (userInfo.role!=null)){
                var roles = userInfo.role;
                for(var i=0;i<roles.length;i++){
                    role = roles[i].code;
                    if(role=="SUP"){
                        return true;
                    }else if(role==label){
                        return true;
                    };
                };
            };
            return false;
        }
    };

} ]);