/**
 * Generate service node used in browser/SieQ wizard
 */
var serviceNode = function(service) {
	if (service != null) {
		var span = "";
		var emptySpan = "<span class='emptySpan'></span>"
		if (service.currentStatusStartingDate != undefined && service.currentStatusStartingDate != null) {
			var tmpDate = new Date(service.currentStatusStartingDate).toISOString().split("T");
			var startingDate = tmpDate[0];
			span = span + "<span class='boldNode'>" + startingDate + "</span>" + emptySpan;
		}
		if (service.typeIdentifier != undefined && service.typeIdentifier != null) {
			typeIdentifier = "";
			if (service.typeIdentifier.indexOf("TrstSvd") != -1) {
				var typeIdentifier = service.typeIdentifier.split("http://uri.etsi.org/TrstSvd/Svctype/")[1];
			} else if (service.typeIdentifier.indexOf("TrstSvc") != -1) {
				var typeIdentifier = service.typeIdentifier.split("http://uri.etsi.org/TrstSvc/Svctype/")[1];
			}
			span = span + typeIdentifier + emptySpan;
		}
		if (service.currentStatus != undefined && service.currentStatus != null) {
			var currentStatus = service.currentStatus.split("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/")[1];
			span = span + currentStatus;
		}
		if (span != "") {
			span = " : " + span;
		}
		return span;
	}

};