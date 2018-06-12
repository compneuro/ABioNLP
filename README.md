#README for ABioNLP tool - A Biological Natural Language Processing Tool for Systems Biology, Translational science and Medicine. 

This tool is part of the paper "Mining Scientific Articles for Inter-Relationships and Visualisation: Natural Language Processing for Systems Biology Modeling" by Nidheesh Melethadathil, Priya Chellaiah, Jaap Heringa, Bipin Nair and Shyam Diwakar. (manuscript submitted)

------
This software is covered by the Simplified BSD license. 
Copyright (C) 2018 Nidheesh Melethadathil, Bipin Nair, Shyam Diwakar
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
-------

Code contributors: Nidheesh Melethdathil and Shyam Diwakar
Computational Neuroscience Lab, Amrita School of Biotechnology
Amrita Vishwa Vidyapeetham, Kollam, Kerala, India. 
www.amrita.edu/compneuro 

last updated: 27 Feb 2018 
---------------

#Installation

This framework uses different open source application and interfaces for article retrieval, document clustering and visualization. This is web based or runs on Tomcat with two other third party APIs for cluster label validation and visualization. 
Installation requires following steps to be executed.

1.	Download and install Java (https://java.com/en/download/) in the server.

2.	Download and configure Tomcat (ideally through a IDE like NetBeans or Eclipse) webserver in the computer. Available at https://netbeans.org/downloads/.
	a.	In Netbeans 8 you may have to install the Tomcat plugin manually. After you download and extract Tomcat follow these steps:
	b.	Tools -> Plugins -> Available plugins, search for 'tomcat' and install the one named "Java EE Base plugin".
	c.	Restart Netbeans
	d.	On the project view (default left side of the screen), go to services, right click on Servers and then "Add Server"
	e.	Select Apache Tomcat, enter username and password and config the rest and finish.

3.	Download and configure MySQL database. Preferably install WAMP server from following link.   https://sourceforge.net/projects/wampserver/ in the server and import the tables from BioNLP.sql file available in the Db_Sql folder).

4.	Download and configure Neo4j(https://neo4j.com/download/), the Graph database in the root folder. 

5.	Download and copy MetaMap, the UMLS Metathesaurus files available from https://metamap.nlm.nih.gov/JavaApi.shtml to root folder. 

6.	Setup the BioNLP Web application by building the project in NetBeans.
--------------------------------------------------------------------------------------------------------------
#Operating Instructions

Server Side
1.	Start three services of MetaMap (by executing the corresponding .bat files available in \public_mm_win32_main_2012\public_mm\bin folder)
	a.	Skrmedpostctl
	b.	Wsdserverctl
	c.	mmserver12

2.	Start Neo4j database

3.	Run the build for this NLP tool in NetBeans which will start Tomcat server and will make the NLP tool available at http://localhost:8080/project_new/.

Browser Side
1.	Open the web application and provide authentication details like username and password.

2.	Provide the search term, select the algorithm and specify the number of documents to be retrieved to submit the query. 

3.	Depends on selection of algorithm, user may provide different values for the parameters.

4.	The results of retrieval after clustering is displayed as radial graphs like a star with nodes representing the cluster. 

5.	Click on the circle with cluster label to see the articles grouped in that cluster labels. These articles are listed in the right side section of the screen. Clicking on title of the articles will display the abstract for the paper.

6.	Right click on cluster label to perform re-clustering to group the articles which are note presently grouped in any group.
--------------------------------------------------------------------------------------------------------------
#Contact: nidheesh@am.amrita.edu, shyam@amrita.edu