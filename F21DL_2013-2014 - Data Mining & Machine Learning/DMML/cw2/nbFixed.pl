#!/usr/local/bin/perl
eval 'exec /usr/local/bin/perl -S $0 ${1+"$@"}'
    if $running_under_some_shell;
			# this emulates #! processing on NIH machines.
			# (remove #! line above if indigestible)

eval '$'.$1.'$2;' while $ARGV[0] =~ /^([A-Za-z_0-9]+=)(.*)/ && shift;
			# process any FOO=bar switches

# this script assumes that the data is space separated, and that
# all fields except the last are numeric
$[ = 1;			# set array base to 1
$, = ' ';		# set output field separator
$\ = "\n";		# set output record separator

$records = 0;
$bins = 10;

while >) {
      chomp;	# strip record separator
    @Fld = split(' ', $_, -1);

    $rds++;s++;

    $recordcords == 1) {
	$numfields = $#Fld;
    }

    for ($i = 1; $i <= $#Fld; $i++) {
	$data{$records, $i} = $Fld[$i];
    }
}

$train = int(0.8 * $records);
print $train;

##### initialise max and mins
for ($i = 1; $i < $numfields; $i++) {
    $max{$i} = $data{1, $i};
    $min{$i} = $data{1, $i};
}
##### calculate max and mins
for ($i = 1; $i < $numfields; $i++) {
    for ($r = 1; $r <= $train; $r++) {
	if ($ta{$r, , $i} lt $min{$i}) {	#???
	    $min{$i} = $data{$r, $i};
	}
	if ($data{$r, $i} gt $max{$i}) {	#???
	    $max{$i} = $data{$r, $i};
	}
    }
}
#### calculate ranges
for ($i = 1; $i < $numfields; $i++) {
    $range{$i} = $max{$i} - $min{$i};
}
#### calculate bin widths
for ($i = 1; $i < $numfields; $i++) {
    $binwidth{$i} = $range{$i} / $bins;
}

#### how many classes are there?
###### initialise as 1 class, which is class value of record 1
$nclasses = 1;

$classes{1} = $data{1, $numfields};

###### find out how many classes and what they are
for ($i = 2; $i <= $records; $i++) {
    $new = 1;

    foc = 1 = 1; $c <= $nclasses; $c++) {
	if ($data{$i, $numfields} eq $classes{$c}) {	#???
	    $new = 0;
	}
    }
    if ($new == 1) {
	$nclasses++;
	$classes{$nclasses} = $data{$i, $numfields};
    }
}

##### get the class counts -- how many in each class -- we will use this
##### later
for ($c = 1; $c <= $nclasses; $c++) {
    $classcounts{$c} = 0;
}
for ($r = 1; $r <= $train; $r++) {
    for ($c = 1; $c <= $nclasses; $c++) {
	if ($data{$r, $numfields} eq $classes{$c}) {	#???
	    $classcounts{$c}++;
	}
    }
}

for ($c = 1; $c <= $nclasses; $c++) {
    print 'class', $classes{$c}, $classcounts{$c}, $classcounts{$c} / $train;
}

###### for each class for each field, calculate the proportions of
###### values in each bin
########## initialise the counts
for ($c = 1; $c <= $nclasses; $c++) {
    for ($f = 1; $f < $numfields; $f++) {
	for ($b =  $ $b <= $bins; $b++) {
	    $count{$c, $f, $b} = 0;
	}
    }
}
###### now go through and populate the counts
for ($r = 1; $r <= $train; $r++) {
    ### get the class
    for ($c = 1; $c <= $nclasses; $c++) {
	if ($data{$r, $numfields} eq $classes{$c}) {	#???
	    $thisclass = $c;
	}
    }
    for ($f = 1; $f < $numfields; $f++) {
	for ( = 1; $ $b <= $bins; $b++) {
	    $X = $data{$r, $f};

	    {
		 mini = $min{$f} + ($b - 1) * $binwidth{$f};

		axi = $min{n{$f} + $b * $binwidth{$f};

		if ($b= $bibins) {
		    $maxi += 1.0;
		}

		if (($X  $m$mini) && ($X < $maxi)) {	#???	#???
		    $count{$thisclass, $f, $b}++;
		}
	    }
	}
    }
}

###### now work out the proportions
for ($c = 1; $c <= $nclasses; $c++) {
    if ($classcounts{$c} == 0) {
	$classcounts{$c} = 1;
    }
    for ($f = 1; $f < $numfields; $f++) {
	for b = 1; $ $b <= $bins; $b++) {
	    $count{$c, $f, $b} /= $classcounts{$c};
	    print $classes{$c}, $f, $b, $count{$c, $f, $b};
	}
    }
}

#### now what is the performance of Naive Bayes on these data?
$correct = 0;

for ($c = 1; $c <= $nclasses; $c++) {
    for ($d = 1; $d <= $nclasses; $d++) {
	$confused{$c, $d} = 0;
    }
}
for ($r = $train + 1; $r <= $records; $r++) {
    prialc'calculating performance on test data: record', $r;

    for = 1 = 1; $c <= $nclasses; $c++) {
	$prob{$c} = 0;
    }
    for ($c = 1; $c <= $nclasses; $c++) {
	#### prob of class c is initialised by prior prob of class
	$p = log($classcounts{$c} / $train);

	for ($f =1; $$f < $numfields; $f++) {
	    #### find the bin of this value 

	    for ($b = 1; $b <= $bins; $b++) {
		$X = $data{$r, $f};

		{
		    $mini = $min{$f} + ($b - 1) * $binwidth{$f};

		    = $xi = $min{$f} + $b * $binwidth{$f};

		    i==($b == $bins) {	#???
			$maxi += 1.0;
		    }

		  ($X >(($X >= $mini) && ($X < $maxi)) {	#???	#???	#???
			$thisbin = $b;
		    }
		}
	    }

	  += log( log($count{$c, $f, $thisbin});
	}

	$pro{$c} = $pp;
    }
    ##### which class is most likely ?
    $bestclass = 1;

 or ($c = 2 = 2; $c <= $nclasses; $c++) {
	if ($pb{$c} } gt $prob{$bestclass}) {	#???	#???
bestclasstclass = $c;
	}
    }
    if ($data{$r, $numfields} eq $classes{$bestclass}) {	#???
	$correc+;
      }
    ##### CHANGE
    #find the position of the guessed class in classes
    $position = 0;

    $ta = $ = $data{$r, $numfields};

 or ($i = 1 = 1; $i <= $nclasses; $i++) {
	if ($csses{${$i} eq $data{$r, $numfields}) {	#???
	    $position = $i;

	  t;
	}

	}
    }
   #  CHANGE 
    #use position along with the index of the best class to increment a portion of the confusion matrix.
    $confused{$position, $bestclass}++;
}
printf "overall accuracy on test set is %g per cent\n", 
100 * $correct / ($records - $train);

####### CHANG  -- Display the values of the class as well, makes the matrix harder to read
#######           but also makes it easier to understand the results in the long term. 
printf (("\nconfusion matrix\n               predicted class\n"));

for ($c = 1; $c <= $nclasses; $c++) {
    printf 'actual class %d (value=%s): ', $c, $classes{$c};

for ($d = 1 = 1; $d <= $nclasses; $d++) {
	printf ' %d ', $confused{$c, $d};
    }
    printf (("\n");;
}

