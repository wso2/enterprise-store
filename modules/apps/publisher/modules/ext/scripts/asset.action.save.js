var meta={
	use:'action',
    purpose:'save',
	type:'form',
    source:'default',
	applyTo:'*',
	required:['model','template'],
	name:'asset.action.save'
};

/*
Description:Saves the contents of the model to an artifact instance and then retrieves the
            id
Filename: asset.action.save.js
Created Date: 8/8/2013
 */


var module=function(){

    var configs=require('/config/publisher.json');
    var log=new Log();


	return{
		execute:function(context){

            var utility=require('/modules/utility.js').rxt_utility();

            log.debug('Entered : '+meta.name);

            log.debug(stringify(context.actionMap));

            var model=context.model;
            var template=context.template;

            var name=model.getField('overview.name').value;
            var version=model.getField('overview.version').value;
            var shortName=template.shortName;

            log.debug('Artifact name: '+name);

            log.debug('Converting model to an artifact for use with an artifact manager');

            //Export the model to an asset
            var asset=context.parent.export('asset.exporter');

            log.debug('Finished exporting model to an artifact');

            //Save the artifact
            log.debug('Saving artifact with name :'+name);


            //Get the artifact using the name
            var rxtManager=context.rxtManager;

            var artifactManager=rxtManager.getArtifactManager(shortName);

            artifactManager.add(asset);

            //name='test-gadget-7';

            log.debug('Finished saving asset : '+name);

            //The predicate object used to compare the assets
            var predicate={
              attributes:{
                  overview_name:name,
                  overview_version:version
              }
            };
            var artifact=artifactManager.find(function(adapter){
                //Check if the name and version are the same
               //return ((adapter.attributes.overview_name==name)&&(adapter.attributes.overview_version==version))?true:false;
               return utility.assertEqual(adapter,predicate);
            },1);

            log.debug('Locating saved asset: '+stringify(artifact)+' to get the asset id.');

            var id=artifact[0].id||' ';

            log.debug('Setting id of model to '+id);

            //Save the id data to the model
            model.setField('*.id',id);

            log.debug('Finished saving asset with id: '+id);
		}
	}
};
