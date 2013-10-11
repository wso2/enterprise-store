/*
 Description: The script is responsible for the implementing the client side logic;
    - Promoting an asset to the next stage of its life-cycle
    - Demoting an asset to the previous stage of its life-cycle
    - Obtaining the check list for a given state
    - Handling the click events associated with the check list items by calling the remote api responsible for
      ticking an asset check list item.
      Sources:
        Disabling a button with jQuery using disabled property
        http://stackoverflow.com/questions/1414365/how-to-disable-enable-an-input-with-jquery
 Filename: lifecycle.asset.js
 Created Date: 23/8/2013
 */

var onCheckListItemClick=function(){};
var sugyamaModule=sugyamaModule();
var sugyama=new sugyamaModule.Sugyama();


$(function(){

    /*
    Obtain the current url to get the id and asset type
     */
    var url=window.location.pathname;

    //Break the url into components
    var comps=url.split('/');


    //Given a url of the form /pub/api/asset/{asset-type}/{asset-id}
    //length=5
    //then: length-2 = {asset-type} length-1 = {asset-id}
    var id=comps[comps.length-1];
    var asset=comps[comps.length-2];

    console.log(asset);
    console.log(id);

    buildCheckList(asset,id);
    buildLCGraph();
    buildHistory(asset,id);

    /*
    Promotes an asset
     */
  /*  $('#btn-asset-promote').on('click',function(e){
    	e.preventDefault();
       console.log('/publisher/api/lifecycle/Promote/'+asset+'/'+id);
        $.ajax({
          url:'/publisher/api/lifecycle/Promote/'+asset+'/'+id,
          type:'PUT',
          success:function(response){
              showAlert('Asset promoted successfully', 'success');


              $.ajax({
                  url:'/publisher/api/lifecycle/'+asset+'/'+id,
                  type:'GET',
                  success:function(response){
                      var statInfo=JSON.parse(response);
                      $('#state').html(statInfo.state);
                      $('#view-lifecyclestate').html(statInfo.state);
                      //disableActions(statInfo.actions);
                      buildCheckList(asset,id);
                      buildLCGraph();
                      buildHistory(asset,id);
                  },
                  error:function(response){
                      $('#state').html('Error obtaining state');
                  }
              });
          },
          error:function(response){
			showAlert('Error occured while promoting', 'error');
          }
        });
    }); */

    /*
    Demotes an asset
     */
 /*   $('#btn-asset-demote').on('click',function(e){
    	e.preventDefault();
        $.ajax({
            url:'/publisher/api/lifecycle/Demote/'+asset+'/'+id,
            type:'PUT',
            success:function(response){
                showAlert('Asset demoted successfully', 'success');
                $.ajax({
                    url:'/publisher/api/lifecycle/'+asset+'/'+id,
                    type:'GET',
                    success:function(response){
                        //Convert the response to a JSON object
                        var statInfo=JSON.parse(response);

                        $('#state').html(statInfo.state);
                        $('#view-lifecyclestate').html(statInfo.state);
                        //disableActions(statInfo.actions);
                        buildCheckList(asset,id);
                        buildLCGraph();
                        buildHistory(asset,id);
                    },
                    error:function(response){
                        $('#state').html('Error obtaining life-cycle state of asset.');
                    }
                });
            },
            error:function(response){
               showAlert('Error occured while demoting', 'error');
            }
        });


    });*/

    /*
    The method only enables and disables the appropriate actions
     */
    function disableActions(actions){

        //Eagerly disable all of the buttons
        var buttons=['btn-asset-promote','btn-asset-demote'];

        for(var buttonIndex in buttons){
            var button=buttons[buttonIndex];
            $('#'+button).prop('disabled',true);
        }

        //Enable only relevant buttons.
        for(var actionIndex in actions){
            var action=actions[actionIndex];
            if(action=='Promote'){
                $('#btn-asset-promote').prop('disabled',false);
            }
            else if(action=='Demote'){
                $('#btn-asset-demote').prop('disabled',false);
            }
        }
    }



    /*
    The function builds the LC graph
     */
    function buildLCGraph(){
        $.ajax({
            url:'/publisher/api/lifecycle/'+asset+'/'+id,
            type:'GET',
            success:function(response){
                var element=$('#canvas');
                if(element){
                    var paper=new Raphael('canvas');
                    //element.html(response);

                   /* if(!graph.Renderer.config.canvas.canvasElement){

                        graph.Renderer.config.canvas.canvasElement=element;

                        graph.Renderer.initRaphael();
                        graph.Renderer.render(graph.NMap);
                    }       */
                    var statInfo=JSON.parse(response);
                    sugyama.init(statInfo.lifecycle,paper);
                    var START_X=10;
                    var START_Y=50;
                    var VERTEX_RADIUS=22;
                    var LAYER_SEP=85;
                    var LAYER_SPACE=200;
                    sugyama.draw(START_X,START_Y,VERTEX_RADIUS,LAYER_SPACE,LAYER_SEP);

                    //graph.Renderer.setSelected(statInfo.state);
                    disableActions(statInfo.actions);
                    buildButtons(statInfo.actions);
                    highlightTransition(statInfo.state);
                }
                //$('#canvas').html(response);
            },
            error:function(response){
                $('#canvas').html('Error obtaining life-cycle state of asset.');
            }
        });
    }

    /*
     The function encapsulates all of the logic which occurs when a user clicks a
     lifecycle button
     @action: The action for the button
     */
    function buttonClickLogic(action){
        $.ajax({
            url:'/publisher/api/lifecycle/'+action+'/'+asset+'/'+id,
            type:'PUT',
            success:function(response){
                showAlert('Asset demoted successfully', 'success');
                $.ajax({
                    url:'/publisher/api/lifecycle/'+asset+'/'+id,
                    type:'GET',
                    success:function(response){
                        //Convert the response to a JSON object
                        var statInfo=JSON.parse(response);

                        $('#state').html(statInfo.state);
                        $('#view-lifecyclestate').html(statInfo.state);
                        //disableActions(statInfo.actions);
                        buildCheckList(asset,id);
                        buildLCGraph();
                        buildHistory(asset,id);
                    },
                    error:function(response){
                        $('#state').html('Error obtaining life-cycle state of asset.');
                    }
                });
            },
            error:function(response){
                showAlert('Error occured while demoting', 'error');
            }
        });
    }

    /*
    The function is used to populate the buttons
    @actions: The actions available to the current state
     */
    function buildButtons(actions){

        //Obtain the button container
        var BUTTON_CONTAINER='#form-actions';

        //Clear the button container of previous buttons
        $(BUTTON_CONTAINER).html('');

        for(var index in actions){
            var action=actions[index];

            //Populate  buttons based on the action
            var element=document.createElement('input');
            element.type='button';
            element.value=action;
            element.className='btn btn-primary pull-right';
            element.id='btn'+action;


            $(BUTTON_CONTAINER).append(element);
            $('#btn'+action).on('click',function(e){
                var clicked=e.target.value;
                e.preventDefault();
                buttonClickLogic(clicked);
            });
        }
    }

    /*
    The function is used to build the representation of the check list.
     */
    function buildCheckList(asset,id){

        //Check if the id exists before making a call
        if((asset=='')||(id=='')){
            console.log('omitting');
            return;
        }

        //Clear the checklist rendering area
        $('#checklist').html('');

        //Make a call to the lifecycle check list
        $.ajax({
            url:'/publisher/api/lifecycle/checklist/'+asset+'/'+id,
            type:'GET',
            success:function(response){

                var out='<ul>';

                var obj=JSON.parse(response);

                for(var index in obj.checkListItems){

                   var current=obj.checkListItems[index];

                   out+='<li><input type="checkbox" onclick="onCheckListItemClick(this,'+index+')" ';

                   if(current.checked){
                        out+='checked';
                   }

                   out+='>'+current.name+'</label></li>';
                }

                out+='</ul>';

                //Render the check list
                $('#checklist').html(out);
            }

        });
    }

    /*
    The function obtains the history information about the life-cycle changes
     */
    function buildHistory(asset,id){
        var version='1.0.0';
        //Make a call to the api to obtain the history
        var path='/publisher/api/lifecycle/information/history/'+asset+'/'+id+'/'+version;
        $.ajax({
            url:path,
            type:'GET',
            success:function(response){
                //console.log(response);
                var obj=JSON.parse(response);
                var out=createHistoryEntry(obj.item);
                $('#lc-history').html(out);
            },
            error:function(response){
               // console.log('lc history not retrieved');
            }
        });
    }

    function createHistoryEntry(items){
        var output='';

        for(var itemIndex in items){
            output+='<tr>';
            output+='<td><span class="dateFull"> '+items[itemIndex].timestamp+'</span></td>';
            output+='<td><a href="#">'+items[itemIndex].user+'</a> changed the asset from '+items[itemIndex].state
            +' to '+items[itemIndex].targetState + '</td>';
            output+='</tr>';
        }

        return output;
    }

    /*
    Click handler which detects when a check list item is clicked
     */
    function onCheckListItemClickHandler(checkbox,index){

        if(checkbox.checked){
            callCheckListItem(checkbox,index);
        }
        else{
            callUncheckListItem(checkbox,index);
        }
    }
    /*
    The function checks a life-cycle check list item by calling the checklist api method
     */
    function callCheckListItem(checkbox,checkListItemIndex){
        $.ajax({
            url:'/publisher/api/lifecycle/checklistitem/'+checkListItemIndex+'/'+asset+'/'+id,
            type:'POST',
            success:function(response){
                alert('Item checked successfully');
            },
            error:function(response){
                checkbox.checked=false; //Revert the checkbox to the previous state
                alert('Could not check item');
            }
        });
    }

    /*
    The function unchecks a life-cycle check list item by calling the checklist api method
     */
    function callUncheckListItem(checkbox,checkListItemIndex){
        $.ajax({
            url:'/publisher/api/lifecycle/checklistitem/'+checkListItemIndex+'/'+asset+'/'+id,
            type:'DELETE',
            success:function(response){
                alert('Item unchecked successfully');
            },
            error:function(response){
                checkbox.checked=true;  //Revert the checkbox to previous state
                alert('Could not uncheck item');
            }
        });
    }
    
    function showAlert(msg, type){
    	var alert = $('.widget-content .alert');
    	alert.removeClass().addClass('info-div alert alert-' + type).find('span').text(msg);
    	alert.delay(1000).fadeIn("fast").delay(2000).fadeOut("fast");
    }

	function highlightTransition(state){
      var elem = $('.'+state);
      elem.attr('fill', '#52B9E9');
      //$('#canvas').attr('data-current', state);
    }
   /*
   Click handlers for the checkboxes
    */
   onCheckListItemClick=onCheckListItemClickHandler;

});