> :warning: This repository was archived automatically since no ownership was defined :warning:
>
> For details on how to claim stewardship of this repository see:
>
> [How to configure a service in OpsLevel](https://www.notion.so/pleo/How-to-configure-a-service-in-OpsLevel-f6483fcb4fdd4dcc9fc32b7dfe14c262)
>
> To learn more about the automatic process for stewardship which archived this repository see:
>
> [Automatic process for stewardship](https://www.notion.so/pleo/Automatic-process-for-stewardship-43d9def9bc9a4010aba27144ef31e0f2)

## Antaeus

Antaeus (/ænˈtiːəs/), in Greek mythology, a giant of Libya, the son of the sea god Poseidon and the Earth goddess Gaia. He compelled all strangers who were passing through the country to wrestle with him. Whenever Antaeus touched the Earth (his mother), his strength was renewed, so that even if thrown to the ground, he was invincible. Heracles, in combat with him, discovered the source of his strength and, lifting him up from Earth, crushed him to death.

Welcome to our challenge.

## The challenge

As most "Software as a Service" (SaaS) companies, Pleo needs to charge a subscription fee every month. Our database contains a few invoices for the different markets in which we operate. Your task is to build the logic that will schedule payment of those invoices on the first of the month. While this may seem simple, there is space for some decisions to be taken and you will be expected to justify them.

## Instructions

Fork this repo with your solution. Ideally, we'd like to see your progression through commits, and don't forget to update the README.md to explain your thought process.

Please let us know how long the challenge takes you. We're not looking for how speedy or lengthy you are. It's just really to give us a clearer idea of what you've produced in the time you decided to take. Feel free to go as big or as small as you want.

## Developing

Requirements:
- \>= Java 11 environment

Open the project using your favorite text editor. If you are using IntelliJ, you can open the `build.gradle.kts` file and it is gonna setup the project in the IDE for you.

### Building

```
./gradlew build
```

### Running

There are 2 options for running Anteus. You either need libsqlite3 or docker. Docker is easier but requires some docker knowledge. We do recommend docker though.

*Running Natively*

Native java with sqlite (requires libsqlite3):

If you use homebrew on MacOS `brew install sqlite`.

```
./gradlew run
```

*Running through docker*

Install docker for your platform

```
docker build -t antaeus
docker run antaeus
```

### App Structure
The code given is structured as follows. Feel free however to modify the structure to fit your needs.
```
├── buildSrc
|  | gradle build scripts and project wide dependency declarations
|  └ src/main/kotlin/utils.kt 
|      Dependencies
|
├── pleo-antaeus-app
|       main() & initialization
|
├── pleo-antaeus-core
|       This is probably where you will introduce most of your new code.
|       Pay attention to the PaymentProvider and BillingService class.
|
├── pleo-antaeus-data
|       Module interfacing with the database. Contains the database 
|       models, mappings and access layer.
|
├── pleo-antaeus-models
|       Definition of the Internal and API models used throughout the
|       application.
|
└── pleo-antaeus-rest
        Entry point for HTTP REST API. This is where the routes are defined.
```

### Main Libraries and dependencies
* [Exposed](https://github.com/JetBrains/Exposed) - DSL for type-safe SQL
* [Javalin](https://javalin.io/) - Simple web framework (for REST)
* [kotlin-logging](https://github.com/MicroUtils/kotlin-logging) - Simple logging framework for Kotlin
* [JUnit 5](https://junit.org/junit5/) - Testing framework
* [Mockk](https://mockk.io/) - Mocking library
* [Sqlite3](https://sqlite.org/index.html) - Database storage engine

Happy hacking 😁!

## Challenge Documentation

### Modified files / Additions:

> InvoiceService

- Added fetch that will be used to get pending invoices and set them as paid (AntaeusDal)

> AntaeusDal

- Added function setInvoiceAsPaid() that updates invoices in db as paid
- Added fetchUnpaidInvoices() which select all unpaid invoices from db

> BillingService

- Added monthlyBilling() function which iterates over unpaid invoices and charge them
- Added simpleBilling() function used if required to charge only one specific invoice from a customer (AntaeusRest)

> AntaeusRest

- Added endpoint all pending invoices in /rest/v1/pending
- Added endpoint to charge a pending invoice by id in /rest/v1/pending/charge/{:id}

> InvoiceScheduler

- Added scheduler that calls BillingService the 1st of each month (coroutine), checks which invoices are pending and charges them
- Library used : **Krontab** (https://github.com/InsanusMokrassar/krontab)

> MailerService

- Added mailer service which notifies whenever an invoices has been charged
- Library used : **SimpleKotlinMail** (https://jakobkmar.github.io/SimpleKotlinMail/)

### Commentary / Thoughts:

This was my first time using Kotlin but really enjoyed it. I've been using Java on my current job and looked very similar so that was a plus when working with it.

Process looked simple at first sight, but turned out to be not so easy as you needed to take care of some things during
the implementation of the solution, at least if you want to do it correctly and keep it both cleaner and not too complicated.

While I was thinking of different solutions I did get a little overwhelmed as the amount of things you can add even if it is working as intended.
To avoid starting the house from the roof (as we say in Spain) and make it more complicated than it was, I decided to stick to the main functionality,
and then, if needed, I could always implement or refactor things to be more precise as I feel closer to finish it.

My approach was the following: 

First I did some recon over the application, checking what I had and what not, and where to implement things to keep it neat and tidy.
Then, started creating the necessary functions inside **BillingService**, **AntaeusDal** and **InvoiceService** that will update the status of an invoice from **PENDING** to **PAID**.
Once that was finished, I added endpoints to test billing invoices and to make sure I was able to display all pending invoices.

When that looked fine to me, I started to figure out how to implement the scheduler to call my function **monthlyBilling()** the 1st of each month, as it requires the challenge.

Finding a library was the main purpose, since I wanted the implementation to be less verbose and simpler, but because I am new
to Kotlin I needed to do some research, **Krontab** and **Kjob** were the first two to pop up when searching for "Kotlin scheduler library". 

Tried to implement **Krontab** (https://github.com/InsanusMokrassar/krontab) with no luck when importing the necessary dependencies,
so moved onto **Kjob** (https://github.com/justwrote/kjob) which I didn't like how it worked and tried to figured out how to fix the errors with **Krontab**.
Turned out that I needed to import the dependencies to the coreLibs. As a downside I'd say that the newer version (0.10.0) wasn't compatible with the gradle version of this project,
so I had to use 0.5.0 (0.6.0 - 0.10.0) was a no-go. 

After all the troubleshooting everything was working fine and pretty much as intended ! 😃

Next thing I wanted to do was either handle exception/tests or make an improvement to make it more realistic in terms of a real application
Decided to go for it and made it using another external library called **SimpleKotlinMail** (https://jakobkmar.github.io/SimpleKotlinMail/) which was fine for the purpose I intended to.
Manage to make it work flawless using mostly the default setup and also was able to see it real-time thanks to a free SMTP server I found https://www.wpoven.com/tools/free-smtp-server-for-testing 

Due to lack of time and that I took many weeks to finish it because of personal reasons (surgery) that I talked with Clemence, I decided to stop here and leave the currency and exceptions handling.
The hole process took around 12-16h between a few time I had during Saturdays, I wish I could've finished it the first week it was assigned to me but the timing was very bad due to the surgery appointments.

It was a really fun experience and totally different POV of programming since I've never worked with fintech technology which I'd love to! 😊

### Other notes:

Decided not to change the architecture of the application or move things to separate files because it will make it chaotic being the small as it is.
It will be nice to do it though if the application was expected to grow.

### TODO:

- ✅ Choose a library for the scheduler
- ✅ Recurring task / coroutine
- ✅ REST modification to test 1 billing
- ✅ Mocking an email notification service when charged (library?)
- ✅ Finish writing thought process and commentary
- ⬛ Currency handling?
- ⬛ Exception handling / Unit tests
