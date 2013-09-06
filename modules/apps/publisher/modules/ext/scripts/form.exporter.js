var meta={
	use:'export',
	type:'form',
	required:['model','template']
};

/*
 Description: Converts the data and template to format that can be used to render a form.
 Filename:form.exporter.js
 Created Dated: 11/8/2013
 */

var module=function(){

    var log=new Log();
	
	/*
	 * Go through each table and extract field data
	 */
	function fillFields(table,fieldArray,template){

        //var username=obtainUserNameFromSession();
        //log.debug('logged in user: '+username);
		//Go through each field
		for each(var field in table.fields){
			
			//Obtain the field details from the template
			var fieldTemplate=template.getField(table.name,field.name);
			
			//We ignore the field if it is not in the template
			if(!fieldTemplate){
                log.debug('Ignoring field: '+stringify(fieldTemplate));
				return;
			}
			var data={};
			
			data['name']=table.name.toLowerCase()+'_'+field.name.toLowerCase();
			data['label']=(fieldTemplate.label)?fieldTemplate.label:field.name;
			data['isRequired']=(fieldTemplate.required)?true:false;
			data['isTextBox']=(fieldTemplate.type=='text')?true:false;
			data['isTextArea']=(fieldTemplate.type=='text-area')?true:false;
			data['isOptions']=(fieldTemplate.type=='options')?true:false;

            data['isReadOnly']=(fieldTemplate.meta.readOnly)?fieldTemplate.meta.readOnly:false;

			data['value']=field.value;
			
			data['valueList']=csvToArray(fieldTemplate.value||'');
			
			fieldArray.push(data);
		}

		return fieldArray;
	}

    /*
    The function obtains the currently logged in user from the session
     */
    function obtainUserNameFromSession(){

        var username='unknown';
        try{
            username=session.get('LOGGED_IN_USER');
        }
        catch(e){
            log.debug('Unable to retrieved logged in user from sessions.The following exception was thrown: '+e);
        }
        return username;
    }
	
	/*
	 * Fills all of the tables except the *(global)
	 */
	function fillTables(model,template){
		
		var fieldArray=[];

		//Go through each table in the model
		for each(var table in model.dataTables){
			
			//Ignore if *
			if(table.name!='*'){
				fillFields(table,fieldArray,template);
			}
		}

        log.info('Fields: '+stringify(fieldArray));
		
		return fieldArray;
	}

    /*
    The function converts a string comma seperated value list to an array
    @str: A string representation of an array
    TODO: Move to utility script
     */
	function csvToArray(str){
		var array=str.split(',');
		return array;
	}
	
	function fillMeta(model,template){
		var meta={};
		meta['shortName']=template.shortName;
		meta['singularLabel']=template.singularLabel;
		meta['pluralLabel']=template.pluralLabel;

        log.info('Meta: '+stringify(meta));
		return meta;
	}
	
	function fillInfo(model){
		var info={};
		
		var field=model.getField('*.id');
		info['id']=field?field.getValue():'';
		
		field=model.getField('*.lifecycle');
		info['lifecycle']=field?field.getValue():'';
		
		field=model.getField('overview.version');
		info['version']=field?field.getValue():'';
		
		field=model.getField('*.lifecycleState');

		info['lifecycleState']=field?field.getValue():'';

        log.info('Info: '+stringify(info));
		
		return info;
	}
	
	return{
		execute:function(context){

            log.info('Entered: '+meta.type);

			var model=context.model;
			var template=context.template;
			
			var struct={};

			//TODO: Move this check outside
			if((!model)||(!template)){
                log.debug('Required parameters: '+meta.required+'not available to adapter');
				throw 'Required model and template data not present';
			}

			var tables= fillTables(model,template);
			
			struct['fields']=tables;
			struct['meta']=fillMeta(model,template);
			struct['info']=fillInfo(model);

            log.info('Leaving: '+meta.type);
			
			return struct;

		}
	}
};
