#!/usr/bin/php
<?php

$infilename = "foundations-jsoninput.txt";
$outfilename = "output.txt";

class ArrayTuple {
	/** Elements in this set */
	private $elements;
	
	/** the number of elements in this set */
	private $size = 0;
	
	/**
	 * Constructs this set.
	 */        
	public function ArrayTuple() {
			$this->elements = array();
	}
			
	/**
	 * @param any $element
	 * @returns true if the specified element was
	 * added to this set.
	 */
	public function add($element) {
			if (! in_array($element, $this->elements)) {
					$this->elements[] = $element;
					$this->size++;
					return true;
			}
			return false;
	}
	
	/**
	 * @param array $collection
	 * 
	 * @returns true if any of the elements in the
	 * specified collection where added to this set. 
	 */        
	public function addAll($collection) {
			$changed = false;
			foreach ($collection as $element) {
					if ($this->add($element)) {
							$changed = true;
					}
			}
			return $changed;
	}
	
	/**
	 * Removes all the elements from this set.
	 */        
	public function clear() {
			$this->elements = array();
			$this->size = 0;
	}
	
	/**
	 * @param any $element
	 * @returns true if this set contains the specified
	 * element.
	 */        
	public function contains($element) {
			return in_array($element, $this->elements);
	}
	
	/**
	 * @param array $collection
	 * @returns true if this set contains all the specified
	 * element. 
	 */        
	public function containsAll($collection) {
			foreach ($collection as $element) {
					if (! in_array($element, $this->elements)) {
							return false;
					}
			}
			return true;
	}
	
	/**
	 * @returns true if this set contains no elements. 
	 */        
	public function isEmpty() {
			return count($this->elements) <= 0;
	}
	
	/**
	 * @returns an iterator over the elements in this set.
	 */        
	public function iterator() {
			return new SimpleIterator($this->elements);
	}
	
	/**
	 * @param any $element
	 * @returns true if the specified element is removed.
	 */        
	public function remove($element) {
			if (! in_array($element, $this->elements)) return false;
			
			foreach ($this->elements as $k => $v) {
					if ($element == $v) {
							unset($this->elements[$k]);
							$this->size--;
							return true;
					}
			}                
	}
	
	/**
	 * @param array $collection
	 * @returns true if all the specified elemensts
	 * are removed from this set. 
	 */
	public function removeAll($collection) {
			$changed = false;
			foreach ($collection as $element) {
					if ($this->remove($element)) {
							$changed = true;
					} 
			}
			return $changed;
	}
	
	/**
	 * Retains the elements in this set that are
	 * in the specified collection.  If the specified
	 * collection is also a set, this method effectively
	 * modifies this set into the intersection of 
	 * this set and the specified collection.
	 * 
	 * @param array $collection
	 *
	 * @returns true if this set changed as a result
	 * of the specified collection.
	 */        
	public function retainAll($collection) {
		$changed = false;
		foreach ($this->elements as $k => $v) {
				if (! in_array($v, $collection)) {
						unset($this->elements[$k]);
						$this->size--;
						$changed = true;
				}
		}
		return $changed;
	}
	
	/**
	 * @returns the number of elements in this set.
	 */        
	public function size() {
		return $this->size;        
	}
	
	/**
	 * @returns an array that contains all the 
	 * elements in this set.
	 */
	public function toArray() {
		$elements = $this->elements;
		return $elements;
	}
	
	/**
	 * @returns an array that contains all the 
	 * elements in this set.
	 */
	public function output() {
		$elements = $this->elements;
		echo "(";
		$lastKey = end(array_keys($elements));
		foreach($elements as $key => $element) {
			if(is_object($element) ) {
				$element->output();
			} else {
				echo $element;
			}
			echo ($key == $lastKey ? '' : ',');
		}
		echo ")";
	}
	
}


class ArraySet {
	/** Elements in this set */
	private $elements;
	
	/** the number of elements in this set */
	private $size = 0;
	
	/**
	 * Constructs this set.
	 */        
	public function ArraySet() {
			$this->elements = array();
	}
			
	/**
	 * @param any $element
	 * @returns true if the specified element was
	 * added to this set.
	 */
	public function add($element) {
			if (! in_array($element, $this->elements)) {
					$this->elements[] = $element;
					$this->size++;
					return true;
			}
			sort($this->elements);
			return false;
	}
	
	/**
	 * @param array $collection
	 * 
	 * @returns true if any of the elements in the
	 * specified collection where added to this set. 
	 */        
	public function addAll($collection) {
			$changed = false;
			foreach ($collection as $element) {
					if ($this->add($element)) {
							$changed = true;
					}
			}
			sort($this->elements);
			return $changed;
	}
	
	/**
	 * Removes all the elements from this set.
	 */        
	public function clear() {
			$this->elements = array();
			$this->size = 0;
	}
	
	/**
	 * @param any $element
	 * @returns true if this set contains the specified
	 * element.
	 */        
	public function contains($element) {
			return in_array($element, $this->elements);
	}
	
