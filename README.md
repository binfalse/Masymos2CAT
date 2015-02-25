# M2CAT
[M2CAT](http://m2cat.sems.uni-rostock.de/) is a web based tool to export reproducible research results.
It links the graph based database [MASYMOS](https://sems.uni-rostock.de/projects/masymos/)
to the [CombineArchiveToolkit](https://sems.uni-rostock.de/projects/combinearchive/) (CAT). 


## Build and Install

    mvn clean package

will create a `war` file in the `target` directory. Just deploy that archive to Tomcat.
