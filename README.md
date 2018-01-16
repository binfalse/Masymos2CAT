# Masymos2CAT
[M2CAT](http://m2cat.sems.uni-rostock.de/) is a web based tool to export reproducible research results.
It links the graph based database [MASYMOS](https://sems.uni-rostock.de/projects/masymos/)
to the [CombineArchiveToolkit](https://sems.uni-rostock.de/projects/combinearchive/) (CAT). 

**This used to be a prototype! For the reimplementation see [semsproject.github.io/M2CAT/](https://semsproject.github.io/M2CAT/)**

## Build and Install

    mvn clean package

will create a `war` file in the `target` directory. Just deploy that archive to Tomcat.

## LICENSE

The code is licensed under an [Apache 2.0 license](LICENSE):

	Copyright martin scharm <https://binfalse.de/contact/>

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
