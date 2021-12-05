# Bank App Backend

![Build & Test](https://github.com/Sysmod-2021/bankappbackend/actions/workflows/gradle.yml/badge.svg)

Backend for banking application - sysmod 2021

The product spec, containing the narrative, functional and non functional requirements, scenarios and use cases are described in the following document:

[OPEN DEMO BANK PRODUCT SPEC](https://docs.google.com/document/d/1AjevAXSdgcHC6yfwMc1QbsvFRuz3pfRowHIOu4XMXEU/edit?usp=sharing)


Currently working on backend:

- God's kenny (kehinde ogundeyi)
- Zhaosi Qu
- Jonas Berx
- Iryna Halenok
- Ihar Suvorau
- Worraanong (jtm)
- Ramil Huseynov

## Event storming

The link to the board with the event storming and user stories: https://miro.com/app/board/o9J_lk2Oh94=/


### Administrator console

#### Login

```bash
# template
$ curl -d '{"email": <email_value>, "password": <password_value>}' -H 'Content-Type: application/json' -X POST -c session.txt http://localhost:40080/authenticate

# example
$ curl -d '{"email": "a@min.ee", "password": "t"}' -H 'Content-Type: application/json' -X POST -c session.txt http://localhost:40080/authenticate
```

#### Create an account

```bash
# template
$ curl -d '{"firstName": <first_name_value>, "lastName": <last_name_value>, "email": <email_value>, "password": <password_value>, "amount": <amount_value>, "currency": <currency_value>}' -H 'Content-Type: application/json' -X POST -c session.txt -b session.txt http://localhost:40080/administrators/accounts/create

# example
$ curl -d '{"firstName": "John", "lastName": "Doe", "email": "johndoe@ut.ee", "password": "123", "amount": "40", "currency": "EUR"}' -H 'Content-Type: application/json' -X POST -c session.txt -b session.txt http://localhost:40080/administrators/accounts/create
```

#### Activate an account
```bash
# template
$ curl -d '{"accountId": <account_id_value>}' -H 'Content-Type: application/json' -X POST -c session.txt -b session.txt http://localhost:40080/administrators/accounts/active

# example
$ curl -d '{"accountId": "8a633aa5-907c-4e57-a313-22e0adb2672e"}' -H 'Content-Type: application/json' -X POST -c session.txt -b session.txt http://localhost:40080/administrators/accounts/active
```

#### Froze an account
```bash
# template
$ curl -d '{"accountId": <account_id_value>}' -H 'Content-Type: application/json' -X POST -c session.txt -b session.txt http://localhost:40080/administrators/accounts/frozen

# example
$ curl -d '{"accountId": "8a633aa5-907c-4e57-a313-22e0adb2672e"}' -H 'Content-Type: application/json' -X POST -c session.txt -b session.txt http://localhost:40080/administrators/accounts/frozen
```

#### Create the transaction 
```bash
# template
$ curl -d '{"senderAccountId": <sender_account_id_value>, "receiverAccountId": <receiver_account_id_value>, "amount": <amount_value>, "description": <description_value>}' -H 'Content-Type: application/json' -X POST -c session.txt -b session.txt http://localhost:40080/administrators/transactions/create

# example
$ curl -d '{"senderAccountId": "7ec351d6-652f-4691-8e72-ea432d44a0c8", "receiverAccountId": "8a633aa5-907c-4e57-a313-22e0adb2672e", "amount": "20", "description": "Test 20"}' -H 'Content-Type: application/json' -X POST -c session.txt -b session.txt http://localhost:40080/administrators/transactions/create
```

#### Create the seed transaction
```bash
# template
$ curl -d '{"receiverAccountId": <receiver_account_id_value>, "amount": <amount_value>, "description": <description_value>, , "currency": <currency_value>}' -H 'Content-Type: application/json' -X POST -c session.txt -b session.txt http://localhost:40080/administrators/transactions/seed

# example
$ curl -d '{"receiverAccountId": "8a633aa5-907c-4e57-a313-22e0adb2672e", "amount": "50", "description": "Seed Test 50", "currency": "EUR"}' -H 'Content-Type: application/json' -X POST -c session.txt -b session.txt http://localhost:40080/administrators/transactions/seed
```

#### Revoke the transaction

```bash
# template
$ curl -d '{"reason": "Test"}' -H 'Content-Type: application/json' -X PUT -c session.txt -b session.txt http://localhost:40080/administrators/transactions/<your_transactionId>/revocation

# example
$ curl -d '{"reason": "Test"}' -H 'Content-Type: application/json' -X PUT -c session.txt -b session.txt http://localhost:40080/administrators/transactions/8505bc03-81b8-47e4-8867-c2dce0fdb4e1/revocation
```

#### Save the bank state

```bash
# example
$ curl -X POST -c session.txt -b session.txt http://localhost:40080/administrators/bank/save
```