	/**
	 * @param array $collection
	 * @returns true if this set contains all the specified
	 * element. 
	 */        
	public function containsAll($collection) {
			foreach ($collection as $element) {
					if (! in_array($element, $this->elements)) {
							return false;
					}
			}
			return true;
	}
	
	/**
	 * @returns true if this set contains no elements. 
	 */        
	public function isEmpty() {
			return count($this->elements) <= 0;
	}
	
	/**
	 * @returns an iterator over the elements in this set.
	 */        
	public function iterator() {
			return new SimpleIterator($this->elements);
	}
	
	/**
	 * @param any $element
	 * @returns true if the specified element is removed.
	 */        
	public function remove($element) {
			if (! in_array($element, $this->elements)) return false;
			
			foreach ($this->elements as $k => $v) {
					if ($element == $v) {
							unset($this->elements[$k]);
							sort($this->elements);
							$this->size--;
							return true;
					}
			}                
	}
	
	/**
	 * @param array $collection
	 * @returns true if all the specified elemensts
	 * are removed from this set. 
	 */
	public function removeAll($collection) {
			$changed = false;
			foreach ($collection as $element) {
					if ($this->remove($element)) {
							$changed = true;
					} 
			}
			return $changed;
	}
	
	/**
	 * Retains the elements in this set that are
	 * in the specified collection.  If the specified
	 * collection is also a set, this method effectively
	 * modifies this set into the intersection of 
	 * this set and the specified collection.
	 * 
	 * @param array $collection
	 *
	 * @returns true if this set changed as a result
	 * of the specified collection.
	 */        
	public function retainAll($collection) {
		$changed = false;
		foreach ($this->elements as $k => $v) {
				if (! in_array($v, $collection)) {
						unset($this->elements[$k]);
						$this->size--;
						$changed = true;
				}
		}
		return $changed;
	}
	
	/**
	 * @returns the number of elements in this set.
	 */        
	public function size() {
		return $this->size;        
	}
	
	/**
	 * @returns an array that contains all the 
	 * elements in this set.
	 */
	public function toArray() {
		$elements = $this->elements;
		return $elements;
	}
	
	/**
	 * @returns an array that contains all the 
	 * elements in this set.
	 */
	public function output() {
		$elements = $this->elements;
		echo "{";
		$lastKey = end(array_keys($elements));
		foreach($elements as $key => $element) {
			if(is_object($element) ) {
				$element->output();
			} else {
				echo $element;
			}
			echo ($key == $lastKey ? '' : ',');
		}
		echo "}";
	}
	
}

// Error var for displaying error to screen if BAD INPUT occurs
$error = "";

$jsoninput = json_decode(file_get_contents($infilename),1);
if(is_null($jsoninput)) $error .= "JSON Could not be decoded.";

echo "<pre>";
if(isset($_GET['dumpfile'])) { echo file_get_contents($infilename); die; }
if(isset($_GET['dumpinput'])) { var_export($jsoninput); die; }

// This stores variables once we have evaluated them so we can back reference and output later
$known_variables = array();

// Callback function to replace all "equal" operators which are descendents of another equal operator with "assignment"
function update_assignment(&$item, $key) {
	if($key==="operator" && $item==="equal") $item = "compare";
}

