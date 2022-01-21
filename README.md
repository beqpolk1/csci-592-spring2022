# CSCI 592 Independent Study - Advanced Database Special Topics

### Course Website: https://beqpolk1.github.io/csci-592-spring2022/

## Course Description

The endless abundance of data in the modern world has uncovered the shortcomings of traditional relational databases. Sometimes the volume of data is too large for processing via a relational system to be workable, sometimes relational schemas are a poor fit for the structure of novel data sets, and sometimes both. As a result, database systems that depart from the relational paradigm have profilerated, and a single modern system can take advantage of multiple data storage paradigms to better fit the needs for particular parts of the application domain. However, coming up with an overall strategy to manage the data in these "polyglot persistence" systems can be daunting given the heterogeneous nature of the data and constituent systems involved. This independent study will spend time researching the problems that arise in a polyglot environment and tools and techniques that have been developed to fight these challenges. It will focus on "universal" models that have been or are being developed that allow multiple database paradigms, including relational, to work together in a more cohesive manner, and apply these ideas to solve problems in a real-world polyglot persistence system.

## Learning Outcomes

As a result of participating in this independent study, the student will be able to:
* Analyze a polyglot persistence system and identify the strengths of the polyglot approach, as well as data management problems arising from the use of heterogeneous data stores.
* Evaluate several techniques for alleviating data management problems in polyglot persistence systems and determine techniques that are suitable for a given application.
* Implement a new solution in a real-world polyglot system that paplies some of the studied techniques to mitigate difficulties posed by the use of heterogeneous data.

## Readings and Course Materials

#### First Week Warm-Up:

