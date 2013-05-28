/*
 * HTML5 Forms Fallback for older and unsupporting browsers
 * Using jQuery, jQuery UI, Modernizr, Webforms2, and other jQuery Plugins
 * 
 * 2010 Cristian I. Colceriu
 *
 * www.ghinda.net
 * contact@ghinda.net
 *
 */
 
/* Slide
 * input[type=range] fallback
 *
 * using jQuery UI Slider
 */
var initSlider = function() {			
	$j('input[type=range]').each(function() {
		var $jinput = $j(this);
		var $jslider = $j('<div id="' + $jinput.attr('id') + '" class="' + $jinput.attr('class') + '"></div>');
		var step = $jinput.attr('step');
		
		$jinput.after($jslider).hide();
						
		$jslider.slider({
			min: $jinput.attr('min'),
			max: $jinput.attr('max'),
			step: $jinput.attr('step'),
			change: function(e, ui) {
				$j(this).val(ui.value);
			}
		});
	});
};

if(!Modernizr.inputtypes.range){
	$j(document).ready(initSlider);
};

/* Numeric Spinner
 * input[type=number] fallback
 * 
 * using jQuery Spinner plugin by Brant Burnett(http://btburnett.com/)
 */
var initSpinner = function() {			
	$j('input[type=number]').each(function() {
		var $jinput = $j(this);
		$jinput.spinner({
			min: $jinput.attr('min'),
			max: $jinput.attr('max'),
			step: $jinput.attr('step')
		});
	});
};
if(!Modernizr.inputtypes.number){		
	$j(document).ready(initSpinner);
};

/* Datepicker
 * input[type=date] fallback
 *
 * using jQuery UI Datepicker
 */
var initDatepicker = function() {
	$j('input[type=date]').each(function() {
		var $jinput = $j(this);
		$jinput.datepicker({
			minDate: $jinput.attr('min'),
			maxDate: $jinput.attr('max'),
			dateFormat: 'yy-mm-dd'
		});
	});
};

if(!Modernizr.inputtypes.date){
	$j(document).ready(initDatepicker);
};

/* ColorPicker
 * input[type=color] fallback
 *
 * using jQuery ColorPicker plugin by Stefan Petre(http://www.eyecon.ro/)
 * http://www.eyecon.ro/colorpicker/
 */
var initColorpicker = function() {
	$j('input[type=color]').each(function() {
		var $jinput = $j(this);
		$jinput.ColorPicker({
			onSubmit: function(hsb, hex, rgb, el) {
				$j(el).val(hex);
				$j(el).ColorPickerHide();
			}
		});
	});			
};

if(!Modernizr.inputtypes.color){
	$j(document).ready(initColorpicker);
};

/* Placeholder
 * placeholder attribute fallback
 *
 * using jQuery Placehold plugin by Viget Inspire(http://www.viget.com/inspire/)
 * http://www.viget.com/inspire/a-jquery-placeholder-enabling-plugin/
 */
var initPlaceholder = function() {
	$j('input[placeholder]').placehold({
		placeholderClassName: 'placeholder'
	});
};

if(!Modernizr.input.placeholder){
	$j(document).ready(initPlaceholder);
};