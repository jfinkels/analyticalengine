Simulator of Charles Babbage's Analytical Engine
================================================

This package contains a Java simulator for Charles Babbage's Analytical Engine,
a mechanical computer first described in the 1830s.

The original source code was written by [John Walker][1], and is available from
[this website][2]. That software is in the public domain, according to [this
website][3].

This file was last updated on January 31, 2014.

[1]: http://www.fourmilab.ch
[2]: http://www.fourmilab.ch/babbage/contents.html
[3]: http://www.fourmilab.ch/babbage/cmdline.html

Copyright license
-----------------

This package is distributed under the terms of the GNU General Public License
version 3. For more information, see the file `LICENSE` in this directory.

Getting started
---------------

This project uses [Maven](http://maven.apache.org/) for build management and
requires Java 1.8 to compile and run.

To package this project into a JAR file, run

    mvn package

This produces an executable JAR file in the `target/` directory.

To compile this project, run

    mvn compile

To test this project, run

    mvn test

To get project reports and development information, run

    mvn site
  
You can then view the site by opening `target/site/index.html` in a web
browser.

Running the Analytical Engine
-----------------------------

To create a shell script that runs the simulator, run

    mvn clean package jar:jar appassembler:assemble

Then, to run the simulator on a file named `myprogram.ae`, run

    sh target/appassembler/bin/analyticalengine myprogram.ae

Writing programs for the Analytical Engine
------------------------------------------

The programming language for this machine is arcane, but relatively similar to
modern machine languages. For more information on the instruction set and how
to write programs for this machine, see [this website][4].

[4]: http://www.fourmilab.ch/babbage/cards.html

Development
-----------

To prepare this package for development in Eclipse, run

    mvn eclipse:eclipse -DdownloadSources=true -DdownloadJavadocs=true

and

    mvn eclipse:configure-workspace -Declipse.workspace=/path/to/workspace

where `/path/to/workspace` is the path to your Eclipse workspace.

Eclipse automatic source formatting rules can be found in `formatter.xml` in
the top-level directory of this project.

To use Checkstyle, the Maven Checkstyle plugin, or the Eclipse Checkstyle
plugin, use the `checkstyle.xml` file in the top-level directory of this
project.

Contact
-------

Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
