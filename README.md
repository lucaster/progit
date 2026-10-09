# progit

[![codecov](https://codecov.io/github/lucaster/progit/graph/badge.svg?token=UFH6DZQ5XB)](https://codecov.io/github/lucaster/progit)
[![CI](https://github.com/lucaster/progit/actions/workflows/ci.yml/badge.svg)](https://github.com/lucaster/progit/actions/workflows/ci.yml)
[![Release](https://img.shields.io/github/v/release/lucaster/progit)](https://github.com/lucaster/progit/releases)

Progetto di esempio Spring Boot usato per imparare e allenare il flusso di lavoro GitHub:
pull request, CI, template e release con changelog generato automaticamente.

L'applicazione è una console app che stampa un saluto.

## Requisiti

- Java 17+
- Maven 3.9+

## Build e test

```bash
mvn verify
```

## Esecuzione

```bash
mvn spring-boot:run
```

oppure con il jar pacchettizzato:

```bash
mvn package
java -jar target/progit-*.jar
```

Output:

```
Hello, World!
```

Per salutare un nome specifico, passalo come argomento:

```bash
java -jar target/progit-*.jar Luca
```

```
Hello, Luca!
```

Il messaggio è configurabile in `src/main/resources/application.properties`:

```properties
progit.greeting=Ciao, mondo!
```

## Come funziona questo repo

Questo repository segue un flusso di lavoro basato su pull request:

1. **Mai commit diretti su `main`.** Ogni modifica parte da un branch (`feat/...`, `fix/...`, `docs/...`).
2. Si apre una **pull request** verso `main`; il template della PR ricorda contesto e verifiche fatte.
3. La **CI** (`.github/workflows/ci.yml`) esegue `mvn verify` su ogni PR: il merge è permesso solo se è verde.
4. Il merge su `main` aggiorna `main`.
5. **Release**: `git tag vX.Y.Z` + `gh release create vX.Y.Z --generate-notes`; GitHub compila il changelog dai titoli delle PR mergeate.

Le release sono visibili nella [pagina Releases](https://github.com/lucaster/progit/releases).

## Licenza

Distribuito sotto licenza MIT. Vedi [LICENSE](LICENSE).
