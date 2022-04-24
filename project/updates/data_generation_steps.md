## Steps ro Run Data Generator

1. Clone [NoSQL Data Engineering repo](https://github.com/catedrasaes-umu/NoSQLDataEngineering)
2. Download [EMF core](https://download.eclipse.org/modeling/emf/emf/builds/release/index.html) (release 2.25)
3. Everyone's favorite part - resolving dependencies!!
    * The data generation project is es.um.nosql.s13e.db.gen (nosql.s13e.db.gen), and depends on the es.um.nosql.s13e (nosql.s13e) and es.um.nosql.s13e.db (nosql.s13e.db) projects
    * Open all three projects in Eclipse and ensure that they have the following on their build paths:
        * nosql.s13e:
            * org.eclipse.emf.common (from EMF core downloaded in step 2)
            * org.eclipse.emf.ecore (from EMF core downloaded in step 2)
            * org.eclipse.emf.ecore.xmi (from EMF core downloaded in step 2)
            * Junit 5
            * JRE System Library (JavaSE-1.8)
        * nosql.s13e.db:
            * project es.um.nosql.s13e
            * org.eclipse.emf.common
            * org.eclipse.emf.ecore
            * JRE System Library (JavaSE-1.8) 
        * nosql.s13e.db.gen:
            * projects es.um.nosql.s13e and es.um.nosql.s13e.db
            * org.eclipse.emf.common
            * org.eclipse.emf.ecore
            * JRE System Library (JavaSE-1.8)
            * JUnit 5
    * Make sure to add other projects (es.um.nosql.s13e, es.um.nosql.s13e.db) as dependencies for run configuration in es.um.nosql.s13e.db.gen
4. Update version of Jackson in nosql.s13e.db.gen Maven file to 2.9.8
5. Modify StringGen() in nosql.s13e.db.gen.generator.primitivetypes to remove illegal character constants
6. Fix bug that always used max value when generating elements of references/aggregates (in ReferenceGen.java and AggregateGen.java)
7. Build (export) as runnable JAR with all dependencies
8. Run the Main class in nosql.s13e.db.gen
    * Example command: ``` java -cp nosql.s13e.db.gen.jar es.um.nosql.s13e.db.gen.Main ```
9. Modify the config.yaml file as appropriate for generation needs