**0.1:** Unified Data Modeling for Relational and NoSQL Databases - Allen Wang (https://www.infoq.com/articles/unified-data-modeling-for-relational-and-nosql-databases/)

#### Part 1 - Background (data modeling and approaches for the relational paradigm)

**1.1:** DB-MAIN: A next generation meta-CASE - Vincent Englebert, Jean-Luc Hainaut (https://www-sciencedirect-com.proxybz.lib.montana.edu/science/article/pii/S0306437999000071)

**1.2:** A vision for management of complex models - Phillip A. Bernstein, Alon Y. Halevy, Rachel A. Pottinger (https://dl-acm-org.proxybz.lib.montana.edu/doi/abs/10.1145/369275.369289)

#### Part 2 - Background (more data modeling with emphasis on NoSQL systems)

**2.1:** Data modeling in the NoSQL world - Paolo Atzeni, Francesca Bugiotti, Luca Cabibbo, Riccardo Torlone(https://www-sciencedirect-com.proxybz.lib.montana.edu/science/article/pii/S0920548916301180)

**2.2:** Inferring Versioned Schemas from NoSQL Databases and Its Applications (book section) - Diego Sevilla Ruiz, Severino Feliciano Morales, Jesús García Molina (https://link-springer-com.proxybz.lib.montana.edu/chapter/10.1007%2F978-3-319-25264-3_35)

**2.3:** A Universal Metamodel and Its Dictionary - Paolo Atzeni, Giorgio Gianforme, Paolo Cappellari (https://link-springer-com.proxybz.lib.montana.edu/chapter/10.1007%2F978-3-642-03722-1_2)

#### Part 3 - Solutions (unified models + examples)

**3.1:** Uniform Access to Non-relational Database Systems: The SOS Platform - Paolo Atzeni, Francesca Bugiotti, Luca Rossi (https://link-springer-com.proxybz.lib.montana.edu/chapter/10.1007%2F978-3-642-31095-9_11)

**3.2:** Multi-model Database Management Systems - A Look Forwrad - Zhen Hua Liu, Jiaheng Lu, Dieter Gawlick, Heli Helskyaho, Gregory Pogossiants, Zhe Wu (https://link.springer.com/chapter/10.1007%2F978-3-030-14177-6_2)

**3.3:** A unified metamodel for NoSQL and relational databases - Carlos J. Fernández Candel, Diego Sevilla Ruiz, Jesús J. García-Molina (https://www-sciencedirect-com.proxybz.lib.montana.edu/science/article/pii/S0306437921001149)

**3.4:** Uniform data access platform for SQL and NoSQL database systems - Ágnes Vathy-Fogarassy, Tamás Hugyák (https://www-sciencedirect-com.proxybz.lib.montana.edu/science/article/pii/S0306437916303398)

#### Potential Other Resources

* PartiQL Specification - The PartiQL Specification Committee (https://partiql.org/assets/PartiQL-Specification.pdf)
* Ontology Matching (book, 2007) - Jérôme Euzenat, Pavel Shvaiko
* Database Systems: The Complete Book (book, 2009) - Hector Garcia-Molina, Jeffrey D. Ullman, Jennifer Widom

## Deliverables

* The student will maintain a blog-style website (GitHub page) containing the coursework and chronicling progress through the course.
* Readings:
  * For each reading, the student will make an approximately 500 - 1000 word post to the website centered on the material.
  * The post should contain a brief summary of the reading, how the reading relates to other studied concepts/ideas, any critiques of the authors' approach, further questions raised by the piece, an overall reflection/opinion on the material, etc.
* Project:
  * The student will propose and complete a large project applying the concepts covered in the course readings. This project will be maintained in the same repository as the course website.
  * Initially, the student will submit a written project proposal outlining the goals (what problems will be addressed and what features will be implemented to address them). The proposal should be part of the website.
  * While working on the project, the student will make weekly posts to the website briefly covering the work that's been done, difficulties encountered, approaches taken, etc.
  * At the end of the semester, the student will submit a final written summary and description of the project (approx. 5-8 pages) that demonstrates how the covered concepts were applied.
  * At the end of the semester, the student will provide a short presentation (5-10 minutes) focusing on the results achieve with the project (with less emphasis on background or supporting material).


#### Grades/Evaluation

* Readings and weekly summaries: 50%
  * Equal weight per each
* Project: 50%
  * Written proposal
  * Weekly posts
  * Delivered solution
  * Final writeup
  * Final presentation

## Tentative Course Schedule

| Week | Ending In | Notes | Assignment Due/Reading to Cover |
|------|-----------|-------|---------------------------------|
| 1 | 1/21 | Semester starts 1/19 | 0.1 |
| 2 | 1/28 | | 1.1 |
| 3 | 2/4 | | 1.2 |
| 4 | 2/11 | | 2.1 |
| 5 | 2/18 | | 2.2 |
| 6 | 2/25 | No class 2/21 | 2.3 |
| 7 | 3/4 | | 3.1 |
| 8 | 3/11 | | 3.2 |
| 9 | 3/18 | Week of spring break - no class | |
| 10 | 3/25 | | 3.3; proj. proposal |
| 11 | 4/1 | | proj. check-in |
| 12 | 4/8 | | 3.4; proj. check-in |
| 13 | 4/15 | No class 4/15 | proj. check-in |
| 14 | 4/22 | | proj. check-in |
| 15 | 4/29 | | proj. check-in |
| 16 | 5/6 | Dead week | proj. check-in |
| 17 | 5/12 | Semester ends Thurs. 5/12 | Final proj. writeup & presentation |

## Weekly Meetings

* The student will agree on a weekly meeting time with the professor, to be conducted either in-person or virtually.
* These meetings should last roughly 30 minutes.
* During the first part of the course (readings), these meetings will serve as an opportunity to review the student's summary of that week's reading. The student and professor will discuss the reading and focus on takeaways and implications of the material as it relates to the independent study.
* During the second part of the course (project), these meetings will serve as an opportunity for the student to report on the progress being made, discuss any difficulties encountered, and solicit advice for solving problems or other resources that might be useful for tackling certain topics.
* Additional ad-hoc meetings may be scheduled as needed.
