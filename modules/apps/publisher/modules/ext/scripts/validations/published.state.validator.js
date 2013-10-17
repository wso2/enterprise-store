/*
 Description: The validator prevents edits from occuring to assets in the Published state. It checks the state of
 the asset if it is present for the Published state.
 Filename: published.state.validator.js
 Created Date: 17/10/2013
 */
var validatorModule = function () {

    var log = new Log('published.state.validator');
    var STATE_PUBLISHED = 'Published';

    /*
    The function checks if the validator can execute,It can execute if the model and templates are present.
     */
    function isApplicable(context) {
        var model = context.model;
        var template = context.template;

        //Check if the template and model are given
        if ((template) && (model)) {

            return true;
        }

        return false;
    }

    /*
     The function checks if the state of the asset is Published.
     @context: The request context containing the model and report objects
     @return: True if other validators should be executed,else false.
     */
    function execute(context) {
        var model = context.model;
        var report = context.report;
        var stateField = model.get('*.lifecycleState') || null;
        var state = stateField ? stateField.value : '';

        //Check if the state is the same
        if (state == STATE_PUBLISHED) {

            report.record('lifecycleState', 'Edit operations have been disabled in this state.');
        }

        return true;

    }

    return{
        isApplicable: isApplicable,
        execute: execute
    }
};
