$(function() {		
		var forename = $( "#forename" ),
			surname = $( "#surname" ),
			email = $( "#email" ),
			address = $( "#address" ),
			item = $( "#item" ),
			allFields = $( [] ).add( forename ).add( surname ).add( email ).add( address ).add( item ),
			tips = $( ".validateTips" ),
			cart = 0;

		function updateTips( t ) {
			tips
				.text( t )
				.addClass( "ui-state-highlight" );
			setTimeout(function() {
				tips.removeClass( "ui-state-highlight", 1500 );
			}, 500 );
		}

		function checkLength( o, n, min, max ) {
			if ( o.val().length > max || o.val().length < min ) {
				o.addClass( "ui-state-error" );
				updateTips( "Length of " + n + " must be between " +
					min + " and " + max + "." );
				return false;
			} else {
				return true;
			}
		}

		function checkRegexp( o, regexp, n ) {
			if ( !( regexp.test( o.val() ) ) ) {
				o.addClass( "ui-state-error" );
				updateTips( n );
				return false;
			} else {
				return true;
			}
		}
		
		function checkSelect( o, value ) {
			if (value == "0") {
				o.addClass( "ui-state-error" );
				updateTips( "Please select a product to buy." );
				return false;
			} else {
				return true;
			}
		}
		
		function addToCart( productID ) {
			cart++;
			$("#cartbutton").html("<span class='ui-button-text'>Cart: "+cart+" products</span>");
			$("option[value='"+productID+"']").attr('selected', 'selected');
			$("#cartpopup").html("Product added to cart!");
			$("#cartpopup").dialog({ autoOpen: true, modal: true });
			setTimeout(function() {
				$('[aria-labelledby]="ui-dialog-title-cartpopup"').hide('explode','',500);
				$( "#cartpopup" ).dialog("close");
			}, 1000 );
			$("#item").change();
		}	
		
		$( "#dialog-form" ).dialog({
			width: 400,
			autoOpen: false,
			modal: true,
			buttons: {
				"Order": function() {
					var bValid = true;
					allFields.removeClass( "ui-state-error" );

					bValid = bValid && checkLength( forename, "forename", 3, 16 );
					bValid = bValid && checkLength( surname, "surname", 3, 16 );
					bValid = bValid && checkLength( email, "email", 6, 80 );
					bValid = bValid && checkLength( address, "address", 10, 150 );

					bValid = bValid && checkRegexp( forename, /^[a-z]([a-z])+$/i, "Forename may consist of a-z, begin with a letter." );
					bValid = bValid && checkRegexp( surname, /^[a-z]([a-z])+$/i, "Surname may consist of a-z, underscores, begin with a letter." );
					bValid = bValid && checkRegexp( email, /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i, "eg. ui@jquery.com" );

					//bValid = bValid && checkSelect( item, item.attr("value") );
					
					if ( bValid ) {
						$('#orderform').submit();
						$( this ).dialog( "close" );
					}
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				allFields.val( "" ).removeClass( "ui-state-error" );
			}
		});

		$( "#cartbutton" )
			.button()
			.click(function() {
				$( "#dialog-form" ).dialog( "open" );
			});
		
		$( ".addtocart" ).click(function() {
			addToCart( $(this).attr("value") );
		}).button();
		
		
		$("#item").asmSelect();
		$("#printbutton").button();
	});