function process($input) {
	global $known_variables, $error;
	
	$type = gettype($input);
	switch($type) {
        case "integer":
			return($input);
		break;
        case "string":
			$error .= "Process was given a string: $input";
			return false;
		break;
        case "object":
			$error .= "Process was given an Object.\n\n";
			process(get_object_vars($input));
		break;
        case "array":
			$error .= "Process was given array with ".count($input)." elements\n";
			foreach($input as $key => $value) {
				switch(true) {
					case (gettype($key)=="integer"):
						if(process($value)===false) return false;
					break;
					case ($key==="statement-list"):
						// We're looking at an element of the input array with key "statement-list". That should only happen right at the start, run process on the contents of the element
						$error .= "Begin list of statements:\n";
						if(process($value)===false) return false;
					break;
					case ($key==="operator"):
						// We're looking at an element of the input array with key "statement-list". That should only happen right at the start, run process on the contents of the element
						$error .=  "Operator found: '$value'.\nProcessing arguments:\n";
						//foreach($input['arguments'] as $argument) echo "Argument: $argument\n";
						if($value==="equal") {
							array_walk_recursive($input['arguments'], 'update_assignment');
							
							// Since this is the left hand side of each operation, we are assuming it should be a "variable". If it isn't, output error
							if(isset($input['arguments'][0]['variable']) ) {
								// Let's look at the second side of the operation and add the output to our known variables list for later printing
								if( gettype($input['arguments'][1]) === "integer" ) {
									// No more processing required, we are done with this operation! \o/
									$lhs = $input['arguments'][0]['variable'];
									$rhs = $input['arguments'][1];
									// Add variable to known variables and print the output
									$known_variables[$lhs] = $rhs;
									echo "$lhs = $rhs;\n";
								} else {
									// The right hand side has an expression on it, or a set, or a tuple or something... process it!
									$lhs = $input['arguments'][0]['variable'];
									$rhs = process($input['arguments'][1]);
									if($rhs===false) return false;
									$known_variables[$lhs] = $rhs;
									
									echo $lhs;
									echo " = ";
									if($rhs instanceof ArraySet) {
										$rhs->output();
									} elseif($rhs instanceof ArrayTuple) {
										$rhs->output();
									} else {
										echo($rhs);
									}
									echo ";\n";
								}
							} else {
								$error .= "Left hand side of equal operator is not a variable, it is: ".var_export($input['arguments'][0],1);
								return false;
							}
						} elseif($value==="set") {
							// Let's instantiate a set object add some interesting things to it
							$set = new ArraySet();
							foreach($input['arguments'] as $argument) {
								//echo "PROCESSING ARGUMENT OF SET: $argument\n\n";
								if(is_array($argument) && isset($argument['variable'])) {
									//echo "$argument is a VARIABLE!\n";
									// in theory, add the variable value (from known variables) to the set
									if(isset($known_variables[$argument['variable']])) {
										$set->add($known_variables[$argument['variable']]);
									} else {
										return("undefined!");
									}
								} elseif(is_array($argument) && isset($argument['operator'])) {
									//echo "$argument is an OPERATOR!\n";
									// process operation, add output to set
									$argument = process($argument);
									if($argument===false) return false;
									$set->add($argument);
								} elseif(gettype($argument)==="integer") {
									//echo "$argument is an INTEGER!\n";
									// add integer to set
									$set->add($argument);
								} else {
									$error .= "Set contains non-integer, non-variable, non-operator expression: $argument";
									return false;
								}
							}
							return $set;
						} elseif($value==="tuple") {
							// Let's instantiate a tuple object add some interesting things to it
							$tuple = new ArrayTuple();
							foreach($input['arguments'] as $argument) {
								if(is_array($argument) && isset($argument['variable'])) {
									// in theory, add the variable value (from known variables) to the tuple
									if(isset($known_variables[$argument['variable']])) {
										$tuple->add($known_variables[$argument['variable']]);
									} else {
										return("undefined!");
									}
								} else {
									// process operation, add output to tuple
									$argument = process($argument);
									if($argument===false) return false;
									$tuple->add($argument);
								}
							}
							return $tuple;
						} elseif($value==="member") {
							// Since this is the left hand side of each operation, we are assuming it should be a "variable". If it isn't, output error
							if(isset($input['arguments'][0]['variable'])) {
								$lhs = $known_variables[$input['arguments'][0]['variable']];
							} else {
								$argument = process($input['arguments'][0]);
								if($argument===false) return false;
								$lhs = process($argument);
							}
							if(isset($input['arguments'][1]['variable'])) {
								$rhs = $known_variables[$input['arguments'][1]['variable']];
							} else {
								$argument = process($input['arguments'][1]);
								if($argument===false) return false;
								$rhs = process($argument);
							}
							
							if( $rhs instanceof ArraySet ||  $rhs instanceof ArrayTuple ) {
								if( $rhs->contains($lhs) ) return 1;
								else return 0;
							} else {
								//echo "In our member comparison, we encountered a non-object: ".var_dump($rhs)."\n";
								return "undefined!";
							}
						} elseif($value==="compare") {
							
							if(isset($input['arguments'][0]['variable'])) {
								$lhs = $known_variables[$input['arguments'][0]['variable']];
							} else {
								$argument = process($input['arguments'][0]);
								if($argument===false) return false;
								$lhs = process($argument);
							}
							if(isset($input['arguments'][1]['variable'])) {
								$rhs = $known_variables[$input['arguments'][1]['variable']];
							} else {
								$argument = process($input['arguments'][1]);
								if($argument===false) return false;
								$rhs = process($argument);
							}
							
							if( !is_null($lhs) && !is_null($rhs) ) {
								if($lhs == $rhs) return 1;
								else return 0;
							} else {
								return "undefined!";
							}
						} else {
							$error .= "Undefined operator: $value";
							return false;
						}
					break;
					case ($key==="arguments"):
						// Do nothing, arguments are handled by operator construct
					break;
					default: 
						$error .= "Array key not recognized: $key \n Dumping value: ".var_export($value,1);
						return false;
				}
			}
		break;
        default:
            $error .= "Process was given an input type it could not process. Dumping variable: ".var_export($input,1);
			return false;
	}
	// SUCCESSFULLY PROCESSED ENTIRE INPUT, WOOO
	return true;
}

// Catch output
ob_start();	
if( process($jsoninput)===true ) {
	file_put_contents($outfilename, ob_get_clean() );
	echo "Successfully processed input file: $infilename.\nOutput has been written to file: $outfilename";
} else {
	file_put_contents($outfilename, "BAD INPUT" );
	echo "An error occurred, debug output: \n\n$error";
}
?>