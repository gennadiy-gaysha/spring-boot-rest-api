## Hiding sensitive data in .env file
1. Create a .env file in your project root:
```
DB_USERNAME=username
DB_PASSWORD=password
```
2. Add .env to .gitignore
3. Install dotenv support in Spring Boot by adding the dependency:
```
<dependency>
    <groupId>io.github.cdimascio</groupId>
    <artifactId>dotenv-java</artifactId>
    <version>2.2.0</version>
</dependency>
```
and reload Maven in IntelliJ
3. In the app's main method set environment variables manually:
```
public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

		SpringApplication.run(CrudApplication.class, args);
	}
```

## Sequence of Events in the DAO and Controller Layers

#### **1. `@Repository` annotation means that `EmployeeDAOImpl` bean is created**
- **`@Repository` annotation** on the `EmployeeDAOImpl` class tells Spring that this class is a **Data Access Object (DAO)** and should be treated as a **Spring Bean**.
- Spring scans the project for classes with `@Component`-derived annotations (`@Repository`, `@Service`, `@Controller`, etc.) during application startup. As a result, **Spring creates an instance of `EmployeeDAOImpl` as a managed bean** in the **Spring ApplicationContext**.
- **This bean is now ready for dependency injection** into other components, like the controller in this case.

---

#### **2. The `EmployeeDAOImpl` bean injects the `EntityManager` bean that was implicitly created by the Spring container**

- **`EmployeeDAOImpl`** requires an instance of `EntityManager` to perform database operations. `EntityManager` is provided by **Spring Data JPA**.
- Spring automatically **injects the `EntityManager`** into the `EmployeeDAOImpl` bean through **constructor-based dependency injection** (because of the `@Autowired` annotation on the constructor).
- **`EntityManager`** itself is also created automatically by Spring when the JPA configuration is set up in the application (this typically happens via the configuration in `application.properties` or `application.yml` files for Spring Data JPA).
- The **`EntityManager` bean** is injected into `EmployeeDAOImpl` by Spring, enabling it to perform operations like querying the database.

---

#### **3. `EmployeeRESTController` is created as a Spring Bean**

- Similar to `EmployeeDAOImpl`, the **`EmployeeRESTController`** class is annotated with `@RestController` and is automatically detected by Spring as a **Spring Bean** during the application startup.
- **Spring creates an instance of `EmployeeRESTController`** and registers it in the Spring ApplicationContext.

---

#### **4. Spring injects the `EmployeeDAOImpl` bean into `EmployeeRESTController`**

- Since the `EmployeeRESTController` class requires an instance of `EmployeeDAO` (which is implemented by `EmployeeDAOImpl`), Spring will **inject the `EmployeeDAOImpl` bean** into the `EmployeeRESTController` during the controller's creation.
- **Constructor-based dependency injection** is used here as well (Spring automatically handles this, as it only has one constructor).
- **`EmployeeDAO employeeDAO`** is passed into the constructor, and Spring provides the existing `EmployeeDAOImpl` instance that was created earlier.

---

#### **5. An HTTP request triggers the `EmployeeRESTController`'s method**

- When an HTTP request is made to the `/api/employees` endpoint, Spring will route that request to the `findAll()` method of `EmployeeRESTController` (because it's mapped to `@GetMapping("/employees")`).

---

#### **6. `findAll()` in `EmployeeRESTController` calls `employeeDAO.findAll()`**

- Inside the `findAll()` method of the controller, it calls the **`findAll()` method** of the injected `EmployeeDAO` bean (which is actually `EmployeeDAOImpl`).

---

#### **7. `EmployeeDAOImpl`'s `findAll()` method executes the JPA query**

- The `findAll()` method in `EmployeeDAOImpl` uses the **`EntityManager`** (injected in step 2) to create and execute a **JPQL query** (Java Persistence Query Language) to fetch all employee records from the database.

  ```java
  TypedQuery<Employee> theQuery = entityManager.createQuery("from Employee", Employee.class);
  List<Employee> employees = theQuery.getResultList();
  ```

- The query is executed, and the result is returned as a list of `Employee` entities.

---

#### **8. The list of employees is returned to the controller**

- The `findAll()` method in `EmployeeDAOImpl` returns the list of `Employee` entities to the `EmployeeRESTController`.

---

#### **9. Spring automatically converts the list into a JSON response**

- Since the `EmployeeRESTController` is annotated with `@RestController`, Spring will **automatically convert** the list of `Employee` entities into a **JSON** (or possibly XML) response, using Jackson (the default JSON converter in Spring).

---

#### **10. The response is sent back to the client**

- The converted JSON data (the list of employees) is then sent back as the HTTP response to the client (browser, Postman, etc.), completing the request-response cycle.

---

### Summary of the Sequence:

1. **`@Repository` annotation** marks `EmployeeDAOImpl` as a Spring Bean, which is then created and managed by the Spring container.
2. Spring **injects the `EntityManager`** (provided by Spring Data JPA) into `EmployeeDAOImpl` to handle database operations.
3. **`EmployeeRESTController`** is created as a Spring Bean and **injects `EmployeeDAOImpl`** into it via constructor injection.
4. When a request comes to `/api/employees`, **`EmployeeRESTController`** calls the `findAll()` method of `EmployeeDAOImpl`.
5. **`EmployeeDAOImpl`** executes a query to retrieve employee data using the `EntityManager`.
6. The **list of employees** is returned to the controller, converted into a JSON response, and sent back to the client.

By following this sequence, Spring manages the creation, dependency injection, and lifecycle of the beans (`EmployeeDAOImpl` and `EmployeeRESTController`) to ensure the system works smoothly and the necessary data is fetched from the database and returned as a response to the client.
