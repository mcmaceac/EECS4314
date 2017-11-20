#!/bin/bash

# Give the .xml file as the argument
file=$1
# We need 2 different cases:
#
#	- C/C++ files:	.c   .cc   .cpp   .h   .hpp
#		- These files all have the same format for <unit>, and the same for <cpp:include>
#
#	- Java files:	.java
#		- These are similar in <unit>, but use <import>

# We are now working with "copy.xml"

# In/out var for <unit></unit> blocks
# 0 - Outside
# 1 - Inside C/C++
# 2 - Inside Java
inside=0

while read -r line; do
	if [[ $line =~ ^\<unit.*language=\"C\+?\+?\".* ]]; then
		# It's a C/C++ file
		# Set status to inside <unit> block
		inside=1
		# Get file name
		base=$(echo "$line" | cut -d ' ' -f 5 | cut -d '"' -f 2)
	elif [[ $line == "</unit>" ]]; then
		# We left the block, now outside <unit> block
		inside=0
	elif [[ $line =~ ^\<unit.*language=\"Java\".* ]]; then
		# It's a Java file
		inside=2
		# Get file name
		base=$(echo "$line" | cut -d ' ' -f 4 | cut -d '"' -f 2)
	elif [[ $inside == 1 ]] && [[ $line =~ ^\<cpp:include.* ]]; then
		# Inside C/C++ <unit> block
		# Get dependancy file
		dep=$(echo $line | cut -d ';' -f 2 | cut -d '&' -f 1)
		if [[ $dep =~ ^\<cpp.* ]]; then
			# Other format, use quotations
			dep=$(echo $line | cut -d '"' -f 2)
		fi
		# Format & write to file
		echo "$base -> $dep" >> srcml.txt
	elif [[ $inside == 2 ]] && [[ $line =~ ^\<import.* ]]; then
		# Get any Java imports
		# Make a copy of the line and work on that
		dep=$(echo $line | cut -d ' ' -f 2 | cut -d ';' -f 1)
		# This is really bad
		dep=${dep//\<name\>}
		dep=${dep//\<\/name\>}
		dep=${dep//\<operator\>}
		dep=${dep//\<\/operator\>}
		# Format & write to file
		echo "$base -> $dep" >> srcml.txt
	fi
done < $file
