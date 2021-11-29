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

#### Revoke the transaction

```bash
# template
$ curl -d "reason=<your_reason>" -X PUT http://localhost:40080/<your_transactionId>/revocation

# example
$ curl -d "reason=Test" -X PUT http://localhost:40080/fe452f48-3304-4063-903a-9f922b5cc372/revocation
```