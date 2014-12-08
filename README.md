spring-data-orient
==================
[![Build Status](https://travis-ci.org/sleroy/spring-data-orient.svg?branch=master)](https://travis-ci.org/sleroy/spring-data-orient)

[![Coverage Status](https://img.shields.io/coveralls/sleroy/spring-data-orient.svg)](https://coveralls.io/r/sleroy/spring-data-orient)



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
