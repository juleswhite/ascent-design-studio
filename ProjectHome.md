## Overview ##
The Ascent Design Studio is a collection of algorithms/frameworks for the deployment and configuration of distributed, real-time, and embedded systems. ASCENT contains frameworks for:

  * Automated deployment planning with real-time scheduling, co-location constraints,   resource constraints, and network topology/resource constraints
  * Deployment optimization algorithms
  * An implementation of a self-healing/self-configuration framework for applications   built with the Java Spring framework
  * Constraint Satisfaction Problem (CSP) based automated feature model configurators
  * An implementation of the Filtered Cartesian Flattening (FCF) algorithm for algorithmically selecting valid feature sets from feature models that adhere to resource constraints
  * ASCENT includes an implementation of the Configuration ExploratioN Technique (ASCENT). The ASCENT algorithm takes one or more models of the hardware and software variability in a system and can solve for an optimal design configuration. The optimization goal is configurable. Ascent also produces a graph of the key trends in the solution space that can be used to perform a number of resource-based design analyses, such as finding the minimum cost to produce a solution with a given value.

## Multi-step SPL Configuration ##

Ascent includes an implementation of the temporal SPL configurator described in: Jules White, David Benavides, Brian Dougherty, Douglas C. Schmidt, Automated Reasoning for Multi-step Software Product-line Configuration Problems, Software Product-lines Conference (SPLC), 10pgs. August 24-28, 2009, San Francisco, CA

For more information, please see:

[TemporalConfigurator](http://code.google.com/p/ascent-design-studio/source/browse/trunk/ascent/code/core/src/org/ascent/configurator/featuremodeling/TemporalConfigurator.java)

## Getting Started ##

New users should start by looking at the wiki pages for the features they are interested in: [Go to the wiki](http://code.google.com/p/ascent-design-studio/w/list).