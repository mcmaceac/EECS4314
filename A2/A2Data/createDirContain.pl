# EECS 4314 
#     Creating directory level containment

use strict;
use warnings;

my ($name, $hash, $value, $count, $file, $kind, $type);
my ($output_file, %DIR_CONTAIN_HASH, $line);

open INPUT, "$ARGV[0]";

# read the parse the files

while (<INPUT>) {
	$line = $_;
	chomp $line;

	($kind, $file, $type) = split(/\s+/, $line);

	if ($kind eq "\$INSTANCE" and $type eq "cFile") {
		&createDirContain($file);
	}
}

close INPUT;

# output containment
if ($ARGV[0] =~ /(.*)\.raw\.ta/) {
	$output_file = $1 . ".contain"; 
}

open OUTPUT, ">$output_file";

while (($name, $hash) = (each %DIR_CONTAIN_HASH)) {
	while (($value, $count) = (each %$hash)) {
		print OUTPUT "contain $name $value\n";
	}
}

close OUTPUT;

# create directory level containment for each file
sub createDirContain {
	my ($local_file) = @_;

	my (@local_array, $local_index, $local_from, $local_to);

	# split using "\" for Windows
	#@local_array = split(/\\/, $local_file);
	
	# split using "/" for Mac/Linux
	@local_array = split(/\//, $local_file);

	$local_from = $local_array[0];
	for ($local_index=1; $local_index<=$#local_array; $local_index++) {
		$local_to = $local_from . "/" . $local_array[$local_index];
		$DIR_CONTAIN_HASH{$local_from}{$local_to}++;
		# print STDERR "$local_to $local_from\n";
		
		$local_from = $local_to;
	}
}
