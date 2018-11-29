<?php 
// Benchmarking
ini_set('max_execution_time', 0); set_time_limit(0); $time_start = microtime(true);

if (!isset($_GET['action'])) {
	header("Location: http://andrewbeveridge.co.uk/coursework/dmml/cw1/run.php?action=report");
	die();
}

if ($_GET['action']=='report') { ?>
	<h1>Communities and Crime Data Set (<a href="http://archive.ics.uci.edu/ml/datasets/Communities+and+Crime">Source</a>)</h1>
	<table>
		<tr>
			<td>Field 1: </td>
			<td><img src='run.php?file=communities&column=0&action=histogram' /></td>
			<td>Field 2: </td>
			<td><img src='run.php?file=communities&column=1&action=histogram' /></td>
		</tr>
		<tr>
			<td>Field 3: </td>
			<td><img src='run.php?file=communities&column=2&action=histogram' /></td>
			<td>Field 4: </td>
			<td><img src='run.php?file=communities&column=3&action=histogram' /></td>
		</tr>
		<tr><td>Field 5: </td><td><img src='run.php?file=communities&column=4&action=histogram' /></td><td></td><td></td></tr>
	</table>
	<h1>Pima Indians Diabetes Data Set (<a href="http://archive.ics.uci.edu/ml/datasets/Pima+Indians+Diabetes">Source</a>)</h1>
	<table>
		<tr>
			<td>Field 1: </td>
			<td><img src='run.php?file=pima&column=0&action=histogram' /></td>
			<td>Field 2: </td>
			<td><img src='run.php?file=pima&column=1&action=histogram' /></td>
		</tr>
		<tr>
			<td>Field 3: </td>
			<td><img src='run.php?file=pima&column=2&action=histogram' /></td>
			<td>Field 4: </td>
			<td><img src='run.php?file=pima&column=3&action=histogram' /></td>
		</tr>
			<tr><td>Field 5: </td><td><img src='run.php?file=pima&column=4&action=histogram' /></td><td></td><td></td></tr>
	</table>
	<h1>Yeast Data Set (<a href="http://archive.ics.uci.edu/ml/datasets/Yeast">Source</a>)</h1>
	<table>
		<tr>
			<td>Field 1: </td>
			<td><img src='run.php?file=yeast&column=0&action=histogram' /></td>
			<td>Field 2: </td>
			<td><img src='run.php?file=yeast&column=1&action=histogram' /></td>
		</tr>
		<tr>
			<td>Field 3: </td>
			<td><img src='run.php?file=yeast&column=2&action=histogram' /></td>
			<td>Field 4: </td>
			<td><img src='run.php?file=yeast&column=3&action=histogram' /></td>
		</tr>
		<tr><td>Field 5: </td><td><img src='run.php?file=yeast&column=4&action=histogram' /></td><td></td><td></td></tr>
	</table>
<? die();
}

// Handy function
function delete_col(&$array, $offset) {
    return array_walk($array, function (&$v) use ($offset) {
        array_splice($v, $offset, 1);
    });
}

// LOAD RAW DATA based on chosen file to process
$file = $_GET['file'];
$raw = file_get_contents("$file.data");

// Set up file-specific initial columns to drop
if($file=='yeast') {
	$raw = preg_replace('|[ ]+|', ',', $raw);
	$dataColsToDrop = array(0);
} elseif ($file=='pima') {
	$dataColsToDrop = array();
} elseif ($file=='communities') {
	$dataColsToDrop = array(0,1,2,3,4);
}

// PARSE RAW DATA INTO ARRAY
// Get raw data from CSV into unmodified multidimensional array
$dataArray = array();
$dataLines = explode("\n",trim($raw));
foreach($dataLines as $dataLine) {
	$dataArray[] = explode(",",$dataLine);
}

