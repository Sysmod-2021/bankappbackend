# Open Bank

![Build & Test](https://github.com/Sysmod-2021/bankappbackend/actions/workflows/gradle.yml/badge.svg)

## Introduction

The Open Bank software project was proposed during the Systems Modeling class in 2021 at University of Tartu. Dozens of students teamed up to develop a simple open source banking simulation system.

The end-goal for the students was to apply system modeling practices that were introduced during the autumn semester to software development while working in large groups of up to 14 team members and following the SCRUM methodology.

Open Bank must contain the following features to be considered MVP:

- Account creation (by administrators)
- Transaction creation (by administrators)
- Transaction modification (by administrators)
- Seed transaction creation (by administrators)
- Transactions between customers (by customers)
- Mobile (Web) transfers (by customers)
- Data persistence
- Authentication, authorization (customers, administrators)
- Console endpoint for administrators
- Web client for customers
- Mobile client for fast p2p transactions
- A distinguishing feature (voice recognition for transactions)

After discussing the implementation with our team we concluded that the following features are nice to haves and can be considered part of the MVP:

- Account overview (balance check)
- Transaction history
- Transaction details
- Transaction restriction (by administrator)
- Account status (customer)

## Project Management Setup

The software development and most of the project management have been done using GitHub at [https://github.com/Sysmod-2021](https://github.com/Sysmod-2021). The organization has two repositories:
 
- [bankappbackend](https://github.com/Sysmod-2021/bankappbackend) for the back-end system (the current repository)
- [bankappfrontend](https://github.com/Sysmod-2021/bankappfrontend) for the front-end

The back-end repository was chosen as the primary one, so most issues are there. The overall GitHub project is located at https://github.com/Sysmod-2021/bankappbackend/projects/1.

Architecture modeling tasks have been done in multiple revisions documented at corresponding GitHub issues.
CI has been set up and [automated testing](https://github.com/Sysmod-2021/bankappbackend/actions) is triggered on each push or pull request to the main branch.

## Team members

- Armin Remenyi
- Joanna Mae Cabuyadao
- Worraanong (jtm)
- Yusuf (thatdamiguy)
- Zhaosi Qu
- Iryna Halenok
- Ramil Huseynov
- God's kenny (kehinde ogundeyi)
- Chioma Jessica Nkem-Eze
- Monika Shrestha
- Jonas Berx
- Ihar Suvorau
- Tahira Iqbal

## Team-wide role distribution

| Role                 | First Name                                                                                                  |
|----------------------|-------------------------------------------------------------------------------------------------------------|
| Product owner        | Armin Remenyi                                                                                               |
| SCRUM master         | Jonas Berx                                                                                                  |
| Architecture owner   | Ihar Suvorau                                                                                                |
| Front-end developers | thatdamiguy, Monika Shrestha, Joanna Mae Cabuyadao, Chioma Jessica Nkem-Eze, Armin Remenyi                  |
| Back-end developers  | Zhaosi Qu, Jonas Berx, Iryna Halenok, Ihar Suvorau, Worraanong (jtm), Ramil Huseynov, Joanna Mae Cabuyadao  |
| Documentation        | Jonas Berx, Iryna Halenok, Ihar Suvorau, Joanna Mae Cabuyadao, God's kenny (kehinde ogundeyi), Tahira Iqbal |

## Deliverables

- [ ] Demonstration and development video
- [x] [Architecture report](https://docs.google.com/document/d/15j40WWla26fPynEhog2gMh3iJXjurKTBTgRmgNp90to/edit?usp=sharing)
- [ ] Working open code repository
- [x] Personal work logs (see personal portfolio)

## Usage manual

### Backend

#### Setup

GitHub Actions’ (CI system) [workflow](https://github.com/Sysmod-2021/bankappbackend/blob/main/.github/workflows/gradle.yml) is a scripted way to set up the back-end application. We use **Java v15** as a development environment, **Gradle** as a building tool and few dependencies which are specified in the [Gradle script](https://github.com/Sysmod-2021/bankappbackend/blob/main/build.gradle). To set up the system on Ubuntu or a *nix-like OS, a user needs to execute the following commands in a shell:

```shell
$ git clone https://github.com/Sysmod-2021/bankappbackend.git
$ gradle build
$ gradle run
```

#### Usage

Login

```bash
# template
$ curl -d '{"email": <email_value>, "password": <password_value>}' -H 'Content-Type: application/json' -X POST -c session.txt http://localhost:40080/authenticate

# example
$ curl -d '{"email": "a@min.ee", "password": "t"}' -H 'Content-Type: application/json' -X POST -c session.txt http://localhost:40080/authenticate
```

Create an account

```bash
# template
$ curl -d '{"firstName": <first_name_value>, "lastName": <last_name_value>, "email": <email_value>, "currency": <currency_value>}' -H 'Content-Type: application/json' -X POST -c session.txt -b session.txt http://localhost:40080/administrators/accounts/create

# example
$ curl -d '{"firstName": "John", "lastName": "Doe", "email": "johndoe@ut.ee", "currency": "EUR"}' -H 'Content-Type: application/json' -X POST -c session.txt -b session.txt http://localhost:40080/administrators/accounts/create
```

Activate an account
```bash
# template
$ curl -d '{"accountId": <account_id_value>}' -H 'Content-Type: application/json' -X POST -c session.txt -b session.txt http://localhost:40080/administrators/accounts/active

# example
$ curl -d '{"accountId": "8a633aa5-907c-4e57-a313-22e0adb2672e"}' -H 'Content-Type: application/json' -X POST -c session.txt -b session.txt http://localhost:40080/administrators/accounts/active
```

Freeze an account
```bash
# template
$ curl -d '{"accountId": <account_id_value>}' -H 'Content-Type: application/json' -X POST -c session.txt -b session.txt http://localhost:40080/administrators/accounts/frozen

# example
$ curl -d '{"accountId": "8a633aa5-907c-4e57-a313-22e0adb2672e"}' -H 'Content-Type: application/json' -X POST -c session.txt -b session.txt http://localhost:40080/administrators/accounts/frozen
```

Create the transaction 
```bash
# template
$ curl -d '{"senderAccountId": <sender_account_id_value>, "receiverAccountId": <receiver_account_id_value>, "amount": <amount_value>, "description": <description_value>}' -H 'Content-Type: application/json' -X POST -c session.txt -b session.txt http://localhost:40080/administrators/transactions/create

# example
$ curl -d '{"senderAccountId": "7ec351d6-652f-4691-8e72-ea432d44a0c8", "receiverAccountId": "8a633aa5-907c-4e57-a313-22e0adb2672e", "amount": "20", "description": "Test 20"}' -H 'Content-Type: application/json' -X POST -c session.txt -b session.txt http://localhost:40080/administrators/transactions/create
```

Create the seed transaction
```bash
# template
$ curl -d '{"receiverAccountId": <receiver_account_id_value>, "amount": <amount_value>, "description": <description_value>, , "currency": <currency_value>}' -H 'Content-Type: application/json' -X POST -c session.txt -b session.txt http://localhost:40080/administrators/transactions/seed

# example
$ curl -d '{"receiverAccountId": "8a633aa5-907c-4e57-a313-22e0adb2672e", "amount": "50", "description": "Seed Test 50", "currency": "EUR"}' -H 'Content-Type: application/json' -X POST -c session.txt -b session.txt http://localhost:40080/administrators/transactions/seed
```

Revoke the transaction

```bash
# template
$ curl -d '{"reason": "Test"}' -H 'Content-Type: application/json' -X PUT -c session.txt -b session.txt http://localhost:40080/administrators/transactions/<your_transactionId>/revocation

# example
$ curl -d '{"reason": "Test"}' -H 'Content-Type: application/json' -X PUT -c session.txt -b session.txt http://localhost:40080/administrators/transactions/8505bc03-81b8-47e4-8867-c2dce0fdb4e1/revocation
```

Save the bank state

```bash
# example
$ curl -X POST -c session.txt -b session.txt http://localhost:40080/administrators/bank/save
```
