This page describes how to build your own problems that can use the Ascent infrastructure including PSO solver.

# Introduction #

The ascent-design-studio contains a myriad of java packages and a huge number of java classes.  The code is designed to take large advantage of OO and Java features. As a result the code is filled with extended classes and implemented interfaces with an absence of expressly implemented default constructors ( as they do not need to be expressly coded in java).

As a result, the code can be extremely difficult to pickup and use. The purpose of this guide is to provide a brief description of the code hierarchy and to point out the files that should be altered ( or replicated and altered) to allow you to use PSO and the other algorithms to solve your custom problems out of the box.

For this introduction, we will focus on PSO.




# Details #
The base package, org.ascent, includes the building blocks for setting up problems to use the algorithms in the ascent-design studio, most notably ProblemConfig.java, ProblemConfigImpl.java, and VectorSolution.java. Since it is critical to understand how these three classes fit in, we'll attack each one of them on an individual basis.

1.) ProblemConfig.java- Problem config defines the interface that must be implemented for a problem to be compatible with any of the solvers. The advantage of this design is that the solvers and associated algorithms can be completely decoupled from the definition of the problem.

2.) ProblemConfigImpl.java- If the name didn't give it away, the ProblemConfigImpl is the superclass of all problem types. It contains a VectorSolution which will be discussed shortly, boundaries for the solution space, a definition of whether or not a solution is feasible (valid), a method to create initial solutions, and a place holder to seed solutions (the seeds determine what initial systems are created ).

**ProblemConfigImpl.java SHOULD NOT BE ALTERED WHEN CREATING YOUR PROBLEMS. Your problems should instead extend ProblemConfigImpl.java.  You can then add any problem specific methods/material in your class that does the extending.**

3.)VectorSolution.java. All solutions using the algorithms in the ADS are represented as VectorSolutions.  They contain an Object Artifact ( all Java Object inherit from Object so it can be any object you like), as well as one dimensional array that defines the current position of the solution in the solution space.


To make your own problem instance compatible with PSO, you need the following:

1.) A base ProblemConfig that extends ProblemConfigImpl. See DeploymentConfig.java in org.ascent.deployment for an example.

2.)  An instance of your problem. In this case, look at DeploymentPlan for an example. It should include the problemTypeConfig and vectorsolution in the constructor.

2b.)You're probably going to want to apply multiple solving strategies to the same problem type or make mild alterations to make different problem versions. In this case, take a look at Deployment MinmizationConfig. It extends DeploymentConfig, to make a new problem type that focuses on minimization. This is optional, but will make extending

4.) Once you've made the config for your specific problem version, you're then going ot want to make a planner object. The planner object is where you define how your specific problem version is scored, as well as what makes it a feasible solution. Remember, if you don't overload the methods already included in the problemTypeConfig(or your more specific problem flavor in 2b.) then the methods defined in the superclasses will be used. Don't forget to define the scoring, soution feasibility etc.


Using a Solver:

Great, you've got a problem instance made. Now you need to get it solved. To do that, you need to make your own PSOSolver for your specific problem type. In this case, let's say you have FooConfig as your problemTypeConfig.

Make a new class called PSOFooSolver. This should look very similar to PSODeployer, only referring to your high level FooConfig type instead of DeploymentConfig and your instance object instead of Deployment plan.

To do that with PSO, make a new class in which you build your ProblemTypeConfig. Then pass that as a parameter to the constructor to instantiate your ProblemTypePlanner. Then make a new PSOFooSolver object.

Finally, your want to call PSOFooSolver.solver (or schedule or deploy or whatever verb you used for what foo does) passing in the ProblemTypePlanner as a parameter. The return type of this will be your foo instance object.

Check out the Tester class in org.ascent.schedule.optimizer for an example of executing the solver.