# provider
Provider Java web application

_by Ruslan Kostiuk_

_**(Internet) Provider** is an organization that provides services for accessing, using, or participating in the Internet; 
this specific provider may also provide **any other kind of services imaginable, such as TV, mobile network and so on**._

## Technologies used
* Java 17
* Maven
* Jakarta Servlet API 5.0; Tomcat 10
* PostgreSQL
* Junit 5
* Mockito
* Log4j2 + slf4j
* Bootstrap 5
* MVC design

**OOP principles and patterns** are widely used in this project.

Design patterns used:
* Front Controller
* Command
* Template Method
* Abstract Factory
* Factory Method
* DAO
* Service Layer
* PRG
* and so on

## Domain specifics

1. The User is provided with a list of Services (Telephone, Internet, Cable TV, IP-TV,etc.) and a list of Tariffs. 
2. Tariff includes at least one Service, has certain rate and duration. **Once created, it can never be deleted, 
may be hidden instead**
3. Tariff list supports:
    * sorting
        * alphabetically(a-z, z-a)
        * by price
    * filtering by Services included
    * pagination
4. Tariff data may be downloaded as PDF
5. The User can choose and Subscribe to one or more services at a certain rate
6. Subscription includes Tariff and payment time data
7. The User has an Account they can replenish. 
8. Funds are withdrawn from the account by the system depending on the User Subscriptions
    * If the amount in the Account is not enough, the system shows the User the message about it
    (and imaginary stops supplying unpaid Subscriptions Service(s)).
    * The required amount is withdrawn automatically after the user replenishes their Account
    (this means Subscription Services are now supplied again)
9. Supported User roles:
    1) GUEST - unauthorized user; no permissions
    2) MEMBER - authorized user; has an Account, may authorize and buy subscriptions
    3) ADMIN - manages users, services and tariffs:
        * may register Users of _MEMBER_ role
        * may ban and unban Users of _MEMBER_ role
        * may create Services
        * may create Tariffs
        * may update Tariffs
        * may hide Tariffs
    4) ROOT - has _ADMIN_ rights, + manages Users of _ADMIN_ role:
        * may create Users of _ADMIN_ role
        * may block and unblock Users of _ADMIN_ role

## Architecture and design specifics
### General:
* MVC architecture is applied.
* Front Controller pattern is applied along with Command pattern, so there is only one Servlet per app
* Commands use Services. Services use DAOs and other Services

### Command classes
* Command instances are obtained via factory instance
* FrontCommand abstract class is a base class for all command classes
* Command hierarchy controlls user authorization
* MemberCommand abstract class is a base class for commands that may be executed by any authorized User
* AdminCommand abstract class is a base class for commands that may be executed by Users of _ADMIN_ role permissions 
or higher

### DAO
* All DAO classes extend EntityDao
* DAO instances **obtain Connaction from the calling code**, so setConnection() method must always be called before 
calling DB methods
* DAO instances **NEVER close Connection**, it must be closed(returned to pool) by the calling code





