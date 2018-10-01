/** Initialization
     * Group check by theirs hrLocation
     * Generate html to be rendered in table
     **/
var initCheckOption = function(list,locationLabel,locationWidth,descriptionWidth) {
    var tableOptions = {};
    if(locationWidth==undefined || locationWidth==null){
        locationWidth = "500px;"
    };
    if(descriptionWidth==undefined || descriptionWidth==null){
        descriptionWidth = "770px;";
    };
    if(list!=null){
        var head = [ {
            label : locationLabel,
            size : locationWidth,
        }, {
            label : "Description",
            size : descriptionWidth,
        } ];
        var tmpTable = [];
        var tmpList = angular.copy(list);
        while(tmpList.length>0){
            var newCheck = new Object();
            newCheck.hrLocation= tmpList[0].hrLocation;
            newCheck.description = "<div>"+typeErrorCheck(tmpList[0].status)+"<span>"+tmpList[0].description+"</span></div>";
            tmpList.shift();
            for(var i=0;i<tmpList.length;i++){
                if(tmpList[i].hrLocation==newCheck.hrLocation){
                    var tmpErrorType =  typeErrorCheck(tmpList[i].status);
                    var tmpErrorDescription = "<span>"+tmpList[i].description+"</span>";
                    var tmpError = "<div>"+tmpErrorType+tmpErrorDescription+"</div>";

                    newCheck.description = newCheck.description + tmpError;
                    tmpList.splice(i,1);
                    i = i-1;
                };
            };
            tmpTable.push(newCheck);
        };
        var dirList = [];
        for (var i = 0; i < tmpTable.length; i++) {
            var obj = new Object();
            obj.hrLocation = explodeLocation(tmpTable[i].hrLocation);
            obj.descrption = tmpTable[i].description;
            dirList.push(obj);
        };
        tableOptions = {
            nbItemPerPage : 5,
            listObj : dirList,
            thead : head,
            displayHeading : true
        };
    };
    return tableOptions;
};


/**
 * Set HTML with for type of Error Transform with ngSanitize in view
 */
var typeErrorCheck = function(value) {
    if (value == "ERROR") {
        return '<span class="fa-stack marginL-1">'+
                    '<i class="fa fa-circle fa-stack-1x white" ></i>'+
                    '<i class="fa fa-exclamation-circle fa-stack-1x errorColor"></i>'+
                '</span> ';
    } else if (value == "WARNING") {
        return "<span class='fa fa-exclamation-triangle warnColor'></span> "
    } else if (value == "SUCCESS") {
        return "<span class='fa fa-exclamation-triangle warnColor'></span> "
    }else{
        return '<span class="fa-stack marginL-1" tooltip="Info" tooltip-placement="right">'+
                    '<i class="fa fa-circle fa-stack-1x white" ></i>'+
                    '<i class="fa fa-question-circle fa-stack-1x infoColor"></i>'+
                '</span> ';
    };
};