// DROP COLUMNS WITH MISSING DATA
// Loop through all values in every row to populate list of missing data columns to drop
foreach($dataArray as $dataRowKey => $dataRow) {
	foreach($dataRow as $dataRowColKey => $dataRowColValue) {
		if( $dataRowColValue=='?' && !in_array($dataRowColKey,$dataColsToDrop) ) 
			$dataColsToDrop[] = $dataRowColKey;
	}
}
// Loop through each row and drop columns with missing data
$dataArrayNoMissingValues = array();
foreach($dataArray as $dataRowKey => $dataRow) {
	$dataArrayNoMissingValues[$dataRowKey] = array();
	foreach($dataRow as $dataRowColKey => $dataRowColValue) {
		if(!in_array($dataRowColKey,$dataColsToDrop)) 
			$dataArrayNoMissingValues[$dataRowKey][] = $dataRowColValue;
	}
}

// CLASSIFY LAST COLUMN
$dataArray = array();
foreach($dataArrayNoMissingValues as $rowKey => $row) {
	if($file=='yeast') {
		$row[count($row)-1] = $row[count($row)-1]=='CYT' ? 1 : 0;
	} elseif($file=='communities') {
		$row[count($row)-1] = $row[count($row)-1] <= 0.4 ? 0 : 1;
	}
	$dataArray[$rowKey] = $row;
}

// IF GET PARAM SAYS WE WANT NON NORMALIZED, DON'T NORMALIZE!
if(isset($_GET['normalize'])) {
	if($file=="communities") {
		// PERFORM Z-NORMALIZATION ON DATA USING AWK SCRIPT
		// Convert prepped (missing data removed) array back into space separated file for z-normalization with awk
		$dataArrayNoMissingValuesSpaced = $dataArray;
		foreach($dataArrayNoMissingValuesSpaced as $key => $row) {
			$dataArrayNoMissingValuesSpaced[$key] = implode(" ",$row);
		}
		$dataArrayNoMissingValuesSpaced = implode("\n",$dataArrayNoMissingValuesSpaced);
		file_put_contents("dataArrayNoMissingValuesSpaced.txt",$dataArrayNoMissingValuesSpaced);
		$dataArrayNoMissingValuesSpacedZNorm = `awk -f /home/andrewbe/public_html/coursework/dmml/cw1/znorm.awk < /home/andrewbe/public_html/coursework/dmml/cw1/dataArrayNoMissingValuesSpaced.txt`;
		// Reset data array, get normalised data from spaced awk output into multidimensional array
		$dataArray = array();
		$dataLines = explode("\n",trim($dataArrayNoMissingValuesSpacedZNorm));
		foreach($dataLines as $dataLine) {
			$rowArray = explode(" ",trim($dataLine));
			$dataArray[] = $rowArray;
		}
	} else {
		// PERFORM MIN-MAX NORMALIZATION ON DATA
		// Find max and min for each column in data
		$colMaxMin = array();
		foreach($dataArray[0] as $colKey => $val) {
			$col = array_column($dataArray, $colKey);
			$colMaxMin[$colKey]['max'] = max($col);
			$colMaxMin[$colKey]['min'] = min($col);
			$colMaxMin[$colKey]['diff'] = $colMaxMin[$colKey]['max'] - $colMaxMin[$colKey]['min'];
		}
		// Loop through all data rows 
		foreach($dataArray as $rowKey => $row) {
			// Loop through values and set new value to old value as percentage of max-min
			foreach($row as $colKey => $val) {
				$dataArray[$rowKey][$colKey] = 100*(($val-$colMaxMin[$colKey]['min'])/$colMaxMin[$colKey]['diff']);
			}
		}
	}
}
	
