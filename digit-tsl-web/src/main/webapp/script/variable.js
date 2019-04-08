digitTslWeb
		.constant(
				'appConstant',
				{

					confirmationDirective : {
						genericConfirmation : "Do you confirm this action ?"
					},

					// Directive & Factory
					digitalIdentity : {
						undefinedDigitalIdentity : "Undefined Digital Identity",
						certificateFileInvalid : "Certificate file is invalid or corrupted.",
						pointerToOthers : "POINTERS_TO_OTHER_TSL"
					},

					errorFactory : {
						errorOccured : "An error occured."
					},

					extensionFactory : {
						addExtensionFailure : "Add extension process failure.",
						editExtensionFailure : "Edit extension process failure.",
						keyUsageList : [ "digitalSignature", "nonRepudiation", "keyEncipherment", "dataEncipherment", "keyAgreement", "keyCertSign",
								"crlSign", "encipherOnly", "decipherOnly" ],
						takenOverBy : "Taken Over By",
						qualificationExtension : "Qualifications Extension",
						additionnalService : "Additional Service Information",
						expiredCertificate : "Expired Certificate Revocation Date",
						anyType : "Extension (AnyType)",
						anyTypeNotSupported : "'AnyType' extension are currently not supported by TL-Manager on edition",
						extUndefined : "Extension (not defined)"
					},

					nexuFactory : {
						oldVersionError : '<p>This message appears because you are using an unsupported version of NexU.</p>'
								+ '<p>The last release of the application can be found here: <a href="%NEXU_URL%">Download NexU</a>.</p>'
								+ '<div>For further questions, please don’t hesitate to contact the EC TL Service team at:</div> <div><a>EC-TL-Service@ec.europa.eu</a>.</div>',
						notInstalledError : '<p>This message appears because either NexU is not running on your PC or is not installed yet.</p>'
								+ '<p>If you want to sign a Trusted List directly from TL Manager, you need to download and/or start NexU on your PC. '
								+ 'NexU is an open-source signing module that can be found here: <a href="%NEXU_URL%">Download NexU</a>.</p>'
								+ '<p>If you prefer not to download NexU, as an alternative you can export the Trusted List, sign it with the signing application of your choice that generates a XAdES signature compliant to the requirement of the ETSI 119 612 Standard, and import it back into TL Manager.</p>'
								+ '<p>A compliant DSS-based offline signing tool is available here: <a href="%SIGNING_TOOL_URL%">Download the offline TL-signing tool</a>.</p>'
								+ '<div>For further questions, please don’t hesitate to contact the EC TL Service team at:</div> <div><a>EC-TL-Service@ec.europa.eu</a>.</div>',
						certificateError : "<p>An error occurred while connecting to the smartcard. Please try again.<p/><div>If the problem persists, please contact the EC TL Service team at:</div> <a>EC-TL-Service@ec.europa.eu</a>.",
						signatureError : "<p>An error occurred while performing the signature digest. Please try again.<p/><div>If the problem persists, please contact the EC TL Service team at:</div> <a>EC-TL-Service@ec.europa.eu</a>.",
						noProductFound : "no.product.found",
						userCancelled : "user.cancel",
						userCancelledMessage : "User has cancelled the signature operation.",
						certificateRequestError : "An error occured while getting certificateRequest.",
						noProductFoundMessage : "<p>No product found, please verify that your smartcard is plugged and try again.</p><div>If the problem persists, please contact the EC TL Service team at:</div> <a>EC-TL-Service@ec.europa.eu</a>.",
						getUrlError : "Get Nexu files url failure.",
						getSealError : "Geat seal list failure."
					},

					nexuLoading : {
						checkVersion : "Check nexU version.",
						getSmart : "Get certificates.",
						getTbs : "Get ToBeSign.",
						finalize : "Finalize procces",
						chooseCert : "You must choose a Certificate to Sign.",
						sigError : "<p>An error occurred while signing with the smartcard. Please try again.</p><div>If the problem persists, please contact the EC TL Service team at:</div> <a>EC-TL-Service@ec.europa.eu</a>.",
						storeNotification : "Store notification."
					},

					trustedListFactory : {
						getTlError : "The requested Trusted List doesn’t exist.",
						getSignatureError : "Signature Information loading failure.",
						getChangeError : "Changes loading failure.",
						getCheckError : "Checks loading failure.",
						getAllCheckError : "Run all checks failure.",
						getDraftEditionCheckError : "Get check(s)/change(s) visible failure.",
						tlccActiveError : "Error while checking TLCC availability.",

						checkProviderError : "Checks performed on Trust Service Provider failed.",
						getLastEditedError : "Last edition date loading failure.",
						conflictTlFailure : "Get conflict TrustedList failure.",
						checkSignatureFailure : "Check performed on signature failed.",
						notifiedPointerFailure : "Get notified pointer(s) failure.",
						switchCheckToRunError : "Switch check to run value failure.",
						runTlccError : "Error occured during TL Conformance Checker process. Please try again. If the problem persists, please contact the administrator of the platform.",
						tlIdIncorrect : "TrustedList ID in url path is incorrect. Type a integer or a country code.",
						getArchivedProdsError : "Error occured while getting previous TL production.",
						getChangesArchivedProdsError : "Error occured while getting changes from archvied TL production.",
						signatureChangeError : "Error occured while getting signature changes."
					},

					cookieFactory : {
						validityError : "The requested repository doesn’t exist.",
						validityFailure : "Check repository validity failure.",
						createFailure : "Create new repository failure.",
						createCookie : "<div> <label><u>Creation of a repository for draft TLs</u></label></div>"
								+ "<div>A personal repository of draft TLs has been created for you under your <b>“My Drafts”</b> menu entry. This repository will gather all draft TLs you create.</div>"
								+ "<div>A unique identifier has been generated for that repository. For you information, this id can be seen in the URL of the page in the address bar of your browser.</div>"
								+ "<br/><div> <label><u>How to access it</u></label></div>"
								+ "<p>For your best convenience, a <b>cookie</b> has been set in this browser that refers to that “My Drafts” repository so that when you come back later, you will find the same repository, with your drafts in it.</p>"
								+ "<div>Please note however that, as a cookie can be deleted by maintenance operation on your PC, we strongly encourage you to <u>bookmark</u> this page for later reference.</div>"
								+ "<div>Another way of remembering the page is to copy-paste the URL in a document, an email, or via any mean that can store textual information.</div>"
								+ "<div>With this URL you can:</div>"
								+ "<div class='alinea'>-	<b>Access</b> the same repository later on, and find the drafts you created.</div>"
								+ "<div class='alinea'>-	<b>Share</b> your repository with other people (e.g. colleagues) by sending them the URL.</div>"
								+ "<br/><div> <label><u>Share of a repository</u></label></div>"
								+ "<p>Sharing your repository with others can be a valuable way of working together on the content of a new TL before publication. If you have shared your repository, please note however that anyone knowing the URL can <b>browse/edit/delete</b> (and sign with its own signature mean) your draft TL(s). If so, and if you are about to publish on the internet the draft TL as the official TL of your Member State, we strongly encourage you to keep a local copy on your PC, to prevent someone else from silently or inadvertently modifying your TL just before signature and publication.</p>"
								+ "<p>If you want to <b>stop sharing</b> the repository to others, we advise you to generate a new repository (see the button on the “My Drafts” page). The persons to whom you shared the previous repository will still be able to access it, but you will start your new work  in a new personal repository.</p>"
								+ "<div><label><u>Retention policy</u></label></div>"
								+ "<div>The repository will be kept <b>2 months</b> after the last draft TL in it is deleted. As long as the repository contains draft TL(s), it will be kept accessible (please refer to the trusted list retention policy).</div>",
						replaceStore : "<div>From the information present in the cookie, it seems that the repository you are trying to access is not your usual “My Drafts” repository.<div>Please click OK if you want to replace your repository with the one designated in this URL.</div><div> Clicking Cancel will abort the access, so that you can for instance bookmark your current repository for later use before switching to the new repository.</div>",
						invalidUrl : "The “My Drafts” repository you are trying to access has expired.",
						invalidDraftStore : "Your usual “My Drafts” repository you are trying to access has expired.",
						newStore : "<div>This will generate a new repository and drop the current one. The persons to whom you <b>shared</b> (if any) the current repository will still be able to access it, but you will now point to a new and <b>empty</b> personal repository.</div>"
								+ "If you want to be able to access this current repository in the future, please <b>bookmark</b> this page before proceeding."
								+ "If you want to transfer some your existing drafts to the newly created repository, please <b>export</b> them beforehand, and import them in the new repository once created."
								+ "<p>Are you sure you want to <b>continue</b> ?</p>",
						repository : "Repository information"
					},

					notificationController : {
						getDraftFailure : "Loading of the notifications list has failed. Please try again. If the problem persists, please contact the administrator of the platform.",
						getOneDraftFailure : "Loading notification draft failure.",
					},

					httpStatus : {
						notAuthorized : "You are not authorized to perform the current operation or your session has expired.",
						tlNotFound : "Trusted list file not found. If the problem persist, send an email to: EC-TL-Service@ec.europa.eu.",
						titleApplicationError : "Application error",
						titleNotAuthorized : "Not authorized",
						titleNotFound : "Not found",
						titleBadRequest : "Bad Request",
						titleForbidden : "Forbidden",
						titleError : "Error"

					},

					modalTitle : {
						warning : "Warning",
						applicationError : "Application error",
						signatureError : "Signature error",
						information : "Information",
						repository : "Repository information",
						confirmation : "Confirmation"
					},

					pointerController : {
						digitalDouble : "The certificate is already in the digital identities : ",
						digitalMultipleDouble : "Those certificates are already in the digital identities : ",
						getLOTLDigital : "This will replace the existing Digital Identities by those referenced from the OJ, do you want to continue ?",
						getLOTLDigitalError : " Get Digital Identities error."
					},

					systemController : {
						loadingFailure : "Run loading job failure.",
						rulesFailure : "Run rules validation failure.",
						signatureFailure : "Run signature validation failure.",
						retentionFailure : "Run retention policy failure.",
						serviceDataFailure : "Run service data reload failure.",
						signatureAlertFailure : "Run signature alerting failure.",
						approachBreakAlertFailure : "Run TL status break alerting failure.",
						lotlValidationFailure : "Run LOTL validation failure.",
						cacheCountriesFailure : "Clean countries cache failure.",
						cachePropertiesFailure : "Clean properties cache failure.",
						cacheCheckFailure : "Clean checks cache failure.",
						cacheCountriesSuccess : "Clean countries cache success.",
						cachePropertiesSuccess : "Clean properties cache success.",
						cacheChecksSuccess : "Clean checks cache success."
					},

					userController : {
						userAdded : "User %NAME% added.",
						updateSuccessful : "Update successful."
					},

					newsController : {
						loading_news : "Loading news..."
					},

					retentionController : {
						loading_data : "Loading datas...",
						loading_failure : "Loading datas failed.",
						delete_draftstore_failure : "Delete draftstore failed."
					},

					draftMergeController : {
						merge_changes_error : "An error occurred during the calcul of conflicts between draft(s).",
						merge_perform_error : "An error occurred during the merge of draft(s)."
					},

					wizardSigningCertificate : {
						form_uncomplete : "Please fill all the fields of the form before submit your request."
					}

				});
