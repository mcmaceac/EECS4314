#!/bin/bash
java -Xms256M -Xmx1024M -classpath ql.jar ca.uwaterloo.cs.ql.Main addcontain.ql MySQL_UnderstandFileDependency.contain MySQL_UnderstandFileDependency.raw.ta MySQL_UnderstandFileDependency.con.ta
cat schema.asv.ta MySQL_UnderstandFileDependency.con.ta > MySQL_UnderstandFileDependency.ls.ta