if($_GET['action']=='accuracy') {
	echo "<html><body><pre>";
	// Limit array sample size for testing
	//$dataArray = array_slice($dataArray,0,100);

	// 1-NN Accuracy Check
	$correctCount = 0;
	$failCount = 0;
	// External loop, through all rows in dataset
	foreach($dataArray as $externalRowKey => $externalRow) {
		//echo "\n\nInside External loop, External Row Key: $externalRowKey\n";
		// Create variable to store minimum distance and row key of nearest neighbour
		$minDistance = $internalRowKeyOfNN = 1000000;
		// Internal loop, through all rows in dataset (except for the external loop row!)
		foreach($dataArray as $internalRowKey => $internalRow) {
			//echo "\n Inside Internal loop, Internal Row Key: $internalRowKey\n";
			// Check if this row is the same as the external loop row - if so, skip
			if($externalRowKey == $internalRowKey) continue;
			// Create variable to store total distance between internal row and external
			$internalExternalDistance = 0;
			// Loop through all values in row from 0 to the last one minus 1, since the last field is the classifier which we /totally/ don't know about
			for($i=0; $i<=count($internalRow)-2; $i++) {
				// Add the distance of this individual value to the total distance
				$internalExternalDistance += pow($externalRow[$i] - $internalRow[$i], 2);
				//echo "  Calculating internalExternalDistance: ({$externalRow[$i]} - {$internalRow[$i]})^2 = $internalExternalDistance\n";
			}
			// Now we've looped through all the values and summed for the total distance, see if it's small
			if($internalExternalDistance < $minDistance ) {
				// We found a new smallest distance, so update the minDistance and set the new NN key
				$minDistance = $internalExternalDistance;
				//echo "  Found new minDistance of $minDistance while comparing internal row $internalRowKey with external row $externalRowKey.\n";
				$internalRowKeyOfNN = $internalRowKey;
			} else {
				//echo "  internalExternalDistance $internalExternalDistance was not less than minDistance $minDistance.\n"; 
			}
		}
		// We've finished the internal loop through all rows to find the NN for this external row, check class
		// Success, as class (field 99) of the external row was the same as the supposed nearest neighbour row
		if($externalRow[count($externalRow)-1] == $dataArray[$internalRowKeyOfNN][count($externalRow)-1]) {
			echo "Classified correctly   *** Ext: $externalRowKey NN: $internalRowKeyOfNN Dist: $minDistance\n";
			$correctCount++;
		} else {
			// Too bad...
			echo "Classified incorrectly --- Ext: $externalRowKey NN: $internalRowKeyOfNN Dist: $minDistance\n";
			$failCount++;
		}
	}
	
	$percentage = ($correctCount/count($dataArray))*100;
	echo "1-NN Correct: $correctCount ( {$percentage}% ), Failed: $failCount";
		
	$runtime = microtime(true) - $time_start;
	echo "\n\nTotal Execution Time: {$runtime}s";

} elseif($_GET['action']=='histogram') {
	function class0($row) { return($row[count($row)-1]==0); }
	function class1($row) { return($row[count($row)-1]==1); }
	$dataArrayClass0 = array_filter($dataArray, "class0");
	$dataArrayClass1 = array_filter($dataArray, "class1");
	delete_col($dataArrayClass0, count($dataArrayClass0[0])-1);
	delete_col($dataArrayClass1, count($dataArrayClass1[0])-1);
	$dataArrayClass0 = array_values($dataArrayClass0);
	$dataArrayClass1 = array_values($dataArrayClass1);
	
	$classColumnValues = array(0=>array(), 1=>array());
	foreach($dataArrayClass0[0] as $colKey => $val) {
		$classColumnValues[0][$colKey] = array_column($dataArrayClass0,$colKey);
	}
	foreach($dataArrayClass1[0] as $colKey => $val) {
		$classColumnValues[1][$colKey] = array_column($dataArrayClass1,$colKey);
	}
	
	// Populate buckets
	$classColumnBucketCounts = array(0=>array(), 1=>array());
	// For both classes
	foreach($classColumnValues as $class => $columns) {
		// Find max and min for each column in data
		foreach($columns as $colKey => $values) {
			$colMax = max(array_merge($classColumnValues[0][$colKey],$classColumnValues[1][$colKey]));
			$colMin = min(array_merge($classColumnValues[0][$colKey],$classColumnValues[1][$colKey]));
			$colBucketSize = ($colMax-$colMin)/5;
			//echo "Class $class, Column $colKey, Max: $colMax, Min: $colMin, Bucket Size: $colBucketSize <br />";
			$classColumnBucketCounts[$class][$colKey]=array(0=>0,1=>0,2=>0,3=>0,4=>0);
			// Loop through values and assign to buckets
			foreach($values as $value) {
				if($value < $colMin+$colBucketSize) {
					$classColumnBucketCounts[$class][$colKey][0]++;
				} elseif($value > $colMin+$colBucketSize && $value < $colMin+($colBucketSize*2) ) {
					$classColumnBucketCounts[$class][$colKey][1]++;
				} elseif($value > $colMin+($colBucketSize*2) && $value < $colMin+($colBucketSize*3) ) {
					$classColumnBucketCounts[$class][$colKey][2]++;
				} elseif($value > $colMin+($colBucketSize*3) && $value < $colMin+($colBucketSize*4) ) {
					$classColumnBucketCounts[$class][$colKey][3]++;
				} elseif($value > $colMin+($colBucketSize*4) ) {
					$classColumnBucketCounts[$class][$colKey][4]++;
				}
			}
			
			$bucketsTotal = array_sum($classColumnBucketCounts[$class][$colKey]);
			foreach($classColumnBucketCounts[$class][$colKey] as $bucket => $bucketFrequency) {
				$classColumnBucketCounts[$class][$colKey][$bucket] = $bucketFrequency/$bucketsTotal;
			}
		}
	}
	
	include("pChart/class/pData.class.php");
	include("pChart/class/pDraw.class.php");
	include("pChart/class/pImage.class.php");
	
	/* Create and populate the pData object */
	$MyData = new pData();
	if($file=="yeast") {
		$MyData->addPoints($classColumnBucketCounts[0][$_GET['column']],"OTHER");
		$MyData->addPoints($classColumnBucketCounts[1][$_GET['column']],"CYT");
	} else {
		$MyData->addPoints($classColumnBucketCounts[0][$_GET['column']],"C0");
		$MyData->addPoints($classColumnBucketCounts[1][$_GET['column']],"C1");
	}
	$MyData->setAxisName(0,"Frequency");
	$MyData->addPoints(array(0,1,2,3,4),"Buckets");
	$MyData->setSerieDescription("Buckets","Bucket");
	$MyData->setAbscissa("Buckets");

	/* Create the pChart object */
	$myPicture = new pImage(700,430,$MyData);

	/* Turn of Antialiasing */
	$myPicture->Antialias = FALSE;

	/* Add a border to the picture */
	$myPicture->drawRectangle(0,0,699,429,array("R"=>0,"G"=>0,"B"=>0));

	/* Set the default font */
	$myPicture->setFontProperties(array("FontName"=>"pChart/fonts/pf_arma_five.ttf","FontSize"=>10));

	/* Define the chart area */
	$myPicture->setGraphArea(60,40,650,400);

	/* Draw the scale */
	$scaleSettings = array("GridR"=>200,"GridG"=>200,"GridB"=>200,"DrawSubTicks"=>TRUE,"CycleBackground"=>TRUE);
	$myPicture->drawScale($scaleSettings);

	/* Write the chart legend */
	$myPicture->drawLegend(580,12,array("Style"=>LEGEND_NOBORDER,"Mode"=>LEGEND_HORIZONTAL));

	/* Turn on shadow computing */ 
	$myPicture->setShadow(TRUE,array("X"=>1,"Y"=>1,"R"=>0,"G"=>0,"B"=>0,"Alpha"=>10));

	/* Draw the chart */
	$myPicture->setShadow(TRUE,array("X"=>1,"Y"=>1,"R"=>0,"G"=>0,"B"=>0,"Alpha"=>10));
	$settings = array("Gradient"=>TRUE,"GradientMode"=>GRADIENT_EFFECT_CAN,"DisplayPos"=>LABEL_POS_INSIDE,"DisplayValues"=>TRUE,"DisplayR"=>255,"DisplayG"=>255,"DisplayB"=>255,"DisplayShadow"=>TRUE,"Surrounding"=>10);
	$myPicture->drawBarChart();

	/* Render the picture (choose the best way) */
	$myPicture->autoOutput("chart.png");
}
?>