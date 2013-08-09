
Description
-----------

Publisher application for UES.

It allows the following functionality;
	- Login has been integrated
	- View assets
	- Add a new asset (gadgets and sites)
	- Delete an existing asset (gadgets and sites) [Feature is present via API]
	
Note:
-----

-The publisher does not allow update operations
-Please copy and paste the lifecycle found in the registry.xml of the DEPENDENCIES folder . 


API
---

The following API calls have been implemented

	Noun	Url					Description
	---------------------------------------------------------------------------------------------------------------------
	GET  	/publisher/api/asset/{type}		Returns a template JSON object describing the structure of an asset
	GET  	/publisher/api/asset/{type}/{id}	Returns an asset of the given {type} and matching the {id}
	POST 	/publisher/api/asset/{type}		Creates a new asset of the given {type}
	DELETE 	/publisher/api/asset/{type}/{id}	Deletes an asset 
	POST 	/publisher/api/lifecycle/{type}/{id}	Attaches a lifecycle for the specified asset type with the id

Known Bugs:
-----------

	The created assets can only be viewed by a logged in user


	


