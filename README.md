spring-data-orient
==================
[![Build Status](https://travis-ci.org/sleroy/spring-data-orient.svg?branch=master)](https://travis-ci.org/sleroy/spring-data-orient)

[![Coverage Status](https://img.shields.io/coveralls/sleroy/spring-data-orient.svg)](https://coveralls.io/r/sleroy/spring-data-orient)

<a href='https://bintray.com/sleroy/maven/spring-data-orient/view?source=watch' alt='Get automatic notifications about new "spring-data-orient" versions'><img src='https://www.bintray.com/docs/images/bintray_badge_color.png'></a>


Spring Data implementation for OrientDB

```java
@Configuration
@EnableTransactionManagement
@EnableOrientRepositories(basePackages = "org.springframework.data.orient.object.person")
public class ApplicationConfiguration {

    @Bean
    public OrientObjectDatabaseFactory factory() {
        OrientObjectDatabaseFactory factory =  new OrientObjectDatabaseFactory();
        
        factory.setUrl("plocal:test/spring-data-test");
        factory.setUsername("admin");
        factory.setPassword("admin");
        
        return factory;
    }
    
    @Bean
    public OrientTransactionManager transactionManager() {
        return new OrientTransactionManager(factory());
    }
    
    @Bean
    public OrientObjectTemplate objectTemplate() {
        return new OrientObjectTemplate(factory());
    }
    
        
    @PostConstruct
    public void registerEntities() {
        factory().db().getEntityManager().registerEntityClass(Person.class);
    }
}
```

Generating a release <http://veithen.github.io/2013/05/26/github-bintray-maven-release-plugin.html> : 

```java
    mvn -Prelease clean install

Performing a release

    Go the the Bintray Web site and add a new version for the package.

    Execute the release process as follows:


    mvn release:prepare
    mvn release:perform



```
