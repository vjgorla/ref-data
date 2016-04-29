# ref-data
Framework to simplify reference data loading for java applications

## Basic usage

1 Create a concreate type
```java
import java.util.List;
import com.github.vjgorla.refdata.AbstractRefDataValue;
import com.github.vjgorla.refdata.RefDataType;

public class Country extends AbstractRefDataValue {
    
    private static final RefDataType<Country> TYPE = new RefDataType<Country>(Country.class);
    
    public static final Country AUS = decode("AUS");

    private Country(String code) {
        super(TYPE, code);
    }
    public static Country decode(String code) {
        return new Country(code);
    }
    public static List<Country> values() {
        return TYPE.values(false);
    }
}
```
2 Specify where data is loaded from in ref-data-config.properties file, and place it on classpath.
```properties
###### JPA ######
ref-data.loader.implementation.class                  = com.github.vjgorla.refdata.loader.impl.JpaRefDataLoader
ref-data.loader.jpa.javax.persistence.provider        = org.hibernate.ejb.HibernatePersistence
ref-data.loader.jpa.javax.persistence.transactionType = RESOURCE_LOCAL
ref-data.loader.jpa.hibernate.connection.username     = sa
ref-data.loader.jpa.hibernate.connection.password     =
ref-data.loader.jpa.hibernate.connection.driver_class = org.hsqldb.jdbcDriver
ref-data.loader.jpa.hibernate.connection.url          = jdbc:hsqldb:file:ref-data-examples.db
ref-data.loader.jpa.hibernate.default_schema          = PUBLIC
ref-data.loader.jpa.hibernate.dialect                 = org.hibernate.dialect.HSQLDialect
ref-data.loader.jpa.hibernate.hbm2ddl.auto            = none
###### or JSON ######
#ref-data.loader.implementation.class=com.github.vjgorla.refdata.loader.impl.JsonRefDataLoader
#ref-data.loader.json.data.file=/ref-data.json
```
3 If using JPA persistence, create database schema (This only needs to be done once regardless of the number of types)
```sql
CREATE TABLE CODE_TYPES(ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 0) NOT NULL PRIMARY KEY,TYPE_CODE VARCHAR(50) NOT NULL,UNIQUE(TYPE_CODE))
CREATE TABLE CODE_VALUES(ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 0) NOT NULL PRIMARY KEY,CODE VARCHAR(50) NOT NULL,DESCRIPTION VARCHAR(4000) NOT NULL,ATTRIBUTES VARCHAR(4000),EFFECTIVE_FROM TIMESTAMP,EFFECTIVE_TO TIMESTAMP,TYPE_ID INTEGER NOT NULL,CONSTRAINT FKF44BD3B42F21B6E1 FOREIGN KEY(TYPE_ID) REFERENCES PUBLIC.CODE_TYPES(ID),UNIQUE(TYPE_ID,CODE))
```
4 Use the type in your code
```java
// Decode from strings
Country nz = Country.decode('NZ');
// Enumerate
List<Country> countries = Country.values();
// Write branching logic
if (customer.getCountry().equals(Country.AUS)) {
```

## Type attributes
Defining type attributes
```java
public class Country extends AbstractRefDataValue {
...
    public String getDialingCode() {
        return getAttribute("dialingCode");
    }
...    
```
Using type attributes
```java
Country.AUS.getDialingCode();
Country.decode('NZ').getDialingCode();
customer.getCountry().getDialingCode();
```

## Relationships between types
Defining relationship
```java
public class Country extends AbstractRefDataValue {
...
    public List<State> getStates() {
        List<State> states = new ArrayList<>();
        for (String stateCode : getAttributeAsList("states")) {
            states.add(State.decode(stateCode.trim()));
        }
        return states;
    }
...    
```
Using relationships
```java
Country.AUS.getStates();
Country.decode('NZ').getStates();
customer.getCountry().getStates();
```